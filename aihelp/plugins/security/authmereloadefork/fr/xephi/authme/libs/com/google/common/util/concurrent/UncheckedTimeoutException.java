package fr.xephi.authme.libs.com.google.common.util.concurrent;

import fr.xephi.authme.libs.com.google.common.annotations.GwtIncompatible;
import javax.annotation.CheckForNull;

@ElementTypesAreNonnullByDefault
@GwtIncompatible
public class UncheckedTimeoutException extends RuntimeException {
   private static final long serialVersionUID = 0L;

   public UncheckedTimeoutException() {
   }

   public UncheckedTimeoutException(@CheckForNull String message) {
      super(message);
   }

   public UncheckedTimeoutException(@CheckForNull Throwable cause) {
      super(cause);
   }

   public UncheckedTimeoutException(@CheckForNull String message, @CheckForNull Throwable cause) {
      super(message, cause);
   }
}
