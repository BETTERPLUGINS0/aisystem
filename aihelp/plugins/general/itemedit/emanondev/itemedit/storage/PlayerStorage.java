package emanondev.itemedit.storage;

import emanondev.itemedit.ItemEdit;
import java.util.Set;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface PlayerStorage {
   @Nullable
   ItemStack getItem(@NotNull OfflinePlayer var1, @NotNull String var2);

   void setItem(@NotNull OfflinePlayer var1, @NotNull String var2, @NotNull ItemStack var3);

   void remove(@NotNull OfflinePlayer var1, @NotNull String var2);

   void clear(@NotNull OfflinePlayer var1);

   @NotNull
   Set<String> getIds(@NotNull OfflinePlayer var1);

   @NotNull
   Set<OfflinePlayer> getPlayers();

   default boolean storeByUUID() {
      return ItemEdit.get().getConfig().loadBoolean("storage.store-by-uuid", true);
   }

   default void validateID(@Nullable String id) {
      if (id == null || id.contains(" ") || id.contains(".") || id.isEmpty()) {
         throw new IllegalArgumentException();
      }
   }

   default void reload() {
   }
}
