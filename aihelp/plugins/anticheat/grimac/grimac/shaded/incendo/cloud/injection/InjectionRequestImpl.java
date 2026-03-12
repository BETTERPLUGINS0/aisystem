package ac.grim.grimac.shaded.incendo.cloud.injection;

import ac.grim.grimac.shaded.geantyref.TypeToken;
import ac.grim.grimac.shaded.incendo.cloud.context.CommandContext;
import ac.grim.grimac.shaded.incendo.cloud.util.annotation.AnnotationAccessor;
import java.util.Objects;
import javax.annotation.CheckReturnValue;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.Immutable;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.immutables.value.Generated;

@ParametersAreNonnullByDefault
@CheckReturnValue
@API(
   status = Status.INTERNAL,
   consumers = {"ac.grim.grimac.shaded.incendo.cloud.*"}
)
@Generated(
   from = "InjectionRequest",
   generator = "Immutables"
)
@Immutable
final class InjectionRequestImpl<C> implements InjectionRequest<C> {
   @NonNull
   private final CommandContext<C> commandContext;
   @NonNull
   private final TypeToken<?> injectedType;
   @NonNull
   private final transient Class<?> injectedClass;
   @NonNull
   private final AnnotationAccessor annotationAccessor;

   private InjectionRequestImpl(@NonNull CommandContext<C> commandContext, @NonNull TypeToken<?> injectedType, @NonNull AnnotationAccessor annotationAccessor) {
      this.commandContext = (CommandContext)Objects.requireNonNull(commandContext, "commandContext");
      this.injectedType = (TypeToken)Objects.requireNonNull(injectedType, "injectedType");
      this.annotationAccessor = (AnnotationAccessor)Objects.requireNonNull(annotationAccessor, "annotationAccessor");
      this.injectedClass = (Class)Objects.requireNonNull(InjectionRequest.super.injectedClass(), "injectedClass");
   }

   private InjectionRequestImpl(InjectionRequestImpl<C> original, @NonNull CommandContext<C> commandContext, @NonNull TypeToken<?> injectedType, @NonNull AnnotationAccessor annotationAccessor) {
      this.commandContext = commandContext;
      this.injectedType = injectedType;
      this.annotationAccessor = annotationAccessor;
      this.injectedClass = (Class)Objects.requireNonNull(InjectionRequest.super.injectedClass(), "injectedClass");
   }

   @NonNull
   public CommandContext<C> commandContext() {
      return this.commandContext;
   }

   @NonNull
   public TypeToken<?> injectedType() {
      return this.injectedType;
   }

   @NonNull
   public Class<?> injectedClass() {
      return this.injectedClass;
   }

   @NonNull
   public AnnotationAccessor annotationAccessor() {
      return this.annotationAccessor;
   }

   public final InjectionRequestImpl<C> withCommandContext(CommandContext<C> value) {
      if (this.commandContext == value) {
         return this;
      } else {
         CommandContext<C> newValue = (CommandContext)Objects.requireNonNull(value, "commandContext");
         return new InjectionRequestImpl(this, newValue, this.injectedType, this.annotationAccessor);
      }
   }

   public final InjectionRequestImpl<C> withInjectedType(TypeToken<?> value) {
      if (this.injectedType == value) {
         return this;
      } else {
         TypeToken<?> newValue = (TypeToken)Objects.requireNonNull(value, "injectedType");
         return new InjectionRequestImpl(this, this.commandContext, newValue, this.annotationAccessor);
      }
   }

   public final InjectionRequestImpl<C> withAnnotationAccessor(AnnotationAccessor value) {
      if (this.annotationAccessor == value) {
         return this;
      } else {
         AnnotationAccessor newValue = (AnnotationAccessor)Objects.requireNonNull(value, "annotationAccessor");
         return new InjectionRequestImpl(this, this.commandContext, this.injectedType, newValue);
      }
   }

   public boolean equals(@Nullable Object another) {
      if (this == another) {
         return true;
      } else {
         return another instanceof InjectionRequestImpl && this.equalTo(0, (InjectionRequestImpl)another);
      }
   }

   private boolean equalTo(int synthetic, InjectionRequestImpl<?> another) {
      return this.commandContext.equals(another.commandContext) && this.injectedType.equals(another.injectedType) && this.injectedClass.equals(another.injectedClass) && this.annotationAccessor.equals(another.annotationAccessor);
   }

   public int hashCode() {
      int h = 5381;
      int h = h + (h << 5) + this.commandContext.hashCode();
      h += (h << 5) + this.injectedType.hashCode();
      h += (h << 5) + this.injectedClass.hashCode();
      h += (h << 5) + this.annotationAccessor.hashCode();
      return h;
   }

   public String toString() {
      return "InjectionRequest{commandContext=" + this.commandContext + ", injectedType=" + this.injectedType + ", injectedClass=" + this.injectedClass + ", annotationAccessor=" + this.annotationAccessor + "}";
   }

   public static <C> InjectionRequestImpl<C> of(@NonNull CommandContext<C> commandContext, @NonNull TypeToken<?> injectedType, @NonNull AnnotationAccessor annotationAccessor) {
      return new InjectionRequestImpl(commandContext, injectedType, annotationAccessor);
   }

   public static <C> InjectionRequestImpl<C> copyOf(InjectionRequest<C> instance) {
      return instance instanceof InjectionRequestImpl ? (InjectionRequestImpl)instance : of(instance.commandContext(), instance.injectedType(), instance.annotationAccessor());
   }
}
