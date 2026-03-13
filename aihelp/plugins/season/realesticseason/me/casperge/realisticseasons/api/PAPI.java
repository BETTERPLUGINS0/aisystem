package me.casperge.realisticseasons.api;

import java.util.Iterator;
import me.casperge.realisticseasons.RealisticSeasons;
import me.casperge.realisticseasons.season.Season;
import me.casperge.realisticseasons.temperature.TemperatureManager;
import me.casperge.realisticseasons.utils.JavaUtils;
import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class PAPI extends PlaceholderExpansion {
   private RealisticSeasons main;
   private StringBuilder text;
   private String bottleEmoij = new String(Character.toChars(129514));

   public PAPI(RealisticSeasons var1) {
      this.main = var1;
   }

   public boolean persist() {
      return true;
   }

   public boolean canRegister() {
      return true;
   }

   public String getAuthor() {
      return "CasperSwagerman";
   }

   public String getIdentifier() {
      return "rs";
   }

   public String getVersion() {
      return "1.0.0";
   }

   public String onRequest(OfflinePlayer var1, String var2) {
      Iterator var3 = this.main.getSeasonManager().worldData.keySet().iterator();

      int var6;
      while(var3.hasNext()) {
         World var4 = (World)var3.next();
         Season var5;
         if (var2.equals("season_" + var4.getName())) {
            var5 = this.main.getSeasonManager().getSeason(var4);
            if (var5 == Season.DISABLED) {
               return "DISABLED";
            }

            return var5.toString();
         }

         if (var2.equals("next_season_" + var4.getName())) {
            var5 = this.main.getSeasonManager().getNextSeason(var4);
            if (var5 == Season.DISABLED) {
               return "DISABLED";
            }

            return var5.toString();
         }

         if (var2.equals("next_event_" + var4.getName())) {
            String var11 = this.main.getEventManager().getNextEventsAsString(var4);
            return var11;
         }

         if (var2.equals("days_until_next_event_" + var4.getName())) {
            if (!this.main.getSettings().doSeasonCycle) {
               return "DISABLED";
            }

            var5 = this.main.getSeasonManager().getSeason(var4);
            if (var5 != Season.DISABLED && var5 != Season.RESTORE) {
               var6 = this.main.getEventManager().getDaysUntilNextEvent(var4);
               if (var6 != -1) {
                  return String.valueOf(var6);
               }

               return "-";
            }

            return "DISABLED";
         }

         if (var2.equals("days_until_next_season_" + var4.getName())) {
            if (!this.main.getSettings().doSeasonCycle) {
               return "DISABLED";
            }

            return String.valueOf(this.main.getTimeManager().getDaysUntilNextSeason(var4));
         }

         if (var2.equals("day_" + var4.getName())) {
            return String.valueOf(this.main.getTimeManager().getDate(var4).getDay());
         }

         if (var2.equals("year_" + var4.getName())) {
            return String.valueOf(this.main.getTimeManager().getDate(var4).getYear());
         }

         if (var2.equals("month_" + var4.getName())) {
            return String.valueOf(this.main.getTimeManager().getDate(var4).getMonth());
         }

         if (var2.equals("month_asname_" + var4.getName())) {
            return this.main.getTimeManager().getCalendar().getMonth(this.main.getTimeManager().getDate(var4).getMonth()).getName();
         }

         if (var2.equals("weekday_" + var4.getName())) {
            return this.main.getTimeManager().getWeekDay(this.main.getTimeManager().getDate(var4));
         }

         if (var2.equals("seasonlength_" + var4.getName())) {
            return String.valueOf(this.main.getTimeManager().getTotalDays(this.main.getSeasonManager().getSeason(var4)));
         }

         if (var2.equals("time_" + var4.getName())) {
            return this.main.getTimeManager().getTimeAsString(var4);
         }

         if (var2.equals("active_events_" + var4.getName())) {
            return this.main.getEventManager().getActiveEventsAsString(var4);
         }
      }

      if (var1 == null) {
         return var2;
      } else if (var1.isOnline()) {
         Player var8 = var1.getPlayer();
         Season var17;
         if (var2.equals("season")) {
            var17 = this.main.getSeasonManager().getSeason(var8.getWorld());
            return var17 == Season.DISABLED ? "DISABLED" : var17.toString();
         } else if (var2.equals("next_season")) {
            var17 = this.main.getSeasonManager().getNextSeason(var8.getWorld());
            return var17 == Season.DISABLED ? "DISABLED" : var17.toString();
         } else {
            String var9;
            if (var2.equals("next_event")) {
               var9 = this.main.getEventManager().getNextEventsAsString(var8.getWorld());
               return var9;
            } else if (var2.equals("days_until_next_season")) {
               if (!this.main.getSettings().doSeasonCycle) {
                  return "DISABLED";
               } else {
                  var17 = this.main.getSeasonManager().getSeason(var8.getWorld());
                  return var17 != Season.DISABLED && var17 != Season.RESTORE ? String.valueOf(this.main.getTimeManager().getDaysUntilNextSeason(var8.getWorld())) : "DISABLED";
               }
            } else {
               int var12;
               if (var2.equals("days_until_next_event")) {
                  if (!this.main.getSettings().doSeasonCycle) {
                     return "DISABLED";
                  } else {
                     var17 = this.main.getSeasonManager().getSeason(var8.getWorld());
                     if (var17 != Season.DISABLED && var17 != Season.RESTORE) {
                        var12 = this.main.getEventManager().getDaysUntilNextEvent(var8.getWorld());
                        return var12 != -1 ? String.valueOf(var12) : "-";
                     } else {
                        return "DISABLED";
                     }
                  }
               } else if (var2.equals("seasonlength")) {
                  return String.valueOf(this.main.getTimeManager().getTotalDays(this.main.getSeasonManager().getSeason(var8.getWorld())));
               } else if (var2.equals("day")) {
                  return String.valueOf(this.main.getTimeManager().getDate(var8.getWorld()).getDay());
               } else if (var2.equals("year")) {
                  return String.valueOf(this.main.getTimeManager().getDate(var8.getWorld()).getYear());
               } else if (var2.equals("month")) {
                  return String.valueOf(this.main.getTimeManager().getDate(var8.getWorld()).getMonth());
               } else if (var2.equals("month_asname")) {
                  return this.main.getTimeManager().getCalendar().getMonth(this.main.getTimeManager().getDate(var8.getWorld()).getMonth()).getName();
               } else if (var2.equals("weekday")) {
                  return this.main.getTimeManager().getWeekDay(this.main.getTimeManager().getDate(var8.getWorld()));
               } else if (var2.equals("time")) {
                  return this.main.getTimeManager().getTimeAsString(var8.getWorld());
               } else if (var2.equals("active_events")) {
                  return this.main.getEventManager().getActiveEventsAsString(var8.getWorld());
               } else {
                  int var10;
                  if (var2.equals("temperature")) {
                     var10 = this.main.getTemperatureManager().getTemperature(var8);
                     if (var8.getFireTicks() > 0 && !this.main.getTemperatureManager().hasRSBurn(var8.getUniqueId())) {
                        var10 += 100;
                     }

                     return this.main.getTemperatureManager().getTempData().getTempSettings().isConvertToFahrenheit() && !this.main.getTemperatureManager().hasFahrenheitEnabled(var8) || !this.main.getTemperatureManager().getTempData().getTempSettings().isConvertToFahrenheit() && this.main.getTemperatureManager().hasFahrenheitEnabled(var8) ? JavaUtils.convertToFahrenheit(var10) + this.main.getTemperatureManager().getTempData().getTempSettings().getFahrenheitMessage() : var10 + this.main.getTemperatureManager().getTempData().getTempSettings().getCelciusMessage();
                  } else if (var2.equals("temperaturecolor")) {
                     boolean var16 = false;
                     if (var8.getFireTicks() > 0 && !this.main.getTemperatureManager().hasRSBurn(var8.getUniqueId())) {
                        var16 = true;
                     }

                     this.text = new StringBuilder();
                     TemperatureManager var14 = this.main.getTemperatureManager();
                     var6 = var14.getTemperature(var8);
                     char var7 = 'a';
                     if (var14.getTempData().getTempSettings().getColdHungerTemp() > var6) {
                        var7 = 'b';
                     }

                     if (var14.getTempData().getTempSettings().getColdSlownessTemp() > var6) {
                        var7 = '9';
                     }

                     if (var14.getTempData().getTempSettings().getColdFreezingTemp() > var6) {
                        var7 = '1';
                     }

                     if (var14.getTempData().getTempSettings().getWarmNoHealingTemp() < var6) {
                        var7 = '6';
                     }

                     if (var14.getTempData().getTempSettings().getWarmSlownessTemp() < var6) {
                        var7 = 'c';
                     }

                     if (var14.getTempData().getTempSettings().getWarmFireTemp() < var6 || var16) {
                        var7 = '4';
                     }

                     if (var14.getTempData().getTempSettings().getBoostMinTemp() <= var6 && var14.getTempData().getTempSettings().getBoostMaxTemp() >= var6 && var14.getTempData().getTempSettings().getBoostPotionEffects().size() > 0) {
                        var7 = 'd';
                     }

                     this.text.append('§');
                     this.text.append(var7);
                     return this.text.toString();
                  } else if (var2.equals("air_temperature")) {
                     return this.main.getTemperatureManager().getTempData().getTempSettings().isConvertToFahrenheit() ? String.valueOf(JavaUtils.convertToFahrenheit(this.main.getTemperatureManager().getPlayerAirTemperature(var8))) : String.valueOf(this.main.getTemperatureManager().getPlayerAirTemperature(var8));
                  } else if (var2.equals("air_temperaturecolor")) {
                     this.text = new StringBuilder();
                     TemperatureManager var15 = this.main.getTemperatureManager();
                     var12 = var15.getPlayerAirTemperature(var8);
                     char var13 = 'a';
                     if (var15.getTempData().getTempSettings().getColdHungerTemp() >= var12) {
                        var13 = 'b';
                     }

                     if (var15.getTempData().getTempSettings().getColdSlownessTemp() >= var12) {
                        var13 = '9';
                     }

                     if (var15.getTempData().getTempSettings().getColdFreezingTemp() >= var12) {
                        var13 = '1';
                     }

                     if (var15.getTempData().getTempSettings().getWarmNoHealingTemp() <= var12) {
                        var13 = '6';
                     }

                     if (var15.getTempData().getTempSettings().getWarmSlownessTemp() <= var12) {
                        var13 = 'c';
                     }

                     if (var15.getTempData().getTempSettings().getWarmFireTemp() <= var12) {
                        var13 = '4';
                     }

                     if (var15.getTempData().getTempSettings().getBoostMinTemp() <= var12 && var15.getTempData().getTempSettings().getBoostMaxTemp() >= var12) {
                        var13 = 'd';
                     }

                     this.text.append('§');
                     this.text.append(var13);
                     return this.text.toString();
                  } else if (var2.equals("bottle_icon")) {
                     return this.main.getTemperatureManager().getTempUtils().hasDrinked(var8) ? this.bottleEmoij : "";
                  } else if (var2.equals("temperature_int")) {
                     var10 = this.main.getTemperatureManager().getTemperature(var8);
                     if (var8.getFireTicks() > 0 && !this.main.getTemperatureManager().hasRSBurn(var8.getUniqueId())) {
                        var10 += 100;
                     }

                     return (!this.main.getTemperatureManager().getTempData().getTempSettings().isConvertToFahrenheit() || this.main.getTemperatureManager().hasFahrenheitEnabled(var8)) && (this.main.getTemperatureManager().getTempData().getTempSettings().isConvertToFahrenheit() || !this.main.getTemperatureManager().hasFahrenheitEnabled(var8)) ? String.valueOf(var10) : String.valueOf(JavaUtils.convertToFahrenheit(var10));
                  } else if (var2.equals("temperature_int_celcius")) {
                     var10 = this.main.getTemperatureManager().getTemperature(var8);
                     if (var8.getFireTicks() > 0 && !this.main.getTemperatureManager().hasRSBurn(var8.getUniqueId())) {
                        var10 += 100;
                     }

                     return String.valueOf(var10);
                  } else if (var2.equals("temperature_int_fahr")) {
                     var10 = this.main.getTemperatureManager().getTemperature(var8);
                     if (var8.getFireTicks() > 0 && !this.main.getTemperatureManager().hasRSBurn(var8.getUniqueId())) {
                        var10 += 100;
                     }

                     return String.valueOf(JavaUtils.convertToFahrenheit(var10));
                  } else if (var2.equals("biome")) {
                     var9 = this.main.getNMSUtils().getBiomeName(var8.getLocation());
                     return var9;
                  } else {
                     return null;
                  }
               }
            }
         }
      } else {
         return "Player is offline!";
      }
   }

   public String setPlaceHolders(Player var1, String var2) {
      return PlaceholderAPI.setPlaceholders(var1, var2);
   }
}
