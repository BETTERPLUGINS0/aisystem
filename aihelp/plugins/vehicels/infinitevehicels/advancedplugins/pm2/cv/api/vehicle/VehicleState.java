package advancedplugins.pm2.cv.api.vehicle;

import com.google.common.base.Preconditions;
import gnu.trove.map.hash.THashMap;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;

public final class VehicleState {
   private static final Map<String, VehicleState> REGISTRY = new THashMap();
   public static final VehicleState IDLE = new VehicleState("idle", "Vehicle is not moving/turning", true);
   public static final VehicleState MOVING = new VehicleState("moving", "Vehicle is just moving", true);
   public static final VehicleState MOVING_BACKWARDS = new VehicleState("moving_backwards", "Vehicle is just moving", true);
   public static final VehicleState INCREASING_HEIGHT = new VehicleState("increasing_height", "Vehicle is increasing height", true);
   public static final VehicleState DECREASING_HEIGHT = new VehicleState("decreasing_height", "Vehicle is decreasing height", true);
   public static final VehicleState TURNING_LEFT = new VehicleState("turning_left", "Vehicle is just turning to the left", true);
   public static final VehicleState TURNING_RIGHT = new VehicleState("turning_right", "Vehicle is just turning to the right", true);
   public static final VehicleState MOVING_TURNING_LEFT = new VehicleState("moving_turning_left", "Vehicle is moving and turning to the left at the same time", true);
   public static final VehicleState MOVING_TURNING_RIGHT = new VehicleState("moving_turning_right", "Vehicle is moving and turning to the right at the same time", true);
   public static final VehicleState MOVING_BACKWARDS_TURNING_LEFT = new VehicleState("moving_backwards_turning_left", "Vehicle is moving and turning to the left at the same time", true);
   public static final VehicleState MOVING_BACKWARDS_TURNING_RIGHT = new VehicleState("moving_backwards_turning_right", "Vehicle is moving and turning to the right at the same time", true);
   public static final VehicleState INCREASING_HEIGHT_TURNING_LEFT = new VehicleState("increasing_height_turning_left", "Vehicle is increasing height and turning to the left at the same time", true);
   public static final VehicleState INCREASING_HEIGHT_TURNING_RIGHT = new VehicleState("increasing_height_turning_right", "Vehicle is increasing height and turning to the right at the same time", true);
   public static final VehicleState DECREASING_HEIGHT_TURNING_LEFT = new VehicleState("decreasing_height_turning_left", "Vehicle is decreasing height and turning to the left at the same time", true);
   public static final VehicleState DECREASING_HEIGHT_TURNING_RIGHT = new VehicleState("decreasing_height_turning_right", "Vehicle is decreasing height and turning to the right at the same time", true);
   @NotNull
   private final String name;
   private final boolean predefined;
   @NotNull
   private final String description;

   public static Collection<VehicleState> getValues() {
      return Collections.unmodifiableCollection(REGISTRY.values());
   }

   public static void register(@NotNull VehicleState var0) {
      VehicleState var1 = (VehicleState)REGISTRY.get(var0.getName());
      if (var1 != null && var1.isPredefined()) {
         throw new IllegalArgumentException("cannot override a pre-defined state");
      } else {
         REGISTRY.put(var0.getName(), var0);
      }
   }

   public static void unregister(@NotNull VehicleState var0) {
      if (var0.isPredefined()) {
         throw new IllegalArgumentException("cannot unregister override a pre-defined");
      } else {
         REGISTRY.remove(var0.getName());
      }
   }

   public VehicleState(@NotNull String var1, @NotNull String var2) {
      this(var1, var2, false);
   }

   private VehicleState(@NotNull String var1, @NotNull String var2, boolean var3) {
      Preconditions.checkArgument(StringUtils.isNotBlank(var1), "name cannot be blank");
      Preconditions.checkArgument(StringUtils.isNotBlank(var2), "description cannot be blank");
      Preconditions.checkArgument(var1.matches("[a-z0-9/._-]+"), "invalid name. must match: [a-z0-9/._-]+");
      this.name = var1;
      this.description = var2;
      this.predefined = var3;
   }

   @NotNull
   public String getName() {
      return this.name;
   }

   @NotNull
   public String getDescription() {
      return this.description;
   }

   public boolean isPredefined() {
      return this.predefined;
   }

   public String toString() {
      return "VehicleState{name='" + this.name + "'description='" + this.description + "'predefined='" + this.predefined + "'}";
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (var1 != null && this.getClass() == var1.getClass()) {
         VehicleState var2 = (VehicleState)var1;
         return this.name.equals(var2.name);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return this.name.hashCode();
   }

   static {
      Field[] var0 = VehicleState.class.getFields();
      int var1 = var0.length;

      for(int var2 = 0; var2 < var1; ++var2) {
         Field var3 = var0[var2];
         if (Modifier.isStatic(var3.getModifiers()) && VehicleState.class.isAssignableFrom(var3.getType())) {
            try {
               VehicleState var4 = (VehicleState)var3.get((Object)null);
               REGISTRY.put(var4.getName(), var4);
            } catch (IllegalAccessException var5) {
               throw new IllegalStateException(var5);
            }
         }
      }

   }
}
