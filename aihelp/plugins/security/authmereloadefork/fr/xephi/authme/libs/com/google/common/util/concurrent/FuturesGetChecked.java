package fr.xephi.authme.libs.com.google.common.util.concurrent;

import fr.xephi.authme.libs.com.google.common.annotations.GwtIncompatible;
import fr.xephi.authme.libs.com.google.common.annotations.VisibleForTesting;
import fr.xephi.authme.libs.com.google.common.base.Function;
import fr.xephi.authme.libs.com.google.common.base.Preconditions;
import fr.xephi.authme.libs.com.google.common.collect.Ordering;
import fr.xephi.authme.libs.com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.lang.ref.WeakReference;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import javax.annotation.CheckForNull;

@ElementTypesAreNonnullByDefault
@GwtIncompatible
final class FuturesGetChecked {
   private static final Ordering<Constructor<?>> WITH_STRING_PARAM_FIRST = Ordering.natural().onResultOf(new Function<Constructor<?>, Boolean>() {
      public Boolean apply(Constructor<?> input) {
         return Arrays.asList(input.getParameterTypes()).contains(String.class);
      }
   }).reverse();

   @ParametricNullness
   @CanIgnoreReturnValue
   static <V, X extends Exception> V getChecked(Future<V> future, Class<X> exceptionClass) throws X {
      return getChecked(bestGetCheckedTypeValidator(), future, exceptionClass);
   }

   @ParametricNullness
   @CanIgnoreReturnValue
   @VisibleForTesting
   static <V, X extends Exception> V getChecked(FuturesGetChecked.GetCheckedTypeValidator validator, Future<V> future, Class<X> exceptionClass) throws X {
      validator.validateClass(exceptionClass);

      try {
         return future.get();
      } catch (InterruptedException var4) {
         Thread.currentThread().interrupt();
         throw newWithCause(exceptionClass, var4);
      } catch (ExecutionException var5) {
         wrapAndThrowExceptionOrError(var5.getCause(), exceptionClass);
         throw new AssertionError();
      }
   }

   @ParametricNullness
   @CanIgnoreReturnValue
   static <V, X extends Exception> V getChecked(Future<V> future, Class<X> exceptionClass, long timeout, TimeUnit unit) throws X {
      bestGetCheckedTypeValidator().validateClass(exceptionClass);

      try {
         return future.get(timeout, unit);
      } catch (InterruptedException var6) {
         Thread.currentThread().interrupt();
         throw newWithCause(exceptionClass, var6);
      } catch (TimeoutException var7) {
         throw newWithCause(exceptionClass, var7);
      } catch (ExecutionException var8) {
         wrapAndThrowExceptionOrError(var8.getCause(), exceptionClass);
         throw new AssertionError();
      }
   }

   private static FuturesGetChecked.GetCheckedTypeValidator bestGetCheckedTypeValidator() {
      return FuturesGetChecked.GetCheckedTypeValidatorHolder.BEST_VALIDATOR;
   }

   @VisibleForTesting
   static FuturesGetChecked.GetCheckedTypeValidator weakSetValidator() {
      return FuturesGetChecked.GetCheckedTypeValidatorHolder.WeakSetValidator.INSTANCE;
   }

   @VisibleForTesting
   static FuturesGetChecked.GetCheckedTypeValidator classValueValidator() {
      return FuturesGetChecked.GetCheckedTypeValidatorHolder.ClassValueValidator.INSTANCE;
   }

   private static <X extends Exception> void wrapAndThrowExceptionOrError(Throwable cause, Class<X> exceptionClass) throws X {
      if (cause instanceof Error) {
         throw new ExecutionError((Error)cause);
      } else if (cause instanceof RuntimeException) {
         throw new UncheckedExecutionException(cause);
      } else {
         throw newWithCause(exceptionClass, cause);
      }
   }

   private static boolean hasConstructorUsableByGetChecked(Class<? extends Exception> exceptionClass) {
      try {
         newWithCause(exceptionClass, new Exception());
         return true;
      } catch (Exception var2) {
         return false;
      }
   }

   private static <X extends Exception> X newWithCause(Class<X> exceptionClass, Throwable cause) {
      List<Constructor<X>> constructors = Arrays.asList(exceptionClass.getConstructors());
      Iterator var3 = preferringStrings(constructors).iterator();

      Exception instance;
      do {
         if (!var3.hasNext()) {
            String var6 = String.valueOf(exceptionClass);
            throw new IllegalArgumentException((new StringBuilder(82 + String.valueOf(var6).length())).append("No appropriate constructor for exception of type ").append(var6).append(" in response to chained exception").toString(), cause);
         }

         Constructor<X> constructor = (Constructor)var3.next();
         instance = (Exception)newFromConstructor(constructor, cause);
      } while(instance == null);

      if (instance.getCause() == null) {
         instance.initCause(cause);
      }

      return instance;
   }

