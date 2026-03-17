package advancedplugins.pm2.cv.api.vehicle.configuration.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.bukkit.block.banner.Pattern;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public class BannerStyle {
   private final List<Pattern> patterns = new ArrayList();

   public BannerStyle(@NotNull List<Pattern> var1) {
      this.patterns.addAll(var1);
   }

   public boolean isEmpty() {
      return this.patterns.isEmpty();
   }

   @NotNull
   public List<Pattern> getPatterns() {
      return this.patterns;
   }

   public void applyStyle(@NotNull ItemStack var1) {
      ItemMeta var2 = var1.getItemMeta();
      if (var2 instanceof BannerMeta) {
         BannerMeta var3 = (BannerMeta)var2;
         var3.setPatterns((List)this.patterns.stream().filter(Objects::nonNull).collect(Collectors.toList()));
         var1.setItemMeta(var3);
      }

   }
}
