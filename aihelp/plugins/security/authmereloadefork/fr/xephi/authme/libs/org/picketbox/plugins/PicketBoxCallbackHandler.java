package fr.xephi.authme.libs.org.picketbox.plugins;

import fr.xephi.authme.libs.org.jboss.security.PicketBoxMessages;
import fr.xephi.authme.libs.org.jboss.security.auth.callback.ObjectCallback;
import fr.xephi.authme.libs.org.picketbox.handlers.HandlerContract;
import java.io.IOException;
import java.security.Principal;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;

public class PicketBoxCallbackHandler implements CallbackHandler, HandlerContract {
   private Principal principal = null;
   private Object credential = null;

   public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
      int len = callbacks.length;
      if (len > 0) {
         Callback[] arr$ = callbacks;
         int len$ = callbacks.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            Callback cb = arr$[i$];
            if (cb instanceof NameCallback) {
               NameCallback nameCallback = (NameCallback)cb;
               nameCallback.setName(this.principal.getName());
            } else if (cb instanceof ObjectCallback) {
               ((ObjectCallback)cb).setCredential(this.credential);
            } else {
               if (!(cb instanceof PasswordCallback)) {
                  throw PicketBoxMessages.MESSAGES.unableToHandleCallback(cb, this.getClass().getName(), cb.getClass().getCanonicalName());
               }

               char[] passwd = null;
               if (this.credential instanceof String) {
                  passwd = ((String)this.credential).toCharArray();
               } else if (this.credential instanceof char[]) {
                  passwd = (char[])((char[])this.credential);
               }

               ((PasswordCallback)cb).setPassword(passwd);
            }
         }
      }

   }

   public void setSecurityInfo(Principal principal, Object credential) {
      this.principal = principal;
      this.credential = credential;
   }
}
