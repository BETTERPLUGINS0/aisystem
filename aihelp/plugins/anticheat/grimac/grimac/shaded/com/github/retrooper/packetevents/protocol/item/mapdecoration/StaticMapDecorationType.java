package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.mapdecoration;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.AbstractMappedEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.ResourceLocation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;

public class StaticMapDecorationType extends AbstractMappedEntity implements MapDecorationType {
   private final ResourceLocation assetId;
   private final boolean showOnItemFrame;
   private final int mapColor;
   private final boolean explorationMapElement;
   private final boolean trackCount;

   @ApiStatus.Internal
   public StaticMapDecorationType(@Nullable TypesBuilderData data, ResourceLocation assetId, boolean showOnItemFrame, int mapColor, boolean explorationMapElement, boolean trackCount) {
      super(data);
      this.assetId = assetId;
      this.showOnItemFrame = showOnItemFrame;
      this.mapColor = mapColor;
      this.explorationMapElement = explorationMapElement;
      this.trackCount = trackCount;
   }

   public ResourceLocation getAssetId() {
      return this.assetId;
   }

   public boolean isShowOnItemFrame() {
      return this.showOnItemFrame;
   }

   public int getMapColor() {
      return this.mapColor;
   }

   public boolean isExplorationMapElement() {
      return this.explorationMapElement;
   }

   public boolean isTrackCount() {
      return this.trackCount;
   }
}
