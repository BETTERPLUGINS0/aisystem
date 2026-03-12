package fr.xephi.authme.libs.org.jboss.security.auth.spi;

import fr.xephi.authme.libs.org.jboss.crypto.digest.DigestCallback;
import fr.xephi.authme.libs.org.jboss.security.PicketBoxLogger;
import fr.xephi.authme.libs.org.jboss.security.PicketBoxMessages;
import fr.xephi.authme.libs.org.jboss.security.vault.SecurityVaultException;
import fr.xephi.authme.libs.org.jboss.security.vault.SecurityVaultUtil;
import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.FailedLoginException;
import javax.security.auth.login.LoginException;

public abstract class UsernamePasswordLoginModule extends AbstractServerLoginModule {
   private static final String HASH_ALGORITHM = "hashAlgorithm";
   private static final String HASH_ENCODING = "hashEncoding";
   private static final String HASH_CHARSET = "hashCharset";
   private static final String HASH_STORE_PASSWORD = "hashStorePassword";
   private static final String HASH_USER_PASSWORD = "hashUserPassword";
   private static final String DIGEST_CALLBACK = "digestCallback";
   private static final String STORE_DIGEST_CALLBACK = "storeDigestCallback";
   private static final String IGNORE_PASSWORD_CASE = "ignorePasswordCase";
   private static final String LEGACY_CREATE_PASSWORD_HASH = "legacyCreatePasswordHash";
   private static final String THROW_VALIDATE_ERROR = "throwValidateError";
   private static final String INPUT_VALIDATOR = "inputValidator";
   private static final String PASS_IS_A1_HASH = "passwordIsA1Hash";
   private static final String[] ALL_VALID_OPTIONS = new String[]{"hashAlgorithm", "hashEncoding", "hashCharset", "hashStorePassword", "hashUserPassword", "digestCallback", "storeDigestCallback", "ignorePasswordCase", "legacyCreatePasswordHash", "throwValidateError", "inputValidator", "passwordIsA1Hash"};
   private Principal identity;
   private char[] credential;
   private String hashAlgorithm = null;
   private String hashCharset = null;
   private String hashEncoding = null;
   private boolean ignorePasswordCase;
   private boolean hashStorePassword;
   private boolean hashUserPassword = true;
   private boolean legacyCreatePasswordHash;
   private boolean throwValidateError = false;
   private Throwable validateError;
   private InputValidator inputValidator = null;

   public void initialize(Subject subject, CallbackHandler callbackHandler, Map<String, ?> sharedState, Map<String, ?> options) {
      this.addValidOptions(ALL_VALID_OPTIONS);
      super.initialize(subject, callbackHandler, sharedState, options);
      this.hashAlgorithm = (String)options.get("hashAlgorithm");
      if (this.hashAlgorithm != null) {
         this.hashEncoding = (String)options.get("hashEncoding");
         if (this.hashEncoding == null) {
            this.hashEncoding = "BASE64";
         }

         this.hashCharset = (String)options.get("hashCharset");
         PicketBoxLogger.LOGGER.debugPasswordHashing(this.hashAlgorithm, this.hashEncoding, this.hashCharset, (String)options.get("digestCallback"), (String)options.get("storeDigestCallback"));
      }

      String flag = (String)options.get("ignorePasswordCase");
      this.ignorePasswordCase = Boolean.valueOf(flag);
      flag = (String)options.get("hashStorePassword");
      this.hashStorePassword = Boolean.valueOf(flag);
      flag = (String)options.get("hashUserPassword");
      if (flag != null) {
         this.hashUserPassword = Boolean.valueOf(flag);
      }

      flag = (String)options.get("legacyCreatePasswordHash");
      if (flag != null) {
         this.legacyCreatePasswordHash = Boolean.valueOf(flag);
      }

      flag = (String)options.get("throwValidateError");
      if (flag != null) {
         this.throwValidateError = Boolean.valueOf(flag);
      }

      flag = (String)options.get("inputValidator");
      if (flag != null) {
         try {
            Class<?> validatorClass = SecurityActions.loadClass(flag);
            this.inputValidator = (InputValidator)validatorClass.newInstance();
         } catch (Exception var7) {
            PicketBoxLogger.LOGGER.debugFailureToInstantiateClass(flag, var7);
         }
      }

   }

