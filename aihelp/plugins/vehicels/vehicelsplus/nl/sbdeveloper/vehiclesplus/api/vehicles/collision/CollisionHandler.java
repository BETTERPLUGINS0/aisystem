/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.World
 *  org.bukkit.block.Block
 *  org.bukkit.block.BlockFace
 *  org.bukkit.block.data.BlockData
 *  org.bukkit.block.data.type.Stairs
 *  org.bukkit.entity.ArmorStand
 *  org.bukkit.entity.Damageable
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.util.Vector
 */
package nl.sbdeveloper.vehiclesplus.api.vehicles.collision;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import lombok.Generated;
import nl.sbdeveloper.vehiclesplus.VehiclesPlus;
import nl.sbdeveloper.vehiclesplus.api.VehiclesPlusAPI;
import nl.sbdeveloper.vehiclesplus.api.events.CancellableVehicleEvent;
import nl.sbdeveloper.vehiclesplus.api.events.impl.VehicleCollisionEvent;
import nl.sbdeveloper.vehiclesplus.api.events.impl.VehicleDespawnEvent;
import nl.sbdeveloper.vehiclesplus.api.events.impl.VehicleDestroyEvent;
import nl.sbdeveloper.vehiclesplus.api.vehicles.collision.BlockHardness;
import nl.sbdeveloper.vehiclesplus.api.vehicles.collision.BlockHeight;
import nl.sbdeveloper.vehiclesplus.api.vehicles.collision.DamageType;
import nl.sbdeveloper.vehiclesplus.api.vehicles.collision.HitboxCalculator;
import nl.sbdeveloper.vehiclesplus.api.vehicles.collision.HitboxPoint;
import nl.sbdeveloper.vehiclesplus.api.vehicles.collision.HitboxResult;
import nl.sbdeveloper.vehiclesplus.api.vehicles.collision.HitboxSide;
import nl.sbdeveloper.vehiclesplus.api.vehicles.impl.SpawnedVehicle;
import nl.sbdeveloper.vehiclesplus.api.vehicles.parts.impl.seat.Seat;
import nl.sbdeveloper.vehiclesplus.api.vehicles.parts.impl.skin.Skin;
import nl.sbdeveloper.vehiclesplus.api.vehicles.types.MovementType;
import nl.sbdeveloper.vehiclesplus.utils.math.BoundingBox;
import nl.sbdeveloper.vehiclesplus.utils.nms.MovementUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Stairs;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.util.Vector;

public class CollisionHandler {
    private static final double MAX_STEP_HEIGHT = 1.375;
    private static final float SPEED_EPS = 0.1f;
    private static final double MOTION_EPS = 0.02;
    private final SpawnedVehicle spawnedVehicle;
    private List<HitboxPoint> hitboxPoints = List.of();
    private List<Vector> hitboxCorners = List.of();
    private float vehicleRotation = 0.0f;
    private boolean busyChecking = false;
    private boolean didAction = false;
    private boolean blocked = false;
    private HitboxSide blockedSide = null;
    private Location lastHolderLoc = null;

    public CollisionHandler(SpawnedVehicle spawnedVehicle) {
        this.spawnedVehicle = spawnedVehicle;
    }

    public void clearBlock() {
        this.blocked = false;
        this.blockedSide = null;
    }

    private void setBlocked(HitboxSide hitboxSide) {
        if (hitboxSide == HitboxSide.FRONT || hitboxSide == HitboxSide.BACK) {
            this.blocked = true;
            this.blockedSide = hitboxSide;
        }
    }

    private BoundingBox getBoundingBox() {
        Vector vector2;
        double d = Double.MAX_VALUE;
        double d2 = Double.MAX_VALUE;
        double d3 = Double.MAX_VALUE;
        double d4 = -1.7976931348623157E308;
        double d5 = -1.7976931348623157E308;
        double d6 = -1.7976931348623157E308;
        for (Vector vector2 : this.hitboxCorners) {
            d = Math.min(d, vector2.getX());
            d4 = Math.max(d4, vector2.getX());
            d2 = Math.min(d2, vector2.getY());
            d5 = Math.max(d5, vector2.getY());
            d3 = Math.min(d3, vector2.getZ());
            d6 = Math.max(d6, vector2.getZ());
        }
        Location location = this.spawnedVehicle.getHolder().getLocation();
        vector2 = location.clone().add(d, d2, d3).add(-2.0, -2.0, -2.0);
        Location location2 = location.clone().add(d4, d5, d6).add(2.0, 2.0, 2.0);
        return new BoundingBox((Location)vector2, location2);
    }

