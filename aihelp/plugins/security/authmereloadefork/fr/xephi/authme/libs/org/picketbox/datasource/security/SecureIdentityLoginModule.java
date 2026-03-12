package fr.xephi.authme.libs.org.picketbox.datasource.security;

import fr.xephi.authme.libs.org.jboss.security.PicketBoxLogger;
import fr.xephi.authme.libs.org.jboss.security.PicketBoxMessages;
import fr.xephi.authme.libs.org.jboss.security.SimplePrincipal;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.security.acl.Group;
import java.util.Map;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import javax.resource.spi.security.PasswordCredential;
import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.LoginException;

public class SecureIdentityLoginModule extends AbstractPasswordCredentialLoginModule {
   private String username;
   private String password;

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
         char[] decodedPassword = decode(this.password);
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

   private static String encode(String secret) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
      byte[] kbytes = "jaas is the way".getBytes();
      SecretKeySpec key = new SecretKeySpec(kbytes, "Blowfish");
      Cipher cipher = Cipher.getInstance("Blowfish");
      cipher.init(1, key);
      byte[] encoding = cipher.doFinal(secret.getBytes());
      BigInteger n = new BigInteger(encoding);
      return n.toString(16);
   }

   private static char[] decode(String secret) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
      byte[] kbytes = "jaas is the way".getBytes();
      SecretKeySpec key = new SecretKeySpec(kbytes, "Blowfish");
      BigInteger n = new BigInteger(secret, 16);
      byte[] encoding = n.toByteArray();
      if (encoding.length % 8 != 0) {
         int length = encoding.length;
         int newLength = (length / 8 + 1) * 8;
         int pad = newLength - length;
         byte[] old = encoding;
         encoding = new byte[newLength];

         int i;
         for(i = old.length - 1; i >= 0; --i) {
            encoding[i + pad] = old[i];
         }

         if (n.signum() == -1) {
            for(i = 0; i < newLength - length; ++i) {
               encoding[i] = -1;
            }
         }
      }

      Cipher cipher = Cipher.getInstance("Blowfish");
      cipher.init(2, key);
      byte[] decode = cipher.doFinal(encoding);
      return (new String(decode)).toCharArray();
   }

   public static void main(String[] args) throws Exception {
      String encode = encode(args[0]);
      System.out.println("Encoded password: " + encode);
   }
}
