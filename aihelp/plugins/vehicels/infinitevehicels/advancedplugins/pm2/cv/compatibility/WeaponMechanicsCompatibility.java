package advancedplugins.pm2.cv.compatibility;

import advancedplugins.pm2.cv.api.InfiniteVehicles;
import advancedplugins.pm2.cv.api.enums.EnumDamageType;
import advancedplugins.pm2.cv.api.handler.VehicleHandler;
import advancedplugins.pm2.cv.api.vehicle.Vehicle;
import java.util.Iterator;
import java.util.Objects;
import me.deecaad.weaponmechanics.weapon.weaponevents.WeaponDamageEntityEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class WeaponMechanicsCompatibility implements Listener {
   public WeaponMechanicsCompatibility(JavaPlugin plugin) {
      Bukkit.getPluginManager().registerEvents(this, var1);
   }

   @EventHandler(
      priority = EventPriority.LOWEST
   )
   public void onDamage(WeaponDamageEntityEvent event) {
      Vehicle var2 = this.matchVehicle(var1.getVictim());
      if (var2 != null) {
         var2.damage(EnumDamageType.WEAPONS_MECHANICS, (float)var1.getFinalDamage(), var1.getEntity(), (Entity)null, var1.getVictim(), var1.getWeaponTitle());
      }

   }

   @Nullable
   private Vehicle matchVehicle(@NotNull Entity damagedEntity) {
      Iterator var2 = ((VehicleHandler)Objects.requireNonNull((VehicleHandler)InfiniteVehicles.getHandler(VehicleHandler.class))).getRegisteredVehicles().iterator();

      Vehicle var3;
      Integer var4;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (Vehicle)var2.next();
         var4 = var3.getCurrentHitBoxEntityId();
      } while(var4 == null || var4 != var1.getEntityId());

      return var3;
   }
}
