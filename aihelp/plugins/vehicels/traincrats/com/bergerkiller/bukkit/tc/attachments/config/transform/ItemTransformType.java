package com.bergerkiller.bukkit.tc.attachments.config.transform;

import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import com.bergerkiller.bukkit.common.internal.CommonCapabilities;
import com.bergerkiller.bukkit.common.utils.CommonUtil;
import com.bergerkiller.bukkit.common.utils.StreamUtil;
import com.bergerkiller.bukkit.common.wrappers.ItemDisplayMode;
import com.bergerkiller.bukkit.tc.attachments.VirtualArmorStandItemEntity;
import com.bergerkiller.bukkit.tc.attachments.VirtualDisplayEntity;
import com.bergerkiller.bukkit.tc.attachments.VirtualDisplayItemEntity;
import com.bergerkiller.bukkit.tc.attachments.VirtualHybridItemEntity;
import com.bergerkiller.bukkit.tc.attachments.VirtualSpawnableObject;
import com.bergerkiller.bukkit.tc.attachments.api.AttachmentManager;
import com.bergerkiller.bukkit.tc.attachments.config.ObjectPosition;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;
import org.bukkit.inventory.ItemStack;

public interface ItemTransformType {
   String typeName();

   default String serializedName() {
      return this.category().name() + "_" + this.serializedNameWithoutCategory();
   }

   String serializedNameWithoutCategory();

   ItemTransformType.Category category();

   ItemTransformType switchCategory(ItemTransformType.Category var1);

   VirtualSpawnableObject create(AttachmentManager var1, ItemStack var2);

   void update(VirtualSpawnableObject var1, ItemStack var2);

   default void load(VirtualSpawnableObject entity, ConfigurationNode config, ObjectPosition position) {
      this.update(entity, (ItemStack)config.get("item", ItemStack.class));
   }

   boolean canUpdate(VirtualSpawnableObject var1);

   static ItemTransformType deserialize(String name) {
      ItemTransformType type = (ItemTransformType)ItemTransformType.Category.typesBySerializedName.get(name);
      if (type == null) {
         type = (ItemTransformType)ItemTransformType.Category.typesBySerializedName.get(name.toUpperCase(Locale.ENGLISH));
         if (type == null) {
            type = ItemTransformType.Category.ARMORSTAND.defaultType();
         }
      }

      if (type.category() != ItemTransformType.Category.ARMORSTAND && !CommonCapabilities.HAS_DISPLAY_ENTITY) {
         type = type.switchCategory(ItemTransformType.Category.ARMORSTAND);
      }

      return type;
   }

   static ItemTransformType deserialize(ConfigurationNode config, String key) {
      String name = (String)config.get(key, String.class, (Object)null);
      if (name == null) {
         ItemTransformType defaultType;
         if (CommonCapabilities.HAS_DISPLAY_ENTITY) {
            defaultType = ItemTransformType.Category.HYBRID.defaultType();
         } else {
            defaultType = ItemTransformType.Category.ARMORSTAND.defaultType();
         }

         config.set(key, defaultType.serializedName());
         return defaultType;
      } else {
         return deserialize(name);
      }
   }

   public static enum Category {
      DISPLAY("display Ⓓ", ItemDisplayMode.HEAD, ItemTransformType.Display::new),
      ARMORSTAND("armorstand Ⓐ", ArmorStandItemTransformType.HEAD, ItemTransformType.ArmorStand::new),
      HYBRID("hybrid Ⓓ/Ⓐ", HybridItemTransformType.ARMORSTAND_HEAD, ItemTransformType.Hybrid::new);

      private final String name;
      private final ItemTransformType defaultType;
      private final List<ItemTransformType> types;
      private static final Map<String, ItemTransformType> typesBySerializedName = new HashMap();

      private <T> Category(String name, T defaultEnumType, Function<T, ItemTransformType> ctor) {
         this(name, (ItemTransformType)ctor.apply(defaultEnumType), (List)Stream.of(CommonUtil.getClassConstants(defaultEnumType.getClass())).map(ctor).collect(StreamUtil.toUnmodifiableList()));
      }

      private Category(String name, ItemTransformType defaultType, List<ItemTransformType> types) {
         this.name = name;
         this.defaultType = defaultType;
         this.types = types;
      }

      public ItemTransformType defaultType() {
         return this.defaultType;
      }

      public List<ItemTransformType> types() {
         return this.types;
      }

      public String toString() {
         return this.name;
      }

      // $FF: synthetic method
      private static ItemTransformType.Category[] $values() {
         return new ItemTransformType.Category[]{DISPLAY, ARMORSTAND, HYBRID};
      }

