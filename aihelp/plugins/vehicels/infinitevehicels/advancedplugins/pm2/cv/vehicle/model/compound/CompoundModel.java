package advancedplugins.pm2.cv.vehicle.model.compound;

import advancedplugins.pm2.cv.api.InfiniteVehicles;
import advancedplugins.pm2.cv.api.configuration.Configuration;
import advancedplugins.pm2.cv.api.vehicle.VehicleState;
import advancedplugins.pm2.cv.api.vehicle.configuration.model.compound.AnimationConfiguration;
import advancedplugins.pm2.cv.api.vehicle.configuration.model.compound.CompoundModelConfiguration;
import advancedplugins.pm2.cv.api.vehicle.configuration.model.compound.PartConfiguration;
import advancedplugins.pm2.cv.api.vehicle.model.VehicleModelBase;
import advancedplugins.pm2.cv.enums.EnumStandProperty;
import advancedplugins.pm2.cv.fake.FakeEntity;
import advancedplugins.pm2.cv.fake.FakeEntityLinker;
import advancedplugins.pm2.cv.fake.FakeEntityShowGroup;
import advancedplugins.pm2.cv.fake.armorstand.FakeArmorStand;
import advancedplugins.pm2.cv.fake.display.FakeDisplay;
import advancedplugins.pm2.cv.fake.display.FakeDisplayHandle;
import advancedplugins.pm2.cv.packet.PacketBatcher;
import advancedplugins.pm2.cv.util.InfiniteModelUtil;
import advancedplugins.pm2.cv.vehicle.VehicleImpl;
import com.google.common.collect.Sets;
import gnu.trove.set.hash.THashSet;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

public class CompoundModel extends VehicleModelBase<CompoundModelConfiguration> {
   private static final double SUFFOCATION_BLACKOUT_FIX = 0.1D;
   final Set<Part> parts = new THashSet();
   final FakeArmorStand bone;
   final PacketBatcher packetBatcher;
   final FakeEntityLinker.Metadata metadataLinker;
   AnimationTicker animationTicker;
   VehicleState animationState;
   int tickCount;
   int playDelayedTick = 0;
   AnimationConfiguration playDelayed = null;
   private HashMap<Part, List<Part>> parentChildMap;
   private BukkitTask autoCorrectTask;
   private boolean lastClimbing = false;

   public CompoundModel(@NotNull VehicleImpl vehicle, @NotNull CompoundModelConfiguration configuration, @NotNull World world, double x, double y, double z) {
      super(var1, var2, var3, var4, var6, var8);
      this.packetBatcher = var1.getPacketBatcher();
      this.bone = new FakeArmorStand(var3);
      this.bone.setLocation(var4, var6, var8);
      this.bone.setProperty(EnumStandProperty.GRAVITY, false);
      this.bone.setProperty(EnumStandProperty.VISIBILITY, false);
      this.bone.setProperty(EnumStandProperty.MARKER, true);
      this.bone.setProperty(EnumStandProperty.SILENT, true);
      this.bone.setProperty(EnumStandProperty.BASE_PLATE, false);
      this.metadataLinker = FakeEntityLinker.getNewMetadataLinker();
      this.parentChildMap = new HashMap();
   }

   public void tick() {
      super.tick();
      if (this.spawned) {
         if (this.animationTicker != null) {
            this.animationTicker.tick();
         }

         if (this.playDelayed != null && this.tickCount == this.playDelayedTick) {
            this.playAnimation(this.playDelayed);
            this.playDelayed = null;
         }

         ++this.tickCount;
      }
   }

