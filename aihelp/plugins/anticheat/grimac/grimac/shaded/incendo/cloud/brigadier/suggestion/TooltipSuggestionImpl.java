package ac.grim.grimac.shaded.incendo.cloud.brigadier.suggestion;

import com.mojang.brigadier.Message;
import java.util.Objects;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.immutables.value.Generated;

@API(
   status = Status.INTERNAL,
   consumers = {"ac.grim.grimac.shaded.incendo.cloud.*"}
)
@Generated(
   from = "TooltipSuggestion",
   generator = "Immutables"
)
final class TooltipSuggestionImpl implements TooltipSuggestion {
   @NonNull
   private final String suggestion;
   @Nullable
   private final Message tooltip;

   private TooltipSuggestionImpl(@NonNull String suggestion, @Nullable Message tooltip) {
      this.suggestion = (String)Objects.requireNonNull(suggestion, "suggestion");
      this.tooltip = tooltip;
   }

   private TooltipSuggestionImpl(TooltipSuggestionImpl original, @NonNull String suggestion, @Nullable Message tooltip) {
      this.suggestion = suggestion;
      this.tooltip = tooltip;
   }

   @NonNull
   public String suggestion() {
      return this.suggestion;
   }

   @Nullable
   public Message tooltip() {
      return this.tooltip;
   }

   public final TooltipSuggestionImpl withSuggestion(String value) {
      String newValue = (String)Objects.requireNonNull(value, "suggestion");
      return this.suggestion.equals(newValue) ? this : new TooltipSuggestionImpl(this, newValue, this.tooltip);
   }

   public final TooltipSuggestionImpl withTooltip(@Nullable Message value) {
      return this.tooltip == value ? this : new TooltipSuggestionImpl(this, this.suggestion, value);
   }

   public boolean equals(Object another) {
      if (this == another) {
         return true;
      } else {
         return another instanceof TooltipSuggestionImpl && this.equalsByValue((TooltipSuggestionImpl)another);
      }
   }

   private boolean equalsByValue(TooltipSuggestionImpl another) {
      return this.suggestion.equals(another.suggestion) && Objects.equals(this.tooltip, another.tooltip);
   }

   public int hashCode() {
      int h = 5381;
      int h = h + (h << 5) + this.suggestion.hashCode();
      h += (h << 5) + Objects.hashCode(this.tooltip);
      return h;
   }

   public String toString() {
      return "TooltipSuggestion{suggestion=" + this.suggestion + ", tooltip=" + this.tooltip + "}";
   }

   public static TooltipSuggestionImpl of(@NonNull String suggestion, @Nullable Message tooltip) {
      return new TooltipSuggestionImpl(suggestion, tooltip);
   }

   public static TooltipSuggestionImpl copyOf(TooltipSuggestion instance) {
      return instance instanceof TooltipSuggestionImpl ? (TooltipSuggestionImpl)instance : of(instance.suggestion(), instance.tooltip());
   }
}
