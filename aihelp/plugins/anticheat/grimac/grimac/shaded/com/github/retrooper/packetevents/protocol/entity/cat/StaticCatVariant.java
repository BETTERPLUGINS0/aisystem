package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.cat;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.AbstractMappedEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.ResourceLocation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import java.util.Objects;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class StaticCatVariant extends AbstractMappedEntity implements CatVariant {
   private final ResourceLocation assetId;

   public StaticCatVariant(ResourceLocation assetId) {
      this((TypesBuilderData)null, assetId);
   }

   @ApiStatus.Internal
   public StaticCatVariant(@Nullable TypesBuilderData data, ResourceLocation assetId) {
      super(data);
      this.assetId = assetId;
   }

   public CatVariant copy(@Nullable TypesBuilderData newData) {
      return new StaticCatVariant(newData, this.assetId);
   }

   public ResourceLocation getAssetId() {
      return this.assetId;
   }

   public boolean deepEquals(@Nullable Object obj) {
      if (!(obj instanceof StaticCatVariant)) {
         return false;
      } else {
         StaticCatVariant that = (StaticCatVariant)obj;
         return this.assetId.equals(that.assetId);
      }
   }

   public int deepHashCode() {
      return Objects.hash(new Object[]{this.assetId});
   }
}