      static {
         ItemTransformType.Category[] var0 = values();
         int var1 = var0.length;

         for(int var2 = 0; var2 < var1; ++var2) {
            ItemTransformType.Category category = var0[var2];
            Iterator var4 = category.types().iterator();

            while(var4.hasNext()) {
               ItemTransformType type = (ItemTransformType)var4.next();
               typesBySerializedName.put(category.name() + "_" + type.serializedNameWithoutCategory(), type);
            }
         }

         Iterator var6 = ARMORSTAND.types().iterator();

         while(var6.hasNext()) {
            ItemTransformType type = (ItemTransformType)var6.next();
            typesBySerializedName.put(type.serializedNameWithoutCategory(), type);
         }

      }
   }

   public static class Hybrid implements ItemTransformType {
      private final HybridItemTransformType transformType;

      public Hybrid(HybridItemTransformType transformType) {
         this.transformType = transformType;
      }

      public String typeName() {
         return this.transformType.toString();
      }

      public String serializedNameWithoutCategory() {
         return this.transformType.name();
      }

      public ItemTransformType.Category category() {
         return ItemTransformType.Category.HYBRID;
      }

      public ItemTransformType switchCategory(ItemTransformType.Category newCategory) {
         switch(newCategory) {
         case ARMORSTAND:
            return new ItemTransformType.ArmorStand(this.transformType.armorStandTransform());
         case DISPLAY:
            return new ItemTransformType.Display(this.transformType.displayMode());
         case HYBRID:
            return this;
         default:
            return newCategory.defaultType();
         }
      }

      public VirtualSpawnableObject create(AttachmentManager manager, ItemStack item) {
         VirtualHybridItemEntity entity = new VirtualHybridItemEntity(manager);
         entity.setItem(this.transformType, item);
         return entity;
      }

      public void update(VirtualSpawnableObject entity, ItemStack item) {
         if (this.canUpdate(entity)) {
            ((VirtualHybridItemEntity)entity).setItem(this.transformType, item);
         } else {
            throw new UnsupportedOperationException("Incompatible virtual entity");
         }
      }

      public void load(VirtualSpawnableObject entity, ConfigurationNode config, ObjectPosition position) {
         ItemTransformType.super.load(entity, config, position);
         VirtualHybridItemEntity hybrid = (VirtualHybridItemEntity)entity;
         hybrid.setClip((Double)config.getOrDefault("position.clip", 0.0D));
         hybrid.setBrightness(VirtualDisplayEntity.loadBrightnessFromConfig(config));
      }

      public boolean canUpdate(VirtualSpawnableObject entity) {
         return entity instanceof VirtualHybridItemEntity;
      }

      public String toString() {
         return "ItemTransformType.Hybrid{" + this.transformType.name() + "}";
      }

      public boolean equals(Object o) {
         if (o == this) {
            return true;
         } else if (o instanceof ItemTransformType.Hybrid) {
            return ((ItemTransformType.Hybrid)o).transformType == this.transformType;
         } else {
            return false;
         }
      }
   }

   public static class Display implements ItemTransformType {
      private final ItemDisplayMode mode;

      public Display(ItemDisplayMode mode) {
         this.mode = mode;
      }

      public String typeName() {
         return this.mode.toString();
      }

      public String serializedNameWithoutCategory() {
         return this.mode.name();
      }

      public ItemTransformType.Category category() {
         return ItemTransformType.Category.DISPLAY;
      }

      public ItemTransformType switchCategory(ItemTransformType.Category newCategory) {
         switch(newCategory) {
         case ARMORSTAND:
            if (this.mode == ItemDisplayMode.HEAD) {
               return new ItemTransformType.ArmorStand(ArmorStandItemTransformType.HEAD);
            }

            if (this.mode == ItemDisplayMode.THIRD_PERSON_LEFT_HAND) {
               return new ItemTransformType.ArmorStand(ArmorStandItemTransformType.LEFT_HAND);
            }

            if (this.mode == ItemDisplayMode.THIRD_PERSON_RIGHT_HAND) {
               return new ItemTransformType.ArmorStand(ArmorStandItemTransformType.RIGHT_HAND);
            }
            break;
         case DISPLAY:
            return this;
         case HYBRID:
            if (this.mode == ItemDisplayMode.HEAD) {
               return new ItemTransformType.Hybrid(HybridItemTransformType.DISPLAY_HEAD);
            }

            if (this.mode == ItemDisplayMode.THIRD_PERSON_RIGHT_HAND) {
               return new ItemTransformType.Hybrid(HybridItemTransformType.DISPLAY_RIGHT_HAND);
            }
         }

         return newCategory.defaultType();
      }

