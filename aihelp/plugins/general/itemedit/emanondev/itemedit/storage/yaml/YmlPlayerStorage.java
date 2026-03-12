package emanondev.itemedit.storage.yaml;

import emanondev.itemedit.ItemEdit;
import emanondev.itemedit.YMLConfig;
import emanondev.itemedit.storage.PlayerStorage;
import emanondev.itemedit.utility.ItemUtils;
import java.io.File;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class YmlPlayerStorage implements PlayerStorage {
   private final YMLConfig database;

   public YmlPlayerStorage() {
      this.database = ItemEdit.get().getConfig("database" + File.separatorChar + "player-database.yml");
   }

   private String getBasePath(OfflinePlayer p) {
      return this.storeByUUID() ? p.getUniqueId().toString() : p.getName();
   }

   public ItemStack getItem(@NotNull OfflinePlayer player, @NotNull String id) {
      this.validateID(id);
      id = id.toLowerCase(Locale.ENGLISH);
      ItemStack item = this.database.getItemStack(this.getBasePath(player) + "." + id + ".item", (ItemStack)null);
      return item == null ? null : item.clone();
   }

   public void setItem(@NotNull OfflinePlayer player, @NotNull String id, @NotNull ItemStack item) {
      this.validateID(id);
      id = id.toLowerCase(Locale.ENGLISH);
      if (ItemUtils.isAirOrNull(item)) {
         throw new IllegalArgumentException();
      } else {
         item.setAmount(1);
         this.database.set(this.getBasePath(player) + "." + id + ".item", item);
         this.database.save();
      }
   }

   public void remove(@NotNull OfflinePlayer player, @NotNull String id) {
      this.validateID(id);
      id = id.toLowerCase(Locale.ENGLISH);
      this.database.set(this.getBasePath(player) + "." + id, (Object)null);
      this.database.save();
   }

   public void clear(@NotNull OfflinePlayer player) {
      this.database.set(this.getBasePath(player), (Object)null);
      this.database.save();
   }

   @NotNull
   public Set<String> getIds(@NotNull OfflinePlayer player) {
      return this.database.getKeys(this.getBasePath(player));
   }

   @NotNull
   public Set<OfflinePlayer> getPlayers() {
      Set<String> playersData = this.database.getKeys(false);
      Set<OfflinePlayer> players = new HashSet();
      boolean uuid = this.storeByUUID();
      Iterator var4 = playersData.iterator();

      while(var4.hasNext()) {
         String val = (String)var4.next();
         if (uuid) {
            players.add(Bukkit.getOfflinePlayer(UUID.fromString(val)));
         } else {
            players.add(Bukkit.getOfflinePlayer(val));
         }
      }

      return players;
   }
}
