package ac.grim.grimac.shaded.com.github.retrooper.packetevents.util;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.Equipment;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.chunk.LightData;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerChunkData;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerDestroyEntities;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityEquipment;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSetCursorItem;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSetPlayerInventory;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSetSlot;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerUpdateLight;
import java.util.Collections;

public class PacketTransformationUtil {
   public static PacketWrapper<?>[] transform(PacketWrapper<?> wrapper) {
      int len;
      PacketWrapper[] output;
      int i;
      if (wrapper instanceof WrapperPlayServerDestroyEntities) {
         WrapperPlayServerDestroyEntities destroyEntities = (WrapperPlayServerDestroyEntities)wrapper;
         len = destroyEntities.getEntityIds().length;
         if (((PacketWrapper)wrapper).getServerVersion() == ServerVersion.V_1_17 && len > 1) {
            output = new PacketWrapper[len];

            for(i = 0; i < len; ++i) {
               int entityId = destroyEntities.getEntityIds()[i];
               output[i] = new WrapperPlayServerDestroyEntities(entityId);
            }

            return output;
         }
      } else if (wrapper instanceof WrapperPlayServerEntityEquipment) {
         WrapperPlayServerEntityEquipment entityEquipment = (WrapperPlayServerEntityEquipment)wrapper;
         len = entityEquipment.getEquipment().size();
         if (entityEquipment.getServerVersion().isOlderThan(ServerVersion.V_1_16) && len > 1) {
            output = new PacketWrapper[len];

            for(i = 0; i < len; ++i) {
               Equipment equipment = (Equipment)entityEquipment.getEquipment().get(i);
               output[i] = new WrapperPlayServerEntityEquipment(entityEquipment.getEntityId(), Collections.singletonList(equipment));
            }

            return output;
         }
      } else if (wrapper instanceof WrapperPlayServerChunkData) {
         WrapperPlayServerChunkData chunkData = (WrapperPlayServerChunkData)wrapper;
         LightData lightData = chunkData.getLightData();
         if (chunkData.getServerVersion().isOlderThan(ServerVersion.V_1_18) && lightData != null) {
            output = new PacketWrapper[]{new WrapperPlayServerUpdateLight(chunkData.getColumn().getX(), chunkData.getColumn().getZ(), lightData), chunkData};
            return output;
         }
      } else if (wrapper instanceof WrapperPlayServerSetSlot) {
         WrapperPlayServerSetSlot setSlot = (WrapperPlayServerSetSlot)wrapper;
         if (setSlot.getSlot() == -1) {
            if (((PacketWrapper)wrapper).getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_21_2)) {
               wrapper = new WrapperPlayServerSetCursorItem(setSlot.getItem());
            }
         } else if (setSlot.getWindowId() == -2 && ((PacketWrapper)wrapper).getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_21_2)) {
            wrapper = new WrapperPlayServerSetPlayerInventory(setSlot.getSlot(), setSlot.getItem());
         }
      }

      return new PacketWrapper[]{(PacketWrapper)wrapper};
   }
}
