package com.finki.emt.bookstore.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileUtil {

    /**
     * Read the file bytes.
     *
     * @param file  file path
     * @return      file bytes content
     */
    public static byte[] readBytes(String file) {
        ClassLoader loader = FileUtil.class.getClassLoader();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        InputStream input = loader.getResourceAsStream(file);
        try {
            while ((length = input.read(buffer)) != -1) {
                out.write(buffer, 0, length);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return out.toByteArray();
    }
}
