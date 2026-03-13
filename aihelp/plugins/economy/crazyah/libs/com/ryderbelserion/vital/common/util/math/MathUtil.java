package libs.com.ryderbelserion.vital.common.util.math;

import ch.jalu.configme.SettingsManager;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import libs.com.ryderbelserion.vital.common.config.ConfigManager;
import libs.com.ryderbelserion.vital.common.config.beans.Plugin;
import libs.com.ryderbelserion.vital.common.config.keys.ConfigKeys;

public class MathUtil {
   private static final SettingsManager config = ConfigManager.getConfig();

   public MathUtil() {
      throw new AssertionError();
   }

   public static String format(double value) {
      DecimalFormat decimalFormat = new DecimalFormat(((Plugin)config.getProperty(ConfigKeys.settings)).numberFormat);
      decimalFormat.setRoundingMode(mode());
      return decimalFormat.format(value);
   }

   public static RoundingMode mode() {
      return RoundingMode.valueOf(((Plugin)config.getProperty(ConfigKeys.settings)).rounding.toUpperCase());
   }
}
