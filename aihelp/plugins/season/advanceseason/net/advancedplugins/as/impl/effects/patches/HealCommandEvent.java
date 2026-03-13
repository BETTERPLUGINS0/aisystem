/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.player.PlayerCommandPreprocessEvent
 *  org.bukkit.inventory.ItemStack
 */
package net.advancedplugins.as.impl.effects.patches;

import java.util.Locale;
import net.advancedplugins.as.impl.effects.armorutils.ArmorType;
import net.advancedplugins.as.impl.effects.effects.mechanics.triggers.internal.ArmorWearTrigger;
import net.advancedplugins.as.impl.effects.effects.mechanics.triggers.internal.HoldItemTrigger;
import net.advancedplugins.as.impl.utils.SchedulerUtils;
import net.advancedplugins.as.impl.utils.hooks.HookPlugin;
import net.advancedplugins.as.impl.utils.hooks.HooksHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.inventory.ItemStack;

public class HealCommandEvent
implements Listener {
    @EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
    public void onHeal(PlayerCommandPreprocessEvent playerCommandPreprocessEvent) {
        Player player;
        String string = playerCommandPreprocessEvent.getMessage().toLowerCase(Locale.ROOT);
        if (!string.startsWith("/heal")) {
            return;
        }
        if (HooksHandler.isEnabled(HookPlugin.ESSENTIALS) && !playerCommandPreprocessEvent.getPlayer().hasPermission("essentials.heal")) {
            return;
        }
        if (HooksHandler.isEnabled(HookPlugin.CMI) && !playerCommandPreprocessEvent.getPlayer().hasPermission("cmi.command.heal")) {
            return;
        }
        String[] stringArray = string.split("\\s+");
        if (HooksHandler.isEnabled(HookPlugin.CMI) && stringArray.length > 1 && !playerCommandPreprocessEvent.getPlayer().hasPermission("cmi.command.heal.others")) {
            return;
        }
        Player player2 = player = stringArray.length > 1 ? Bukkit.getPlayer((String)stringArray[1]) : playerCommandPreprocessEvent.getPlayer();
        if (player == null) {
            return;
        }
        SchedulerUtils.runTaskLater(() -> {
            ArmorType.getArmorContents((LivingEntity)player).forEach((armorType, itemStack) -> {
                ArmorWearTrigger.getArmorWearTrigger().updateWornArmor((LivingEntity)player, (ItemStack)itemStack, null, (ArmorType)((Object)((Object)armorType)));
                ArmorWearTrigger.getArmorWearTrigger().updateWornArmor((LivingEntity)player, null, (ItemStack)itemStack, (ArmorType)((Object)((Object)armorType)));
            });
            HoldItemTrigger.getHoldItemTrigger().executeCheck((LivingEntity)player, null, player.getItemInHand());
            HoldItemTrigger.getHoldItemTrigger().executeOffhandCheck((LivingEntity)player, null, player.getInventory().getItemInOffHand());
        });
    }
}

