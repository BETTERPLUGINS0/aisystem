package fr.xephi.authme.libs.org.jboss.security;

import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.message.MessageInfo;

public interface ServerAuthenticationManager extends AuthenticationManager {
   boolean isValid(MessageInfo var1, Subject var2, String var3, CallbackHandler var4);

   boolean isValid(MessageInfo var1, Subject var2, String var3, String var4, CallbackHandler var5);

   void secureResponse(MessageInfo var1, Subject var2, String var3, String var4, CallbackHandler var5);

   void cleanSubject(MessageInfo var1, Subject var2, String var3, String var4, CallbackHandler var5);
}
