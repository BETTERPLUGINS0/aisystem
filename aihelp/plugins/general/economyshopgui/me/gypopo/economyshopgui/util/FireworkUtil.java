package me.gypopo.economyshopgui.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import me.gypopo.economyshopgui.methodes.SendMessage;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Builder;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;

public class FireworkUtil {
   public static ItemStack addEffect(ItemStack item, ConfigurationSection config) {
      FireworkMeta meta = (FireworkMeta)item.getItemMeta();
      if (config.getInt("duration", 3) != 0) {
         meta.setPower(config.getInt("duration", 3));
      }

      if (config.contains("flicker") || config.contains("trail") || config.contains("shape") || config.contains("fade-colors")) {
         if (!config.getStringList("colors").isEmpty()) {
            Builder builder = FireworkEffect.builder();
            List<FireworkUtil.FireworkColor> colors = FireworkUtil.FireworkColor.matchColorsNames(config.getStringList("colors"));
            if (!colors.isEmpty()) {
               colors.forEach((fireworkColor) -> {
                  builder.withColor(fireworkColor.parseColor());
               });
            } else {
               builder.withColor(FireworkUtil.FireworkColor.WHITE.parseColor());
            }

            if (!config.getStringList("fade-colors").isEmpty()) {
               FireworkUtil.FireworkColor.matchColorsNames(config.getStringList("fade-colors")).forEach((fireworkColor) -> {
                  builder.withFade(fireworkColor.parseColor());
               });
            }

            if (config.getBoolean("flicker", false)) {
               builder.withFlicker();
            }

            if (config.getBoolean("trail", false)) {
               builder.trail(true);
            }

            if (config.getString("shape") != null) {
               try {
                  builder.with(Type.valueOf(config.getString("shape").toUpperCase(Locale.ENGLISH)));
               } catch (IllegalArgumentException var6) {
                  SendMessage.errorMessage("Could not add the firework shape '" + config.getString("shape") + "'");
               }
            }

            meta.addEffect(builder.build());
         } else {
            SendMessage.errorMessage("To apply a flicker, trail or firework type you need to give the firework at least one color using the 'colors' option");
         }
      }

      item.setItemMeta(meta);
      return item;
   }

   public static Map<String, Object> serialize(FireworkMeta meta) {
      Map<String, Object> keys = new HashMap();
      keys.put("duration", meta.getPower());
      if (!meta.hasEffects()) {
         return keys;
      } else {
         FireworkEffect effect = (FireworkEffect)meta.getEffects().get(0);
         keys.put("colors", effect.getColors().stream().map((c) -> {
            return FireworkUtil.FireworkColor.matchColor(c.asRGB()).name();
         }).collect(Collectors.toList()));
         keys.put("fade-colors", effect.getFadeColors().stream().map((c) -> {
            return FireworkUtil.FireworkColor.matchColor(c.asRGB()).name();
         }).collect(Collectors.toList()));
         keys.put("shape", effect.getType().name());
         keys.put("trail", effect.hasTrail());
         keys.put("flicker", effect.hasFlicker());
         return keys;
      }
   }

   public static enum FireworkColor {
      WHITE(1, 15790320),
      ORANGE(2, 15435844),
      MAGENTA(3, 12801229),
      LIGHT_BLUE(4, 6719955),
      YELLOW(5, 14602026),
      LIME(6, 4312372),
      PINK(7, 14188952),
      GRAY(8, 4408131),
      LIGHT_GRAY(9, 11250603),
      CYAN(10, 2651799),
      PURPLE(11, 8073150),
      BLUE(12, 2437522),
      BROWN(13, 5320730),
      GREEN(14, 3887386),
      RED(15, 11743532),
      BLACK(16, 1973019);

      int id;
      int rgb;

      private FireworkColor(int param3, int param4) {
         this.id = id;
         this.rgb = rgb;
      }

      public static FireworkUtil.FireworkColor matchColor(String name) {
         FireworkUtil.FireworkColor[] var1 = values();
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            FireworkUtil.FireworkColor color = var1[var3];
            if (color.name().equalsIgnoreCase(name)) {
               return color;
            }
         }

         return null;
      }

      public static List<FireworkUtil.FireworkColor> matchColorsNames(List<String> list) {
         List<FireworkUtil.FireworkColor> colors = new ArrayList();
         Iterator var2 = list.iterator();

         while(var2.hasNext()) {
            String c = (String)var2.next();
            FireworkUtil.FireworkColor[] var4 = values();
            int var5 = var4.length;

            for(int var6 = 0; var6 < var5; ++var6) {
               FireworkUtil.FireworkColor color = var4[var6];
               if (color.name().equalsIgnoreCase(c)) {
                  colors.add(color);
               }
            }
         }

         return colors;
      }

      public static List<FireworkUtil.FireworkColor> matchColorsIds(String s) {
         List<FireworkUtil.FireworkColor> colors = new ArrayList();
         String[] var2 = s.split(",");
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            String i = var2[var4];

            try {
               FireworkUtil.FireworkColor[] var6 = values();
               int var7 = var6.length;

               for(int var8 = 0; var8 < var7; ++var8) {
                  FireworkUtil.FireworkColor color = var6[var8];
                  if (color.id == Integer.parseInt(i)) {
                     colors.add(color);
                  }
               }
            } catch (NumberFormatException var10) {
            }
         }

         return colors;
      }

      public static FireworkUtil.FireworkColor getFromID(int id) {
         FireworkUtil.FireworkColor[] var1 = values();
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            FireworkUtil.FireworkColor color = var1[var3];
            if (color.id == id) {
               return color;
            }
         }

         return null;
      }

      public static FireworkUtil.FireworkColor matchColor(int rgb) {
         FireworkUtil.FireworkColor[] var1 = values();
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            FireworkUtil.FireworkColor color = var1[var3];
            if (color.rgb == rgb) {
               return color;
            }
         }

         return null;
      }

      public Color parseColor() {
         return Color.fromRGB(this.rgb);
      }

      // $FF: synthetic method
      private static FireworkUtil.FireworkColor[] $values() {
         return new FireworkUtil.FireworkColor[]{WHITE, ORANGE, MAGENTA, LIGHT_BLUE, YELLOW, LIME, PINK, GRAY, LIGHT_GRAY, CYAN, PURPLE, BLUE, BROWN, GREEN, RED, BLACK};
      }
   }
}
