package fr.xephi.authme.libs.org.postgresql.shaded.com.ongres.scram.common;

import fr.xephi.authme.libs.org.postgresql.shaded.com.ongres.scram.common.bouncycastle.pbkdf2.DigestFactory;
import fr.xephi.authme.libs.org.postgresql.shaded.com.ongres.scram.common.bouncycastle.pbkdf2.KeyParameter;
import fr.xephi.authme.libs.org.postgresql.shaded.com.ongres.scram.common.bouncycastle.pbkdf2.PBEParametersGenerator;
import fr.xephi.authme.libs.org.postgresql.shaded.com.ongres.scram.common.bouncycastle.pbkdf2.PKCS5S2ParametersGenerator;
import fr.xephi.authme.libs.org.postgresql.shaded.com.ongres.scram.common.stringprep.StringPreparation;
import fr.xephi.authme.libs.org.postgresql.shaded.com.ongres.scram.common.util.CryptoUtil;
import fr.xephi.authme.libs.org.postgresql.shaded.com.ongres.scram.common.util.Preconditions;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.Mac;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.SecretKeySpec;

public enum ScramMechanisms implements ScramMechanism {
   SCRAM_SHA_1("SHA-1", "SHA-1", 160, "HmacSHA1", false, 1),
   SCRAM_SHA_1_PLUS("SHA-1", "SHA-1", 160, "HmacSHA1", true, 1),
   SCRAM_SHA_256("SHA-256", "SHA-256", 256, "HmacSHA256", false, 10),
   SCRAM_SHA_256_PLUS("SHA-256", "SHA-256", 256, "HmacSHA256", true, 10);

   private static final String SCRAM_MECHANISM_NAME_PREFIX = "SCRAM-";
   private static final String CHANNEL_BINDING_SUFFIX = "-PLUS";
   private static final String PBKDF2_PREFIX_ALGORITHM_NAME = "PBKDF2With";
   private static final Map<String, ScramMechanisms> BY_NAME_MAPPING = valuesAsMap();
   private final String mechanismName;
   private final String hashAlgorithmName;
   private final int keyLength;
   private final String hmacAlgorithmName;
   private final boolean channelBinding;
   private final int priority;

   private ScramMechanisms(String name, String hashAlgorithmName, int keyLength, String hmacAlgorithmName, boolean channelBinding, int priority) {
      this.mechanismName = "SCRAM-" + (String)Preconditions.checkNotNull(name, "name") + (channelBinding ? "-PLUS" : "");
      this.hashAlgorithmName = (String)Preconditions.checkNotNull(hashAlgorithmName, "hashAlgorithmName");
      this.keyLength = Preconditions.gt0(keyLength, "keyLength");
      this.hmacAlgorithmName = (String)Preconditions.checkNotNull(hmacAlgorithmName, "hmacAlgorithmName");
      this.channelBinding = channelBinding;
      this.priority = Preconditions.gt0(priority, "priority");
   }

   protected String getHashAlgorithmName() {
      return this.hashAlgorithmName;
   }

   protected String getHmacAlgorithmName() {
      return this.hmacAlgorithmName;
   }

   public String getName() {
      return this.mechanismName;
   }

   public boolean supportsChannelBinding() {
      return this.channelBinding;
   }

   public int algorithmKeyLength() {
      return this.keyLength;
   }

   public byte[] digest(byte[] message) {
      try {
         return MessageDigest.getInstance(this.hashAlgorithmName).digest(message);
      } catch (NoSuchAlgorithmException var3) {
         throw new RuntimeException("Algorithm " + this.hashAlgorithmName + " not present in current JVM");
      }
   }

   public byte[] hmac(byte[] key, byte[] message) {
      try {
         return CryptoUtil.hmac(new SecretKeySpec(key, this.hmacAlgorithmName), Mac.getInstance(this.hmacAlgorithmName), message);
      } catch (NoSuchAlgorithmException var4) {
         throw new RuntimeException("MAC Algorithm " + this.hmacAlgorithmName + " not present in current JVM");
      }
   }

   public byte[] saltedPassword(StringPreparation stringPreparation, String password, byte[] salt, int iterations) {
      char[] normalizedString = stringPreparation.normalize(password).toCharArray();

      try {
         return CryptoUtil.hi(SecretKeyFactory.getInstance("PBKDF2With" + this.hmacAlgorithmName), this.algorithmKeyLength(), normalizedString, salt, iterations);
      } catch (NoSuchAlgorithmException var9) {
         if (!SCRAM_SHA_256.getHmacAlgorithmName().equals(this.getHmacAlgorithmName())) {
            throw new RuntimeException("Unsupported PBKDF2 for " + this.mechanismName);
         } else {
            PBEParametersGenerator generator = new PKCS5S2ParametersGenerator(DigestFactory.createSHA256());
            generator.init(PBEParametersGenerator.PKCS5PasswordToUTF8Bytes(normalizedString), salt, iterations);
            KeyParameter params = (KeyParameter)generator.generateDerivedParameters(this.algorithmKeyLength());
            return params.getKey();
         }
      }
   }

   public static ScramMechanisms byName(String name) {
      Preconditions.checkNotNull(name, "name");
      return (ScramMechanisms)BY_NAME_MAPPING.get(name);
   }

   public static ScramMechanism selectMatchingMechanism(boolean channelBinding, String... peerMechanisms) {
      ScramMechanisms selectedScramMechanisms = null;
      String[] var3 = peerMechanisms;
      int var4 = peerMechanisms.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         String peerMechanism = var3[var5];
         ScramMechanisms matchedScramMechanisms = (ScramMechanisms)BY_NAME_MAPPING.get(peerMechanism);
         if (matchedScramMechanisms != null) {
            ScramMechanisms[] var8 = values();
            int var9 = var8.length;

            for(int var10 = 0; var10 < var9; ++var10) {
               ScramMechanisms candidateScramMechanisms = var8[var10];
               if (channelBinding == candidateScramMechanisms.channelBinding && candidateScramMechanisms.mechanismName.equals(matchedScramMechanisms.mechanismName) && (selectedScramMechanisms == null || selectedScramMechanisms.priority < candidateScramMechanisms.priority)) {
                  selectedScramMechanisms = candidateScramMechanisms;
               }
            }
         }
      }

      return selectedScramMechanisms;
   }

   private static Map<String, ScramMechanisms> valuesAsMap() {
      Map<String, ScramMechanisms> mapScramMechanisms = new HashMap(values().length);
      ScramMechanisms[] var1 = values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         ScramMechanisms scramMechanisms = var1[var3];
         mapScramMechanisms.put(scramMechanisms.getName(), scramMechanisms);
      }

      return mapScramMechanisms;
   }
}
