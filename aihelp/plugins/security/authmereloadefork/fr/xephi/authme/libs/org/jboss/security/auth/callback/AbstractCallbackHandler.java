package fr.xephi.authme.libs.org.jboss.security.auth.callback;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.NameCallback;

public class AbstractCallbackHandler {
   protected String userName;

   protected String getUserName(Callback[] callbacks) {
      if (this.userName == null) {
         for(int i = 0; i < callbacks.length; ++i) {
            Callback callback = callbacks[i];
            if (callback instanceof NameCallback) {
               NameCallback nc = (NameCallback)callback;
               this.userName = nc.getName();
               break;
            }
         }
      }

      return this.userName;
   }
}
