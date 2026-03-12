package ac.grim.grimac.platform.api.entity;

import ac.grim.grimac.api.GrimIdentity;
import ac.grim.grimac.platform.api.world.PlatformWorld;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.utils.math.Location;
import java.util.concurrent.CompletableFuture;

public interface GrimEntity extends GrimIdentity {
   boolean eject();

   CompletableFuture<Boolean> teleportAsync(Location var1);

   @NotNull
   Object getNative();

   boolean isDead();

   PlatformWorld getWorld();

   Location getLocation();

   double distanceSquared(double var1, double var3, double var5);
}
