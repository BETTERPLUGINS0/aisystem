/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.GameMode
 *  org.bukkit.Material
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.EntityType
 *  org.bukkit.entity.Player
 *  org.bukkit.entity.Projectile
 *  org.bukkit.entity.TNTPrimed
 *  org.bukkit.event.Event
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 *  org.bukkit.event.entity.EntityRegainHealthEvent
 *  org.bukkit.event.entity.FoodLevelChangeEvent
 *  org.bukkit.event.inventory.InventoryClickEvent
 *  org.bukkit.event.inventory.InventoryCloseEvent
 *  org.bukkit.event.player.PlayerInteractEntityEvent
 *  org.bukkit.event.player.PlayerInteractEvent
 *  org.bukkit.event.player.PlayerTeleportEvent
 *  org.bukkit.event.player.PlayerTeleportEvent$TeleportCause
 *  org.bukkit.event.player.PlayerToggleSneakEvent
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.projectiles.ProjectileSource
 */
package com.andrei1058.bedwars.arena.spectator;

import com.andrei1058.bedwars.BedWars;
import com.andrei1058.bedwars.api.arena.IArena;
import com.andrei1058.bedwars.api.events.player.PlayerKillEvent;
import com.andrei1058.bedwars.api.events.player.PlayerLeaveArenaEvent;
import com.andrei1058.bedwars.api.events.spectator.SpectatorFirstPersonEnterEvent;
import com.andrei1058.bedwars.api.events.spectator.SpectatorFirstPersonLeaveEvent;
import com.andrei1058.bedwars.api.events.spectator.SpectatorTeleportToPlayerEvent;
import com.andrei1058.bedwars.api.language.Language;
import com.andrei1058.bedwars.api.language.Messages;
import com.andrei1058.bedwars.arena.Arena;
import com.andrei1058.bedwars.arena.spectator.TeleporterGUI;
import com.andrei1058.bedwars.configuration.Sounds;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.projectiles.ProjectileSource;