   private static <X extends Exception> List<Constructor<X>> preferringStrings(List<Constructor<X>> constructors) {
      return WITH_STRING_PARAM_FIRST.sortedCopy(constructors);
   }

   @CheckForNull
   private static <X> X newFromConstructor(Constructor<X> constructor, Throwable cause) {
      Class<?>[] paramTypes = constructor.getParameterTypes();
      Object[] params = new Object[paramTypes.length];

      for(int i = 0; i < paramTypes.length; ++i) {
         Class<?> paramType = paramTypes[i];
         if (paramType.equals(String.class)) {
            params[i] = cause.toString();
         } else {
            if (!paramType.equals(Throwable.class)) {
               return null;
            }

            params[i] = cause;
         }
      }

      try {
         return constructor.newInstance(params);
      } catch (InstantiationException | IllegalAccessException | InvocationTargetException | IllegalArgumentException var6) {
         return null;
      }
   }

   @VisibleForTesting
   static boolean isCheckedException(Class<? extends Exception> type) {
      return !RuntimeException.class.isAssignableFrom(type);
   }

   @VisibleForTesting
   static void checkExceptionClassValidity(Class<? extends Exception> exceptionClass) {
      Preconditions.checkArgument(isCheckedException(exceptionClass), "Futures.getChecked exception type (%s) must not be a RuntimeException", (Object)exceptionClass);
      Preconditions.checkArgument(hasConstructorUsableByGetChecked(exceptionClass), "Futures.getChecked exception type (%s) must be an accessible class with an accessible constructor whose parameters (if any) must be of type String and/or Throwable", (Object)exceptionClass);
   }

   private FuturesGetChecked() {
   }

   @VisibleForTesting
   static class GetCheckedTypeValidatorHolder {
      static final String CLASS_VALUE_VALIDATOR_NAME = String.valueOf(FuturesGetChecked.GetCheckedTypeValidatorHolder.class.getName()).concat("$ClassValueValidator");
      static final FuturesGetChecked.GetCheckedTypeValidator BEST_VALIDATOR = getBestValidator();

      static FuturesGetChecked.GetCheckedTypeValidator getBestValidator() {
         try {
            Class<? extends Enum> theClass = Class.forName(CLASS_VALUE_VALIDATOR_NAME).asSubclass(Enum.class);
            return (FuturesGetChecked.GetCheckedTypeValidator)((Enum[])theClass.getEnumConstants())[0];
         } catch (Throwable var1) {
            return FuturesGetChecked.weakSetValidator();
         }
      }

      static enum WeakSetValidator implements FuturesGetChecked.GetCheckedTypeValidator {
         INSTANCE;

         private static final Set<WeakReference<Class<? extends Exception>>> validClasses = new CopyOnWriteArraySet();

         public void validateClass(Class<? extends Exception> exceptionClass) {
            Iterator var2 = validClasses.iterator();

            WeakReference knownGood;
            do {
               if (!var2.hasNext()) {
                  FuturesGetChecked.checkExceptionClassValidity(exceptionClass);
                  if (validClasses.size() > 1000) {
                     validClasses.clear();
                  }

                  validClasses.add(new WeakReference(exceptionClass));
                  return;
               }

               knownGood = (WeakReference)var2.next();
            } while(!exceptionClass.equals(knownGood.get()));

         }

         // $FF: synthetic method
         private static FuturesGetChecked.GetCheckedTypeValidatorHolder.WeakSetValidator[] $values() {
            return new FuturesGetChecked.GetCheckedTypeValidatorHolder.WeakSetValidator[]{INSTANCE};
         }
      }

      static enum ClassValueValidator implements FuturesGetChecked.GetCheckedTypeValidator {
         INSTANCE;

         private static final ClassValue<Boolean> isValidClass = new ClassValue<Boolean>() {
            protected Boolean computeValue(Class<?> type) {
               FuturesGetChecked.checkExceptionClassValidity(type.asSubclass(Exception.class));
               return true;
            }
         };

         public void validateClass(Class<? extends Exception> exceptionClass) {
            isValidClass.get(exceptionClass);
         }

         // $FF: synthetic method
         private static FuturesGetChecked.GetCheckedTypeValidatorHolder.ClassValueValidator[] $values() {
            return new FuturesGetChecked.GetCheckedTypeValidatorHolder.ClassValueValidator[]{INSTANCE};
         }
      }
   }

   @VisibleForTesting
   interface GetCheckedTypeValidator {
      void validateClass(Class<? extends Exception> var1);
   }
}
