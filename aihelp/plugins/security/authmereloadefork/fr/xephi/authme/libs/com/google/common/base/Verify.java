package fr.xephi.authme.libs.com.google.common.base;

import fr.xephi.authme.libs.com.google.common.annotations.GwtCompatible;
import fr.xephi.authme.libs.com.google.errorprone.annotations.CanIgnoreReturnValue;
import javax.annotation.CheckForNull;

@ElementTypesAreNonnullByDefault
@GwtCompatible
public final class Verify {
   public static void verify(boolean expression) {
      if (!expression) {
         throw new VerifyException();
      }
   }

   public static void verify(boolean expression, String errorMessageTemplate, @CheckForNull Object... errorMessageArgs) {
      if (!expression) {
         throw new VerifyException(Strings.lenientFormat(errorMessageTemplate, errorMessageArgs));
      }
   }

   public static void verify(boolean expression, String errorMessageTemplate, char p1) {
      if (!expression) {
         throw new VerifyException(Strings.lenientFormat(errorMessageTemplate, p1));
      }
   }

   public static void verify(boolean expression, String errorMessageTemplate, int p1) {
      if (!expression) {
         throw new VerifyException(Strings.lenientFormat(errorMessageTemplate, p1));
      }
   }

   public static void verify(boolean expression, String errorMessageTemplate, long p1) {
      if (!expression) {
         throw new VerifyException(Strings.lenientFormat(errorMessageTemplate, p1));
      }
   }

   public static void verify(boolean expression, String errorMessageTemplate, @CheckForNull Object p1) {
      if (!expression) {
         throw new VerifyException(Strings.lenientFormat(errorMessageTemplate, p1));
      }
   }

   public static void verify(boolean expression, String errorMessageTemplate, char p1, char p2) {
      if (!expression) {
         throw new VerifyException(Strings.lenientFormat(errorMessageTemplate, p1, p2));
      }
   }

   public static void verify(boolean expression, String errorMessageTemplate, int p1, char p2) {
      if (!expression) {
         throw new VerifyException(Strings.lenientFormat(errorMessageTemplate, p1, p2));
      }
   }

   public static void verify(boolean expression, String errorMessageTemplate, long p1, char p2) {
      if (!expression) {
         throw new VerifyException(Strings.lenientFormat(errorMessageTemplate, p1, p2));
      }
   }

   public static void verify(boolean expression, String errorMessageTemplate, @CheckForNull Object p1, char p2) {
      if (!expression) {
         throw new VerifyException(Strings.lenientFormat(errorMessageTemplate, p1, p2));
      }
   }

   public static void verify(boolean expression, String errorMessageTemplate, char p1, int p2) {
      if (!expression) {
         throw new VerifyException(Strings.lenientFormat(errorMessageTemplate, p1, p2));
      }
   }

   public static void verify(boolean expression, String errorMessageTemplate, int p1, int p2) {
      if (!expression) {
         throw new VerifyException(Strings.lenientFormat(errorMessageTemplate, p1, p2));
      }
   }

   public static void verify(boolean expression, String errorMessageTemplate, long p1, int p2) {
      if (!expression) {
         throw new VerifyException(Strings.lenientFormat(errorMessageTemplate, p1, p2));
      }
   }

   public static void verify(boolean expression, String errorMessageTemplate, @CheckForNull Object p1, int p2) {
      if (!expression) {
         throw new VerifyException(Strings.lenientFormat(errorMessageTemplate, p1, p2));
      }
   }

   public static void verify(boolean expression, String errorMessageTemplate, char p1, long p2) {
      if (!expression) {
         throw new VerifyException(Strings.lenientFormat(errorMessageTemplate, p1, p2));
      }
   }

   public static void verify(boolean expression, String errorMessageTemplate, int p1, long p2) {
      if (!expression) {
         throw new VerifyException(Strings.lenientFormat(errorMessageTemplate, p1, p2));
      }
   }

   public static void verify(boolean expression, String errorMessageTemplate, long p1, long p2) {
      if (!expression) {
         throw new VerifyException(Strings.lenientFormat(errorMessageTemplate, p1, p2));
      }
   }

   public static void verify(boolean expression, String errorMessageTemplate, @CheckForNull Object p1, long p2) {
      if (!expression) {
         throw new VerifyException(Strings.lenientFormat(errorMessageTemplate, p1, p2));
      }
   }

   public static void verify(boolean expression, String errorMessageTemplate, char p1, @CheckForNull Object p2) {
      if (!expression) {
         throw new VerifyException(Strings.lenientFormat(errorMessageTemplate, p1, p2));
      }
   }

   public static void verify(boolean expression, String errorMessageTemplate, int p1, @CheckForNull Object p2) {
      if (!expression) {
         throw new VerifyException(Strings.lenientFormat(errorMessageTemplate, p1, p2));
      }
   }

   public static void verify(boolean expression, String errorMessageTemplate, long p1, @CheckForNull Object p2) {
      if (!expression) {
         throw new VerifyException(Strings.lenientFormat(errorMessageTemplate, p1, p2));
      }
   }

   public static void verify(boolean expression, String errorMessageTemplate, @CheckForNull Object p1, @CheckForNull Object p2) {
      if (!expression) {
         throw new VerifyException(Strings.lenientFormat(errorMessageTemplate, p1, p2));
      }
   }

   public static void verify(boolean expression, String errorMessageTemplate, @CheckForNull Object p1, @CheckForNull Object p2, @CheckForNull Object p3) {
      if (!expression) {
         throw new VerifyException(Strings.lenientFormat(errorMessageTemplate, p1, p2, p3));
      }
   }

   public static void verify(boolean expression, String errorMessageTemplate, @CheckForNull Object p1, @CheckForNull Object p2, @CheckForNull Object p3, @CheckForNull Object p4) {
      if (!expression) {
         throw new VerifyException(Strings.lenientFormat(errorMessageTemplate, p1, p2, p3, p4));
      }
   }

   @CanIgnoreReturnValue
   public static <T> T verifyNotNull(@CheckForNull T reference) {
      return verifyNotNull(reference, "expected a non-null reference");
   }

   @CanIgnoreReturnValue
   public static <T> T verifyNotNull(@CheckForNull T reference, String errorMessageTemplate, @CheckForNull Object... errorMessageArgs) {
      if (reference == null) {
         throw new VerifyException(Strings.lenientFormat(errorMessageTemplate, errorMessageArgs));
      } else {
         return reference;
      }
   }

   private Verify() {
   }
}
