package fr.xephi.authme.libs.org.jboss.security.auth.container.modules;

import javax.security.auth.Subject;
import javax.security.auth.message.AuthException;
import javax.security.auth.message.AuthStatus;
import javax.security.auth.message.MessageInfo;

public class AllFailureServerAuthModule extends AbstractServerAuthModule {
   protected boolean validate(Subject clientSubject, MessageInfo messageInfo) throws AuthException {
      return false;
   }

   public AuthStatus secureResponse(MessageInfo arg0, Subject arg1) throws AuthException {
      return AuthStatus.FAILURE;
   }

   public Class[] getSupportedMessageTypes() {
      this.supportedTypes.add(Object.class);
      return super.getSupportedMessageTypes();
   }
}
