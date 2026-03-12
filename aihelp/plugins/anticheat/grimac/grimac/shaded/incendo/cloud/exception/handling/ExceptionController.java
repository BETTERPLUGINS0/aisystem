package ac.grim.grimac.shaded.incendo.cloud.exception.handling;

import ac.grim.grimac.shaded.geantyref.TypeToken;
import ac.grim.grimac.shaded.incendo.cloud.context.CommandContext;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletionException;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.common.returnsreceiver.qual.This;

@API(
   status = Status.STABLE
)
public final class ExceptionController<C> {
   private final ExceptionContextFactory<C> exceptionContextFactory = new ExceptionContextFactory(this);
   private final Map<Type, LinkedList<ExceptionHandlerRegistration<C, ?>>> registrations = new HashMap();

   @NonNull
   public static Throwable unwrapCompletionException(@NonNull final Throwable throwable) {
      return throwable instanceof CompletionException ? unwrapCompletionException(throwable.getCause()) : throwable;
   }

   public <T extends Throwable> void handleException(@NonNull final CommandContext<C> commandContext, @NonNull final T exception) throws Throwable {
      ExceptionContext<C, T> exceptionContext = this.exceptionContextFactory.createContext(commandContext, exception);
      Class exceptionClass = exception.getClass();

      label35:
      while(exceptionClass != Object.class) {
         List<ExceptionHandlerRegistration<C, ?>> registrations = this.registrations(exceptionClass);
         Iterator var6 = registrations.iterator();

         while(true) {
            ExceptionHandlerRegistration registration;
            do {
               if (!var6.hasNext()) {
                  exceptionClass = exceptionClass.getSuperclass();
                  continue label35;
               }

               registration = (ExceptionHandlerRegistration)var6.next();
            } while(!registration.exceptionFilter().test(exception));

            try {
               registration.exceptionHandler().handle(exceptionContext);
               return;
            } catch (Throwable var9) {
               if (!var9.equals(exception)) {
                  this.handleException(commandContext, var9);
                  return;
               }
            }
         }
      }

      throw exception;
   }

   @This
   @NonNull
   public synchronized <T extends Throwable> ExceptionController<C> register(@NonNull final ExceptionHandlerRegistration<C, ? extends T> registration) {
      ((LinkedList)this.registrations.computeIfAbsent(registration.exceptionType().getType(), (t) -> {
         return new LinkedList();
      })).addFirst(registration);
      return this;
   }

   @This
   @NonNull
   public <T extends Throwable> ExceptionController<C> register(@NonNull final TypeToken<T> exceptionType, @NonNull final ExceptionHandlerRegistration.BuilderDecorator<C, T> decorator) {
      return this.register(decorator.decorate(ExceptionHandlerRegistration.builder(exceptionType)).build());
   }

   @This
   @NonNull
   public <T extends Throwable> ExceptionController<C> register(@NonNull final Class<T> exceptionType, @NonNull final ExceptionHandlerRegistration.BuilderDecorator<C, T> decorator) {
      return this.register(decorator.decorate(ExceptionHandlerRegistration.builder(TypeToken.get(exceptionType))).build());
   }

   @This
   @NonNull
   public <T extends Throwable> ExceptionController<C> registerHandler(@NonNull final TypeToken<T> exceptionType, @NonNull final ExceptionHandler<C, ? extends T> exceptionHandler) {
      return this.register(ExceptionHandlerRegistration.of(exceptionType, exceptionHandler));
   }

   @This
   @NonNull
   public <T extends Throwable> ExceptionController<C> registerHandler(@NonNull final Class<T> exceptionType, @NonNull final ExceptionHandler<C, ? extends T> exceptionHandler) {
      return this.register(ExceptionHandlerRegistration.of(TypeToken.get(exceptionType), exceptionHandler));
   }

   public void clearHandlers() {
      this.registrations.clear();
   }

   @NonNull
   private List<ExceptionHandlerRegistration<C, ?>> registrations(@NonNull final Type type) {
      return Collections.unmodifiableList((List)this.registrations.getOrDefault(type, new LinkedList()));
   }
}
