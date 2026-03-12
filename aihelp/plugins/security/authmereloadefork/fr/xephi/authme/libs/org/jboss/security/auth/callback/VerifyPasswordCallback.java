package fr.xephi.authme.libs.org.jboss.security.auth.callback;

import javax.security.auth.callback.Callback;

public class VerifyPasswordCallback implements Callback {
   protected String value;
   protected boolean verified = false;

   public String getValue() {
      return this.value;
   }

   public void setValue(String value) {
      this.value = value;
   }

   public void setValue(char[] value) {
      this.value = new String(value);
   }

   public void setValue(byte[] value) {
      this.value = new String(value);
   }

   public boolean isVerified() {
      return this.verified;
   }

   public void setVerified(boolean verified) {
      this.verified = verified;
   }
}