    public void calculateHitbox() {
        float f = this.spawnedVehicle.getHolder().getLocation().getYaw();
        if (f != this.vehicleRotation || this.hitboxPoints.isEmpty()) {
            this.vehicleRotation = f;
            HitboxResult hitboxResult = HitboxCalculator.calculate(this.spawnedVehicle.getVehicleModel().getHitbox(), f);
            this.hitboxPoints = hitboxResult.getHitboxPoints();
            this.hitboxCorners = hitboxResult.getHitboxCorners();
        }
    }

    public boolean hasNoCollision(CollisionHandler collisionHandler) {
        return HitboxCalculator.hasNoCollision(this.hitboxCorners, collisionHandler.hitboxCorners);
    }

    public void checkCollision() {
        if (!Bukkit.isPrimaryThread()) {
            throw new UnsupportedOperationException("CollisionHandler#checkCollision() should always be run sync!");
        }
        boolean bl = this.spawnedVehicle.getParts().stream().anyMatch(part -> part instanceof Seat && ((Seat)part).isOccupied());
        if (bl && !this.busyChecking) {
            Location location;
            double d;
            this.refreshBlockedStateIfClear();
            this.didAction = false;
            this.busyChecking = true;
            this.calculateHitbox();
            Location location2 = this.spawnedVehicle.getHolder().getLocation();
            Collection<Entity> collection = this.getBoundingBox().getNearbyEntities(this.spawnedVehicle.getHolder().getWorld(), entity -> !entity.isInsideVehicle() && entity instanceof Damageable);
            double d2 = 0.0;
            Vector vector = location2.getDirection().normalize();
            if (this.lastHolderLoc != null && this.lastHolderLoc.getWorld() == location2.getWorld()) {
                Vector vector2 = location2.toVector().subtract(this.lastHolderLoc.toVector());
                d2 = vector2.dot(vector);
            }
            float f = this.spawnedVehicle.getStatics().getCurrentSpeed();
            double d3 = d = Math.abs(d2) >= 0.02 ? d2 : (double)f;
            if (Math.abs(d) > (double)0.1f) {
                for (HitboxPoint hitboxPoint : this.hitboxPoints) {
                    if (hitboxPoint.getSide() != HitboxSide.BOTTOM) continue;
                    location = hitboxPoint.toLocation(location2);
                    Object object = this.classifyFrontBack(location, location2, vector);
                    this.handleBottomStepUp(location, (HitboxSide)((Object)object), d);
                    if (!this.didAction) continue;
                    break;
                }
            }
            HashSet hashSet = new HashSet();
            if (!this.didAction) {
                HitboxPoint hitboxPoint;
                hitboxPoint = null;
                location = null;
                for (HitboxPoint hitboxPoint2 : this.hitboxPoints) {
                    Location location3 = hitboxPoint2.toLocation(location2);
                    HitboxSide hitboxSide = this.classifyFrontBack(location3, location2, vector);
                    if (hitboxSide != HitboxSide.FRONT && hitboxSide != HitboxSide.BACK || this.blocked && this.blockedSide != null && this.blockedSide != hitboxSide) continue;
                    Block block = location3.getBlock();
                    if (this.isVerticalWall(block)) {
                        if (hitboxSide == HitboxSide.FRONT) {
                            if (hitboxPoint == null) {
                                hitboxPoint = block;
                            }
                            if (location != null) {
                                this.sameBlock((Block)hitboxPoint, (Block)location);
                            }
                        } else {
                            if (location == null) {
                                location = block;
                            }
                            if (hitboxPoint != null) {
                                this.sameBlock((Block)hitboxPoint, (Block)location);
                            }
                        }
                        boolean bl2 = this.isMovingIntoSide(d, hitboxSide);
                        this.handleFrontBackBlock(location3, hitboxSide, d);
                        if (bl2) {
                            this.didAction = true;
                        }
                    }
                    if (!this.didAction) {
                        this.handleEntityCollisionFrontBack(location3, hitboxSide, collection.iterator(), hashSet, d);
                    }
                    if (!this.didAction || !collection.isEmpty()) continue;
                    break;
                }
            }
            this.busyChecking = false;
            this.lastHolderLoc = location2.clone();
        }
    }

