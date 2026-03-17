package advancedplugins.pm2.cv.models.v1_21_R5.parser.visual;

import advancedplugins.pm2.cv.models.api.model.rpc.entity.data.IEntityData;
import advancedplugins.pm2.cv.models.api.model.rpc.visual.renderer.VisualDisplayRenderer;
import advancedplugins.pm2.cv.models.api.model.rpc.visual.renderer.VisualRendererParser;
import advancedplugins.pm2.cv.models.v1_21_R5.entity.EntityContainer;
import advancedplugins.pm2.cv.models.v1_21_R5.entity.EntityUtils;
import advancedplugins.pm2.cv.models.v1_21_R5.network.utils.NetworkUtils;
import advancedplugins.pm2.cv.models.v1_21_R5.network.utils.Packets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.network.protocol.game.ClientboundSetPassengersPacket;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData.DataValue;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.phys.Vec3;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class VisualDisplayParser implements VisualRendererParser<VisualDisplayRenderer> {
   public void dispatch(VisualDisplayRenderer var1) {
      IEntityData var2 = var1.getVisual().getOriginal().getData();
      if (var1.isRespawnRequired()) {
         this.spawn(var2.getTracking().keySet(), var1.getVisualModel());
         var1.setRespawnRequired(false);
      } else {
         this.update(var2.getTracking().keySet(), var1.getVisualModel());
         this.spawn(var2.getStartTracking(), var1.getVisualModel());
         this.remove(var2.getStopTracking(), var1.getVisualModel());
      }

   }

   public void dispose(VisualDisplayRenderer var1) {
      IEntityData var2 = var1.getVisual().getOriginal().getData();
      HashSet var3 = new HashSet(var2.getStartTracking());
      var3.addAll(var2.getTracking().keySet());
      var3.addAll(var2.getStopTracking());
      this.remove(var3, var1.getVisualModel());
   }

   public void spawn(Set<UUID> var1, VisualDisplayRenderer.RendererVisualModel var2) {
      if (!var1.isEmpty()) {
         Packets var3 = new Packets();
         var3.add(this.pivotSpawn(var2));
         var3.add((Packet)this.pivotData(var2));
         var3.add((Packet)this.vfxSpawn(var2));
         var3.add((Packet)this.vfxData(var2, true));
         var3.add((Packet)this.mount(var2));
         NetworkUtils.sendBundled(var1, var3);
      }

   }

   public void update(Set<UUID> var1, VisualDisplayRenderer.RendererVisualModel var2) {
      if (!var1.isEmpty()) {
         Packets var3 = new Packets();
         var3.add(this.teleport(var2));
         var3.add((Packet)this.vfxData(var2, false));
         NetworkUtils.sendBundled(var1, var3);
      }

   }

   public void remove(Set<UUID> var1, VisualDisplayRenderer.RendererVisualModel var2) {
      if (!var1.isEmpty()) {
         NetworkUtils.send((Set)var1, new ClientboundRemoveEntitiesPacket(new int[]{var2.getPivotId(), var2.getModelId()}));
      }

   }

   private Packets.PacketSupplier pivotSpawn(VisualDisplayRenderer.RendererVisualModel var1) {
      return NetworkUtils.createPivotSpawn(var1.getPivotId(), var1.getPivotUuid(), (Vector3f)var1.getOrigin().get());
   }

   private ClientboundSetEntityDataPacket pivotData(VisualDisplayRenderer.RendererVisualModel var1) {
      return new ClientboundSetEntityDataPacket(var1.getPivotId(), EntityUtils.DEFAULT_AREA_EFFECT_CLOUD_DATA);
   }

   private ClientboundAddEntityPacket vfxSpawn(VisualDisplayRenderer.RendererVisualModel var1) {
      Vector3f var2 = (Vector3f)var1.getOrigin().get();
      return new ClientboundAddEntityPacket(var1.getModelId(), var1.getModelUuid(), (double)var2.x, (double)var2.y, (double)var2.z, 0.0F, 0.0F, EntityType.ITEM_DISPLAY, 0, Vec3.ZERO, 0.0D);
   }

   private ClientboundSetEntityDataPacket vfxData(VisualDisplayRenderer.RendererVisualModel var1, boolean var2) {
      if (!var2 && !var1.isModelDirty()) {
         return null;
      } else {
         ArrayList var3 = new ArrayList(9);
         RegistryFriendlyByteBuf var4 = NetworkUtils.createByteBuf();
         var4.writeVarInt(var1.getModelId());
         if (var2) {
            var3.add(new DataValue(0, EntityDataSerializers.BYTE, (byte)32));
            var3.add(new DataValue(1, EntityDataSerializers.INT, Integer.MAX_VALUE));
            var3.add(new DataValue(8, EntityDataSerializers.INT, 0));
            var3.add(new DataValue(9, EntityDataSerializers.INT, 1));
            var3.add(new DataValue(17, EntityDataSerializers.FLOAT, 4096.0F));
         } else if (var1.isModelDirty()) {
            var3.add(new DataValue(8, EntityDataSerializers.INT, 0));
         }

         var1.getPosition().ifDirty((var1x) -> {
            var3.add(new DataValue(11, EntityDataSerializers.VECTOR3, var1x));
         }, var2);
         var1.getScale().ifDirty((var1x) -> {
            var3.add(new DataValue(12, EntityDataSerializers.VECTOR3, var1x));
         }, var2);
         var1.getLeftRotation().ifDirty((var1x) -> {
            var3.add(new DataValue(13, EntityDataSerializers.QUATERNION, var1x.rotateY(3.1415927F, new Quaternionf())));
         }, var2);
         var1.getModel().ifDirty((var1x) -> {
            var3.add(new DataValue(23, EntityDataSerializers.ITEM_STACK, CraftItemStack.asNMSCopy(var1x)));
         }, var2);
         var1.clearModelDirty();
         return new ClientboundSetEntityDataPacket(var1.getModelId(), var3);
      }
   }

   private Packets.PacketSupplier teleport(VisualDisplayRenderer.RendererVisualModel var1) {
      return !var1.getOrigin().isDirty() ? null : NetworkUtils.createPivotTeleport(var1.getPivotId(), (Vector3f)var1.getOrigin().get());
   }

   private ClientboundSetPassengersPacket mount(VisualDisplayRenderer.RendererVisualModel var1) {
      RegistryFriendlyByteBuf var2 = NetworkUtils.createByteBuf();
      var2.writeVarInt(var1.getPivotId());
      var2.writeVarInt(1);
      var2.writeVarInt(var1.getModelId());
      return new ClientboundSetPassengersPacket(EntityContainer.of(var1.getPivotId(), var1.getModelId()));
   }
}
