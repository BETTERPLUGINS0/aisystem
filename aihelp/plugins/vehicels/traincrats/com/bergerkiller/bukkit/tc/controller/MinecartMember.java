package com.bergerkiller.bukkit.tc.controller;

import com.bergerkiller.bukkit.common.Common;
import com.bergerkiller.bukkit.common.ToggledState;
import com.bergerkiller.bukkit.common.bases.IntVector3;
import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import com.bergerkiller.bukkit.common.controller.EntityController;
import com.bergerkiller.bukkit.common.controller.EntityPositionApplier;
import com.bergerkiller.bukkit.common.entity.type.CommonMinecart;
import com.bergerkiller.bukkit.common.inventory.MergedInventory;
import com.bergerkiller.bukkit.common.math.OrientedBoundingBox;
import com.bergerkiller.bukkit.common.math.Quaternion;
import com.bergerkiller.bukkit.common.resources.SoundEffect;
import com.bergerkiller.bukkit.common.utils.BlockUtil;
import com.bergerkiller.bukkit.common.utils.CommonUtil;
import com.bergerkiller.bukkit.common.utils.EntityUtil;
import com.bergerkiller.bukkit.common.utils.FaceUtil;
import com.bergerkiller.bukkit.common.utils.MathUtil;
import com.bergerkiller.bukkit.common.utils.WorldUtil;
import com.bergerkiller.bukkit.common.wrappers.DamageSource;
import com.bergerkiller.bukkit.common.wrappers.HumanHand;
import com.bergerkiller.bukkit.common.wrappers.MoveType;
import com.bergerkiller.bukkit.tc.CollisionMode;
import com.bergerkiller.bukkit.tc.TCConfig;
import com.bergerkiller.bukkit.tc.TCListener;
import com.bergerkiller.bukkit.tc.TCSeatChangeListener;
import com.bergerkiller.bukkit.tc.TrainCarts;
import com.bergerkiller.bukkit.tc.Util;
import com.bergerkiller.bukkit.tc.attachments.animation.Animation;
import com.bergerkiller.bukkit.tc.attachments.animation.AnimationOptions;
import com.bergerkiller.bukkit.tc.attachments.api.Attachment;
import com.bergerkiller.bukkit.tc.attachments.config.AttachmentModel;
import com.bergerkiller.bukkit.tc.attachments.control.CartAttachmentSeat;
import com.bergerkiller.bukkit.tc.controller.components.ActionTrackerMember;
import com.bergerkiller.bukkit.tc.controller.components.AnimationController;
import com.bergerkiller.bukkit.tc.controller.components.AttachmentControllerMember;
import com.bergerkiller.bukkit.tc.controller.components.RailPath;
import com.bergerkiller.bukkit.tc.controller.components.RailPiece;
import com.bergerkiller.bukkit.tc.controller.components.RailState;
import com.bergerkiller.bukkit.tc.controller.components.RailTracker;
import com.bergerkiller.bukkit.tc.controller.components.RailTrackerMember;
import com.bergerkiller.bukkit.tc.controller.components.SignTracker;
import com.bergerkiller.bukkit.tc.controller.components.SignTrackerMember;
import com.bergerkiller.bukkit.tc.controller.components.SoundLoop;
import com.bergerkiller.bukkit.tc.controller.components.WheelTrackerMember;
import com.bergerkiller.bukkit.tc.controller.persistence.DisplayedBlockPersistentCartAttribute;
import com.bergerkiller.bukkit.tc.controller.persistence.EntityTagsPersistentCartAttribute;
import com.bergerkiller.bukkit.tc.controller.persistence.PersistentCartAttribute;
import com.bergerkiller.bukkit.tc.exception.GroupUnloadedException;
import com.bergerkiller.bukkit.tc.exception.MemberMissingException;
import com.bergerkiller.bukkit.tc.properties.CartProperties;
import com.bergerkiller.bukkit.tc.properties.CartPropertiesStore;
import com.bergerkiller.bukkit.tc.properties.IPropertiesHolder;
import com.bergerkiller.bukkit.tc.properties.TrainProperties;
import com.bergerkiller.bukkit.tc.properties.standard.type.CollisionOptions;
import com.bergerkiller.bukkit.tc.properties.standard.type.SlowdownMode;
import com.bergerkiller.bukkit.tc.rails.RailLookup;
import com.bergerkiller.bukkit.tc.rails.WorldRailLookup;
import com.bergerkiller.bukkit.tc.rails.logic.RailLogic;
import com.bergerkiller.bukkit.tc.rails.logic.RailLogicVertical;
import com.bergerkiller.bukkit.tc.rails.type.RailType;
import com.bergerkiller.bukkit.tc.rails.type.RailTypeActivator;
import com.bergerkiller.bukkit.tc.signactions.SignActionType;
import com.bergerkiller.bukkit.tc.utils.TrackIterator;
import com.bergerkiller.bukkit.tc.utils.TrackMap;
import com.bergerkiller.bukkit.tc.utils.TrackWalkingPoint;
import com.bergerkiller.generated.net.minecraft.world.entity.EntityHandle;
import com.bergerkiller.generated.net.minecraft.world.entity.EntityLivingHandle;
import com.bergerkiller.generated.net.minecraft.world.phys.AxisAlignedBBHandle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import org.bukkit.Chunk;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.vehicle.VehicleDamageEvent;
import org.bukkit.event.vehicle.VehicleDestroyEvent;
import org.bukkit.event.vehicle.VehicleEntityCollisionEvent;
import org.bukkit.event.vehicle.VehicleMoveEvent;
import org.bukkit.event.vehicle.VehicleUpdateEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public abstract class MinecartMember<T extends CommonMinecart<?>> extends EntityController<T> implements IPropertiesHolder, AnimationController, TrainCarts.Provider {
   public static final double GRAVITY_MULTIPLIER_RAILED = 0.015625D;
   public static final double GRAVITY_MULTIPLIER = 0.04D;
   public static final int MAXIMUM_DAMAGE_SUSTAINED = 40;
   private final TrainCarts traincarts;
   protected final ToggledState forcedBlockUpdate = new ToggledState(true);
   private final SignTrackerMember signTracker;
   private final ActionTrackerMember actionTracker;
   private final RailTrackerMember railTrackerMember;
   private final WheelTrackerMember wheelTracker;
   private final AttachmentControllerMember attachmentController;
   private final ToggledState railActivated = new ToggledState(false);
   public boolean vertToSlope = false;
   protected MinecartGroup group;
   protected boolean died = false;
   private boolean unloaded = true;
   protected boolean unloadedLastPlayerTakable = false;
   protected SoundLoop<?> soundLoop;
   protected BlockFace direction;
   private BlockFace directionTo;
   private BlockFace directionFrom = null;
   private boolean ignoreAllCollisions = false;
   private int collisionEnterTimer = 0;
   private CartProperties properties;
   private final List<PersistentCartAttribute<? super T>> persistentCartAttributes = new ArrayList();
   private Map<UUID, AtomicInteger> collisionIgnoreTimes = new HashMap();
   private Vector speedFactor = new Vector(0.0D, 0.0D, 0.0D);
   private double roll = 0.0D;
   private Quaternion cachedOrientation_quat = null;
   private float cachedOrientation_yaw = 0.0F;
   private float cachedOrientation_pitch = 0.0F;
   private boolean hasLinkedFarMinecarts = false;
   private Vector lastRailRefreshPosition = null;
   private Vector lastRailRefreshDirection = null;
   private Location firstKnownDerailedPosition = null;
   private List<Entity> enterForced = new ArrayList(1);
   private boolean wasMoving = false;
   private Location lastLocation;
   private Location lastLocationSync;
   private WorldRailLookup railLookup;

   public MinecartMember(TrainCarts traincarts) {
      this.railLookup = WorldRailLookup.NONE;
      if (traincarts == null) {
         throw new IllegalArgumentException("TrainCarts plugin cannot be null");
      } else {
         this.traincarts = traincarts;
         this.signTracker = new SignTrackerMember(this);
         this.actionTracker = new ActionTrackerMember(this);
         this.railTrackerMember = new RailTrackerMember(this);
         this.wheelTracker = new WheelTrackerMember(this);
         this.attachmentController = new AttachmentControllerMember(this);
         this.addPersistentCartAttribute(new DisplayedBlockPersistentCartAttribute());
         if (Common.evaluateMCVersion(">=", "1.10.2")) {
            this.addPersistentCartAttribute(new EntityTagsPersistentCartAttribute());
         }

      }
   }

   public TrainCarts getTrainCarts() {
      return this.traincarts;
   }

   public static boolean isTrackConnected(MinecartMember<?> m1, MinecartMember<?> m2) {
      boolean m1moving = m1.isMoving();
      boolean m2moving = m2.isMoving();
      if (m1moving && m2moving) {
         if (!m1.isFollowingOnTrack(m2) && !m2.isFollowingOnTrack(m1)) {
            return false;
         }
      } else if (m1moving) {
         if (!m1.isFollowingOnTrack(m2)) {
            return false;
         }
      } else if (m2moving) {
         if (!m2.isFollowingOnTrack(m1)) {
            return false;
         }
      } else {
         if (!m1.isNearOf(m2)) {
            return false;
         }

         if (!TrackIterator.isConnected(m1.getBlock(), m2.getBlock(), false)) {
            return false;
         }
      }

      return true;
   }

   public void onAttached() {
      super.onAttached();
      this.setUnloaded(true);
      this.railTrackerMember.onAttached();
      this.soundLoop = new SoundLoop(this);
      this.updateDirection();
      this.wheelTracker.update();
      this.hasLinkedFarMinecarts = false;
      ((CommonMinecart)this.entity).setPreventBlockPlace(false);
      this.setBlockCollisionBounds(new Vector(0.98D, 0.7D, 0.98D));
   }

   public CartProperties getProperties() {
      if (this.properties == null) {
         this.properties = CartPropertiesStore.createForMember(this);
      }

      return this.properties;
   }

   public ConfigurationNode saveConfig() {
      ConfigurationNode savedCartConfig = this.getProperties().saveToConfig().clone();
      savedCartConfig.set("entityType", ((CommonMinecart)this.getEntity()).getType());
      savedCartConfig.set("flipped", this.getOrientationForward().dot(FaceUtil.faceToVector(this.getDirection())) < 0.0D);
      savedCartConfig.remove("owners");
      if (this.entity != null) {
         ConfigurationNode data = new ConfigurationNode();
         Iterator var3 = this.persistentCartAttributes.iterator();

         while(var3.hasNext()) {
            PersistentCartAttribute<? super T> attribute = (PersistentCartAttribute)var3.next();
            attribute.save(this.entity, data);
         }

         if (!data.isEmpty()) {
            savedCartConfig.set("data", data);
         }
      }

      return savedCartConfig;
   }

   public MinecartGroup getGroup() {
      if (this.isUnloaded()) {
         throw new RuntimeException("Unloaded members do not have groups!");
      } else {
         if (this.group == null) {
            MinecartGroupStore.create(this);
            if (this.group == null) {
               if (this.isUnloaded()) {
                  throw new RuntimeException("Unloaded members do not have groups!");
               }

               throw new IllegalStateException("Failed to initialize new group for member at world=" + ((CommonMinecart)this.entity).loc.getWorld().getName() + " x=" + ((CommonMinecart)this.entity).loc.getX() + " y=" + ((CommonMinecart)this.entity).loc.getY() + " z=" + ((CommonMinecart)this.entity).loc.getZ());
            }
         }

         return this.group;
      }
   }

   public boolean hasInitializedGroup() {
      return this.group != null && !this.isUnloaded();
   }

   protected void setGroup(MinecartGroup group) {
      if (this.group != null && this.group != group) {
         this.group.removeSilent(this);
      }

      this.setUnloaded(false);
      this.group = group;
   }

   public void clearGroup() {
      this.setGroup((MinecartGroup)null);
   }

   public int getIndex() {
      if (this.group == null) {
         return ((CommonMinecart)this.entity).isRemoved() ? -1 : 0;
      } else {
         return this.group.indexOf(this);
      }
   }

   public World getWorld() {
      return this.entity == null ? null : ((CommonMinecart)this.entity).getWorld();
   }

   protected void addPersistentCartAttribute(PersistentCartAttribute<? super T> attribute) {
      this.persistentCartAttributes.add(attribute);
   }

   public void onTrainSpawned(ConfigurationNode data) {
      if (this.entity != null) {
         Iterator var2 = this.persistentCartAttributes.iterator();

         while(var2.hasNext()) {
            PersistentCartAttribute<? super T> attribute = (PersistentCartAttribute)var2.next();
            attribute.load(this.entity, data);
         }
      }

   }

   public boolean isOrientationInverted() {
      return Util.isOrientationInverted(this.calculateOrientation(), this.getWheels().getLastOrientation());
   }

   public Vector calculateOrientation() {
      double dx = 0.0D;
      double dy = 0.0D;
      double dz = 0.0D;
      Vector forward;
      if (this.group != null && this.group.size() > 1) {
         int n = 0;
         MinecartMember m;
         Vector s_pos;
         if (this != this.group.head()) {
            m = this.getNeighbour(-1);
            forward = m.calcSpeedFactorPos();
            s_pos = this.calcSpeedFactorPos();
            dx += forward.getX() - s_pos.getX();
            dy += forward.getY() - s_pos.getY();
            dz += forward.getZ() - s_pos.getZ();
            ++n;
         }

         if (this != this.group.tail()) {
            m = this.getNeighbour(1);
            forward = m.calcSpeedFactorPos();
            s_pos = this.calcSpeedFactorPos();
            dx += s_pos.getX() - forward.getX();
            dy += s_pos.getY() - forward.getY();
            dz += s_pos.getZ() - forward.getZ();
            ++n;
         }

         dx /= (double)n;
         dy /= (double)n;
         dz /= (double)n;
      } else {
         dx = ((CommonMinecart)this.entity).getMovedX();
         dy = ((CommonMinecart)this.entity).getMovedY();
         dz = ((CommonMinecart)this.entity).getMovedZ();
      }

      double n = MathUtil.getNormalizationFactor(dx, dy, dz);
      if (Double.isInfinite(n) || n >= 1.0E10D) {
         dx = ((CommonMinecart)this.entity).vel.getX();
         dy = ((CommonMinecart)this.entity).vel.getY();
         dz = ((CommonMinecart)this.entity).vel.getZ();
         n = MathUtil.getNormalizationFactor(dx, dy, dz);
      }

      if (Double.isInfinite(n)) {
         forward = this.getOrientationForward();
         if (this.direction != null) {
            double dot = forward.getX() * (double)this.direction.getModX() + forward.getY() * (double)this.direction.getModY() + forward.getZ() * (double)this.direction.getModZ();
            if (dot < 0.0D) {
               forward.multiply(-1.0D);
            }
         }

         return forward;
      } else {
         return new Vector(dx * n, dy * n, dz * n);
      }
   }

   private Vector calcSpeedFactorPos() {
      return this.getWheels().getPosition();
   }

   public Quaternion getOrientation() {
      if (((CommonMinecart)this.entity).loc.getYaw() != this.cachedOrientation_yaw) {
         this.cachedOrientation_yaw = ((CommonMinecart)this.entity).loc.getYaw();
         this.cachedOrientation_quat = null;
      }

      if (((CommonMinecart)this.entity).loc.getPitch() != this.cachedOrientation_pitch) {
         this.cachedOrientation_pitch = ((CommonMinecart)this.entity).loc.getPitch();
         this.cachedOrientation_quat = null;
      }

      Quaternion orientation = this.cachedOrientation_quat;
      if (orientation == null) {
         this.cachedOrientation_quat = orientation = Quaternion.fromYawPitchRoll((double)this.cachedOrientation_pitch, (double)(this.cachedOrientation_yaw + 90.0F), 0.0D);
      }

      return orientation.clone();
   }

   public Vector getOrientationForward() {
      return this.getOrientation().forwardVector();
   }

   public void setOrientation(Quaternion orientation) {
      if (this.cachedOrientation_quat != null) {
         double dx = this.cachedOrientation_quat.getX() - orientation.getX();
         double dy = this.cachedOrientation_quat.getY() - orientation.getY();
         double dz = this.cachedOrientation_quat.getZ() - orientation.getZ();
         double dw = this.cachedOrientation_quat.getW() - orientation.getW();
         if (dx * dx + dy * dy + dz * dz + dw * dw < 1.0E-20D) {
            this.cachedOrientation_quat = orientation.clone();
            return;
         }
      }

      this.cachedOrientation_quat = orientation.clone();
      Vector ypr = this.cachedOrientation_quat.getYawPitchRoll();
      ((CommonMinecart)this.entity).setRotation((float)ypr.getY() - 90.0F, (float)ypr.getX());
      this.cachedOrientation_yaw = ((CommonMinecart)this.entity).loc.getYaw();
      this.cachedOrientation_pitch = ((CommonMinecart)this.entity).loc.getPitch();
   }

   public void flipOrientation() {
      Quaternion orientation = this.getOrientation();
      orientation.rotateYFlip();
      this.setOrientation(orientation);
      this.getWheels().startTeleport();
      this.getWheels().update();
      this.getAttachments().syncRespawn();
   }

   public MinecartMember<?> getNeighbour(int offset) {
      int index = this.getIndex();
      if (index == -1) {
         return null;
      } else {
         index += offset;
         return this.getGroup().containsIndex(index) ? (MinecartMember)this.getGroup().get(index) : null;
      }
   }

   public MinecartMember<?>[] getNeightbours() {
      if (this.getGroup() == null) {
         return new MinecartMember[0];
      } else {
         int index = this.getIndex();
         if (index == -1) {
            return new MinecartMember[0];
         } else if (index > 0) {
            return index < this.getGroup().size() - 1 ? new MinecartMember[]{(MinecartMember)this.getGroup().get(index - 1), (MinecartMember)this.getGroup().get(index + 1)} : new MinecartMember[]{(MinecartMember)this.getGroup().get(index - 1)};
         } else {
            return index < this.getGroup().size() - 1 ? new MinecartMember[]{(MinecartMember)this.getGroup().get(index + 1)} : new MinecartMember[0];
         }
      }
   }

   public SignTrackerMember getSignTracker() {
      return this.signTracker;
   }

   public WheelTrackerMember getWheels() {
      return this.wheelTracker;
   }

   public AttachmentControllerMember getAttachments() {
      return this.attachmentController;
   }

   public void setUnloaded(boolean unloaded) {
      if (this.unloaded != unloaded) {
         this.unloaded = unloaded;
         if (unloaded && this.group != null) {
            this.unloadedLastPlayerTakable = this.group.getProperties().isPlayerTakeable();
         }
      }

   }

   public boolean isUnloaded() {
      return this.unloaded || this.entity == null;
   }

   public boolean isInteractable() {
      return this.entity != null && !((CommonMinecart)this.entity).isRemoved() && !this.isUnloaded();
   }

   public double calcSubBlockDistance() {
      double distance = 0.0D;
      IntVector3 blockPos = ((CommonMinecart)this.entity).loc.block();
      distance += (double)this.direction.getModX() * (((CommonMinecart)this.entity).loc.getX() - blockPos.midX());
      distance += (double)this.direction.getModY() * (((CommonMinecart)this.entity).loc.getY() - blockPos.midY());
      distance += (double)this.direction.getModZ() * (((CommonMinecart)this.entity).loc.getZ() - blockPos.midZ());
      if (FaceUtil.isSubCardinal(this.direction)) {
         distance /= 2.0D;
      }

      return distance;
   }

   public boolean canTakeDamage(Entity passenger, DamageCause cause) {
      if (this.getGroup().isTeleportImmune()) {
         return false;
      } else {
         return cause != DamageCause.SUFFOCATION || this.isPassengerSuffocating(passenger);
      }
   }

   public boolean isPassengerSuffocating(Entity passenger) {
      if (!this.isUnloaded() && this.getGroup().getProperties().hasSuffocation()) {
         Location position = this.getPassengerLocation(passenger);
         position.setY(position.getY() + 1.0D);
         Block block = position.getBlock();
         return BlockUtil.isSuffocating(block);
      } else {
         return false;
      }
   }

   public Location getPassengerEjectLocation(Entity passenger) {
      CartAttachmentSeat seat = this.getAttachments().findSeat(passenger);
      if (seat != null && seat.isAttached()) {
         return seat.getEjectPosition(passenger);
      } else {
         Location mloc = ((CommonMinecart)this.entity).getLocation();
         mloc.setYaw((float)FaceUtil.faceToYaw(this.getDirection()));
         mloc.setPitch(0.0F);
         return MathUtil.move(mloc, this.getProperties().getExitOffset().getPosition());
      }
   }

   public Location getPassengerLocation(Entity passenger) {
      CartAttachmentSeat seat = this.getAttachments().findSeat(passenger);
      if (seat == null) {
         Location mloc = ((CommonMinecart)this.entity).getLocation();
         mloc.setYaw((float)FaceUtil.faceToYaw(this.getDirection()));
         mloc.setPitch(0.0F);
         return mloc;
      } else {
         return seat.getPosition(passenger);
      }
   }

   public boolean isInChunk(Chunk chunk) {
      return this.isInChunk(chunk.getWorld(), chunk.getX(), chunk.getZ());
   }

   public boolean isInChunk(World world, int cx, int cz) {
      return this.entity != null && world == ((CommonMinecart)this.entity).getWorld() && Math.abs(cx - ((CommonMinecart)this.entity).getChunkX()) <= 2 && Math.abs(cz - ((CommonMinecart)this.entity).getChunkZ()) <= 2;
   }

   public boolean isSingle() {
      return this.group == null || this.group.size() == 1;
   }

   /** @deprecated */
   @Deprecated
   public boolean isYawInverted() {
      return this.isOrientationInverted();
   }

   public Block getBlock(int dx, int dy, int dz) {
      IntVector3 pos = this.getBlockPos();
      return ((CommonMinecart)this.entity).getWorld().getBlockAt(pos.x + dx, pos.y + dy, pos.z + dz);
   }

   public Block getBlock(BlockFace face) {
      return this.getBlock(face.getModX(), face.getModY(), face.getModZ());
   }

   public Block getBlockRelative(int notchOffset) {
      return this.getBlock(FaceUtil.notchFaceOffset(this.direction, notchOffset));
   }

   public Block getGroundBlock() {
      return this.getBlock(0, -1, 0);
   }

   public double getForce() {
      return ((CommonMinecart)this.entity).vel.length();
   }

   public double getRealSpeed() {
      return this.group != null ? ((CommonMinecart)this.entity).vel.length() / this.group.getUpdateSpeedFactor() : ((CommonMinecart)this.entity).vel.length();
   }

   public double getRealSpeedLimited() {
      if (this.group != null) {
         double baseSpeed = Math.min(((CommonMinecart)this.entity).vel.length(), ((CommonMinecart)this.entity).getMaxSpeed()) / this.group.getUpdateSpeedFactor();
         return Math.min(baseSpeed, this.group.getObstacleTracker().getSpeedLimit());
      } else {
         return Math.min(((CommonMinecart)this.entity).vel.length(), ((CommonMinecart)this.entity).getMaxSpeed());
      }
   }

   public double getForwardForce() {
      return this.getRailLogic().getForwardVelocity(this);
   }

   public void setForwardForce(double force) {
      this.getRailLogic().setForwardVelocity(this, force);
   }

   public void limitSpeed() {
      double currvel = this.getForce();
      if (currvel > ((CommonMinecart)this.entity).getMaxSpeed() && currvel > 0.01D) {
         ((CommonMinecart)this.entity).vel.xz.multiply(((CommonMinecart)this.entity).getMaxSpeed() / currvel);
      }

   }

   public Vector getLimitedVelocity() {
      double max;
      if (this.isUnloaded()) {
         max = ((CommonMinecart)this.entity).getMaxSpeed();
      } else {
         max = this.getGroup().getProperties().getSpeedLimit();
      }

      return new Vector(((CommonMinecart)this.entity).vel.x.getClamped(max), ((CommonMinecart)this.entity).vel.y.getClamped(max), ((CommonMinecart)this.entity).vel.z.getClamped(max));
   }

   public TrackMap makeTrackMap(int size) {
      return new TrackMap(this.getBlock(), this.direction, size);
   }

   public boolean isCollisionIgnored(Entity entity) {
      MinecartMember<?> member = MinecartMemberStore.getFromEntity(entity);
      if (member != null) {
         return this.isCollisionIgnored(member);
      } else {
         return this.ignoreAllCollisions || this.collisionIgnoreTimes.containsKey(entity.getUniqueId());
      }
   }

   public boolean isCollisionIgnored(MinecartMember<?> member) {
      return this.ignoreAllCollisions || member.ignoreAllCollisions || this.collisionIgnoreTimes.containsKey(((CommonMinecart)member.entity).getUniqueId()) || member.collisionIgnoreTimes.containsKey(((CommonMinecart)this.entity).getUniqueId());
   }

   public void ignoreCollision(Entity entity, int ticktime) {
      this.collisionIgnoreTimes.put(entity.getUniqueId(), new AtomicInteger(ticktime));
   }

   public boolean canCollisionEnter() {
      return this.collisionEnterTimer == 0;
   }

   public void resetCollisionEnter() {
      this.collisionEnterTimer = TCConfig.collisionReEnterDelay;
   }

   public void pushSideways(Entity entity) {
      this.pushSideways(entity, TCConfig.pushAwayForce);
   }

   public void pushSideways(Entity entity, double force) {
      force = Math.min(1.0D, force);
      float yaw = (float)FaceUtil.faceToYaw(this.direction);
      float lookat = MathUtil.getLookAtYaw(((CommonMinecart)this.entity).getEntity(), entity) - yaw;
      lookat = MathUtil.wrapAngle(lookat);
      if (lookat > 0.0F) {
         yaw -= 180.0F;
      }

      Vector vel = MathUtil.getDirection(yaw, 0.0F).multiply(force);
      entity.setVelocity(vel);
   }

   public void push(Entity entity, double force) {
      force = Math.min(1.0D, force);
      Vector offset = ((CommonMinecart)this.entity).loc.offsetTo(entity);
      MathUtil.setVectorLength(offset, force);
      entity.setVelocity(entity.getVelocity().add(offset));
   }

   public void playLinkEffect() {
      this.playLinkEffect(true);
   }

   public void playLinkEffect(boolean showSmoke) {
      Location loc = ((CommonMinecart)this.entity).getLocation();
      if (showSmoke) {
         loc.getWorld().playEffect(loc, Effect.SMOKE, 0);
      }

      WorldUtil.playSound(loc, SoundEffect.EXTINGUISH, 1.0F, 2.0F);
   }

   public void checkMissing() throws MemberMissingException {
      if (this.entity == null) {
         throw new MemberMissingException();
      } else if (((CommonMinecart)this.entity).isRemoved()) {
         this.onDie(true);
         throw new MemberMissingException();
      } else if (this.isUnloaded()) {
         throw new MemberMissingException();
      }
   }

   public ActionTrackerMember getActions() {
      return this.actionTracker;
   }

   public RailTrackerMember getRailTracker() {
      return this.railTrackerMember;
   }

   public IntVector3 getBlockPos() {
      return this.getRailTracker().getBlockPos();
   }

   public Block getLastBlock() {
      return this.getRailTracker().getLastBlock();
   }

   public Block getBlock() {
      return this.getRailTracker().getBlock();
   }

   private final Vector calcMotionVector(boolean ignoreVelocity) {
      Vector motionVector = ((CommonMinecart)this.entity).getVelocity();
      double motionLengthSq = motionVector.lengthSquared();
      if (Double.isNaN(motionLengthSq)) {
         motionVector = new Vector();
         motionLengthSq = 0.0D;
      }

      if (ignoreVelocity || motionLengthSq <= 1.0E-20D) {
         if (!this.isDerailed() && this.direction != null) {
            motionVector = FaceUtil.faceToVector(this.direction);
         } else if (!this.isSingle()) {
            Vector alterMotionVector = motionVector;
            MinecartMember<?> next = this.getNeighbour(-1);
            if (next != null) {
               alterMotionVector = ((CommonMinecart)this.getEntity()).last.offsetTo(((CommonMinecart)next.getEntity()).last);
            } else {
               MinecartMember<?> prev = this.getNeighbour(1);
               if (prev != null) {
                  alterMotionVector = ((CommonMinecart)prev.getEntity()).last.offsetTo(((CommonMinecart)this.getEntity()).last);
               }
            }

            if (!Double.isNaN(alterMotionVector.lengthSquared())) {
               motionVector = alterMotionVector;
            }
         }
      }

      if (Double.isNaN(motionVector.getX())) {
         throw new IllegalStateException("Motion vector is NaN");
      } else {
         return motionVector;
      }
   }

   private final boolean fillRailInformation(RailState state) {
      state.setRailPiece(RailPiece.createWorldPlaceholder(this.railLookup()));
      state.setMember(this);
      state.position().setMotion(this.calcMotionVector(false));
      state.position().setLocation(((CommonMinecart)this.entity).getLocation());
      return RailType.loadRailInformation(state);
   }

   public WorldRailLookup railLookup() {
      WorldRailLookup result = this.railLookup;
      if (!result.isValidForWorld(((CommonMinecart)this.entity).getWorld())) {
         result = this.railLookup = RailLookup.forWorld(((CommonMinecart)this.entity).getWorld());
      }

      return result;
   }

   public RailState discoverRail() {
      RailState state = new RailState();
      state.setMember(this);
      boolean result = this.fillRailInformation(state);
      if (!result) {
         state.setRailPiece(RailPiece.create(RailType.NONE, state.railBlock(), state.railLookup()));
         state.position().setLocation(((CommonMinecart)this.entity).getLocation());
         state.setMotionVector(this.calcMotionVector(true));
         state.initEnterDirection();
      }

      state.position().normalizeMotion();
      if (state.railType() != RailType.NONE) {
         RailLogic logic = state.loadRailLogic();
         RailPath path = logic.getPath();
         if (!path.isEmpty()) {
            path.snap(state.position(), state.railBlock());
         }
      }

      return state;
   }

   public void snapToPath(RailPath path) {
      if (!path.isEmpty()) {
         RailPath.Position pos = RailPath.Position.fromPosDir(((CommonMinecart)this.entity).loc.vector(), ((CommonMinecart)this.entity).getVelocity());
         path.move(pos, this.getBlock(), 0.0D);
         this.snapToPosition(pos);
      }

   }

   public void snapToPosition(RailPath.Position position) {
      this.snapToPosition(position, false);
   }

   private void snapToPosition(RailPath.Position position, boolean invertedMotion) {
      position.assertAbsolute();
      double velocity = ((CommonMinecart)this.entity).vel.length();
      if (invertedMotion) {
         velocity = -velocity;
      }

      ((CommonMinecart)this.entity).setPosition(position.posX, position.posY, position.posZ);
      ((CommonMinecart)this.entity).vel.set(position.motX * velocity, position.motY * velocity, position.motZ * velocity);
   }

   public boolean isMoving() {
      return ((CommonMinecart)this.entity).isMoving();
   }

   public boolean isTurned() {
      return FaceUtil.isSubCardinal(this.direction);
   }

   public boolean isDerailed() {
      return this.getRailType() == RailType.NONE;
   }

   public Location getFirstKnownDerailedPosition() {
      return this.firstKnownDerailedPosition;
   }

   public boolean isOnVertical() {
      return this.getRailLogic() instanceof RailLogicVertical;
   }

   public RailLogic getLastRailLogic() {
      return this.getRailTracker().getLastLogic();
   }

   public RailLogic getRailLogic() {
      return this.getRailTracker().getRailLogic();
   }

   public RailType getRailType() {
      return this.getRailTracker().getRailType();
   }

   public boolean hasBlockChanged() {
      return this.getRailTracker().hasBlockChanged();
   }

   public boolean isOnSlope() {
      return this.getRailLogic().isSloped();
   }

   public boolean isFlying() {
      return this.isDerailed() && !((CommonMinecart)this.entity).isOnGround();
   }

   public boolean isMovingHorizontally() {
      return ((CommonMinecart)this.entity).isMovingHorizontally();
   }

   public boolean isMovingVerticalOnly() {
      return this.isMovingVertically() && !this.isMovingHorizontally();
   }

   public boolean isMovingVertically() {
      if (((CommonMinecart)this.entity).isOnGround()) {
         return ((CommonMinecart)this.entity).vel.getY() > 0.001D;
      } else {
         return this.isDerailed() || ((CommonMinecart)this.entity).isMovingVertically();
      }
   }

   public boolean isNearOf(MinecartMember<?> member) {
      double max = this.getMaximumDistance(member);
      return ((CommonMinecart)this.entity).loc.distanceSquared(member.entity) <= max * max;
   }

   public boolean isHeadingTo(Entity entity) {
      return this.isHeadingTo(entity.getLocation());
   }

   public boolean isHeadingTo(Vector movement) {
      return MathUtil.isHeadingTo(movement, ((CommonMinecart)this.entity).getVelocity());
   }

   public boolean isHeadingTo(IntVector3 location) {
      return MathUtil.isHeadingTo(((CommonMinecart)this.entity).loc.offsetTo((double)location.x, (double)location.y, (double)location.z), ((CommonMinecart)this.entity).getVelocity());
   }

   public boolean isHeadingTo(Location target) {
      return MathUtil.isHeadingTo(((CommonMinecart)this.entity).getLocation(), target, ((CommonMinecart)this.entity).getVelocity());
   }

   public boolean isHeadingTo(BlockFace direction) {
      return MathUtil.isHeadingTo(direction, ((CommonMinecart)this.entity).getVelocity());
   }

   public boolean isFollowingOnTrack(MinecartMember<?> member) {
      if (!this.isNearOf(member)) {
         return false;
      } else if (!this.isDerailed() && !member.isDerailed()) {
         Block memberrail = member.getBlock();
         if (BlockUtil.equals(this.getBlock(), memberrail)) {
            return true;
         } else {
            if (this.isMoving()) {
               if (TrackIterator.canReach(this.getBlock(), this.getDirectionTo(), memberrail)) {
                  return true;
               }

               if (TrackIterator.isConnected(this.getBlock(), memberrail, true)) {
                  return true;
               }
            } else if (TrackIterator.isConnected(this.getBlock(), memberrail, false)) {
               return true;
            }

            return false;
         }
      } else {
         return true;
      }
   }

   public boolean isDirectionTo(BlockFace direction) {
      return this.directionTo == direction || this.direction == direction;
   }

   public BlockFace getDirection() {
      return this.direction;
   }

   public BlockFace getDirectionFrom() {
      if (this.directionFrom == null) {
         this.directionFrom = this.directionTo;
      }

      return this.directionFrom;
   }

   public BlockFace getDirectionTo() {
      return this.directionTo;
   }

   /** @deprecated */
   @Deprecated
   public void setDirectionForward() {
      this.setDirectionForward(false);
   }

   public void setDirectionForward(boolean flipped) {
      this.directionFrom = this.directionTo = null;
      this.direction = Util.vecToFace(this.getOrientationForward(), true);
      if (flipped) {
         this.direction = this.direction.getOppositeFace();
      }

   }

   void reverseDirection() {
      ((CommonMinecart)this.entity).vel.multiply(-1.0D);
      if (this.direction != null) {
         this.direction = this.direction.getOppositeFace();
      }

   }

   public int getDirectionDifference(BlockFace dircomparer) {
      return FaceUtil.getFaceYawDifference(this.getDirection(), dircomparer);
   }

   public int getDirectionDifference(MinecartMember<?> comparer) {
      return this.getDirectionDifference(comparer.getDirection());
   }

   public void updateDirection() {
      RailTrackerMember tracker = this.getRailTracker();
      RailState state = tracker.getState();
      if (this.direction != null && !(((CommonMinecart)this.entity).vel.lengthSquared() > 1.0E-10D) && !(state.position().motDot(this.direction) >= 0.0D)) {
         this.direction = state.position().getMotionFaceWithSubCardinal().getOppositeFace();
      } else {
         this.direction = state.position().getMotionFaceWithSubCardinal();
      }

      RailState state_inv = state.clone();
      state_inv.position().invertMotion();
      state_inv.initEnterDirection();
      this.directionTo = state_inv.enterFace().getOppositeFace();
   }

   public boolean onDamage(DamageSource damagesource, double damage) {
      if (((CommonMinecart)this.entity).isRemoved()) {
         return false;
      } else if (damagesource.toString().equals("fireworks")) {
         return false;
      } else {
         Entity damager = damagesource.getEntity();
         boolean executePostLogic = true;
         boolean isInstantlyDestroyed;
         if (damager instanceof HumanEntity) {
            ItemStack itemInMainHand = HumanHand.getItemInMainHand((HumanEntity)damager);
            isInstantlyDestroyed = false;
            if (itemInMainHand != null && itemInMainHand.hasItemMeta() && itemInMainHand.getItemMeta().hasEnchant(Enchantment.KNOCKBACK)) {
               isInstantlyDestroyed = true;
            } else if (damager instanceof Player && ((Player)damager).isSprinting()) {
               isInstantlyDestroyed = true;
            }

            if (isInstantlyDestroyed) {
               if (this.isUnloaded()) {
                  executePostLogic = false;
               } else if (!this.getGroup().getProperties().getCollisionMode(damager).permitsKnockback()) {
                  executePostLogic = false;
               }
            }
         }

         try {
            VehicleDamageEvent event = new VehicleDamageEvent((Vehicle)((CommonMinecart)this.entity).getEntity(), damager, damage);
            if (((VehicleDamageEvent)CommonUtil.callEvent(event)).isCancelled()) {
               return executePostLogic;
            }

            damage = event.getDamage();
            ((CommonMinecart)this.entity).setShakingDirection(-((CommonMinecart)this.entity).getShakingDirection());
            ((CommonMinecart)this.entity).setShakingFactor(10);
            ((CommonMinecart)this.entity).setVelocityChanged(true);
            ((CommonMinecart)this.entity).setDamage(((CommonMinecart)this.entity).getDamage() + damage * 10.0D);
            isInstantlyDestroyed = Util.canInstantlyBreakMinecart(damager);
            if (isInstantlyDestroyed) {
               ((CommonMinecart)this.entity).setDamage(100.0D);
            }

            if (((CommonMinecart)this.entity).getDamage() > 40.0D) {
               List<ItemStack> drops = new ArrayList(2);
               if (!isInstantlyDestroyed && this.getProperties().getSpawnItemDrops()) {
                  if (TCConfig.breakCombinedCarts) {
                     drops.addAll(((CommonMinecart)this.entity).getBrokenDrops());
                  } else {
                     drops.add(new ItemStack(((CommonMinecart)this.entity).getCombinedItem()));
                  }
               }

               VehicleDestroyEvent destroyEvent = new VehicleDestroyEvent((Vehicle)((CommonMinecart)this.entity).getEntity(), damager);
               if (((VehicleDestroyEvent)CommonUtil.callEvent(destroyEvent)).isCancelled()) {
                  ((CommonMinecart)this.entity).setDamage(40.0D);
                  return executePostLogic;
               }

               Iterator var10 = drops.iterator();

               while(var10.hasNext()) {
                  ItemStack stack = (ItemStack)var10.next();
                  ((CommonMinecart)this.entity).spawnItemDrop(stack, 0.0F);
               }

               this.onDie(true);
            } else if (damager instanceof Player) {
               this.traincarts.getPlayer((Player)damager).editMember(this);
            }
         } catch (Throwable var12) {
            this.traincarts.handle(var12);
         }

         return executePostLogic;
      }
   }

   public void onDie(boolean killed) {
      try {
         if (!((CommonMinecart)this.entity).isRemoved() || !this.died) {
            boolean cancelDrops = false;
            if (!this.isUnloaded()) {
               cancelDrops = !this.getProperties().getSpawnItemDrops();
               if (((CommonMinecart)this.entity).hasPassenger()) {
                  this.eject();
               }

               if (this.group != null) {
                  this.getSignTracker().clear();
               }

               if (((CommonMinecart)this.entity).hasPassenger()) {
                  Iterator var3 = ((CommonMinecart)this.entity).getPassengers().iterator();

                  while(var3.hasNext()) {
                     Entity passenger = (Entity)var3.next();
                     ((CommonMinecart)this.entity).removePassenger(passenger);
                  }
               }

               if (this.group != null) {
                  this.group.remove(this);
               }

               CartPropertiesStore.remove(((CommonMinecart)this.entity).getUniqueId());
            }

            boolean cancelDropsOriginal = TCListener.cancelNextDrops;
            TCListener.cancelNextDrops |= cancelDrops;

            try {
               super.onDie(killed);
            } finally {
               TCListener.cancelNextDrops = cancelDropsOriginal;
            }

            this.died = true;
         }
      } catch (Throwable var9) {
         this.traincarts.handle(var9);
      }

   }

   public boolean onEntityCollision(Entity e) {
      MinecartMember<?> vehicleTrain = MinecartMemberStore.getFromEntity(((CommonMinecart)this.entity).getVehicle());
      if (vehicleTrain != null && vehicleTrain.group == this.group) {
         return false;
      } else if (!this.isInteractable()) {
         return false;
      } else {
         CollisionMode mode = this.getGroup().getProperties().getCollisionMode(e);
         if (mode == CollisionMode.CANCEL) {
            return false;
         } else if (!this.isModelIntersectingWith(e)) {
            return false;
         } else if (!mode.execute(this, e)) {
            return false;
         } else {
            if (this.isHeadingTo(e)) {
               if (this.entity instanceof Minecart) {
                  return false;
               }

               this.getGroup().stop();
            }

            return true;
         }
      }
   }

   public void onEntityBump(Entity e) {
      VehicleEntityCollisionEvent collisionEvent = new VehicleEntityCollisionEvent((Vehicle)((CommonMinecart)this.entity).getEntity(), e);
      if (!((VehicleEntityCollisionEvent)CommonUtil.callEvent(collisionEvent)).isCancelled()) {
         Vector pos_diff;
         double len_sq;
         double n;
         if (e instanceof Minecart) {
            pos_diff = e.getLocation().subtract(((CommonMinecart)this.entity).getLocation()).toVector();
            len_sq = pos_diff.lengthSquared();
            if (len_sq >= 1.0E-4D) {
               n = MathUtil.getNormalizationFactorLS(len_sq);
               pos_diff.multiply(n);
               if (n > 1.0D) {
                  n = 1.0D;
               }

               pos_diff.multiply(0.05D * n);
               applyBump(e, pos_diff);
               applyBump(((CommonMinecart)this.entity).getEntity(), pos_diff.multiply(-1.0D));
            }

         } else {
            pos_diff = e.getLocation().subtract(((CommonMinecart)this.entity).getLocation()).toVector();
            if (this.isDerailed()) {
               pos_diff.setY(0.0D);
            }

            len_sq = pos_diff.lengthSquared();
            if (len_sq >= 1.0E-4D) {
               n = MathUtil.getNormalizationFactorLS(len_sq);
               pos_diff.multiply(n);
               if (n > 1.0D) {
                  n = 1.0D;
               }

               pos_diff.multiply(0.05D * n);
               applyBump(e, pos_diff.clone().multiply(0.25D));
               pos_diff.multiply(-1.0D);
               if (!this.isDerailed()) {
                  Vector railMot = this.getRailTracker().getRail().state.motionVector().normalize();
                  if (railMot.dot(pos_diff) < 0.0D) {
                     railMot.multiply(-1.0D);
                  }

                  pos_diff = railMot.multiply(pos_diff.multiply(railMot).length());
               }

               applyBump(((CommonMinecart)this.entity).getEntity(), pos_diff);
            }

         }
      }
   }

   private static void applyBump(Entity entity, Vector v) {
      EntityHandle eh = EntityHandle.fromBukkit(entity);
      eh.setMot(eh.getMotX() + v.getX(), eh.getMotY() + v.getY(), eh.getMotZ() + v.getZ());
      eh.setPositionChanged(true);
   }

   public boolean onBlockCollision(Block hitBlock, BlockFace hitFace) {
      Vector upVector = this.getOrientation().upVector();
      if (upVector.getY() >= -0.1D && upVector.getY() <= 0.1D) {
         double closest_dx = ((CommonMinecart)this.entity).loc.getX() - (double)hitBlock.getX();
         double closest_dz = ((CommonMinecart)this.entity).loc.getZ() - (double)hitBlock.getZ();
         double MIN_COORD = 1.0E-10D;
         double MAX_COORD = 0.9999999999D;
         if (!(closest_dx >= 1.0E-10D) || !(closest_dx <= 0.9999999999D) || !(closest_dz >= 1.0E-10D) || !(closest_dz <= 0.9999999999D)) {
            if (upVector.getX() >= -0.1D && upVector.getX() <= 0.1D) {
               if (-closest_dz < -0.5D) {
                  --closest_dz;
               }

               if (upVector.getZ() > 0.0D && -closest_dz < -0.01D) {
                  return false;
               }

               if (upVector.getZ() < 0.0D && -closest_dz > 0.01D) {
                  return false;
               }
            } else if (upVector.getZ() >= -0.1D && upVector.getZ() <= 0.1D) {
               if (-closest_dx < -0.5D) {
                  --closest_dx;
               }

               if (upVector.getX() > 0.0D && -closest_dx < -0.01D) {
                  return false;
               }

               if (upVector.getX() < 0.0D && -closest_dx > 0.01D) {
                  return false;
               }
            }
         }
      }

      if (!RailType.getType(hitBlock).onCollide(this, hitBlock, hitFace)) {
         return false;
      } else if (!this.getRailType().onBlockCollision(this, this.getBlock(), hitBlock, hitFace)) {
         return false;
      } else {
         if (this.getRailType().isHeadOnCollision(this, this.getBlock(), hitBlock)) {
            this.getGroup().stop();
         }

         return true;
      }
   }

   public void onPositionPassenger(Entity passenger, EntityPositionApplier applier) {
      CartAttachmentSeat seat = this.attachmentController.findSeat(passenger);
      if (seat == null) {
         super.onPositionPassenger(passenger, applier);
      } else {
         applier.setPosition(seat.getTransform().toVector());
      }
   }

   public boolean isModelIntersectingWith(Entity entity) {
      MinecartMember<?> other = MinecartMemberStore.getFromEntity(entity);
      if (other == null) {
         return this.isModelIntersectingWith_impl(entity);
      } else {
         return this.isModelIntersectingWith_impl(((CommonMinecart)other.entity).getWrappedHandle()) && other.isModelIntersectingWith_impl(((CommonMinecart)this.entity).getWrappedHandle());
      }
   }

   private final boolean isModelIntersectingWith_impl(Entity entity) {
      return this.isModelIntersectingWith_impl(EntityHandle.fromBukkit(entity));
   }

   private final boolean isModelIntersectingWith_impl(EntityHandle entityHandle) {
      AxisAlignedBBHandle aabb = entityHandle.getBoundingBox();
      double[] xval = new double[]{aabb.getMinX(), 0.5D * (aabb.getMinX() + aabb.getMaxX()), aabb.getMaxX()};
      double[] yval = new double[]{aabb.getMinY(), 0.5D * (aabb.getMinY() + aabb.getMaxY()), aabb.getMaxY()};
      double[] zval = new double[]{aabb.getMinZ(), 0.5D * (aabb.getMinZ() + aabb.getMaxZ()), aabb.getMaxZ()};
      double[] var6 = xval;
      int var7 = xval.length;

      for(int var8 = 0; var8 < var7; ++var8) {
         double x = var6[var8];
         double[] var11 = yval;
         int var12 = yval.length;

         for(int var13 = 0; var13 < var12; ++var13) {
            double y = var11[var13];
            double[] var16 = zval;
            int var17 = zval.length;

            for(int var18 = 0; var18 < var17; ++var18) {
               double z = var16[var18];
               if (this.isModelIntersectingWith_pointTest(x, y, z)) {
                  return true;
               }
            }
         }
      }

      return false;
   }

   private final boolean isModelIntersectingWith_pointTest(double x, double y, double z) {
      return this.calculateModelDistance(new Vector(x, y, z)) <= 0.1D;
   }

   public double calculateModelDistance(Vector point) {
      point = point.clone().subtract(this.getWheels().getPosition());
      Quaternion invOri = this.getOrientation().clone();
      invOri.invert();
      invOri.transformPoint(point);
      double x_min = -0.5D;
      double x_max = 0.5D;
      double y_min = 0.0D;
      double y_max = 1.0D;
      double z_min = -0.5D * (double)((CommonMinecart)this.entity).getWidth();
      double z_max = 0.5D * (double)((CommonMinecart)this.entity).getWidth();
      double dx = Math.max(0.0D, Math.max(x_min - point.getX(), point.getX() - x_max));
      double dy = Math.max(0.0D, Math.max(y_min - point.getY(), point.getY() - y_max));
      double dz = Math.max(0.0D, Math.max(z_min - point.getZ(), point.getZ() - z_max));
      return Math.sqrt(dx * dx + dy * dy + dz * dz);
   }

   public double calculateRailDistanceToMemberAhead(MinecartMember<?> memberAhead) {
      if (!this.isDerailed() && !memberAhead.isDerailed()) {
         RailTracker.TrackedRailWalker walker = this.getRailTracker().getTrackedRailWalker();
         RailPath.Position targetPos = memberAhead.getRailTracker().getRail().state.position();
         double distanceRemaining = walker.position().distance(targetPos);
         double distanceLimit = 5.0D * distanceRemaining;
         double distanceMoved = 0.0D;
         double moved;
         if (this.getGroup() == memberAhead.group) {
            while((moved = walker.move(distanceRemaining)) > 0.0D) {
               distanceMoved += moved;
               distanceRemaining = walker.position().distance(targetPos);
               if (distanceRemaining < 1.0E-10D) {
                  return distanceMoved;
               }

               if (distanceMoved > distanceLimit) {
                  return Double.NaN;
               }
            }
         }

         walker.state().initEnterDirection();
         TrackWalkingPoint p = new TrackWalkingPoint(walker.state());
         p.skipFirst();
         p.movedTotal = distanceMoved;
         int zeroMoveLimit = 100;

         while(p.move(distanceRemaining)) {
            distanceRemaining = p.state.position().distance(targetPos);
            if (distanceRemaining < 1.0E-10D) {
               return p.movedTotal;
            }

            if (p.movedTotal > distanceLimit) {
               break;
            }

            if (p.moved < 1.0E-10D) {
               --zeroMoveLimit;
               if (zeroMoveLimit == 0) {
                  break;
               }
            }
         }

         return Double.NaN;
      } else {
         return Double.NaN;
      }
   }

   public Inventory getPlayerInventory() {
      Inventory[] source = (Inventory[])((CommonMinecart)this.getEntity()).getPlayerPassengers().stream().map(HumanEntity::getInventory).toArray((x$0) -> {
         return new Inventory[x$0];
      });
      return new MergedInventory(source);
   }

   public void eject() {
      ((CommonMinecart)this.getEntity()).eject();
      this.resetCollisionEnter();
   }

   public void eject(Vector offset) {
      this.eject(new Location(((CommonMinecart)this.entity).getWorld(), ((CommonMinecart)this.entity).loc.getX() + offset.getX(), ((CommonMinecart)this.entity).loc.getY() + offset.getY(), ((CommonMinecart)this.entity).loc.getZ() + offset.getZ(), 0.0F, 0.0F), true);
   }

   public void eject(Vector offset, float yaw, float pitch) {
      this.eject(new Location(((CommonMinecart)this.entity).getWorld(), ((CommonMinecart)this.entity).loc.getX() + offset.getX(), ((CommonMinecart)this.entity).loc.getY() + offset.getY(), ((CommonMinecart)this.entity).loc.getZ() + offset.getZ(), yaw, pitch));
   }

   public void eject(Location to) {
      this.eject(to, false);
   }

   public void eject(Location to, boolean retainEntityRotation) {
      if (((CommonMinecart)this.entity).hasPassenger()) {
         List<Entity> oldPassengers = new ArrayList(((CommonMinecart)this.entity).getPassengers());
         TCSeatChangeListener.exemptFromEjectOffset.addAll(oldPassengers);
         this.eject();
         if (!oldPassengers.isEmpty()) {
            CommonUtil.nextTick(() -> {
               Iterator var3 = oldPassengers.iterator();

               while(var3.hasNext()) {
                  Entity oldPassenger = (Entity)var3.next();
                  if (retainEntityRotation) {
                     Util.teleportPosition(oldPassenger, to);
                  } else {
                     EntityUtil.teleport(oldPassenger, to);
                  }
               }

            });
         }

         Iterator var4 = oldPassengers.iterator();

         while(var4.hasNext()) {
            Entity oldPassenger = (Entity)var4.next();
            EntityUtil.teleportNextTick(oldPassenger, to);
         }

         TCSeatChangeListener.exemptFromEjectOffset.removeAll(oldPassengers);
      }

   }

   public boolean addPassengerForced(Entity passenger) {
      boolean var2;
      try {
         this.enterForced.add(passenger);
         var2 = ((CommonMinecart)this.entity).addPassenger(passenger);
      } finally {
         this.enterForced.remove(passenger);
      }

      return var2;
   }

   public boolean isPassengerEnterForced(Entity entity) {
      return this.enterForced.contains(entity);
   }

   public boolean connect(MinecartMember<?> with) {
      return this.getGroup().connect(this, with);
   }

   public void onPropertiesChanged() {
      this.getSignTracker().update();
      if (this.group != null) {
         CollisionOptions collision = this.group.getProperties().getCollision();
         this.setEntityCollisionEnabled(collision.collidesWithEntities());
         this.setBlockCollisionEnabled(collision.blockMode() == CollisionMode.DEFAULT);
      }

   }

   public boolean onModelChanged(AttachmentModel model) {
      if (this.entity == null) {
         return false;
      } else {
         ((CommonMinecart)this.entity).setSize(model.getCartLength(), 0.7F);
         this.wheelTracker.back().setDistance(0.5D * model.getWheelDistance() - model.getWheelCenter());
         this.wheelTracker.front().setDistance(0.5D * model.getWheelDistance() + model.getWheelCenter());
         double halfLength = 0.5D * (double)model.getCartLength();
         if (this.wheelTracker.back().getDistance() < 0.0D) {
            this.wheelTracker.back().setDistance(0.0D);
         } else if (this.wheelTracker.back().getDistance() > halfLength) {
            this.wheelTracker.back().setDistance(halfLength);
         }

         if (this.wheelTracker.front().getDistance() < 0.0D) {
            this.wheelTracker.front().setDistance(0.0D);
         } else if (this.wheelTracker.front().getDistance() > halfLength) {
            this.wheelTracker.front().setDistance(halfLength);
         }

         return true;
      }
   }

   public boolean isMovementControlled() {
      return this.getActions().isMovementControlled() || this.getGroup().getActions().isMovementControlled();
   }

   public boolean isIgnoringCollisions() {
      return this.ignoreAllCollisions;
   }

   public void setIgnoreCollisions(boolean ignoreAll) {
      this.ignoreAllCollisions = ignoreAll;
   }

   public void stop() {
      this.stop(false);
   }

   public void stop(boolean cancelLocationChange) {
      ((CommonMinecart)this.entity).vel.setZero();
      if (cancelLocationChange) {
         ((CommonMinecart)this.entity).loc.set(((CommonMinecart)this.entity).last);
      }

   }

   protected void updateUnloaded() {
      this.setUnloaded(this.entity == null || ((CommonMinecart)this.entity).isRemoved() || this.traincarts.getOfflineGroups().containsMinecart(((CommonMinecart)this.entity).getUniqueId()));
      if (!this.unloaded && (this.group == null || this.group.canUnload())) {
         World world = ((CommonMinecart)this.entity).getWorld();
         int midX = ((CommonMinecart)this.entity).getChunkX();
         int midZ = ((CommonMinecart)this.entity).getChunkZ();

         for(int cx = -2; cx <= 2; ++cx) {
            for(int cz = -2; cz <= 2; ++cz) {
               if (!WorldUtil.isLoaded(world, cx + midX, cz + midZ)) {
                  this.setUnloaded(true);
                  return;
               }
            }
         }
      }

   }

   public void respawn() {
      ((CommonMinecart)this.entity).getNetworkController().syncRespawn();
   }

   public void onBlockChange(Block from, Block to) {
      if (BlockUtil.getManhattanDistance(from, to, true) > 3) {
         this.directionFrom = null;
      }

      if (!this.isDerailed() && this.getProperties().hasBlockBreakTypes()) {
         Block left = this.getBlockRelative(-2);
         Block right = this.getBlockRelative(2);
         if (this.getProperties().canBreak(left)) {
            WorldUtil.getBlockData(left).destroy(left, 20.0F);
         }

         if (this.getProperties().canBreak(right)) {
            WorldUtil.getBlockData(right).destroy(right, 20.0F);
         }
      }

   }

   public void onPhysicsStart() {
      Iterator times = this.collisionIgnoreTimes.values().iterator();

      while(times.hasNext()) {
         if (((AtomicInteger)times.next()).decrementAndGet() <= 0) {
            times.remove();
         }
      }

      if (this.collisionEnterTimer > 0) {
         --this.collisionEnterTimer;
      }

      ((CommonMinecart)this.entity).vel.fixNaN();
      ((CommonMinecart)this.entity).last.set(((CommonMinecart)this.entity).loc);
      if (this.lastLocation == null || this.lastLocation.getWorld() != ((CommonMinecart)this.entity).getWorld()) {
         this.lastLocation = ((CommonMinecart)this.entity).getLocation();
      }

      if (this.lastLocationSync == null) {
         this.lastLocationSync = this.lastLocation;
      }

   }

   public void updateManualMovement() {
      boolean player_manual = this.getGroup().getProperties().isManualMovementAllowed();
      boolean mob_manual = this.getGroup().getProperties().isMobManualMovementAllowed();
      if ((player_manual || mob_manual) && ((CommonMinecart)this.entity).vel.lengthSquared() < 0.01D && !this.isDerailed()) {
         Iterator var3 = ((CommonMinecart)this.entity).getPassengers().iterator();

         while(true) {
            Entity passenger;
            while(true) {
               do {
                  if (!var3.hasNext()) {
                     return;
                  }

                  passenger = (Entity)var3.next();
               } while(!(passenger instanceof LivingEntity));

               if (passenger instanceof Player) {
                  if (player_manual) {
                     break;
                  }
               } else {
                  if (!mob_manual) {
                     continue;
                  }
                  break;
               }
            }

            float forwardMovement = EntityLivingHandle.fromBukkit((LivingEntity)passenger).getForwardMovement();
            if (forwardMovement > 0.0F) {
               Vector direction = ((LivingEntity)passenger).getEyeLocation().getDirection();
               ((CommonMinecart)this.entity).vel.add(direction.getX() * TCConfig.manualMovementFactor, 0.0D, direction.getZ() * TCConfig.manualMovementFactor);
            }
         }
      }
   }

   public void onPhysicsPreMove() {
      this.getRailTracker().snapshotRailLogic();
      if (((CommonMinecart)this.entity).getShakingFactor() > 0) {
         ((CommonMinecart)this.entity).setShakingFactor(((CommonMinecart)this.entity).getShakingFactor() - 1);
      }

      if (((CommonMinecart)this.entity).getDamage() > 0.0D) {
         ((CommonMinecart)this.entity).setDamage(((CommonMinecart)this.entity).getDamage() - 1.0D);
      }

      this.dieIfOutsideWorldBorder();
      if (!this.isDerailed()) {
         ((CommonMinecart)this.entity).setFallDistance(0.0F);
      }

      this.railTrackerMember.getRailLogic().onPreMove(this);
      this.getRailTracker().updateLast();
      ((CommonMinecart)this.entity).setPosition(((CommonMinecart)this.entity).loc.getX(), ((CommonMinecart)this.entity).loc.getY(), ((CommonMinecart)this.entity).loc.getZ());
   }

   private void dieIfOutsideWorldBorder() {
      double limitSq = TCConfig.worldBorderKillDistance * TCConfig.worldBorderKillDistance;
      if (WorldUtil.getBlockBorder(this.getWorld()).distanceSquared(((CommonMinecart)this.entity).loc.vector()) > limitSq) {
         this.onDie(true);
         throw new MemberMissingException();
      }
   }

   public void doPostMoveLogic() {
   }

   public void onActivatorUpdate(boolean activated) {
   }

   public void onActivate() {
   }

   public void calculateSpeedFactor() {
      this.speedFactor.setX(0.0D).setY(0.0D).setZ(0.0D);
      MinecartGroup group = this.getGroup();
      if (group.size() != 1 && !group.getActions().isMovementControlled() && !this.getActions().isMovementControlled()) {
         MinecartMember<?> n1 = this.getNeighbour(-1);
         MinecartMember<?> n2 = this.getNeighbour(1);
         if (n1 != null) {
            this.speedFactor.add(this.calculateSpeedFactor(this, n1));
         }

         if (n2 != null) {
            this.speedFactor.add(this.calculateSpeedFactor(n2, this));
         }

         if (n1 != null && n2 != null) {
            this.speedFactor.multiply(0.5D);
         }
      }

   }

   public static double calculateGapAndDirection(MinecartMember<?> back, MinecartMember<?> front, Vector direction) {
      WheelTrackerMember.Wheel frontwheel = front.getWheels().movingBackwards();
      WheelTrackerMember.Wheel backwheel = back.getWheels().movingForwards();
      Vector frontpos = frontwheel.getAbsolutePosition();
      Vector backpos = backwheel.getAbsolutePosition();
      direction.setX(frontpos.getX() - backpos.getX());
      direction.setY(frontpos.getY() - backpos.getY());
      direction.setZ(frontpos.getZ() - backpos.getZ());
      double distance = direction.length();
      if (distance < 0.01D) {
         direction.setX(front.getDirection().getModX());
         direction.setY(front.getDirection().getModY());
         direction.setZ(front.getDirection().getModZ());
         direction.normalize();
      } else {
         direction.multiply(1.0D / distance);
      }

      return distance - frontwheel.getEdgeDistance() - backwheel.getEdgeDistance();
   }

   private final Vector calculateSpeedFactor(MinecartMember<?> back, MinecartMember<?> front) {
      Vector direction = new Vector();
      double gap = calculateGapAndDirection(back, front, direction);
      if (back == this) {
         direction.multiply(-1.0D);
      }

      double distanceDiff = back.getCartCouplerLength() + front.getCartCouplerLength() - gap;
      direction.multiply(distanceDiff);
      return direction;
   }

   public void onPhysicsPostMove() throws MemberMissingException, GroupUnloadedException {
      this.checkMissing();
      ((CommonMinecart)this.entity).vel.fixNaN();
      Vector vel = ((CommonMinecart)this.entity).getVelocity();
      if (TCConfig.legacySpeedLimiting) {
         vel.setX(MathUtil.clamp(vel.getX(), ((CommonMinecart)this.entity).getMaxSpeed()));
         vel.setY(MathUtil.clamp(vel.getY(), ((CommonMinecart)this.entity).getMaxSpeed()));
         vel.setZ(MathUtil.clamp(vel.getZ(), ((CommonMinecart)this.entity).getMaxSpeed()));
      } else {
         double vel_length = ((CommonMinecart)this.entity).vel.length();
         if (vel_length > ((CommonMinecart)this.entity).getMaxSpeed()) {
            double vel_factor = ((CommonMinecart)this.entity).getMaxSpeed() / vel_length;
            vel.multiply(vel_factor);
         }
      }

      this.getRailLogic().onSpacingUpdate(this, vel, this.speedFactor);
      this.directionFrom = this.directionTo;
      if (TCConfig.optimizeBlockActivation) {
         boolean enabled = false;
         Iterator var3 = this.getGroup().getRailTracker().getRailInformation().iterator();

         while(var3.hasNext()) {
            RailTracker.TrackedRail rail = (RailTracker.TrackedRail)var3.next();
            if (rail.member == this && rail.state.railPiece().hasBlockActivation()) {
               enabled = true;
               break;
            }
         }

         this.setBlockActivationEnabled(enabled);
      }

      if (this.wasMoving != vel.lengthSquared() >= 1.0E-10D) {
         this.wasMoving = !this.wasMoving;
         if (this.wasMoving) {
            String effectName = this.getProperties().getDriveSound();
            if (effectName != null && !effectName.isEmpty()) {
               com.bergerkiller.bukkit.tc.utils.Effect effect = new com.bergerkiller.bukkit.tc.utils.Effect();
               effect.parseEffect(effectName);
               effect.volume = 100.0F;
               Iterator var16 = ((CommonMinecart)this.entity).getPlayerPassengers().iterator();

               while(var16.hasNext()) {
                  Player p = (Player)var16.next();
                  effect.play(p);
               }

               var16 = ((CommonMinecart)this.entity).getNearbyEntities(64.0D).iterator();

               while(var16.hasNext()) {
                  Entity nearby = (Entity)var16.next();
                  if (nearby instanceof Player && !((CommonMinecart)this.entity).isPassenger(nearby)) {
                     effect.play(((CommonMinecart)this.entity).getLocation(), (Player)nearby);
                  }
               }
            }
         }

         this.onPropertiesChanged();
      }

      boolean preMoveInverted = false;
      RailState preMoveState = this.railTrackerMember.getRail().state.clone();
      if (preMoveState.position().motDot(vel) < 0.0D) {
         preMoveState = preMoveState.cloneAndInvertMotion();
         preMoveInverted = true;
      }

      this.onMove(MoveType.SELF, vel.getX(), vel.getY(), vel.getZ());
      this.checkMissing();
      boolean powered = false;
      double factor;
      if (preMoveState != null && preMoveState.railType() != RailType.NONE) {
         factor = preMoveState.position().distance(((CommonMinecart)this.entity).loc);
         TrackWalkingPoint p = new TrackWalkingPoint(preMoveState);

         do {
            this.snapToPosition(p.state.position(), preMoveInverted);
            p.currentRailLogic.onPostMove(this);
            p.state.railType().onPostMove(this);
         } while(p.moveStep(factor - p.movedTotal));

         if (powered = p.failReason == TrackWalkingPoint.FailReason.LIMIT_REACHED) {
            this.snapToPosition(p.state.position(), preMoveInverted);
            p.currentRailLogic.onPostMove(this);
            p.state.railType().onPostMove(this);
         } else {
            double remaining = factor - p.movedTotal;
            if (remaining > 1.0E-10D) {
               p.state.position().move(remaining);
            }

            this.snapToPosition(p.state.position(), preMoveInverted);
         }
      }

      if (!powered) {
         RailState newRailState = this.discoverRail();
         if (newRailState.railType() != RailType.NONE) {
            this.snapToPosition(newRailState.position(), preMoveInverted);
         }
      }

      this.updateManualMovement();
      this.doPostMoveLogic();
      if (!this.isDerailed()) {
         TrainProperties trainProp = this.getGroup().getProperties();
         if (trainProp.isSlowingDown(SlowdownMode.FRICTION) && ((CommonMinecart)this.entity).getMaxSpeed() > 0.0D) {
            if (!((CommonMinecart)this.entity).hasPassenger() && ((CommonMinecart)this.entity).isSlowWhenEmpty() && TCConfig.slowDownEmptyCarts) {
               factor = TCConfig.slowDownMultiplierSlow;
            } else {
               factor = TCConfig.slowDownMultiplierNormal;
            }

            factor = Math.max(0.0D, 1.0D + trainProp.getFriction() * (factor - 1.0D));
            if (this.getGroup().getUpdateStepCount() > 1) {
               factor = Math.pow(factor, this.getGroup().getUpdateSpeedFactor());
            }

            ((CommonMinecart)this.entity).vel.multiply(factor);
         }
      }

      if (this.getRailType() instanceof RailTypeActivator) {
         powered = ((RailTypeActivator)this.getRailType()).isPowered();
         this.onActivatorUpdate(powered);
         if (powered && this.railActivated.set()) {
            this.onActivate();
         } else {
            this.railActivated.clear();
         }
      } else {
         this.railActivated.clear();
      }

      this.getRailType().onPostMove(this);
      this.getRailTracker().setLiveRailLogic();
      Location from = this.lastLocation;
      Location to = ((CommonMinecart)this.entity).getLocation();
      if (from == null || from.getWorld() != to.getWorld()) {
         from = to;
         this.lastLocation = to;
      }

      this.lastLocationSync = from;
      this.lastLocation = to;
      ((CommonMinecart)this.entity).last.set(from);
      Vehicle vehicle = (Vehicle)((CommonMinecart)this.entity).getEntity();
      CommonUtil.callEvent(new VehicleUpdateEvent(vehicle));
      Iterator var23;
      if (from.getX() != to.getX() || from.getY() != to.getY() || from.getZ() != to.getZ()) {
         CommonUtil.callEvent(new VehicleMoveEvent(vehicle, from, to));
         var23 = this.getSignTracker().getActiveTrackedSigns().cloneAsIterable().iterator();

         while(var23.hasNext()) {
            SignTracker.ActiveSign sign = (SignTracker.ActiveSign)var23.next();
            sign.executeEventForMember(SignActionType.MEMBER_MOVE, this);
         }
      }

      if (!this.isDerailed()) {
         this.firstKnownDerailedPosition = null;
      } else if (this.firstKnownDerailedPosition == null || this.firstKnownDerailedPosition.getWorld() != ((CommonMinecart)this.entity).getWorld()) {
         this.firstKnownDerailedPosition = ((CommonMinecart)this.entity).getLocation();
      }

      Entity passenger;
      if (!this.hasLinkedFarMinecarts) {
         this.hasLinkedFarMinecarts = true;
         var23 = ((CommonMinecart)this.entity).getNearbyEntities(0.2D, 0.0D, 0.2D).iterator();

         while(var23.hasNext()) {
            passenger = (Entity)var23.next();
            if (passenger instanceof Minecart && !((CommonMinecart)this.entity).isPassenger(passenger)) {
               EntityUtil.doCollision(passenger, ((CommonMinecart)this.entity).getEntity());
            }
         }
      }

      var23 = ((CommonMinecart)this.entity).getPassengers().iterator();

      while(var23.hasNext()) {
         passenger = (Entity)var23.next();
         if (passenger.isDead()) {
            ((CommonMinecart)this.entity).removePassenger(passenger);
         }
      }

      this.checkMissing();
      this.soundLoop.onTick();
   }

   public void onTick() {
      if (!this.isUnloaded()) {
         this.getGroup();
      }

      if (this.lastLocationSync != null && this.lastLocationSync.getWorld() == ((CommonMinecart)this.entity).getWorld()) {
         ((CommonMinecart)this.entity).last.set(this.lastLocationSync);
      }

   }

   public void setRoll(double newroll) {
      if (newroll != this.roll) {
         this.roll = newroll;
      }

   }

   public double getRoll() {
      double result = this.roll;
      return result + this.getWheels().getBankingRoll();
   }

   public void setRotationWrap(float newyaw, float newpitch) {
      float oldyaw;
      for(oldyaw = ((CommonMinecart)this.entity).loc.getYaw(); newyaw - oldyaw >= 90.0F; newpitch = -newpitch) {
         newyaw -= 180.0F;
      }

      while(newyaw - oldyaw < -90.0F) {
         newyaw += 180.0F;
         newpitch = -newpitch;
      }

      while(newyaw - oldyaw <= -180.0F) {
         newyaw += 360.0F;
      }

      while(newyaw - oldyaw > 180.0F) {
         newyaw -= 360.0F;
      }

      ((CommonMinecart)this.entity).setRotation(newyaw, newpitch);
   }

   public String getLocalizedName() {
      String name = super.getLocalizedName();
      if (!this.isSingle()) {
         name = name + " (Train)";
      }

      return name;
   }

   public boolean isPlayerTakeable() {
      if (this.isUnloaded()) {
         return this.unloadedLastPlayerTakable;
      } else {
         return this.isSingle() && this.getGroup().getProperties().isPlayerTakeable();
      }
   }

   public int getAvailableSeatCount(Entity passenger) {
      MinecartMemberNetwork network = (MinecartMemberNetwork)CommonUtil.tryCast(((CommonMinecart)this.entity).getNetworkController(), MinecartMemberNetwork.class);
      if (network != null) {
         network.syncPassengers();
      }

      return this.getAttachments().getAvailableSeatCount(passenger);
   }

   public double getPreferredDistance(MinecartMember<?> member) {
      return 0.5D * ((double)((CommonMinecart)this.entity).getWidth() + (double)((CommonMinecart)member.getEntity()).getWidth()) + this.getCartCouplerLength() + member.getCartCouplerLength();
   }

   public double getMaximumDistance(MinecartMember<?> member) {
      return 0.5D * ((double)((CommonMinecart)this.entity).getWidth() + (double)((CommonMinecart)member.getEntity()).getWidth()) + TCConfig.cartDistanceGapMax;
   }

   public int getMaximumBlockDistance(MinecartMember<?> member) {
      return MathUtil.ceil(2.0D * this.getMaximumDistance(member));
   }

   public OrientedBoundingBox getHitBox() {
      Quaternion orientation = this.getOrientation();
      Vector position = this.getWheels().getPosition().clone();
      double height = 1.0D;
      position.add(orientation.upVector().multiply(0.5D));
      OrientedBoundingBox box = new OrientedBoundingBox();
      box.setPosition(position);
      box.setSize(1.0D, 1.0D, (double)((CommonMinecart)this.entity).getWidth());
      box.setOrientation(orientation);
      return box;
   }

   public double getCartCouplerLength() {
      return this.getProperties().getModel().getCartCouplerLength();
   }

   boolean railDetectPositionChange() {
      Vector nvel = ((CommonMinecart)this.entity).vel.vector();
      double fact = MathUtil.getNormalizationFactor(nvel);
      if (fact != Double.POSITIVE_INFINITY && !Double.isNaN(fact)) {
         nvel.multiply(fact);
      }

      if (this.lastRailRefreshPosition != null && this.lastRailRefreshDirection != null) {
         if (this.lastRailRefreshPosition.getX() == ((CommonMinecart)this.entity).loc.getX() && this.lastRailRefreshPosition.getY() == ((CommonMinecart)this.entity).loc.getY() && this.lastRailRefreshPosition.getZ() == ((CommonMinecart)this.entity).loc.getZ() && this.lastRailRefreshDirection.getX() == nvel.getX() && this.lastRailRefreshDirection.getY() == nvel.getY() && this.lastRailRefreshDirection.getZ() == nvel.getZ()) {
            return false;
         } else {
            this.lastRailRefreshPosition.setX(((CommonMinecart)this.entity).loc.getX());
            this.lastRailRefreshPosition.setY(((CommonMinecart)this.entity).loc.getY());
            this.lastRailRefreshPosition.setZ(((CommonMinecart)this.entity).loc.getZ());
            this.lastRailRefreshDirection.setX(nvel.getX());
            this.lastRailRefreshDirection.setY(nvel.getY());
            this.lastRailRefreshDirection.setZ(nvel.getZ());
            return true;
         }
      } else {
         this.lastRailRefreshPosition = ((CommonMinecart)this.entity).loc.vector();
         this.lastRailRefreshDirection = ((CommonMinecart)this.entity).vel.vector();
         return true;
      }
   }

   public List<String> getAnimationNames() {
      return this.getAttachments().isAttached() ? this.getAttachments().getRootAttachment().getAnimationNamesRecursive() : Collections.emptyList();
   }

   public Set<String> getAnimationScenes(String animationName) {
      return this.getAttachments().isAttached() ? this.getAttachments().getRootAttachment().getAnimationScenesRecursive(animationName) : Collections.emptySet();
   }

   public boolean playNamedAnimationFor(int[] targetPath, AnimationOptions options) {
      Attachment attachment = this.findAttachment(targetPath);
      return attachment != null && attachment.playNamedAnimation(options);
   }

   public boolean playAnimationFor(int[] targetPath, Animation animation) {
      Attachment attachment = this.findAttachment(targetPath);
      if (attachment == null) {
         return false;
      } else {
         attachment.startAnimation(animation);
         return true;
      }
   }

   public boolean playNamedAnimation(String name) {
      return AnimationController.super.playNamedAnimation(name);
   }

   public boolean playNamedAnimation(AnimationOptions options) {
      return this.getAttachments().isAttached() && this.getAttachments().getRootAttachment().playNamedAnimationRecursive(options);
   }

   public Attachment findAttachment(int[] targetPath) {
      return this.getAttachments().isAttached() ? this.getAttachments().getRootAttachment().findChild(targetPath) : null;
   }
}
