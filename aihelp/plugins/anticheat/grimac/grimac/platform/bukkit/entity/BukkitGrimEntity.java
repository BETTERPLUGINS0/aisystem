package ac.grim.grimac.platform.bukkit.entity;

import ac.grim.grimac.platform.api.entity.GrimEntity;
import ac.grim.grimac.platform.api.world.PlatformWorld;
import ac.grim.grimac.platform.bukkit.utils.convert.BukkitConversionUtils;
import ac.grim.grimac.platform.bukkit.utils.reflection.PaperUtils;
import ac.grim.grimac.platform.bukkit.world.BukkitPlatformWorld;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.utils.math.Location;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import org.bukkit.entity.Entity;

public class BukkitGrimEntity implements GrimEntity {
   protected static final boolean CAN_USE_DIRECT_GETTERS;
   private final Entity entity;
   private BukkitPlatformWorld bukkitPlatformWorld;

   public BukkitGrimEntity(Entity entity) {
      this.entity = (Entity)Objects.requireNonNull(entity);
   }

   public Entity getBukkitEntity() {
      return this.entity;
   }

   public UUID getUniqueId() {
      return this.entity.getUniqueId();
   }

   public boolean eject() {
      return this.entity.eject();
   }

   public CompletableFuture<Boolean> teleportAsync(Location location) {
      org.bukkit.Location bLoc = BukkitConversionUtils.toBukkitLocation(location);
      return PaperUtils.teleportAsync(this.entity, bLoc);
   }

   @NotNull
   public Entity getNative() {
      return this.entity;
   }

   public boolean isDead() {
      return this.entity.isDead();
   }

   public PlatformWorld getWorld() {
      if (this.bukkitPlatformWorld == null || !this.bukkitPlatformWorld.getBukkitWorld().equals(this.entity.getWorld())) {
         this.bukkitPlatformWorld = new BukkitPlatformWorld(this.entity.getWorld());
      }

      return this.bukkitPlatformWorld;
   }

   public Location getLocation() {
      org.bukkit.Location location = this.entity.getLocation();
      return new Location(this.getWorld(), location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
   }

   public double distanceSquared(double oX, double oY, double oZ) {
      if (CAN_USE_DIRECT_GETTERS) {
         double x = this.entity.getX();
         double y = this.entity.getY();
         double z = this.entity.getZ();
         double distX = (x - oX) * (x - oX);
         double distY = (y - oY) * (y - oY);
         double distZ = (z - oZ) * (z - oZ);
         return distX + distY + distZ;
      } else {
         org.bukkit.Location location = this.entity.getLocation();
         double x = location.getX();
         double y = location.getY();
         double z = location.getZ();
         double distX = (x - oX) * (x - oX);
         double distY = (y - oY) * (y - oY);
         double distZ = (z - oZ) * (z - oZ);
         return distX + distY + distZ;
      }
   }

   static {
      CAN_USE_DIRECT_GETTERS = PacketEvents.getAPI().getServerManager().getVersion().isNewerThan(ServerVersion.V_1_20_1) && PaperUtils.PAPER;
   }
}
