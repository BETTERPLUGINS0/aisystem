package fr.xephi.authme.libs.org.jboss.security.auth.callback;

import fr.xephi.authme.libs.org.jboss.security.PicketBoxMessages;
import java.io.IOException;
import java.lang.reflect.Method;
import java.security.Principal;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;

public class SecurityAssociationHandler implements CallbackHandler {
   private Principal principal;
   private Object credential;

   public SecurityAssociationHandler() {
   }

   public SecurityAssociationHandler(Principal principal, Object credential) {
      this.principal = principal;
      this.credential = credential;
   }

   public void setSecurityInfo(Principal principal, Object credential) {
      this.principal = principal;
      this.credential = credential;
   }

   public void handle(Callback[] callbacks) throws UnsupportedCallbackException, IOException {
      for(int i = 0; i < callbacks.length; ++i) {
         Callback c = callbacks[i];
         if (c instanceof SecurityAssociationCallback) {
            SecurityAssociationCallback sac = (SecurityAssociationCallback)c;
            sac.setPrincipal(this.principal);
            sac.setCredential(this.credential);
         } else if (c instanceof ObjectCallback) {
            ObjectCallback oc = (ObjectCallback)c;
            oc.setCredential(this.credential);
         } else if (c instanceof NameCallback) {
            NameCallback nc = (NameCallback)c;
            if (this.principal != null) {
               nc.setName(this.principal.getName());
            }
         } else {
            if (!(c instanceof PasswordCallback)) {
               try {
                  CallbackHandler handler = SecurityActions.getContextCallbackHandler();
                  if (handler != null) {
                     Callback[] unknown = new Callback[]{c};
                     handler.handle(unknown);
                     return;
                  }
               } catch (Exception var6) {
               }

               throw PicketBoxMessages.MESSAGES.unableToHandleCallback(c, this.getClass().getName(), c.getClass().getCanonicalName());
            }

            PasswordCallback pc = (PasswordCallback)c;
            char[] password = this.getPassword();
            if (password != null) {
               pc.setPassword(password);
            }
         }
      }

   }

   private char[] getPassword() {
      char[] password = null;
      if (this.credential instanceof char[]) {
         password = (char[])((char[])this.credential);
      } else if (this.credential instanceof String) {
         String s = (String)this.credential;
         password = s.toCharArray();
      } else {
         try {
            Class<?>[] types = new Class[0];
            Method m = this.credential.getClass().getMethod("toCharArray", types);
            Object[] args = new Object[0];
            password = (char[])((char[])m.invoke(this.credential, args));
         } catch (Exception var5) {
            if (this.credential != null) {
               String s = this.credential.toString();
               password = s.toCharArray();
            }
         }
      }

      return password;
   }
}
