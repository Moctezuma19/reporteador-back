package com.jrivera.reporteador.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.sql.Timestamp;

public class Codificador {

    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

    private static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }

    public static String Hash(Integer idIncidencia, String nombre) {
        Timestamp t = new Timestamp(System.currentTimeMillis());
        String preHash = idIncidencia.toString() + nombre + t.toString();
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] res = messageDigest.digest(preHash.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(res);
        } catch (Exception e) {
            return null;
        }
    }
}
