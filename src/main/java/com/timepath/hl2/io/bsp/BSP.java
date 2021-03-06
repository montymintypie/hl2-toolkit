package com.timepath.hl2.io.bsp;

import com.timepath.hl2.io.bsp.lump.LumpType;
import com.timepath.io.OrderedInputStream;
import com.timepath.io.struct.StructField;
import com.timepath.steam.io.storage.Files;
import com.timepath.steam.io.storage.Files.FileHandler;
import com.timepath.vfs.SimpleVFile;
import com.timepath.vfs.ZipFS;

import java.io.*;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Collection;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author TimePath
 * @see <a>https://developer.valvesoftware.com/wiki/Source_BSP_File_Format</a>
 * @see <a>https://github.com/TimePath/webgl-source/blob/master/js/source-bsp.js</a>
 * @see <a>https://github.com/TimePath/webgl-source/blob/master/js/source-bsp-struct.js</a>
 * @see <a>https://github.com/TimePath/webgl-source/blob/master/js/source-bsp-tree.js</a>
 */
public abstract class BSP {

    private static final Logger LOG = Logger.getLogger(BSP.class.getName());
    protected BSPHeader          header;
    protected OrderedInputStream in;
    IntBuffer   indexBuffer;
    FloatBuffer vertexBuffer;

    static {
        Files.registerHandler(new FileHandler() {
            @Override
            public Collection<? extends SimpleVFile> handle(final File file) throws IOException {
                if(!file.getName().endsWith(".bsp")) return null;
                final String name = file.getName().replace(".bsp", "");
                return Collections.singleton(new SimpleVFile() {
                    void checkBSP() {
                        if(z != null) return;
                        LOG.log(Level.INFO, "Loading {0}", file);
                        try(InputStream is = new FileInputStream(file)) {
                            BSP b = BSP.load(is);
                            z = b.getLump(LumpType.LUMP_PAKFILE);
                        } catch(IOException e) {
                            LOG.log(Level.SEVERE, null, e);
                        }
                    }

                    ZipFS z;

                    @Override
                    public boolean isDirectory() {
                        return true;
                    }

                    @Override
                    public String getName() {
                        return name;
                    }

                    @Override
                    public InputStream openStream() {
                        return null;
                    }

                    @Override
                    public Collection<? extends SimpleVFile> list() {
                        checkBSP();
                        return z.list();
                    }

                    @Override
                    public SimpleVFile get(final String name) {
                        checkBSP();
                        return z.get(name);
                    }
                });
            }
        });
    }

    BSP() {
    }

    public static BSP load(InputStream is) throws IOException {
        try {
            OrderedInputStream in = new OrderedInputStream(new BufferedInputStream(is));
            in.order(ByteOrder.LITTLE_ENDIAN);
            in.mark(in.available());
            BSPHeader header = in.readStruct(new BSPHeader());
            // TODO: Other BSP types
            VBSP bsp = new VBSP();
            bsp.in = in;
            bsp.header = header;
            // TODO: Struct parser callbacks
            for(int i = 0; i < header.lumps.length; i++) {
                header.lumps[i].type = LumpType.values()[i];
            }
            LOG.info("Processing map...");
            bsp.process();
            return bsp;
        } catch(InstantiationException | IllegalAccessException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public IntBuffer getIndices() {
        return indexBuffer;
    }

    /**
     * Examples:
     * <br/>
     * {@code String ents = b.<String>getLump(LumpType.LUMP_ENTITIES);}
     * <br/>
     * {@code String ents = (String) b.getLump(LumpType.LUMP_ENTITIES);}
     * <br/>
     * {@code String ents = b.getLump(LumpType.LUMP_ENTITIES);}
     *
     * @param <T>
     *         Expected return type. TODO: Wouldn't it be nice if we just knew at compile time?
     * @param type
     *         The lump
     *
     * @return The lump
     *
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    public <T> T getLump(LumpType type) throws IOException {
        return getLump(type, (LumpHandler<T>) type.getHandler());
    }

    /**
     * Intended for overriding to change handler functionality
     *
     * @param <T>
     * @param type
     * @param handler
     *         *
     *
     * @return *
     *
     * @throws IOException
     */
    protected <T> T getLump(LumpType type, LumpHandler<T> handler) throws IOException {
        if(handler == null) {
            return null;
        }
        Lump lump = header.lumps[type.getID()];
        if(lump.isEmpty()) {
            return null;
        }
        in.reset();
        in.skipBytes(lump.offset);
        return handler.handle(lump, in);
    }

    /**
     * @return The map revision
     */
    int getRevision() {
        return header.mapRevision;
    }

    public FloatBuffer getVertices() {
        return vertexBuffer;
    }

    protected abstract void process() throws IOException;

    private static class BSPHeader {

        /**
         * BSP file identifier: VBSP
         */
        @StructField(index = 0)
        int ident;
        /**
         * BSP file identifier: VBSP
         */
        @StructField(index = 2)
        Lump[] lumps = new Lump[64];
        /**
         * The map's revision (iteration, version) number
         */
        @StructField(index = 3)
        int mapRevision;
        /**
         * BSP file version
         */
        @StructField(index = 1)
        int version;

        private BSPHeader() {}
    }
}
