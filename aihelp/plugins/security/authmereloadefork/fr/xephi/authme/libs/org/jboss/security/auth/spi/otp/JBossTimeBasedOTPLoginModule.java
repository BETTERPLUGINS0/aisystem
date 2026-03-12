package fr.xephi.authme.libs.org.jboss.security.auth.spi.otp;

import fr.xephi.authme.libs.org.jboss.security.PicketBoxLogger;
import fr.xephi.authme.libs.org.jboss.security.PicketBoxMessages;
import fr.xephi.authme.libs.org.jboss.security.SimplePrincipal;
import fr.xephi.authme.libs.org.jboss.security.otp.TimeBasedOTPUtil;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.acl.Group;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;
import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;
import javax.security.jacc.PolicyContext;
import javax.security.jacc.PolicyContextException;
import javax.servlet.http.HttpServletRequest;

public class JBossTimeBasedOTPLoginModule implements LoginModule {
   private static final String PASSWORD_STACKING = "password-stacking";
   private static final String USE_FIRST_PASSWORD = "useFirstPass";
   private static final String NUM_OF_DIGITS_OPT = "numOfDigits";
   private static final String ALGORITHM = "algorithm";
   private static final String ADDITIONAL_ROLES = "additionalRoles";
   private static final String[] ALL_VALID_OPTIONS = new String[]{"password-stacking", "useFirstPass", "numOfDigits", "algorithm", "additionalRoles"};
   public static final String TOTP = "totp";
   private Map<String, Object> lmSharedState = new HashMap();
   private Map<String, Object> lmOptions = new HashMap();
   private CallbackHandler callbackHandler;
   private boolean useFirstPass;
   private int NUMBER_OF_DIGITS = 6;
   private String additionalRoles = null;
   private String algorithm = "HmacSHA1";
   private Subject subject;

   public void initialize(Subject subject, CallbackHandler callbackHandler, Map<String, ?> sharedState, Map<String, ?> options) {
      HashSet<String> validOptions = new HashSet(Arrays.asList(ALL_VALID_OPTIONS));
      Iterator i$ = options.keySet().iterator();

      String numDigitString;
      while(i$.hasNext()) {
         numDigitString = (String)i$.next();
         if (!validOptions.contains(numDigitString)) {
            PicketBoxLogger.LOGGER.warnInvalidModuleOption(numDigitString);
         }
      }

      this.subject = subject;
      this.callbackHandler = callbackHandler;
      this.lmSharedState.putAll(sharedState);
      this.lmOptions.putAll(options);
      String passwordStacking = (String)options.get("password-stacking");
      if (passwordStacking != null && passwordStacking.equalsIgnoreCase("useFirstPass")) {
         this.useFirstPass = true;
      }

      numDigitString = (String)options.get("numOfDigits");
      if (numDigitString != null && numDigitString.length() > 0) {
         this.NUMBER_OF_DIGITS = Integer.parseInt(numDigitString);
      }

      String algorithmStr = (String)options.get("algorithm");
      if (algorithmStr != null && !algorithmStr.isEmpty()) {
         if (algorithmStr.equalsIgnoreCase("HmacSHA256")) {
            this.algorithm = "HmacSHA256";
         }

         if (algorithmStr.equalsIgnoreCase("HmacSHA512")) {
            this.algorithm = "HmacSHA512";
         }
      }

      this.additionalRoles = (String)options.get("additionalRoles");
   }

   public boolean login() throws LoginException {
      String username;
      if (this.useFirstPass) {
         username = (String)this.lmSharedState.get("javax.security.auth.login.name");
      } else {
         NameCallback nc = new NameCallback(PicketBoxMessages.MESSAGES.enterUsernameMessage(), "guest");
         Callback[] callbacks = new Callback[]{nc};

         try {
            this.callbackHandler.handle(callbacks);
         } catch (Exception var16) {
            LoginException le = new LoginException();
            le.initCause(var16);
            throw le;
         }

         username = nc.getName();
      }

      ClassLoader tcl = SecurityActions.getContextClassLoader();
      InputStream is = null;
      Properties otp = new Properties();

      try {
         is = tcl.getResourceAsStream("otp-users.properties");
         otp.load(is);
      } catch (IOException var14) {
         LoginException le = new LoginException();
         le.initCause(var14);
         throw le;
      } finally {
         this.safeClose(is);
      }

      String seed = otp.getProperty(username);
      String submittedTOTP = this.getTimeBasedOTPFromRequest();
      if (submittedTOTP != null && submittedTOTP.length() != 0) {
         try {
            boolean result = false;
            if (this.algorithm.equals("HmacSHA1")) {
               result = TimeBasedOTPUtil.validate(submittedTOTP, seed.getBytes(), this.NUMBER_OF_DIGITS);
            } else if (this.algorithm.equals("HmacSHA256")) {
               result = TimeBasedOTPUtil.validate256(submittedTOTP, seed.getBytes(), this.NUMBER_OF_DIGITS);
            } else if (this.algorithm.equals("HmacSHA512")) {
               result = TimeBasedOTPUtil.validate512(submittedTOTP, seed.getBytes(), this.NUMBER_OF_DIGITS);
            }

            if (!result) {
               throw new LoginException();
            } else {
               Set<Group> groupPrincipals = this.subject.getPrincipals(Group.class);
               if (groupPrincipals != null && groupPrincipals.size() > 0) {
                  this.appendRoles((Group)groupPrincipals.iterator().next());
               }

               return result;
            }
         } catch (GeneralSecurityException var13) {
            LoginException le = new LoginException();
            le.initCause(var13);
            throw le;
         }
      } else {
         throw new LoginException();
      }
   }

   public boolean commit() throws LoginException {
      return true;
   }

   public boolean abort() throws LoginException {
      return true;
   }

   public boolean logout() throws LoginException {
      return true;
   }

   private String getTimeBasedOTPFromRequest() {
      String totp = null;
      String WEB_REQUEST_KEY = "javax.servlet.http.HttpServletRequest";

      try {
         HttpServletRequest request = (HttpServletRequest)PolicyContext.getContext(WEB_REQUEST_KEY);
         totp = request.getParameter("totp");
      } catch (PolicyContextException var4) {
         PicketBoxLogger.LOGGER.debugErrorGettingRequestFromPolicyContext(var4);
      }

      return totp;
   }

   private void appendRoles(Group group) {
      if (group.getName().equals("Roles")) {
         if (this.additionalRoles != null && !this.additionalRoles.isEmpty()) {
            StringTokenizer st = new StringTokenizer(this.additionalRoles, ",");

            while(st.hasMoreTokens()) {
               group.addMember(new SimplePrincipal(st.nextToken().trim()));
            }
         }

      }
   }

   private void safeClose(InputStream fis) {
      try {
         if (fis != null) {
            fis.close();
         }
      } catch (Exception var3) {
      }

   }
}