    private void handleBottomStepUp(Location location, HitboxSide hitboxSide, double d) {
        Skin skin;
        if (!this.didAction && !this.spawnedVehicle.getVehicleModel().getType().isMovementType(MovementType.WATER) && this.isMovingIntoSide(d, hitboxSide) && (skin = this.spawnedVehicle.getPart(Skin.class)) != null) {
            double d2;
            Location location2 = this.spawnedVehicle.getHolder().getLocation();
            Location location3 = location2.clone().add(-skin.getXOffset(), -skin.getYOffset(), -skin.getZOffset());
            Vector vector = location2.getDirection().normalize();
            Location location4 = this.clampProbeToImmediateStep(location2, vector, location);
            double d3 = this.sampleTopWorldYAtColumn(location4, d2 = this.sampleTopWorldYAt(location3));
            if (!Double.isInfinite(d3)) {
                boolean bl;
                double d4 = d3 - d2;
                boolean bl2 = bl = d4 > 0.0 && d4 <= 0.5 && VehiclesPlus.getStorage().getConfig().getCollision().isSlabDriving() || d4 > 0.5 && d4 <= 1.375 && VehiclesPlus.getStorage().getConfig().getCollision().isBlockDriving();
                if (bl) {
                    Location location5 = location2.clone();
                    location5.add(location5.getDirection().multiply((double)this.spawnedVehicle.getStatics().getCurrentSpeed() / 100.0));
                    location5.setY(location2.getY() + d4);
                    MovementUtil.setPosition(this.spawnedVehicle.getHolder(), location5);
                    this.didAction = true;
                } else if (d4 > 1.375 && (hitboxSide == HitboxSide.FRONT || hitboxSide == HitboxSide.BACK)) {
                    this.handleFrontBackBlock(location, hitboxSide, d);
                    this.didAction = true;
                }
            }
        }
    }

    private Location clampProbeToImmediateStep(Location location, Vector vector, Location location2) {
        Vector vector2 = location2.toVector().subtract(location.toVector());
        double d = vector2.dot(vector);
        double d2 = d >= 0.0 ? 1.0 : -1.0;
        double d3 = Math.min(Math.abs(d), 0.6) * d2;
        Vector vector3 = vector.clone().multiply(d3);
        return location.clone().add(vector3);
    }

    private double sampleTopWorldYAt(Location location) {
        World world = location.getWorld();
        int n = location.getBlockX();
        int n2 = location.getBlockY();
        int n3 = location.getBlockZ();
        for (int i = 0; i >= -2; --i) {
            assert (world != null);
            Block block = world.getBlockAt(n, n2 + i, n3);
            if (block.isPassable()) continue;
            double d = BlockHeight.getHeight(block);
            BlockData blockData = block.getBlockData();
            if (blockData instanceof Stairs) {
                Stairs stairs = (Stairs)blockData;
                switch (stairs.getHalf()) {
                    case TOP: {
                        d = Math.max(d, 1.0);
                        break;
                    }
                    case BOTTOM: {
                        d = Math.max(d, 0.5);
                    }
                }
            }
            return (double)(n2 + i) + d;
        }
        return n2;
    }

