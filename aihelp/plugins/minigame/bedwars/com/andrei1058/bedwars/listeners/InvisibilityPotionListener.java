/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.player.PlayerItemConsumeEvent
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.potion.PotionEffect
 *  org.jetbrains.annotations.NotNull
 */
package com.andrei1058.bedwars.listeners;

import com.andrei1058.bedwars.BedWars;
import com.andrei1058.bedwars.api.arena.IArena;
import com.andrei1058.bedwars.api.arena.team.ITeam;
import com.andrei1058.bedwars.api.events.player.PlayerInvisibilityPotionEvent;
import com.andrei1058.bedwars.arena.Arena;
import com.andrei1058.bedwars.sidebar.SidebarService;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.jetbrains.annotations.NotNull;

public class InvisibilityPotionListener
implements Listener {
    @EventHandler
    public void onPotion(@NotNull PlayerInvisibilityPotionEvent e) {
        if (e.getTeam() == null) {
            return;
        }
        SidebarService.getInstance().handleInvisibility(e.getTeam(), e.getPlayer(), e.getType() == PlayerInvisibilityPotionEvent.Type.ADDED);
    }

    @EventHandler
    public void onDrink(PlayerItemConsumeEvent e) {
        IArena a = Arena.getArenaByPlayer(e.getPlayer());
        if (a == null) {
            return;
        }
        if (e.getItem().getType() != Material.POTION) {
            return;
        }
        Bukkit.getScheduler().runTaskLater((Plugin)BedWars.plugin, () -> BedWars.nms.minusAmount(e.getPlayer(), new ItemStack(Material.GLASS_BOTTLE), 1), 5L);
        if (BedWars.nms.isInvisibilityPotion(e.getItem())) {
            Bukkit.getScheduler().runTaskLater((Plugin)BedWars.plugin, () -> {
                for (PotionEffect pe : e.getPlayer().getActivePotionEffects()) {
                    if (!pe.getType().toString().contains("INVISIBILITY")) continue;
                    if (a.getShowTime().containsKey(e.getPlayer())) {
                        ITeam t = a.getTeam(e.getPlayer());
                        a.getShowTime().replace(e.getPlayer(), pe.getDuration() / 20);
                        Bukkit.getPluginManager().callEvent((Event)new PlayerInvisibilityPotionEvent(PlayerInvisibilityPotionEvent.Type.ADDED, t, e.getPlayer(), t.getArena()));
                        break;
                    }
                    ITeam t = a.getTeam(e.getPlayer());
                    a.getShowTime().put(e.getPlayer(), pe.getDuration() / 20);
                    for (Player p1 : e.getPlayer().getWorld().getPlayers()) {
                        if (a.isSpectator(p1)) {
                            BedWars.nms.hideArmor(e.getPlayer(), p1);
                            continue;
                        }
                        if (t == a.getTeam(p1)) continue;
                        BedWars.nms.hideArmor(e.getPlayer(), p1);
                    }
                    Bukkit.getPluginManager().callEvent((Event)new PlayerInvisibilityPotionEvent(PlayerInvisibilityPotionEvent.Type.ADDED, t, e.getPlayer(), t.getArena()));
                    break;
                }
            }, 5L);
        }
    }
}

