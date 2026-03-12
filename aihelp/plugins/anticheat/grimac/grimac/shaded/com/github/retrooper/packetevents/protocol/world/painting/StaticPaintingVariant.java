package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.painting;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.AbstractMappedEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.ResourceLocation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import java.util.Objects;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class StaticPaintingVariant extends AbstractMappedEntity implements PaintingVariant {
   private final int width;
   private final int height;
   private final ResourceLocation assetId;

   public StaticPaintingVariant(int width, int height, ResourceLocation assetId) {
      this((TypesBuilderData)null, width, height, assetId);
   }

   @ApiStatus.Internal
   public StaticPaintingVariant(@Nullable TypesBuilderData data, int width, int height, ResourceLocation assetId) {
      super(data);
      this.width = width;
      this.height = height;
      this.assetId = assetId;
   }

   public PaintingVariant copy(@Nullable TypesBuilderData newData) {
      return new StaticPaintingVariant(newData, this.width, this.height, this.assetId);
   }

   public int getWidth() {
      return this.width;
   }

   public int getHeight() {
      return this.height;
   }

   public ResourceLocation getAssetId() {
      return this.assetId;
   }

   public boolean deepEquals(@Nullable Object obj) {
      if (this == obj) {
         return true;
      } else if (!(obj instanceof StaticPaintingVariant)) {
         return false;
      } else {
         StaticPaintingVariant that = (StaticPaintingVariant)obj;
         if (this.width != that.width) {
            return false;
         } else {
            return this.height != that.height ? false : this.assetId.equals(that.assetId);
         }
      }
   }

   public int deepHashCode() {
      return Objects.hash(new Object[]{this.width, this.height, this.assetId});
   }

   public String toString() {
      return "StaticPaintingVariant{width=" + this.width + ", height=" + this.height + ", assetId=" + this.assetId + '}';
   }
}
