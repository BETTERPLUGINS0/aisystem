package fr.xephi.authme.libs.org.jboss.security.auth.message.config;

import fr.xephi.authme.libs.org.jboss.security.PicketBoxMessages;
import java.util.Iterator;
import javax.security.auth.Subject;
import javax.security.auth.message.AuthException;
import javax.security.auth.message.AuthStatus;
import javax.security.auth.message.MessageInfo;
import javax.security.auth.message.config.ClientAuthContext;
import javax.security.auth.message.module.ClientAuthModule;

public class JBossClientAuthContext implements ClientAuthContext {
   private JBossClientAuthConfig config;

   public JBossClientAuthContext(JBossClientAuthConfig config) {
      if (config == null) {
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("config");
      } else {
         this.config = config;
      }
   }

   public void cleanSubject(MessageInfo messageInfo, Subject subject) throws AuthException {
      Iterator iter = this.config.getClientAuthModules().iterator();

      while(iter.hasNext()) {
         ((ClientAuthModule)iter.next()).cleanSubject(messageInfo, subject);
      }

   }

   public AuthStatus secureRequest(MessageInfo messageInfo, Subject clientSubject) throws AuthException {
      Iterator iter = this.config.getClientAuthModules().iterator();
      AuthStatus status = null;

      while(iter.hasNext()) {
         status = ((ClientAuthModule)iter.next()).secureRequest(messageInfo, clientSubject);
         if (status == AuthStatus.FAILURE) {
            break;
         }
      }

      return status;
   }

   public AuthStatus validateResponse(MessageInfo messageInfo, Subject clientSubject, Subject serviceSubject) throws AuthException {
      Iterator iter = this.config.getClientAuthModules().iterator();
      AuthStatus status = null;

      while(iter.hasNext()) {
         status = ((ClientAuthModule)iter.next()).validateResponse(messageInfo, clientSubject, serviceSubject);
         if (status == AuthStatus.FAILURE) {
            break;
         }
      }

      return status;
   }
}
