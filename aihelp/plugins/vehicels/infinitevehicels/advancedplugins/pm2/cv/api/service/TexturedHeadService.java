package advancedplugins.pm2.cv.api.service;

import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface TexturedHeadService extends Service {
   @Nullable
   String getTexture(@NotNull Block var1);

   void applyTexture(@NotNull ItemStack var1, @NotNull String var2);

   void applyTexture(@NotNull ItemStack var1, OfflinePlayer var2);
}
