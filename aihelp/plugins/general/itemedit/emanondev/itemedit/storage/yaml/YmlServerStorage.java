package emanondev.itemedit.storage.yaml;

import emanondev.itemedit.ItemEdit;
import emanondev.itemedit.YMLConfig;
import emanondev.itemedit.storage.ServerStorage;
import emanondev.itemedit.utility.ItemUtils;
import emanondev.itemedit.utility.VersionUtils;
import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class YmlServerStorage implements ServerStorage {
   private final YMLConfig database;
   private final Map<ItemStack, String> reversedMap;

   public YmlServerStorage() {
      this.database = ItemEdit.get().getConfig("database" + File.separatorChar + "server-database.yml");
      this.reversedMap = (Map)(VersionUtils.hasFoliaAPI() ? new ConcurrentHashMap() : new HashMap());
      this.reload();
   }

   public ItemStack getItem(@NotNull String id) {
      this.validateID(id);
      id = id.toLowerCase(Locale.ENGLISH);
      ItemStack item = this.database.getItemStack(id + ".item", (ItemStack)null);
      return item == null ? null : item.clone();
   }

   public String getNick(@NotNull String id) {
      this.validateID(id);
      id = id.toLowerCase(Locale.ENGLISH);
      String nick = this.database.getMessage(id + ".nick", (String)null, true);
      if (nick != null) {
         return nick;
      } else if (!this.database.contains(id)) {
         return null;
      } else {
         ItemStack item = this.getItem(id);
         if (!item.hasItemMeta()) {
            return item.getType().name().toLowerCase(Locale.ENGLISH);
         } else {
            ItemMeta meta = ItemUtils.getMeta(item);
            return !meta.hasDisplayName() ? item.getType().name().toLowerCase(Locale.ENGLISH) : meta.getDisplayName();
         }
      }
   }

   public void remove(@NotNull String id) {
      this.validateID(id);
      id = id.toLowerCase(Locale.ENGLISH);
      this.reversedMap.remove(this.getItem(id));
      this.database.set(id, (Object)null);
      this.database.save();
   }

   public void clear() {
      Iterator var1 = this.database.getKeys(false).iterator();

      while(var1.hasNext()) {
         String key = (String)var1.next();
         this.database.set(key, (Object)null);
      }

      this.reversedMap.clear();
      this.database.save();
   }

   @NotNull
   public Set<String> getIds() {
      return this.database.getKeys(false);
   }

   public void setItem(@NotNull String id, @NotNull ItemStack item) {
      this.validateID(id);
      id = id.toLowerCase(Locale.ENGLISH);
      if (ItemUtils.isAirOrNull(item)) {
         throw new IllegalArgumentException();
      } else {
         item.setAmount(1);
         this.database.set(id + ".item", item);
         this.reversedMap.put(item, id);
         this.database.save();
      }
   }

   public void setNick(@NotNull String id, String nick) {
      this.validateID(id);
      if (this.database.contains(id)) {
         id = id.toLowerCase(Locale.ENGLISH);
         this.database.set(id + ".nick", nick);
         this.database.save();
      }
   }

   @Nullable
   public String getId(ItemStack item) {
      if (item == null) {
         return null;
      } else {
         int amount = item.getAmount();
         if (item.getAmount() != 1) {
            item.setAmount(1);
         }

         String id = (String)this.reversedMap.get(item);
         if (amount != 1) {
            item.setAmount(amount);
         }

         return id;
      }
   }

   public void reload() {
      this.reversedMap.clear();
      Iterator var1 = this.database.getKeys(false).iterator();

      while(var1.hasNext()) {
         String id = (String)var1.next();

         try {
            this.validateID(id);
         } catch (Exception var4) {
            continue;
         }

         this.reversedMap.put(this.database.getItemStack(id + ".item", (ItemStack)null), id);
      }

   }
}
