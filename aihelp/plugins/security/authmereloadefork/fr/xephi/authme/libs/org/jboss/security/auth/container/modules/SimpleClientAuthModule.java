package fr.xephi.authme.libs.org.jboss.security.auth.container.modules;

import fr.xephi.authme.libs.org.jboss.security.PicketBoxMessages;
import fr.xephi.authme.libs.org.jboss.security.SimplePrincipal;
import java.util.Map;
import java.util.Set;
import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.message.AuthException;
import javax.security.auth.message.AuthStatus;
import javax.security.auth.message.MessageInfo;
import javax.security.auth.message.MessagePolicy;
import javax.security.auth.message.module.ClientAuthModule;

public class SimpleClientAuthModule implements ClientAuthModule {
   private Class[] supportedTypes = null;
   private SimplePrincipal principal = null;
   private Object credential = null;
   private MessagePolicy requestPolicy = null;
   private MessagePolicy responsePolicy = null;
   private CallbackHandler handler = null;
   private Map options = null;

   public SimpleClientAuthModule(Class[] supportedTypes) {
      this.supportedTypes = supportedTypes;
   }

   public void initialize(MessagePolicy requestPolicy, MessagePolicy responsePolicy, CallbackHandler handler, Map options) throws AuthException {
      this.requestPolicy = requestPolicy;
      this.responsePolicy = responsePolicy;
      this.handler = handler;
      this.options = options;
   }

   public AuthStatus secureRequest(MessageInfo param, Subject source) throws AuthException {
      source.getPrincipals().add(this.principal);
      source.getPublicCredentials().add(this.credential);
      return AuthStatus.SUCCESS;
   }

   public AuthStatus validateResponse(MessageInfo messageInfo, Subject source, Subject recipient) throws AuthException {
      Set sourceSet = source.getPrincipals(SimplePrincipal.class);
      Set recipientSet = recipient.getPrincipals(SimplePrincipal.class);
      if (sourceSet == null && recipientSet == null) {
         throw new AuthException();
      } else if (sourceSet.size() != recipientSet.size()) {
         throw new AuthException(PicketBoxMessages.MESSAGES.sizeMismatchMessage("source", "recipient"));
      } else {
         return AuthStatus.SUCCESS;
      }
   }

   public Class[] getSupportedMessageTypes() {
      return this.supportedTypes;
   }

   public void cleanSubject(MessageInfo messageInfo, Subject subject) throws AuthException {
      subject.getPrincipals().remove(this.principal);
      subject.getPublicCredentials().remove(this.credential);
   }
}
