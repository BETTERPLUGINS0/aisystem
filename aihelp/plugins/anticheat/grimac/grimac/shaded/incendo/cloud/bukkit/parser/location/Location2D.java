package ac.grim.grimac.shaded.incendo.cloud.bukkit.parser.location;

import org.bukkit.Location;
import org.bukkit.World;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public class Location2D extends Location {
   protected Location2D(@Nullable final World world, final double x, final double z) {
      super(world, x, 0.0D, z);
   }

   @NonNull
   public static Location2D from(@Nullable final World world, final double x, final double z) {
      return new Location2D(world, x, z);
   }
}
