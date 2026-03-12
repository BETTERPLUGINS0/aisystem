package ac.grim.grimac.shaded.incendo.cloud.bukkit.parser.location;

import java.util.Locale;
import java.util.Objects;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class LocationCoordinate {
   private final LocationCoordinateType type;
   private final double coordinate;

   private LocationCoordinate(@NonNull final LocationCoordinateType type, final double coordinate) {
      this.type = type;
      this.coordinate = coordinate;
   }

   @NonNull
   public static LocationCoordinate of(@NonNull final LocationCoordinateType type, final double coordinate) {
      return new LocationCoordinate(type, coordinate);
   }

   @NonNull
   public LocationCoordinateType type() {
      return this.type;
   }

   public double coordinate() {
      return this.coordinate;
   }

   public boolean equals(final Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         LocationCoordinate that = (LocationCoordinate)o;
         return Double.compare(that.coordinate, this.coordinate) == 0 && this.type == that.type;
      } else {
         return false;
      }
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.type, this.coordinate});
   }

   public String toString() {
      return String.format("LocationCoordinate{type=%s, coordinate=%f}", this.type.name().toLowerCase(Locale.ROOT), this.coordinate);
   }
}
