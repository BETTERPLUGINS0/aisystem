/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Material
 *  org.bukkit.entity.ArmorStand
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.entity.EntityResurrectEvent
 *  org.bukkit.event.entity.PlayerDeathEvent
 *  org.bukkit.event.player.PlayerChangedWorldEvent
 *  org.bukkit.event.player.PlayerJoinEvent
 *  org.bukkit.event.player.PlayerQuitEvent
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.plugin.Plugin
 */
package net.advancedplugins.as.impl.effects.effects.mechanics.triggers.internal;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import net.advancedplugins.as.impl.effects.armorutils.ArmorEquipEvent;
import net.advancedplugins.as.impl.effects.armorutils.ArmorType;
import net.advancedplugins.as.impl.effects.effects.EffectsHandler;
import net.advancedplugins.as.impl.effects.effects.actions.AdvancedTrigger;
import net.advancedplugins.as.impl.effects.effects.effects.internal.ApplyPotionEffect;
import net.advancedplugins.as.impl.effects.effects.mechanics.triggers.internal.RepeatingTrigger;
import net.advancedplugins.as.impl.utils.ASManager;
import net.advancedplugins.as.impl.utils.SchedulerUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityResurrectEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class ArmorWearTrigger
extends AdvancedTrigger {
    private static final Multimap<UUID, ArmorType> processQueue = ArrayListMultimap.create();
    private static ArmorWearTrigger armorWearTrigger;

    public ArmorWearTrigger() {
        super("EFFECT_STATIC");
        this.setDescription("Activates when person equips/unequips armor");
        armorWearTrigger = this;
    }

    @EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
    public void onArmor(ArmorEquipEvent armorEquipEvent) {
        ArmorType armorType = armorEquipEvent.getType();
        if (armorType == null) {
            return;
        }
        ItemStack[] itemStackArray = new ItemStack[]{armorEquipEvent.getOldArmorPiece()};
        ItemStack[] itemStackArray2 = new ItemStack[]{armorEquipEvent.getNewArmorPiece()};
        SchedulerUtils.runTaskLater(() -> {
            if (armorEquipEvent.getMethod().equals((Object)ArmorEquipEvent.EquipMethod.PICK_DROP)) {
                boolean bl;
                boolean bl2 = bl = itemStackArray[0].getType().name().endsWith("HEAD") || armorEquipEvent.getNewArmorPiece().getType().name().endsWith("HEAD");
                if (bl) {
                    itemStackArray[0] = armorEquipEvent.getPlayer().getItemOnCursor();
                    itemStackArray2[0] = armorEquipEvent.getPlayer().getInventory().getHelmet();
                }
            }
        }, 1L);
        boolean bl = this.updateWornArmor((LivingEntity)armorEquipEvent.getPlayer(), itemStackArray[0], itemStackArray2[0], armorType, false, armorEquipEvent.isSendMessage());
        if (!bl) {
            armorEquipEvent.setCancelled(true);
        }
    }

    @EventHandler(priority=EventPriority.LOWEST, ignoreCancelled=true)
    public void onPlayerJoin(PlayerJoinEvent playerJoinEvent) {
        SchedulerUtils.runTaskLater(() -> {
            Player player = playerJoinEvent.getPlayer();
            for (Map.Entry<ArmorType, ItemStack> entry : ArmorType.getArmorContents((LivingEntity)player).entrySet()) {
                if (entry.getValue() == null || entry.getValue().getType().name().equalsIgnoreCase("AIR")) continue;
                this.updateWornArmor((LivingEntity)player, null, entry.getValue(), entry.getKey(), false, false);
            }
        }, 20L);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent playerQuitEvent) {
        Player player = playerQuitEvent.getPlayer();
        ArmorType.getArmorContents((LivingEntity)player).forEach((armorType, itemStack) -> this.runCheck((LivingEntity)player, (ItemStack)itemStack, (ArmorType)((Object)armorType), true, false));
    }

    @EventHandler(priority=EventPriority.HIGHEST, ignoreCancelled=true)
    public void onWorldChange(PlayerChangedWorldEvent playerChangedWorldEvent) {
        Player player = playerChangedWorldEvent.getPlayer();
        double d = player.getHealth();
        SchedulerUtils.runTaskLater(() -> {
            List list = ApplyPotionEffect.getActivatedPermanentPotions().getOrDefault(player.getUniqueId(), new ArrayList());
            List.copyOf((Collection)list).forEach(potionEffect -> ApplyPotionEffect.handlePermanentRemoval((LivingEntity)player, list, potionEffect.getType(), potionEffect.getAmplifier()));
            ArmorType.getArmorContents((LivingEntity)player).forEach((armorType, itemStack) -> {
                this.runCheck((LivingEntity)player, (ItemStack)itemStack, (ArmorType)((Object)((Object)armorType)), false, false);
                Bukkit.getPluginManager().callEvent((Event)new ArmorEquipEvent(player, ArmorEquipEvent.EquipMethod.WORLD_CHANGE, (ArmorType)((Object)((Object)armorType)), null, (ItemStack)itemStack, false));
            });
        });
    }

    @EventHandler(priority=EventPriority.NORMAL, ignoreCancelled=true)
    public void onResurrect(EntityResurrectEvent entityResurrectEvent) {
        LivingEntity livingEntity = entityResurrectEvent.getEntity();
        if (!(livingEntity instanceof Player)) {
            return;
        }
        Player player = (Player)livingEntity;
        SchedulerUtils.runTaskLater(() -> ArmorType.getArmorContents((LivingEntity)player).forEach((armorType, itemStack) -> this.updateWornArmor((LivingEntity)player, null, (ItemStack)itemStack, (ArmorType)((Object)((Object)armorType)), false, false)), 3L);
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent playerDeathEvent) {
        if (playerDeathEvent.getKeepInventory()) {
            return;
        }
        ArrayList<ItemStack> arrayList = new ArrayList<ItemStack>();
        for (ItemStack itemStack : playerDeathEvent.getEntity().getInventory().getArmorContents()) {
            if (!playerDeathEvent.getDrops().contains(itemStack)) continue;
            arrayList.add(itemStack);
        }
        SchedulerUtils.runTaskLater(() -> arrayList.forEach(itemStack -> this.updateWornArmor((LivingEntity)playerDeathEvent.getEntity(), (ItemStack)itemStack, null, ArmorType.matchType(itemStack), true)), 10L);
    }

    public boolean updateWornArmor(LivingEntity livingEntity, ItemStack itemStack, ItemStack itemStack2, ArmorType armorType) {
        if (!this.isEnabled()) {
            return false;
        }
        return this.updateWornArmor(livingEntity, itemStack, itemStack2, armorType, false);
    }

    public boolean updateWornArmor(LivingEntity livingEntity, ItemStack itemStack, ItemStack itemStack2, ArmorType armorType, boolean bl) {
        return this.updateWornArmor(livingEntity, itemStack, itemStack2, armorType, bl, true);
    }

    public boolean updateWornArmor(LivingEntity livingEntity, ItemStack itemStack, ItemStack itemStack2, ArmorType armorType, boolean bl, boolean bl2) {
        if (!(bl || !livingEntity.isDead() && livingEntity.isValid())) {
            return false;
        }
        if (livingEntity instanceof ArmorStand) {
            return false;
        }
        if (processQueue.containsEntry(livingEntity.getUniqueId(), (Object)armorType)) {
            EffectsHandler.debug(livingEntity.getName() + " is already in armor wear queue.");
            return false;
        }
        processQueue.put(livingEntity.getUniqueId(), armorType);
        double d = livingEntity.getHealth();
        try {
            boolean bl3;
            boolean bl4 = armorType != null && ArmorType.HELMET.equals((Object)armorType) && itemStack != null && itemStack2 != null && (!itemStack2.getType().name().endsWith("_HELMET") & !itemStack2.getType().name().equalsIgnoreCase("AIR") || !itemStack.getType().name().endsWith("_HELMET") && !itemStack.getType().name().equalsIgnoreCase("AIR")) ? true : (bl3 = false);
            if (bl3 && livingEntity instanceof Player) {
                ItemStack itemStack3 = itemStack.clone();
                Bukkit.getScheduler().runTaskLater((Plugin)EffectsHandler.getInstance(), () -> {
                    ItemStack itemStack4;
                    ItemStack itemStack5 = ((Player)livingEntity).getInventory().getHelmet();
                    ItemStack itemStack6 = itemStack4 = itemStack2.isSimilar(itemStack5) ? itemStack : itemStack2;
                    if ((itemStack5 == null || itemStack5.getType() == Material.AIR) && itemStack4.getType() == Material.AIR) {
                        this.runCheck(livingEntity, itemStack3, armorType, true, bl2);
                        processQueue.remove(livingEntity.getUniqueId(), (Object)armorType);
                        return;
                    }
                    this.runCheck(livingEntity, itemStack4, armorType, true, bl2);
                    this.runCheck(livingEntity, itemStack5, armorType, false, bl2);
                    processQueue.remove(livingEntity.getUniqueId(), (Object)armorType);
                }, 1L);
                return true;
            }
            this.runCheck(livingEntity, itemStack, armorType, true, bl2);
            this.runCheck(livingEntity, itemStack2, armorType, false, bl2);
            if (livingEntity instanceof Player && !bl) {
                ASManager.resetPlayerHealth((Player)livingEntity, d);
            }
            processQueue.remove(livingEntity.getUniqueId(), (Object)armorType);
            return true;
        } catch (Exception exception) {
            exception.printStackTrace();
            processQueue.remove(livingEntity.getUniqueId(), (Object)armorType);
            return true;
        }
    }

    public void runCheck(LivingEntity livingEntity, ItemStack itemStack, ArmorType armorType, boolean bl, boolean bl2) {
        if (armorType == null) {
            return;
        }
        ArmorEquipEvent armorEquipEvent = new ArmorEquipEvent(livingEntity instanceof Player ? (Player)livingEntity : null, ArmorEquipEvent.EquipMethod.HOTBAR, armorType, (ItemStack)(bl ? itemStack : null), bl ? null : itemStack, bl2);
        if (!bl) {
            RepeatingTrigger.getTrigger().activate(livingEntity, armorType.getRollItemType(), itemStack, (Event)armorEquipEvent);
        } else {
            RepeatingTrigger.getTrigger().stop(livingEntity, armorType.getRollItemType(), (Event)armorEquipEvent);
        }
        this.executionBuilder().setAttacker(livingEntity).setAttackerMain(true).setEvent((Event)armorEquipEvent).setRemoval(bl).setItemType(armorType.getRollItemType()).setItem(itemStack).buildAndExecute();
    }

    public static ArmorWearTrigger getArmorWearTrigger() {
        return armorWearTrigger;
    }
}