   public boolean login() throws LoginException {
      String username;
      String password;
      if (super.login()) {
         Object username = this.sharedState.get("javax.security.auth.login.name");
         if (username instanceof Principal) {
            this.identity = (Principal)username;
         } else {
            username = username.toString();

            try {
               this.identity = this.createIdentity(username);
            } catch (Exception var7) {
               LoginException le = PicketBoxMessages.MESSAGES.failedToCreatePrincipal(var7.getLocalizedMessage());
               le.initCause(var7);
               throw le;
            }
         }

         Object password = this.sharedState.get("javax.security.auth.login.password");
         if (password instanceof char[]) {
            this.credential = (char[])((char[])password);
         } else if (password != null) {
            password = password.toString();
            this.credential = password.toCharArray();
         }

         return true;
      } else {
         super.loginOk = false;
         String[] info = this.getUsernameAndPassword();
         username = info[0];
         password = info[1];
         if (this.inputValidator != null) {
            try {
               this.inputValidator.validateUsernameAndPassword(username, password);
            } catch (InputValidationException var10) {
               throw new FailedLoginException(var10.getLocalizedMessage());
            }
         }

         if (username == null && password == null) {
            this.identity = this.unauthenticatedIdentity;
            PicketBoxLogger.LOGGER.traceUsingUnauthIdentity(this.identity != null ? this.identity.getName() : null);
         }

         if (this.identity == null) {
            try {
               this.identity = this.createIdentity(username);
            } catch (Exception var9) {
               LoginException le = PicketBoxMessages.MESSAGES.failedToCreatePrincipal(var9.getLocalizedMessage());
               le.initCause(var9);
               throw le;
            }

            if (this.hashAlgorithm != null && this.hashUserPassword) {
               password = this.createPasswordHash(username, password, "digestCallback");
            }

            String expectedPassword = this.getUsersPassword();
            if (SecurityVaultUtil.isVaultFormat(expectedPassword)) {
               try {
                  expectedPassword = SecurityVaultUtil.getValueAsString(expectedPassword);
               } catch (SecurityVaultException var8) {
                  LoginException le = PicketBoxMessages.MESSAGES.unableToGetPasswordFromVault();
                  le.initCause(var8);
                  throw le;
               }
            }

            if (this.hashAlgorithm != null && this.hashStorePassword) {
               expectedPassword = this.createPasswordHash(username, expectedPassword, "storeDigestCallback");
            }

            if (!this.validatePassword(password, expectedPassword)) {
               Throwable ex = this.getValidateError();
               FailedLoginException fle = PicketBoxMessages.MESSAGES.invalidPassword();
               PicketBoxLogger.LOGGER.debugBadPasswordForUsername(username);
               if (ex != null && this.throwValidateError) {
                  fle.initCause(ex);
               }

               throw fle;
            }
         }

         if (this.getUseFirstPass()) {
            this.sharedState.put("javax.security.auth.login.name", this.identity);
            this.sharedState.put("javax.security.auth.login.password", this.credential);
         }

         super.loginOk = true;
         PicketBoxLogger.LOGGER.traceEndLogin(super.loginOk);
         return true;
      }
   }

   protected Principal getIdentity() {
      return this.identity;
   }

   protected Principal getUnauthenticatedIdentity() {
      return this.unauthenticatedIdentity;
   }

   protected Object getCredentials() {
      return this.credential;
   }

   protected String getUsername() {
      String username = null;
      if (this.getIdentity() != null) {
         username = this.getIdentity().getName();
      }

      return username;
   }

   protected String[] getUsernameAndPassword() throws LoginException {
      String[] info = new String[]{null, null};
      if (this.callbackHandler == null) {
         throw PicketBoxMessages.MESSAGES.noCallbackHandlerAvailable();
      } else {
         NameCallback nc = new NameCallback(PicketBoxMessages.MESSAGES.enterUsernameMessage(), "guest");
         PasswordCallback pc = new PasswordCallback(PicketBoxMessages.MESSAGES.enterPasswordMessage(), false);
         Callback[] callbacks = new Callback[]{nc, pc};
         String username = null;
         String password = null;

         LoginException le;
         try {
            this.callbackHandler.handle(callbacks);
            username = nc.getName();
            char[] tmpPassword = pc.getPassword();
            if (tmpPassword != null) {
               this.credential = new char[tmpPassword.length];
               System.arraycopy(tmpPassword, 0, this.credential, 0, tmpPassword.length);
               pc.clearPassword();
               password = new String(this.credential);
            }
         } catch (IOException var9) {
            le = PicketBoxMessages.MESSAGES.failedToInvokeCallbackHandler();
            le.initCause(var9);
            throw le;
         } catch (UnsupportedCallbackException var10) {
            le = new LoginException();
            le.initCause(var10);
            throw le;
         }

         info[0] = username;
         info[1] = password;
         return info;
      }
   }

   protected String createPasswordHash(String username, String password, String digestOption) throws LoginException {
      DigestCallback callback = null;
      String callbackClassName = (String)this.options.get(digestOption);
      if (callbackClassName != null) {
         try {
            Class<?> callbackClass = SecurityActions.loadClass(callbackClassName);
            callback = (DigestCallback)callbackClass.newInstance();
            PicketBoxLogger.LOGGER.traceCreateDigestCallback(callbackClassName);
         } catch (Exception var12) {
            LoginException le = new LoginException(PicketBoxMessages.MESSAGES.failedToInstantiateClassMessage(Callback.class));
            le.initCause(var12);
            throw le;
         }

         Map<String, Object> tmp = new HashMap();
         tmp.putAll(this.options);
         tmp.put("javax.security.auth.login.name", username);
         tmp.put("javax.security.auth.login.password", password);
         callback.init(tmp);
         Callback[] callbacks = (Callback[])((Callback[])tmp.get("callbacks"));
         if (callbacks != null) {
            LoginException le;
            try {
               this.callbackHandler.handle(callbacks);
            } catch (IOException var10) {
               le = PicketBoxMessages.MESSAGES.failedToInvokeCallbackHandler();
               le.initCause(var10);
               throw le;
            } catch (UnsupportedCallbackException var11) {
               le = PicketBoxMessages.MESSAGES.failedToInvokeCallbackHandler();
               le.initCause(var11);
               throw le;
            }
         }
      }

      String passwordHash = Util.createPasswordHash(this.hashAlgorithm, this.hashEncoding, this.hashCharset, username, password, callback);
      return passwordHash;
   }

   protected Throwable getValidateError() {
      return this.validateError;
   }

   protected void setValidateError(Throwable validateError) {
      this.validateError = validateError;
   }

   protected boolean validatePassword(String inputPassword, String expectedPassword) {
      if (inputPassword != null && expectedPassword != null) {
         boolean valid = false;
         if (this.ignorePasswordCase) {
            valid = inputPassword.equalsIgnoreCase(expectedPassword);
         } else {
            valid = inputPassword.equals(expectedPassword);
         }

         return valid;
      } else {
         return false;
      }
   }

   protected abstract String getUsersPassword() throws LoginException;

   protected void safeClose(InputStream fis) {
      try {
         if (fis != null) {
            fis.close();
         }
      } catch (Exception var3) {
      }

   }
}
