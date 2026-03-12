package fr.xephi.authme.libs.org.jboss.security.auth.container.modules;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.message.AuthException;
import javax.security.auth.message.AuthStatus;
import javax.security.auth.message.MessageInfo;
import javax.security.auth.message.MessagePolicy;
import javax.security.auth.message.module.ServerAuthModule;
import javax.security.auth.spi.LoginModule;

public abstract class AbstractServerAuthModule implements ServerAuthModule {
   protected CallbackHandler callbackHandler = null;
   protected MessagePolicy requestPolicy = null;
   protected MessagePolicy responsePolicy = null;
   protected Map options = null;
   protected ArrayList<Class> supportedTypes = new ArrayList();

   public void initialize(MessagePolicy requestPolicy, MessagePolicy responsePolicy, CallbackHandler handler, Map options) throws AuthException {
      this.requestPolicy = requestPolicy;
      this.responsePolicy = responsePolicy;
      this.callbackHandler = handler;
      if (options == null) {
         options = new HashMap();
      }

      this.options = (Map)options;
   }

   public void cleanSubject(MessageInfo messageInfo, Subject subject) throws AuthException {
      subject.getPrincipals().clear();
      subject.getPublicCredentials().clear();
      subject.getPrivateCredentials().clear();
   }

   public AuthStatus validateRequest(MessageInfo messageInfo, Subject clientSubject, Subject serviceSubject) throws AuthException {
      String loginModuleName = (String)this.options.get("login-module-delegate");
      if (loginModuleName != null) {
         ClassLoader tcl = SecurityActions.getContextClassLoader();

         try {
            Class clazz = tcl.loadClass(loginModuleName);
            LoginModule lm = (LoginModule)clazz.newInstance();
            lm.initialize(clientSubject, this.callbackHandler, new HashMap(), this.options);
            lm.login();
            lm.commit();
         } catch (Exception var8) {
            throw new AuthException(var8.getLocalizedMessage());
         }

         return AuthStatus.SUCCESS;
      } else {
         return this.validate(clientSubject, messageInfo) ? AuthStatus.SUCCESS : AuthStatus.FAILURE;
      }
   }

   public Class[] getSupportedMessageTypes() {
      Class[] clsarr = new Class[this.supportedTypes.size()];
      this.supportedTypes.toArray(clsarr);
      return clsarr;
   }

   public CallbackHandler getCallbackHandler() {
      return this.callbackHandler;
   }

   public void setCallbackHandler(CallbackHandler callbackHandler) {
      this.callbackHandler = callbackHandler;
   }

   protected abstract boolean validate(Subject var1, MessageInfo var2) throws AuthException;
}
