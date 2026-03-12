package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.chicken;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.AbstractMappedEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.ResourceLocation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import java.util.Objects;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class StaticChickenVariant extends AbstractMappedEntity implements ChickenVariant {
   private final ChickenVariant.ModelType modelType;
   private final ResourceLocation assetId;

   public StaticChickenVariant(ChickenVariant.ModelType modelType, ResourceLocation assetId) {
      this((TypesBuilderData)null, modelType, assetId);
   }

   @ApiStatus.Internal
   public StaticChickenVariant(@Nullable TypesBuilderData data, ChickenVariant.ModelType modelType, ResourceLocation assetId) {
      super(data);
      this.modelType = modelType;
      this.assetId = assetId;
   }

   public ChickenVariant copy(@Nullable TypesBuilderData newData) {
      return new StaticChickenVariant(newData, this.modelType, this.assetId);
   }

   public ChickenVariant.ModelType getModelType() {
      return this.modelType;
   }

   public ResourceLocation getAssetId() {
      return this.assetId;
   }

   public boolean deepEquals(@Nullable Object obj) {
      if (!(obj instanceof StaticChickenVariant)) {
         return false;
      } else {
         StaticChickenVariant that = (StaticChickenVariant)obj;
         return !this.modelType.equals(that.modelType) ? false : this.assetId.equals(that.assetId);
      }
   }

   public int deepHashCode() {
      return Objects.hash(new Object[]{this.modelType, this.assetId});
   }
}
