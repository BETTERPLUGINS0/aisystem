/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.configuration.serialization.ConfigurationSerializable
 *  org.bukkit.entity.Ageable
 *  org.bukkit.entity.ArmorStand
 *  org.bukkit.entity.Chicken
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.EntityType
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Mob
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.Inventory
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.util.Vector
 *  org.jetbrains.annotations.NotNull
 */
package me.zombie_striker.qav;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import me.zombie_striker.qav.BoundingBox;
import me.zombie_striker.qav.Main;
import me.zombie_striker.qav.MessagesConfig;
import me.zombie_striker.qav.ModelSize;
import me.zombie_striker.qav.api.QualityArmoryVehicles;
import me.zombie_striker.qav.hooks.model.Animation;
import me.zombie_striker.qav.hooks.model.ModelEngineHook;
import me.zombie_striker.qav.util.BlockCollisionUtil;
import me.zombie_striker.qav.util.ExposeDebug;
import me.zombie_striker.qav.util.xseries.XPotion;
import me.zombie_striker.qav.vehicles.AbstractVehicle;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public class VehicleEntity
implements ConfigurationSerializable {
    public AbstractVehicle vehicleType;
    @ExposeDebug
    public boolean allowsPassagers = false;
    @ExposeDebug
    private double rotation = 0.0;
    @ExposeDebug
    private Vector direction = new Vector(0, 0, 1);
    @ExposeDebug
    private Vector centerOffset = new Vector(0, 0, 0);
    private BoundingBox boundingBox;
    @ExposeDebug
    private double speed = 0.0;
    @ExposeDebug
    private UUID vehicleUUID = UUID.randomUUID();
    @ExposeDebug
    private UUID owner;
    private List<ArmorStand> modelParts = new ArrayList<ArmorStand>();
    private Entity driverseat;
    @ExposeDebug
    private HashMap<Integer, Entity> passagers = new HashMap();
    @ExposeDebug
    private HashMap<Integer, Entity> attachments = new HashMap();
    private Inventory inventory;
    private Inventory fuels;
    @ExposeDebug
    private int fuel = 0;
    @ExposeDebug
    private double yheight = 0.0;
    @ExposeDebug
    private List<UUID> whitelist = new ArrayList<UUID>();
    @ExposeDebug
    private double health;

    public VehicleEntity(AbstractVehicle abstractVehicle, Location location, UUID uUID) {
        this.vehicleType = abstractVehicle;
        this.boundingBox = new BoundingBox(location, abstractVehicle.getWidthRadius(), abstractVehicle.getHeight());
        this.owner = Main.onlyPublicVehicles ? null : uUID;
        this.whitelist.add(uUID);
        this.health = abstractVehicle.getMaxHealth();
        Main.vehicles.add(this);
    }

    public VehicleEntity(Map<String, Object> map) {
        List list;
        this.vehicleUUID = UUID.fromString((String)map.get("uuid"));
        this.vehicleType = QualityArmoryVehicles.getVehicle((String)map.get("type"));
        if (this.vehicleType == null) {
            return;
        }
        Location location = (Location)map.get("loc");
        if (location == null) {
            return;
        }
        try {
            if (!location.isWorldLoaded()) {
                location.getChunk().load();
            }
        } catch (Error | Exception throwable) {
            // empty catch block
        }
        if (map.containsKey("owner")) {
            this.owner = Main.onlyPublicVehicles ? null : UUID.fromString((String)map.get("owner"));
        }
        this.fuel = (Integer)map.get("fuel");
        if (map.containsKey("fuels")) {
            list = (ArrayList)map.get("fuels");
            this.getFuels().setContents(((ArrayList)list).toArray(new ItemStack[0]));
        }
        if (map.containsKey("inventory")) {
            list = (ArrayList)map.get("inventory");
            this.getTrunk().setContents(((ArrayList)list).toArray(new ItemStack[0]));
        }
        this.health = (Double)map.get("health");
        this.boundingBox = new BoundingBox(location, this.vehicleType.getWidthRadius(), this.vehicleType.getHeight());
        list = (List)map.get("whitelist");
        for (String string : list) {
            this.whitelist.add(UUID.fromString(string));
        }
        this.rotation = (Double)map.get("angle");
        this.spawnOrFind(location);
    }

    public Map<String, Object> serialize() {
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("uuid", this.vehicleUUID.toString());
        if (this.driverseat != null) {
            hashMap.put("loc", this.getDriverSeat().getLocation());
        }
        hashMap.put("fuel", this.fuel);
        if (this.fuels != null) {
            hashMap.put("fuels", this.fuels.getContents());
        }
        if (this.getTrunk() != null) {
            hashMap.put("inventory", this.getTrunk().getContents());
        }
        if (this.vehicleType != null) {
            hashMap.put("type", this.vehicleType.getName());
        }
        if (this.owner != null) {
            hashMap.put("owner", this.owner.toString());
        }
        hashMap.put("health", this.health);
        hashMap.put("angle", this.getAngleRotation());
        ArrayList<String> arrayList = new ArrayList<String>();
        for (UUID uUID : this.getWhiteList()) {
            arrayList.add(uUID.toString());
        }
        hashMap.put("whitelist", arrayList);
        return hashMap;
    }

    public void spawnOrFind(Location location) {
        if (this.driverseat != null && this.driverseat.isValid()) {
            return;
        }
        if (location != null) {
            for (Entity entity : location.getWorld().getNearbyEntities(location, 10.0, 10.0, 10.0)) {
                if (entity.getCustomName() != null && entity.getCustomName().startsWith(Main.VEHICLEPREFIX) && entity.getCustomName().trim().endsWith(this.vehicleUUID.toString().trim())) {
                    this.driverseat = entity;
                }
                if (!(entity instanceof ArmorStand) || entity.getCustomName() == null || !entity.getCustomName().startsWith(Main.MODEL_PREFIX) || !entity.getCustomName().trim().endsWith(this.vehicleUUID.toString().trim())) continue;
                this.modelParts.add((ArmorStand)entity);
            }
        }
        if (!Main.separateModelAndDriver && this.driverseat != null && this.modelParts.size() == 0) {
            this.modelParts.add((ArmorStand)this.driverseat);
        }
        if (this.driverseat == null) {
            this.spawn();
        }
    }

    public void spawn() {
        Location location = this.boundingBox.getLocation();
        ArmorStand armorStand = (ArmorStand)location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
        this.modelParts.add(armorStand);
        armorStand.setHelmet(this.getType().getModel());
        if (this.getType().getModelType() == ModelSize.BABY_ARMORSTAND_HEAD) {
            armorStand.setSmall(true);
        }
        armorStand.setInvulnerable(true);
        armorStand.setVisible(false);
        if (Main.separateModelAndDriver) {
            Entity entity;
            armorStand.setCustomName(Main.MODEL_PREFIX + this.vehicleUUID.toString());
            if (this.getType().getModelType().equals((Object)ModelSize.TURTLE)) {
                try {
                    entity = location.getWorld().spawnEntity(location.clone().add(this.vehicleType.getDriverSeat()), EntityType.TURTLE);
                    this.giveEffects((LivingEntity)entity);
                } catch (Exception exception) {
                    entity = location.getWorld().spawnEntity(location.clone().add(this.vehicleType.getDriverSeat()), EntityType.ARMOR_STAND);
                    ((ArmorStand)entity).setVisible(false);
                }
            } else {
                entity = location.getWorld().spawnEntity(location.clone().add(this.vehicleType.getDriverSeat()), EntityType.ARMOR_STAND);
                ((ArmorStand)entity).setVisible(false);
            }
            entity.setCustomName(Main.VEHICLEPREFIX + this.vehicleUUID.toString());
            entity.setInvulnerable(true);
            this.driverseat = entity;
        } else {
            armorStand.setCustomName(Main.VEHICLEPREFIX + this.vehicleUUID.toString());
            this.driverseat = armorStand;
        }
        ModelEngineHook.createModel(this);
        this.vehicleType.playAnimation(this, Animation.AnimationType.SPAWN, new String[0]);
    }

    public void tick() {
        this.vehicleType.tick(this);
    }

    public List<ArmorStand> getModelEntities() {
        return this.modelParts;
    }

    public ArmorStand getModelEntity() {
        return this.modelParts.get(0);
    }

    public Entity getDriverSeat() {
        return this.driverseat;
    }

    public double getAngleRotation() {
        return this.rotation;
    }

    public Vector getDirection() {
        return this.direction;
    }

    public BoundingBox getBoundingBox() {
        return this.boundingBox;
    }

    public void setBoundingBox(BoundingBox boundingBox) {
        this.boundingBox = boundingBox;
    }

    public AbstractVehicle getType() {
        return this.vehicleType;
    }

    public void setType(AbstractVehicle abstractVehicle) {
        this.vehicleType = abstractVehicle;
    }

    public Collection<Entity> getPassagerSeats() {
        return this.passagers.values();
    }

    public Map<Integer, Entity> getAttachments() {
        return this.attachments;
    }

    public void setAngle(double d) {
        Main.DEBUG("Setting angle to " + d);
        this.rotation = d;
        this.direction = new Vector(-Math.sin(this.rotation), this.direction.getY(), Math.cos(this.rotation));
    }

    public double getSpeed() {
        return this.speed;
    }

    public void setSpeed(double d) {
        this.speed = d;
    }

    public Inventory getTrunk() {
        if (this.inventory == null) {
            this.inventory = Bukkit.createInventory(null, (int)this.getType().getTrunkSize());
        }
        return this.inventory;
    }

    public boolean isInvalid() {
        return this.driverseat == null;
    }

    public Inventory getFuels() {
        if (this.fuels == null) {
            this.fuels = Bukkit.createInventory(null, (int)9, (String)ChatColor.translateAlternateColorCodes((char)'&', (String)MessagesConfig.MENU_FUELTANK_TITLE.replace("%cartype%", this.getType().getDisplayname())));
        }
        return this.fuels;
    }

    public int getFuel() {
        return this.fuel;
    }

    public void setFuel(int n) {
        this.fuel = n;
    }

    public Location getCenter() {
        return this.driverseat.getLocation().add(QualityArmoryVehicles.rotateRelToCar(this, (Entity)this.getModelEntity(), this.centerOffset, false));
    }

    public void setDirectionYHeight(double d) {
        this.yheight = d;
    }

    public double getDirectionYheight() {
        return this.yheight;
    }

    public UUID getVehicleUUID() {
        return this.vehicleUUID;
    }

    public UUID getOwner() {
        return this.owner;
    }

    public void setOwner(UUID uUID) {
        this.owner = Main.onlyPublicVehicles ? null : uUID;
    }

    public void deconstruct(Player player, String string) {
        this.deconstruct(player, string, false);
    }

    public void deconstruct(Player player, String string, boolean bl) {
        this.vehicleType.playAnimation(this, Animation.AnimationType.DESPAWN, new String[0]);
        this.driverseat.remove();
        this.driverseat = null;
        for (ArmorStand armorStand : this.getModelEntities()) {
            armorStand.remove();
        }
        for (Entity entity : this.getPassagerSeats()) {
            entity.remove();
        }
        for (Entity entity : this.getAttachments().values()) {
            entity.remove();
        }
        this.passagers.clear();
        this.modelParts.clear();
        if (!bl) {
            Main.vehicles.remove(this);
        }
        Main.DEBUG(this.getVehicleUUID() + " removed: " + string);
    }

    public List<UUID> getWhiteList() {
        return this.whitelist;
    }

    public boolean allowUserPassager(UUID uUID) {
        if (this.allowsPassagers || Main.onlyPublicVehicles) {
            return true;
        }
        return this.whitelist == null || this.whitelist.contains(uUID);
    }

    public boolean allowsPassagers() {
        return this.allowsPassagers;
    }

    public void setAllowsPassagers(boolean bl) {
        this.allowsPassagers = bl;
    }

    public double getHealth() {
        return this.health;
    }

    public void setHealth(double d) {
        this.health = d;
    }

    public void addToWhitelist(UUID uUID) {
        this.whitelist.add(uUID);
    }

    public void removeFromWhitelist(UUID uUID) {
        this.whitelist.remove(uUID);
    }

    public boolean allowUserDriver(UUID uUID) {
        return Main.onlyPublicVehicles || this.whitelist.contains(uUID);
    }

    public Entity getPassager(int n) {
        return this.passagers.get(n);
    }

    public HashMap<Integer, Entity> getPassagers() {
        return this.passagers;
    }

    public void updateSeats() {
        for (Map.Entry<Integer, Entity> entry : new ArrayList<Map.Entry<Integer, Entity>>(this.passagers.entrySet())) {
            if (entry.getValue().getPassenger() != null) continue;
            this.passagers.remove(entry.getKey());
            entry.getValue().remove();
        }
    }

    public int getFirstSeat() {
        for (Map.Entry<Integer, Entity> entry : this.passagers.entrySet()) {
            if (entry.getValue().getPassenger() != null) continue;
            return entry.getKey();
        }
        return -1;
    }

    public void addPassager(int n, Entity entity) {
        this.passagers.put(n, entity);
    }

    public Entity spawnSeat(Location location, int n) {
        Entity entity = null;
        double d = 2.0;
        if (this.getType().getModelType().equals((Object)ModelSize.TURTLE) || d < 1.0) {
            try {
                entity = location.getWorld().spawnEntity(location, EntityType.TURTLE);
                entity.setInvulnerable(true);
                ((Ageable)entity).setBaby();
            } catch (Error | Exception throwable) {
                try {
                    if (!Main.swapEndermiteWithChicken) {
                        entity = location.getWorld().spawnEntity(location, EntityType.ENDERMITE);
                    } else {
                        entity = location.getWorld().spawnEntity(location, EntityType.CHICKEN);
                        ((Chicken)entity).setBaby();
                    }
                } catch (Error | Exception throwable2) {
                    entity = location.getWorld().spawnEntity(location, EntityType.CHICKEN);
                    ((Chicken)entity).setBaby();
                }
            }
            try {
                ((LivingEntity)entity).setCollidable(false);
                ((LivingEntity)entity).setSilent(true);
            } catch (Error | Exception throwable) {
                // empty catch block
            }
            this.giveEffects((LivingEntity)entity);
        } else {
            entity = location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
            if (d < 2.0) {
                ((ArmorStand)entity).setSmall(true);
            }
            ((ArmorStand)entity).setVisible(false);
            ((ArmorStand)entity).setInvulnerable(false);
            ((ArmorStand)entity).setCollidable(false);
        }
        entity.setCustomName(Main.VEHICLEPREFIX + this.vehicleUUID.toString());
        return entity;
    }

    private void giveEffects(@NotNull LivingEntity livingEntity) {
        livingEntity.addPotionEffect(new PotionEffect(XPotion.JUMP_BOOST.getPotionEffectType(), Integer.MAX_VALUE, -10000, false, false), false);
        livingEntity.addPotionEffect(new PotionEffect(XPotion.INVISIBILITY.getPotionEffectType(), Integer.MAX_VALUE, 1, false, false), false);
        livingEntity.addPotionEffect(new PotionEffect(XPotion.SLOWNESS.getPotionEffectType(), Integer.MAX_VALUE, 16, false, false), false);
        ((Mob)livingEntity).setAware(false);
    }

    public boolean isOnGround() {
        Location location = this.getCenter().clone().subtract(0.0, 1.0, 0.0);
        return BlockCollisionUtil.isSolidAt(location);
    }

    public boolean isSubmerged() {
        if (this.driverseat == null) {
            return false;
        }
        return BlockCollisionUtil.getMaterial(this.driverseat.getLocation().add(0.0, 1.6, 0.0)) == Material.WATER;
    }

    public String toString() {
        return "{vehicleType=" + this.vehicleType + ", allowsPassagers=" + this.allowsPassagers + ", rotation=" + this.rotation + ", direction=" + this.direction + ", centerOffset=" + this.centerOffset + ", boundingBox=" + this.boundingBox + ", speed=" + this.speed + ", vehicleUUID=" + this.vehicleUUID + ", owner=" + this.owner + ", modelParts=" + this.modelParts + ", driverseat=" + this.driverseat + ", passagers=" + this.passagers + ", inventory=" + this.inventory + ", fuels=" + this.fuels + ", fuel=" + this.fuel + ", yheight=" + this.yheight + ", whitelist=" + this.whitelist + ", health=" + this.health + '}';
    }
}

