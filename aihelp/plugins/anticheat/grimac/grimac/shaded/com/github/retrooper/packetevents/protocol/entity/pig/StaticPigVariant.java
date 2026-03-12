package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.pig;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.AbstractMappedEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.ResourceLocation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import java.util.Objects;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class StaticPigVariant extends AbstractMappedEntity implements PigVariant {
   private final PigVariant.ModelType modelType;
   private final ResourceLocation assetId;

   public StaticPigVariant(PigVariant.ModelType modelType, ResourceLocation assetId) {
      this((TypesBuilderData)null, modelType, assetId);
   }

   @ApiStatus.Internal
   public StaticPigVariant(@Nullable TypesBuilderData data, PigVariant.ModelType modelType, ResourceLocation assetId) {
      super(data);
      this.modelType = modelType;
      this.assetId = assetId;
   }

   public PigVariant copy(@Nullable TypesBuilderData newData) {
      return new StaticPigVariant(newData, this.modelType, this.assetId);
   }

   public PigVariant.ModelType getModelType() {
      return this.modelType;
   }

   public ResourceLocation getAssetId() {
      return this.assetId;
   }

   public boolean deepEquals(@Nullable Object obj) {
      if (!(obj instanceof StaticPigVariant)) {
         return false;
      } else {
         StaticPigVariant that = (StaticPigVariant)obj;
         return !this.modelType.equals(that.modelType) ? false : this.assetId.equals(that.assetId);
      }
   }

   public int deepHashCode() {
      return Objects.hash(new Object[]{this.modelType, this.assetId});
   }
}