    private double sampleTopWorldYAtColumn(Location location, double d) {
        World world = location.getWorld();
        int n = location.getBlockX();
        int n2 = location.getBlockZ();
        int n3 = (int)Math.floor(d);
        double d2 = Double.NEGATIVE_INFINITY;
        for (int i = -1; i <= 2; ++i) {
            double d3;
            assert (world != null);
            Block block = world.getBlockAt(n, n3 + i, n2);
            if (block.isPassable()) continue;
            double d4 = BlockHeight.getHeight(block);
            BlockData blockData = block.getBlockData();
            if (blockData instanceof Stairs) {
                Stairs stairs = (Stairs)blockData;
                Vector vector = location.getDirection();
                Vector vector2 = switch (stairs.getFacing()) {
                    case BlockFace.NORTH -> new Vector(0, 0, -1);
                    case BlockFace.SOUTH -> new Vector(0, 0, 1);
                    case BlockFace.WEST -> new Vector(-1, 0, 0);
                    case BlockFace.EAST -> new Vector(1, 0, 0);
                    default -> new Vector(0, 0, 0);
                };
                double d5 = vector.clone().setY(0).normalize().dot(vector2);
                if (d5 > 0.3) {
                    d4 = Math.max(d4, 1.0);
                } else if (d5 < -0.3) {
                    d4 = Math.max(d4, 0.5);
                }
            }
            if (!((d3 = (double)(n3 + i) + d4) > d2)) continue;
            d2 = d3;
        }
        return d2 == Double.NEGATIVE_INFINITY ? Double.POSITIVE_INFINITY : d2;
    }

    private boolean isMovingIntoSide(double d, HitboxSide hitboxSide) {
        return hitboxSide == HitboxSide.FRONT && d > (double)0.1f || hitboxSide == HitboxSide.BACK && d < (double)0.1f;
    }

    private void handleFrontBackBlock(Location location, HitboxSide hitboxSide, double d) {
        boolean bl;
        Block block = location.getBlock();
        if (block.getType().isSolid() && (!this.blocked || this.blockedSide == null || this.blockedSide == hitboxSide) && (bl = this.isMovingIntoSide(d, hitboxSide))) {
            double d2 = this.spawnedVehicle.getStatics().getCurrentSpeedKMPH();
            double d3 = BlockHardness.getHardnessForBlock(block);
            double d4 = BlockHeight.getHeight(block);
            double d5 = Math.pow(d2 / 10.0, 1.5) * Math.min(1.0, d4) * Math.min(1.5, d3);
            this.stopVehicle(hitboxSide);
            this.setBlocked(hitboxSide);
            if (d5 > 0.15) {
                this.damageVehicle(this.spawnedVehicle, d3, d2);
            }
            String string = String.valueOf((Object)hitboxSide);
        }
    }

    private void handleEntityCollisionFrontBack(Location location, HitboxSide hitboxSide, Iterator<Entity> iterator, Set<SpawnedVehicle> set, double d) {
        if (!(hitboxSide != HitboxSide.FRONT && hitboxSide != HitboxSide.BACK || this.blocked && this.blockedSide != null && this.blockedSide != hitboxSide)) {
            while (iterator.hasNext()) {
                Entity entity = iterator.next();
                if (entity instanceof ArmorStand) {
                    if (location.distanceSquared(entity.getLocation()) > 2.0) continue;
                    iterator.remove();
                    VehiclesPlusAPI.getVehicleFromHolder((ArmorStand)entity).ifPresent(spawnedVehicle -> {
                        if (!spawnedVehicle.getStorageVehicle().getUuid().equals(this.spawnedVehicle.getStorageVehicle().getUuid()) && set.add((SpawnedVehicle)spawnedVehicle)) {
                            this.handleVehicleCollisionFrontBack((SpawnedVehicle)spawnedVehicle, hitboxSide, d);
                        }
                    });
                    continue;
                }
                if (!entity.getBoundingBox().contains(location.getX(), location.getY(), location.getZ())) continue;
                iterator.remove();
                this.handleEntityCollisionFrontBack(entity, hitboxSide, d);
            }
        }
    }

    private void handleEntityCollisionFrontBack(Entity entity, HitboxSide hitboxSide, double d) {
        double d2 = this.spawnedVehicle.getStatics().getCurrentSpeedKMPH();
        VehicleCollisionEvent vehicleCollisionEvent = new VehicleCollisionEvent(this.spawnedVehicle, entity, d2 >= 10.0, d2, hitboxSide);
        Bukkit.getPluginManager().callEvent((Event)vehicleCollisionEvent);
        if (!vehicleCollisionEvent.isCancelled()) {
            boolean bl = this.isMovingIntoSide(d, hitboxSide);
            if (VehiclesPlus.getStorage().getConfig().getCollision().isStopAtEntity() && bl) {
                this.stopVehicle(hitboxSide);
                this.setBlocked(hitboxSide);
            }
            if (d2 >= 10.0 && bl) {
                this.damageVehicle(this.spawnedVehicle, 1.5, d2);
                if (entity instanceof Damageable) {
                    this.damageEntity(this.spawnedVehicle, (Damageable)entity, d2);
                }
            }
            if (bl) {
                this.didAction = true;
            }
        }
    }

