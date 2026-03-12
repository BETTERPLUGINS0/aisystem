package fr.xephi.authme.libs.org.jboss.crypto;

import java.security.Provider;

public class JBossSXProvider extends Provider {
   private static final long serialVersionUID = -2338131128387727845L;
   public static final String PROVIDER_NAME = "JBossSX";
   public static final String PROVIDER_INFO = "JBossSX Provier Version 1.0";
   public static final double PROVIDER_VERSION = 1.0D;

   public JBossSXProvider() {
      this("JBossSX", 1.0D, "JBossSX Provier Version 1.0");
   }

   protected JBossSXProvider(String name, double version, String info) {
      super(name, version, info);
      super.put("MessageDigest.SHA_Interleave", "fr.xephi.authme.libs.org.jboss.crypto.digest.SHAInterleave");
      super.put("Alg.Alias.MessageDigest.SHA-Interleave", "SHA_Interleave");
      super.put("Alg.Alias.MessageDigest.SHA-SRP", "SHA_Interleave");
      super.put("MessageDigest.SHA_ReverseInterleave", "fr.xephi.authme.libs.org.jboss.crypto.digest.SHAReverseInterleave");
      super.put("Alg.Alias.MessageDigest.SHA-SRP-Reverse", "SHA_ReverseInterleave");
   }
}
