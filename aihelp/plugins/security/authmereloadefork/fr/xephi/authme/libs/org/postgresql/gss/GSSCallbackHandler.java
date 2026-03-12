package fr.xephi.authme.libs.org.postgresql.gss;

import java.io.IOException;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.TextOutputCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import org.checkerframework.checker.nullness.qual.Nullable;

class GSSCallbackHandler implements CallbackHandler {
   private final String user;
   @Nullable
   private final char[] password;

   GSSCallbackHandler(String user, @Nullable char[] password) {
      this.user = user;
      this.password = password;
   }

   public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
      Callback[] var2 = callbacks;
      int var3 = callbacks.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Callback callback = var2[var4];
         if (callback instanceof TextOutputCallback) {
            TextOutputCallback toc = (TextOutputCallback)callback;
            switch(toc.getMessageType()) {
            case 0:
               System.out.println("INFO: " + toc.getMessage());
               break;
            case 1:
               System.out.println("WARNING: " + toc.getMessage());
               break;
            case 2:
               System.out.println("ERROR: " + toc.getMessage());
               break;
            default:
               throw new IOException("Unsupported message type: " + toc.getMessageType());
            }
         } else if (callback instanceof NameCallback) {
            NameCallback nc = (NameCallback)callback;
            nc.setName(this.user);
         } else {
            if (!(callback instanceof PasswordCallback)) {
               throw new UnsupportedCallbackException(callback, "Unrecognized Callback");
            }

            PasswordCallback pc = (PasswordCallback)callback;
            if (this.password == null) {
               throw new IOException("No cached kerberos ticket found and no password supplied.");
            }

            pc.setPassword(this.password);
         }
      }

   }
}
