package fr.xephi.authme.libs.com.google.common.base;

import fr.xephi.authme.libs.com.google.common.annotations.GwtCompatible;
import fr.xephi.authme.libs.com.google.common.annotations.GwtIncompatible;
import fr.xephi.authme.libs.com.google.common.annotations.VisibleForTesting;
import fr.xephi.authme.libs.com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.annotation.CheckForNull;

@ElementTypesAreNonnullByDefault
@GwtCompatible(
   emulated = true
)
public final class Throwables {
   @GwtIncompatible
   private static final String JAVA_LANG_ACCESS_CLASSNAME = "sun.misc.JavaLangAccess";
   @GwtIncompatible
   @VisibleForTesting
   static final String SHARED_SECRETS_CLASSNAME = "sun.misc.SharedSecrets";
   @CheckForNull
   @GwtIncompatible
   private static final Object jla = getJLA();
   @CheckForNull
   @GwtIncompatible
   private static final Method getStackTraceElementMethod;
   @CheckForNull
   @GwtIncompatible
   private static final Method getStackTraceDepthMethod;

   private Throwables() {
   }

   @GwtIncompatible
   public static <X extends Throwable> void throwIfInstanceOf(Throwable throwable, Class<X> declaredType) throws X {
      Preconditions.checkNotNull(throwable);
      if (declaredType.isInstance(throwable)) {
         throw (Throwable)declaredType.cast(throwable);
      }
   }

   /** @deprecated */
   @Deprecated
   @GwtIncompatible
   public static <X extends Throwable> void propagateIfInstanceOf(@CheckForNull Throwable throwable, Class<X> declaredType) throws X {
      if (throwable != null) {
         throwIfInstanceOf(throwable, declaredType);
      }

   }

   public static void throwIfUnchecked(Throwable throwable) {
      Preconditions.checkNotNull(throwable);
      if (throwable instanceof RuntimeException) {
         throw (RuntimeException)throwable;
      } else if (throwable instanceof Error) {
         throw (Error)throwable;
      }
   }

   /** @deprecated */
   @Deprecated
   @GwtIncompatible
   public static void propagateIfPossible(@CheckForNull Throwable throwable) {
      if (throwable != null) {
         throwIfUnchecked(throwable);
      }

   }

   @GwtIncompatible
   public static <X extends Throwable> void propagateIfPossible(@CheckForNull Throwable throwable, Class<X> declaredType) throws X {
      propagateIfInstanceOf(throwable, declaredType);
      propagateIfPossible(throwable);
   }

   @GwtIncompatible
   public static <X1 extends Throwable, X2 extends Throwable> void propagateIfPossible(@CheckForNull Throwable throwable, Class<X1> declaredType1, Class<X2> declaredType2) throws X1, X2 {
      Preconditions.checkNotNull(declaredType2);
      propagateIfInstanceOf(throwable, declaredType1);
      propagateIfPossible(throwable, declaredType2);
   }

   /** @deprecated */
   @Deprecated
   @CanIgnoreReturnValue
   @GwtIncompatible
   public static RuntimeException propagate(Throwable throwable) {
      throwIfUnchecked(throwable);
      throw new RuntimeException(throwable);
   }

   public static Throwable getRootCause(Throwable throwable) {
      Throwable slowPointer = throwable;

      Throwable cause;
      for(boolean advanceSlowPointer = false; (cause = throwable.getCause()) != null; advanceSlowPointer = !advanceSlowPointer) {
         throwable = cause;
         if (cause == slowPointer) {
            throw new IllegalArgumentException("Loop in causal chain detected.", cause);
         }

         if (advanceSlowPointer) {
            slowPointer = slowPointer.getCause();
         }
      }

      return throwable;
   }

   public static List<Throwable> getCausalChain(Throwable throwable) {
      Preconditions.checkNotNull(throwable);
      List<Throwable> causes = new ArrayList(4);
      causes.add(throwable);
      Throwable slowPointer = throwable;

      Throwable cause;
      for(boolean advanceSlowPointer = false; (cause = throwable.getCause()) != null; advanceSlowPointer = !advanceSlowPointer) {
         throwable = cause;
         causes.add(cause);
         if (cause == slowPointer) {
            throw new IllegalArgumentException("Loop in causal chain detected.", cause);
         }

         if (advanceSlowPointer) {
            slowPointer = slowPointer.getCause();
         }
      }

      return Collections.unmodifiableList(causes);
   }

