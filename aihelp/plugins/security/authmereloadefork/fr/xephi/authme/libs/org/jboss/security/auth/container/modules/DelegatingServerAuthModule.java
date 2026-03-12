package fr.xephi.authme.libs.org.jboss.security.auth.container.modules;

import javax.security.auth.Subject;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;
import javax.security.auth.message.AuthException;
import javax.security.auth.message.AuthStatus;
import javax.security.auth.message.MessageInfo;

public class DelegatingServerAuthModule extends AbstractServerAuthModule {
   private LoginContext loginContext;
   private String loginContextName;

   public DelegatingServerAuthModule() {
      this.loginContext = null;
      this.loginContextName = null;
      this.supportedTypes.add(Object.class);
   }

   public DelegatingServerAuthModule(String loginModuleStackHolderName) {
      this();
      this.loginContextName = loginModuleStackHolderName;
   }

   public Class[] getSupportedMessageTypes() {
      Class[] clarr = new Class[this.supportedTypes.size()];
      this.supportedTypes.toArray(clarr);
      return clarr;
   }

   public void cleanSubject(MessageInfo messageInfo, Subject subject) throws AuthException {
      if (this.loginContext != null) {
         try {
            this.loginContext.logout();
         } catch (LoginException var4) {
            throw new AuthException(var4.getLocalizedMessage());
         }
      }

   }

   public AuthStatus secureResponse(MessageInfo messageInfo, Subject arg1) throws AuthException {
      throw new UnsupportedOperationException();
   }

   protected boolean validate(Subject clientSubject, MessageInfo messageInfo) throws AuthException {
      try {
         this.loginContext = SecurityActions.createLoginContext(this.getSecurityDomainName(), clientSubject, this.callbackHandler);
         this.loginContext.login();
         return true;
      } catch (Exception var4) {
         throw new AuthException(var4.getLocalizedMessage());
      }
   }

   private String getSecurityDomainName() {
      if (this.loginContextName != null) {
         return this.loginContextName;
      } else {
         String domainName = (String)this.options.get("javax.security.auth.login.LoginContext");
         if (domainName == null) {
            domainName = this.getClass().getName();
         }

         return domainName;
      }
   }
}
