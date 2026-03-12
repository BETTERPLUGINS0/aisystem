package fr.xephi.authme.libs.org.jboss.security.auth.callback;

import fr.xephi.authme.libs.org.jboss.security.PicketBoxMessages;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;

public class UsernamePasswordHandler implements CallbackHandler {
   private transient String username;
   private transient char[] password;
   private transient Object credential;

   public UsernamePasswordHandler(String username, char[] password) {
      this.username = username;
      this.password = password;
      this.credential = password;
   }

   public UsernamePasswordHandler(String username, Object credential) {
      this.username = username;
      this.credential = credential;
   }

   public void handle(Callback[] callbacks) throws UnsupportedCallbackException {
      for(int i = 0; i < callbacks.length; ++i) {
         Callback c = callbacks[i];
         if (c instanceof NameCallback) {
            NameCallback nc = (NameCallback)c;
            nc.setName(this.username);
         } else if (c instanceof PasswordCallback) {
            PasswordCallback pc = (PasswordCallback)c;
            if (this.password == null && this.credential != null) {
               String tmp = this.credential.toString();
               this.password = tmp.toCharArray();
            }

            pc.setPassword(this.password);
         } else {
            if (!(c instanceof ObjectCallback)) {
               throw PicketBoxMessages.MESSAGES.unableToHandleCallback(c, this.getClass().getName(), c.getClass().getCanonicalName());
            }

            ObjectCallback oc = (ObjectCallback)c;
            oc.setCredential(this.credential);
         }
      }

   }
}
