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
 */
package net.advancedplugins.as.impl.effects.patches;

import java.util.Locale;
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

public class HatCommandEvent
implements Listener {
    @EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
    public void onHeal(PlayerCommandPreprocessEvent playerCommandPreprocessEvent) {
        Player player;
        String string = playerCommandPreprocessEvent.getMessage().toLowerCase(Locale.ROOT);
        if (!string.startsWith("/hat")) {
            return;
        }
        if (HooksHandler.isEnabled(HookPlugin.ESSENTIALS) && !playerCommandPreprocessEvent.getPlayer().hasPermission("essentials.hat") && playerCommandPreprocessEvent.getPlayer().hasPermission("essentials.hat.prevent-type." + playerCommandPreprocessEvent.getPlayer().getItemInHand().getType().name().toLowerCase(Locale.ROOT))) {
            return;
        }
        if (HooksHandler.isEnabled(HookPlugin.CMI) && !playerCommandPreprocessEvent.getPlayer().hasPermission("cmi.command.hat")) {
            return;
        }
        String[] stringArray = string.split("\\s+");
        if (HooksHandler.isEnabled(HookPlugin.CMI) && stringArray.length > 1 && !playerCommandPreprocessEvent.getPlayer().hasPermission("cmi.command.hat.others")) {
            return;
        }
        Player player2 = player = stringArray.length > 1 ? Bukkit.getPlayer((String)stringArray[1]) : playerCommandPreprocessEvent.getPlayer();
        if (player == null) {
            return;
        }
        HoldItemTrigger.getHoldItemTrigger().executeCheck((LivingEntity)player, player.getItemInHand(), null);
    }
}

