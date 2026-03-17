package advancedplugins.pm2.cv.fake;

import advancedplugins.pm2.cv.api.InfiniteVehicles;
import advancedplugins.pm2.cv.api.handler.PlayerWrapperHandler;
import advancedplugins.pm2.cv.api.util.reflection.ClassReflection;
import advancedplugins.pm2.cv.enums.PropertiesEnum;
import advancedplugins.pm2.cv.nms.NmsImplementations;
import advancedplugins.pm2.cv.service.PacketService;
import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import gnu.trove.set.hash.THashSet;
import io.netty.channel.ChannelPipeline;
import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class FakeEntity<P extends Enum<P>, T extends FakeEntityHandle<P>> {
   protected final UUID uuid;
   protected final World world;
   public final T handle;
   protected final PacketService packetService;
   protected final FakeEntityHandler handler;
   protected final PlayerWrapperHandler playerWrapperHandler;
   @NotNull
   protected final Class<P> propertiesEnumClass;
   @NotNull
   protected final Object[] properties;
   protected double x;
   protected double y;
   protected double z;
   protected double lastX;
   protected double lastY;
   protected double lastZ;
   protected float yaw;
   protected float pitch;
   protected float headRotation;
   @Nullable
   protected FakeEntityLinker.Location locationLinker;
   @Nullable
   protected FakeEntityLinker.Metadata metadataLinker;
   protected boolean forceTeleport;
   @Nullable
   protected FakeEntity<?, ?> riding = null;
   protected final Set<FakeEntity<?, ?>> passengers = Sets.newConcurrentHashSet();
   protected int hideFarAwayChunks = 5;
   protected FakeEntityShowGroup showGroup;
   protected boolean registered;

   public FakeEntity(@NotNull Class<P> propertiesEnumClass, @NotNull World world) {
      Preconditions.checkArgument(PropertiesEnum.class.isAssignableFrom(var1), "properties enum class must implement " + PropertiesEnum.class.getName());
      this.propertiesEnumClass = var1;
      this.uuid = UUID.randomUUID();
      this.world = var2;
      this.handle = this.createHandle(var2);
      this.packetService = (PacketService)NmsImplementations.getSingleInstanceImplementation(PacketService.class);
      this.handler = (FakeEntityHandler)InfiniteVehicles.getHandler(FakeEntityHandler.class);
      this.playerWrapperHandler = InfiniteVehicles.getPlayerWrapperHandler();
      Enum[] var3 = (Enum[])var1.getEnumConstants();
      this.properties = new Object[var3.length];

      for(int var4 = 0; var4 < var3.length; ++var4) {
         this.properties[var4] = ((PropertiesEnum)var3[var4]).getDefaultValueRaw();
      }

   }

   protected abstract T createHandle(@NotNull World world);

   public int getId() {
      return this.handle.getId();
   }

   public void register() {
      this.handler.register(this);
      this.registered = true;
   }

   public void unregister() {
      this.handler.unregister(this);
      this.registered = false;
   }

   protected void show(@NotNull Player player, boolean noDelay) {
      if (this.showGroup != null && !var2) {
         this.showGroup.show(var1);
      } else {
         if (!this.handle.isShownTo(var1)) {
            if (!var2 && this.showGroup != null) {
               this.showGroup.show(var1);
            } else {
               this.handle.show(var1);
            }
         }

      }
   }

   protected void show(@NotNull Player player) {
      this.show(var1, false);
   }

   protected void hide(Player player, boolean disconnected, boolean noDelay) {
      if (var2) {
         var3 = true;
      }

      if (this.showGroup != null && !var3) {
         this.showGroup.hide(var1);
      } else {
         if (this.handle.isShownTo(var1)) {
            if (!var3 && this.showGroup != null) {
               this.showGroup.hide(var1);
            } else {
               this.handle.hide(var1, var2);
            }
         }

      }
   }

   protected void hide(Player player, boolean disconnected) {
      this.hide(var1, var2, false);
   }

   protected void hide(boolean noDelay) {
      if (!var1 && this.showGroup != null) {
         this.showGroup.hideAll();
      } else {
         this.handle.hide();
      }

   }

   protected void hide() {
      this.hide(false);
   }

   public void sendMetadata(boolean all) {
      if (this.metadataLinker != null && this.metadataLinker.active) {
         this.metadataLinker.toFlush(this.handle.writeMetadata(var1));
      } else {
         this.handle.sendMetadata(var1);
      }

   }

   public void sendMetadata() {
      this.sendMetadata(false);
   }

   @NotNull
   public Object getPropertyRaw(@NotNull P property) {
      return this.properties[var1.ordinal()];
   }

   @NotNull
   public <V> V getProperty(@NotNull Class<V> caster, @NotNull P property) {
      return var1.cast(this.getPropertyRaw(var2));
   }

   public void setProperty(@NotNull P property, @NotNull Object value, boolean forceSet, boolean send) {
      Class var5 = ClassReflection.isPrimitiveType(var2.getClass()) ? ClassReflection.getPrimitiveType(var2.getClass()) : var2.getClass();
      if (!((PropertiesEnum)var1).getValueType().isAssignableFrom(var5)) {
         throw new IllegalArgumentException("value of type " + String.valueOf(((PropertiesEnum)var1).getValueType()) + " expected!");
      } else {
         if (var3 || !Objects.equals(this.properties[var1.ordinal()], var2)) {
            this.properties[var1.ordinal()] = var2;
            this.handle.applyProperty(var1, var2);
            if (var4) {
               this.sendMetadata(false);
            }
         }

      }
   }

   public void setProperty(@NotNull P property, @NotNull Object value, boolean send) {
      this.setProperty(var1, var2, false, var3);
   }

   public void setProperty(@NotNull P property, @NotNull Object value) {
      this.setProperty(var1, var2, true);
   }

   public Location getBukkitLocation() {
      return new Location(this.world, this.x, this.y, this.z, this.yaw, this.pitch);
   }

   public void setLocationRotation(double x, double y, double z, float yaw, float pitch, boolean send) {
      boolean var10 = this.setXRaw(var1) || this.setYRaw(var3) || this.setZRaw(var5);
      boolean var11 = this.setRotationRaw(var7, var8);
      double var12 = this.getLastY();
      double var14 = var12 - var3;
      if (var14 > 0.01D || var14 < -0.5D) {
         var3 += 0.3D;
      }

      if (var10 && var11) {
         this.handle.applyLocationRotation(var1, var3, var5, var7, var8);
         if (var9) {
            this.sendLocationRotation();
         }
      } else if (var10) {
         this.handle.applyLocation(var1, var3, var5);
         if (var9) {
            this.sendLocation();
         }
      } else if (var11) {
         this.handle.applyRotation(var7, var8);
         if (var9) {
            this.sendRotation();
         }
      }

      if (var10) {
         this.handler.onLocationSet(this);
      }

      Iterator var16 = this.passengers.iterator();

      while(var16.hasNext()) {
         FakeEntity var17 = (FakeEntity)var16.next();
         var17.setLocation(var1, var3, var5, false);
      }

   }

   public void setLocationRotation(double x, double y, double z, float yaw, float pitch) {
      this.setLocationRotation(var1, var3, var5, var7, var8, true);
   }

   public void setLocation(double x, double y, double z, boolean force, boolean send) {
      boolean var9 = this.setXRaw(var1);
      boolean var10 = this.setYRaw(var3);
      boolean var11 = this.setZRaw(var5);
      if (var7 || var9 || var10 || var11) {
         this.handle.applyLocation(var1, var3, var5);
         if (var8) {
            this.sendLocation();
         }

         this.handler.onLocationSet(this);
      }

      Iterator var12 = this.passengers.iterator();

      while(var12.hasNext()) {
         FakeEntity var13 = (FakeEntity)var12.next();
         var13.setLocation(var1, var3, var5, false);
      }

   }

   public void setLocation(double x, double y, double z, boolean send) {
      this.setLocation(var1, var3, var5, false, var7);
   }

   protected boolean setXRaw(double x) {
      if (Double.compare(var1, this.x) != 0) {
         this.lastX = this.x;
         this.x = var1;
         return true;
      } else {
         return false;
      }
   }

   protected boolean setYRaw(double y) {
      if (Double.compare(var1, this.y) != 0) {
         this.lastY = this.y;
         this.y = var1;
      }

      return true;
   }

   protected boolean setZRaw(double z) {
      if (Double.compare(var1, this.z) != 0) {
         this.lastZ = this.z;
         this.z = var1;
         return true;
      } else {
         return false;
      }
   }

   public void setLocation(double x, double y, double z) {
      this.setLocation(var1, var3, var5, false, true);
   }

   public void sendLocationRotation() {
      if (this.locationLinker != null && this.locationLinker.active) {
         this.locationLinker.toFlush(this.handle.writeLocationRotation(this.forceTeleport));
      } else {
         this.handle.sendLocationRotation(this.forceTeleport);
      }

      this.forceTeleport = false;
   }

   public void sendLocation() {
      if (this.locationLinker != null && this.locationLinker.active) {
         this.locationLinker.toFlush(this.handle.writeLocation(this.forceTeleport));
      } else {
         this.handle.sendLocationRotation(this.forceTeleport);
      }

      this.forceTeleport = false;
   }

   @NotNull
   public Collection<ChannelPipeline> writeLocationRotation() {
      return this.handle.writeLocationRotation(this.forceTeleport);
   }

   @NotNull
   public Collection<ChannelPipeline> writeLocation() {
      return this.handle.writeLocation(this.forceTeleport);
   }

   @NotNull
   public Collection<ChannelPipeline> writeRotation() {
      return this.handle.writeRotation();
   }

   public void setRotation(float yaw, float pitch, boolean send) {
      if (this.setRotationRaw(var1, var2)) {
         this.handle.applyRotation(var1, var2);
         if (var3) {
            this.sendRotation();
         }
      }

   }

   public void setRotation(float yaw, float pitch) {
      this.setRotation(var1, var2, true);
   }

   protected boolean setRotationRaw(float yaw, float pitch) {
      if (Float.compare(this.yaw, var1) == 0 && Float.compare(this.pitch, var2) == 0) {
         return false;
      } else {
         this.yaw = var1;
         this.pitch = var2;
         return true;
      }
   }

   public void setHeadRotation(float headRotation, boolean send) {
      if (Float.compare(this.headRotation, var1) != 0) {
         this.headRotation = var1;
         this.handle.applyHeadRotation(var1);
         if (var2) {
            this.sendHeadRotation();
         }
      }

   }

   public void setHeadRotation(float headRotation) {
      this.setHeadRotation(var1, true);
   }

   public void sendRotation() {
      if (this.locationLinker != null && this.locationLinker.active) {
         this.locationLinker.toFlush(this.handle.writeRotation());
      } else {
         this.handle.sendRotation();
      }

   }

   public void sendHeadRotation() {
      this.handle.sendHeadRotation();
   }

   @Nullable
   public FakeEntity<?, ?> getRiding() {
      return this.riding;
   }

   public void addPassenger(@NotNull FakeEntity<?, ?> passenger, boolean send) {
      if (var1 != this) {
         if (this.passengers.add(var1)) {
            this.handle.setPassengers(this.buildPassengerHandlesSet());
            var1.setLocation(this.x, this.y, this.z, false);
            if (var2) {
               this.sendPassengers();
            }
         }

         var1.riding = this;
      }
   }

   public void addPassenger(@NotNull FakeEntity<?, ?> passenger) {
      this.addPassenger(var1, true);
   }

   public void removePassenger(@NotNull FakeEntity<?, ?> passenger, boolean send) {
      if (this.passengers.remove(var1)) {
         this.handle.setPassengers(this.buildPassengerHandlesSet());
         if (var2) {
            this.sendPassengers();
         }
      }

      if (var1.riding == this) {
         var1.riding = null;
      }

   }

   public void removePassenger(@NotNull FakeEntity<?, ?> passenger) {
      this.removePassenger(var1, true);
   }

   public void setPassengers(@Nullable Collection<FakeEntity<?, ?>> passengers, boolean send) {
      this.passengers.clear();
      if (var1 != null) {
         var1.forEach((var1x) -> {
            this.addPassenger(var1x, false);
         });
      }

      this.handle.setPassengers(this.buildPassengerHandlesSet());
      if (var2) {
         this.sendPassengers();
      }

   }

   public void setPassengers(@Nullable Collection<FakeEntity<?, ?>> passengers) {
      this.setPassengers(var1, true);
   }

   public void sendPassengers(@NotNull Player player) {
      this.handle.sendPassengersTo(var1);
   }

   public void sendPassengers() {
      this.handle.sendPassengers();
   }

   protected Set<FakeEntityHandle<?>> buildPassengerHandlesSet() {
      THashSet var1 = new THashSet();
      Iterator var2 = this.passengers.iterator();

      while(var2.hasNext()) {
         FakeEntity var3 = (FakeEntity)var2.next();
         var1.add(var3.handle);
      }

      return var1;
   }

   public boolean equals(Object o) {
      if (this == var1) {
         return true;
      } else if (var1 != null && this.getClass() == var1.getClass()) {
         FakeEntity var2 = (FakeEntity)var1;
         return this.uuid.equals(var2.uuid);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return this.uuid.hashCode();
   }

   public UUID getUuid() {
      return this.uuid;
   }

   public World getWorld() {
      return this.world;
   }

   public double getX() {
      return this.x;
   }

   public double getY() {
      return this.y;
   }

   public double getZ() {
      return this.z;
   }

   public double getLastX() {
      return this.lastX;
   }

   public double getLastY() {
      return this.lastY;
   }

   public double getLastZ() {
      return this.lastZ;
   }

   public float getYaw() {
      return this.yaw;
   }

   public float getPitch() {
      return this.pitch;
   }

   public float getHeadRotation() {
      return this.headRotation;
   }

   public void setForceTeleport(final boolean forceTeleport) {
      this.forceTeleport = var1;
   }

   public int getHideFarAwayChunks() {
      return this.hideFarAwayChunks;
   }

   public void setHideFarAwayChunks(final int hideFarAwayChunks) {
      this.hideFarAwayChunks = var1;
   }

   public FakeEntityShowGroup getShowGroup() {
      return this.showGroup;
   }

   public void setShowGroup(final FakeEntityShowGroup showGroup) {
      this.showGroup = var1;
   }
}
