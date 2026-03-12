package com.nisovin.shopkeepers.shopobjects.living;

import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.api.shopobjects.living.LivingShopEquipment;
import com.nisovin.shopkeepers.api.util.UnmodifiableItemStack;
import com.nisovin.shopkeepers.util.data.container.DataContainer;
import com.nisovin.shopkeepers.util.data.serialization.DataSerializer;
import com.nisovin.shopkeepers.util.data.serialization.InvalidDataException;
import com.nisovin.shopkeepers.util.data.serialization.bukkit.ItemStackSerializers;
import com.nisovin.shopkeepers.util.data.serialization.bukkit.MinecraftEnumSerializers;
import com.nisovin.shopkeepers.util.data.serialization.java.DataContainerSerializers;
import com.nisovin.shopkeepers.util.inventory.ItemUtils;
import com.nisovin.shopkeepers.util.java.Validate;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import org.bukkit.inventory.EquipmentSlot;
import org.checkerframework.checker.nullness.qual.Nullable;

public class SKLivingShopEquipment implements LivingShopEquipment {
   public static final DataSerializer<LivingShopEquipment> SERIALIZER = new DataSerializer<LivingShopEquipment>() {
      @Nullable
      public Object serialize(LivingShopEquipment value) {
         Validate.notNull(value, (String)"value is null");
         DataContainer equipmentData = DataContainer.create();
         Iterator var3 = value.getItems().entrySet().iterator();

         while(var3.hasNext()) {
            Entry<? extends EquipmentSlot, ? extends UnmodifiableItemStack> entry = (Entry)var3.next();
            UnmodifiableItemStack item = (UnmodifiableItemStack)entry.getValue();
            equipmentData.set(((EquipmentSlot)entry.getKey()).name(), ItemStackSerializers.UNMODIFIABLE.serialize(item));
         }

         return equipmentData.serialize();
      }

      public LivingShopEquipment deserialize(Object data) throws InvalidDataException {
         SKLivingShopEquipment equipment = new SKLivingShopEquipment();
         DataContainer equipmentData = (DataContainer)DataContainerSerializers.DEFAULT.deserialize(data);
         Iterator var4 = equipmentData.getKeys().iterator();

         while(var4.hasNext()) {
            String equipmentSlotName = (String)var4.next();
            EquipmentSlot equipmentSlot = (EquipmentSlot)MinecraftEnumSerializers.EquipmentSlots.LENIENT.deserialize(equipmentSlotName);
            Object itemData = Unsafe.assertNonNull(equipmentData.get(equipmentSlotName));
            UnmodifiableItemStack item = (UnmodifiableItemStack)ItemStackSerializers.UNMODIFIABLE.deserialize(itemData);
            equipment.setItem(equipmentSlot, item);
         }

         return equipment;
      }
   };
   private final Map<EquipmentSlot, UnmodifiableItemStack> items = new EnumMap(EquipmentSlot.class);
   private final Map<? extends EquipmentSlot, ? extends UnmodifiableItemStack> itemsView;
   @Nullable
   private Runnable changedListener;

   public SKLivingShopEquipment() {
      this.itemsView = Collections.unmodifiableMap(this.items);
   }

   void setChangedListener(@Nullable Runnable changedListener) {
      Validate.State.isTrue(this.changedListener == null, "changedListener already set!");
      this.changedListener = changedListener;
   }

   public Map<? extends EquipmentSlot, ? extends UnmodifiableItemStack> getItems() {
      return this.itemsView;
   }

   @Nullable
   public UnmodifiableItemStack getItem(EquipmentSlot slot) {
      return (UnmodifiableItemStack)this.items.get(slot);
   }

   public void setItem(EquipmentSlot slot, @Nullable UnmodifiableItemStack item) {
      if (ItemUtils.isEmpty(item)) {
         this.items.remove(slot);
      } else {
         assert item != null;

         this.items.put(slot, item);
      }

      if (this.changedListener != null) {
         this.changedListener.run();
      }

   }

   public void clear() {
      this.items.clear();
      if (this.changedListener != null) {
         this.changedListener.run();
      }

   }

   public int hashCode() {
      int prime = true;
      int result = 1;
      int result = 31 * result + this.itemsView.hashCode();
      return result;
   }

   public boolean equals(@Nullable Object obj) {
      if (this == obj) {
         return true;
      } else if (!(obj instanceof SKLivingShopEquipment)) {
         return false;
      } else {
         SKLivingShopEquipment other = (SKLivingShopEquipment)obj;
         return this.itemsView.equals(other.itemsView);
      }
   }
}
