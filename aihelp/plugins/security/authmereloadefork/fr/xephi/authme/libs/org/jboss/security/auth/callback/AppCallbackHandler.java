package fr.xephi.authme.libs.org.jboss.security.auth.callback;

import fr.xephi.authme.libs.org.jboss.security.PicketBoxMessages;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.security.Principal;
import java.util.Iterator;
import java.util.Map;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.TextInputCallback;
import javax.security.auth.callback.UnsupportedCallbackException;

public class AppCallbackHandler implements CallbackHandler {
   private String username;
   private char[] password;
   private byte[] data;
   private String text;
   private transient String prompt;
   private transient Object credential;
   private Map<String, Object> keyValuePair;
   private boolean consoleHandler = false;

   public AppCallbackHandler() {
   }

   public AppCallbackHandler(String username, char[] password) {
      this.username = username;
      this.password = password;
   }

   public AppCallbackHandler(String username, char[] password, byte[] data) {
      this.username = username;
      this.password = password;
      this.data = data;
   }

   public AppCallbackHandler(String username, char[] password, byte[] data, String text) {
      this.username = username;
      this.password = password;
      this.data = data;
      this.text = text;
   }

   public AppCallbackHandler(boolean isConsoleHandler) {
      this.consoleHandler = isConsoleHandler;
   }

   public AppCallbackHandler(String prompt) {
      this.prompt = prompt;
   }

   public AppCallbackHandler(Map<String, Object> mapOfValues) {
      this.keyValuePair = mapOfValues;
   }

   public void setSecurityInfo(Principal p, Object cred) {
      this.username = p.getName();
      this.credential = cred;
   }

   public String getPrompt() {
      return this.prompt;
   }

   public Object getCredential() {
      return this.credential;
   }

   public void setCredential(Object credential) {
      this.credential = credential;
   }

   public void clearCredential() {
      this.credential = null;
   }

   public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
      for(int i = 0; i < callbacks.length; ++i) {
         Callback c = callbacks[i];
         String prompt;
         if (c instanceof NameCallback) {
            NameCallback nc = (NameCallback)c;
            prompt = nc.getPrompt();
            if (prompt == null) {
               prompt = PicketBoxMessages.MESSAGES.enterUsernameMessage();
            }

            if (this.consoleHandler) {
               nc.setName(this.getUserNameFromConsole(prompt));
            } else {
               nc.setName(this.username);
            }
         } else if (c instanceof PasswordCallback) {
            PasswordCallback pc = (PasswordCallback)c;
            prompt = pc.getPrompt();
            if (prompt == null) {
               prompt = PicketBoxMessages.MESSAGES.enterPasswordMessage();
            }

            if (this.consoleHandler) {
               pc.setPassword(this.getPasswordFromConsole(prompt));
            } else if (this.credential != null && this.password == null) {
               pc.setPassword(this.getPassword());
            } else {
               pc.setPassword(this.password);
            }
         } else if (c instanceof TextInputCallback) {
            TextInputCallback tc = (TextInputCallback)c;
            tc.setText(this.text);
         } else if (c instanceof ByteArrayCallback) {
            ByteArrayCallback bac = (ByteArrayCallback)c;
            bac.setByteArray(this.data);
         } else if (c instanceof ObjectCallback) {
            ObjectCallback oc = (ObjectCallback)c;
            oc.setCredential(this.credential);
         } else {
            if (!(c instanceof MapCallback)) {
               throw PicketBoxMessages.MESSAGES.unableToHandleCallback(c, this.getClass().getName(), c.getClass().getCanonicalName());
            }

            MapCallback mc = (MapCallback)c;
            if (this.keyValuePair != null && !this.keyValuePair.isEmpty()) {
               Iterator i$ = this.keyValuePair.keySet().iterator();

               while(i$.hasNext()) {
                  String key = (String)i$.next();
                  mc.setInfo(key, this.keyValuePair.get(key));
               }
            }
         }
      }

   }

   private String getUserNameFromConsole(String prompt) {
      String uName = "";
      System.out.print(prompt);
      InputStreamReader isr = new InputStreamReader(System.in);
      BufferedReader br = new BufferedReader(isr);

      try {
         uName = br.readLine();
         return uName;
      } catch (IOException var6) {
         throw PicketBoxMessages.MESSAGES.failedToObtainUsername(var6);
      }
   }

   private char[] getPasswordFromConsole(String prompt) {
      String pwd = "";
      System.out.print(prompt);
      InputStreamReader isr = new InputStreamReader(System.in);
      BufferedReader br = new BufferedReader(isr);

      try {
         pwd = br.readLine();
      } catch (IOException var6) {
         throw PicketBoxMessages.MESSAGES.failedToObtainPassword(var6);
      }

      return pwd.toCharArray();
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
