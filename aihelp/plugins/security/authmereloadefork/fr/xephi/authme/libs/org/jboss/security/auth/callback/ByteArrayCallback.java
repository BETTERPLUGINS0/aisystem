package fr.xephi.authme.libs.org.jboss.security.auth.callback;

import javax.security.auth.callback.Callback;

public class ByteArrayCallback implements Callback {
   private transient String prompt;
   private transient byte[] data;

   public ByteArrayCallback(String prompt) {
      this.prompt = prompt;
   }

   public String getPrompt() {
      return this.prompt;
   }

   public byte[] getByteArray() {
      return this.data;
   }

   public void setByteArray(byte[] data) {
      this.data = data;
   }

   public void clearByteArray() {
      this.data = null;
   }
}
