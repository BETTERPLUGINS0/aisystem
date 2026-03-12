package fr.xephi.authme.libs.org.jboss.security.auth.container.modules;

import fr.xephi.authme.libs.org.jboss.security.PicketBoxMessages;
import fr.xephi.authme.libs.org.jboss.security.SimplePrincipal;
import fr.xephi.authme.libs.org.jboss.security.auth.callback.JBossCallbackHandler;
import java.security.Principal;
import javax.security.auth.Subject;
import javax.security.auth.message.AuthException;
import javax.security.auth.message.AuthStatus;
import javax.security.auth.message.MessageInfo;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HttpServletServerAuthModule extends DelegatingServerAuthModule {
   public HttpServletServerAuthModule() {
      this("");
   }

   public HttpServletServerAuthModule(String lmshName) {
      super(lmshName);
      this.supportedTypes.add(HttpServletRequest.class);
      this.supportedTypes.add(HttpServletResponse.class);
   }

   protected boolean validate(Subject clientSubject, MessageInfo messageInfo) throws AuthException {
      this.callbackHandler = new JBossCallbackHandler(this.getUserName(messageInfo), this.getCredential(messageInfo));
      return super.validate(clientSubject, messageInfo);
   }

   public AuthStatus secureResponse(MessageInfo arg0, Subject arg1) throws AuthException {
      throw new UnsupportedOperationException();
   }

   private Principal getUserName(MessageInfo messageInfo) {
      Object requestInfo = messageInfo.getRequestMessage();
      String userNameParam = (String)this.options.get("userNameParam");
      if (!(requestInfo instanceof HttpServletRequest)) {
         throw PicketBoxMessages.MESSAGES.invalidType(HttpServletRequest.class.getName());
      } else {
         HttpServletRequest hsr = (HttpServletRequest)requestInfo;
         return new SimplePrincipal(hsr.getParameter(userNameParam));
      }
   }

   private Object getCredential(MessageInfo messageInfo) {
      Object requestInfo = messageInfo.getRequestMessage();
      String passwordParam = (String)this.options.get("passwordParam");
      if (!(requestInfo instanceof HttpServletRequest)) {
         throw PicketBoxMessages.MESSAGES.invalidType(HttpServletRequest.class.getName());
      } else {
         HttpServletRequest hsr = (HttpServletRequest)requestInfo;
         return hsr.getParameter(passwordParam);
      }
   }
}
