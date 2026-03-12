package ac.grim.grimac.shaded.incendo.cloud.exception.handling;

import ac.grim.grimac.shaded.geantyref.TypeToken;
import java.util.function.Predicate;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.checkerframework.checker.nullness.qual.NonNull;

@API(
   status = Status.STABLE
)
public final class ExceptionHandlerRegistration<C, T extends Throwable> {
   private final TypeToken<T> exceptionType;
   private final ExceptionHandler<C, ? extends T> exceptionHandler;
   private final Predicate<T> exceptionFilter;

   @NonNull
   public static <C, T extends Throwable> ExceptionHandlerRegistration<C, ? extends T> of(@NonNull final TypeToken<T> exceptionType, @NonNull final ExceptionHandler<C, ? extends T> exceptionHandler) {
      return builder(exceptionType).exceptionHandler(exceptionHandler).build();
   }

   @NonNull
   public static <C, T extends Throwable> ExceptionHandlerRegistration.ExceptionControllerBuilder<C, T> builder(@NonNull final TypeToken<T> exceptionType) {
      return new ExceptionHandlerRegistration.ExceptionControllerBuilder(exceptionType);
   }

   private ExceptionHandlerRegistration(@NonNull final TypeToken<T> exceptionType, @NonNull final ExceptionHandler<C, ? extends T> exceptionHandler, @NonNull final Predicate<T> exceptionFilter) {
      this.exceptionType = exceptionType;
      this.exceptionHandler = exceptionHandler;
      this.exceptionFilter = exceptionFilter;
   }

   @NonNull
   public TypeToken<T> exceptionType() {
      return this.exceptionType;
   }

   @NonNull
   public ExceptionHandler<C, ? extends T> exceptionHandler() {
      return this.exceptionHandler;
   }

   @NonNull
   public Predicate<T> exceptionFilter() {
      return this.exceptionFilter;
   }

   // $FF: synthetic method
   ExceptionHandlerRegistration(TypeToken x0, ExceptionHandler x1, Predicate x2, Object x3) {
      this(x0, x1, x2);
   }

   @API(
      status = Status.STABLE
   )
   public static final class ExceptionControllerBuilder<C, T extends Throwable> {
      private final TypeToken<T> exceptionType;
      private final ExceptionHandler<C, ? extends T> exceptionHandler;
      private final Predicate<T> exceptionFilter;

      private ExceptionControllerBuilder(@NonNull final TypeToken<T> exceptionType, @NonNull final ExceptionHandler<C, ? extends T> exceptionHandler, @NonNull final Predicate<T> exceptionFilter) {
         this.exceptionType = exceptionType;
         this.exceptionHandler = exceptionHandler;
         this.exceptionFilter = exceptionFilter;
      }

      private ExceptionControllerBuilder(@NonNull final TypeToken<T> exceptionType) {
         this(exceptionType, ExceptionHandler.noopHandler(), (exception) -> {
            return true;
         });
      }

      @NonNull
      public ExceptionHandlerRegistration.ExceptionControllerBuilder<C, T> exceptionHandler(@NonNull final ExceptionHandler<C, ? extends T> exceptionHandler) {
         return new ExceptionHandlerRegistration.ExceptionControllerBuilder(this.exceptionType, exceptionHandler, this.exceptionFilter);
      }

      @NonNull
      public ExceptionHandlerRegistration.ExceptionControllerBuilder<C, T> exceptionFilter(@NonNull final Predicate<T> exceptionFilter) {
         return new ExceptionHandlerRegistration.ExceptionControllerBuilder(this.exceptionType, this.exceptionHandler, exceptionFilter);
      }

      @NonNull
      public ExceptionHandlerRegistration<C, ? extends T> build() {
         return new ExceptionHandlerRegistration(this.exceptionType, this.exceptionHandler, this.exceptionFilter);
      }

      // $FF: synthetic method
      ExceptionControllerBuilder(TypeToken x0, Object x1) {
         this(x0);
      }
   }

   @FunctionalInterface
   @API(
      status = Status.STABLE
   )
   public interface BuilderDecorator<C, T extends Throwable> {
      @NonNull
      ExceptionHandlerRegistration.ExceptionControllerBuilder<C, T> decorate(@NonNull ExceptionHandlerRegistration.ExceptionControllerBuilder<C, T> builder);
   }
}
