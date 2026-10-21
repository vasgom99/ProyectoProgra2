package com.garagecoffee.service;

import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

// Las claves nuevas se guardan como texto para este proyecto académico.
// También permite validar las claves protegidas por versiones anteriores.
public final class Claves {
    private Claves() { }
    public static boolean protegida(String clave) { return clave != null && clave.startsWith("pbkdf2$"); }
    public static String guardar(char[] clave) {
        return new String(clave);
    }
    public static boolean verificar(char[] clave, String guardada) {
        if (guardada == null) return false;
        if (!protegida(guardada)) return guardada.equals(new String(clave));
        try {
            String[] partes = guardada.split("\\$");
            if (partes.length != 4) return false;
            int vueltas = Integer.parseInt(partes[1]);
            if (vueltas < 100000 || vueltas > 2000000) return false;
            byte[] sal = Base64.getDecoder().decode(partes[2]);
            byte[] esperada = Base64.getDecoder().decode(partes[3]);
            return MessageDigest.isEqual(calcular(clave, sal, vueltas), esperada);
        } catch (IllegalArgumentException e) { return false; }
    }
    private static byte[] calcular(char[] clave, byte[] sal, int vueltas) {
        PBEKeySpec spec = new PBEKeySpec(clave, sal, vueltas, 256);
        try { return SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256").generateSecret(spec).getEncoded(); }
        catch (GeneralSecurityException e) { throw new IllegalStateException("No se pudo comprobar una clave guardada anteriormente.", e); }
        finally { spec.clearPassword(); }
    }
}
