/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.block.Block
 *  org.bukkit.block.Sign
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.EntityType
 *  org.bukkit.entity.Fireball
 *  org.bukkit.entity.ItemFrame
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.block.Action
 *  org.bukkit.event.inventory.PrepareItemCraftEvent
 *  org.bukkit.event.player.PlayerArmorStandManipulateEvent
 *  org.bukkit.event.player.PlayerBedEnterEvent
 *  org.bukkit.event.player.PlayerInteractEntityEvent
 *  org.bukkit.event.player.PlayerInteractEvent
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.material.Openable
 *  org.bukkit.metadata.FixedMetadataValue
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.util.Vector
 */
package com.andrei1058.bedwars.listeners;

import com.andrei1058.bedwars.BedWars;
import com.andrei1058.bedwars.api.arena.IArena;
import com.andrei1058.bedwars.api.arena.team.ITeam;
import com.andrei1058.bedwars.api.language.Language;
import com.andrei1058.bedwars.api.language.Messages;
import com.andrei1058.bedwars.api.server.ServerType;
import com.andrei1058.bedwars.arena.Arena;
import com.andrei1058.bedwars.configuration.Sounds;
import com.andrei1058.bedwars.listeners.BreakPlace;
import com.andrei1058.bedwars.shop.ShopCache;
import com.andrei1058.bedwars.shop.listeners.InventoryListener;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Openable;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

