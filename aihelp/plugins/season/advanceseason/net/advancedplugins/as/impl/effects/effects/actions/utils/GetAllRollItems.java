/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.EntityEquipment
 *  org.bukkit.inventory.InventoryHolder
 *  org.bukkit.inventory.ItemStack
 *  org.jetbrains.annotations.NotNull
 */
package net.advancedplugins.as.impl.effects.effects.actions.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import net.advancedplugins.as.impl.effects.effects.actions.utils.RollItemType;
import net.advancedplugins.as.impl.effects.effects.actions.utils.StackItem;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class GetAllRollItems {
    public static List<StackItem> getAllItems(LivingEntity livingEntity) {
        List<StackItem> list = GetAllRollItems.getMainItems(livingEntity);
        List list2 = list.stream().map(stackItem -> stackItem.i).collect(Collectors.toList());
        if (livingEntity instanceof InventoryHolder) {
            for (ItemStack itemStack : ((InventoryHolder)livingEntity).getInventory().getContents()) {
                if (itemStack == null || list2.stream().anyMatch(itemStack2 -> itemStack2.isSimilar(itemStack))) continue;
                list.add(new StackItem(itemStack, RollItemType.OTHER));
            }
        }
        AtomicInteger atomicInteger = new AtomicInteger(0);
        return list.stream().peek(stackItem -> stackItem.setNumber(atomicInteger.getAndIncrement())).collect(Collectors.toList());
    }

    public static List<StackItem> get(LivingEntity livingEntity) {
        ArrayList<StackItem> arrayList = new ArrayList<StackItem>();
        if (livingEntity instanceof InventoryHolder) {
            for (ItemStack itemStack : ((InventoryHolder)livingEntity).getInventory().getContents()) {
                if (itemStack == null) continue;
                arrayList.add(new StackItem(itemStack, RollItemType.OTHER));
            }
        }
        AtomicInteger atomicInteger = new AtomicInteger(0);
        return arrayList.stream().peek(stackItem -> stackItem.setNumber(atomicInteger.getAndIncrement())).collect(Collectors.toList());
    }

    public static List<StackItem> getItemsInHands(LivingEntity livingEntity) {
        StackItem[] stackItemArray = new StackItem[2];
        EntityEquipment entityEquipment = livingEntity.getEquipment();
        stackItemArray[0] = new StackItem(entityEquipment.getItemInHand(), RollItemType.HAND);
        stackItemArray[1] = new StackItem(livingEntity instanceof Player ? entityEquipment.getItemInOffHand() : null, RollItemType.OFFHAND);
        AtomicInteger atomicInteger = new AtomicInteger(0);
        return Arrays.stream(stackItemArray).peek(stackItem -> stackItem.setNumber(atomicInteger.getAndIncrement())).collect(Collectors.toList());
    }

    public static List<StackItem> getMainItems(@NotNull LivingEntity livingEntity) {
        StackItem[] stackItemArray = new StackItem[6];
        EntityEquipment entityEquipment = livingEntity.getEquipment();
        stackItemArray[0] = new StackItem(entityEquipment.getHelmet(), RollItemType.HELMET);
        stackItemArray[1] = new StackItem(entityEquipment.getChestplate(), RollItemType.CHESTPLATE);
        stackItemArray[2] = new StackItem(entityEquipment.getLeggings(), RollItemType.LEGGINGS);
        stackItemArray[3] = new StackItem(entityEquipment.getBoots(), RollItemType.BOOTS);
        stackItemArray[4] = new StackItem(entityEquipment.getItemInHand(), RollItemType.HAND);
        stackItemArray[5] = new StackItem(livingEntity instanceof Player ? entityEquipment.getItemInOffHand() : null, RollItemType.OFFHAND);
        AtomicInteger atomicInteger = new AtomicInteger(0);
        return Arrays.stream(stackItemArray).filter(stackItem -> stackItem.getItem() != null).peek(stackItem -> stackItem.setNumber(atomicInteger.getAndIncrement())).collect(Collectors.toList());
    }

    public static List<StackItem> getItems(LivingEntity livingEntity, RollItemType rollItemType) {
        switch (rollItemType) {
            case HANDS: {
                return GetAllRollItems.getItemsInHands(livingEntity);
            }
            case ALL: {
                return GetAllRollItems.getAllItems(livingEntity);
            }
            case GET_FOR_DEATH: {
                return GetAllRollItems.get(livingEntity);
            }
            case MAIN: {
                return GetAllRollItems.getMainItems(livingEntity);
            }
        }
        return new ArrayList<StackItem>();
    }
}

