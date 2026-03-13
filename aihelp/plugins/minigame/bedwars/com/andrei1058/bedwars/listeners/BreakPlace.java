/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.block.Block
 *  org.bukkit.block.BlockFace
 *  org.bukkit.block.Sign
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.EntityType
 *  org.bukkit.entity.Player
 *  org.bukkit.entity.TNTPrimed
 *  org.bukkit.event.Event
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.block.BlockBreakEvent
 *  org.bukkit.event.block.BlockBurnEvent
 *  org.bukkit.event.block.BlockCanBuildEvent
 *  org.bukkit.event.block.BlockExplodeEvent
 *  org.bukkit.event.block.BlockFadeEvent
 *  org.bukkit.event.block.BlockPhysicsEvent
 *  org.bukkit.event.block.BlockPlaceEvent
 *  org.bukkit.event.block.SignChangeEvent
 *  org.bukkit.event.entity.EntityChangeBlockEvent
 *  org.bukkit.event.entity.EntityExplodeEvent
 *  org.bukkit.event.entity.ItemSpawnEvent
 *  org.bukkit.event.hanging.HangingBreakByEntityEvent
 *  org.bukkit.event.player.PlayerBucketEmptyEvent
 *  org.bukkit.event.player.PlayerBucketFillEvent
 *  org.bukkit.event.player.PlayerInteractEvent
 *  org.bukkit.plugin.Plugin
 *  org.jetbrains.annotations.NotNull
 */
package com.andrei1058.bedwars.listeners;

