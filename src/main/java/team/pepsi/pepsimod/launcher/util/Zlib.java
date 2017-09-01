package team.pepsi.pepsimod.launcher.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.zip.Deflater;
import java.util.zip.InflaterInputStream;

public abstract class Zlib {

    public static byte[] deflate(byte[] data) {
        try {
            return deflate(data, Deflater.DEFAULT_COMPRESSION);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static byte[] deflate(byte[] data, int level) {
        Deflater deflater = new Deflater(level);
        deflater.reset();
        deflater.setInput(data);
        deflater.finish();
        ByteArrayOutputStream bos = new ByteArrayOutputStream(data.length);
        byte[] buf = new byte[1024];
        try {
            while (!deflater.finished()) {
                int i = deflater.deflate(buf);
                bos.write(buf, 0, i);
            }
        } finally {
            deflater.end();
        }
        return bos.toByteArray();
    }

    public static byte[] inflate(InputStream stream) {
        try {
            InflaterInputStream inputStream = new InflaterInputStream(stream);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length;

            while ((length = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, length);
            }

            buffer = outputStream.toByteArray();
            outputStream.flush();
            outputStream.close();
            inputStream.close();

            return buffer;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static byte[] inflate(byte[] data) {
        return inflate(new ByteArrayInputStream(data));
    }

    public static byte[] inflate(byte[] data, int maxSize) {
        return inflate(new ByteArrayInputStream(data, 0, maxSize));
    }

}
