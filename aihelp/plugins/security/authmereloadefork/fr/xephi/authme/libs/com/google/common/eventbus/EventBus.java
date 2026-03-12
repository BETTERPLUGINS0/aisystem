package fr.xephi.authme.libs.com.google.common.eventbus;

import fr.xephi.authme.libs.com.google.common.base.MoreObjects;
import fr.xephi.authme.libs.com.google.common.base.Preconditions;
import fr.xephi.authme.libs.com.google.common.util.concurrent.MoreExecutors;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Locale;
import java.util.concurrent.Executor;
import java.util.logging.Level;
import java.util.logging.Logger;

@ElementTypesAreNonnullByDefault
public class EventBus {
   private static final Logger logger = Logger.getLogger(EventBus.class.getName());
   private final String identifier;
   private final Executor executor;
   private final SubscriberExceptionHandler exceptionHandler;
   private final SubscriberRegistry subscribers;
   private final Dispatcher dispatcher;

   public EventBus() {
      this("default");
   }

   public EventBus(String identifier) {
      this(identifier, MoreExecutors.directExecutor(), Dispatcher.perThreadDispatchQueue(), EventBus.LoggingHandler.INSTANCE);
   }

   public EventBus(SubscriberExceptionHandler exceptionHandler) {
      this("default", MoreExecutors.directExecutor(), Dispatcher.perThreadDispatchQueue(), exceptionHandler);
   }

   EventBus(String identifier, Executor executor, Dispatcher dispatcher, SubscriberExceptionHandler exceptionHandler) {
      this.subscribers = new SubscriberRegistry(this);
      this.identifier = (String)Preconditions.checkNotNull(identifier);
      this.executor = (Executor)Preconditions.checkNotNull(executor);
      this.dispatcher = (Dispatcher)Preconditions.checkNotNull(dispatcher);
      this.exceptionHandler = (SubscriberExceptionHandler)Preconditions.checkNotNull(exceptionHandler);
   }

   public final String identifier() {
      return this.identifier;
   }

   final Executor executor() {
      return this.executor;
   }

   void handleSubscriberException(Throwable e, SubscriberExceptionContext context) {
      Preconditions.checkNotNull(e);
      Preconditions.checkNotNull(context);

      try {
         this.exceptionHandler.handleException(e, context);
      } catch (Throwable var4) {
         logger.log(Level.SEVERE, String.format(Locale.ROOT, "Exception %s thrown while handling exception: %s", var4, e), var4);
      }

   }

   public void register(Object object) {
      this.subscribers.register(object);
   }

   public void unregister(Object object) {
      this.subscribers.unregister(object);
   }

   public void post(Object event) {
      Iterator<Subscriber> eventSubscribers = this.subscribers.getSubscribers(event);
      if (eventSubscribers.hasNext()) {
         this.dispatcher.dispatch(event, eventSubscribers);
      } else if (!(event instanceof DeadEvent)) {
         this.post(new DeadEvent(this, event));
      }

   }

   public String toString() {
      return MoreObjects.toStringHelper((Object)this).addValue(this.identifier).toString();
   }

   static final class LoggingHandler implements SubscriberExceptionHandler {
      static final EventBus.LoggingHandler INSTANCE = new EventBus.LoggingHandler();

      public void handleException(Throwable exception, SubscriberExceptionContext context) {
         Logger logger = logger(context);
         if (logger.isLoggable(Level.SEVERE)) {
            logger.log(Level.SEVERE, message(context), exception);
         }

      }

      private static Logger logger(SubscriberExceptionContext context) {
         String var1 = EventBus.class.getName();
         String var2 = context.getEventBus().identifier();
         return Logger.getLogger((new StringBuilder(1 + String.valueOf(var1).length() + String.valueOf(var2).length())).append(var1).append(".").append(var2).toString());
      }

      private static String message(SubscriberExceptionContext context) {
         Method method = context.getSubscriberMethod();
         String var2 = method.getName();
         String var3 = method.getParameterTypes()[0].getName();
         String var4 = String.valueOf(context.getSubscriber());
         String var5 = String.valueOf(context.getEvent());
         return (new StringBuilder(80 + String.valueOf(var2).length() + String.valueOf(var3).length() + String.valueOf(var4).length() + String.valueOf(var5).length())).append("Exception thrown by subscriber method ").append(var2).append('(').append(var3).append(')').append(" on subscriber ").append(var4).append(" when dispatching event: ").append(var5).toString();
      }
   }
}
