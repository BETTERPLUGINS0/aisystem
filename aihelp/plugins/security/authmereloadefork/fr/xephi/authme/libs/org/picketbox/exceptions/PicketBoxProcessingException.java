package fr.xephi.authme.libs.org.picketbox.exceptions;

import java.security.GeneralSecurityException;

public class PicketBoxProcessingException extends GeneralSecurityException {
   private static final long serialVersionUID = 1L;

   public PicketBoxProcessingException() {
   }

   public PicketBoxProcessingException(String arg0, Throwable arg1) {
      super(arg0, arg1);
   }

   public PicketBoxProcessingException(String arg0) {
      super(arg0);
   }

   public PicketBoxProcessingException(Throwable arg0) {
      super(arg0);
   }
}
