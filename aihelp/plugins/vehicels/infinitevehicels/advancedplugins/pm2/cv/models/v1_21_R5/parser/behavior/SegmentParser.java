package advancedplugins.pm2.cv.models.v1_21_R5.parser.behavior;

import advancedplugins.pm2.cv.models.api.model.rpc.entity.data.IEntityData;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.renderer.BehaviorRendererParser;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.renderer.renderer.SegmentRenderer;
import advancedplugins.pm2.cv.models.v1_21_R5.entity.EntityContainer;
import advancedplugins.pm2.cv.models.v1_21_R5.entity.EntityUtils;
import advancedplugins.pm2.cv.models.v1_21_R5.network.utils.NetworkUtils;
import advancedplugins.pm2.cv.models.v1_21_R5.network.utils.Packets;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
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

public class SegmentParser implements BehaviorRendererParser<SegmentRenderer> {
   private final Set<Runnable> cleanupQueue = new HashSet();

   public void sendToClients(SegmentRenderer var1) {
      IEntityData var2 = var1.getModelRenderer().getActiveModel().getModeledEntity().getBase().getData();
      this.update(var2.getTracking().keySet(), var1);
      this.spawn(var2.getStartTracking(), var1);
      this.remove(var2.getStopTracking(), var1);
      this.cleanupQueue.forEach(Runnable::run);
      this.cleanupQueue.clear();
   }

   public void destroy(SegmentRenderer var1) {
      IEntityData var2 = var1.getVisualModel().getModeledEntity().getBase().getData();
      HashSet var3 = new HashSet(var2.getStartTracking());
      var3.addAll(var2.getTracking().keySet());
      var3.addAll(var2.getStopTracking());
      this.remove(var3, var1);
   }

   private void spawn(Packets var1, Map<String, SegmentRenderer.Pivot> var2) {
      Iterator var3 = var2.values().iterator();

      while(var3.hasNext()) {
         SegmentRenderer.Pivot var4 = (SegmentRenderer.Pivot)var3.next();
         var1.add(this.pivotSpawn(var4));
         var1.add((Packet)this.pivotData(var4));
         Iterator var5 = var4.getRendered().values().iterator();

         SegmentRenderer.Joint var6;
         while(var5.hasNext()) {
            var6 = (SegmentRenderer.Joint)var5.next();
            var1.add((Packet)this.displaySpawn(var4, var6));
            var1.add((Packet)this.displayData(var6, true, false));
         }

         var5 = var4.getSpawnQueue().values().iterator();

         while(var5.hasNext()) {
            var6 = (SegmentRenderer.Joint)var5.next();
            var1.add((Packet)this.displaySpawn(var4, var6));
            var1.add((Packet)this.displayData(var6, true, false));
         }

         var1.add((Packet)this.pivotMount(var4));
         Set var7 = this.cleanupQueue;
         Objects.requireNonNull(var4);
         Objects.requireNonNull(var4);
         var7.add(var4::clearDirty);
      }

   }

   private void spawn(Set<UUID> var1, SegmentRenderer var2) {
      if (!var1.isEmpty()) {
         Packets var3 = new Packets();
         this.spawn(var3, var2.getRendered());
         this.spawn(var3, var2.getSpawnQueue());
         NetworkUtils.sendBundled(var1, var3);
      }

   }

   private void update(Set<UUID> var1, SegmentRenderer var2) {
      if (!var1.isEmpty()) {
         Packets var3 = new Packets();
         IntArrayList var4 = new IntArrayList();
         Iterator var5 = var2.getRendered().values().iterator();

         while(var5.hasNext()) {
            SegmentRenderer.Pivot var6 = (SegmentRenderer.Pivot)var5.next();
            var3.add(this.pivotTeleport(var6));
            var6.getRendered().values().forEach((var2x) -> {
               var3.add((Packet)this.displayData(var2x, false, false));
            });
            var6.getSpawnQueue().values().forEach((var3x) -> {
               var3.add((Packet)this.displaySpawn(var6, var3x));
               var3.add((Packet)this.displayData(var3x, true, false));
            });
            var6.getDestroyQueue().values().forEach((var1x) -> {
               var4.add(var1x.getId());
            });
            if (var6.getPassengers().isDirty()) {
               var3.add((Packet)this.pivotMount(var6));
            }

            Set var7 = this.cleanupQueue;
            Objects.requireNonNull(var6);
            Objects.requireNonNull(var6);
            var7.add(var6::clearDirty);
         }

         this.spawn(var3, var2.getSpawnQueue());
         var2.getDestroyQueue().forEach((var1x, var2x) -> {
            var4.add(var2x.getId());
            var4.addAll(var2x.getPassengers());
         });
         if (!var4.isEmpty()) {
            var3.add((Packet)(new ClientboundRemoveEntitiesPacket(var4)));
         }

         NetworkUtils.sendBundled(var1, var3);
      }

   }

