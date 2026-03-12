package fr.xephi.authme.libs.org.apache.http.cookie;

import fr.xephi.authme.libs.org.apache.http.ProtocolException;

public class MalformedCookieException extends ProtocolException {
   private static final long serialVersionUID = -6695462944287282185L;

   public MalformedCookieException() {
   }

   public MalformedCookieException(String message) {
      super(message);
   }

   public MalformedCookieException(String message, Throwable cause) {
      super(message, cause);
   }
}
