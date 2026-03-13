/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.GameMode
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.enchantments.Enchantment
 *  org.bukkit.entity.ArmorStand
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.EntityType
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.event.player.PlayerTeleportEvent$TeleportCause
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.inventory.meta.LeatherArmorMeta
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 *  org.bukkit.util.Vector
 *  org.jetbrains.annotations.NotNull
 */
package com.andrei1058.bedwars.arena.team;

import com.andrei1058.bedwars.BedWars;
import com.andrei1058.bedwars.api.arena.generator.GeneratorType;
import com.andrei1058.bedwars.api.arena.generator.IGenerator;
import com.andrei1058.bedwars.api.arena.team.ITeam;
import com.andrei1058.bedwars.api.arena.team.TeamColor;
import com.andrei1058.bedwars.api.arena.team.TeamEnchant;
import com.andrei1058.bedwars.api.events.player.PlayerFirstSpawnEvent;
import com.andrei1058.bedwars.api.events.player.PlayerReSpawnEvent;
import com.andrei1058.bedwars.api.language.Language;
import com.andrei1058.bedwars.api.language.Messages;
import com.andrei1058.bedwars.api.region.Cuboid;
import com.andrei1058.bedwars.api.upgrades.EnemyBaseEnterTrap;
import com.andrei1058.bedwars.arena.Arena;
import com.andrei1058.bedwars.arena.OreGenerator;
import com.andrei1058.bedwars.configuration.Sounds;
import com.andrei1058.bedwars.shop.ShopCache;
import com.andrei1058.bedwars.support.paper.TeleportManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public class BedWarsTeam
implements ITeam {
    private List<Player> members = new ArrayList<Player>();
    private TeamColor color;
    private Location spawn;
    private Location bed;
    private Location shop;
    private Location teamUpgrades;
    private String name;
    private Arena arena;
    private boolean bedDestroyed = false;
    private Vector killDropsLoc = null;
    private List<IGenerator> generators = new ArrayList<IGenerator>();
    private ConcurrentHashMap<String, Integer> teamUpgradeList = new ConcurrentHashMap();
    private List<PotionEffect> teamEffects = new ArrayList<PotionEffect>();
    private List<PotionEffect> base = new ArrayList<PotionEffect>();
    private List<TeamEnchant> bowsEnchantments = new ArrayList<TeamEnchant>();
    private List<TeamEnchant> swordsEnchantemnts = new ArrayList<TeamEnchant>();
    private List<TeamEnchant> armorsEnchantemnts = new ArrayList<TeamEnchant>();
    private HashMap<UUID, BedHolo> beds = new HashMap();
    private LinkedList<EnemyBaseEnterTrap> enemyBaseEnterTraps = new LinkedList();
    private int dragons = 1;
    private List<Player> membersCache = new ArrayList<Player>();
    public static HashMap<UUID, Long> reSpawnInvulnerability = new HashMap();
    private UUID identity;

    public BedWarsTeam(String name, TeamColor color, Location spawn, Location bed, Location shop, Location teamUpgrades, Arena arena) {
        if (arena == null) {
            return;
        }
        this.name = name;
        this.color = color;
        this.spawn = spawn;
        this.bed = bed;
        this.arena = arena;
        this.shop = shop;
        this.teamUpgrades = teamUpgrades;
        Language.saveIfNotExists("team-name-{arena}-{team}".replace("{arena}", this.getArena().getArenaName()).replace("{team}", this.getName()), name);
        arena.getRegionsList().add(new Cuboid(spawn, arena.getConfig().getInt("spawn-protection"), true));
        Location drops = this.getArena().getConfig().getArenaLoc("Team." + this.getName() + ".kill-drops-loc");
        if (drops != null) {
            this.setKillDropsLocation(drops);
        }
        this.identity = UUID.randomUUID();
    }

    @Override
    public int getSize() {
        return this.members.size();
    }

    @Override
    public void addPlayers(Player ... players) {
        if (players == null) {
            return;
        }
        for (Player p : players) {
            if (p == null) continue;
            this.members.removeIf(player -> player.getUniqueId().equals(p.getUniqueId()));
            this.members.add(p);
            this.membersCache.removeIf(player -> player.getUniqueId().equals(p.getUniqueId()));
            this.membersCache.add(p);
            new BedHolo(p, this.getArena());
        }
    }

    @Override
    public void firstSpawn(Player p) {
        if (p == null) {
            return;
        }
        TeleportManager.teleportC((Entity)p, this.spawn, PlayerTeleportEvent.TeleportCause.PLUGIN);
        p.setGameMode(GameMode.SURVIVAL);
        p.setCanPickupItems(true);
        BedWars.nms.setCollide(p, this.getArena(), true);
        this.sendDefaultInventory(p, true);
        Bukkit.getPluginManager().callEvent((Event)new PlayerFirstSpawnEvent(p, this.getArena(), this));
    }

    @Override
    public void spawnNPCs() {
        if (this.getMembers().isEmpty() && this.getArena().getConfig().getBoolean("disable-npcs-for-empty-teams")) {
            return;
        }
        Bukkit.getScheduler().runTaskLater((Plugin)BedWars.plugin, () -> {
            BedWars.nms.colorBed(this);
            BedWars.nms.spawnShop(this.getArena().getConfig().getArenaLoc("Team." + this.getName() + ".Upgrade"), this.getArena().getMaxInTeam() > 1 ? Messages.NPC_NAME_TEAM_UPGRADES : Messages.NPC_NAME_SOLO_UPGRADES, this.getArena().getPlayers(), this.getArena());
            BedWars.nms.spawnShop(this.getArena().getConfig().getArenaLoc("Team." + this.getName() + ".Shop"), this.getArena().getMaxInTeam() > 1 ? Messages.NPC_NAME_TEAM_SHOP : Messages.NPC_NAME_SOLO_SHOP, this.getArena().getPlayers(), this.getArena());
        }, 20L);
        Cuboid c1 = new Cuboid(this.getArena().getConfig().getArenaLoc("Team." + this.getName() + ".Upgrade"), this.getArena().getConfig().getInt("upgrades-protection"), true);
        c1.setMinY(c1.getMinY() - 1);
        c1.setMaxY(c1.getMaxY() + 4);
        this.getArena().getRegionsList().add(c1);
        Cuboid c2 = new Cuboid(this.getArena().getConfig().getArenaLoc("Team." + this.getName() + ".Shop"), this.getArena().getConfig().getInt("shop-protection"), true);
        c2.setMinY(c2.getMinY() - 1);
        c2.setMaxY(c2.getMaxY() + 4);
        this.getArena().getRegionsList().add(c2);
    }

    @Override
    public void reJoin(@NotNull Player p) {
        this.reJoin(p, BedWars.config.getInt("countdowns.player-re-spawn"));
    }

    @Override
    public void reJoin(@NotNull Player p, int respawnTime) {
        this.addPlayers(p);
        this.arena.startReSpawnSession(p, respawnTime);
    }

    @Override
    public void sendDefaultInventory(Player p, boolean clean) {
        if (clean) {
            p.getInventory().clear();
        }
        String path = BedWars.config.getYml().get("start-items-per-group." + this.arena.getGroup()) == null ? "start-items-per-group.Default" : "start-items-per-group." + this.arena.getGroup();
        for (String s : BedWars.config.getYml().getStringList(path)) {
            String[] parm = s.split(",");
            if (parm.length == 0) continue;
            try {
                ItemStack i;
                if (parm.length > 1) {
                    try {
                        Integer.parseInt(parm[1]);
                    } catch (Exception ex) {
                        BedWars.plugin.getLogger().severe(parm[1] + " is not an integer at: " + s + " (config)");
                        continue;
                    }
                    i = new ItemStack(Material.valueOf((String)parm[0]), Integer.parseInt(parm[1]));
                } else {
                    i = new ItemStack(Material.valueOf((String)parm[0]));
                }
                if (parm.length > 2) {
                    try {
                        Integer.parseInt(parm[2]);
                    } catch (Exception ex) {
                        BedWars.plugin.getLogger().severe(parm[2] + " is not an integer at: " + s + " (config)");
                        continue;
                    }
                    i.setAmount(Integer.parseInt(parm[2]));
                }
                ItemMeta im = i.getItemMeta();
                if (parm.length > 3) {
                    im.setDisplayName(ChatColor.translateAlternateColorCodes((char)'&', (String)parm[3]));
                }
                BedWars.nms.setUnbreakable(im);
                i.setItemMeta(im);
                i = BedWars.nms.addCustomData(i, "DEFAULT_ITEM");
                if (BedWars.nms.isSword(i)) {
                    boolean hasSword = false;
                    for (ItemStack item : p.getInventory().getContents()) {
                        if (item == null || item.getType() == Material.AIR || !BedWars.nms.isSword(item)) continue;
                        hasSword = true;
                        break;
                    }
                    if (hasSword) continue;
                    p.getInventory().addItem(new ItemStack[]{i});
                    continue;
                }
                if (BedWars.nms.isBow(i)) {
                    boolean hasBow = false;
                    for (ItemStack item : p.getInventory().getContents()) {
                        if (item == null || item.getType() == Material.AIR || !BedWars.nms.isBow(item)) continue;
                        hasBow = true;
                        break;
                    }
                    if (hasBow) continue;
                    p.getInventory().addItem(new ItemStack[]{i});
                    continue;
                }
                p.getInventory().addItem(new ItemStack[]{i});
            } catch (Exception exception) {}
        }
        this.sendArmor(p);
    }

    @Override
    public void defaultSword(Player p, boolean sword) {
        if (!sword) {
            return;
        }
        String path = BedWars.config.getYml().get("start-items-per-group." + this.arena.getGroup()) == null ? "start-items-per-group.Default" : "start-items-per-group." + this.arena.getGroup();
        for (String s : BedWars.config.getYml().getStringList(path)) {
            String[] parm = s.split(",");
            if (parm.length == 0) continue;
            try {
                ItemStack i;
                if (parm.length > 1) {
                    try {
                        Integer.parseInt(parm[1]);
                    } catch (Exception ex) {
                        BedWars.plugin.getLogger().severe(parm[1] + " is not an integer at: " + s + " (config)");
                        continue;
                    }
                    i = new ItemStack(Material.valueOf((String)parm[0]), Integer.parseInt(parm[1]));
                } else {
                    i = new ItemStack(Material.valueOf((String)parm[0]));
                }
                if (parm.length > 2) {
                    try {
                        Integer.parseInt(parm[2]);
                    } catch (Exception ex) {
                        BedWars.plugin.getLogger().severe(parm[2] + " is not an integer at: " + s + " (config)");
                        continue;
                    }
                    i.setAmount(Integer.parseInt(parm[2]));
                }
                ItemMeta im = i.getItemMeta();
                if (parm.length > 3) {
                    im.setDisplayName(ChatColor.translateAlternateColorCodes((char)'&', (String)parm[3]));
                }
                BedWars.nms.setUnbreakable(im);
                i.setItemMeta(im);
                if (!BedWars.nms.isSword(i = BedWars.nms.addCustomData(i, "DEFAULT_ITEM"))) continue;
                p.getInventory().addItem(new ItemStack[]{i});
                break;
            } catch (Exception exception) {
            }
        }
    }

    public void spawnGenerators() {
        for (String type : new String[]{"Iron", "Gold"}) {
            GeneratorType gt = GeneratorType.valueOf(type.toUpperCase());
            List<Object> locs = new ArrayList<Location>();
            Object o = this.getArena().getConfig().getYml().get("Team." + this.getName() + "." + type);
            if (o instanceof String) {
                locs.add(this.getArena().getConfig().getArenaLoc("Team." + this.getName() + "." + type));
            } else {
                locs = this.getArena().getConfig().getArenaLocations("Team." + this.getName() + "." + type);
            }
            for (Location location : locs) {
                OreGenerator gen = new OreGenerator(location, this.getArena(), gt, this);
                this.generators.add(gen);
            }
        }
    }

    @Override
    public void respawnMember(@NotNull Player p) {
        ItemMeta im;
        if (reSpawnInvulnerability.containsKey(p.getUniqueId())) {
            reSpawnInvulnerability.replace(p.getUniqueId(), System.currentTimeMillis() + (long)BedWars.config.getInt("re-spawn-invulnerability"));
        } else {
            reSpawnInvulnerability.put(p.getUniqueId(), System.currentTimeMillis() + (long)BedWars.config.getInt("re-spawn-invulnerability"));
        }
        TeleportManager.teleportC((Entity)p, this.getSpawn(), PlayerTeleportEvent.TeleportCause.PLUGIN);
        p.setVelocity(new Vector(0, 0, 0));
        p.removePotionEffect(PotionEffectType.INVISIBILITY);
        BedWars.nms.setCollide(p, this.arena, true);
        p.setAllowFlight(false);
        p.setFlying(false);
        p.setHealth(20.0);
        Bukkit.getScheduler().runTaskLater((Plugin)BedWars.plugin, () -> {
            this.getArena().getRespawnSessions().remove(p);
            for (Player inGame : this.arena.getPlayers()) {
                if (inGame.equals((Object)p)) continue;
                BedWars.nms.spigotShowPlayer(p, inGame);
                BedWars.nms.spigotShowPlayer(inGame, p);
            }
            for (Player spectator : this.arena.getSpectators()) {
                BedWars.nms.spigotShowPlayer(p, spectator);
            }
        }, 8L);
        BedWars.nms.sendTitle(p, Language.getMsg(p, Messages.PLAYER_DIE_RESPAWNED_TITLE), "", 0, 20, 10);
        this.sendDefaultInventory(p, false);
        ShopCache sc = ShopCache.getShopCache(p.getUniqueId());
        if (sc != null) {
            sc.managePermanentsAndDowngradables(this.getArena());
        }
        p.setHealth(20.0);
        if (!this.getBaseEffects().isEmpty()) {
            for (PotionEffect ef : this.getBaseEffects()) {
                p.addPotionEffect(ef, true);
            }
        }
        if (!this.getTeamEffects().isEmpty()) {
            for (PotionEffect ef : this.getTeamEffects()) {
                p.addPotionEffect(ef, true);
            }
        }
        if (!this.getBowsEnchantments().isEmpty()) {
            for (ItemStack i : p.getInventory().getContents()) {
                if (i == null) continue;
                if (i.getType() == Material.BOW) {
                    im = i.getItemMeta();
                    for (TeamEnchant e : this.getBowsEnchantments()) {
                        im.addEnchant(e.getEnchantment(), e.getAmplifier(), true);
                    }
                    i.setItemMeta(im);
                }
                p.updateInventory();
            }
        }
        if (!this.getSwordsEnchantments().isEmpty()) {
            for (ItemStack i : p.getInventory().getContents()) {
                if (i == null) continue;
                if (BedWars.nms.isSword(i)) {
                    im = i.getItemMeta();
                    for (TeamEnchant e : this.getSwordsEnchantments()) {
                        im.addEnchant(e.getEnchantment(), e.getAmplifier(), true);
                    }
                    i.setItemMeta(im);
                }
                p.updateInventory();
            }
        }
        if (!this.getArmorsEnchantments().isEmpty()) {
            for (ItemStack i : p.getInventory().getArmorContents()) {
                if (i == null) continue;
                if (BedWars.nms.isArmor(i)) {
                    im = i.getItemMeta();
                    for (TeamEnchant e : this.getArmorsEnchantments()) {
                        im.addEnchant(e.getEnchantment(), e.getAmplifier(), true);
                    }
                    i.setItemMeta(im);
                }
                p.updateInventory();
            }
        }
        Bukkit.getPluginManager().callEvent((Event)new PlayerReSpawnEvent(p, this.getArena(), this));
        BedWars.nms.sendPlayerSpawnPackets(p, this.getArena());
        Bukkit.getScheduler().runTaskLater((Plugin)BedWars.plugin, () -> {
            if (this.getArena() != null) {
                BedWars.nms.sendPlayerSpawnPackets(p, this.getArena());
                for (Player on : this.getArena().getShowTime().keySet()) {
                    BedWars.nms.hideArmor(on, p);
                }
            }
        }, 10L);
        Sounds.playSound("player-re-spawn", p);
    }

    private ItemStack createArmor(Material material) {
        ItemStack i = new ItemStack(material);
        LeatherArmorMeta lam = (LeatherArmorMeta)i.getItemMeta();
        lam.setColor(this.color.bukkitColor());
        BedWars.nms.setUnbreakable((ItemMeta)lam);
        i.setItemMeta((ItemMeta)lam);
        return i;
    }

    @Override
    public void sendArmor(Player p) {
        if (p.getInventory().getHelmet() == null) {
            p.getInventory().setHelmet(this.createArmor(Material.LEATHER_HELMET));
        }
        if (p.getInventory().getChestplate() == null) {
            p.getInventory().setChestplate(this.createArmor(Material.LEATHER_CHESTPLATE));
        }
        if (p.getInventory().getLeggings() == null) {
            p.getInventory().setLeggings(this.createArmor(Material.LEATHER_LEGGINGS));
        }
        if (p.getInventory().getBoots() == null) {
            p.getInventory().setBoots(this.createArmor(Material.LEATHER_BOOTS));
        }
    }

    @Override
    public UUID getIdentity() {
        return this.identity;
    }

    @Override
    public void addTeamEffect(PotionEffectType pef, int amp, int duration) {
        this.getTeamEffects().add(new PotionEffect(pef, duration, amp));
        for (Player p : this.getMembers()) {
            p.addPotionEffect(new PotionEffect(pef, duration, amp), true);
        }
    }

    @Override
    public void addBaseEffect(PotionEffectType pef, int amp, int duration) {
        this.getBaseEffects().add(new PotionEffect(pef, duration, amp));
        for (Player p : new ArrayList<Player>(this.getMembers())) {
            if (!(p.getLocation().distance(this.getBed()) <= (double)this.getArena().getIslandRadius())) continue;
            for (PotionEffect e : this.getBaseEffects()) {
                p.addPotionEffect(e, true);
            }
        }
    }

    @Override
    public void addBowEnchantment(Enchantment e, int a) {
        this.getBowsEnchantments().add(new Enchant(e, a));
        for (Player p : this.getMembers()) {
            for (ItemStack i : p.getInventory().getContents()) {
                if (i == null || i.getType() != Material.BOW) continue;
                ItemMeta im = i.getItemMeta();
                im.addEnchant(e, a, true);
                i.setItemMeta(im);
            }
            p.updateInventory();
        }
    }

    @Override
    public void addSwordEnchantment(Enchantment e, int a) {
        this.getSwordsEnchantments().add(new Enchant(e, a));
        for (Player p : this.getMembers()) {
            for (ItemStack i : p.getInventory().getContents()) {
                if (i == null || !BedWars.nms.isSword(i) && !BedWars.nms.isAxe(i)) continue;
                ItemMeta im = i.getItemMeta();
                im.addEnchant(e, a, true);
                i.setItemMeta(im);
            }
            p.updateInventory();
        }
    }

    @Override
    public void addArmorEnchantment(Enchantment e, int a) {
        this.getArmorsEnchantments().add(new Enchant(e, a));
        for (Player p : this.getMembers()) {
            for (ItemStack i : p.getInventory().getArmorContents()) {
                if (i == null || !BedWars.nms.isArmor(i)) continue;
                ItemMeta im = i.getItemMeta();
                im.addEnchant(e, a, true);
                i.setItemMeta(im);
            }
            p.updateInventory();
        }
        Bukkit.getScheduler().runTaskLater((Plugin)BedWars.plugin, () -> {
            for (Player m : this.getMembers()) {
                if (!m.hasPotionEffect(PotionEffectType.INVISIBILITY)) continue;
                for (Player p : this.getArena().getPlayers()) {
                    BedWars.nms.hideArmor(m, p);
                }
                for (Player p : this.getArena().getSpectators()) {
                    BedWars.nms.hideArmor(m, p);
                }
            }
        }, 20L);
    }

    @Override
    public boolean isMember(Player u) {
        if (u == null) {
            return false;
        }
        return this.members.contains(u);
    }

    @Override
    public boolean wasMember(UUID u) {
        if (u == null) {
            return false;
        }
        for (Player p : this.membersCache) {
            if (!p.getUniqueId().equals(u)) continue;
            return true;
        }
        return false;
    }

    @Override
    public boolean isBedDestroyed() {
        return this.bedDestroyed;
    }

    @Override
    public Location getSpawn() {
        return this.spawn;
    }

    @Override
    public Location getShop() {
        return this.shop;
    }

    @Override
    public Location getTeamUpgrades() {
        return this.teamUpgrades;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getDisplayName(Language language) {
        String m = language.m("team-name-{arena}-{team}".replace("{arena}", this.getArena().getArenaName()).replace("{team}", this.getName()));
        return m == null ? this.getName() : m;
    }

    @Override
    public TeamColor getColor() {
        return this.color;
    }

    @Override
    public List<Player> getMembers() {
        return this.members;
    }

    @Override
    public Location getBed() {
        return this.bed;
    }

    @Override
    public ConcurrentHashMap<String, Integer> getTeamUpgradeTiers() {
        return this.teamUpgradeList;
    }

    public BedHolo getBedHolo(@NotNull Player p) {
        return this.beds.get(p.getUniqueId());
    }

    @Override
    public void setBedDestroyed(boolean bedDestroyed) {
        this.bedDestroyed = bedDestroyed;
        if (!bedDestroyed) {
            if (!this.getBed().getBlock().getType().toString().contains("BED")) {
                BedWars.plugin.getLogger().severe("Bed not set for team: " + this.getName() + " in arena: " + this.getArena().getArenaName());
                return;
            }
            BedWars.nms.colorBed(this);
        } else {
            this.bed.getBlock().setType(Material.AIR);
        }
        for (BedHolo bh : this.beds.values()) {
            bh.hide();
            bh.show();
        }
    }

    @Override
    @Deprecated
    public IGenerator getIronGenerator() {
        IGenerator[] gens = (IGenerator[])this.generators.stream().filter(f -> f.getType() == GeneratorType.IRON).toArray();
        if (gens.length == 0) {
            return null;
        }
        return gens[0];
    }

    @Override
    @Deprecated
    public IGenerator getGoldGenerator() {
        IGenerator[] gens = (IGenerator[])this.generators.stream().filter(f -> f.getType() == GeneratorType.GOLD).toArray();
        if (gens.length == 0) {
            return null;
        }
        return gens[0];
    }

    @Override
    @Deprecated
    public IGenerator getEmeraldGenerator() {
        IGenerator[] gens = (IGenerator[])this.generators.stream().filter(f -> f.getType() == GeneratorType.EMERALD).toArray();
        if (gens.length == 0) {
            return null;
        }
        return gens[0];
    }

    @Override
    @Deprecated
    public void setEmeraldGenerator(IGenerator emeraldGenerator) {
        this.generators.add(emeraldGenerator);
    }

    @Override
    public List<IGenerator> getGenerators() {
        return this.generators;
    }

    @Override
    public List<PotionEffect> getBaseEffects() {
        return this.base;
    }

    public List<PotionEffect> getTeamEffects() {
        return this.teamEffects;
    }

    @Override
    public List<TeamEnchant> getBowsEnchantments() {
        return this.bowsEnchantments;
    }

    @Override
    public List<TeamEnchant> getSwordsEnchantments() {
        return this.swordsEnchantemnts;
    }

    @Override
    public List<TeamEnchant> getArmorsEnchantments() {
        return this.armorsEnchantemnts;
    }

    @Override
    public Arena getArena() {
        return this.arena;
    }

    @Override
    public int getDragons() {
        return this.dragons;
    }

    @Override
    public void setDragons(int amount) {
        this.dragons = amount;
    }

    @Override
    public List<Player> getMembersCache() {
        return this.membersCache;
    }

    public HashMap<UUID, BedHolo> getBeds() {
        return this.beds;
    }

    @Override
    public void destroyData() {
        this.members = null;
        this.spawn = null;
        this.bed = null;
        this.shop = null;
        this.teamUpgrades = null;
        for (IGenerator ig : new ArrayList<IGenerator>(this.generators)) {
            ig.destroyData();
        }
        this.arena = null;
        this.teamEffects = null;
        this.base = null;
        this.bowsEnchantments = null;
        this.swordsEnchantemnts = null;
        this.armorsEnchantemnts = null;
        this.enemyBaseEnterTraps.clear();
        this.membersCache = null;
    }

    @Override
    public void destroyBedHolo(@NotNull Player player) {
        if (this.getBeds().get(player.getUniqueId()) != null) {
            this.getBeds().get(player.getUniqueId()).destroy();
        }
    }

    @Override
    public LinkedList<EnemyBaseEnterTrap> getActiveTraps() {
        return this.enemyBaseEnterTraps;
    }

    @Override
    public Vector getKillDropsLocation() {
        if (this.killDropsLoc == null) {
            List gen = this.generators.stream().filter(p -> p.getType() == GeneratorType.IRON || p.getType() == GeneratorType.GOLD).collect(Collectors.toList());
            if (gen.isEmpty()) {
                return new Vector(this.getSpawn().getX(), this.getSpawn().getY(), this.getSpawn().getZ());
            }
            return new Vector(((IGenerator)gen.get(0)).getLocation().getX(), ((IGenerator)gen.get(0)).getLocation().getY(), ((IGenerator)gen.get(0)).getLocation().getZ());
        }
        return this.killDropsLoc;
    }

    @Override
    public void setKillDropsLocation(Vector loc) {
        if (loc == null) {
            this.killDropsLoc = null;
            return;
        }
        this.killDropsLoc = new Vector((double)loc.getBlockX() + 0.5, (double)loc.getBlockY(), (double)loc.getBlockZ() + 0.5);
    }

    @Override
    public boolean isBed(@NotNull Location location) {
        for (int x = location.getBlockX() - 1; x < location.getBlockX() + 1; ++x) {
            for (int z = location.getBlockZ() - 1; z < location.getBlockZ() + 1; ++z) {
                if (this.getBed().getBlockX() != x || this.getBed().getBlockY() != location.getBlockY() || this.getBed().getBlockZ() != z) continue;
                return true;
            }
        }
        return false;
    }

    public void setKillDropsLocation(Location loc) {
        if (loc == null) {
            this.killDropsLoc = null;
            return;
        }
        this.killDropsLoc = new Vector((double)loc.getBlockX() + 0.5, (double)loc.getBlockY(), (double)loc.getBlockZ() + 0.5);
    }

    public class BedHolo {
        private ArmorStand a;
        private UUID p;
        private Arena arena;
        private boolean hidden = false;
        private boolean bedDestroyed = false;

        public BedHolo(Player p, Arena arena) {
            this.p = p.getUniqueId();
            this.arena = arena;
            this.spawn();
            BedWarsTeam.this.beds.put(p.getUniqueId(), this);
        }

        public void spawn() {
            if (!this.arena.getConfig().getBoolean("use-bed-hologram")) {
                return;
            }
            this.a = (ArmorStand)BedWarsTeam.this.bed.getWorld().spawnEntity(BedWarsTeam.this.bed.getBlock().getLocation().add(0.5, 1.0, 0.5), EntityType.ARMOR_STAND);
            this.a.setGravity(false);
            if (BedWarsTeam.this.name != null) {
                if (BedWarsTeam.this.isBedDestroyed()) {
                    this.a.setCustomName(Language.getMsg(Bukkit.getPlayer((UUID)this.p), Messages.BED_HOLOGRAM_DESTROYED));
                    this.bedDestroyed = true;
                } else {
                    this.a.setCustomName(Language.getMsg(Bukkit.getPlayer((UUID)this.p), Messages.BED_HOLOGRAM_DEFEND));
                }
                this.a.setCustomNameVisible(true);
            }
            this.a.setRemoveWhenFarAway(false);
            this.a.setCanPickupItems(false);
            this.a.setArms(false);
            this.a.setBasePlate(false);
            this.a.setMarker(true);
            this.a.setVisible(false);
            for (Player p2 : this.arena.getWorld().getPlayers()) {
                if (this.p == p2.getUniqueId()) continue;
                BedWars.nms.hideEntity((Entity)this.a, p2);
            }
        }

        public void hide() {
            if (!this.arena.getConfig().getBoolean("use-bed-hologram")) {
                return;
            }
            if (this.bedDestroyed) {
                return;
            }
            this.hidden = true;
            this.a.remove();
        }

        public void destroy() {
            if (!this.arena.getConfig().getBoolean("use-bed-hologram")) {
                return;
            }
            this.a.remove();
            BedWarsTeam.this.beds.remove(this.p);
        }

        public void show() {
            if (!this.arena.getConfig().getBoolean("use-bed-hologram")) {
                return;
            }
            this.hidden = false;
            this.spawn();
        }

        public Arena getArena() {
            return this.arena;
        }

        public boolean isHidden() {
            return this.hidden;
        }
    }

    public static class Enchant
    implements TeamEnchant {
        Enchantment enchantment;
        int amplifier;

        public Enchant(Enchantment enchantment, int amplifier) {
            this.enchantment = enchantment;
            this.amplifier = amplifier;
        }

        @Override
        public Enchantment getEnchantment() {
            return this.enchantment;
        }

        @Override
        public int getAmplifier() {
            return this.amplifier;
        }
    }
}

