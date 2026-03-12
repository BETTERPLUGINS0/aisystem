package ac.grim.grimac.shaded.kyori.option;

import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.kyori.option.value.ValueSource;
import java.util.Map;
import java.util.function.Consumer;
import org.jspecify.annotations.Nullable;

@ApiStatus.NonExtendable
public interface OptionState {
   /** @deprecated */
   @Deprecated
   static OptionState emptyOptionState() {
      return OptionSchema.globalSchema().emptyState();
   }

   /** @deprecated */
   @Deprecated
   static OptionState.Builder optionState() {
      return OptionSchema.globalSchema().stateBuilder();
   }

   /** @deprecated */
   @Deprecated
   static OptionState.VersionedBuilder versionedOptionState() {
      return OptionSchema.globalSchema().versionedStateBuilder();
   }

   OptionSchema schema();

   boolean has(final Option<?> option);

   @Nullable
   <V> V value(final Option<V> option);

   @ApiStatus.NonExtendable
   public interface Builder {
      <V> OptionState.Builder value(final Option<V> option, @Nullable final V value);

      OptionState.Builder values(final OptionState existing);

      OptionState.Builder values(final ValueSource source);

      OptionState build();
   }

   @ApiStatus.NonExtendable
   public interface VersionedBuilder {
      OptionState.VersionedBuilder version(final int version, final Consumer<OptionState.Builder> versionBuilder);

      OptionState.Versioned build();
   }

   @ApiStatus.NonExtendable
   public interface Versioned extends OptionState {
      Map<Integer, OptionState> childStates();

      OptionState.Versioned at(final int version);
   }
}