public class Interact
implements Listener {
    private final double fireballSpeedMultiplier = BedWars.config.getYml().getDouble("fireball.speed-multiplier");
    private final double fireballCooldown = BedWars.config.getYml().getDouble("fireball.cooldown");
    private final float fireballExplosionSize = (float)BedWars.config.getYml().getDouble("fireball.explosion-size");

    @EventHandler
    public void onItemCommand(PlayerInteractEvent e) {
        if (e == null) {
            return;
        }
        Player p = e.getPlayer();
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR) {
            ItemStack i = BedWars.nms.getItemInHand(p);
            if (!BedWars.nms.isCustomBedWarsItem(i)) {
                return;
            }
            String[] customData = BedWars.nms.getCustomData(i).split("_");
            if (customData.length >= 2 && customData[0].equals("RUNCOMMAND")) {
                e.setCancelled(true);
                Bukkit.getScheduler().runTask((Plugin)BedWars.plugin, () -> Bukkit.dispatchCommand((CommandSender)p, (String)customData[1]));
            }
        }
    }

    @EventHandler
    public void onInventoryInteract(PlayerInteractEvent e) {
        if (e == null) {
            return;
        }
        if (e.isCancelled()) {
            return;
        }
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        Block b = e.getClickedBlock();
        if (b == null) {
            return;
        }
        if (BedWars.getServerType() == ServerType.MULTIARENA && b.getWorld().getName().equals(BedWars.getLobbyWorld()) && !BreakPlace.isBuildSession(e.getPlayer()) || Arena.getArenaByPlayer(e.getPlayer()) != null) {
            if (b.getType() == BedWars.nms.materialCraftingTable() && BedWars.config.getBoolean("inventories.disable-crafting-table")) {
                e.setCancelled(true);
            } else if (b.getType() == BedWars.nms.materialEnchantingTable() && BedWars.config.getBoolean("inventories.disable-enchanting-table")) {
                e.setCancelled(true);
            } else if (b.getType() == Material.FURNACE && BedWars.config.getBoolean("inventories.disable-furnace")) {
                e.setCancelled(true);
            } else if (b.getType() == Material.BREWING_STAND && BedWars.config.getBoolean("inventories.disable-brewing-stand")) {
                e.setCancelled(true);
            } else if (b.getType() == Material.ANVIL && BedWars.config.getBoolean("inventories.disable-anvil")) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        IArena a;
        if (e == null) {
            return;
        }
        Player p = e.getPlayer();
        Arena.afkCheck.remove(p.getUniqueId());
        if (BedWars.getAPI().getAFKUtil().isPlayerAFK(e.getPlayer())) {
            BedWars.getAPI().getAFKUtil().setPlayerAFK(e.getPlayer(), false);
        }
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Block b = e.getClickedBlock();
            if (b == null) {
                return;
            }
            if (b.getType() == Material.AIR) {
                return;
            }
            a = Arena.getArenaByPlayer(p);
            if (a != null) {
                if (a.getRespawnSessions().containsKey(e.getPlayer())) {
                    e.setCancelled(true);
                    return;
                }
                if (BedWars.nms.isBed(b.getType())) {
                    if (p.isSneaking()) {
                        ItemStack i = BedWars.nms.getItemInHand(p);
                        if (i == null) {
                            e.setCancelled(true);
                        } else if (i.getType() == Material.AIR) {
                            e.setCancelled(true);
                        }
                    } else {
                        e.setCancelled(true);
                    }
                    return;
                }
                if (b.getType() == Material.CHEST) {
                    if (a.isSpectator(p) || a.getRespawnSessions().containsKey(p)) {
                        e.setCancelled(true);
                        return;
                    }
                    Object owner = null;
                    int isRad = a.getConfig().getInt("island-radius");
                    for (ITeam t : a.getTeams()) {
                        if (!(t.getSpawn().distance(e.getClickedBlock().getLocation()) <= (double)isRad)) continue;
                        owner = t;
                    }
                    if (!(owner == null || owner.isMember(p) || owner.getMembers().isEmpty() && owner.isBedDestroyed())) {
                        e.setCancelled(true);
                        p.sendMessage(Language.getMsg(p, Messages.INTERACT_CHEST_CANT_OPEN_TEAM_ELIMINATED));
                    }
                }
                if (a.isSpectator(p) || a.getRespawnSessions().containsKey(p)) {
                    switch (b.getType().toString()) {
                        case "CHEST": 
                        case "ENDER_CHEST": 
                        case "ANVIL": 
                        case "WORKBENCH": 
                        case "HOPPER": 
                        case "TRAPPED_CHEST": 
                        case "CRAFTING_TABLE": {
                            e.setCancelled(true);
                        }
                    }
                    if (b.getState() instanceof Openable) {
                        e.setCancelled(true);
                    }
                }
            }
            if (b.getState() instanceof Sign) {
                for (IArena a1 : Arena.getArenas()) {
                    if (!a1.getSigns().contains(b)) continue;
                    if (a1.addPlayer(p, false)) {
                        Sounds.playSound("join-allowed", p);
                    } else {
                        Sounds.playSound("join-denied", p);
                    }
                    return;
                }
            }
        }
        ItemStack inHand = e.getItem();
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR) {
            if (inHand == null) {
                return;
            }
            a = Arena.getArenaByPlayer(p);
            if (a != null && a.isPlayer(p) && inHand.getType() == BedWars.nms.materialFireball()) {
                e.setCancelled(true);
                if ((double)(System.currentTimeMillis() - a.getFireballCooldowns().getOrDefault(p.getUniqueId(), 0L)) > this.fireballCooldown * 1000.0) {
                    a.getFireballCooldowns().put(p.getUniqueId(), System.currentTimeMillis());
                    Fireball fb = (Fireball)p.launchProjectile(Fireball.class);
                    Vector direction = p.getEyeLocation().getDirection();
                    fb = BedWars.nms.setFireballDirection(fb, direction);
                    fb.setVelocity(fb.getDirection().multiply(this.fireballSpeedMultiplier));
                    fb.setYield(this.fireballExplosionSize);
                    fb.setMetadata("bw1058", (MetadataValue)new FixedMetadataValue((Plugin)BedWars.plugin, (Object)"ceva"));
                    BedWars.nms.minusAmount(p, inHand, 1);
                }
            }
        }
    }

    @EventHandler
    public void disableItemFrameRotation(PlayerInteractEntityEvent e) {
        if (e == null) {
            return;
        }
        if (e.getRightClicked().getType() == EntityType.ITEM_FRAME) {
            if (((ItemFrame)e.getRightClicked()).getItem().getType().equals((Object)Material.AIR)) {
                ShopCache sc;
                ItemStack i = BedWars.nms.getItemInHand(e.getPlayer());
                if (i != null && i.getType() != Material.AIR && (sc = ShopCache.getShopCache(e.getPlayer().getUniqueId())) != null && InventoryListener.shouldCancelMovement(i, sc)) {
                    e.setCancelled(true);
                }
                return;
            }
            IArena a = Arena.getArenaByIdentifier(e.getPlayer().getWorld().getName());
            if (a != null) {
                e.setCancelled(true);
            }
            if (BedWars.getServerType() == ServerType.MULTIARENA && BedWars.getLobbyWorld().equals(e.getPlayer().getWorld().getName()) && !BreakPlace.isBuildSession(e.getPlayer())) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onEntityInteract(PlayerInteractEntityEvent e) {
        if (e == null) {
            return;
        }
        IArena a = Arena.getArenaByPlayer(e.getPlayer());
        if (a == null) {
            return;
        }
        Location l = e.getRightClicked().getLocation();
        for (ITeam t : a.getTeams()) {
            Location l2 = t.getShop();
            Location l3 = t.getTeamUpgrades();
            if (l.getBlockX() == l2.getBlockX() && l.getBlockY() == l2.getBlockY() && l.getBlockZ() == l2.getBlockZ()) {
                e.setCancelled(true);
                continue;
            }
            if (l.getBlockX() != l3.getBlockX() || l.getBlockY() != l3.getBlockY() || l.getBlockZ() != l3.getBlockZ()) continue;
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onBedEnter(PlayerBedEnterEvent e) {
        if (e == null) {
            return;
        }
        if (Arena.getArenaByPlayer(e.getPlayer()) != null) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onArmorManipulate(PlayerArmorStandManipulateEvent e) {
        if (e == null) {
            return;
        }
        if (e.isCancelled()) {
            return;
        }
        if (Arena.getArenaByPlayer(e.getPlayer()) != null) {
            e.setCancelled(true);
        }
        if (BedWars.getServerType() == ServerType.MULTIARENA && e.getPlayer().getLocation().getWorld().getName().equalsIgnoreCase(BedWars.getLobbyWorld()) && !BreakPlace.isBuildSession(e.getPlayer())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onCrafting(PrepareItemCraftEvent e) {
        if (e == null) {
            return;
        }
        if (Arena.getArenaByPlayer((Player)e.getView().getPlayer()) != null && BedWars.config.getBoolean("inventories.disable-crafting-table")) {
            e.getInventory().setResult(new ItemStack(Material.AIR));
        }
    }
}

