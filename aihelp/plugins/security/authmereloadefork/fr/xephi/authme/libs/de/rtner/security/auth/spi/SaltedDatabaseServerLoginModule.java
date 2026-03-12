package fr.xephi.authme.libs.de.rtner.security.auth.spi;

import fr.xephi.authme.libs.org.jboss.security.PicketBoxMessages;
import fr.xephi.authme.libs.org.jboss.security.auth.spi.DatabaseServerLoginModule;
import java.util.Map;
import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.LoginException;

public class SaltedDatabaseServerLoginModule extends DatabaseServerLoginModule {
   private static final String HMAC_ALGORITHM = "hmacAlgorithm";
   private static final String HASH_CHARSET = "hashCharset";
   private static final String FORMATTER = "formatter";
   private static final String ENGINE = "engine";
   private static final String ENGINE_PARAMETERS = "engine-parameters";
   private static final String[] ALL_VALID_OPTIONS = new String[]{"hmacAlgorithm", "hashCharset", "formatter", "engine", "engine-parameters"};
   public final String DEFAULT_FORMATTER = "fr.xephi.authme.libs.de.rtner.security.auth.spi.PBKDF2HexFormatter";
   public final String DEFAULT_ENGINE = "fr.xephi.authme.libs.de.rtner.security.auth.spi.PBKDF2Engine";
   public final String DEFAULT_PARAMETER = "fr.xephi.authme.libs.de.rtner.security.auth.spi.PBKDF2Parameters";
   protected String hashAlgorithm = null;
   protected String hashCharset = null;
   protected String formatterClassName = null;
   protected PBKDF2Formatter formatter = null;
   protected String engineClassName = null;
   protected String parameterClassName = null;

   public void initialize(Subject subject, CallbackHandler callbackHandler, Map<String, ?> sharedState, Map<String, ?> options) {
      this.addValidOptions(ALL_VALID_OPTIONS);
      super.initialize(subject, callbackHandler, sharedState, options);
      this.hashAlgorithm = (String)options.get("hmacAlgorithm");
      if (this.hashAlgorithm == null) {
         this.hashAlgorithm = "HMacSHA1";
      }

      this.hashCharset = (String)options.get("hashCharset");
      this.formatterClassName = (String)options.get("formatter");
      if (this.formatterClassName == null) {
         this.formatterClassName = "fr.xephi.authme.libs.de.rtner.security.auth.spi.PBKDF2HexFormatter";
      }

      this.engineClassName = (String)options.get("engine");
      if (this.engineClassName == null) {
         this.engineClassName = "fr.xephi.authme.libs.de.rtner.security.auth.spi.PBKDF2Engine";
      }

      this.parameterClassName = (String)options.get("engine-parameters");
      if (this.parameterClassName == null) {
         this.parameterClassName = "fr.xephi.authme.libs.de.rtner.security.auth.spi.PBKDF2Parameters";
      }

   }

   protected boolean validatePassword(String inputPassword, String expectedPassword) {
      boolean verifyOK = false;
      if (inputPassword != null && expectedPassword != null) {
         PBKDF2Parameters p = this.getEngineParameters();
         if (p != null) {
            PBKDF2Formatter f = this.getFormatter();
            if (f != null && !f.fromString(p, expectedPassword)) {
               PBKDF2 pBKDF2Engine = this.getEngine(p);
               if (pBKDF2Engine != null) {
                  verifyOK = pBKDF2Engine.verifyKey(inputPassword);
               }
            }
         }
      }

      return verifyOK;
   }

   protected PBKDF2Parameters getEngineParameters() {
      PBKDF2Parameters p = (PBKDF2Parameters)this.newInstance(this.parameterClassName, PBKDF2Parameters.class);
      if (p != null) {
         p.setHashAlgorithm(this.hashAlgorithm);
         p.setHashCharset(this.hashCharset);
      }

      return p;
   }

   protected PBKDF2 getEngine(PBKDF2Parameters parameters) {
      PBKDF2 engine = (PBKDF2)this.newInstance(this.engineClassName, PBKDF2.class);
      if (engine != null) {
         engine.setParameters(parameters);
      }

      return engine;
   }

   protected PBKDF2Formatter getFormatter() {
      if (this.formatter == null) {
         this.formatter = (PBKDF2Formatter)this.newInstance(this.formatterClassName, PBKDF2Formatter.class);
      }

      return this.formatter;
   }

   protected <T> T newInstance(String name, Class<T> clazz) {
      Object r = null;

      try {
         Class<?> loadedClass = this.getClass().getClassLoader().loadClass(name);
         r = loadedClass.newInstance();
      } catch (Exception var6) {
         LoginException le = new LoginException(PicketBoxMessages.MESSAGES.failedToInstantiateClassMessage(clazz));
         le.initCause(var6);
         this.setValidateError(le);
      }

      return r;
   }
}