   public void spawn() {
      if (!this.spawned) {
         this.bone.register();
         FakeEntityShowGroup.FakeEntityShowGroupBuilder var1 = FakeEntityShowGroup.builder();
         Iterator var2 = ((CompoundModelConfiguration)this.configuration).getParts().iterator();

         while(var2.hasNext()) {
            PartConfiguration var3 = (PartConfiguration)var2.next();
            Part var4 = new Part(this, var3, this.world, this.x, this.y, this.z);
            this.parts.add(var4);
            var1.entry(var4.display);
            this.metadataLinker.link(var4.display);
            this.bone.addPassenger(var4.display, false);
         }

         if (((CompoundModelConfiguration)this.getConfiguration()).getModelID() != null && !this.vehicle.getConfiguration().isBlockBenchPartsLoaded()) {
            InfiniteModelUtil.loadModelBones(this, this.world, this.x, this.y, this.z, this.vehicle, var1);
         }

         if (((CompoundModelConfiguration)this.getConfiguration()).getModelID() != null && !this.vehicle.getConfiguration().isBlockBenchAnimationsLoaded()) {
            InfiniteModelUtil.loadBlockBenchAnimations(this, new Location(this.world, this.x, this.y, this.z, this.getLocation().getYaw(), this.getLocation().getPitch()), ((CompoundModelConfiguration)this.getConfiguration()).getModelID(), this.vehicle);
         }

         FakeEntityShowGroup var5 = var1.build();
         var5.setHideFarAwayChunks(Configuration.RENDER_FAR_AWAY_CHUNKS.intValueClamp(1, Integer.MAX_VALUE));
         var5.setShowHideDelay(Configuration.RENDER_DELAY.longValueClamp(0L, 100L));
         var5.setListener(new FakeEntityShowGroup.Listener() {
            private final Set<UUID> immediate = Sets.newConcurrentHashSet();

            public void onBeginShowing(@NotNull Player player) {
               Location var2 = var1.getLocation();
               if ((var2.getBlockX() >> 4 != Location.locToBlock(CompoundModel.this.vehicle.getX()) >> 4 || var2.getBlockZ() >> 4 != Location.locToBlock(CompoundModel.this.vehicle.getZ()) >> 4) && CompoundModel.this.vehicle.getMomentumX() == 0.0D && CompoundModel.this.vehicle.getMomentumY() == 0.0D && CompoundModel.this.vehicle.getMomentumZ() == 0.0D) {
                  this.immediate.remove(var1.getUniqueId());
               } else {
                  this.immediate.add(var1.getUniqueId());
               }

            }

            public void onEntriesShown(@NotNull Player player, Collection<FakeEntity<?, ?>> entries) {
               if (this.immediate.contains(var1.getUniqueId())) {
                  FakeEntityLinker.Generic var3 = FakeEntityLinker.getNewGenericLinker();
                  var3.begin();
                  Iterator var4 = var2.iterator();

                  while(var4.hasNext()) {
                     FakeEntity var5 = (FakeEntity)var4.next();
                     if (var5 instanceof FakeDisplay) {
                        ((FakeDisplay)var5).trickySetInvisibleTo(var1, true, var3);
                     }
                  }

                  var3.complete();
               }
            }

            public void onCompleteShowing(@NotNull Player player) {
               CompoundModel.this.bone.sendPassengers(var1);
               if (this.immediate.remove(var1.getUniqueId())) {
                  FakeEntityLinker.Generic var2 = FakeEntityLinker.getNewGenericLinker();
                  var2.begin();
                  Iterator var3 = CompoundModel.this.parts.iterator();

                  while(var3.hasNext()) {
                     Part var4 = (Part)var3.next();
                     var4.display.trickySetInvisibleTo(var1, false, var2);
                  }

                  var2.complete();
               }

            }
         });
         this.parts.forEach(Part::show);
         this.spawned = true;
         this.playAnimation(VehicleState.IDLE);
      }
   }

   protected void onStateChanged() {
      if (this.spawned) {
         this.playAnimation(this.vehicle.getCurrentState());
      }

   }

