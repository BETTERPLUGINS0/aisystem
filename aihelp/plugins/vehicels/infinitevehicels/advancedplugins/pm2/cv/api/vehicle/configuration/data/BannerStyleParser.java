package advancedplugins.pm2.cv.api.vehicle.configuration.data;

import advancedplugins.pm2.cv.api.InfiniteVehicles;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;
import me.PM2.infinitevehicles.xseries.XPatternType;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BannerStyleParser extends DataParser {
   @NotNull
   public String getIdentifier() {
      return "banner-style";
   }

   @NotNull
   public Class<?> getType() {
      return BannerStyle.class;
   }

   @Nullable
   public BannerStyle parse(@NotNull ConfigurationSection var1) {
      ArrayList var2 = new ArrayList();
      Iterator var3 = var1.getKeys(false).iterator();

      while(true) {
         Pattern var5;
         while(true) {
            String var4;
            do {
               do {
                  if (!var3.hasNext()) {
                     return new BannerStyle(var2);
                  }

                  var4 = (String)var3.next();
               } while(!var1.contains(var4));
            } while(!var1.isConfigurationSection(var4));

            var5 = new Pattern(((ConfigurationSection)Objects.requireNonNull(var1.getConfigurationSection(var4))).getValues(false));
            if (var5 == null || var5.getPattern() != null) {
               break;
            }

            if (var1.getString(var4 + ".pattern") == null) {
               var5 = new Pattern(var5.getColor(), PatternType.BASE);
               break;
            }

            try {
               var5 = new Pattern(var5.getColor(), (PatternType)Registry.BANNER_PATTERN.get(NamespacedKey.fromString(var1.getString(var4 + ".pattern"))));
               break;
            } catch (Throwable var8) {
               XPatternType var7 = (XPatternType)XPatternType.of(var1.getString(var4 + ".pattern").replace("minecraft:", "").toLowerCase()).orElse((Object)null);
               if (var7 != null && var7.isSupported()) {
                  var5 = new Pattern(var5.getColor(), (PatternType)var7.get());
                  break;
               }

               Logger var10000 = InfiniteVehicles.getPlugin().getLogger();
               String var10001 = var1.getString(var4 + ".pattern");
               var10000.warning("Failed to find a pattern type by name: " + var10001.replace("minecraft:", ""));
            }
         }

         if (var5 != null) {
            var2.add(var5);
         }
      }
   }

   public void write(@NotNull Object var1, @NotNull ConfigurationSection var2) {
      super.write(var1, var2);
      if (var1 instanceof BannerStyle) {
         BannerStyle var3 = (BannerStyle)var1;
         List var4 = var3.getPatterns();

         for(int var5 = 0; var5 < var4.size(); ++var5) {
            var2.set("pattern-" + var5, var4.get(var5));
         }
      }

   }
}
