package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.ResourceLocation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3i;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;

public class WorldBlockPosition {
   public static final ResourceLocation OVERWORLD_DIMENSION = ResourceLocation.minecraft("overworld");
   private ResourceLocation world;
   private Vector3i blockPosition;

   public WorldBlockPosition(@NotNull ResourceLocation world, @NotNull Vector3i blockPosition) {
      this.world = world;
      this.blockPosition = blockPosition;
   }

   public WorldBlockPosition(@NotNull ResourceLocation world, int x, int y, int z) {
      this.world = world;
      this.blockPosition = new Vector3i(x, y, z);
   }

   /** @deprecated */
   @Deprecated
   public WorldBlockPosition(@NotNull Dimension dimension, @NotNull Vector3i blockPosition) {
      this(new ResourceLocation(dimension.getDimensionName()), blockPosition);
   }

   public WorldBlockPosition(@NotNull ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.dimension.DimensionType dimensionType, @NotNull Vector3i blockPosition) {
      this(dimensionType.getName(), blockPosition);
   }

   public ResourceLocation getWorld() {
      return this.world;
   }

   public void setWorld(ResourceLocation world) {
      this.world = world;
   }

   public Vector3i getBlockPosition() {
      return this.blockPosition;
   }

   public void setBlockPosition(Vector3i blockPosition) {
      this.blockPosition = blockPosition;
   }
}
