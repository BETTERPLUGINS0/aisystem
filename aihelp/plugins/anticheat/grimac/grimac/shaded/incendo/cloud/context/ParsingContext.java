package ac.grim.grimac.shaded.incendo.cloud.context;

import ac.grim.grimac.shaded.incendo.cloud.component.CommandComponent;
import java.time.Duration;
import java.util.Objects;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

@API(
   status = Status.MAINTAINED
)
public final class ParsingContext<C> {
   private final CommandComponent<C> component;
   @Nullable
   private String consumed = null;
   private long startTime = -1L;
   private long endTime = -1L;
   private int consumedFrom = -1;
   private int consumedTo = -1;
   private boolean success;

   @API(
      status = Status.INTERNAL,
      consumers = {"ac.grim.grimac.shaded.incendo.cloud.*"}
   )
   public ParsingContext(@NonNull final CommandComponent<C> component) {
      this.component = component;
   }

   @NonNull
   public CommandComponent<C> component() {
      return this.component;
   }

   @NonNull
   public Duration parseDuration() {
      if (this.startTime < 0L) {
         throw new IllegalStateException("No start time has been registered");
      } else if (this.endTime < 0L) {
         throw new IllegalStateException("No end time has been registered");
      } else {
         return Duration.ofNanos(this.endTime - this.startTime);
      }
   }

   @API(
      status = Status.INTERNAL,
      consumers = {"ac.grim.grimac.shaded.incendo.cloud.*"}
   )
   public void markStart() {
      this.startTime = System.nanoTime();
   }

   @API(
      status = Status.INTERNAL,
      consumers = {"ac.grim.grimac.shaded.incendo.cloud.*"}
   )
   public void markEnd() {
      this.endTime = System.nanoTime();
   }

   long startTime() {
      return this.startTime;
   }

   long endTime() {
      return this.endTime;
   }

   public boolean success() {
      return this.success;
   }

   @API(
      status = Status.INTERNAL,
      consumers = {"ac.grim.grimac.shaded.incendo.cloud.*"}
   )
   public void success(final boolean success) {
      this.success = success;
   }

   @API(
      status = Status.INTERNAL,
      consumers = {"ac.grim.grimac.shaded.incendo.cloud.*"}
   )
   public void consumedInput(@NonNull final CommandInput original, @NonNull final CommandInput postParse) {
      if (this.consumed != null) {
         throw new IllegalStateException();
      } else {
         this.consumed = original.difference(postParse);
         this.consumedFrom = original.cursor();
         this.consumedTo = original.cursor() + this.consumed.length();
      }
   }

   @API(
      status = Status.STABLE
   )
   @NonNull
   public String consumedInput() {
      return (String)Objects.requireNonNull(this.consumed);
   }

   @Nullable
   public String exactAlias() {
      return this.success && this.component.type() == CommandComponent.ComponentType.LITERAL ? this.consumed : null;
   }

   @API(
      status = Status.STABLE
   )
   public int consumedFrom() {
      return this.consumedFrom;
   }

   @API(
      status = Status.STABLE
   )
   public int consumedTo() {
      return this.consumedTo;
   }
}
