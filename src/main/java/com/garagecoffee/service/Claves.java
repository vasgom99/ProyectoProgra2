package com.garagecoffee.service;

import java.security.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

// Guarda una huella con sal aleatoria, nunca la contraseña nueva en texto.
public final class Claves {
    private Claves() { }
    public static boolean protegida(String clave) { return clave != null && clave.startsWith("pbkdf2$"); }
    public static String proteger(char[] clave) {
        byte[] sal = new byte[16]; new SecureRandom().nextBytes(sal);
        return "pbkdf2$600000$" + Base64.getEncoder().encodeToString(sal) + "$"
                + Base64.getEncoder().encodeToString(calcular(clave, sal, 600000));
    }
    public static boolean verificar(char[] clave, String guardada) {
        if (guardada == null) return false;
        if (!protegida(guardada)) return MessageDigest.isEqual(new String(clave).getBytes(StandardCharsets.UTF_8), guardada.getBytes(StandardCharsets.UTF_8));
        try {
            String[] partes = guardada.split("\\$");
            int vueltas = Integer.parseInt(partes[1]);
            if (partes.length != 4 || vueltas < 100000 || vueltas > 2000000) return false;
            return MessageDigest.isEqual(calcular(clave, Base64.getDecoder().decode(partes[2]), vueltas), Base64.getDecoder().decode(partes[3]));
        } catch (IllegalArgumentException | IndexOutOfBoundsException e) { return false; }
    }
    private static byte[] calcular(char[] clave, byte[] sal, int vueltas) {
        PBEKeySpec spec = new PBEKeySpec(clave, sal, vueltas, 256);
        try { return SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256").generateSecret(spec).getEncoded(); }
        catch (GeneralSecurityException e) { throw new IllegalStateException("No se pudo proteger la contraseña.", e); }
        finally { spec.clearPassword(); }
    }
}
