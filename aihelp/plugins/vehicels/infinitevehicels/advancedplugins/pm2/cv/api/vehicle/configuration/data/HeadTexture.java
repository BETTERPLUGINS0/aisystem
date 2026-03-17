package advancedplugins.pm2.cv.api.vehicle.configuration.data;

import advancedplugins.pm2.cv.api.InfiniteVehicles;
import advancedplugins.pm2.cv.api.util.inventory.ItemStackUtil;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class HeadTexture {
   private final String value;

   public HeadTexture(String var1) {
      this.value = var1;
   }

   public ItemStack getItemStack(@Nullable Integer var1) {
      ItemStack var2 = ItemStackUtil.buildCustomItem(Material.PLAYER_HEAD, var1);
      this.applyTexture(var2);
      return var2;
   }

   public ItemStack getItemStack() {
      return this.getItemStack((Integer)null);
   }

   public void applyTexture(@NotNull ItemStack var1) {
      InfiniteVehicles.getTexturedHeadService().applyTexture(var1, this.value);
   }

   public String getValue() {
      return this.value;
   }
}
