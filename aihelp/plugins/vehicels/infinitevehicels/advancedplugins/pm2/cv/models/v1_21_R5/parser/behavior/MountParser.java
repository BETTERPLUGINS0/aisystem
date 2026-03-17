package advancedplugins.pm2.cv.models.v1_21_R5.parser.behavior;

import advancedplugins.pm2.cv.models.api.model.rpc.entity.data.IEntityData;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.renderer.BehaviorRendererParser;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.renderer.renderer.MountRenderer;
import advancedplugins.pm2.cv.models.api.utils.math.MathUtils;
import advancedplugins.pm2.cv.models.v1_21_R5.entity.EntityContainer;
import advancedplugins.pm2.cv.models.v1_21_R5.entity.EntityUtils;
import advancedplugins.pm2.cv.models.v1_21_R5.network.utils.NetworkUtils;
import advancedplugins.pm2.cv.models.v1_21_R5.network.utils.Packets;
import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.network.protocol.game.ClientboundSetPassengersPacket;
import net.minecraft.network.protocol.game.ClientboundUpdateAttributesPacket;
import net.minecraft.network.protocol.game.ClientboundMoveEntityPacket.Rot;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData.DataValue;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public class MountParser implements BehaviorRendererParser<MountRenderer> {
   public void sendToClients(MountRenderer var1) {
      IEntityData var2 = var1.getModelRenderer().getActiveModel().getModeledEntity().getBase().getData();
      this.update(var2.getTracking().keySet(), var1);
      this.spawn(var2.getStartTracking(), var1);
      this.remove(var2.getStopTracking(), var1);
   }

   public void destroy(MountRenderer var1) {
      IEntityData var2 = var1.getVisualModel().getModeledEntity().getBase().getData();
      HashSet var3 = new HashSet(var2.getStartTracking());
      var3.addAll(var2.getTracking().keySet());
      var3.addAll(var2.getStopTracking());
      this.remove(var3, var1);
   }

   private void spawn(Set<UUID> var1, MountRenderer var2) {
      if (!var1.isEmpty()) {
         Packets var3 = new Packets();
         Iterator var4 = var2.getRendered().values().iterator();

         MountRenderer.Mount var5;
         while(var4.hasNext()) {
            var5 = (MountRenderer.Mount)var4.next();
            var3.add(this.pivotSpawn(var5));
            var3.add((Packet)this.pivotData(var5));
            var3.add((Packet)this.mountSpawn(var5));
            var3.add((Packet)this.mountData(var5));
            var3.add((Packet)this.pivotMount(var5));
            var3.add((Packet)this.mount(var5));
         }

         var4 = var2.getSpawnQueue().values().iterator();

         while(var4.hasNext()) {
            var5 = (MountRenderer.Mount)var4.next();
            var3.add(this.pivotSpawn(var5));
            var3.add((Packet)this.pivotData(var5));
            var3.add((Packet)this.mountSpawn(var5));
            var3.add((Packet)this.mountData(var5));
            var3.add((Packet)this.pivotMount(var5));
            var3.add((Packet)this.mount(var5));
         }

         NetworkUtils.sendBundled(var1, var3);
      }

   }

   private void update(Set<UUID> var1, MountRenderer var2) {
      if (!var1.isEmpty()) {
         Packets var3 = new Packets();
         Iterator var4 = var2.getRendered().values().iterator();

         MountRenderer.Mount var5;
         while(var4.hasNext()) {
            var5 = (MountRenderer.Mount)var4.next();
            if (var5.getPosition().isDirty()) {
               var3.add(this.pivotMove(var5));
               var5.getPosition().clearDirty();
            }

            if (var5.getYaw().isDirty()) {
               var3.add((Packet)this.mountRotate(var5));
               var5.getYaw().clearDirty();
            }

            if (var5.getHealth().isDirty()) {
               var3.add((Packet)this.mountHealth(var5));
               var5.getHealth().clearDirty();
            }

            if (var5.getMaxHealth().isDirty()) {
               var3.add((Packet)this.mountMaxHealth(var5));
               var5.getMaxHealth().clearDirty();
            }

            if (var5.getPassengers().isDirty()) {
               var3.add((Packet)this.mount(var5));
               var5.getPassengers().clearDirty();
            }
         }

         var4 = var2.getSpawnQueue().values().iterator();

         while(var4.hasNext()) {
            var5 = (MountRenderer.Mount)var4.next();
            var3.add(this.pivotSpawn(var5));
            var3.add((Packet)this.pivotData(var5));
            var3.add((Packet)this.mountSpawn(var5));
            var3.add((Packet)this.mountData(var5));
            var3.add((Packet)this.pivotMount(var5));
            var3.add((Packet)this.mount(var5));
         }

         Map var6 = var2.getDestroyQueue();
         if (!var6.isEmpty()) {
            IntArrayList var7 = new IntArrayList(var6.size() * 2);
            var7.addAll(IntArrayList.toList(var6.values().stream().mapToInt(MountRenderer.Mount::getPivotId)));
            var7.addAll(IntArrayList.toList(var6.values().stream().mapToInt(MountRenderer.Mount::getMountId)));
            var3.add((Packet)(new ClientboundRemoveEntitiesPacket(var7)));
         }

         NetworkUtils.sendBundled(var1, var3);
      }

   }

   private void remove(Set<UUID> var1, MountRenderer var2) {
      if (!var1.isEmpty()) {
         Collection var3 = var2.getRendered().values();
         IntArrayList var4 = new IntArrayList(var3.size() * 2);
         var4.addAll(IntArrayList.toList(var3.stream().mapToInt(MountRenderer.Mount::getPivotId)));
         var4.addAll(IntArrayList.toList(var3.stream().mapToInt(MountRenderer.Mount::getMountId)));
         NetworkUtils.send((Set)var1, new ClientboundRemoveEntitiesPacket(var4));
      }

   }

   private Packets.PacketSupplier pivotSpawn(MountRenderer.Mount var1) {
      return NetworkUtils.createPivotSpawn(var1.getPivotId(), var1.getPivotUuid(), (Vector3f)var1.getPosition().get());
   }

   private ClientboundSetEntityDataPacket pivotData(MountRenderer.Mount var1) {
      return new ClientboundSetEntityDataPacket(var1.getPivotId(), EntityUtils.DEFAULT_AREA_EFFECT_CLOUD_DATA);
   }

   private ClientboundAddEntityPacket mountSpawn(MountRenderer.Mount var1) {
      Vector3f var2 = (Vector3f)var1.getPosition().get();
      return new ClientboundAddEntityPacket(var1.getMountId(), var1.getMountUuid(), (double)var2.x, (double)var2.y, (double)var2.z, 0.0F, MathUtils.byteToRot((Byte)var1.getYaw().get()), EntityType.ARMOR_STAND, 0, Vec3.ZERO, 0.0D);
   }

   private ClientboundSetEntityDataPacket mountData(MountRenderer.Mount var1) {
      return new ClientboundSetEntityDataPacket(var1.getMountId(), EntityUtils.DEFAULT_ARMOR_STAND_DATA);
   }

   private ClientboundSetPassengersPacket pivotMount(MountRenderer.Mount var1) {
      return new ClientboundSetPassengersPacket(EntityContainer.of(var1.getPivotId(), var1.getMountId()));
   }

   private ClientboundSetPassengersPacket mount(MountRenderer.Mount var1) {
      return new ClientboundSetPassengersPacket(EntityContainer.of(var1.getMountId(), (Collection)var1.getPassengers()));
   }

   private Packets.PacketSupplier pivotMove(MountRenderer.Mount var1) {
      return NetworkUtils.createPivotTeleport(var1.getPivotId(), (Vector3f)var1.getPosition().get());
   }

   private Rot mountRotate(MountRenderer.Mount var1) {
      return new Rot(var1.getMountId(), (Byte)var1.getYaw().get(), (byte)0, false);
   }

   private ClientboundSetEntityDataPacket mountHealth(MountRenderer.Mount var1) {
      return new ClientboundSetEntityDataPacket(var1.getMountId(), List.of(new DataValue(9, EntityDataSerializers.FLOAT, (Float)var1.getHealth().get())));
   }

   private ClientboundUpdateAttributesPacket mountMaxHealth(MountRenderer.Mount var1) {
      AttributeInstance var2 = new AttributeInstance(Attributes.MAX_HEALTH, (var0) -> {
      });
      var2.setBaseValue((double)(Float)var1.getMaxHealth().get());
      return new ClientboundUpdateAttributesPacket(var1.getMountId(), Lists.newArrayList(new AttributeInstance[]{var2}));
   }
}
