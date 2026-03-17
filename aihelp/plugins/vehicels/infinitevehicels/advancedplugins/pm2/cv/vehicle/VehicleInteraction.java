package advancedplugins.pm2.cv.vehicle;

import advancedplugins.pm2.cv.InfiniteVehiclesPlugin;
import advancedplugins.pm2.cv.api.InfiniteVehicles;
import advancedplugins.pm2.cv.api.player.PlayerWrapper;
import advancedplugins.pm2.cv.api.util.Run;
import advancedplugins.pm2.cv.api.util.SpamUtil;
import advancedplugins.pm2.cv.api.vehicle.configuration.model.VehicleHitBoxConfiguration;
import advancedplugins.pm2.cv.api.vehicle.configuration.model.VehicleSeatConfiguration;
import advancedplugins.pm2.cv.api.vehicle.input.PlayerInput;
import advancedplugins.pm2.cv.enums.EnumStandProperty;
import advancedplugins.pm2.cv.fake.FakeEntity;
import advancedplugins.pm2.cv.fake.armorstand.FakeArmorStand;
import com.google.common.collect.Maps;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import me.PM2.infinitevehicles.math.geometry.euclidean.threed.Vector3D;
import me.PM2.infinitevehicles.math.util.FastMath;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class VehicleInteraction {
   private static final double FAR_AWAY = 8.0D;
   private static final double GAP = 0.35D;
   final VehicleImpl vehicle;
   final Map<UUID, FakeArmorStand> handleMap = Maps.newConcurrentMap();
   final double range;
   private final Map<Player, PlayerInput.InputType> primaryBindings = new ConcurrentHashMap();
   private final Map<Player, PlayerInput.InputType> secondaryBindings = new ConcurrentHashMap();

   public VehicleInteraction(VehicleImpl vehicle) {
      this.vehicle = var1;
      VehicleHitBoxConfiguration var2 = var1.getConfiguration().model().getHitBox();
      double var3 = var2.getWidth();
      double var5 = var2.getHeight();
      double var7 = var2.getDepth();
      double var9 = FastMath.max(FastMath.max(var3, var5), var7);
      Iterator var11 = var1.getConfiguration().model().getSeats().iterator();

      while(var11.hasNext()) {
         VehicleSeatConfiguration var12 = (VehicleSeatConfiguration)var11.next();
         Vector3D var13 = var12.getOffset();
         double var14 = var13.distance(Vector3D.ZERO);
         if (var14 > var9) {
            var9 = var14;
         }
      }

      this.range = var9;
   }

   public PlayerInput.InputType getPrimaryBinding(@NotNull Player player) {
      return (PlayerInput.InputType)this.primaryBindings.getOrDefault(var1, (Object)null);
   }

   public PlayerInput.InputType getSecondaryBinding(@NotNull Player player) {
      return (PlayerInput.InputType)this.secondaryBindings.getOrDefault(var1, (Object)null);
   }

   public void addPrimaryBinding(@NotNull Player player, @NotNull PlayerInput.InputType input) {
      if (!this.hasInteractionCooldown(var1, "primary") || !this.multiKeyProjectiles()) {
         this.primaryBindings.put(var1, var2);
         Run.syncDelayed(() -> {
            this.addInteractionCooldown(var1, "primary", 10L);
         }, 1L);
         this.tryAndExecuteProjectiles(var1);
      }
   }

   public void addSecondaryBinding(@NotNull Player player, @NotNull PlayerInput.InputType input) {
      if (this.hasInteractionCooldown(var1, "primary")) {
         this.secondaryBindings.put(var1, var2);
         this.tryAndExecuteProjectiles(var1);
         this.removeInteractionCooldown(var1, "primary");
      }
   }

   private void removeInteractionCooldown(Player player, String id) {
      String var10000 = String.valueOf(var1.getUniqueId());
      SpamUtil.removeSpam(var10000 + "_spam_" + var2);
   }

   private void addInteractionCooldown(Player player, String id, long duration) {
      if (!this.hasInteractionCooldown(var1, var2)) {
         SpamUtil.addSpam(String.valueOf(var1.getUniqueId()) + "_spam_" + var2, var3);
      }
   }

   private boolean hasInteractionCooldown(Player player, String id) {
      String var10000 = String.valueOf(var1.getUniqueId());
      return SpamUtil.isSpam(var10000 + "_spam_" + var2);
   }

   private void tryAndExecuteProjectiles(Player player) {
      PlayerInput.InputType var2 = this.getPrimaryBinding(var1);
      PlayerInput.InputType var3 = this.getSecondaryBinding(var1);
      if (var2 != null) {
         this.vehicle.getProjectileShooters().forEach((var3x) -> {
            boolean var4 = var3x.getBind() == var2;
            boolean var5 = var3x.getSecondaryBind() == var3 || var3x.getSecondaryBind() == null || var3x.getSecondaryBind() == PlayerInput.InputType.NONE;
            if (var4 && var5) {
               if (var3x.getCooldown() != 0) {
                  String var10000 = String.valueOf(var1.getUniqueId());
                  if (SpamUtil.isSpam(var10000 + "_projectile_shoot_cooldown_" + var3x.hashCode())) {
                     return;
                  }
               }

               SpamUtil.addSpam(String.valueOf(var1.getUniqueId()) + "_projectile_shoot_cooldown_" + var3x.hashCode(), (long)var3x.getCooldown());
               var3x.shoot();
            }

         });
      }
   }

   private boolean multiKeyProjectiles() {
      return this.vehicle.getProjectileShooters().stream().anyMatch((var0) -> {
         return var0.getBind() != null && var0.getBind() != PlayerInput.InputType.NONE && var0.getSecondaryBind() != null && var0.getSecondaryBind() != PlayerInput.InputType.NONE;
      });
   }

   void add(@NotNull UUID playerId) {
      if (!this.handleMap.containsKey(var1)) {
         FakeArmorStand var2 = new FakeArmorStand(this.vehicle.getWorld());
         var2.setLocation(this.vehicle.getX(), this.vehicle.getY(), this.vehicle.getZ());
         var2.setProperty(EnumStandProperty.VISIBILITY, false);
         var2.setProperty(EnumStandProperty.GRAVITY, false);
         var2.setProperty(EnumStandProperty.SILENT, true);
         var2.register();
         this.handleMap.put(var1, var2);
      }
   }

   void add(@NotNull Player player) {
      this.add(var1.getUniqueId());
   }

   void remove(@NotNull Player player) {
      FakeArmorStand var2 = (FakeArmorStand)this.handleMap.remove(var1.getUniqueId());
      if (var2 != null) {
         var2.unregister();
      }

   }

   void clear() {
      this.handleMap.values().forEach(FakeEntity::unregister);
      this.handleMap.clear();
   }

   void setWorld(@NotNull World world) {
      this.handleMap.values().forEach(FakeEntity::unregister);
      this.handleMap.clear();
      var1.getPlayers().forEach(this::add);
   }

   void processVehicleLocationChanged() {
      Iterator var1 = this.handleMap.entrySet().iterator();

      while(var1.hasNext()) {
         Entry var2 = (Entry)var1.next();
         PlayerWrapper var3 = InfiniteVehicles.getPlayerWrapperHandler().getWrapper((UUID)var2.getKey());
         Player var4 = var3.get();
         FakeArmorStand var5 = (FakeArmorStand)var2.getValue();
         if (var4 != null) {
            this.relocateHandle(var4, var5);
         }
      }

   }

   void processPlayerLocationChanged(@NotNull Player player, @Nullable Location playerLocationOverride) {
      FakeArmorStand var3 = (FakeArmorStand)this.handleMap.get(var1.getUniqueId());
      if (var3 != null) {
         this.relocateHandle(var1, var3, var2);
      }

   }

   void relocateHandle(@NotNull Player player, @NotNull FakeArmorStand handle) {
      this.relocateHandle(var1, var2, (Location)null);
   }

   void relocateHandle(@NotNull Player player, @NotNull FakeArmorStand handle, @Nullable Location playerLocationOverride) {
      Location var4 = this.vehicle.getLocation();
      Location var5 = var3 != null ? var3 : var1.getEyeLocation();
      double var6 = this.range / 2.0D + 8.0D;
      if (!(var4.toVector().distanceSquared(var5.toVector()) > var6 * var6)) {
         try {
            Vector var8 = var4.toVector();
            Vector var9 = var5.toVector();
            Vector var10 = var9.clone().subtract(var8).normalize();
            double var11 = FastMath.min(var8.distance(var9), this.range) - 0.35D;
            double var13 = var8.getX() + var10.getX() * var11;
            double var15 = var8.getY() + var10.getY() * var11 - 0.65D;
            double var17 = var8.getZ() + var10.getZ() * var11;
            var2.setLocation(var13, var15, var17, true);
         } catch (Exception var19) {
         }

      }
   }

   void processPlayerWorldChanged(@NotNull Player player, @NotNull World world) {
      if (Objects.equals(var2, this.vehicle.getWorld())) {
         this.add(var1);
      } else {
         this.remove(var1);
      }

   }

   boolean isClicked(@NotNull Player player, int entityId) {
      FakeArmorStand var3 = (FakeArmorStand)this.handleMap.get(var1.getUniqueId());
      int var4 = -1;
      if (InfiniteVehiclesPlugin.getInstance().getVehicleRightClickWorkaroundTask().getArmorStands().containsKey(var1.getUniqueId())) {
         var4 = ((ArmorStand)InfiniteVehiclesPlugin.getInstance().getVehicleRightClickWorkaroundTask().getArmorStands().get(var1.getUniqueId())).getEntityId();
      }

      return var3 != null && (var3.getId() == var2 || var4 != -1 && var4 == var2);
   }
}
