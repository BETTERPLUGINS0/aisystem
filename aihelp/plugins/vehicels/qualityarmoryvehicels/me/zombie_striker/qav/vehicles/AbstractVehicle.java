/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.GameMode
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.block.BlockFace
 *  org.bukkit.entity.ArmorStand
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.util.EulerAngle
 *  org.bukkit.util.Vector
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package me.zombie_striker.qav.vehicles;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import me.zombie_striker.qav.ItemFact;
import me.zombie_striker.qav.Main;
import me.zombie_striker.qav.MessagesConfig;
import me.zombie_striker.qav.ModelSize;
import me.zombie_striker.qav.VehicleEntity;
import me.zombie_striker.qav.api.QualityArmoryVehicles;
import me.zombie_striker.qav.attachments.Attachment;
import me.zombie_striker.qav.finput.FInput;
import me.zombie_striker.qav.fuel.FuelItemStack;
import me.zombie_striker.qav.hooks.ProtectionHandler;
import me.zombie_striker.qav.hooks.model.Animation;
import me.zombie_striker.qav.hooks.model.ModelEngineHook;
import me.zombie_striker.qav.nms.NMSUtil;
import me.zombie_striker.qav.util.BlockCollisionUtil;
import me.zombie_striker.qav.util.HeadPoseUtil;
import me.zombie_striker.qav.util.HotbarMessager;
import me.zombie_striker.qav.util.VehicleUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractVehicle {
    protected static final double pitchIncrement = 0.05235987755982988;
    protected static final double maxAngle = 0.7853981633974483;
    private double rotationMultiplier = 1.0;
    private Map<String, FInput> inputs = new HashMap<String, FInput>();
    private double widthRadius = 5.0;
    private double height = 4.0;
    private String internalName;
    private boolean bodyFix = false;
    private double rotationDelta = 0.1;
    private double maxSpeed = 0.9;
    private double maxBackupSpeed = 0.8;
    private boolean destructable = true;
    private boolean disableMeleeDamage = true;
    private boolean disableProjectileDamage = true;
    private double jumphiehgt = 0.2;
    private Vector driverSeat;
    private HashMap<Vector, Integer> passagerOffset = new HashMap();
    private List<Attachment> attachments = new ArrayList<Attachment>();
    private List<Animation> animations = new ArrayList<Animation>();
    private int id;
    private Material material;
    private ItemStack vehicleModel = null;
    private Vector center;
    private String displayname;
    private List<String> lore;
    private double acceleration = 0.1;
    private ModelSize size = ModelSize.BABY_ARMORSTAND_HEAD;
    private boolean canJump = true;
    private double maxhealth;
    private int price;
    private boolean inShop;
    private String sound;
    private boolean playSoundsDriving;
    private float soundVolume = 1.0f;
    private int trunksize;
    private boolean enableFuels = false;

    public AbstractVehicle(String string, int n) {
        this.internalName = string;
        this.id = n;
    }

    public boolean handleFuel(VehicleEntity vehicleEntity, Player player) {
        boolean bl;
        block9: {
            Entity entity;
            bl = true;
            if (Main.bypassCoalInCreative && (entity = vehicleEntity.getDriverSeat().getPassenger()) instanceof Player && ((Player)entity).getGameMode().equals((Object)GameMode.CREATIVE)) {
                return true;
            }
            if (this.enableFuel()) {
                FuelItemStack.updateFuel(vehicleEntity);
                if (vehicleEntity.getFuel() <= 0) {
                    bl = false;
                    try {
                        if (player == null) break block9;
                        if (!Main.useChatForMessage) {
                            try {
                                HotbarMessager.sendHotBarMessage(player, MessagesConfig.MESSAGE_HOTBAR_OUTOFFUEL);
                            } catch (Error | Exception throwable) {}
                            break block9;
                        }
                        player.sendMessage(Main.prefix + MessagesConfig.MESSAGE_HOTBAR_OUTOFFUEL);
                    } catch (Error | Exception throwable) {}
                } else {
                    vehicleEntity.setFuel(vehicleEntity.getFuel() - 1);
                }
            }
        }
        return bl;
    }

    public boolean hasFuel(VehicleEntity vehicleEntity) {
        Entity entity;
        if (!this.enableFuel()) {
            return true;
        }
        if (Main.bypassCoalInCreative && (entity = vehicleEntity.getDriverSeat().getPassenger()) instanceof Player && ((Player)entity).getGameMode().equals((Object)GameMode.CREATIVE)) {
            return true;
        }
        return vehicleEntity.getFuel() > 0;
    }

    public abstract void handleTurnLeft(VehicleEntity var1, Player var2);

    public abstract void handleTurnRight(VehicleEntity var1, Player var2);

    public abstract void handleSpeedIncrease(VehicleEntity var1, Player var2);

    public abstract void handleSpeedDecrease(VehicleEntity var1, Player var2);

    public abstract void handleSpace(VehicleEntity var1, Player var2);

    public abstract void tick(VehicleEntity var1);

    public ItemStack getModel() {
        if (this.vehicleModel == null) {
            this.vehicleModel = ItemFact.getItem(this);
        }
        return this.vehicleModel;
    }

    public void setModelItemStack(ItemStack itemStack) {
        this.vehicleModel = itemStack;
    }

    public double getHeight() {
        return this.height;
    }

    public void setHeight(double d) {
        this.height = d;
    }

    public double getWidthRadius() {
        return this.widthRadius;
    }

    public void setWidthRadius(double d) {
        this.widthRadius = d;
    }

    public boolean enableBodyFix() {
        return this.bodyFix;
    }

    public double getRotationDelta() {
        return this.rotationDelta;
    }

    public double getMaxSpeed() {
        return this.maxSpeed;
    }

    public void setMaxSpeed(double d) {
        this.maxSpeed = d;
    }

    public void setDeconstructable(boolean bl) {
        this.destructable = bl;
    }

    public void setStopsProjectileDamage(boolean bl) {
        this.disableProjectileDamage = bl;
    }

    public void setStopsMeleeDamage(boolean bl) {
        this.disableMeleeDamage = bl;
    }

    public Material getMaterial() {
        if (this.material != null) {
            return this.material;
        }
        this.material = Material.RABBIT_HIDE;
        if (this.vehicleModel == null) {
            this.vehicleModel = QualityArmoryVehicles.getVehicleItemStack(this);
        }
        return this.vehicleModel.getType();
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public int getItemData() {
        return this.id;
    }

    public void setJumpHeight(double d) {
        this.jumphiehgt = d;
    }

    public void setCenter(Vector vector) {
        this.center = vector;
    }

    public String getDisplayname() {
        return this.displayname;
    }

    public void setDisplayname(String string) {
        this.displayname = string;
    }

    public List<String> getLore() {
        return this.lore;
    }

    public void setLore(List<String> list) {
        this.lore = list;
    }

    public boolean hasLore() {
        return this.lore != null;
    }

    public void setMaxBackupSpeed(double d) {
        this.maxBackupSpeed = d;
    }

    public double getMaxBackupSpeed() {
        return this.maxBackupSpeed;
    }

    public void setAccerlationSpeed(double d) {
        this.acceleration = d;
    }

    public void setModelSize(ModelSize modelSize) {
        this.size = modelSize;
    }

    boolean canJump() {
        return this.canJump;
    }

    public void setTurnRate(double d) {
        this.rotationDelta = d;
    }

    public void setEnableFuel(boolean bl) {
        this.enableFuels = bl;
    }

    public void setCanJump(boolean bl) {
        this.canJump = bl;
    }

    public void setBodyFix(boolean bl) {
        this.bodyFix = bl;
    }

    public void setPrice(int n) {
        this.price = n;
    }

    public void setAllowInShop(boolean bl) {
        this.inShop = bl;
    }

    public void setPlayCustomSounds(boolean bl) {
        this.playSoundsDriving = bl;
    }

    public int getTrunkSize() {
        return this.trunksize;
    }

    public void setTrunkSize(int n) {
        this.trunksize = n;
    }

    public Vector getCenterFromControlSeat() {
        return new Vector(0, 0, 0);
    }

    public Vector getDriverSeat() {
        return this.driverSeat;
    }

    public void setDriverSeat(Vector vector) {
        this.driverSeat = vector;
    }

    public String getName() {
        return this.internalName;
    }

    public String getSound() {
        return this.sound;
    }

    public void setSound(String string) {
        this.sound = string;
    }

    public double getSoundVolume() {
        return this.soundVolume;
    }

    public void setSoundVolume(float f) {
        this.soundVolume = f;
    }

    public boolean canPlaySkidSounds() {
        return this.playSoundsDriving;
    }

    public ModelSize getModelType() {
        return this.size;
    }

    public void basicDirections(VehicleEntity vehicleEntity, boolean bl, boolean bl2) {
        this.basicDirections(vehicleEntity, bl, bl2, true, false);
    }

    public void basicDirections(VehicleEntity vehicleEntity, boolean bl, boolean bl2, boolean bl3, boolean bl4) {
        if (vehicleEntity.getHealth() <= 0.0 && Main.freezeOnDestroy) {
            return;
        }
        Location location = Main.separateModelAndDriver ? vehicleEntity.getModelEntity().getLocation().subtract(0.0, 0.4, 0.0) : vehicleEntity.getDriverSeat().getLocation().subtract(0.0, 0.4, 0.0);
        Material material = BlockCollisionUtil.getMaterial(location);
        if (Main.customSpeedModifier.containsKey(material)) {
            vehicleEntity.setSpeed(vehicleEntity.getSpeed() * Main.customSpeedModifier.getOrDefault(material, 1.0));
        }
        if (vehicleEntity.getSpeed() > 0.0) {
            if (bl4) {
                if (vehicleEntity.getDirectionYheight() < -0.15) {
                    double d = vehicleEntity.getSpeed() + 0.01;
                    vehicleEntity.setSpeed(Math.min(d, vehicleEntity.getType().getMaxSpeed() * 2.0));
                } else if (vehicleEntity.getDirectionYheight() > 0.15) {
                    vehicleEntity.setSpeed(vehicleEntity.getSpeed() - 0.02);
                } else {
                    vehicleEntity.setSpeed(vehicleEntity.getSpeed() - 0.005);
                }
                if (Main.modernPlaneMovements && vehicleEntity.getDriverSeat().getPassenger() instanceof Player) {
                    Player player = (Player)vehicleEntity.getDriverSeat().getPassenger();
                    Vector vector = player.getEyeLocation().getDirection();
                    vehicleEntity.setDirectionYHeight(vector.getY());
                    HeadPoseUtil.setHeadPoseUsingReflection(vehicleEntity);
                    vehicleEntity.getModelEntity().setHeadPose(new EulerAngle(vector.getY() * -1.0, vehicleEntity.getModelEntity().getHeadPose().getY(), 0.0));
                }
            } else {
                vehicleEntity.setSpeed(vehicleEntity.getSpeed() - 0.01);
            }
        }
        if (vehicleEntity.getSpeed() < 0.0) {
            vehicleEntity.setSpeed(vehicleEntity.getSpeed() + 0.01);
        }
        if (vehicleEntity.getSpeed() > 0.0 && vehicleEntity.getSpeed() < 0.09) {
            vehicleEntity.setSpeed(0.0);
        }
        Vector vector = vehicleEntity.getDirection().clone();
        vector.normalize().multiply(vehicleEntity.getSpeed());
        if (bl4) {
            double d = vector.length();
            double d2 = 0.05;
            if (vehicleEntity.getSpeed() < 0.3) {
                d2 = 0.1;
            }
            vector.setY(vehicleEntity.getDirectionYheight());
            if (vector.length() != 0.0) {
                vector.normalize();
            }
            vector.multiply(d);
            if (vehicleEntity.getSpeed() < 0.1) {
                vector.setY(vector.getY() - d2);
            }
            double d3 = vehicleEntity.getModelEntity().getHeadPose().getX();
            if (vehicleEntity.getSpeed() <= 0.2 && !vehicleEntity.isOnGround()) {
                d3 = vehicleEntity.getModelEntity().getHeadPose().getX();
                if ((d3 += 0.05235987755982988) > 0.7853981633974483) {
                    d3 = 0.7853981633974483;
                } else if (d3 < -0.7853981633974483) {
                    d3 = -0.7853981633974483;
                } else {
                    vehicleEntity.setDirectionYHeight(vehicleEntity.getDirectionYheight() - 0.1);
                }
                vehicleEntity.getModelEntity().setHeadPose(new EulerAngle(d3, vehicleEntity.getModelEntity().getHeadPose().getY(), 0.0));
            } else if (vehicleEntity.isOnGround()) {
                double d4 = 0.01;
                if (Math.abs(d3) < d4) {
                    d3 = 0.0;
                } else if (d3 > 0.0) {
                    d3 -= d4;
                } else if (d3 < 0.0) {
                    d3 += d4;
                }
                vehicleEntity.getModelEntity().setHeadPose(new EulerAngle(d3, vehicleEntity.getModelEntity().getHeadPose().getY(), 0.0));
                if (vehicleEntity.getDirectionYheight() < 0.0) {
                    vehicleEntity.setSpeed(Math.max(0.0, vehicleEntity.getSpeed() - 0.03));
                    vehicleEntity.setDirectionYHeight(0.0);
                }
            }
            if (d3 > 0.7853981633974483) {
                d3 = 0.7853981633974483;
            }
            if (d3 < -0.7853981633974483) {
                d3 = -0.7853981633974483;
            }
            vehicleEntity.getModelEntity().setHeadPose(new EulerAngle(d3, vehicleEntity.getModelEntity().getHeadPose().getY(), 0.0));
        } else if (bl3) {
            if (!BlockCollisionUtil.isSolid(material)) {
                vector.setY(Math.max(-1.0, vehicleEntity.getDriverSeat().getVelocity().getY() - 0.05));
            }
        } else {
            vector.setY(vehicleEntity.getDirectionYheight());
            if (vehicleEntity.getDirectionYheight() > 0.0) {
                vehicleEntity.setDirectionYHeight(vehicleEntity.getDirectionYheight() - 0.05);
            } else if (vehicleEntity.getDirectionYheight() < 0.0) {
                vehicleEntity.setDirectionYHeight(vehicleEntity.getDirectionYheight() + 0.05);
            }
        }
        if (bl2) {
            Material material2 = BlockCollisionUtil.getMaterial(vehicleEntity.getDriverSeat().getLocation());
            if (material2 == Material.WATER || material2 == Material.SEAGRASS || material2 == Material.TALL_SEAGRASS || material2 == Material.KELP) {
                if (vehicleEntity.getDriverSeat().getLocation().getBlock().getRelative(BlockFace.UP).getType() == Material.WATER) {
                    vector.setY(0.1);
                } else {
                    vector.setY(0);
                }
            } else {
                return;
            }
        }
        if (this.canJump()) {
            Location location2;
            Location location3;
            Location location4;
            Location location5 = vehicleEntity.getCenter().clone().add(vehicleEntity.getDirection().clone().multiply(vehicleEntity.getBoundingBox().getWidth()));
            boolean bl5 = false;
            if (BlockCollisionUtil.isSolidAt(location5) && !BlockCollisionUtil.isSolidAt(location5.clone().add(0.0, 1.0, 0.0))) {
                vector.setY(this.jumphiehgt);
                bl5 = true;
            }
            if (!bl5 && BlockCollisionUtil.isSolidAt(location4 = vehicleEntity.getCenter().clone().add(vehicleEntity.getDirection().clone())) && !BlockCollisionUtil.isSolidAt(location4.clone().add(0.0, 1.0, 0.0))) {
                vector.setY(this.jumphiehgt);
                bl5 = true;
            }
            if (!bl5 && BlockCollisionUtil.isSolidAt(location3 = vehicleEntity.getCenter().clone().subtract(vehicleEntity.getDirection().clone().multiply(vehicleEntity.getBoundingBox().getWidth()))) && !BlockCollisionUtil.isSolidAt(location3.clone().add(0.0, 1.0, 0.0))) {
                vector.setY(this.jumphiehgt);
                bl5 = true;
            }
            if (!bl5 && BlockCollisionUtil.isSolidAt(location2 = vehicleEntity.getCenter().clone().subtract(vehicleEntity.getDirection().clone())) && !BlockCollisionUtil.isSolidAt(location2.clone().add(0.0, 1.0, 0.0))) {
                vector.setY(this.jumphiehgt);
            }
        }
        try {
            if (vehicleEntity.getDriverSeat().getLocation().getY() >= (double)vehicleEntity.getDriverSeat().getLocation().getWorld().getMaxHeight()) {
                Main.DEBUG("Y limit: " + vehicleEntity.getDriverSeat().getLocation().getWorld().getMaxHeight() + " y: " + vehicleEntity.getDriverSeat().getLocation().getY());
                vector.setY(vector.getY() - 1.0);
            }
        } catch (Error | Exception throwable) {
            // empty catch block
        }
        if (vehicleEntity.getDriverSeat().getLocation().getY() >= (double)Main.maxYheightForVehicles) {
            Main.DEBUG("Y limit: " + Main.maxYheightForVehicles + " y: " + vehicleEntity.getDriverSeat().getLocation().getY());
            vector.setY(vector.getY() - 1.0);
        }
        if (Main.enableCrossVehicleCollision) {
            for (VehicleEntity vehicleEntity2 : Main.vehicles) {
                if (vehicleEntity2.equals(vehicleEntity) || !QualityArmoryVehicles.isWithinVehicle(vehicleEntity.getDriverSeat().getLocation(), vehicleEntity2)) continue;
                Main.DEBUG("Collision with vehicle: " + vehicleEntity2.getType().getName());
                vector.multiply(-1);
            }
        }
        this.applyModifiers(vehicleEntity, vector);
        vehicleEntity.getDriverSeat().setVelocity(vector);
        this.handleOtherStands(vehicleEntity, vector);
        this.playAnimation(vehicleEntity, Animation.AnimationType.RUN, new String[0]);
        Entity entity = vehicleEntity.getDriverSeat().getPassenger();
        if (entity instanceof Player) {
            Player player = (Player)entity;
            if (!ProtectionHandler.canMove(player, vehicleEntity.getDriverSeat().getLocation())) {
                VehicleUtils.callback(vehicleEntity, Bukkit.getPlayer((UUID)vehicleEntity.getOwner()), "Not allowed");
            }
            if (Main.sendActionBarOnMove) {
                if (!(player.getGameMode().equals((Object)GameMode.CREATIVE) && Main.bypassCoalInCreative || vehicleEntity.getFuel() > 0)) {
                    return;
                }
                HotbarMessager.sendHotBarMessage(player, MessagesConfig.MESSAGE_ACTIOBAR_MOVE.replace("%type%", vehicleEntity.getType().getDisplayname()).replace("%fuel%", String.valueOf(vehicleEntity.getFuel())).replace("%speed%", String.valueOf(Math.round(vehicleEntity.getSpeed() * 20.0 * (double)(vehicleEntity.getSpeed() < 0.0 ? -1 : 1)))));
            }
        }
    }

    public void applyModifiers(VehicleEntity vehicleEntity, Vector vector) {
    }

    public void handleOtherStands(VehicleEntity vehicleEntity, Vector vector) {
        Vector vector2;
        Location location;
        Object object;
        if (!vehicleEntity.getDriverSeat().equals((Object)vehicleEntity.getModelEntity())) {
            Object object2;
            Object object3 = object2 = this.getDriverSeat() != null ? vehicleEntity.getDriverSeat().getLocation().subtract(QualityArmoryVehicles.rotateRelToCar(vehicleEntity, (Entity)vehicleEntity.getModelEntity(), this.getDriverSeat(), false)) : vehicleEntity.getDriverSeat().getLocation();
            if (this.checkDistance((Entity)vehicleEntity.getModelEntity(), (Location)object2, false)) {
                HeadPoseUtil.setHeadPoseUsingReflection(vehicleEntity);
            }
            Vector object4 = vector.clone();
            object = object2.clone().subtract(vehicleEntity.getModelEntity().getLocation()).toVector();
            object4.add((Vector)object);
            vehicleEntity.getModelEntity().setVelocity(object4);
        }
        for (Entity entity : vehicleEntity.getPassagerSeats()) {
            object = vehicleEntity.getDriverSeat().getLocation().clone().add(QualityArmoryVehicles.rotateRelToCar(vehicleEntity, vehicleEntity.getDriverSeat(), this.getPassagerSpots().get(Integer.parseInt(entity.getCustomName().split(Main.PASSAGER_PREFIX)[1])), false));
            object.add(vehicleEntity.getDriverSeat().getVelocity());
            object.subtract(0.0, 0.6, 0.0);
            location = vector.clone();
            this.checkDistance(entity, (Location)object, true);
            vector2 = object.toVector().clone().subtract(entity.getLocation().toVector());
            location.add(vector2);
            entity.setVelocity((Vector)location);
        }
        for (Map.Entry entry : vehicleEntity.getAttachments().entrySet()) {
            object = this.attachments.get((Integer)entry.getKey());
            location = vehicleEntity.getDriverSeat().getLocation().clone().add(((Attachment)object).getVector());
            location.add(vehicleEntity.getDriverSeat().getVelocity());
            location.subtract(0.0, 0.6, 0.0);
            vector2 = vector.clone();
            this.checkDistance((Entity)entry.getValue(), location, true);
            Vector vector3 = location.toVector().clone().subtract(((Entity)entry.getValue()).getLocation().toVector());
            vector2.add(vector3);
            ((Entity)entry.getValue()).setVelocity(vector2);
            ((Attachment)object).animate(vehicleEntity, (ArmorStand)entry.getValue());
        }
    }

    private boolean checkDistance(Entity entity, Location location, boolean bl) {
        double d = location.distance(entity.getLocation());
        if (d > 1.0) {
            Entity entity2 = entity.getPassenger();
            if (bl && entity2 == null) {
                entity.remove();
                return false;
            }
            NMSUtil.teleport(entity, location);
            Main.DEBUG("Moved other stand. Previous rider: " + entity2 + " - new rider: " + entity.getPassenger());
            return true;
        }
        return false;
    }

    public boolean isAllowedInShop() {
        return this.inShop;
    }

    public int getCost() {
        return this.price;
    }

    public boolean enableFuel() {
        return this.enableFuels;
    }

    public double getMaxHealth() {
        return this.maxhealth;
    }

    public void setMaxHealth(double d) {
        this.maxhealth = d;
    }

    public List<Vector> getPassagerSpots() {
        return new ArrayList<Vector>(this.passagerOffset.keySet());
    }

    public void setPassagerSpots(HashMap<Vector, Integer> hashMap) {
        this.passagerOffset = hashMap;
    }

    public void setAttachments(List<Attachment> list) {
        this.attachments = list;
    }

    public List<Animation> getAnimations() {
        return this.animations;
    }

    public void playAnimation(VehicleEntity vehicleEntity, Animation.AnimationType animationType, String ... stringArray) {
        this.animations.stream().filter(animation -> animation.getType().equals((Object)animationType) && Arrays.equals(animation.getArgs(), stringArray)).forEach(animation -> ModelEngineHook.playAnimation(vehicleEntity, animation.getId()));
    }

    @Nullable
    public FInput getInput(@NotNull FInput.ClickType clickType) {
        return this.inputs.get(clickType.toString());
    }

    public Map<String, FInput> getInputs() {
        return this.inputs;
    }

    public double getRotationMultiplier() {
        return this.rotationMultiplier;
    }

    public void setRotationMultiplier(double d) {
        this.rotationMultiplier = d;
    }

    public List<Attachment> getAttachments() {
        return this.attachments;
    }

    public String toString() {
        return "{className='" + this.getClass().getName() + '\'' + "rotationMultiplier=" + this.rotationMultiplier + ", inputs=" + this.inputs + ", widthRadius=" + this.widthRadius + ", height=" + this.height + ", internalName='" + this.internalName + '\'' + ", bodyFix=" + this.bodyFix + ", rotationDelta=" + this.rotationDelta + ", maxSpeed=" + this.maxSpeed + ", maxBackupSpeed=" + this.maxBackupSpeed + ", destructable=" + this.destructable + ", disableMeleeDamage=" + this.disableMeleeDamage + ", disableProjectileDamage=" + this.disableProjectileDamage + ", jumphiehgt=" + this.jumphiehgt + ", driverSeat=" + this.driverSeat + ", passagerOffset=" + this.passagerOffset + ", animations=" + this.animations + ", id=" + this.id + ", material=" + this.material + ", vehicleModel=" + this.vehicleModel + ", center=" + this.center + ", displayname='" + this.displayname + '\'' + ", lore=" + this.lore + ", acceleration=" + this.acceleration + ", size=" + (Object)((Object)this.size) + ", canJump=" + this.canJump + ", maxhealth=" + this.maxhealth + ", price=" + this.price + ", inShop=" + this.inShop + ", sound='" + this.sound + '\'' + ", playSoundsDriving=" + this.playSoundsDriving + ", soundVolume=" + this.soundVolume + ", trunksize=" + this.trunksize + ", enableFuels=" + this.enableFuels + '}';
    }
}

