package fr.xephi.authme.libs.org.jboss.security.auth.container.modules;

import javax.security.auth.Subject;
import javax.security.auth.message.AuthException;
import javax.security.auth.message.AuthStatus;
import javax.security.auth.message.MessageInfo;

public class AllSuccessServerAuthModule extends AbstractServerAuthModule {
   public Class[] getSupportedMessageTypes() {
      this.supportedTypes.add(Object.class);
      return super.getSupportedMessageTypes();
   }

   protected boolean validate(Subject clientSubject, MessageInfo messageInfo) throws AuthException {
      return true;
   }

   public AuthStatus secureResponse(MessageInfo arg0, Subject arg1) throws AuthException {
      return AuthStatus.SUCCESS;
   }
}
