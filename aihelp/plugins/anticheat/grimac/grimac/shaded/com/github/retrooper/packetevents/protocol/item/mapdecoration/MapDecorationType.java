package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.mapdecoration;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.MappedEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.ResourceLocation;

public interface MapDecorationType extends MappedEntity {
   ResourceLocation getAssetId();

   boolean isShowOnItemFrame();

   int getMapColor();

   boolean isExplorationMapElement();

   boolean isTrackCount();
}
