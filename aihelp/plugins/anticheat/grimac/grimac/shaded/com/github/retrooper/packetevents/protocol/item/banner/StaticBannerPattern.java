package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.banner;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.AbstractMappedEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.ResourceLocation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import java.util.Objects;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class StaticBannerPattern extends AbstractMappedEntity implements BannerPattern {
   private final ResourceLocation assetId;
   private final String translationKey;

   public StaticBannerPattern(ResourceLocation assetId, String translationKey) {
      this((TypesBuilderData)null, assetId, translationKey);
   }

   @ApiStatus.Internal
   public StaticBannerPattern(@Nullable TypesBuilderData data, ResourceLocation assetId, String translationKey) {
      super(data);
      this.assetId = assetId;
      this.translationKey = translationKey;
   }

   public BannerPattern copy(@Nullable TypesBuilderData newData) {
      return new StaticBannerPattern(newData, this.assetId, this.translationKey);
   }

   public ResourceLocation getAssetId() {
      return this.assetId;
   }

   public String getTranslationKey() {
      return this.translationKey;
   }

   public boolean deepEquals(@Nullable Object obj) {
      if (this == obj) {
         return true;
      } else if (!(obj instanceof StaticBannerPattern)) {
         return false;
      } else {
         StaticBannerPattern that = (StaticBannerPattern)obj;
         return !this.assetId.equals(that.assetId) ? false : this.translationKey.equals(that.translationKey);
      }
   }

   public int deepHashCode() {
      return Objects.hash(new Object[]{this.assetId, this.translationKey});
   }

   public String toString() {
      return "StaticBannerPattern{assetId=" + this.assetId + ", translationKey='" + this.translationKey + '\'' + '}';
   }
}
