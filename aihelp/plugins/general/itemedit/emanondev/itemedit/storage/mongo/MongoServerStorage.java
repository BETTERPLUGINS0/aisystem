package emanondev.itemedit.storage.mongo;

import com.mongodb.CursorType;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import emanondev.itemedit.storage.ServerStorage;
import emanondev.itemedit.utility.ItemUtils;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import org.bson.Document;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MongoServerStorage implements ServerStorage {
   @NotNull
   private final MongoStorage mongoStorage;

   public MongoServerStorage(@NotNull MongoStorage mongoStorage) {
      this.mongoStorage = mongoStorage;
   }

   @Nullable
   public ItemStack getItem(@NotNull String id) {
      this.validateID(id);
      id = id.toLowerCase(Locale.ENGLISH);
      Document document = (Document)this.mongoStorage.getServerStorage().find(Filters.eq("item_id", id)).projection(Projections.include(new String[]{"item"})).cursorType(CursorType.NonTailable).first();
      if (document == null) {
         return null;
      } else {
         Map<String, Object> serializedItem = (Map)document.get("item", Document.class);
         return this.mapToItem(serializedItem);
      }
   }

   @Nullable
   private ItemStack mapToItem(@Nullable Map<String, Object> serializedItem) {
      if (serializedItem == null) {
         return null;
      } else {
         ItemStack item = ItemStack.deserialize(serializedItem);
         return item.clone();
      }
   }

   @Nullable
   public String getNick(@NotNull String id) {
      this.validateID(id);
      id = id.toLowerCase(Locale.ENGLISH);
      Document document = (Document)this.mongoStorage.getServerStorage().find(Filters.eq("item_id", id)).projection(Projections.include(new String[]{"nick", "item"})).cursorType(CursorType.NonTailable).first();
      if (document == null) {
         return null;
      } else {
         String nick = document.getString("nick");
         if (nick != null) {
            return nick;
         } else {
            Map<String, Object> serializedItem = (Map)document.get("item", Document.class);
            ItemStack item = this.mapToItem(serializedItem);
            if (item == null) {
               return null;
            } else {
               ItemMeta itemMeta = item.getItemMeta();
               return itemMeta != null && itemMeta.hasDisplayName() ? itemMeta.getDisplayName() : item.getType().name().toLowerCase(Locale.ENGLISH);
            }
         }
      }
   }

   public void setItem(@NotNull String id, @NotNull ItemStack item) {
      this.validateID(id);
      id = id.toLowerCase(Locale.ENGLISH);
      if (ItemUtils.isAirOrNull(item)) {
         throw new IllegalArgumentException();
      } else {
         item.setAmount(1);
         this.mongoStorage.getServerStorage().insertOne((new Document()).append("item_id", id).append("item", item.serialize()));
      }
   }

   public void setNick(@NotNull String id, @Nullable String nick) {
      this.validateID(id);
      id = id.toLowerCase(Locale.ENGLISH);
      this.mongoStorage.getServerStorage().updateOne(Filters.eq("item_id", id), nick != null ? new Document("$set", new Document("nick", nick)) : new Document("$unset", new Document("nick", "")));
   }

   public void remove(@NotNull String id) {
      this.validateID(id);
      id = id.toLowerCase(Locale.ENGLISH);
      this.mongoStorage.getServerStorage().deleteOne(Filters.eq("item_id", id));
   }

   public void clear() {
      this.mongoStorage.getServerStorage().drop();
   }

   @NotNull
   public Set<String> getIds() {
      return (Set)this.mongoStorage.getServerStorage().find().cursorType(CursorType.NonTailable).projection(Projections.include(new String[]{"item_id"})).map((document) -> {
         return document.getString("item_id");
      }).into(new HashSet());
   }

   public String getId(ItemStack item) {
      item = item.clone();
      if (item.getAmount() != 1) {
         item.setAmount(1);
      }

      Map<String, Object> serializedItem = item.serialize();
      Document document = (Document)this.mongoStorage.getServerStorage().find(Filters.eq("item", serializedItem)).cursorType(CursorType.NonTailable).projection(Projections.include(new String[]{"item_id"})).first();
      return document != null ? document.getString("item_id") : null;
   }

   public void reload() {
   }
}