    private void handleVehicleCollisionFrontBack(SpawnedVehicle spawnedVehicle, HitboxSide hitboxSide, double d) {
        if (!this.hasNoCollision(spawnedVehicle.getCollisionHandler())) {
            double d2 = this.spawnedVehicle.getStatics().getCurrentSpeedKMPH();
            VehicleCollisionEvent vehicleCollisionEvent = new VehicleCollisionEvent(this.spawnedVehicle, spawnedVehicle, d2 >= 10.0, d2, hitboxSide);
            Bukkit.getPluginManager().callEvent((Event)vehicleCollisionEvent);
            if (!vehicleCollisionEvent.isCancelled()) {
                boolean bl = this.isMovingIntoSide(d, hitboxSide);
                if (VehiclesPlus.getStorage().getConfig().getCollision().isStopAtVehicle() && bl) {
                    this.stopVehicle(hitboxSide);
                    this.setBlocked(hitboxSide);
                }
                if (d2 >= 10.0 && bl) {
                    this.damageVehicle(this.spawnedVehicle, 1.5, d2);
                    this.damageVehicle(spawnedVehicle, 1.5, d2);
                }
                if (bl) {
                    this.didAction = true;
                }
            }
        }
    }

    private boolean isFenceOrWall(Block block) {
        String string = block.getType().name();
        return string.endsWith("_FENCE") || string.endsWith("_WALL");
    }

    private boolean isVerticalWall(Block block) {
        if (!block.getType().isSolid()) {
            return false;
        }
        if (this.isFenceOrWall(block)) {
            return true;
        }
        double d = BlockHeight.getHeight(block);
        if (d > 1.375) {
            return true;
        }
        int n = (int)Math.ceil(1.375);
        for (int i = 1; i <= n; ++i) {
            Block block2 = block.getRelative(0, i, 0);
            if (!block2.isPassable()) continue;
            return false;
        }
        return true;
    }

    private HitboxSide classifyFrontBack(Location location, Location location2, Vector vector) {
        Vector vector2 = location.toVector().subtract(location2.toVector());
        double d = vector2.dot(vector);
        return d >= 0.0 ? HitboxSide.FRONT : HitboxSide.BACK;
    }

    private boolean sameBlock(Block block, Block block2) {
        return block.getX() == block2.getX() && block.getY() == block2.getY() && block.getZ() == block2.getZ() && block.getWorld() == block2.getWorld();
    }

    private void stopVehicle(HitboxSide hitboxSide) {
        double d = hitboxSide == HitboxSide.FRONT ? -0.02 : 0.02;
        Location location = this.spawnedVehicle.getHolder().getLocation().clone();
        Vector vector = location.getDirection().normalize().multiply(d);
        Location location2 = location.add(vector.getX(), 0.0, vector.getZ());
        this.spawnedVehicle.getHolder().teleport(location2);
        this.spawnedVehicle.getStatics().setCurrentSpeed(0.0f);
        String string = String.valueOf((Object)hitboxSide);
    }

    private DamageType getConfiguredDamageType() {
        String string = VehiclesPlus.getStorage().getConfig().getCollision().getDamageLevel();
        return DamageType.fromString(string);
    }