import com.andrei1058.bedwars.BedWars;
import com.andrei1058.bedwars.api.arena.GameState;
import com.andrei1058.bedwars.api.arena.IArena;
import com.andrei1058.bedwars.api.arena.NextEvent;
import com.andrei1058.bedwars.api.arena.generator.IGenerator;
import com.andrei1058.bedwars.api.arena.team.ITeam;
import com.andrei1058.bedwars.api.arena.team.TeamColor;
import com.andrei1058.bedwars.api.events.player.PlayerBedBreakEvent;
import com.andrei1058.bedwars.api.language.Language;
import com.andrei1058.bedwars.api.language.Messages;
import com.andrei1058.bedwars.api.region.Region;
import com.andrei1058.bedwars.api.server.ServerType;
import com.andrei1058.bedwars.api.util.BlastProtectionUtil;
import com.andrei1058.bedwars.arena.Arena;
import com.andrei1058.bedwars.configuration.Sounds;
import com.andrei1058.bedwars.popuptower.TowerEast;
import com.andrei1058.bedwars.popuptower.TowerNorth;
import com.andrei1058.bedwars.popuptower.TowerSouth;
import com.andrei1058.bedwars.popuptower.TowerWest;
import com.andrei1058.bedwars.support.paper.TeleportManager;
import java.io.File;
import java.lang.invoke.CallSite;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockCanBuildEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class BreakPlace
implements Listener {
    private static final List<Player> buildSession = new ArrayList<Player>();
    private final boolean allowFireBreak = BedWars.config.getBoolean("allow-fire-extinguish");
    private final BlastProtectionUtil blastProtection = new BlastProtectionUtil(BedWars.nms, BedWars.getAPI());

    @EventHandler
    public void onIceMelt(BlockFadeEvent e) {
        if (BedWars.getServerType() == ServerType.MULTIARENA && Objects.requireNonNull(e.getBlock().getLocation().getWorld()).getName().equalsIgnoreCase(BedWars.getLobbyWorld())) {
            e.setCancelled(true);
            return;
        }
        if (e.getBlock().getType() == Material.ICE && Arena.getArenaByIdentifier(e.getBlock().getWorld().getName()) != null) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onCactus(BlockPhysicsEvent e) {
        if (e.getBlock().getType() == Material.CACTUS && Arena.getArenaByIdentifier(e.getBlock().getWorld().getName()) != null) {
            e.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled=true)
    public void onBurn(@NotNull BlockBurnEvent event) {
        IArena arena = Arena.getArenaByIdentifier(event.getBlock().getWorld().getName());
        if (arena == null) {
            return;
        }
        if (!arena.isAllowMapBreak()) {
            event.setCancelled(true);
            return;
        }
        if (arena.isTeamBed(event.getBlock().getLocation())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        Player p;
        IArena a;
        if (e.isCancelled()) {
            return;
        }
        IArena arena = Arena.getArenaByIdentifier(e.getBlock().getWorld().getName());
        if (arena != null) {
            if (arena.getStatus() != GameState.playing) {
                e.setCancelled(true);
                return;
            }
            if (e.getItemInHand().getType().equals((Object)BedWars.nms.materialFireball()) && e.getBlockPlaced().getType().equals((Object)Material.FIRE)) {
                e.setCancelled(true);
            }
        }
        if ((a = Arena.getArenaByPlayer(p = e.getPlayer())) != null) {
            if (a.isSpectator(p)) {
                e.setCancelled(true);
                return;
            }
            if (a.getRespawnSessions().containsKey(p)) {
                e.setCancelled(true);
                return;
            }
            if (a.getStatus() != GameState.playing) {
                e.setCancelled(true);
                return;
            }
            if (e.getBlockPlaced().getLocation().getBlockY() >= a.getConfig().getInt("max-build-y")) {
                e.setCancelled(true);
                return;
            }
            for (Region r : a.getRegionsList()) {
                if (!r.isInRegion(e.getBlock().getLocation()) || !r.isProtected()) continue;
                e.setCancelled(true);
                p.sendMessage(Language.getMsg(p, Messages.INTERACT_CANNOT_PLACE_BLOCK));
                return;
            }
            if (e.getBlockPlaced().getType().toString().contains("STRIPPED_") && e.getBlock().getType().toString().contains("_WOOD") && null != arena && !arena.isAllowMapBreak()) {
                e.setCancelled(true);
                return;
            }
            a.addPlacedBlock(e.getBlock());
            if (e.getBlock().getType() == Material.TNT) {
                if (BedWars.config.getBoolean("tnt-prime-settings.auto-ignite")) {
                    e.getBlockPlaced().setType(Material.AIR);
                    TNTPrimed tnt = (TNTPrimed)Objects.requireNonNull(e.getBlock().getLocation().getWorld()).spawn(e.getBlock().getLocation().add(0.5, 0.0, 0.5), TNTPrimed.class);
                    tnt.setFuseTicks(BedWars.config.getInt("tnt-prime-settings.fuse-ticks"));
                    BedWars.nms.setSource(tnt, p);
                    return;
                }
            } else if (BedWars.shop.getBoolean("shop-specials.tower.enable") && e.getBlock().getType() == Material.valueOf((String)BedWars.shop.getString("shop-specials.tower.material"))) {
                e.setCancelled(true);
                Location loc = e.getBlock().getLocation();
                IArena a1 = Arena.getArenaByPlayer(p);
                TeamColor col = a1.getTeam(p).getColor();
                double rotation = (p.getLocation().getYaw() - 90.0f) % 360.0f;
                if (rotation < 0.0) {
                    rotation += 360.0;
                }
                if (45.0 <= rotation && rotation < 135.0) {
                    new TowerSouth(loc, e.getBlockPlaced(), col, p);
                } else if (225.0 <= rotation && rotation < 315.0) {
                    new TowerNorth(loc, e.getBlockPlaced(), col, p);
                } else if (135.0 <= rotation && rotation < 225.0) {
                    new TowerWest(loc, e.getBlockPlaced(), col, p);
                } else if (0.0 <= rotation && rotation < 45.0) {
                    new TowerEast(loc, e.getBlockPlaced(), col, p);
                } else if (315.0 <= rotation && rotation < 360.0) {
                    new TowerEast(loc, e.getBlockPlaced(), col, p);
                }
            }
            return;
        }
        if (BedWars.getServerType() == ServerType.MULTIARENA && Objects.requireNonNull(e.getBlock().getLocation().getWorld()).getName().equalsIgnoreCase(BedWars.getLobbyWorld()) && !BreakPlace.isBuildSession(p)) {
            e.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled=true)
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (BedWars.getServerType() == ServerType.MULTIARENA && player.getWorld().getName().equalsIgnoreCase(BedWars.getLobbyWorld()) && event.getClickedBlock() != null && event.getClickedBlock().getRelative(BlockFace.UP).getType() == Material.FIRE && !BreakPlace.isBuildSession(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
    public void onBlockBreakMonitor(BlockBreakEvent event) {
        IArena a = Arena.getArenaByPlayer(event.getPlayer());
        if (a != null) {
            a.removePlacedBlock(event.getBlock());
        }
    }

    @EventHandler
    public void onBlockDrop(ItemSpawnEvent event) {
        IArena arena = Arena.getArenaByIdentifier(event.getEntity().getWorld().getName());
        if (arena == null) {
            return;
        }
        Material material = event.getEntity().getItemStack().getType();
        if (BedWars.nms.isBed(material) || material.toString().equalsIgnoreCase("SEEDS") || material.toString().equalsIgnoreCase("WHEAT_SEEDS")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        if (e.isCancelled()) {
            return;
        }
        Player p = e.getPlayer();
        if (BedWars.getServerType() == ServerType.MULTIARENA && Objects.requireNonNull(e.getBlock().getLocation().getWorld()).getName().equalsIgnoreCase(BedWars.getLobbyWorld()) && !BreakPlace.isBuildSession(p)) {
            e.setCancelled(true);
            return;
        }
        IArena a = Arena.getArenaByPlayer(p);
        if (a != null) {
            if (!a.isPlayer(p)) {
                e.setCancelled(true);
                return;
            }
            if (a.getRespawnSessions().containsKey(p)) {
                e.setCancelled(true);
                return;
            }
            if (a.getStatus() != GameState.playing) {
                e.setCancelled(true);
                return;
            }
            switch (e.getBlock().getType().toString()) {
                case "LONG_GRASS": 
                case "TALL_GRASS": 
                case "TALL_SEAGRASS": 
                case "SEAGRASS": 
                case "SUGAR_CANE": 
                case "SUGAR_CANE_BLOCK": 
                case "GRASS_PATH": 
                case "DOUBLE_PLANT": {
                    if (e.isCancelled()) {
                        e.setCancelled(false);
                    }
                    return;
                }
                case "FIRE": {
                    if (!this.allowFireBreak) break;
                    e.setCancelled(false);
                    return;
                }
            }
            if (BedWars.nms.isBed(e.getBlock().getType())) {
                for (ITeam t : a.getTeams()) {
                    for (int x = e.getBlock().getX() - 2; x < e.getBlock().getX() + 2; ++x) {
                        for (int y = e.getBlock().getY() - 2; y < e.getBlock().getY() + 2; ++y) {
                            for (int z = e.getBlock().getZ() - 2; z < e.getBlock().getZ() + 2; ++z) {
                                if (t.getBed().getBlockX() != x || t.getBed().getBlockY() != y || t.getBed().getBlockZ() != z || t.isBedDestroyed()) continue;
                                if (t.isMember(p)) {
                                    p.sendMessage(Language.getMsg(p, Messages.INTERACT_CANNOT_BREAK_OWN_BED));
                                    e.setCancelled(true);
                                    if (e.getPlayer().getLocation().getBlock().getType().toString().contains("BED")) {
                                        TeleportManager.teleport((Entity)e.getPlayer(), e.getPlayer().getLocation().add(0.0, 0.5, 0.0));
                                    }
                                } else {
                                    e.setCancelled(false);
                                    t.setBedDestroyed(true);
                                    PlayerBedBreakEvent breakEvent = new PlayerBedBreakEvent(e.getPlayer(), a.getTeam(p), t, a, player -> {
                                        if (t.isMember((Player)player)) {
                                            return Language.getMsg(player, Messages.INTERACT_BED_DESTROY_CHAT_ANNOUNCEMENT_TO_VICTIM);
                                        }
                                        return Language.getMsg(player, Messages.INTERACT_BED_DESTROY_CHAT_ANNOUNCEMENT);
                                    }, player -> {
                                        if (t.isMember((Player)player)) {
                                            return Language.getMsg(player, Messages.INTERACT_BED_DESTROY_TITLE_ANNOUNCEMENT);
                                        }
                                        return null;
                                    }, player -> {
                                        if (t.isMember((Player)player)) {
                                            return Language.getMsg(player, Messages.INTERACT_BED_DESTROY_SUBTITLE_ANNOUNCEMENT);
                                        }
                                        return null;
                                    });
                                    Bukkit.getPluginManager().callEvent((Event)breakEvent);
                                    for (Player on : a.getWorld().getPlayers()) {
                                        if (breakEvent.getMessage() != null) {
                                            on.sendMessage(breakEvent.getMessage().apply(on).replace("{TeamColor}", t.getColor().chat().toString()).replace("{TeamName}", t.getDisplayName(Language.getPlayerLanguage(on))).replace("{PlayerColor}", a.getTeam(p).getColor().chat().toString()).replace("{PlayerName}", p.getDisplayName()).replace("{PlayerNameUnformatted}", p.getName()));
                                        }
                                        if (breakEvent.getTitle() != null && breakEvent.getSubTitle() != null) {
                                            BedWars.nms.sendTitle(on, breakEvent.getTitle().apply(on), breakEvent.getSubTitle().apply(on), 0, 40, 10);
                                        }
                                        if (t.isMember(on)) {
                                            Sounds.playSound("bed-destroy-own", on);
                                            continue;
                                        }
                                        Sounds.playSound("bed-destroy", on);
                                    }
                                }
                                return;
                            }
                        }
                    }
                }
            }
            for (Region r : a.getRegionsList()) {
                if (!r.isInRegion(e.getBlock().getLocation()) || !r.isProtected()) continue;
                e.setCancelled(true);
                p.sendMessage(Language.getMsg(p, Messages.INTERACT_CANNOT_BREAK_BLOCK));
                return;
            }
            if (!a.isAllowMapBreak() && !a.isBlockPlaced(e.getBlock())) {
                p.sendMessage(Language.getMsg(p, Messages.INTERACT_CANNOT_BREAK_BLOCK));
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onSignChange(SignChangeEvent e) {
        if (e == null) {
            return;
        }
        Player p = e.getPlayer();
        if (Objects.requireNonNull(e.getLine(0)).equalsIgnoreCase("[" + BedWars.mainCmd + "]")) {
            File dir = new File(BedWars.plugin.getDataFolder(), "/Arenas");
            boolean exists = false;
            if (dir.exists()) {
                IArena a;
                for (File f : Objects.requireNonNull(dir.listFiles())) {
                    if (!f.isFile() || !f.getName().contains(".yml") || !Objects.equals(e.getLine(1), f.getName().replace(".yml", ""))) continue;
                    exists = true;
                }
                ArrayList<CallSite> s = BedWars.signs.getYml().get("locations") == null ? new ArrayList<CallSite>() : new ArrayList(BedWars.signs.getYml().getStringList("locations"));
                if (exists) {
                    s.add((CallSite)((Object)(e.getLine(1) + "," + BedWars.signs.stringLocationConfigFormat(e.getBlock().getLocation()))));
                    BedWars.signs.set("locations", s);
                }
                if ((a = Arena.getArenaByName(e.getLine(1))) != null) {
                    p.sendMessage("\u00a7a\u25aa \u00a77Sign saved for arena: " + e.getLine(1));
                    a.addSign(e.getBlock().getLocation());
                    Sign b = (Sign)e.getBlock().getState();
                    int line = 0;
                    for (String string : BedWars.signs.getList("format")) {
                        e.setLine(line, string.replace("[on]", String.valueOf(a.getPlayers().size())).replace("[max]", String.valueOf(a.getMaxPlayers())).replace("[arena]", a.getDisplayName()).replace("[status]", a.getDisplayStatus(Language.getDefaultLanguage())).replace("[type]", String.valueOf(a.getMaxInTeam())));
                        ++line;
                    }
                    b.update(true);
                }
            } else {
                p.sendMessage("\u00a7c\u25aa \u00a77You didn't set any arena yet!");
            }
        }
    }

    @EventHandler
    public void onBucketFill(PlayerBucketFillEvent e) {
        IArena a;
        if (e.isCancelled()) {
            return;
        }
        if (BedWars.getServerType() == ServerType.MULTIARENA && Objects.requireNonNull(e.getPlayer().getLocation().getWorld()).getName().equalsIgnoreCase(BedWars.getLobbyWorld()) && !BreakPlace.isBuildSession(e.getPlayer())) {
            e.setCancelled(true);
        }
        if ((a = Arena.getArenaByPlayer(e.getPlayer())) != null && (a.isSpectator(e.getPlayer()) || a.getStatus() != GameState.playing || a.getRespawnSessions().containsKey(e.getPlayer()))) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onBucketEmpty(PlayerBucketEmptyEvent e) {
        IArena arena;
        if (e.isCancelled()) {
            return;
        }
        if (BedWars.getServerType() == ServerType.MULTIARENA && Objects.requireNonNull(e.getPlayer().getLocation().getWorld()).getName().equalsIgnoreCase(BedWars.getLobbyWorld()) && !BreakPlace.isBuildSession(e.getPlayer())) {
            e.setCancelled(true);
        }
        if ((arena = Arena.getArenaByIdentifier(e.getBlockClicked().getWorld().getName())) != null && arena.getStatus() != GameState.playing) {
            e.setCancelled(true);
            return;
        }
        Player p = e.getPlayer();
        IArena a = Arena.getArenaByPlayer(p);
        if (a != null) {
            if (a.isSpectator(p)) {
                e.setCancelled(true);
                return;
            }
            if (a.getRespawnSessions().containsKey(p)) {
                e.setCancelled(true);
                return;
            }
            if (a.getStatus() != GameState.playing) {
                e.setCancelled(true);
                return;
            }
            if (e.getBlockClicked().getLocation().getBlockY() >= a.getConfig().getInt("max-build-y")) {
                e.setCancelled(true);
                return;
            }
            try {
                for (ITeam t : a.getTeams()) {
                    if (t.getSpawn().distance(e.getBlockClicked().getLocation()) <= (double)a.getConfig().getInt("spawn-protection")) {
                        e.setCancelled(true);
                        p.sendMessage(Language.getMsg(p, Messages.INTERACT_CANNOT_PLACE_BLOCK));
                        return;
                    }
                    if (t.getShop().distance(e.getBlockClicked().getLocation()) <= (double)a.getConfig().getInt("shop-protection")) {
                        e.setCancelled(true);
                        p.sendMessage(Language.getMsg(p, Messages.INTERACT_CANNOT_PLACE_BLOCK));
                        return;
                    }
                    if (t.getTeamUpgrades().distance(e.getBlockClicked().getLocation()) <= (double)a.getConfig().getInt("upgrades-protection")) {
                        e.setCancelled(true);
                        p.sendMessage(Language.getMsg(p, Messages.INTERACT_CANNOT_PLACE_BLOCK));
                        return;
                    }
                    for (IGenerator o : t.getGenerators()) {
                        if (!(o.getLocation().distance(e.getBlockClicked().getLocation()) <= (double)a.getConfig().getInt("generator-protection"))) continue;
                        e.setCancelled(true);
                        p.sendMessage(Language.getMsg(p, Messages.INTERACT_CANNOT_PLACE_BLOCK));
                        return;
                    }
                }
                for (IGenerator o : a.getOreGenerators()) {
                    if (!(o.getLocation().distance(e.getBlockClicked().getLocation()) <= (double)a.getConfig().getInt("generator-protection"))) continue;
                    e.setCancelled(true);
                    p.sendMessage(Language.getMsg(p, Messages.INTERACT_CANNOT_PLACE_BLOCK));
                    return;
                }
            } catch (Exception exception) {
                // empty catch block
            }
            Bukkit.getScheduler().runTaskLater((Plugin)BedWars.plugin, () -> BedWars.nms.minusAmount(e.getPlayer(), e.getItemStack(), 1), 3L);
        }
    }

    @EventHandler
    public void onBlow(@NotNull EntityExplodeEvent e) {
        if (e.isCancelled()) {
            return;
        }
        IArena a = Arena.getArenaByIdentifier(e.getLocation().getWorld().getName());
        if (a != null) {
            if (a.getStatus() == GameState.playing) {
                e.blockList().removeIf(b -> this.blastProtection.isProtected(a, e.getLocation(), (Block)b, 0.3));
                return;
            }
            e.blockList().clear();
        }
    }

    @EventHandler
    public void onBlockExplode(@NotNull BlockExplodeEvent e) {
        if (e.isCancelled()) {
            return;
        }
        if (e.blockList().isEmpty()) {
            return;
        }
        IArena a = Arena.getArenaByIdentifier(((Block)e.blockList().get(0)).getWorld().getName());
        if (a != null && a.getNextEvent() != NextEvent.GAME_END) {
            e.blockList().removeIf(b -> this.blastProtection.isProtected(a, e.getBlock().getLocation(), (Block)b, 0.3));
        }
    }

    @EventHandler
    public void onPaintingRemove(HangingBreakByEntityEvent e) {
        IArena a = Arena.getArenaByIdentifier(e.getEntity().getWorld().getName());
        if (a == null) {
            if (BedWars.getServerType() == ServerType.SHARED) {
                return;
            }
            if (!BedWars.getLobbyWorld().equals(e.getEntity().getWorld().getName())) {
                return;
            }
        }
        if (e.getEntity().getType() == EntityType.PAINTING || e.getEntity().getType() == EntityType.ITEM_FRAME) {
            e.setCancelled(true);
        }
    }

    @EventHandler(priority=EventPriority.LOW)
    public void onBlockCanBuildEvent(BlockCanBuildEvent e) {
        if (e.isBuildable()) {
            return;
        }
        IArena a = Arena.getArenaByIdentifier(e.getBlock().getWorld().getName());
        if (a != null) {
            boolean bed = false;
            for (ITeam t : a.getTeams()) {
                block1: for (int x = e.getBlock().getX() - 1; x < e.getBlock().getX() + 1; ++x) {
                    for (int z = e.getBlock().getZ() - 1; z < e.getBlock().getZ() + 1; ++z) {
                        if (t.getBed().getBlockX() != x || t.getBed().getBlockY() != e.getBlock().getY() || t.getBed().getBlockZ() != z) continue;
                        e.setBuildable(false);
                        bed = true;
                        continue block1;
                    }
                }
                if (t.getBed().getBlockX() != e.getBlock().getX() || t.getBed().getBlockY() + 1 != e.getBlock().getY() || t.getBed().getBlockZ() != e.getBlock().getZ() || bed) continue;
                e.setBuildable(true);
                break;
            }
        }
    }

    @EventHandler
    public void soilChangeEntity(EntityChangeBlockEvent e) {
        if (e.getTo() == Material.DIRT && (e.getBlock().getType().toString().equals("FARMLAND") || e.getBlock().getType().toString().equals("SOIL")) && (Arena.getArenaByIdentifier(e.getBlock().getWorld().getName()) != null || e.getBlock().getWorld().getName().equals(BedWars.getLobbyWorld()))) {
            e.setCancelled(true);
        }
    }

    public static boolean isBuildSession(Player p) {
        return buildSession.contains(p);
    }

    public static void addBuildSession(Player p) {
        buildSession.add(p);
    }

    public static void removeBuildSession(Player p) {
        buildSession.remove(p);
    }
}

