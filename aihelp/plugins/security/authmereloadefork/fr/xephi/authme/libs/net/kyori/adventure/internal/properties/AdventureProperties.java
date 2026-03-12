package fr.xephi.authme.libs.net.kyori.adventure.internal.properties;

import java.util.function.Function;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ApiStatus.Internal
public final class AdventureProperties {
   public static final AdventureProperties.Property<Boolean> DEBUG = property("debug", Boolean::parseBoolean, false);
   public static final AdventureProperties.Property<String> DEFAULT_TRANSLATION_LOCALE = property("defaultTranslationLocale", Function.identity(), (Object)null);
   public static final AdventureProperties.Property<Boolean> SERVICE_LOAD_FAILURES_ARE_FATAL;
   public static final AdventureProperties.Property<Boolean> TEXT_WARN_WHEN_LEGACY_FORMATTING_DETECTED;

   private AdventureProperties() {
   }

   @NotNull
   public static <T> AdventureProperties.Property<T> property(@NotNull final String name, @NotNull final Function<String, T> parser, @Nullable final T defaultValue) {
      return AdventurePropertiesImpl.property(name, parser, defaultValue);
   }

   static {
      SERVICE_LOAD_FAILURES_ARE_FATAL = property("serviceLoadFailuresAreFatal", Boolean::parseBoolean, Boolean.TRUE);
      TEXT_WARN_WHEN_LEGACY_FORMATTING_DETECTED = property("text.warnWhenLegacyFormattingDetected", Boolean::parseBoolean, Boolean.FALSE);
   }

   @ApiStatus.Internal
   @ApiStatus.NonExtendable
   public interface Property<T> {
      @Nullable
      T value();
   }
}