    private void damageVehicle(SpawnedVehicle spawnedVehicle, double d, double d2) {
        DamageType damageType = this.getConfiguredDamageType();
        if (damageType != DamageType.NONE) {
            Object object;
            double d3 = this.calculateDamage(spawnedVehicle, d, d2);
            int n = (int)d3;
            int n2 = spawnedVehicle.getStatics().getCurrentHealth();
            int n3 = n2 - n;
            spawnedVehicle.getStatics().setCurrentHealth(n3);
            if (n3 <= 0) {
                object = new VehicleDestroyEvent(spawnedVehicle);
                Bukkit.getPluginManager().callEvent((Event)object);
                if (!((CancellableVehicleEvent)((Object)object)).isCancelled()) {
                    spawnedVehicle.getStatics().setBroken(true);
                    if (VehiclesPlus.getStorage().getConfig().getCollision().isDespawnVehicle()) {
                        spawnedVehicle.despawn(VehicleDespawnEvent.DespawnReason.DESTROY, true);
                    }
                }
            }
            if (n > 0) {
                object = spawnedVehicle.getParts().stream().filter(part -> part instanceof Seat).map(part -> (Seat)part).filter(seat -> seat.getHolder() != null).flatMap(seat -> seat.getPassenger().stream()).toList();
                Iterator iterator = object.iterator();
                while (iterator.hasNext()) {
                    Player player = (Player)iterator.next();
                    this.damageEntity(spawnedVehicle, n, (Damageable)player);
                }
            }
        }
    }

    private double calculateDamage(SpawnedVehicle spawnedVehicle, double d, double d2) {
        DamageType damageType = this.getConfiguredDamageType();
        if (damageType == DamageType.NONE) {
            return 0.0;
        }
        double d3 = d2 / 20.0;
        double d4 = Math.pow(d3, damageType.getDamageMultiplier()) * d * 5.0;
        return Math.min(d4, (double)spawnedVehicle.getVehicleModel().getHealth());
    }

    private void damageEntity(SpawnedVehicle spawnedVehicle, Damageable damageable, double d) {
        double d2 = this.calculateDamage(spawnedVehicle, 1.5, d);
        int n = (int)d2;
        damageable.damage((double)n / 3.0, (Entity)spawnedVehicle.getHolder());
    }

    private void damageEntity(SpawnedVehicle spawnedVehicle, int n, Damageable damageable) {
        damageable.damage((double)n / 3.0, (Entity)spawnedVehicle.getHolder());
    }

    private String coords(Location location) {
        return String.format(Locale.ROOT, "(%.1f, %.1f, %.1f @ %s)", location.getX(), location.getY(), location.getZ(), Objects.requireNonNull(location.getWorld()).getName());
    }

    private String blockPos(Block block) {
        int n = block.getX();
        return "[" + n + "," + block.getY() + "," + block.getZ() + "]";
    }

    private void refreshBlockedStateIfClear() {
        if (this.blocked) {
            Location location = this.spawnedVehicle.getHolder().getLocation();
            Skin skin = this.spawnedVehicle.getPart(Skin.class);
            Location location2 = location.clone();
            if (skin != null) {
                location2.add(-skin.getXOffset(), -skin.getYOffset(), -skin.getZOffset());
            }
            Vector vector = location.getDirection().normalize();
            double d = 0.6;
            Location location3 = location2.clone().add(vector.multiply(this.blockedSide == HitboxSide.FRONT ? d : -d));
            Block block = location3.getBlock();
            if (block.isPassable()) {
                this.blocked = false;
                this.blockedSide = null;
            } else {
                boolean bl;
                double d2 = this.sampleTopWorldYAt(location2);
                double d3 = this.sampleTopWorldYAtColumn(location3, d2);
                double d4 = d3 - d2;
                boolean bl2 = bl = d4 > 0.0 && d4 <= 0.5 && VehiclesPlus.getStorage().getConfig().getCollision().isSlabDriving() || d4 > 0.5 && d4 <= 1.375 && VehiclesPlus.getStorage().getConfig().getCollision().isBlockDriving();
                if (bl) {
                    this.blocked = false;
                    this.blockedSide = null;
                }
            }
        }
    }

    public String toString() {
        String string = String.valueOf(this.hitboxPoints);
        return "CollisionHandler{hitboxPoints=" + string + ", busyChecking=" + this.busyChecking + ", didAction=" + this.didAction + ", blocked=" + this.blocked + ", blockedSide=" + String.valueOf((Object)this.blockedSide) + "}";
    }

    @Generated
    public List<HitboxPoint> getHitboxPoints() {
        return this.hitboxPoints;
    }

    @Generated
    public boolean isBlocked() {
        return this.blocked;
    }

    @Generated
    public HitboxSide getBlockedSide() {
        return this.blockedSide;
    }
}

