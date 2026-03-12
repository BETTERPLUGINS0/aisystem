package ac.grim.grimac.shaded.io.github.retrooper.packetevents.adventure.option.value;

import ac.grim.grimac.shaded.io.github.retrooper.packetevents.adventure.option.Option;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jspecify.annotations.Nullable;

final class ValueSources {
   static final ValueSource ENVIRONMENT = new ValueSources.EnvironmentVariable("");
   static final ValueSource SYSTEM_PROPERTIES = new ValueSources.SystemProperty("");

   private ValueSources() {
   }

   static final class EnvironmentVariable implements ValueSource {
      private static final Pattern ENVIRONMENT_SUBST_PATTERN = Pattern.compile("[:\\-/]");
      private static final String ENVIRONMENT_VAR_SEPARATOR = "_";
      private final String prefix;

      EnvironmentVariable(final String prefix) {
         this.prefix = prefix.isEmpty() ? "" : prefix.toUpperCase(Locale.ROOT) + "_";
      }

      @Nullable
      public <T> T value(final Option<T> option) {
         StringBuffer buf = new StringBuffer(option.id().length() + this.prefix.length());
         buf.append(this.prefix);
         Matcher match = ENVIRONMENT_SUBST_PATTERN.matcher(option.id());

         while(match.find()) {
            match.appendReplacement(buf, "_");
         }

         match.appendTail(buf);
         String value = System.getenv(buf.toString().toUpperCase(Locale.ROOT));
         return value == null ? null : option.valueType().parse(value);
      }
   }

   static final class SystemProperty implements ValueSource {
      private static final Pattern SYSTEM_PROP_SUBST_PATTERN = Pattern.compile("[:/]");
      private static final String SYSTEM_PROPERTY_SEPARATOR = ".";
      private final String prefix;

      SystemProperty(final String prefix) {
         this.prefix = prefix.isEmpty() ? "" : prefix + ".";
      }

      @Nullable
      public <T> T value(final Option<T> option) {
         StringBuffer buf = new StringBuffer(option.id().length() + this.prefix.length());
         buf.append(this.prefix);
         Matcher match = SYSTEM_PROP_SUBST_PATTERN.matcher(option.id());

         while(match.find()) {
            match.appendReplacement(buf, ".");
         }

         match.appendTail(buf);
         String value = System.getProperty(buf.toString());
         return value == null ? null : option.valueType().parse(value);
      }
   }
}
