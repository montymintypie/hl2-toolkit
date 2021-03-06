package com.timepath.hl2.io.studiomodel;

import com.timepath.DataUtils;
import com.timepath.io.ByteBufferInputStream;
import com.timepath.io.OrderedInputStream;
import com.timepath.io.struct.StructField;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

class VVD {

    private static final Logger LOG       = Logger.getLogger(VVD.class.getName());
    private static final Level  VERBOSITY = Level.FINE;
    final ByteBuffer vertexBuffer, normalBuffer, tangentBuffer;
    final         FloatBuffer        uvBuffer;
    private final OrderedInputStream is;

    private VVD(InputStream in) throws IOException, InstantiationException, IllegalAccessException {
        is = new OrderedInputStream(in);
        is.mark(Integer.MAX_VALUE);
        is.order(ByteOrder.LITTLE_ENDIAN);
        VertexFileHeader header = is.readStruct(new VertexFileHeader());
        LOG.log(VERBOSITY, "VertexFileHeader header = {0}", header.toString());
        int lod = 0;
        int vertCount = header.numLODVertexes[lod];
        position(header.vertexDataStart);
        vertexBuffer = ByteBuffer.allocateDirect(vertCount * 3 * 4).order(ByteOrder.LITTLE_ENDIAN);
        normalBuffer = ByteBuffer.allocateDirect(vertCount * 4 * 4).order(ByteOrder.LITTLE_ENDIAN);
        uvBuffer = ByteBuffer.allocateDirect(vertCount * 2 * 4).order(ByteOrder.LITTLE_ENDIAN).asFloatBuffer();
        tangentBuffer = ByteBuffer.allocateDirect(vertCount * 4 * 4).order(ByteOrder.LITTLE_ENDIAN);
        for(int i = 0; i < Math.max(header.numFixups, 1); i++) { // at least once
            int sourceVertexID = 0;
            int numVertexes = vertCount;
            if(header.numFixups != 0) { // Fixup Table
                position(header.fixupTableStart + ( i * 12 ));
                int fixlod = is.readInt(); // used to skip culled root lod
                sourceVertexID = is.readInt(); // absolute index from start of vertex/tangent blocks
                numVertexes = is.readInt();
                if(fixlod < lod) {
                    continue;
                }
            }
            for(int j = 0; j < numVertexes; j++) {
                // Vertex table, 48 byte rows
                position(header.vertexDataStart + ( ( sourceVertexID + j ) * 48 ));
                // TODO: Bones
                byte[] boneWeightBuf = new byte[3 * 4];
                is.readFully(boneWeightBuf);
                byte[] boneIdBuf = new byte[4];
                is.readFully(boneIdBuf);
                byte[] vertBuf = new byte[3 * 4];
                is.readFully(vertBuf);
                vertexBuffer.put(vertBuf);
                byte[] normBuf = new byte[3 * 4];
                is.readFully(normBuf);
                normalBuffer.put(normBuf);
                float u = is.readFloat();
                float v = 1 - is.readFloat();
                uvBuffer.put(u).put(v);
                // Tangent table, 16 byte rows
                position(header.tangentDataStart + ( ( sourceVertexID + j ) * 16 ));
                byte[] tanBuf = new byte[4 * 4];
                is.readFully(tanBuf);
                tangentBuffer.put(tanBuf);
            }
        }
        vertexBuffer.flip();
        normalBuffer.flip();
        uvBuffer.flip();
        tangentBuffer.flip();
        LOG.log(VERBOSITY, "Underflow: {0}", new Object[] { is.available() });
    }

    private void position(int index) {
        //        LOG.log(VERBOSITY, "seeking to {0}", index);
        try {
            is.reset();
            is.skipBytes(index - is.position());
        } catch(IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
    }

    public static VVD load(File file) throws IOException {
        LOG.log(Level.INFO, "Loading VVD {0}", file);
        return load(new ByteBufferInputStream(DataUtils.mapFile(file)));
    }

    public static VVD load(InputStream in) throws IOException {
        try {
            return new VVD(new BufferedInputStream(in));
        } catch(InstantiationException | IllegalAccessException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
        return null;
    }

    static class VertexFileHeader {

        /**
         * MODEL_VERTEX_FILE_ID
         */
        @StructField(index = 0)
        int id;
        /**
         * MODEL_VERTEX_FILE_VERSION
         */
        @StructField(index = 1)
        int version;
        /**
         * same as studiohdr_t, ensures sync
         */
        @StructField(index = 2)
        int checksum;
        /**
         * num of valid lods
         */
        @StructField(index = 3)
        int numLODs;
        /**
         * num verts for desired root lod
         */
        @StructField(index = 4)
        int[] numLODVertexes = new int[StudioModel.MAX_NUM_LODS];
        /**
         * num of vertexFileFixup_t
         */
        @StructField(index = 5)
        int numFixups;
        /**
         * offset from base to fixup table
         */
        @StructField(index = 6)
        int fixupTableStart;
        /**
         * offset from base to vertex block
         */
        @StructField(index = 7)
        int vertexDataStart;
        /**
         * offset from base to tangent block
         */
        @StructField(index = 8)
        int tangentDataStart;

        VertexFileHeader() {}

        @Override
        public String toString() {
            return MessageFormat.format(
                    "\nid: {0}\nv: {1}\ncksum: {2}\nlods: {3}\nfixups: {4}\nfixoff: {5}\nvertoff: {6}\ntanoff: {7}",
                    id,
                    version,
                    checksum,
                    Arrays.toString(numLODVertexes),
                    numFixups,
                    fixupTableStart,
                    vertexDataStart,
                    tangentDataStart);
        }
    }
}
