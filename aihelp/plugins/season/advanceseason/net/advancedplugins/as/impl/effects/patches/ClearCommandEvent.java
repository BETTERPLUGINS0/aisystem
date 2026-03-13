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

public class ClearCommandEvent
implements Listener {
    @EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
    public void onHeal(PlayerCommandPreprocessEvent playerCommandPreprocessEvent) {
        Object object;
        String string = playerCommandPreprocessEvent.getMessage().toLowerCase(Locale.ROOT);
        if (!string.startsWith("/clear") && !string.startsWith("/ci")) {
            return;
        }
        if (HooksHandler.isEnabled(HookPlugin.ESSENTIALS) && !playerCommandPreprocessEvent.getPlayer().hasPermission("essentials.clearinventory")) {
            return;
        }
        if (HooksHandler.isEnabled(HookPlugin.CMI) && !playerCommandPreprocessEvent.getPlayer().hasPermission("cmi.command.clear")) {
            return;
        }
        String[] stringArray = string.split("\\s+");
        if (stringArray.length == 1) {
            object = playerCommandPreprocessEvent.getPlayer();
        } else {
            if (stringArray.length > 1 && !playerCommandPreprocessEvent.getPlayer().isOp()) {
                return;
            }
            object = stringArray.length > 2 && stringArray[1].equals("*") || stringArray[1].equals("**") ? (stringArray.length == 3 ? Bukkit.getPlayer((String)stringArray[2]) : playerCommandPreprocessEvent.getPlayer()) : null;
        }
        if (object == null) {
            return;
        }
        object.getInventory();
        ArmorType.getArmorContents((LivingEntity)object).forEach((arg_0, arg_1) -> ClearCommandEvent.lambda$onHeal$0((Player)object, arg_0, arg_1));
        HoldItemTrigger.getHoldItemTrigger().executeCheck((LivingEntity)object, object.getItemInHand(), null);
        HoldItemTrigger.getHoldItemTrigger().executeOffhandCheck((LivingEntity)object, object.getInventory().getItemInOffHand(), null);
    }

    private static /* synthetic */ void lambda$onHeal$0(Player player, ArmorType armorType, ItemStack itemStack) {
        ArmorWearTrigger.getArmorWearTrigger().updateWornArmor((LivingEntity)player, itemStack, null, armorType);
    }
}

