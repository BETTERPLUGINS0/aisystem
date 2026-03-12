package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.frog;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.AbstractMappedEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.ResourceLocation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import java.util.Objects;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class StaticFrogVariant extends AbstractMappedEntity implements FrogVariant {
   private final ResourceLocation assetId;

   public StaticFrogVariant(ResourceLocation assetId) {
      this((TypesBuilderData)null, assetId);
   }

   @ApiStatus.Internal
   public StaticFrogVariant(@Nullable TypesBuilderData data, ResourceLocation assetId) {
      super(data);
      this.assetId = assetId;
   }

   public FrogVariant copy(@Nullable TypesBuilderData newData) {
      return new StaticFrogVariant(newData, this.assetId);
   }

   public ResourceLocation getAssetId() {
      return this.assetId;
   }

   public boolean deepEquals(@Nullable Object obj) {
      if (!(obj instanceof StaticFrogVariant)) {
         return false;
      } else {
         StaticFrogVariant that = (StaticFrogVariant)obj;
         return this.assetId.equals(that.assetId);
      }
   }

   public int deepHashCode() {
      return Objects.hash(new Object[]{this.assetId});
   }
}
