package fr.xephi.authme.libs.org.picketbox.datasource.security;

import fr.xephi.authme.libs.org.jboss.security.Base64Utils;
import fr.xephi.authme.libs.org.jboss.security.PicketBoxLogger;
import fr.xephi.authme.libs.org.jboss.security.PicketBoxMessages;
import fr.xephi.authme.libs.org.jboss.security.SimplePrincipal;
import fr.xephi.authme.libs.org.jboss.security.Util;
import java.security.Principal;
import java.security.acl.Group;
import java.util.Map;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.resource.spi.security.PasswordCredential;
import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.LoginException;

public class PBEIdentityLoginModule extends AbstractPasswordCredentialLoginModule {
   private String username;
   private String password;
   private char[] pbepass = "jaas is the way".toCharArray();
   private String pbealgo = "PBEwithMD5andDES";
   private byte[] salt = new byte[]{1, 7, 2, 9, 3, 11, 4, 13};
   private int iterationCount = 37;
   private PBEParameterSpec cipherSpec;

   public PBEIdentityLoginModule() {
   }

   PBEIdentityLoginModule(String algo, char[] pass, byte[] pbesalt, int iter) {
      if (pass != null) {
         this.pbepass = pass;
      }

      if (algo != null) {
         this.pbealgo = algo;
      }

      if (pbesalt != null) {
         this.salt = pbesalt;
      }

      if (iter > 0) {
         this.iterationCount = iter;
      }

   }

   public void initialize(Subject subject, CallbackHandler handler, Map<String, ?> sharedState, Map<String, ?> options) {
      super.initialize(subject, handler, sharedState, options);
      this.username = (String)options.get("username");
      if (this.username == null) {
         this.username = (String)options.get("userName");
         if (this.username == null) {
            throw new IllegalArgumentException(PicketBoxMessages.MESSAGES.missingRequiredModuleOptionMessage("username"));
         }
      }

      this.password = (String)options.get("password");
      if (this.password == null) {
         throw new IllegalArgumentException(PicketBoxMessages.MESSAGES.missingRequiredModuleOptionMessage("password"));
      } else {
         String tmp = (String)options.get("pbepass");
         if (tmp != null) {
            try {
               this.pbepass = Util.loadPassword(tmp);
            } catch (Exception var7) {
               throw new IllegalStateException(var7);
            }
         }

         tmp = (String)options.get("pbealgo");
         if (tmp != null) {
            this.pbealgo = tmp;
         }

         tmp = (String)options.get("salt");
         if (tmp != null) {
            this.salt = tmp.substring(0, 8).getBytes();
         }

         tmp = (String)options.get("iterationCount");
         if (tmp != null) {
            this.iterationCount = Integer.parseInt(tmp);
         }

      }
   }

   public boolean login() throws LoginException {
      PicketBoxLogger.LOGGER.traceBeginLogin();
      if (super.login()) {
         return true;
      } else {
         super.loginOk = true;
         return true;
      }
   }

   public boolean commit() throws LoginException {
      Principal principal = new SimplePrincipal(this.username);
      SubjectActions.addPrincipals(this.subject, principal);
      this.sharedState.put("javax.security.auth.login.name", this.username);

      try {
         char[] decodedPassword = this.decode(this.password);
         PasswordCredential cred = new PasswordCredential(this.username, decodedPassword);
         SubjectActions.addCredentials(this.subject, cred);
         return true;
      } catch (Exception var4) {
         LoginException le = new LoginException(var4.getLocalizedMessage());
         le.initCause(var4);
         throw le;
      }
   }

   public boolean abort() {
      this.username = null;
      this.password = null;
      return true;
   }

   protected Principal getIdentity() {
      PicketBoxLogger.LOGGER.traceBeginGetIdentity(this.username);
      Principal principal = new SimplePrincipal(this.username);
      return principal;
   }

   protected Group[] getRoleSets() throws LoginException {
      return new Group[0];
   }

   private String encode(String secret) throws Exception {
      this.cipherSpec = new PBEParameterSpec(this.salt, this.iterationCount);
      PBEKeySpec keySpec = new PBEKeySpec(this.pbepass);
      SecretKeyFactory factory = SecretKeyFactory.getInstance(this.pbealgo);
      SecretKey cipherKey = factory.generateSecret(keySpec);
      Cipher cipher = Cipher.getInstance(this.pbealgo);
      cipher.init(1, cipherKey, this.cipherSpec);
      byte[] encoding = cipher.doFinal(secret.getBytes());
      return Base64Utils.tob64(encoding);
   }

   private char[] decode(String secret) throws Exception {
      this.cipherSpec = new PBEParameterSpec(this.salt, this.iterationCount);
      PBEKeySpec keySpec = new PBEKeySpec(this.pbepass);
      SecretKeyFactory factory = SecretKeyFactory.getInstance(this.pbealgo);
      SecretKey cipherKey = factory.generateSecret(keySpec);

      byte[] encoding;
      try {
         encoding = Base64Utils.fromb64(secret);
      } catch (IllegalArgumentException var8) {
         encoding = Base64Utils.fromb64("0" + secret);
         PicketBoxLogger.LOGGER.wrongBase64StringUsed("0" + secret);
      }

      Cipher cipher = Cipher.getInstance(this.pbealgo);
      cipher.init(2, cipherKey, this.cipherSpec);
      byte[] decode = cipher.doFinal(encoding);
      return (new String(decode)).toCharArray();
   }

   public static void main(String[] args) throws Exception {
      String algo = null;
      char[] pass = "jaas is the way".toCharArray();
      byte[] salt = null;
      int iter = -1;
      if (args.length >= 2) {
         pass = args[1].toCharArray();
      }

      if (args.length >= 3) {
         salt = args[2].getBytes();
      }

      if (args.length >= 4) {
         iter = Integer.decode(args[3]);
      }

      if (args.length >= 5) {
         algo = args[4];
      }

      PBEIdentityLoginModule pbe = new PBEIdentityLoginModule(algo, pass, salt, iter);
      String encode = pbe.encode(args[0]);
      System.out.println("Encoded password: " + encode);
   }
}
