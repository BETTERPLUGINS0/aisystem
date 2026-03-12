package com.nisovin.shopkeepers.currency;

import com.nisovin.shopkeepers.util.inventory.ItemData;
import com.nisovin.shopkeepers.util.inventory.ItemUtils;
import com.nisovin.shopkeepers.util.java.StringUtils;
import com.nisovin.shopkeepers.util.java.Validate;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class Currency {
   private final String id;
   private final String displayName;
   private final ItemData itemData;
   private final int value;

   public Currency(String id, String displayName, ItemData itemData, int value) {
      Validate.notEmpty(id, "id is null or empty");
      Validate.notEmpty(displayName, "displayName is null or empty");
      Validate.notNull(itemData, (String)"itemData is null");
      Validate.isTrue(!ItemUtils.isEmpty(itemData.asUnmodifiableItemStack()), "itemData is empty");
      Validate.isTrue(value > 0, "value has to be positive");
      this.id = StringUtils.normalize(id);
      Validate.notEmpty(this.id, "id is blank");
      this.displayName = displayName;
      this.itemData = itemData;
      this.value = value;
   }

   public String getId() {
      return this.id;
   }

   public String getDisplayName() {
      return this.displayName;
   }

   public ItemData getItemData() {
      return this.itemData;
   }

   public int getValue() {
      return this.value;
   }

   public int getMaxStackSize() {
      return this.itemData.getMaxStackSize();
   }

   public int getStackValue() {
      return this.getMaxStackSize() * this.value;
   }

   public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("Currency [id=");
      builder.append(this.id);
      builder.append(", itemData=");
      builder.append(this.itemData);
      builder.append(", value=");
      builder.append(this.value);
      builder.append("]");
      return builder.toString();
   }

   public int hashCode() {
      int prime = true;
      int result = 1;
      int result = 31 * result + this.id.hashCode();
      return result;
   }

   public boolean equals(@Nullable Object obj) {
      if (this == obj) {
         return true;
      } else if (!(obj instanceof Currency)) {
         return false;
      } else {
         Currency other = (Currency)obj;
         return this.id.equals(other.id);
      }
   }
}
