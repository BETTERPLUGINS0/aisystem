package ac.grim.grimac.shaded.incendo.cloud.help;

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
   from = "HelpQuery",
   generator = "Immutables"
)
@Immutable
final class HelpQueryImpl<C> implements HelpQuery<C> {
   private final C sender;
   @NonNull
   private final String query;

   private HelpQueryImpl(C sender, @NonNull String query) {
      this.sender = Objects.requireNonNull(sender, "sender");
      this.query = (String)Objects.requireNonNull(query, "query");
   }

   private HelpQueryImpl(HelpQueryImpl<C> original, C sender, @NonNull String query) {
      this.sender = sender;
      this.query = query;
   }

   public C sender() {
      return this.sender;
   }

   @NonNull
   public String query() {
      return this.query;
   }

   public final HelpQueryImpl<C> withSender(C value) {
      if (this.sender == value) {
         return this;
      } else {
         C newValue = Objects.requireNonNull(value, "sender");
         return new HelpQueryImpl(this, newValue, this.query);
      }
   }

   public final HelpQueryImpl<C> withQuery(String value) {
      String newValue = (String)Objects.requireNonNull(value, "query");
      return this.query.equals(newValue) ? this : new HelpQueryImpl(this, this.sender, newValue);
   }

   public boolean equals(@Nullable Object another) {
      if (this == another) {
         return true;
      } else {
         return another instanceof HelpQueryImpl && this.equalTo(0, (HelpQueryImpl)another);
      }
   }

   private boolean equalTo(int synthetic, HelpQueryImpl<?> another) {
      return this.sender.equals(another.sender) && this.query.equals(another.query);
   }

   public int hashCode() {
      int h = 5381;
      int h = h + (h << 5) + this.sender.hashCode();
      h += (h << 5) + this.query.hashCode();
      return h;
   }

   public String toString() {
      return "HelpQuery{sender=" + this.sender + ", query=" + this.query + "}";
   }

   public static <C> HelpQueryImpl<C> of(C sender, @NonNull String query) {
      return new HelpQueryImpl(sender, query);
   }

   public static <C> HelpQueryImpl<C> copyOf(HelpQuery<C> instance) {
      return instance instanceof HelpQueryImpl ? (HelpQueryImpl)instance : of(instance.sender(), instance.query());
   }
}
