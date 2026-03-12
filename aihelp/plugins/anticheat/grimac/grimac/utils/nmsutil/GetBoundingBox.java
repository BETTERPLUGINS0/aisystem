package ac.grim.grimac.utils.nmsutil;

import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.attribute.Attributes;
import ac.grim.grimac.shaded.jetbrains.annotations.Contract;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.utils.collisions.datatypes.SimpleCollisionBox;
import ac.grim.grimac.utils.data.packetentity.PacketEntity;
import lombok.Generated;

public final class GetBoundingBox {
   public static SimpleCollisionBox getCollisionBoxForPlayer(@NotNull GrimPlayer player, double centerX, double centerY, double centerZ) {
      return player.inVehicle() ? getPacketEntityBoundingBox(player, centerX, centerY, centerZ, player.compensatedEntities.self.getRiding()) : getPlayerBoundingBox(player, centerX, centerY, centerZ);
   }

   @NotNull
   public static SimpleCollisionBox getPacketEntityBoundingBox(GrimPlayer player, double centerX, double minY, double centerZ, PacketEntity entity) {
      float width = BoundingBoxSize.getWidth(player, entity);
      float height = BoundingBoxSize.getHeight(player, entity);
      return getBoundingBoxFromPosAndSize(entity, centerX, minY, centerZ, width, height);
   }

   @NotNull
   public static SimpleCollisionBox getPlayerBoundingBox(@NotNull GrimPlayer player, double centerX, double minY, double centerZ) {
      float width = player.pose.width;
      float height = player.pose.height;
      return getBoundingBoxFromPosAndSize(player, centerX, minY, centerZ, width, height);
   }

   @NotNull
   public static SimpleCollisionBox getBoundingBoxFromPosAndSize(@NotNull GrimPlayer player, double centerX, double minY, double centerZ, float width, float height) {
      return getBoundingBoxFromPosAndSize((PacketEntity)player.compensatedEntities.self, centerX, minY, centerZ, width, height);
   }

   @NotNull
   public static SimpleCollisionBox getBoundingBoxFromPosAndSize(@NotNull PacketEntity entity, double centerX, double minY, double centerZ, float width, float height) {
      float scale = (float)entity.getAttributeValue(Attributes.SCALE);
      return getBoundingBoxFromPosAndSizeRaw(centerX, minY, centerZ, width * scale, height * scale);
   }

   @Contract("_, _, _, _, _ -> new")
   @NotNull
   public static SimpleCollisionBox getBoundingBoxFromPosAndSizeRaw(double centerX, double minY, double centerZ, float width, float height) {
      double minX = centerX - (double)(width / 2.0F);
      double maxX = centerX + (double)(width / 2.0F);
      double maxY = minY + (double)height;
      double minZ = centerZ - (double)(width / 2.0F);
      double maxZ = centerZ + (double)(width / 2.0F);
      return new SimpleCollisionBox(Math.min(minX, maxX), Math.min(minY, maxY), Math.min(minZ, maxZ), Math.max(minX, maxX), Math.max(minY, maxY), Math.max(minZ, maxZ), false);
   }

   @NotNull
   public static double[] getEntityDimensions(GrimPlayer player, @NotNull PacketEntity entity) {
      float scale = (float)entity.getAttributeValue(Attributes.SCALE);
      float width = BoundingBoxSize.getWidth(player, entity) * scale;
      float height = BoundingBoxSize.getHeight(player, entity) * scale;
      return new double[]{(double)width, (double)height, (double)width};
   }

   public static void expandBoundingBoxByEntityDimensions(@NotNull SimpleCollisionBox box, GrimPlayer player, PacketEntity entity) {
      double[] dimensions = getEntityDimensions(player, entity);
      double halfWidth = dimensions[0] / 2.0D;
      double height = dimensions[1];
      double halfDepth = dimensions[2] / 2.0D;
      double minX = box.minX - halfWidth;
      double minY = box.minY;
      double minZ = box.minZ - halfDepth;
      double maxX = box.maxX + halfWidth;
      double maxY = box.maxY + height;
      double maxZ = box.maxZ + halfDepth;
      box.minX = Math.min(minX, maxX);
      box.minY = Math.min(minY, maxY);
      box.minZ = Math.min(minZ, maxZ);
      box.maxX = Math.max(minX, maxX);
      box.maxY = Math.max(minY, maxY);
      box.maxZ = Math.max(minZ, maxZ);
   }

   @Generated
   private GetBoundingBox() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }
}
