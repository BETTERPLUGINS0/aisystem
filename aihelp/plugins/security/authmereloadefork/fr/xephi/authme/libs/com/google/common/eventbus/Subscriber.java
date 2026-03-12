package fr.xephi.authme.libs.com.google.common.eventbus;

import fr.xephi.authme.libs.com.google.common.annotations.VisibleForTesting;
import fr.xephi.authme.libs.com.google.common.base.Preconditions;
import fr.xephi.authme.libs.com.google.j2objc.annotations.Weak;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.Executor;
import javax.annotation.CheckForNull;

@ElementTypesAreNonnullByDefault
class Subscriber {
   @Weak
   private EventBus bus;
   @VisibleForTesting
   final Object target;
   private final Method method;
   private final Executor executor;

   static Subscriber create(EventBus bus, Object listener, Method method) {
      return (Subscriber)(isDeclaredThreadSafe(method) ? new Subscriber(bus, listener, method) : new Subscriber.SynchronizedSubscriber(bus, listener, method));
   }

   private Subscriber(EventBus bus, Object target, Method method) {
      this.bus = bus;
      this.target = Preconditions.checkNotNull(target);
      this.method = method;
      method.setAccessible(true);
      this.executor = bus.executor();
   }

   final void dispatchEvent(Object event) {
      this.executor.execute(() -> {
         try {
            this.invokeSubscriberMethod(event);
         } catch (InvocationTargetException var3) {
            this.bus.handleSubscriberException(var3.getCause(), this.context(event));
         }

      });
   }

   @VisibleForTesting
   void invokeSubscriberMethod(Object event) throws InvocationTargetException {
      String var3;
      try {
         this.method.invoke(this.target, Preconditions.checkNotNull(event));
      } catch (IllegalArgumentException var4) {
         var3 = String.valueOf(event);
         throw new Error((new StringBuilder(33 + String.valueOf(var3).length())).append("Method rejected target/argument: ").append(var3).toString(), var4);
      } catch (IllegalAccessException var5) {
         var3 = String.valueOf(event);
         throw new Error((new StringBuilder(28 + String.valueOf(var3).length())).append("Method became inaccessible: ").append(var3).toString(), var5);
      } catch (InvocationTargetException var6) {
         if (var6.getCause() instanceof Error) {
            throw (Error)var6.getCause();
         } else {
            throw var6;
         }
      }
   }

   private SubscriberExceptionContext context(Object event) {
      return new SubscriberExceptionContext(this.bus, event, this.target, this.method);
   }

   public final int hashCode() {
      return (31 + this.method.hashCode()) * 31 + System.identityHashCode(this.target);
   }

   public final boolean equals(@CheckForNull Object obj) {
      if (!(obj instanceof Subscriber)) {
         return false;
      } else {
         Subscriber that = (Subscriber)obj;
         return this.target == that.target && this.method.equals(that.method);
      }
   }

   private static boolean isDeclaredThreadSafe(Method method) {
      return method.getAnnotation(AllowConcurrentEvents.class) != null;
   }

   // $FF: synthetic method
   Subscriber(EventBus x0, Object x1, Method x2, Object x3) {
      this(x0, x1, x2);
   }

   @VisibleForTesting
   static final class SynchronizedSubscriber extends Subscriber {
      private SynchronizedSubscriber(EventBus bus, Object target, Method method) {
         super(bus, target, method, null);
      }

      void invokeSubscriberMethod(Object event) throws InvocationTargetException {
         synchronized(this) {
            super.invokeSubscriberMethod(event);
         }
      }

      // $FF: synthetic method
      SynchronizedSubscriber(EventBus x0, Object x1, Method x2, Object x3) {
         this(x0, x1, x2);
      }
   }
}