      public VirtualSpawnableObject create(AttachmentManager manager, ItemStack item) {
         VirtualDisplayItemEntity entity = new VirtualDisplayItemEntity(manager);
         entity.setItem(this.mode, item);
         return entity;
      }

      public void update(VirtualSpawnableObject entity, ItemStack item) {
         if (this.canUpdate(entity)) {
            ((VirtualDisplayItemEntity)entity).setItem(this.mode, item);
         } else {
            throw new UnsupportedOperationException("Incompatible virtual entity");
         }
      }

      public void load(VirtualSpawnableObject entity, ConfigurationNode config, ObjectPosition position) {
         ItemTransformType.super.load(entity, config, position);
         VirtualDisplayItemEntity itemDisplay = (VirtualDisplayItemEntity)entity;
         itemDisplay.setScale(position.size);
         itemDisplay.setClip((Double)config.getOrDefault("position.clip", 0.0D));
         itemDisplay.setBrightness(VirtualDisplayEntity.loadBrightnessFromConfig(config));
      }

      public boolean canUpdate(VirtualSpawnableObject entity) {
         return entity instanceof VirtualDisplayItemEntity;
      }

      public String toString() {
         return "ItemTransformType.Display{" + this.mode.name() + "}";
      }

      public boolean equals(Object o) {
         if (o == this) {
            return true;
         } else if (o instanceof ItemTransformType.Display) {
            return ((ItemTransformType.Display)o).mode == this.mode;
         } else {
            return false;
         }
      }
   }

   public static class ArmorStand implements ItemTransformType {
      private final ArmorStandItemTransformType transformType;

      public ArmorStand(ArmorStandItemTransformType transformType) {
         this.transformType = transformType;
      }

      public String typeName() {
         return this.transformType.toString();
      }

      public String serializedNameWithoutCategory() {
         return this.transformType.name();
      }

      public String serializedName() {
         return this.serializedNameWithoutCategory();
      }

      public ItemTransformType.Category category() {
         return ItemTransformType.Category.ARMORSTAND;
      }

      public ItemTransformType switchCategory(ItemTransformType.Category newCategory) {
         switch(newCategory) {
         case ARMORSTAND:
            return this;
         case DISPLAY:
            if (this.transformType.isHead()) {
               return new ItemTransformType.Display(ItemDisplayMode.HEAD);
            }

            if (this.transformType.isLeftHand()) {
               return new ItemTransformType.Display(ItemDisplayMode.THIRD_PERSON_LEFT_HAND);
            }

            if (this.transformType.isRightHand()) {
               return new ItemTransformType.Display(ItemDisplayMode.THIRD_PERSON_RIGHT_HAND);
            }
            break;
         case HYBRID:
            if (this.transformType.isHead()) {
               return new ItemTransformType.Hybrid(this.transformType.isSmallArmorStand() ? HybridItemTransformType.ARMORSTAND_HEAD_SMALL : HybridItemTransformType.ARMORSTAND_HEAD);
            }

            if (this.transformType.isRightHand() || this.transformType.isLeftHand()) {
               return new ItemTransformType.Hybrid(this.transformType.isSmallArmorStand() ? HybridItemTransformType.ARMORSTAND_RIGHT_HAND_SMALL : HybridItemTransformType.ARMORSTAND_RIGHT_HAND);
            }
         }

         return newCategory.defaultType();
      }

      public VirtualSpawnableObject create(AttachmentManager manager, ItemStack item) {
         VirtualArmorStandItemEntity entity = new VirtualArmorStandItemEntity(manager);
         entity.setItem(this.transformType, item);
         return entity;
      }

      public void update(VirtualSpawnableObject entity, ItemStack item) {
         if (this.canUpdate(entity)) {
            ((VirtualArmorStandItemEntity)entity).setItem(this.transformType, item);
         } else {
            throw new UnsupportedOperationException("Incompatible virtual entity");
         }
      }

      public boolean canUpdate(VirtualSpawnableObject entity) {
         return entity instanceof VirtualArmorStandItemEntity;
      }

      public String toString() {
         return "ItemTransformType.ArmorStand{" + this.transformType.name() + "}";
      }

      public boolean equals(Object o) {
         if (o == this) {
            return true;
         } else if (o instanceof ItemTransformType.ArmorStand) {
            return ((ItemTransformType.ArmorStand)o).transformType == this.transformType;
         } else {
            return false;
         }
      }
   }
}
