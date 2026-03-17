package advancedplugins.pm2.cv.models.v1_21_R5.parser.behavior;

import advancedplugins.pm2.cv.models.api.model.rpc.entity.BaseEntity;
import advancedplugins.pm2.cv.models.api.model.rpc.entity.data.IEntityData;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.renderer.BehaviorRendererParser;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.renderer.renderer.SubHitboxRenderer;
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
import org.joml.Vector3f;

public class SubHitboxParser implements BehaviorRendererParser<SubHitboxRenderer> {
   public void sendToClients(SubHitboxRenderer var1) {
      IEntityData var2 = var1.getModelRenderer().getActiveModel().getModeledEntity().getBase().getData();
      this.update(var2.getTracking().keySet(), var1);
      this.spawn(var2.getStartTracking(), var1);
      this.remove(var2.getStopTracking(), var1);
   }

   public void destroy(SubHitboxRenderer var1) {
      IEntityData var2 = var1.getVisualModel().getModeledEntity().getBase().getData();
      HashSet var3 = new HashSet(var2.getStartTracking());
      var3.addAll(var2.getTracking().keySet());
      var3.addAll(var2.getStopTracking());
      this.remove(var3, var1);
   }

   private void spawn(Set<UUID> var1, SubHitboxRenderer var2) {
      if (!var1.isEmpty()) {
         Packets var3 = new Packets();
         Iterator var4 = var2.getRendered().values().iterator();

         SubHitboxRenderer.SubHitbox var5;
         while(var4.hasNext()) {
            var5 = (SubHitboxRenderer.SubHitbox)var4.next();
            var3.add(this.pivotSpawn(var5));
            var3.add((Packet)this.pivotData(var5));
            var3.add((Packet)this.hitboxSpawn(var5));
            var3.add((Packet)this.hitboxData(var5, true));
            var3.add((Packet)this.pivotMount(var5));
         }

         var4 = var2.getSpawnQueue().values().iterator();

         while(var4.hasNext()) {
            var5 = (SubHitboxRenderer.SubHitbox)var4.next();
            var3.add(this.pivotSpawn(var5));
            var3.add((Packet)this.pivotData(var5));
            var3.add((Packet)this.hitboxSpawn(var5));
            var3.add((Packet)this.hitboxData(var5, true));
            var3.add((Packet)this.pivotMount(var5));
         }

         BaseEntity var6 = var2.getVisualModel().getModeledEntity().getBase();
         NetworkUtils.sendBundled(var1, var3, (var1x) -> {
            return var1x != var6.getOriginal();
         });
      }

   }

   private void update(Set<UUID> var1, SubHitboxRenderer var2) {
      if (!var1.isEmpty()) {
         Packets var3 = new Packets();
         Iterator var4 = var2.getRendered().values().iterator();

         SubHitboxRenderer.SubHitbox var5;
         while(var4.hasNext()) {
            var5 = (SubHitboxRenderer.SubHitbox)var4.next();
            var3.add((Packet)this.hitboxData(var5, false));
            if (var5.getPosition().isDirty()) {
               var3.add(this.pivotMove(var5));
               var5.getPosition().clearDirty();
            }
         }

         var4 = var2.getSpawnQueue().values().iterator();

         while(var4.hasNext()) {
            var5 = (SubHitboxRenderer.SubHitbox)var4.next();
            var3.add(this.pivotSpawn(var5));
            var3.add((Packet)this.pivotData(var5));
            var3.add((Packet)this.hitboxSpawn(var5));
            var3.add((Packet)this.hitboxData(var5, true));
            var3.add((Packet)this.pivotMount(var5));
         }

         Map var6 = var2.getDestroyQueue();
         if (!var6.isEmpty()) {
            IntArrayList var7 = new IntArrayList(var6.size() * 2);
            var7.addAll(IntArrayList.toList(var6.values().stream().mapToInt(SubHitboxRenderer.SubHitbox::getPivotId)));
            var7.addAll(IntArrayList.toList(var6.values().stream().mapToInt(SubHitboxRenderer.SubHitbox::getHitboxId)));
            var3.add((Packet)(new ClientboundRemoveEntitiesPacket(var7)));
         }

         NetworkUtils.sendBundled(var1, var3);
      }

   }

   private void remove(Set<UUID> var1, SubHitboxRenderer var2) {
      if (!var1.isEmpty()) {
         Collection var3 = var2.getRendered().values();
         IntArrayList var4 = new IntArrayList(var3.size() * 2);
         var4.addAll(IntArrayList.toList(var3.stream().mapToInt(SubHitboxRenderer.SubHitbox::getPivotId)));
         var4.addAll(IntArrayList.toList(var3.stream().mapToInt(SubHitboxRenderer.SubHitbox::getHitboxId)));
         NetworkUtils.send((Set)var1, new ClientboundRemoveEntitiesPacket(var4));
      }

   }

   private Packets.PacketSupplier pivotSpawn(SubHitboxRenderer.SubHitbox var1) {
      return NetworkUtils.createPivotSpawn(var1.getPivotId(), var1.getPivotUuid(), (Vector3f)var1.getPosition().get());
   }

   private ClientboundSetEntityDataPacket pivotData(SubHitboxRenderer.SubHitbox var1) {
      return new ClientboundSetEntityDataPacket(var1.getPivotId(), EntityUtils.DEFAULT_AREA_EFFECT_CLOUD_DATA);
   }

   private Packets.PacketSupplier pivotMove(SubHitboxRenderer.SubHitbox var1) {
      return NetworkUtils.createPivotTeleport(var1.getPivotId(), (Vector3f)var1.getPosition().get());
   }

   private ClientboundSetPassengersPacket pivotMount(SubHitboxRenderer.SubHitbox var1) {
      return new ClientboundSetPassengersPacket(EntityContainer.of(var1.getPivotId(), var1.getHitboxId()));
   }

   private ClientboundAddEntityPacket hitboxSpawn(SubHitboxRenderer.SubHitbox var1) {
      Vector3f var2 = (Vector3f)var1.getPosition().get();
      return new ClientboundAddEntityPacket(var1.getHitboxId(), var1.getHitboxUuid(), (double)var2.x, (double)var2.y, (double)var2.z, 0.0F, 0.0F, EntityType.INTERACTION, 0, Vec3.ZERO, 0.0D);
   }

   private ClientboundSetEntityDataPacket hitboxData(SubHitboxRenderer.SubHitbox var1, boolean var2) {
      if (!var2 && !var1.isDirty()) {
         return null;
      } else {
         ArrayList var3 = new ArrayList(4);
         if (var2) {
            var3.add(new DataValue(1, EntityDataSerializers.INT, Integer.MAX_VALUE));
            var3.add(new DataValue(10, EntityDataSerializers.BOOLEAN, false));
         }

         var1.getWidth().ifDirty((var1x) -> {
            var3.add(new DataValue(8, EntityDataSerializers.FLOAT, var1x));
         }, var2);
         var1.getHeight().ifDirty((var1x) -> {
            var3.add(new DataValue(9, EntityDataSerializers.FLOAT, var1x));
         }, var2);
         var1.clearDirty();
         return new ClientboundSetEntityDataPacket(var1.getHitboxId(), var3);
      }
   }
}