   @CheckForNull
   @GwtIncompatible
   public static <X extends Throwable> X getCauseAs(Throwable throwable, Class<X> expectedCauseType) {
      try {
         return (Throwable)expectedCauseType.cast(throwable.getCause());
      } catch (ClassCastException var3) {
         var3.initCause(throwable);
         throw var3;
      }
   }

   @GwtIncompatible
   public static String getStackTraceAsString(Throwable throwable) {
      StringWriter stringWriter = new StringWriter();
      throwable.printStackTrace(new PrintWriter(stringWriter));
      return stringWriter.toString();
   }

   /** @deprecated */
   @Deprecated
   @GwtIncompatible
   public static List<StackTraceElement> lazyStackTrace(Throwable throwable) {
      return lazyStackTraceIsLazy() ? jlaStackTrace(throwable) : Collections.unmodifiableList(Arrays.asList(throwable.getStackTrace()));
   }

   /** @deprecated */
   @Deprecated
   @GwtIncompatible
   public static boolean lazyStackTraceIsLazy() {
      return getStackTraceElementMethod != null && getStackTraceDepthMethod != null;
   }

   @GwtIncompatible
   private static List<StackTraceElement> jlaStackTrace(final Throwable t) {
      Preconditions.checkNotNull(t);
      return new AbstractList<StackTraceElement>() {
         public StackTraceElement get(int n) {
            return (StackTraceElement)Throwables.invokeAccessibleNonThrowingMethod((Method)java.util.Objects.requireNonNull(Throwables.getStackTraceElementMethod), java.util.Objects.requireNonNull(Throwables.jla), t, n);
         }

         public int size() {
            return (Integer)Throwables.invokeAccessibleNonThrowingMethod((Method)java.util.Objects.requireNonNull(Throwables.getStackTraceDepthMethod), java.util.Objects.requireNonNull(Throwables.jla), t);
         }
      };
   }

   @GwtIncompatible
   private static Object invokeAccessibleNonThrowingMethod(Method method, Object receiver, Object... params) {
      try {
         return method.invoke(receiver, params);
      } catch (IllegalAccessException var4) {
         throw new RuntimeException(var4);
      } catch (InvocationTargetException var5) {
         throw propagate(var5.getCause());
      }
   }

   @CheckForNull
   @GwtIncompatible
   private static Object getJLA() {
      try {
         Class<?> sharedSecrets = Class.forName("sun.misc.SharedSecrets", false, (ClassLoader)null);
         Method langAccess = sharedSecrets.getMethod("getJavaLangAccess");
         return langAccess.invoke((Object)null);
      } catch (ThreadDeath var2) {
         throw var2;
      } catch (Throwable var3) {
         return null;
      }
   }

   @CheckForNull
   @GwtIncompatible
   private static Method getGetMethod() {
      return getJlaMethod("getStackTraceElement", Throwable.class, Integer.TYPE);
   }

   @CheckForNull
   @GwtIncompatible
   private static Method getSizeMethod(Object jla) {
      try {
         Method getStackTraceDepth = getJlaMethod("getStackTraceDepth", Throwable.class);
         if (getStackTraceDepth == null) {
            return null;
         } else {
            getStackTraceDepth.invoke(jla, new Throwable());
            return getStackTraceDepth;
         }
      } catch (IllegalAccessException | InvocationTargetException | UnsupportedOperationException var2) {
         return null;
      }
   }

   @CheckForNull
   @GwtIncompatible
   private static Method getJlaMethod(String name, Class<?>... parameterTypes) throws ThreadDeath {
      try {
         return Class.forName("sun.misc.JavaLangAccess", false, (ClassLoader)null).getMethod(name, parameterTypes);
      } catch (ThreadDeath var3) {
         throw var3;
      } catch (Throwable var4) {
         return null;
      }
   }

   static {
      getStackTraceElementMethod = jla == null ? null : getGetMethod();
      getStackTraceDepthMethod = jla == null ? null : getSizeMethod(jla);
   }
}
