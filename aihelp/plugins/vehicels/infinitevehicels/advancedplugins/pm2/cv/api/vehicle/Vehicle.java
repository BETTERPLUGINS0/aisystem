package advancedplugins.pm2.cv.api.vehicle;

import advancedplugins.pm2.cv.api.enums.EnumDamageType;
import advancedplugins.pm2.cv.api.enums.EnumSurface;
import advancedplugins.pm2.cv.api.interfaces.Tickable;
import advancedplugins.pm2.cv.api.upgrade.UpgradeConfiguration;
import advancedplugins.pm2.cv.api.vehicle.configuration.VehicleConfiguration;
import advancedplugins.pm2.cv.api.vehicle.configuration.VehicleStorageSlotsConfiguration;
import advancedplugins.pm2.cv.api.vehicle.controller.VehicleController;
import advancedplugins.pm2.cv.api.vehicle.input.PlayerInput;
import advancedplugins.pm2.cv.api.vehicle.input.PlayerSteerInput;
import advancedplugins.pm2.cv.api.vehicle.item.storage.VehicleItemStorage;
import advancedplugins.pm2.cv.api.vehicle.model.VehicleModel;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.IJoint;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface Vehicle extends Tickable {
   @NotNull
   UUID getUniqueId();

   @NotNull
   VehicleConfiguration getConfiguration();

   @Nullable
   UpgradeConfiguration getUpgradeConfiguration();

   @NotNull
   Map<String, Integer> getUpgradeTiers();

   void setUpgradeTier(UUID var1, String var2, int var3);

   @NotNull
   Map<String, Integer> getUpgradeTier(UUID var1);

   void despawn();

   @NotNull
   VehicleModel<?> getModel();

   @NotNull
   VehicleHitBox getCurrentHitBox();

   @Nullable
   Integer getCurrentHitBoxEntityId();

   @NotNull
   World getWorld();

   @NotNull
   VehicleState getCurrentState();

   List<VehicleItemStorage> getStorage();

   default VehicleStorageSlotsConfiguration getStorageSize() {
      return this.getConfiguration().storage();
   }

   void setStorageSize(VehicleStorageSlotsConfiguration var1);

   boolean isRemoved();

   default boolean isExists() {
      return !this.isRemoved();
   }

   default float getFuelCapacity() {
      return this.getConfiguration().fuel().getCapacity();
   }

   float getFuelLevel();

   void setFuelLevel(float var1);

   default void addFuel(float amount) {
      float current = this.getFuelLevel();
      this.setFuelLevel(current + amount);
   }

   default void consumeFuel(float consumption) {
      float current = this.getFuelLevel();
      this.setFuelLevel(current - consumption);
   }

   default float getMaxHealth() {
      return this.getConfiguration().health().getMaxHealth();
   }

   float getHealth();

   void setHealth(float var1);

   default void heal(float amount) {
      float current = this.getHealth();
      this.setHealth(current + amount);
   }

   void damage(@Nullable EnumDamageType var1, float var2, @Nullable Entity var3, @Nullable Entity var4, @NotNull Entity var5, @Nullable Object var6);

   @Nullable
   UUID getOwnerUniqueId();

   @Nullable
   default Player getOwner() {
      UUID uniqueId = this.getOwnerUniqueId();
      return uniqueId != null ? Bukkit.getPlayer(uniqueId) : null;
   }

   default boolean hasOwner() {
      return this.getOwnerUniqueId() != null;
   }

   void setOwner(@Nullable UUID var1);

   default void setOwner(@Nullable Player owner) {
      this.setOwner(owner != null ? owner.getUniqueId() : null);
   }

   default boolean isTheOwner(@NotNull UUID playerUniqueId) {
      return Objects.equals(playerUniqueId, this.getOwnerUniqueId());
   }

   default boolean isTheOwner(@NotNull Player player) {
      return this.isTheOwner(player.getUniqueId());
   }

   boolean isIn(@NotNull Entity var1);

   boolean isPassenger(@NotNull Entity var1);

   boolean isOperator(@NotNull Entity var1);

   boolean isPersistent();

   void setPersistent(boolean var1);

   @NotNull
   Map<EnumSurface, Double> getCurrentSurface(@Nullable Collection<EnumSurface> var1);

   @NotNull
   default Map<EnumSurface, Double> getCurrentSurface() {
      return this.getCurrentSurface((Collection)null);
   }

   boolean isOnGround();

   boolean isOnSurfaces(@NotNull EnumSurface... var1);

   boolean isOnAnySurface(@NotNull EnumSurface... var1);

   boolean isOnSurface(@NotNull EnumSurface var1);

   boolean isOnSolidSurface();

   boolean isOnWaterSurface();

   boolean isOnLavaSurface();

   boolean isOnLiquidSurface();

   boolean isInTheAir();

   boolean containedWithin(@NotNull EnumSurface... var1);

   boolean containsAnyWithin(@NotNull EnumSurface... var1);

   @NotNull
   Set<? extends VehicleSeat> getSeats();

   @NotNull
   VehicleSeat getMainSeat();

   @Nullable
   Entity getOperator();

   void setOperator(@Nullable Entity var1);

   @NotNull
   Set<? extends VehicleProjectileShooter> getProjectileShooters();

   boolean hasController(@NotNull VehicleController var1);

   void addController(@NotNull VehicleController var1);

   void removeController(@NotNull VehicleController var1);

   void setState(@NotNull VehicleState var1);

   @NotNull
   Location getLocation();

   double getX();

   double getY();

   double getZ();

   float getRotation();

   void setLocation(double var1, double var3, double var5);

   void setRotation(float var1);

   void setLocationAndRotation(double var1, double var3, double var5, float var7);

   void moveToWorld(@NotNull World var1, double var2, double var4, double var6);

   double getMomentumX();

   double getMomentumY();

   double getMomentumZ();

   void setMomentumX(double var1);

   void setMomentumY(double var1);

   void setMomentumZ(double var1);

   void setMomentum(double var1, double var3, double var5);

   void addMomentumX(double var1);

   void addMomentumY(double var1);

   void addMomentumZ(double var1);

   void addMomentum(double var1, double var3, double var5);

   void input(@NotNull PlayerInput var1);

   void input(@NotNull PlayerSteerInput var1);

   @NotNull
   List<IJoint> getBlockBenchBones();

   Set<VehicleController> getVehicleControllers();

   boolean isMoving();

   void remove();

   void remove(boolean var1);

   void setKey(boolean var1);

   boolean isKeyed();
}