   private void remove(Set<UUID> var1, SegmentRenderer var2) {
      if (!var1.isEmpty()) {
         Collection var3 = var2.getRendered().values();
         IntArrayList var4 = new IntArrayList();
         var3.forEach((var1x) -> {
            var4.add(var1x.getId());
            var4.addAll(var1x.getPassengers());
         });
         NetworkUtils.send((Set)var1, new ClientboundRemoveEntitiesPacket(var4));
      }

   }

   private Packets.PacketSupplier pivotSpawn(SegmentRenderer.Pivot var1) {
      return NetworkUtils.createPivotSpawn(var1.getId(), var1.getUuid(), (Vector3f)var1.getPosition().get());
   }

   private ClientboundSetEntityDataPacket pivotData(SegmentRenderer.Pivot var1) {
      return new ClientboundSetEntityDataPacket(var1.getId(), EntityUtils.DEFAULT_AREA_EFFECT_CLOUD_DATA);
   }

   private ClientboundSetPassengersPacket pivotMount(SegmentRenderer.Pivot var1) {
      return new ClientboundSetPassengersPacket(EntityContainer.of(var1.getId(), (Collection)var1.getPassengers()));
   }

   private Packets.PacketSupplier pivotTeleport(SegmentRenderer.Pivot var1) {
      if (!var1.getPosition().isDirty()) {
         return null;
      } else {
         this.cleanupQueue.add(() -> {
            var1.getPosition().clearDirty();
         });
         return NetworkUtils.createPivotTeleport(var1.getId(), (Vector3f)var1.getPosition().get());
      }
   }

   private ClientboundAddEntityPacket displaySpawn(SegmentRenderer.Pivot var1, SegmentRenderer.Joint var2) {
      Vector3f var3 = (Vector3f)var1.getPosition().get();
      return new ClientboundAddEntityPacket(var2.getId(), var2.getUuid(), (double)var3.x, (double)var3.y, (double)var3.z, 0.0F, 0.0F, EntityType.ITEM_DISPLAY, 0, Vec3.ZERO, 0.0D);
   }

   private ClientboundSetEntityDataPacket displayData(SegmentRenderer.Joint var1, boolean var2, boolean var3) {
      if (!var2 && !var3 && !var1.isDirty()) {
         return null;
      } else {
         ArrayList var4 = new ArrayList(13);
         if (var2) {
            var4.add(new DataValue(1, EntityDataSerializers.INT, Integer.MAX_VALUE));
            var4.add(new DataValue(8, EntityDataSerializers.INT, 0));
         } else if (var1.isTransformDirty() || var3) {
            var4.add(new DataValue(8, EntityDataSerializers.INT, 0));
         }

         var1.getStep().ifDirty((var1x) -> {
            var4.add(new DataValue(9, EntityDataSerializers.INT, var1x ? 0 : 1));
         }, var2 || var3);
         var1.getGlowing().ifDirty((var1x) -> {
            var4.add(new DataValue(0, EntityDataSerializers.BYTE, (byte)(var1x ? 96 : 32)));
         }, var2 || var3);
         var1.getGlowColor().ifDirty((var1x) -> {
            var4.add(new DataValue(22, EntityDataSerializers.INT, var1x));
         }, var2 || var3);
         var1.getBrightness().ifDirty((var1x) -> {
            var4.add(new DataValue(16, EntityDataSerializers.INT, var1x));
         }, var2 || var3);
         var1.getPosition().ifDirty((var1x) -> {
            var4.add(new DataValue(11, EntityDataSerializers.VECTOR3, var1x));
         }, var2 || var3);
         var1.getScale().ifDirty((var1x) -> {
            var4.add(new DataValue(12, EntityDataSerializers.VECTOR3, var1x));
         }, var2 || var3);
         var1.getLeftRotation().ifDirty((var1x) -> {
            var4.add(new DataValue(13, EntityDataSerializers.QUATERNION, var1x.rotateY(3.1415927F, new Quaternionf())));
         }, var2 || var3);
         var1.getRightRotation().ifDirty((var1x) -> {
            var4.add(new DataValue(14, EntityDataSerializers.QUATERNION, var1x));
         }, var2 || var3);
         var1.getVisibility().ifDirty((var1x) -> {
            var4.add(new DataValue(17, EntityDataSerializers.FLOAT, var1x ? 4096.0F : 0.0F));
         }, var2 || var3);
         var1.getModel().ifDirty((var1x) -> {
            var4.add(new DataValue(23, EntityDataSerializers.ITEM_STACK, CraftItemStack.asNMSCopy(var1x)));
         }, var2 || var3);
         var1.getDisplay().ifDirty((var1x) -> {
            var4.add(new DataValue(24, EntityDataSerializers.BYTE, var1x == null ? 0 : (byte)var1x.ordinal()));
         }, var2 || var3);
         Set var5 = this.cleanupQueue;
         Objects.requireNonNull(var1);
         Objects.requireNonNull(var1);
         var5.add(var1::clearDirty);
         return new ClientboundSetEntityDataPacket(var1.getId(), var4);
      }
   }
}