   private void playAnimation(@NotNull VehicleState state) {
      AnimationConfiguration var2 = ((CompoundModelConfiguration)this.configuration).getAnimationByState(var1);
      if (var2 == null) {
         var2 = ((CompoundModelConfiguration)this.configuration).getAnimationAllStates();
      }

      if (this.animationTicker == null || !Objects.equals(var2, this.animationTicker.getAnimation().configuration)) {
         this.animationState = var1;
         boolean var3 = this.animationTicker != null;
         if (this.animationTicker != null) {
            this.animationTicker.stop();
            this.animationTicker = null;
         }

         if (var2 != null) {
            this.playAnimation(var2);
         }

      }
   }

   protected void playAnimation(@NotNull AnimationConfiguration animation) {
      this.animationTicker = new AnimationTicker(var1, this);
      this.animationTicker.start();
   }

   protected void onLocationChanged() {
      this.onLocationChanged(false);
   }

   protected void onLocationChanged(boolean climbing) {
      this.onLocationChanged(var1, true);
   }

   protected void onLocationChanged(boolean climbing, boolean writeLocation) {
      this.bone.setForceTeleport(true);
      this.bone.setLocation(this.x, this.y + 0.1D + (!var1 && !this.lastClimbing ? 0.0D : 0.25D), this.z, false);
      if (var2) {
         this.packetBatcher.track(this.bone.writeLocation());
      }

      if (var1) {
         this.lastClimbing = true;
         if (this.autoCorrectTask != null) {
            this.autoCorrectTask.cancel();
         }

         this.autoCorrectTask = Bukkit.getScheduler().runTaskLater(InfiniteVehicles.getPlugin(), () -> {
            this.lastClimbing = false;
            this.bone.setLocation(this.x, this.y + 0.1D, this.z, false);
            this.packetBatcher.track(this.bone.writeLocation());
            this.bone.setForceTeleport(false);
         }, 5L);
      } else {
         this.bone.setForceTeleport(false);
      }

   }

   protected void onRotationChanged() {
      this.onRotationChanged(true);
   }

   protected void onRotationChanged(boolean writeRot) {
      Iterator var2 = this.parts.iterator();

      while(var2.hasNext()) {
         Part var3 = (Part)var2.next();
         var3.display.setRotation(this.rotation, 0.0F, false);
         if (var1) {
            this.packetBatcher.track(((FakeDisplayHandle)var3.display.handle).writeMetadata(false));
         }
      }

   }

   protected void onLocationAndRotationChanged(boolean climbing) {
      this.onRotationChanged();
      this.onLocationChanged(var1);
   }

   public void destroy() {
      this.bone.unregister();
      if (this.animationTicker != null) {
         this.animationTicker.stop();
      }

      this.parts.forEach(Part::destroy);
      this.parts.clear();
   }

   public void despawnParts() {
      Optional.ofNullable(this.animationTicker).ifPresent(AnimationTicker::stop);
      this.parts.forEach(Part::destroy);
   }

   public Set<Part> getParts() {
      return this.parts;
   }

   public FakeArmorStand getBone() {
      return this.bone;
   }

   public PacketBatcher getPacketBatcher() {
      return this.packetBatcher;
   }

   public FakeEntityLinker.Metadata getMetadataLinker() {
      return this.metadataLinker;
   }

   public AnimationTicker getAnimationTicker() {
      return this.animationTicker;
   }

   public VehicleState getAnimationState() {
      return this.animationState;
   }

   public int getTickCount() {
      return this.tickCount;
   }

   public int getPlayDelayedTick() {
      return this.playDelayedTick;
   }

   public AnimationConfiguration getPlayDelayed() {
      return this.playDelayed;
   }

   public BukkitTask getAutoCorrectTask() {
      return this.autoCorrectTask;
   }

   public boolean isLastClimbing() {
      return this.lastClimbing;
   }

   public HashMap<Part, List<Part>> getParentChildMap() {
      return this.parentChildMap;
   }

   public void setParentChildMap(final HashMap<Part, List<Part>> parentChildMap) {
      this.parentChildMap = var1;
   }
}
