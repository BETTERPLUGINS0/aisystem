package fr.xephi.authme.libs.org.jboss.security.callbacks;

import fr.xephi.authme.libs.org.jboss.security.SecurityContext;
import java.io.IOException;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;

public class SecurityContextCallbackHandler implements CallbackHandler {
   private SecurityContext securityContext = null;

   public SecurityContextCallbackHandler(SecurityContext securityContext) {
      this.securityContext = securityContext;
   }

   public SecurityContext getSecurityContext() {
      return this.securityContext;
   }

   public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
      Callback[] arr$ = callbacks;
      int len$ = callbacks.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         Callback cb = arr$[i$];
         if (!(cb instanceof SecurityContextCallback)) {
            throw new UnsupportedCallbackException(cb);
         }

         SecurityContextCallback scb = (SecurityContextCallback)cb;
         scb.setSecurityContext(this.securityContext);
      }

   }
}
