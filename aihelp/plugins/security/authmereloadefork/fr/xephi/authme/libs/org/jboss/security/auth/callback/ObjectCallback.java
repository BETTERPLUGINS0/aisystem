package fr.xephi.authme.libs.org.jboss.security.auth.callback;

import javax.security.auth.callback.Callback;

public class ObjectCallback implements Callback {
   private transient String prompt;
   private transient Object credential;

   public ObjectCallback(String prompt) {
      this.prompt = prompt;
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
}
