/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.md_5.bungee.api.chat.ClickEvent
 *  net.md_5.bungee.api.chat.ClickEvent$Action
 *  net.md_5.bungee.api.chat.ComponentBuilder
 *  net.md_5.bungee.api.chat.HoverEvent
 *  net.md_5.bungee.api.chat.HoverEvent$Action
 *  net.md_5.bungee.api.chat.TextComponent
 *  org.bukkit.Bukkit
 *  org.bukkit.Color
 *  org.bukkit.FireworkEffect
 *  org.bukkit.FireworkEffect$Type
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.WorldBorder
 *  org.bukkit.block.BlockFace
 *  org.bukkit.configuration.file.YamlConfiguration
 *  org.bukkit.enchantments.Enchantment
 *  org.bukkit.entity.Firework
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.Inventory
 *  org.bukkit.inventory.ItemFlag
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.FireworkMeta
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.plugin.Plugin
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.andrei1058.bedwars.arena;

import com.andrei1058.bedwars.BedWars;
import com.andrei1058.bedwars.api.arena.GameState;
import com.andrei1058.bedwars.api.arena.IArena;
import com.andrei1058.bedwars.api.arena.generator.IGenerator;
import com.andrei1058.bedwars.api.arena.team.ITeam;
import com.andrei1058.bedwars.api.exceptions.InvalidMaterialException;
import com.andrei1058.bedwars.api.language.Language;
import com.andrei1058.bedwars.api.language.Messages;
import com.andrei1058.bedwars.api.region.Region;
import com.andrei1058.bedwars.api.server.ServerType;
import com.andrei1058.bedwars.configuration.Sounds;
import com.andrei1058.bedwars.stats.PlayerStats;
import com.andrei1058.bedwars.support.papi.SupportPAPI;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.WorldBorder;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Misc {
    public static void moveToLobbyOrKick(Player p, @Nullable IArena arena, boolean notAbandon) {
        if (BedWars.getServerType() != ServerType.BUNGEE) {
            if (!p.getWorld().getName().equalsIgnoreCase(BedWars.config.getLobbyWorldName())) {
                Location loc = BedWars.config.getConfigLoc("lobbyLoc");
                if (loc != null) {
                    try {
                        p.teleport(loc);
                    } catch (Exception ignored) {
                        Bukkit.getLogger().severe("Could not teleport player to lobby! Try setting the lobby again with /bw setLobby");
                    }
                } else {
                    Misc.forceKick(p, arena, notAbandon);
                    return;
                }
                if (arena != null) {
                    if (arena.isSpectator(p)) {
                        arena.removeSpectator(p, false);
                    } else {
                        arena.removePlayer(p, false);
                        if (!notAbandon && arena.getStatus() == GameState.playing && BedWars.config.getBoolean("mark-leave-as-abandon")) {
                            arena.abandonGame(p);
                        }
                    }
                }
            } else {
                Misc.forceKick(p, arena, notAbandon);
            }
            return;
        }
        Misc.forceKick(p, arena, notAbandon);
    }

    private static void forceKick(Player p, @Nullable IArena arena, boolean notAbandon) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Connect");
        out.writeUTF(BedWars.config.getYml().getString("lobbyServer"));
        p.sendPluginMessage((Plugin)BedWars.plugin, "BungeeCord", out.toByteArray());
        if (arena != null && !notAbandon && arena.getStatus() == GameState.playing && BedWars.config.getBoolean("mark-leave-as-abandon")) {
            arena.abandonGame(p);
        }
        if (BedWars.getServerType() == ServerType.BUNGEE) {
            Bukkit.getScheduler().runTaskLater((Plugin)BedWars.plugin, () -> {
                if (p.isOnline()) {
                    p.kickPlayer(Language.getMsg(p, Messages.ARENA_RESTART_PLAYER_KICK));
                    if (arena != null && !notAbandon && arena.getStatus() == GameState.playing && BedWars.config.getBoolean("mark-leave-as-abandon")) {
                        arena.abandonGame(p);
                    }
                }
            }, 30L);
        }
    }

    public static void launchFirework(Player p) {
        Color[] colors = new Color[]{Color.WHITE, Color.AQUA, Color.BLUE, Color.FUCHSIA, Color.GRAY, Color.GREEN, Color.LIME, Color.RED, Color.YELLOW, Color.BLACK, Color.MAROON, Color.NAVY, Color.OLIVE, Color.ORANGE, Color.PURPLE};
        Random r = new Random();
        Firework fw = (Firework)p.getWorld().spawn(p.getEyeLocation(), Firework.class);
        FireworkMeta meta = fw.getFireworkMeta();
        meta.setPower(1);
        meta.addEffect(FireworkEffect.builder().withFade(colors[r.nextInt(colors.length - 1)]).withTrail().withColor(colors[r.nextInt(colors.length - 1)]).with(FireworkEffect.Type.BALL_LARGE).build());
        fw.setFireworkMeta(meta);
        fw.setVelocity(p.getEyeLocation().getDirection());
    }

    public static void launchFirework(Location l) {
        Color[] colors = new Color[]{Color.WHITE, Color.AQUA, Color.BLUE, Color.FUCHSIA, Color.GRAY, Color.GREEN, Color.LIME, Color.RED, Color.YELLOW, Color.BLACK, Color.MAROON, Color.NAVY, Color.OLIVE, Color.ORANGE, Color.PURPLE};
        Random r = new Random();
        Firework fw = (Firework)l.getWorld().spawn(l, Firework.class);
        FireworkMeta meta = fw.getFireworkMeta();
        meta.setPower(1);
        meta.addEffect(FireworkEffect.builder().withFade(colors[r.nextInt(colors.length - 1)]).withTrail().withColor(colors[r.nextInt(colors.length - 1)]).with(FireworkEffect.Type.BALL_LARGE).build());
        fw.setFireworkMeta(meta);
    }

    public static String replaceFirst(String text, String regex, String replacement) {
        return text.replaceFirst("(?s)" + regex + "(?!.*?" + regex + ")", replacement);
    }

    static ItemStack createItem(Material material, byte data, boolean enchanted, String name, List<String> lore, Player owner, String metaKey, String metaData) {
        ItemStack i = new ItemStack(material, 1, (short)data);
        ItemMeta im = i.getItemMeta();
        im.setDisplayName(name);
        im.setLore(lore);
        if (enchanted) {
            im.addEnchant(Enchantment.LUCK, 1, true);
            im.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ENCHANTS});
        }
        i.setItemMeta(im);
        if (!metaData.isEmpty() && !metaKey.isEmpty()) {
            i = BedWars.nms.addCustomData(i, metaKey + "_" + metaData);
        }
        if (owner != null && BedWars.nms.isPlayerHead(material.toString(), data)) {
            i = BedWars.nms.getPlayerHead(owner, i);
        }
        return i;
    }

    public static ItemStack createItemStack(String material, int data, String name, List<String> lore, boolean enchanted, String customData) throws InvalidMaterialException {
        Material m;
        try {
            m = Material.valueOf((String)material);
        } catch (Exception e) {
            throw new InvalidMaterialException(material);
        }
        ItemStack i = new ItemStack(m, 1, (short)data);
        ItemMeta im = i.getItemMeta();
        im.setDisplayName(name);
        im.setLore(lore);
        if (enchanted) {
            im.addEnchant(Enchantment.LUCK, 1, true);
            im.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ENCHANTS});
        }
        i.setItemMeta(im);
        if (!customData.isEmpty()) {
            i = BedWars.nms.addCustomData(i, customData);
        }
        return i;
    }

    public static BlockFace getDirection(Location loc) {
        int rotation = (int)loc.getYaw();
        if (rotation < 0) {
            rotation += 360;
        }
        if (0 <= rotation && rotation < 22) {
            return BlockFace.SOUTH;
        }
        if (22 <= rotation && rotation < 67) {
            return BlockFace.SOUTH;
        }
        if (67 <= rotation && rotation < 112) {
            return BlockFace.WEST;
        }
        if (112 <= rotation && rotation < 157) {
            return BlockFace.NORTH;
        }
        if (157 <= rotation && rotation < 202) {
            return BlockFace.NORTH;
        }
        if (202 <= rotation && rotation < 247) {
            return BlockFace.NORTH;
        }
        if (247 <= rotation && rotation < 292) {
            return BlockFace.EAST;
        }
        if (292 <= rotation && rotation < 337) {
            return BlockFace.SOUTH;
        }
        if (337 <= rotation && rotation < 360) {
            return BlockFace.SOUTH;
        }
        return BlockFace.SOUTH;
    }

    public static boolean isProjectile(Material i) {
        return Material.EGG == i || BedWars.nms.materialFireball() == i || BedWars.nms.materialSnowball() == i || Material.ARROW == i;
    }

    public static TextComponent msgHoverClick(String msg, String hover, String click, ClickEvent.Action clickAction) {
        TextComponent tc = new TextComponent(msg);
        tc.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(hover).create()));
        tc.setClickEvent(new ClickEvent(clickAction, click));
        return tc;
    }

    public static void addDefaultStatsItem(YamlConfiguration yml, int slot, Material itemstack, int data, String path) {
        yml.addDefault("stats-gui.%path%.material".replace("%path%", path), (Object)itemstack.toString());
        yml.addDefault("stats-gui.%path%.data".replace("%path%", path), (Object)data);
        yml.addDefault("stats-gui.%path%.slot".replace("%path%", path), (Object)slot);
    }

    public static void openStatsGUI(Player p) {
        Bukkit.getScheduler().runTask((Plugin)BedWars.plugin, () -> {
            Inventory inv = Bukkit.createInventory(null, (int)BedWars.config.getInt("stats-gui.inv-size"), (String)Misc.replaceStatsPlaceholders(p, Language.getMsg(p, Messages.PLAYER_STATS_GUI_INV_NAME), true));
            for (String s : BedWars.config.getYml().getConfigurationSection("stats-gui").getKeys(false)) {
                if ("stats-gui.inv-size".contains(s)) continue;
                ItemStack i = BedWars.nms.createItemStack(BedWars.config.getYml().getString("stats-gui.%path%.material".replace("%path%", s)).toUpperCase(), 1, (short)BedWars.config.getInt("stats-gui.%path%.data".replace("%path%", s)));
                ItemMeta im = i.getItemMeta();
                im.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ATTRIBUTES});
                im.setDisplayName(Misc.replaceStatsPlaceholders(p, Language.getMsg(p, Messages.PLAYER_STATS_GUI_PATH + "-" + s + "-name"), true));
                ArrayList<String> lore = new ArrayList<String>();
                for (String string : Language.getList(p, Messages.PLAYER_STATS_GUI_PATH + "-" + s + "-lore")) {
                    lore.add(Misc.replaceStatsPlaceholders(p, string, true));
                }
                im.setLore(lore);
                i.setItemMeta(im);
                inv.setItem(BedWars.config.getInt("stats-gui.%path%.slot".replace("%path%", s)), i);
            }
            p.openInventory(inv);
            Sounds.playSound("stats-gui-open", p);
        });
    }

    public static String replaceStatsPlaceholders(Player player, @NotNull String s, boolean papiReplacements) {
        PlayerStats stats = BedWars.getStatsManager().get(player.getUniqueId());
        if (s.contains("{kills}")) {
            s = s.replace("{kills}", String.valueOf(stats.getKills()));
        }
        if (s.contains("{deaths}")) {
            s = s.replace("{deaths}", String.valueOf(stats.getDeaths()));
        }
        if (s.contains("{losses}")) {
            s = s.replace("{losses}", String.valueOf(stats.getLosses()));
        }
        if (s.contains("{wins}")) {
            s = s.replace("{wins}", String.valueOf(stats.getWins()));
        }
        if (s.contains("{finalKills}")) {
            s = s.replace("{finalKills}", String.valueOf(stats.getFinalKills()));
        }
        if (s.contains("{finalDeaths}")) {
            s = s.replace("{finalDeaths}", String.valueOf(stats.getFinalDeaths()));
        }
        if (s.contains("{bedsDestroyed}")) {
            s = s.replace("{bedsDestroyed}", String.valueOf(stats.getBedsDestroyed()));
        }
        if (s.contains("{gamesPlayed}")) {
            s = s.replace("{gamesPlayed}", String.valueOf(stats.getGamesPlayed()));
        }
        if (s.contains("{firstPlay}")) {
            s = s.replace("{firstPlay}", new SimpleDateFormat(Language.getMsg(player, Messages.FORMATTING_STATS_DATE_FORMAT)).format(stats.getFirstPlay() != null ? Timestamp.from(stats.getFirstPlay()) : Timestamp.from(Instant.now())));
        }
        if (s.contains("{lastPlay}")) {
            s = s.replace("{lastPlay}", new SimpleDateFormat(Language.getMsg(player, Messages.FORMATTING_STATS_DATE_FORMAT)).format(stats.getLastPlay() != null ? Timestamp.from(stats.getLastPlay()) : Timestamp.from(Instant.now())));
        }
        if (s.contains("{player}")) {
            s = s.replace("{player}", player.getDisplayName());
        }
        if (s.contains("{playername")) {
            s = s.replace("{playername}", player.getName());
        }
        if (s.contains("{prefix}")) {
            s = s.replace("{prefix}", BedWars.getChatSupport().getPrefix(player));
        }
        return papiReplacements ? SupportPAPI.getSupportPAPI().replace(player, s) : s;
    }

    public static boolean isNumber(String s) {
        try {
            Double.parseDouble(s);
        } catch (Exception e) {
            try {
                Integer.parseInt(s);
            } catch (Exception ex) {
                try {
                    Long.parseLong(s);
                } catch (Exception exx) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean isOutsideOfBorder(Location l) {
        WorldBorder border = l.getWorld().getWorldBorder();
        double radius = border.getSize() / 2.0 + (double)border.getWarningDistance();
        Location center = border.getCenter();
        return center.distance(l) >= radius;
    }

    public static boolean isBuildProtected(Location l, IArena a) {
        for (Region region : a.getRegionsList()) {
            if (!region.isInRegion(l)) continue;
            return true;
        }
        for (ITeam t : a.getTeams()) {
            for (IGenerator o : t.getGenerators()) {
                if (!(o.getLocation().distance(l) <= (double)a.getConfig().getInt("generator-protection"))) continue;
                return true;
            }
        }
        for (IGenerator o : a.getOreGenerators()) {
            if (!(o.getLocation().distance(l) <= (double)a.getConfig().getInt("generator-protection"))) continue;
            return true;
        }
        return Misc.isOutsideOfBorder(l);
    }

    public static Location minLoc(Location loc1, Location loc2) {
        if (loc1.getWorld() != loc2.getWorld()) {
            throw new IllegalStateException("Locations are not in the same world!");
        }
        double x = Math.min(loc1.getX(), loc2.getX());
        double y = Math.min(loc1.getY(), loc2.getY());
        double z = Math.min(loc1.getZ(), loc2.getZ());
        return new Location(loc1.getWorld(), x, y, z);
    }

    public static Location maxLoc(Location loc1, Location loc2) {
        if (loc1.getWorld() != loc2.getWorld()) {
            throw new IllegalStateException("Locations are not in the same world!");
        }
        double x = Math.max(loc1.getX(), loc2.getX());
        double y = Math.max(loc1.getY(), loc2.getY());
        double z = Math.max(loc1.getZ(), loc2.getZ());
        return new Location(loc1.getWorld(), x, y, z);
    }
}

