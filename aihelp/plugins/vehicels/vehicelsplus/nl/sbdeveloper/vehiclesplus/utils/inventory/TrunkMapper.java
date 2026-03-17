/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.inventory.Inventory
 *  org.bukkit.inventory.ItemStack
 */
package nl.sbdeveloper.vehiclesplus.utils.inventory;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class TrunkMapper {
    private TrunkMapper() {
    }

    public static Map<Integer, ItemStack> toSparse(Inventory inventory) {
        HashMap<Integer, ItemStack> hashMap = new HashMap<Integer, ItemStack>();
        ItemStack[] itemStackArray = inventory.getContents();
        for (int i = 0; i < itemStackArray.length; ++i) {
            ItemStack itemStack = itemStackArray[i];
            if (itemStack == null || itemStack.getType().isAir()) continue;
            hashMap.put(i, itemStack.clone());
        }
        return hashMap;
    }

    public static void applySparse(Inventory inventory, Map<Integer, ItemStack> map) {
        inventory.clear();
        if (map != null && !map.isEmpty()) {
            map.forEach((n, itemStack) -> {
                if (n >= 0 && n < inventory.getSize() && itemStack != null && !itemStack.getType().isAir()) {
                    inventory.setItem(n.intValue(), itemStack.clone());
                }
            });
        }
    }
}