public class SpectatorListeners
implements Listener {
    @EventHandler
    public void onSpectatorItemInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        ItemStack i = BedWars.nms.getItemInHand(p);
        if (i == null) {
            return;
        }
        if (i.getType() == Material.AIR) {
            return;
        }
        if (!BedWars.nms.isCustomBedWarsItem(i)) {
            return;
        }
        IArena a = Arena.getArenaByPlayer(p);
        if (a == null) {
            return;
        }
        if (!a.isSpectator(p)) {
            return;
        }
        e.setCancelled(true);
    }

    @EventHandler
    public void onSpectatorBlockInteract(PlayerInteractEvent e) {
        if (e.getClickedBlock() == null) {
            return;
        }
        if (!BedWars.getAPI().getArenaUtil().isSpectating(e.getPlayer())) {
            return;
        }
        if (e.getClickedBlock().getType().toString().contains("DOOR")) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onSpectatorInventoryClose(InventoryCloseEvent e) {
        Player p = (Player)e.getPlayer();
        TeleporterGUI.closeGUI(p);
    }

    @EventHandler
    public void onSpectatorClick(InventoryClickEvent e) {
        if (e.getWhoClicked().getGameMode() == GameMode.SPECTATOR) {
            e.setCancelled(true);
            return;
        }
        if (e.getCurrentItem() == null) {
            return;
        }
        ItemStack i = e.getCurrentItem();
        if (i.getType() == Material.AIR) {
            return;
        }
        Player p = (Player)e.getWhoClicked();
        IArena a = Arena.getArenaByPlayer(p);
        if (a == null) {
            return;
        }
        if (!a.isSpectator(p)) {
            return;
        }
        if (BedWars.nms.isPlayerHead(i.getType().toString(), 3) && BedWars.nms.itemStackDataCompare(i, (short)3) && BedWars.nms.isCustomBedWarsItem(i)) {
            e.setCancelled(true);
            String data = BedWars.nms.getCustomData(i);
            if (data.contains("spectatorTeleporterGUIhead_")) {
                String player = data.replace("spectatorTeleporterGUIhead_", "");
                Player target = Bukkit.getPlayer((String)player);
                if (target == null) {
                    return;
                }
                if (target.isDead()) {
                    return;
                }
                if (!target.isOnline()) {
                    return;
                }
                SpectatorTeleportToPlayerEvent event = new SpectatorTeleportToPlayerEvent(p, target, a);
                Bukkit.getPluginManager().callEvent((Event)event);
                if (!event.isCancelled()) {
                    p.teleport((Entity)target);
                }
                Sounds.playSound("spectator-gui-click", p);
                p.closeInventory();
            }
        }
    }

    @EventHandler
    public void onHealthChange(EntityRegainHealthEvent e) {
        if (e.getEntity().getType() != EntityType.PLAYER) {
            return;
        }
        Player p = (Player)e.getEntity();
        IArena arena = Arena.getArenaByPlayer(p);
        if (arena == null) {
            return;
        }
        if (arena.isPlayer(p)) {
            TeleporterGUI.refreshAllGUIs();
        }
    }

    @EventHandler
    public void onFoodChange(FoodLevelChangeEvent e) {
        if (e.getEntity().getType() != EntityType.PLAYER) {
            return;
        }
        Player p = (Player)e.getEntity();
        IArena arena = Arena.getArenaByPlayer(p);
        if (arena == null) {
            return;
        }
        if (arena.isPlayer(p)) {
            TeleporterGUI.refreshAllGUIs();
        }
    }

    @EventHandler
    public void onPlayerLeave(PlayerLeaveArenaEvent e) {
        if (e.getArena().isPlayer(e.getPlayer())) {
            TeleporterGUI.refreshAllGUIs();
        }
    }

    @EventHandler
    public void onSpectatorInteractPlayer(PlayerInteractEntityEvent e) {
        if (e.getRightClicked().getType() != EntityType.PLAYER) {
            return;
        }
        Player p = e.getPlayer();
        IArena a = Arena.getArenaByPlayer(p);
        if (a == null) {
            return;
        }
        if (a.isPlayer(p)) {
            return;
        }
        e.setCancelled(true);
        Player target = (Player)e.getRightClicked();
        if (a.isPlayer(target)) {
            if (p.getSpectatorTarget() != null) {
                SpectatorFirstPersonLeaveEvent e2 = new SpectatorFirstPersonLeaveEvent(p, a, player -> Language.getMsg(player, Messages.ARENA_SPECTATOR_FIRST_PERSON_LEAVE_TITLE), player -> Language.getMsg(player, Messages.ARENA_SPECTATOR_FIRST_PERSON_LEAVE_SUBTITLE));
                Bukkit.getPluginManager().callEvent((Event)e2);
            }
            SpectatorFirstPersonEnterEvent event = new SpectatorFirstPersonEnterEvent(p, target, a, player -> Language.getMsg(player, Messages.ARENA_SPECTATOR_FIRST_PERSON_ENTER_TITLE), player -> Language.getMsg(player, Messages.ARENA_SPECTATOR_FIRST_PERSON_ENTER_SUBTITLE));
            Bukkit.getPluginManager().callEvent((Event)event);
            if (event.isCancelled()) {
                return;
            }
            p.getInventory().setHeldItemSlot(5);
            p.setGameMode(GameMode.SPECTATOR);
            p.setSpectatorTarget((Entity)target);
            BedWars.nms.sendTitle(p, event.getTitle().apply(p).replace("{playername}", p.getName()).replace("{player}", target.getDisplayName()), event.getSubTitle().apply(p).replace("{player}", target.getDisplayName()), event.getFadeIn(), event.getStay(), event.getFadeOut());
        }
    }

    @EventHandler
    public void onSneak(PlayerToggleSneakEvent e) {
        Player p = e.getPlayer();
        IArena a = Arena.getArenaByPlayer(p);
        if (a == null) {
            return;
        }
        if (a.isSpectator(p) && p.getSpectatorTarget() != null) {
            p.setGameMode(GameMode.ADVENTURE);
            p.setAllowFlight(true);
            p.setFlying(true);
            SpectatorFirstPersonLeaveEvent event = new SpectatorFirstPersonLeaveEvent(p, a, player -> Language.getMsg(player, Messages.ARENA_SPECTATOR_FIRST_PERSON_LEAVE_TITLE), player -> Language.getMsg(player, Messages.ARENA_SPECTATOR_FIRST_PERSON_LEAVE_SUBTITLE));
            Bukkit.getPluginManager().callEvent((Event)event);
            BedWars.nms.sendTitle(p, event.getTitle().apply(p), event.getSubTitle().apply(p), event.getFadeIn(), event.getStay(), event.getFadeOut());
        }
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent e) {
        IArena a = Arena.getArenaByPlayer(e.getPlayer());
        if (a == null) {
            return;
        }
        if (a.isSpectator(e.getPlayer()) && !e.getTo().getWorld().equals((Object)e.getPlayer().getWorld()) && e.getCause() == PlayerTeleportEvent.TeleportCause.SPECTATE) {
            Player p = e.getPlayer();
            e.setCancelled(true);
            p.setGameMode(GameMode.ADVENTURE);
            p.setAllowFlight(true);
            p.setFlying(true);
            SpectatorFirstPersonLeaveEvent event = new SpectatorFirstPersonLeaveEvent(p, Arena.getArenaByPlayer(p), player -> Language.getMsg(player, Messages.ARENA_SPECTATOR_FIRST_PERSON_LEAVE_TITLE), player -> Language.getMsg(player, Messages.ARENA_SPECTATOR_FIRST_PERSON_LEAVE_SUBTITLE));
            Bukkit.getPluginManager().callEvent((Event)event);
            BedWars.nms.sendTitle(p, event.getTitle().apply(p), event.getSubTitle().apply(p), event.getFadeIn(), event.getStay(), event.getFadeOut());
        }
    }

    @EventHandler
    public void onTargetDeath(PlayerKillEvent e) {
        for (Player p : e.getArena().getSpectators()) {
            if (p.getSpectatorTarget() == null || p.getSpectatorTarget() != e.getVictim()) continue;
            p.setGameMode(GameMode.ADVENTURE);
            p.setAllowFlight(true);
            p.setFlying(true);
            SpectatorFirstPersonLeaveEvent event = new SpectatorFirstPersonLeaveEvent(p, e.getArena(), player -> Language.getMsg(player, Messages.ARENA_SPECTATOR_FIRST_PERSON_LEAVE_TITLE), player -> Language.getMsg(player, Messages.ARENA_SPECTATOR_FIRST_PERSON_LEAVE_SUBTITLE));
            Bukkit.getPluginManager().callEvent((Event)event);
            BedWars.nms.sendTitle(p, event.getTitle().apply(p), event.getSubTitle().apply(p), event.getFadeIn(), event.getStay(), event.getFadeOut());
        }
    }

    @EventHandler
    public void onDamageByEntity(EntityDamageByEntityEvent e) {
        TNTPrimed tnt;
        if (e.isCancelled()) {
            return;
        }
        IArena a = Arena.getArenaByIdentifier(e.getEntity().getWorld().getName());
        if (a == null) {
            return;
        }
        Player damager = null;
        if (e.getDamager() instanceof Projectile) {
            ProjectileSource shooter = ((Projectile)e.getDamager()).getShooter();
            if (shooter instanceof Player) {
                damager = (Player)shooter;
            }
        } else if (e.getDamager() instanceof Player) {
            damager = (Player)e.getDamager();
            if (a.getRespawnSessions().containsKey(damager)) {
                e.setCancelled(true);
                return;
            }
        } else if (e.getDamager() instanceof TNTPrimed && (tnt = (TNTPrimed)e.getDamager()).getSource() instanceof Player) {
            damager = (Player)tnt.getSource();
        }
        if (damager == null) {
            return;
        }
        if (a.isSpectator(damager)) {
            e.setCancelled(true);
        }
    }
}

