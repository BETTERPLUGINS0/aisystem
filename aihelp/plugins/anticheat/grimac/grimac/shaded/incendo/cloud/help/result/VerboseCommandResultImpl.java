package ac.grim.grimac.shaded.incendo.cloud.help.result;

import ac.grim.grimac.shaded.incendo.cloud.help.HelpQuery;
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
   from = "VerboseCommandResult",
   generator = "Immutables"
)
@Immutable
final class VerboseCommandResultImpl<C> implements VerboseCommandResult<C> {
   @NonNull
   private final HelpQuery<C> query;
   @NonNull
   private final CommandEntry<C> entry;

   private VerboseCommandResultImpl(@NonNull HelpQuery<C> query, @NonNull CommandEntry<C> entry) {
      this.query = (HelpQuery)Objects.requireNonNull(query, "query");
      this.entry = (CommandEntry)Objects.requireNonNull(entry, "entry");
   }

   private VerboseCommandResultImpl(VerboseCommandResultImpl<C> original, @NonNull HelpQuery<C> query, @NonNull CommandEntry<C> entry) {
      this.query = query;
      this.entry = entry;
   }

   @NonNull
   public HelpQuery<C> query() {
      return this.query;
   }

   @NonNull
   public CommandEntry<C> entry() {
      return this.entry;
   }

   public final VerboseCommandResultImpl<C> withQuery(HelpQuery<C> value) {
      if (this.query == value) {
         return this;
      } else {
         HelpQuery<C> newValue = (HelpQuery)Objects.requireNonNull(value, "query");
         return new VerboseCommandResultImpl(this, newValue, this.entry);
      }
   }

   public final VerboseCommandResultImpl<C> withEntry(CommandEntry<C> value) {
      if (this.entry == value) {
         return this;
      } else {
         CommandEntry<C> newValue = (CommandEntry)Objects.requireNonNull(value, "entry");
         return new VerboseCommandResultImpl(this, this.query, newValue);
      }
   }

   public boolean equals(@Nullable Object another) {
      if (this == another) {
         return true;
      } else {
         return another instanceof VerboseCommandResultImpl && this.equalTo(0, (VerboseCommandResultImpl)another);
      }
   }

   private boolean equalTo(int synthetic, VerboseCommandResultImpl<?> another) {
      return this.query.equals(another.query) && this.entry.equals(another.entry);
   }

   public int hashCode() {
      int h = 5381;
      int h = h + (h << 5) + this.query.hashCode();
      h += (h << 5) + this.entry.hashCode();
      return h;
   }

   public String toString() {
      return "VerboseCommandResult{query=" + this.query + ", entry=" + this.entry + "}";
   }

   public static <C> VerboseCommandResultImpl<C> of(@NonNull HelpQuery<C> query, @NonNull CommandEntry<C> entry) {
      return new VerboseCommandResultImpl(query, entry);
   }

   public static <C> VerboseCommandResultImpl<C> copyOf(VerboseCommandResult<C> instance) {
      return instance instanceof VerboseCommandResultImpl ? (VerboseCommandResultImpl)instance : of(instance.query(), instance.entry());
   }
}
