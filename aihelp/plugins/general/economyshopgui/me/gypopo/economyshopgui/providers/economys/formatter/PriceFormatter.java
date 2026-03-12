package me.gypopo.economyshopgui.providers.economys.formatter;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import javax.annotation.Nullable;
import me.gypopo.economyshopgui.files.ConfigManager;
import me.gypopo.economyshopgui.methodes.SendMessage;
import me.gypopo.economyshopgui.providers.EconomyProvider;
import me.gypopo.economyshopgui.util.EconomyHandler;
import org.bukkit.configuration.ConfigurationSection;

public class PriceFormatter {
   public static PriceFormatter.Format getFormatter() {
      Locale locale = Locale.forLanguageTag(ConfigManager.getConfig().getString("locale", "en-US"));
      NumberFormat format = getCurrencyFormat(locale, ConfigManager.getConfig().getString("currency-format", "#,##0.00"), "currency-format");
      List<String> magnitudes = getMagnitudes((String)null, (String)null);
      return (PriceFormatter.Format)(magnitudes.isEmpty() ? new PriceFormatter.LongFormat(locale, format) : new PriceFormatter.ShortFormat(locale, format, (String[])magnitudes.toArray(new String[0])));
   }

   public static PriceFormatter.Format getFormatter(String path, @Nullable String key) {
      path = "currency-formats." + path;
      if ((key == null || !ConfigManager.getConfig().contains(path + ":" + key + ".format")) && !ConfigManager.getConfig().contains(path + ".format") && (key == null || !ConfigManager.getConfig().contains(path + ":" + key + ".locale")) && !ConfigManager.getConfig().contains(path + ".locale") && (key == null || !ConfigManager.getConfig().contains(path + ":" + key + ".abbreviations")) && !ConfigManager.getConfig().contains(path + ".abbreviations")) {
         return EconomyHandler.getDefaultFormatter();
      } else {
         Locale locale = Locale.forLanguageTag(getLocale(path, key));
         NumberFormat format = getCurrencyFormat(locale, getFormat(path, key), path + (key == null ? "" : ":" + key) + ".format");
         List<String> magnitudes = getMagnitudes(path, key);
         return (PriceFormatter.Format)(magnitudes.isEmpty() ? new PriceFormatter.LongFormat(locale, format) : new PriceFormatter.ShortFormat(locale, format, (String[])magnitudes.toArray(new String[0])));
      }
   }

   private static String getFormat(String path, @Nullable String key) {
      String defFormat = ConfigManager.getConfig().getString("currency-format", "#,##0.00");
      return key == null ? ConfigManager.getConfig().getString(path + ".format", defFormat) : ConfigManager.getConfig().getString(path + ":" + key + ".format", ConfigManager.getConfig().getString(path + ".format", defFormat));
   }

   private static String getLocale(String path, @Nullable String key) {
      String defLocale = ConfigManager.getConfig().getString("locale", "en-US");
      return key == null ? ConfigManager.getConfig().getString(path + ".locale", defLocale) : ConfigManager.getConfig().getString(path + ":" + key + ".locale", ConfigManager.getConfig().getString(path + ".locale", defLocale));
   }

   private static List<String> getMagnitudes(@Nullable String path, @Nullable String key) {
      if (key != null && ConfigManager.getConfig().contains(path + ":" + key + ".abbreviations.enabled")) {
         return (List)(ConfigManager.getConfig().getBoolean(path + ":" + key + ".abbreviations.enabled") ? ConfigManager.getConfig().getStringList(path + ":" + key + ".abbreviations.formats") : new ArrayList());
      } else if (path != null && ConfigManager.getConfig().contains(path + ".abbreviations.enabled")) {
         return (List)(ConfigManager.getConfig().getBoolean(path + ".abbreviations.enabled") ? ConfigManager.getConfig().getStringList(path + ".abbreviations.formats") : new ArrayList());
      } else {
         return (List)(ConfigManager.getConfig().getBoolean("abbreviations.enabled") ? ConfigManager.getConfig().getStringList("abbreviations.formats") : new ArrayList());
      }
   }

   private static List<String> getMagnitudes(ConfigurationSection section) {
      List<String> formats = new ArrayList();
      Iterator var2 = section.getKeys(false).iterator();

      while(var2.hasNext()) {
         String magnitude = (String)var2.next();
         formats.add(section.getString(magnitude));
      }

      return formats;
   }

   private static NumberFormat getCurrencyFormat(Locale loc, String format, String path) {
      DecimalFormat currencyFormat;
      try {
         currencyFormat = new DecimalFormat(format, DecimalFormatSymbols.getInstance(loc));
      } catch (IllegalArgumentException var5) {
         SendMessage.warnMessage("Could not load currency format of '" + format + "' at " + path + ", with reason: " + var5.getMessage());
         currencyFormat = new DecimalFormat("#,##0.00", DecimalFormatSymbols.getInstance(loc));
      }

      currencyFormat.setRoundingMode(RoundingMode.FLOOR);
      return currencyFormat;
   }

   public static final class LongFormat implements PriceFormatter.Format {
      private final Locale locale;
      private final NumberFormat format;

      public LongFormat(Locale locale, NumberFormat format) {
         this.locale = locale;
         this.format = format;
      }

      public String format(EconomyProvider provider, double amount) {
         return !provider.isDecimal() ? (amount == 1.0D ? provider.getSingular() : provider.getPlural()).replace("%price%", String.format(this.locale, "%,.0f", (double)Math.round(amount))) : (amount == 1.0D ? provider.getSingular() : provider.getPlural()).replace("%price%", this.format.format(amount));
      }
   }

   public static final class ShortFormat implements PriceFormatter.Format {
      private final Locale locale;
      private final NumberFormat format;
      private final String[] magnitudes;

      public ShortFormat(Locale locale, NumberFormat format, String[] magnitudes) {
         this.locale = locale;
         this.format = format;
         this.magnitudes = magnitudes;
      }

      public String format(EconomyProvider provider, double amount) {
         if (amount >= 1000.0D) {
            return provider.getPlural().replace("%price%", this.formatShort(amount));
         } else {
            return !provider.isDecimal() ? (amount == 1.0D ? provider.getSingular() : provider.getPlural()).replace("%price%", String.format(this.locale, "%,.0f", (double)Math.round(amount))) : (amount == 1.0D ? provider.getSingular() : provider.getPlural()).replace("%price%", this.format.format(amount));
         }
      }

      private String formatShort(double amount) {
         int i = 0;

         while(true) {
            amount /= 1000.0D;
            if (amount < 1000.0D) {
               return amount == (double)((int)amount) ? (int)amount + this.magnitudes[i] : String.format(this.locale, "%,.1f", amount) + this.magnitudes[i];
            }

            ++i;
         }
      }
   }

   public interface Format {
      String format(EconomyProvider var1, double var2);
   }
}
