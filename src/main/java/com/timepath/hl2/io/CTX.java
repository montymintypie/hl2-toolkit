package com.timepath.hl2.io;

import com.timepath.crypto.IceKey;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Thin-ICE encrypted files. Often VDF
 *
 * @author TimePath
 */
public class CTX {

    public static final  String TF2            = "E2NcUkG2";
    public static final  String TF2_ITEMS      = "A5fSXbf7";
    public static final  String SOURCE_DEFAULT = "x9Ke0BY7";
    private static final Logger LOG            = Logger.getLogger(CTX.class.getName());

    private CTX() {}

    public static void main(String... args) {
        try {
            InputStream is = new FileInputStream(args[0]);
            String key = args[1];
            if(key == null) {
                key = TF2;
            }
            InputStream de = decrypt(key.getBytes("UTF-8"), is);
            InputStream is2 = encrypt(key.getBytes("UTF-8"), de);
            is = new FileInputStream(args[0]);
            de = decrypt(key.getBytes("UTF-8"), is);
            is = new FileInputStream(args[0]);
            int bs = 4096;
            ByteBuffer debuf = ByteBuffer.allocate(de.available());
            byte[] inde = new byte[bs];
            for(int read = 0; read != -1; read = de.read(inde, 0, bs)) {
                debuf.put(inde, 0, read);
            }
            LOG.info(new String(debuf.array(), "UTF-8"));
            ByteBuffer buf = ByteBuffer.allocate(is.available());
            byte[] in = new byte[bs];
            for(int read = 0; read != -1; read = is.read(in, 0, bs)) {
                buf.put(in, 0, read);
            }
            ByteBuffer buf2 = ByteBuffer.allocate(is2.available());
            byte[] in2 = new byte[bs];
            for(int read = 0; read != -1; read = is2.read(in2, 0, bs)) {
                buf2.put(in2, 0, read);
            }
            LOG.info("Equal = " + Arrays.equals(buf.array(), buf2.array()));
        } catch(Exception ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
    }

    private static InputStream encrypt(byte[] key, InputStream is) throws IOException {
        return new ByteArrayInputStream(method(key, is, false));
    }

    public static InputStream decrypt(byte[] key, InputStream is) throws IOException {
        return new ByteArrayInputStream(method(key, is, true));
    }

    private static byte[] method(byte[] key, InputStream is, boolean decrypt) throws IOException {
        ByteBuffer buf = ByteBuffer.allocate(is.available());
        IceKey ice = new IceKey(0);
        ice.set(key);
        int bs = 8; // ice.blockSize();
        byte[] in = new byte[bs];
        byte[] out = new byte[bs];
        int prevRead = 0;
        for(int read = 0; read != -1; read = is.read(in, 0, bs)) {
            prevRead = read;
            //            if (read != bs) {
            //                if (read == 0) {
            ////                    System.err.println("read 0 bytes?");
            //                } else {
            //                    System.err.println("read: " + read);
            //                }
            //            }
            if(decrypt) {
                ice.decrypt(in, out);
            } else {
                ice.encrypt(in, out);
            }
            buf.put(out, 0, read);
        }
        // Last block is not encrypted if there are fewer bytes in it than the blocksize
        if(prevRead < bs) {
            buf.position(buf.limit() - prevRead);
            buf.put(in, 0, prevRead);
        }
        return buf.array();
    }
}
