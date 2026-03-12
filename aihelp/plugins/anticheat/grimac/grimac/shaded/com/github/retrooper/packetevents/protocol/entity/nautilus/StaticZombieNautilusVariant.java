package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.nautilus;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.AbstractMappedEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.ResourceLocation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import java.util.Objects;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class StaticZombieNautilusVariant extends AbstractMappedEntity implements ZombieNautilusVariant {
   private final ZombieNautilusVariant.ModelType modelType;
   private final ResourceLocation assetId;

   public StaticZombieNautilusVariant(ZombieNautilusVariant.ModelType modelType, ResourceLocation assetId) {
      this((TypesBuilderData)null, modelType, assetId);
   }

   @ApiStatus.Internal
   public StaticZombieNautilusVariant(@Nullable TypesBuilderData data, ZombieNautilusVariant.ModelType modelType, ResourceLocation assetId) {
      super(data);
      this.modelType = modelType;
      this.assetId = assetId;
   }

   public ZombieNautilusVariant copy(@Nullable TypesBuilderData newData) {
      return new StaticZombieNautilusVariant(newData, this.modelType, this.assetId);
   }

   public ZombieNautilusVariant.ModelType getModelType() {
      return this.modelType;
   }

   public ResourceLocation getAssetId() {
      return this.assetId;
   }

   public boolean deepEquals(@Nullable Object obj) {
      if (obj != null && this.getClass() == obj.getClass()) {
         StaticZombieNautilusVariant that = (StaticZombieNautilusVariant)obj;
         return this.modelType != that.modelType ? false : this.assetId.equals(that.assetId);
      } else {
         return false;
      }
   }

   public int deepHashCode() {
      return Objects.hash(new Object[]{this.modelType, this.assetId});
   }
}
