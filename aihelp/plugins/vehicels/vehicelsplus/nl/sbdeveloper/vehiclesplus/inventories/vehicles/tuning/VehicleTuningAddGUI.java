/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 */
package nl.sbdeveloper.vehiclesplus.inventories.vehicles.tuning;

import com.fasterxml.jackson.databind.jsontype.NamedType;
import nl.sbdeveloper.vehiclesplus.api.vehicles.impl.DrivableVehicle;
import nl.sbdeveloper.vehiclesplus.api.vehicles.parts.AddonPart;
import nl.sbdeveloper.vehiclesplus.api.vehicles.parts.Part;
import nl.sbdeveloper.vehiclesplus.libs.inventory.ClickableItem;
import nl.sbdeveloper.vehiclesplus.utils.inventories.PaginationInventory;
import nl.sbdeveloper.vehiclesplus.utils.jackson.JacksonHelper;
import nl.sbdeveloper.vehiclesplus.utils.nms.ReflectionUtil;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class VehicleTuningAddGUI
extends PaginationInventory {
    public VehicleTuningAddGUI(Player player, DrivableVehicle drivableVehicle) {
        super(5, "&6Vehicle Tuning - Add");
        JacksonHelper.getPartTypes().stream().map(NamedType::getType).filter(AddonPart.class::isAssignableFrom).forEach(clazz -> {
            Object object = ReflectionUtil.callDeclaredConstructor(clazz, new Object[0]);
            if (object == null) {
                return;
            }
            ItemStack itemStack = ((Part)object).getPartGUIItem();
            this.addItem(ClickableItem.of(itemStack, inventoryClickEvent -> {}));
        });
        this.open(player);
    }
}

