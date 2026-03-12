package fr.xephi.authme.libs.org.apache.http.impl.auth;

import fr.xephi.authme.libs.org.apache.http.auth.AuthenticationException;

public class NTLMEngineException extends AuthenticationException {
   private static final long serialVersionUID = 6027981323731768824L;

   public NTLMEngineException() {
   }

   public NTLMEngineException(String message) {
      super(message);
   }

   public NTLMEngineException(String message, Throwable cause) {
      super(message, cause);
   }
}
