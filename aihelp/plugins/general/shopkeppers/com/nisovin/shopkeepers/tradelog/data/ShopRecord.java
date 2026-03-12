package com.nisovin.shopkeepers.tradelog.data;

import com.nisovin.shopkeepers.api.shopkeeper.Shopkeeper;
import com.nisovin.shopkeepers.api.shopkeeper.player.PlayerShopkeeper;
import com.nisovin.shopkeepers.util.java.Validate;
import java.util.Objects;
import java.util.UUID;
import org.checkerframework.checker.nullness.qual.Nullable;

public class ShopRecord {
   private final UUID uniqueId;
   private final String typeId;
   @Nullable
   private final PlayerRecord owner;
   private final String name;
   @Nullable
   private final String worldName;
   private final int x;
   private final int y;
   private final int z;

   public static ShopRecord of(Shopkeeper shopkeeper) {
      Validate.notNull(shopkeeper, (String)"shopkeeper is null");
      UUID shopUniqueId = shopkeeper.getUniqueId();
      String shopTypeId = shopkeeper.getType().getIdentifier();
      PlayerShopkeeper playerShop = shopkeeper instanceof PlayerShopkeeper ? (PlayerShopkeeper)shopkeeper : null;
      PlayerRecord owner = null;
      if (playerShop != null) {
         owner = PlayerRecord.of(playerShop.getOwnerUUID(), playerShop.getOwnerName());
      }

      String shopName = shopkeeper.getName();
      String worldName = shopkeeper.getWorldName();
      int x = shopkeeper.getX();
      int y = shopkeeper.getY();
      int z = shopkeeper.getZ();
      return new ShopRecord(shopUniqueId, shopTypeId, owner, shopName, worldName, x, y, z);
   }

   public ShopRecord(UUID shopUniqueId, String shopTypeId, @Nullable PlayerRecord owner, String shopName, @Nullable String worldName, int x, int y, int z) {
      Validate.notNull(shopUniqueId, (String)"shopUniqueId is null");
      Validate.notNull(shopTypeId, (String)"shopTypeId is null");
      Validate.notNull(shopName, (String)"shopName is null");
      if (worldName != null) {
         Validate.notEmpty(worldName, "worldName is empty");
      }

      this.uniqueId = shopUniqueId;
      this.typeId = shopTypeId;
      this.owner = owner;
      this.name = shopName;
      this.worldName = worldName;
      this.x = x;
      this.y = y;
      this.z = z;
   }

   public UUID getUniqueId() {
      return this.uniqueId;
   }

   public String getTypeId() {
      return this.typeId;
   }

   public String getName() {
      return this.name;
   }

   @Nullable
   public PlayerRecord getOwner() {
      return this.owner;
   }

   @Nullable
   public String getWorldName() {
      return this.worldName;
   }

   public int getX() {
      return this.x;
   }

   public int getY() {
      return this.y;
   }

   public int getZ() {
      return this.z;
   }

   public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("ShopRecord [uniqueId=");
      builder.append(this.uniqueId);
      builder.append(", typeId=");
      builder.append(this.typeId);
      builder.append(", owner=");
      builder.append(this.owner);
      builder.append(", name=");
      builder.append(this.name);
      builder.append(", worldName=");
      builder.append(this.worldName);
      builder.append(", x=");
      builder.append(this.x);
      builder.append(", y=");
      builder.append(this.y);
      builder.append(", z=");
      builder.append(this.z);
      builder.append("]");
      return builder.toString();
   }

   public int hashCode() {
      int prime = true;
      int result = 1;
      int result = 31 * result + this.uniqueId.hashCode();
      result = 31 * result + this.typeId.hashCode();
      result = 31 * result + Objects.hashCode(this.owner);
      result = 31 * result + this.name.hashCode();
      result = 31 * result + Objects.hashCode(this.worldName);
      result = 31 * result + this.x;
      result = 31 * result + this.y;
      result = 31 * result + this.z;
      return result;
   }

   public boolean equals(@Nullable Object obj) {
      if (this == obj) {
         return true;
      } else if (obj == null) {
         return false;
      } else if (!(obj instanceof ShopRecord)) {
         return false;
      } else {
         ShopRecord other = (ShopRecord)obj;
         if (!this.uniqueId.equals(other.uniqueId)) {
            return false;
         } else if (!this.typeId.equals(other.typeId)) {
            return false;
         } else if (!Objects.equals(this.owner, other.owner)) {
            return false;
         } else if (!this.name.equals(other.name)) {
            return false;
         } else if (!Objects.equals(this.worldName, other.worldName)) {
            return false;
         } else if (this.x != other.x) {
            return false;
         } else if (this.y != other.y) {
            return false;
         } else {
            return this.z == other.z;
         }
      }
   }
}
