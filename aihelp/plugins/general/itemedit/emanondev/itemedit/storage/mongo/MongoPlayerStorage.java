package emanondev.itemedit.storage.mongo;

import com.mongodb.CursorType;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.UpdateOptions;
import emanondev.itemedit.storage.PlayerStorage;
import emanondev.itemedit.utility.ItemUtils;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MongoPlayerStorage implements PlayerStorage {
   @NotNull
   private final MongoStorage mongoStorage;
   @NotNull
   private final Logger logger;

   public MongoPlayerStorage(@NotNull MongoStorage mongoStorage, @NotNull Logger logger) {
      this.mongoStorage = mongoStorage;
      this.logger = logger;
   }

   private String getStore(OfflinePlayer player) {
      return this.storeByUUID() ? player.getUniqueId().toString() : player.getName();
   }

   @Nullable
   public ItemStack getItem(@NotNull OfflinePlayer player, @NotNull String id) {
      this.validateID(id);
      id = id.toLowerCase(Locale.ENGLISH);
      Document document = (Document)this.mongoStorage.getPlayerStorage().find(Filters.eq("store", this.getStore(player))).cursorType(CursorType.NonTailable).first();
      if (document == null) {
         return null;
      } else {
         Map<String, Object> serializedItem = (Map)document.get("items." + id, Document.class);
         if (serializedItem == null) {
            return null;
         } else {
            ItemStack item = ItemStack.deserialize(serializedItem);
            return item.clone();
         }
      }
   }

   public void setItem(@NotNull OfflinePlayer player, @NotNull String id, @NotNull ItemStack item) {
      this.validateID(id);
      id = id.toLowerCase(Locale.ENGLISH);
      if (ItemUtils.isAirOrNull(item)) {
         throw new IllegalArgumentException();
      } else {
         item.setAmount(1);
         Map<String, Object> serializedItem = item.serialize();
         String store = this.getStore(player);
         this.mongoStorage.getPlayerStorage().updateOne(Filters.eq("store", store), (new Document()).append("$setOnInsert", new Document("store", store)).append("$set", new Document("items." + id, serializedItem)), (new UpdateOptions()).upsert(true));
      }
   }

   public void remove(@NotNull OfflinePlayer player, @NotNull String id) {
      this.validateID(id);
      id = id.toLowerCase(Locale.ENGLISH);
      this.mongoStorage.getPlayerStorage().updateOne(Filters.eq("store", this.getStore(player)), new Document("$unset", new Document("items." + id, "")));
   }

   public void clear(@NotNull OfflinePlayer player) {
      String store = this.getStore(player);
      this.mongoStorage.getPlayerStorage().deleteOne(Filters.eq("store", store));
   }

   @NotNull
   public Set<String> getIds(@NotNull OfflinePlayer player) {
      Document document = (Document)this.mongoStorage.getPlayerStorage().find(Filters.eq("store", this.getStore(player))).cursorType(CursorType.NonTailable).first();
      if (document == null) {
         return Collections.emptySet();
      } else {
         Document itemsDocument = (Document)document.get("items", Document.class);
         return itemsDocument != null ? itemsDocument.keySet() : Collections.emptySet();
      }
   }

   @NotNull
   public Set<OfflinePlayer> getPlayers() {
      Set<String> playerData = (Set)this.mongoStorage.getPlayerStorage().find().cursorType(CursorType.NonTailable).projection(Projections.include(new String[]{"store"})).map((document) -> {
         return document.getString("store");
      }).into(new HashSet());
      Set<OfflinePlayer> players = new HashSet();
      boolean uuid = this.storeByUUID();
      Iterator var4 = playerData.iterator();

      while(var4.hasNext()) {
         String store = (String)var4.next();

         UUID uniqueId;
         try {
            uniqueId = uuid ? UUID.fromString(store) : null;
         } catch (IllegalArgumentException var8) {
            this.logger.log(Level.SEVERE, "Failed to validate uuid " + store + ", skipped player.", var8);
            continue;
         }

         players.add(uuid ? Bukkit.getOfflinePlayer(uniqueId) : Bukkit.getOfflinePlayer(store));
      }

      return players;
   }
}
