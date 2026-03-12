package com.nisovin.shopkeepers.api.shopobjects.living;

import com.nisovin.shopkeepers.api.util.UnmodifiableItemStack;
import java.util.Map;
import org.bukkit.inventory.EquipmentSlot;
import org.checkerframework.checker.nullness.qual.Nullable;

public interface LivingShopEquipment {
   Map<? extends EquipmentSlot, ? extends UnmodifiableItemStack> getItems();

   @Nullable
   UnmodifiableItemStack getItem(EquipmentSlot var1);

   void setItem(EquipmentSlot var1, @Nullable UnmodifiableItemStack var2);

   void clear();
}
