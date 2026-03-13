/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.Sound
 *  org.bukkit.entity.ArmorStand
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.EntityType
 *  org.bukkit.entity.Item
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.util.EulerAngle
 *  org.bukkit.util.Vector
 */
package com.andrei1058.bedwars.arena;

import com.andrei1058.bedwars.BedWars;
import com.andrei1058.bedwars.api.arena.GameState;
import com.andrei1058.bedwars.api.arena.IArena;
import com.andrei1058.bedwars.api.arena.generator.GeneratorType;
import com.andrei1058.bedwars.api.arena.generator.IGenHolo;
import com.andrei1058.bedwars.api.arena.generator.IGenerator;
import com.andrei1058.bedwars.api.arena.team.ITeam;
import com.andrei1058.bedwars.api.events.gameplay.GeneratorUpgradeEvent;
import com.andrei1058.bedwars.api.language.Language;
import com.andrei1058.bedwars.api.language.Messages;
import com.andrei1058.bedwars.api.region.Cuboid;
import java.util.Collection;
import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

public class OreGenerator
implements IGenerator {
    private Location location;
    private int delay = 1;
    private int upgradeStage = 1;
    private int lastSpawn;
    private int spawnLimit = 0;
    private int amount = 1;
    private IArena arena;
    private ItemStack ore;
    private GeneratorType type;
    private int rotate = 0;
    private int dropID = 0;
    private ITeam bwt;
    boolean up = true;
    private HashMap<String, IGenHolo> armorStands = new HashMap();
    private ArmorStand item;
    public boolean stack = BedWars.getGeneratorsCfg().getBoolean("stack-items");
    private static final ConcurrentLinkedDeque<OreGenerator> rotation = new ConcurrentLinkedDeque();

    public OreGenerator(Location location, IArena arena, GeneratorType type, ITeam bwt) {
        this.location = type == GeneratorType.EMERALD || type == GeneratorType.DIAMOND ? new Location(location.getWorld(), (double)location.getBlockX() + 0.5, (double)location.getBlockY() + 1.3, (double)location.getBlockZ() + 0.5) : location.add(0.0, 1.3, 0.0);
        this.arena = arena;
        this.bwt = bwt;
        this.type = type;
        this.loadDefaults();
        BedWars.debug("Initializing new generator at: " + String.valueOf(location) + " - " + String.valueOf((Object)type) + " - " + (bwt == null ? "NOTEAM" : bwt.getName()));
        Cuboid c = new Cuboid(location, this.getArena().getConfig().getInt("generator-protection"), true);
        c.setMaxY(c.getMaxY() + 5);
        c.setMinY(c.getMinY() - 2);
        arena.getRegionsList().add(c);
    }

    @Override
    public void upgrade() {
        switch (this.type) {
            case DIAMOND: {
                ++this.upgradeStage;
                if (this.upgradeStage == 2) {
                    this.delay = BedWars.getGeneratorsCfg().getInt((String)(BedWars.getGeneratorsCfg().getYml().get(this.arena.getGroup() + ".diamond.tierII.delay") == null ? "Default.diamond.tierII.delay" : this.arena.getGroup() + ".diamond.tierII.delay"));
                    this.amount = BedWars.getGeneratorsCfg().getInt((String)(BedWars.getGeneratorsCfg().getYml().get(this.arena.getGroup() + ".diamond.tierII.amount") == null ? "Default.diamond.tierII.amount" : this.arena.getGroup() + ".diamond.tierII.amount"));
                    this.spawnLimit = BedWars.getGeneratorsCfg().getInt((String)(BedWars.getGeneratorsCfg().getYml().get(this.arena.getGroup() + ".diamond.tierII.spawn-limit") == null ? "Default.diamond.tierII.spawn-limit" : this.arena.getGroup() + ".diamond.tierII.spawn-limit"));
                } else if (this.upgradeStage == 3) {
                    this.delay = BedWars.getGeneratorsCfg().getInt((String)(BedWars.getGeneratorsCfg().getYml().get(this.arena.getGroup() + ".diamond.tierIII.delay") == null ? "Default.diamond.tierIII.delay" : this.arena.getGroup() + ".diamond.tierIII.delay"));
                    this.amount = BedWars.getGeneratorsCfg().getInt((String)(BedWars.getGeneratorsCfg().getYml().get(this.arena.getGroup() + ".diamond.tierIII.amount") == null ? "Default.diamond.tierIII.amount" : this.arena.getGroup() + ".diamond.tierIII.amount"));
                    this.spawnLimit = BedWars.getGeneratorsCfg().getInt((String)(BedWars.getGeneratorsCfg().getYml().get(this.arena.getGroup() + ".diamond.tierII.spawn-limit") == null ? "Default.diamond.tierIII.spawn-limit" : this.arena.getGroup() + ".diamond.tierIII.spawn-limit"));
                }
                this.ore = new ItemStack(Material.DIAMOND);
                for (IGenHolo e : this.armorStands.values()) {
                    e.setTierName(Language.getLang(e.getIso()).m(Messages.GENERATOR_HOLOGRAM_TIER).replace("{tier}", Language.getLang(e.getIso()).m(this.upgradeStage == 2 ? Messages.FORMATTING_GENERATOR_TIER2 : Messages.FORMATTING_GENERATOR_TIER3)));
                }
                break;
            }
            case EMERALD: {
                ++this.upgradeStage;
                if (this.upgradeStage == 2) {
                    this.delay = BedWars.getGeneratorsCfg().getInt((String)(BedWars.getGeneratorsCfg().getYml().get(this.arena.getGroup() + ".emerald.tierII.delay") == null ? "Default.emerald.tierII.delay" : this.arena.getGroup() + ".emerald.tierII.delay"));
                    this.amount = BedWars.getGeneratorsCfg().getInt((String)(BedWars.getGeneratorsCfg().getYml().get(this.arena.getGroup() + ".emerald.tierII.amount") == null ? "Default.emerald.tierII.amount" : this.arena.getGroup() + ".emerald.tierII.amount"));
                    this.spawnLimit = BedWars.getGeneratorsCfg().getInt((String)(BedWars.getGeneratorsCfg().getYml().get(this.arena.getGroup() + ".emerald.tierII.spawn-limit") == null ? "Default.emerald.tierII.spawn-limit" : this.arena.getGroup() + ".emerald.tierII.spawn-limit"));
                } else if (this.upgradeStage == 3) {
                    this.delay = BedWars.getGeneratorsCfg().getInt((String)(BedWars.getGeneratorsCfg().getYml().get(this.arena.getGroup() + ".emerald.tierIII.delay") == null ? "Default.emerald.tierIII.delay" : this.arena.getGroup() + ".emerald.tierIII.delay"));
                    this.amount = BedWars.getGeneratorsCfg().getInt((String)(BedWars.getGeneratorsCfg().getYml().get(this.arena.getGroup() + ".emerald.tierIII.amount") == null ? "Default.emerald.tierIII.amount" : this.arena.getGroup() + ".emerald.tierIII.amount"));
                    this.spawnLimit = BedWars.getGeneratorsCfg().getInt((String)(BedWars.getGeneratorsCfg().getYml().get(this.arena.getGroup() + ".emerald.tierII.spawn-limit") == null ? "Default.emerald.tierIII.spawn-limit" : this.arena.getGroup() + ".emerald.tierIII.spawn-limit"));
                }
                this.ore = new ItemStack(Material.EMERALD);
                for (IGenHolo e : this.armorStands.values()) {
                    e.setTierName(Language.getLang(e.getIso()).m(Messages.GENERATOR_HOLOGRAM_TIER).replace("{tier}", Language.getLang(e.getIso()).m(this.upgradeStage == 2 ? Messages.FORMATTING_GENERATOR_TIER2 : Messages.FORMATTING_GENERATOR_TIER3)));
                }
                break;
            }
        }
        Bukkit.getPluginManager().callEvent((Event)new GeneratorUpgradeEvent(this));
    }

    @Override
    public void spawn() {
        if (this.arena.getStatus() != GameState.playing) {
            return;
        }
        if (this.lastSpawn == 0) {
            this.lastSpawn = this.delay;
            if (this.spawnLimit != 0) {
                int oreCount = 0;
                for (Entity e : this.location.getWorld().getNearbyEntities(this.location, 3.0, 3.0, 3.0)) {
                    if (e.getType() != EntityType.DROPPED_ITEM) continue;
                    Item i = (Item)e;
                    if (i.getItemStack().getType() == this.ore.getType()) {
                        ++oreCount;
                    }
                    if (oreCount < this.spawnLimit) continue;
                    return;
                }
                this.lastSpawn = this.delay;
            }
            if (this.bwt == null) {
                this.dropItem(this.location);
                return;
            }
            if (this.bwt.getMembers().size() == 1) {
                this.dropItem(this.location);
                return;
            }
            if (BedWars.plugin.getConfig().getBoolean("enable-gen-split")) {
                Object[] players = this.location.getWorld().getNearbyEntities(this.location, 1.0, 1.0, 1.0).stream().filter(entity -> entity.getType() == EntityType.PLAYER).filter(entity -> this.arena.isPlayer((Player)entity)).toArray();
                if (players.length <= 1) {
                    this.dropItem(this.location);
                    return;
                }
                for (Object o : players) {
                    Player player = (Player)o;
                    ItemStack item = this.ore.clone();
                    item.setAmount(this.amount);
                    player.playSound(player.getLocation(), Sound.valueOf((String)BedWars.getForCurrentVersion("ITEM_PICKUP", "ENTITY_ITEM_PICKUP", "ENTITY_ITEM_PICKUP")), 0.6f, 1.3f);
                    Collection excess = player.getInventory().addItem(new ItemStack[]{item}).values();
                    for (ItemStack value : excess) {
                        this.dropItem(player.getLocation(), value.getAmount());
                    }
                }
                return;
            }
            this.dropItem(this.location);
            return;
        }
        --this.lastSpawn;
        for (IGenHolo e : this.armorStands.values()) {
            e.setTimerName(Language.getLang(e.getIso()).m(Messages.GENERATOR_HOLOGRAM_TIMER).replace("{seconds}", String.valueOf(this.lastSpawn)));
        }
    }

    private void dropItem(Location location, int amount) {
        for (int temp = amount; temp > 0; --temp) {
            ItemStack itemStack = new ItemStack(this.ore);
            if (!this.stack) {
                ItemMeta itemMeta = itemStack.getItemMeta();
                itemMeta.setDisplayName("custom" + this.dropID++);
                itemStack.setItemMeta(itemMeta);
            }
            Item item = location.getWorld().dropItem(location, itemStack);
            item.setVelocity(new Vector(0, 0, 0));
        }
    }

    @Override
    public void dropItem(Location location) {
        this.dropItem(location, this.amount);
    }

    @Override
    public void setOre(ItemStack ore) {
        BedWars.debug("Changing ore for generator at " + this.location.toString() + " from " + String.valueOf(this.ore) + " to " + String.valueOf(ore));
        this.ore = ore;
    }

    @Override
    public IArena getArena() {
        return this.arena;
    }

    public static ConcurrentLinkedDeque<OreGenerator> getRotation() {
        return rotation;
    }

    @Override
    public HashMap<String, IGenHolo> getLanguageHolograms() {
        return this.armorStands;
    }

    private static ArmorStand createArmorStand(String name, Location l) {
        ArmorStand a = (ArmorStand)l.getWorld().spawnEntity(l, EntityType.ARMOR_STAND);
        a.setGravity(false);
        if (name != null) {
            a.setCustomName(name);
            a.setCustomNameVisible(true);
        }
        a.setRemoveWhenFarAway(false);
        a.setVisible(false);
        a.setCanPickupItems(false);
        a.setArms(false);
        a.setBasePlate(false);
        a.setMarker(true);
        return a;
    }

    @Override
    public void rotate() {
        if (this.up) {
            if (this.rotate >= 540) {
                this.up = false;
            }
            if (this.rotate > 500) {
                this.item.setHeadPose(new EulerAngle(0.0, Math.toRadians(++this.rotate), 0.0));
            } else if (this.rotate > 470) {
                this.item.setHeadPose(new EulerAngle(0.0, Math.toRadians(this.rotate += 2), 0.0));
            } else if (this.rotate > 450) {
                this.item.setHeadPose(new EulerAngle(0.0, Math.toRadians(this.rotate += 3), 0.0));
            } else {
                this.item.setHeadPose(new EulerAngle(0.0, Math.toRadians(this.rotate += 4), 0.0));
            }
        } else {
            if (this.rotate <= 0) {
                this.up = true;
            }
            if (this.rotate > 120) {
                this.item.setHeadPose(new EulerAngle(0.0, Math.toRadians(this.rotate -= 4), 0.0));
            } else if (this.rotate > 90) {
                this.item.setHeadPose(new EulerAngle(0.0, Math.toRadians(this.rotate -= 3), 0.0));
            } else if (this.rotate > 70) {
                this.item.setHeadPose(new EulerAngle(0.0, Math.toRadians(this.rotate -= 2), 0.0));
            } else {
                this.item.setHeadPose(new EulerAngle(0.0, Math.toRadians(--this.rotate), 0.0));
            }
        }
    }

    @Override
    public void setDelay(int delay) {
        this.delay = delay;
    }

    @Override
    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public Location getLocation() {
        return this.location;
    }

    @Override
    public ItemStack getOre() {
        return this.ore;
    }

    @Override
    public void disable() {
        if (this.getOre().getType() == Material.EMERALD || this.getOre().getType() == Material.DIAMOND) {
            rotation.remove(this);
            for (IGenHolo a : this.armorStands.values()) {
                a.destroy();
            }
        }
        this.armorStands.clear();
    }

    @Override
    public void updateHolograms(Player p, String iso) {
        for (IGenHolo h : this.armorStands.values()) {
            h.updateForPlayer(p, iso);
        }
    }

    @Override
    public void enableRotation() {
        rotation.add(this);
        for (Language lan : Language.getLanguages()) {
            IGenHolo h = this.armorStands.get(lan.getIso());
            if (h != null) continue;
            this.armorStands.put(lan.getIso(), new HoloGram(lan.getIso()));
        }
        for (IGenHolo hg : this.armorStands.values()) {
            hg.updateForAll();
        }
        this.item = OreGenerator.createArmorStand(null, this.location.clone().add(0.0, 0.5, 0.0));
        this.item.setHelmet(new ItemStack(this.type == GeneratorType.DIAMOND ? Material.DIAMOND_BLOCK : Material.EMERALD_BLOCK));
    }

    @Override
    public void setSpawnLimit(int value) {
        this.spawnLimit = value;
    }

    private void loadDefaults() {
        switch (this.type) {
            case GOLD: {
                this.delay = BedWars.getGeneratorsCfg().getInt((String)(BedWars.getGeneratorsCfg().getYml().get(this.arena.getGroup() + ".gold.delay") == null ? "Default.gold.delay" : this.arena.getGroup() + ".gold.delay"));
                this.ore = new ItemStack(Material.GOLD_INGOT);
                this.amount = BedWars.getGeneratorsCfg().getInt((String)(BedWars.getGeneratorsCfg().getYml().get(this.arena.getGroup() + ".gold.amount") == null ? "Default.gold.amount" : this.arena.getGroup() + ".gold.amount"));
                this.spawnLimit = BedWars.getGeneratorsCfg().getInt((String)(BedWars.getGeneratorsCfg().getYml().get(this.arena.getGroup() + ".gold.spawn-limit") == null ? "Default.gold.spawn-limit" : this.arena.getGroup() + ".gold.spawn-limit"));
                break;
            }
            case IRON: {
                this.delay = BedWars.getGeneratorsCfg().getInt((String)(BedWars.getGeneratorsCfg().getYml().get(this.arena.getGroup() + ".iron.delay") == null ? "Default.iron.delay" : this.arena.getGroup() + ".iron.delay"));
                this.amount = BedWars.getGeneratorsCfg().getInt((String)(BedWars.getGeneratorsCfg().getYml().get(this.arena.getGroup() + ".iron.amount") == null ? "Default.iron.amount" : this.arena.getGroup() + ".iron.amount"));
                this.ore = new ItemStack(Material.IRON_INGOT);
                this.spawnLimit = BedWars.getGeneratorsCfg().getInt((String)(BedWars.getGeneratorsCfg().getYml().get(this.arena.getGroup() + ".iron.spawn-limit") == null ? "Default.iron.spawn-limit" : this.arena.getGroup() + ".iron.spawn-limit"));
                break;
            }
            case DIAMOND: {
                this.delay = BedWars.getGeneratorsCfg().getInt((String)(BedWars.getGeneratorsCfg().getYml().get(this.arena.getGroup() + ".diamond.tierI.delay") == null ? "Default.diamond.tierI.delay" : this.arena.getGroup() + ".diamond.tierI.delay"));
                this.amount = BedWars.getGeneratorsCfg().getInt((String)(BedWars.getGeneratorsCfg().getYml().get(this.arena.getGroup() + ".diamond.tierI.amount") == null ? "Default.diamond.tierI.amount" : this.arena.getGroup() + ".diamond.tierI.amount"));
                this.spawnLimit = BedWars.getGeneratorsCfg().getInt((String)(BedWars.getGeneratorsCfg().getYml().get(this.arena.getGroup() + ".diamond.tierI.spawn-limit") == null ? "Default.diamond.tierI.spawn-limit" : this.arena.getGroup() + ".diamond.tierI.spawn-limit"));
                this.ore = new ItemStack(Material.DIAMOND);
                break;
            }
            case EMERALD: {
                this.delay = BedWars.getGeneratorsCfg().getInt((String)(BedWars.getGeneratorsCfg().getYml().get(this.arena.getGroup() + ".emerald.tierI.delay") == null ? "Default.emerald.tierI.delay" : this.arena.getGroup() + ".emerald.tierI.delay"));
                this.amount = BedWars.getGeneratorsCfg().getInt((String)(BedWars.getGeneratorsCfg().getYml().get(this.arena.getGroup() + ".emerald.tierI.amount") == null ? "Default.emerald.tierI.amount" : this.arena.getGroup() + ".emerald.tierI.amount"));
                this.spawnLimit = BedWars.getGeneratorsCfg().getInt((String)(BedWars.getGeneratorsCfg().getYml().get(this.arena.getGroup() + ".emerald.tierI.spawn-limit") == null ? "Default.emerald.tierI.spawn-limit" : this.arena.getGroup() + ".emerald.tierI.spawn-limit"));
                this.ore = new ItemStack(Material.EMERALD);
            }
        }
        this.lastSpawn = this.delay;
    }

    @Override
    public ITeam getBwt() {
        return this.bwt;
    }

    @Override
    public ArmorStand getHologramHolder() {
        return this.item;
    }

    @Override
    public GeneratorType getType() {
        return this.type;
    }

    @Override
    public int getAmount() {
        return this.amount;
    }

    @Override
    public int getDelay() {
        return this.delay;
    }

    @Override
    public int getNextSpawn() {
        return this.lastSpawn;
    }

    @Override
    public int getSpawnLimit() {
        return this.spawnLimit;
    }

    @Override
    public void setNextSpawn(int nextSpawn) {
        this.lastSpawn = nextSpawn;
    }

    @Override
    public void setStack(boolean stack) {
        this.stack = stack;
    }

    @Override
    public boolean isStack() {
        return this.stack;
    }

    @Override
    public void setType(GeneratorType type) {
        this.type = type;
    }

    @Override
    public void destroyData() {
        rotation.remove(this);
        this.location = null;
        this.arena = null;
        this.ore = null;
        this.bwt = null;
        this.armorStands = null;
        this.item = null;
    }

    public class HoloGram
    implements IGenHolo {
        String iso;
        ArmorStand tier;
        ArmorStand timer;
        ArmorStand name;

        public HoloGram(String iso) {
            this.iso = iso;
            this.tier = OreGenerator.createArmorStand(Language.getLang(iso).m(Messages.GENERATOR_HOLOGRAM_TIER).replace("{tier}", Language.getLang(iso).m(Messages.FORMATTING_GENERATOR_TIER1)), OreGenerator.this.location.clone().add(0.0, 3.0, 0.0));
            this.timer = OreGenerator.createArmorStand(Language.getLang(iso).m(Messages.GENERATOR_HOLOGRAM_TIMER).replace("{seconds}", String.valueOf(OreGenerator.this.lastSpawn)), OreGenerator.this.location.clone().add(0.0, 2.4, 0.0));
            this.name = OreGenerator.createArmorStand(Language.getLang(iso).m(OreGenerator.this.getOre().getType() == Material.DIAMOND ? Messages.GENERATOR_HOLOGRAM_TYPE_DIAMOND : Messages.GENERATOR_HOLOGRAM_TYPE_EMERALD), OreGenerator.this.location.clone().add(0.0, 2.7, 0.0));
        }

        @Override
        public void updateForAll() {
            for (Player p2 : this.timer.getWorld().getPlayers()) {
                this.updateForPlayer(p2, Language.getPlayerLanguage(p2).getIso());
            }
        }

        @Override
        public void updateForPlayer(Player p, String lang) {
            if (lang.equalsIgnoreCase(this.iso)) {
                return;
            }
            BedWars.nms.hideEntity((Entity)this.tier, p);
            BedWars.nms.hideEntity((Entity)this.timer, p);
            BedWars.nms.hideEntity((Entity)this.name, p);
        }

        @Override
        public void setTierName(String name) {
            if (this.tier.isDead()) {
                return;
            }
            this.tier.setCustomName(name);
        }

        @Override
        public String getIso() {
            return this.iso;
        }

        @Override
        public void setTimerName(String name) {
            if (this.timer.isDead()) {
                return;
            }
            this.timer.setCustomName(name);
        }

        @Override
        public void destroy() {
            this.tier.remove();
            this.timer.remove();
            this.name.remove();
        }
    }
}

