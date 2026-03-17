package advancedplugins.pm2.cv.models.api.model.nrpc.nms;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface FakeItemDisplayEntity extends FakeDisplayEntity {
   void setItemStack(ItemStack var1);

   default void setItemStack(@NotNull Material material) {
      this.setItemStack(new ItemStack(material));
   }
}
