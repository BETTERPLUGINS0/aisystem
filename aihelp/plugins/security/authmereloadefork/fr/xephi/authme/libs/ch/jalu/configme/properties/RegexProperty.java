package fr.xephi.authme.libs.ch.jalu.configme.properties;

import fr.xephi.authme.libs.ch.jalu.configme.SettingsManager;
import fr.xephi.authme.libs.ch.jalu.configme.properties.convertresult.ConvertErrorRecorder;
import fr.xephi.authme.libs.ch.jalu.configme.resource.PropertyReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class RegexProperty extends BaseProperty<Pattern> {
   public RegexProperty(String path, Pattern defaultValue) {
      super(path, defaultValue);
   }

   public RegexProperty(String path, String defaultRegexValue) {
      this(path, Pattern.compile(defaultRegexValue));
   }

   protected Pattern getFromReader(PropertyReader reader, ConvertErrorRecorder errorRecorder) {
      String pattern = reader.getString(this.getPath());
      if (pattern != null) {
         try {
            return Pattern.compile(pattern);
         } catch (PatternSyntaxException var5) {
         }
      }

      return null;
   }

   public Object toExportValue(Pattern value) {
      return value.pattern();
   }

   public boolean matches(String value, SettingsManager settingsManager) {
      Matcher matcher = ((Pattern)settingsManager.getProperty(this)).matcher(value);
      return matcher.matches();
   }
}
