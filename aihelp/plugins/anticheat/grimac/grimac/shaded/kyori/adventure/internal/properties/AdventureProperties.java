package ac.grim.grimac.shaded.kyori.adventure.internal.properties;

import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.util.PlatformAPI;
import java.util.Objects;
import java.util.function.Function;

@ApiStatus.Internal
public final class AdventureProperties {
   public static final AdventureProperties.Property<Boolean> DEBUG = property("debug", Boolean::parseBoolean, false);
   public static final AdventureProperties.Property<String> DEFAULT_TRANSLATION_LOCALE = property("defaultTranslationLocale", Function.identity(), (Object)null);
   public static final AdventureProperties.Property<Boolean> SERVICE_LOAD_FAILURES_ARE_FATAL;
   public static final AdventureProperties.Property<Boolean> TEXT_WARN_WHEN_LEGACY_FORMATTING_DETECTED;
   public static final AdventureProperties.Property<Integer> DEFAULT_FLATTENER_NESTING_LIMIT;

   private AdventureProperties() {
   }

   @NotNull
   public static <T> AdventureProperties.Property<T> property(@NotNull final String name, @NotNull final Function<String, T> parser, @Nullable final T defaultValue) {
      return property(name, parser, defaultValue, true);
   }

   @NotNull
   public static <T> AdventureProperties.Property<T> property(@NotNull final String name, @NotNull final Function<String, T> parser, @Nullable final T defaultValue, final boolean allowProviderDefaultOverride) {
      return AdventurePropertiesImpl.property(name, parser, defaultValue, allowProviderDefaultOverride);
   }

   static {
      SERVICE_LOAD_FAILURES_ARE_FATAL = property("serviceLoadFailuresAreFatal", Boolean::parseBoolean, Boolean.TRUE, false);
      TEXT_WARN_WHEN_LEGACY_FORMATTING_DETECTED = property("text.warnWhenLegacyFormattingDetected", Boolean::parseBoolean, Boolean.FALSE);
      DEFAULT_FLATTENER_NESTING_LIMIT = property("defaultFlattenerNestingLimit", Integer::parseInt, -1);
   }

   @ApiStatus.Internal
   @ApiStatus.NonExtendable
   public interface Property<T> {
      @Nullable
      T value();

      @NotNull
      default T valueOr(@NotNull final T defaultValue) {
         T value = this.value();
         return value == null ? Objects.requireNonNull(defaultValue, "defaultValue") : value;
      }
   }

   @PlatformAPI
   @ApiStatus.Internal
   public interface DefaultOverrideProvider {
      @Nullable
      <T> T overrideDefault(@NotNull final AdventureProperties.Property<T> property, @Nullable final T existingDefault);
   }
}
