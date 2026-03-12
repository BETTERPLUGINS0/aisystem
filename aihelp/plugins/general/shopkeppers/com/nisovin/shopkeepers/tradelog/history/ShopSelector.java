package com.nisovin.shopkeepers.tradelog.history;

import com.nisovin.shopkeepers.api.shopkeeper.Shopkeeper;
import com.nisovin.shopkeepers.text.Text;
import com.nisovin.shopkeepers.util.bukkit.TextUtils;
import com.nisovin.shopkeepers.util.java.Validate;
import java.util.UUID;
import org.checkerframework.checker.nullness.qual.Nullable;

public interface ShopSelector {
   ShopSelector ALL = new ShopSelector() {
      public String toString() {
         return "ShopSelector.ALL";
      }
   };
   ShopSelector ADMIN_SHOPS = new ShopSelector() {
      public String toString() {
         return "ShopSelector.ADMIN_SHOPS";
      }
   };
   ShopSelector PLAYER_SHOPS = new ShopSelector() {
      public String toString() {
         return "ShopSelector.PLAYER_SHOPS";
      }
   };

   public static class ByOwnerUUID implements ShopSelector {
      private final UUID ownerUUID;
      @Nullable
      private final String ownerName;

      public ByOwnerUUID(UUID ownerUUID, @Nullable String ownerName) {
         Validate.notNull(ownerUUID, (String)"Owner uuid is null!");
         this.ownerUUID = ownerUUID;
         this.ownerName = ownerName;
      }

      public UUID getOwnerUUID() {
         return this.ownerUUID;
      }

      @Nullable
      public String getOwnerName() {
         return this.ownerName;
      }

      public String toString() {
         StringBuilder builder = new StringBuilder();
         builder.append("ShopSelector.ByOwnerUUID [ownerUUID=");
         builder.append(this.ownerUUID);
         builder.append(", ownerName=");
         builder.append(this.ownerName);
         builder.append("]");
         return builder.toString();
      }
   }

   public static class ByExistingShop extends ShopSelector.ByShopUUID {
      private final Shopkeeper shopkeeper;

      public ByExistingShop(Shopkeeper shopkeeper) {
         this(shopkeeper, (UUID)null, (String)null);
      }

      public ByExistingShop(Shopkeeper shopkeeper, @Nullable UUID ownerUUID, @Nullable String ownerName) {
         super(((Shopkeeper)Validate.notNull(shopkeeper)).getUniqueId(), ownerUUID, ownerName);
         this.shopkeeper = shopkeeper;
      }

      public Shopkeeper getShopkeeper() {
         return this.shopkeeper;
      }

      public Text getShopIdentifier() {
         return TextUtils.getShopText(this.shopkeeper);
      }

      public String toString() {
         StringBuilder builder = new StringBuilder();
         builder.append("ShopSelector.ByExistingShop [shopUUID=");
         builder.append(this.shopkeeper.getUniqueId());
         builder.append(", ownerUUID=");
         builder.append(this.getOwnerUUID());
         builder.append("]");
         return builder.toString();
      }
   }

   public static class ByShopUUID extends ShopSelector.ByShopIdentifier {
      private final UUID shopUUID;

      public ByShopUUID(UUID shopUUID) {
         this(shopUUID, (UUID)null, (String)null);
      }

      public ByShopUUID(UUID shopUUID, @Nullable UUID ownerUUID, @Nullable String ownerName) {
         super(ownerUUID, ownerName);
         Validate.notNull(shopUUID, (String)"Shop uuid is null!");
         this.shopUUID = shopUUID;
      }

      public UUID getShopUUID() {
         return this.shopUUID;
      }

      public Text getShopIdentifier() {
         return Text.of(this.shopUUID.toString());
      }

      public String toString() {
         StringBuilder builder = new StringBuilder();
         builder.append("ShopSelector.ByShopUUID [shopUUID=");
         builder.append(this.shopUUID);
         builder.append(", ownerUUID=");
         builder.append(this.getOwnerUUID());
         builder.append("]");
         return builder.toString();
      }
   }

   public abstract static class ByShopIdentifier implements ShopSelector {
      @Nullable
      private final UUID ownerUUID;
      @Nullable
      private final String ownerName;

      public ByShopIdentifier(@Nullable UUID ownerUUID, @Nullable String ownerName) {
         this.ownerUUID = ownerUUID;
         this.ownerName = ownerName;
      }

      @Nullable
      public UUID getOwnerUUID() {
         return this.ownerUUID;
      }

      @Nullable
      public String getOwnerName() {
         return this.ownerName;
      }

      public abstract Text getShopIdentifier();
   }
}
