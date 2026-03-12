package fr.xephi.authme.libs.org.jboss.security.auth.container.modules;

import java.util.Arrays;
import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.message.AuthException;
import javax.security.auth.message.AuthStatus;
import javax.security.auth.message.MessageInfo;

public class SimpleServerAuthModule extends AbstractServerAuthModule {
   public SimpleServerAuthModule() {
      this.supportedTypes.add(Object.class);
      this.supportedTypes.add(Object.class);
   }

   public SimpleServerAuthModule(Class<?>[] supTypes) {
      this.supportedTypes.addAll(Arrays.asList(supTypes));
   }

   public AuthStatus secureResponse(MessageInfo param, Subject source) throws AuthException {
      return AuthStatus.SUCCESS;
   }

   protected boolean validate(Subject clientSubject, MessageInfo messageInfo) throws AuthException {
      NameCallback nc = new NameCallback("Dummy");
      PasswordCallback pc = new PasswordCallback("B", true);

      try {
         this.callbackHandler.handle(new Callback[]{nc, pc});
         String userName = nc.getName();
         String pwd = new String(pc.getPassword());
         return userName.equals(this.options.get("principal")) && pwd.equals(this.options.get("pass"));
      } catch (Exception var7) {
         throw new AuthException(var7.getLocalizedMessage());
      }
   }
}
