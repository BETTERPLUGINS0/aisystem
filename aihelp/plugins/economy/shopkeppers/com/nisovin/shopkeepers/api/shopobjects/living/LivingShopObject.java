package com.nisovin.shopkeepers.api.shopobjects.living;

import com.nisovin.shopkeepers.api.shopobjects.entity.EntityShopObject;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public interface LivingShopObject extends EntityShopObject {
   EntityType getEntityType();

   LivingShopEquipment getEquipment();

   boolean openEquipmentEditor(Player var1, boolean var2);
}
