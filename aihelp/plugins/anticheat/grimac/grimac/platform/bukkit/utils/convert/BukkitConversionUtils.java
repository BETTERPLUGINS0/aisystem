package ac.grim.grimac.platform.bukkit.utils.convert;

import ac.grim.grimac.platform.api.permissions.PermissionDefaultValue;
import ac.grim.grimac.platform.bukkit.world.BukkitPlatformWorld;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.BlockFace;
import ac.grim.grimac.shaded.jetbrains.annotations.Contract;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import org.bukkit.Location;
import org.bukkit.permissions.PermissionDefault;

public class BukkitConversionUtils {
   @Contract("null -> null; !null -> new")
   public static Location toBukkitLocation(ac.grim.grimac.utils.math.Location location) {
      return location == null ? null : new Location(((BukkitPlatformWorld)location.getWorld()).getBukkitWorld(), location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
   }

   @Contract(
      value = "null -> null; !null -> !null",
      pure = true
   )
   @Nullable
   public static PermissionDefault toBukkitPermissionDefault(@Nullable PermissionDefaultValue permissionDefaultValue) {
      if (permissionDefaultValue == null) {
         return null;
      } else {
         PermissionDefault var10000;
         switch(permissionDefaultValue) {
         case TRUE:
            var10000 = PermissionDefault.TRUE;
            break;
         case FALSE:
            var10000 = PermissionDefault.FALSE;
            break;
         case OP:
            var10000 = PermissionDefault.OP;
            break;
         case NOT_OP:
            var10000 = PermissionDefault.NOT_OP;
            break;
         default:
            throw new IncompatibleClassChangeError();
         }

         return var10000;
      }
   }

   public static BlockFace fromBukkitFace(org.bukkit.block.BlockFace face) {
      BlockFace var10000;
      switch(face) {
      case NORTH:
         var10000 = BlockFace.NORTH;
         break;
      case SOUTH:
         var10000 = BlockFace.SOUTH;
         break;
      case WEST:
         var10000 = BlockFace.WEST;
         break;
      case EAST:
         var10000 = BlockFace.EAST;
         break;
      case UP:
         var10000 = BlockFace.UP;
         break;
      case DOWN:
         var10000 = BlockFace.DOWN;
         break;
      default:
         var10000 = BlockFace.OTHER;
      }

      return var10000;
   }
}
