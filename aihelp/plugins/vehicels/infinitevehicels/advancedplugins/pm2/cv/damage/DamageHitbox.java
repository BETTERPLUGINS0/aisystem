package advancedplugins.pm2.cv.damage;

import advancedplugins.pm2.cv.api.enums.EnumDamageType;
import advancedplugins.pm2.cv.api.interfaces.VersionSensible;
import advancedplugins.pm2.cv.api.vehicle.Vehicle;
import advancedplugins.pm2.cv.nms.NmsImplementations;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@VersionSensible
public interface DamageHitbox {
   float DEFAULT_WIDTH = 0.15F;
   float DEFAULT_HEIGHT = 0.15F;
   String IDENTIFIER_KEY = "crafty-vehicles-damage-hitbox-handle";

   static DamageHitbox create(@NotNull World world) {
      Class implementation = NmsImplementations.getImplementation(DamageHitbox.class);

      try {
         Constructor<?> constructor = implementation.getConstructor(World.class);
         return (DamageHitbox)constructor.newInstance(world);
      } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException var3) {
         throw new IllegalStateException("couldn't create damage hitbox", var3);
      }
   }

   int getHandleId();

   void setVehicle(@NotNull Vehicle vehicle);

   void setListener(@Nullable DamageHitbox.DamageListener listener);

   void spawn();

   void destroy();

   Vector getLocation();

   void setLocation(double x, double y, double z);

   default void setLocation(@NotNull Vector location) {
      this.setLocation(location.getX(), location.getY(), location.getZ());
   }

   void setOrientation(float orientation);

   float getWidth();

   float getHeight();

   void setWidth(float width);

   void setHeight(float height);

   public interface DamageListener {
      void notify(@Nullable EnumDamageType type, float damage, @Nullable Entity causing, @Nullable Entity direct, @NotNull Entity victim);
   }
}
