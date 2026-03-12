package fr.xephi.authme.libs.org.postgresql.shaded.com.ongres.scram.common;

import fr.xephi.authme.libs.org.postgresql.shaded.com.ongres.scram.common.stringprep.StringPreparation;
import fr.xephi.authme.libs.org.postgresql.shaded.com.ongres.scram.common.util.CryptoUtil;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class ScramFunctions {
   private static final byte[] CLIENT_KEY_HMAC_KEY;
   private static final byte[] SERVER_KEY_HMAC_KEY;

   public static byte[] saltedPassword(ScramMechanism scramMechanism, StringPreparation stringPreparation, String password, byte[] salt, int iteration) {
      return scramMechanism.saltedPassword(stringPreparation, password, salt, iteration);
   }

   public static byte[] hmac(ScramMechanism scramMechanism, byte[] message, byte[] key) {
      return scramMechanism.hmac(key, message);
   }

   public static byte[] clientKey(ScramMechanism scramMechanism, byte[] saltedPassword) {
      return hmac(scramMechanism, CLIENT_KEY_HMAC_KEY, saltedPassword);
   }

   public static byte[] clientKey(ScramMechanism scramMechanism, StringPreparation stringPreparation, String password, byte[] salt, int iteration) {
      return clientKey(scramMechanism, saltedPassword(scramMechanism, stringPreparation, password, salt, iteration));
   }

   public static byte[] serverKey(ScramMechanism scramMechanism, byte[] saltedPassword) {
      return hmac(scramMechanism, SERVER_KEY_HMAC_KEY, saltedPassword);
   }

   public static byte[] serverKey(ScramMechanism scramMechanism, StringPreparation stringPreparation, String password, byte[] salt, int iteration) {
      return serverKey(scramMechanism, saltedPassword(scramMechanism, stringPreparation, password, salt, iteration));
   }

   public static byte[] hash(ScramMechanism scramMechanism, byte[] value) {
      return scramMechanism.digest(value);
   }

   public static byte[] storedKey(ScramMechanism scramMechanism, byte[] clientKey) {
      return hash(scramMechanism, clientKey);
   }

   public static byte[] clientSignature(ScramMechanism scramMechanism, byte[] storedKey, String authMessage) {
      return hmac(scramMechanism, authMessage.getBytes(StandardCharsets.UTF_8), storedKey);
   }

   public static byte[] clientProof(byte[] clientKey, byte[] clientSignature) {
      return CryptoUtil.xor(clientKey, clientSignature);
   }

   public static byte[] serverSignature(ScramMechanism scramMechanism, byte[] serverKey, String authMessage) {
      return clientSignature(scramMechanism, serverKey, authMessage);
   }

   public static boolean verifyClientProof(ScramMechanism scramMechanism, byte[] clientProof, byte[] storedKey, String authMessage) {
      byte[] clientSignature = clientSignature(scramMechanism, storedKey, authMessage);
      byte[] clientKey = CryptoUtil.xor(clientSignature, clientProof);
      byte[] computedStoredKey = hash(scramMechanism, clientKey);
      return Arrays.equals(storedKey, computedStoredKey);
   }

   public static boolean verifyServerSignature(ScramMechanism scramMechanism, byte[] serverKey, String authMessage, byte[] serverSignature) {
      return Arrays.equals(serverSignature(scramMechanism, serverKey, authMessage), serverSignature);
   }

   static {
      CLIENT_KEY_HMAC_KEY = "Client Key".getBytes(StandardCharsets.UTF_8);
      SERVER_KEY_HMAC_KEY = "Server Key".getBytes(StandardCharsets.UTF_8);
   }
}
