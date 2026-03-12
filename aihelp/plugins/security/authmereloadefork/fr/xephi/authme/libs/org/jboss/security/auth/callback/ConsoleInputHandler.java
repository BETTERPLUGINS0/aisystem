package fr.xephi.authme.libs.org.jboss.security.auth.callback;

import fr.xephi.authme.libs.org.jboss.security.PicketBoxMessages;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;

public class ConsoleInputHandler implements CallbackHandler {
   public void handle(Callback[] callbacks) throws UnsupportedCallbackException {
      for(int i = 0; i < callbacks.length; ++i) {
         Callback c = callbacks[i];
         String prompt;
         InputStreamReader isr;
         BufferedReader br;
         String password;
         if (c instanceof NameCallback) {
            NameCallback nc = (NameCallback)c;
            prompt = nc.getPrompt();
            if (prompt == null) {
               prompt = PicketBoxMessages.MESSAGES.enterUsernameMessage();
            }

            System.out.print(prompt);
            isr = new InputStreamReader(System.in);
            br = new BufferedReader(isr);

            try {
               password = br.readLine();
               nc.setName(password);
            } catch (IOException var10) {
               throw PicketBoxMessages.MESSAGES.failedToObtainUsername(var10);
            }
         } else {
            if (!(c instanceof PasswordCallback)) {
               throw PicketBoxMessages.MESSAGES.unableToHandleCallback(c, this.getClass().getName(), c.getClass().getCanonicalName());
            }

            PasswordCallback pc = (PasswordCallback)c;
            prompt = pc.getPrompt();
            if (prompt == null) {
               prompt = PicketBoxMessages.MESSAGES.enterPasswordMessage();
            }

            System.out.print(prompt);
            isr = new InputStreamReader(System.in);
            br = new BufferedReader(isr);

            try {
               password = br.readLine();
               pc.setPassword(password.toCharArray());
            } catch (IOException var9) {
               throw PicketBoxMessages.MESSAGES.failedToObtainPassword(var9);
            }
         }
      }

   }
}
