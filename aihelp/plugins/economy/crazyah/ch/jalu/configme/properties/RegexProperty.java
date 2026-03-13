package ch.jalu.configme.properties;

import ch.jalu.configme.SettingsManager;
import ch.jalu.configme.properties.convertresult.ConvertErrorRecorder;
import ch.jalu.configme.resource.PropertyReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RegexProperty extends BaseProperty<Pattern> {
   public RegexProperty(@NotNull String path, @NotNull Pattern defaultValue) {
      super(path, defaultValue);
   }

   public RegexProperty(@NotNull String path, @NotNull String defaultRegexValue) {
      this(path, Pattern.compile(defaultRegexValue));
   }

   @Nullable
   protected Pattern getFromReader(@NotNull PropertyReader reader, @NotNull ConvertErrorRecorder errorRecorder) {
      String pattern = reader.getString(this.getPath());
      if (pattern != null) {
         try {
            return Pattern.compile(pattern);
         } catch (PatternSyntaxException var5) {
         }
      }

      return null;
   }

   @NotNull
   public Object toExportValue(@NotNull Pattern value) {
      return value.pattern();
   }

   public boolean matches(@NotNull String value, @NotNull SettingsManager settingsManager) {
      Matcher matcher = ((Pattern)settingsManager.getProperty(this)).matcher(value);
      return matcher.matches();
   }
}
