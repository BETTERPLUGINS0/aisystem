package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.cow;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.AbstractMappedEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.ResourceLocation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import java.util.Objects;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class StaticCowVariant extends AbstractMappedEntity implements CowVariant {
   private final CowVariant.ModelType modelType;
   private final ResourceLocation assetId;

   public StaticCowVariant(CowVariant.ModelType modelType, ResourceLocation assetId) {
      this((TypesBuilderData)null, modelType, assetId);
   }

   @ApiStatus.Internal
   public StaticCowVariant(@Nullable TypesBuilderData data, CowVariant.ModelType modelType, ResourceLocation assetId) {
      super(data);
      this.modelType = modelType;
      this.assetId = assetId;
   }

   public CowVariant copy(@Nullable TypesBuilderData newData) {
      return new StaticCowVariant(newData, this.modelType, this.assetId);
   }

   public CowVariant.ModelType getModelType() {
      return this.modelType;
   }

   public ResourceLocation getAssetId() {
      return this.assetId;
   }

   public boolean deepEquals(@Nullable Object obj) {
      if (!(obj instanceof StaticCowVariant)) {
         return false;
      } else {
         StaticCowVariant that = (StaticCowVariant)obj;
         return !this.modelType.equals(that.modelType) ? false : this.assetId.equals(that.assetId);
      }
   }

   public int deepHashCode() {
      return Objects.hash(new Object[]{this.modelType, this.assetId});
   }
}
