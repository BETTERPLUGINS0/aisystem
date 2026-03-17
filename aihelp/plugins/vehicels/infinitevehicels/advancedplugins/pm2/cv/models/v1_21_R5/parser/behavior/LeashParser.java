package advancedplugins.pm2.cv.models.v1_21_R5.parser.behavior;

import advancedplugins.pm2.cv.models.api.model.rpc.entity.data.IEntityData;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.renderer.BehaviorRendererParser;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.renderer.renderer.LeashRenderer;
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
import net.minecraft.network.protocol.game.ClientboundSetEntityLinkPacket;
import net.minecraft.network.protocol.game.ClientboundSetPassengersPacket;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public class LeashParser implements BehaviorRendererParser<LeashRenderer> {
   public void sendToClients(LeashRenderer var1) {
      IEntityData var2 = var1.getModelRenderer().getActiveModel().getModeledEntity().getBase().getData();
      this.update(var2.getTracking().keySet(), var1);
      this.spawn(var2.getStartTracking(), var1);
      HashSet var3 = new HashSet(var2.getStopTracking());
      var3.removeAll(var2.getTracking().keySet());
      this.remove(var3, var1);
   }

   public void destroy(LeashRenderer var1) {
      IEntityData var2 = var1.getVisualModel().getModeledEntity().getBase().getData();
      HashSet var3 = new HashSet(var2.getStartTracking());
      var3.addAll(var2.getTracking().keySet());
      var3.addAll(var2.getStopTracking());
      this.remove(var3, var1);
   }

   private void spawn(Set<UUID> var1, LeashRenderer var2) {
      if (!var1.isEmpty()) {
         Packets var3 = new Packets();
         Iterator var4 = var2.getRendered().values().iterator();

         LeashRenderer.Leash var5;
         while(var4.hasNext()) {
            var5 = (LeashRenderer.Leash)var4.next();
            var3.add(this.pivotSpawn(var5));
            var3.add((Packet)this.pivotData(var5));
            var3.add((Packet)this.leashSpawn(var5));
            var3.add((Packet)this.leashData(var5));
            var3.add((Packet)this.mount(var5));
            var3.add((Packet)this.link(var5, true));
         }

         var4 = var2.getSpawnQueue().values().iterator();

         while(var4.hasNext()) {
            var5 = (LeashRenderer.Leash)var4.next();
            var3.add(this.pivotSpawn(var5));
            var3.add((Packet)this.pivotData(var5));
            var3.add((Packet)this.leashSpawn(var5));
            var3.add((Packet)this.leashData(var5));
            var3.add((Packet)this.mount(var5));
            var3.add((Packet)this.link(var5, true));
         }

         NetworkUtils.sendBundled(var1, var3);
      }

   }

   private void update(Set<UUID> var1, LeashRenderer var2) {
      if (!var1.isEmpty()) {
         Packets var3 = new Packets();
         Iterator var4 = var2.getRendered().values().iterator();

         LeashRenderer.Leash var5;
         while(var4.hasNext()) {
            var5 = (LeashRenderer.Leash)var4.next();
            var3.add((Packet)this.link(var5, false));
            var3.add(this.move(var5));
         }

         var4 = var2.getSpawnQueue().values().iterator();

         while(var4.hasNext()) {
            var5 = (LeashRenderer.Leash)var4.next();
            var3.add(this.pivotSpawn(var5));
            var3.add((Packet)this.pivotData(var5));
            var3.add((Packet)this.leashSpawn(var5));
            var3.add((Packet)this.leashData(var5));
            var3.add((Packet)this.mount(var5));
            var3.add((Packet)this.link(var5, true));
         }

         Map var6 = var2.getDestroyQueue();
         if (!var6.isEmpty()) {
            var3.add((Packet)(new ClientboundRemoveEntitiesPacket(IntArrayList.toList(var6.values().stream().mapMultiToInt((var0, var1x) -> {
               var1x.accept(var0.getLeashId());
               var1x.accept(var0.getPivotId());
            })))));
         }

         NetworkUtils.sendBundled(var1, var3);
      }

   }

   private void remove(Set<UUID> var1, LeashRenderer var2) {
      if (!var1.isEmpty()) {
         Map var3 = var2.getRendered();
         NetworkUtils.send((Set)var1, new ClientboundRemoveEntitiesPacket(IntArrayList.toList(var3.values().stream().mapMultiToInt((var0, var1x) -> {
            var1x.accept(var0.getLeashId());
            var1x.accept(var0.getPivotId());
         }))));
      }

   }

   private Packets.PacketSupplier pivotSpawn(LeashRenderer.Leash var1) {
      Vector3f var2 = (Vector3f)var1.getPosition().get();
      return NetworkUtils.createPivotSpawn(var1.getPivotId(), var1.getPivotUUID(), var2.add(0.0F, -0.45F, -0.2F, new Vector3f()));
   }

   private ClientboundAddEntityPacket leashSpawn(LeashRenderer.Leash var1) {
      Vector3f var2 = (Vector3f)var1.getPosition().get();
      return new ClientboundAddEntityPacket(var1.getLeashId(), var1.getLeastUUID(), (double)var2.x, (double)var2.y - 0.45D, (double)var2.z - 0.2D, 0.0F, 0.0F, EntityType.BAT, 0, Vec3.ZERO, 0.0D);
   }

   private ClientboundSetEntityDataPacket pivotData(LeashRenderer.Leash var1) {
      return new ClientboundSetEntityDataPacket(var1.getPivotId(), EntityUtils.DEFAULT_AREA_EFFECT_CLOUD_DATA);
   }

   private ClientboundSetEntityDataPacket leashData(LeashRenderer.Leash var1) {
      return new ClientboundSetEntityDataPacket(var1.getLeashId(), EntityUtils.DEFAULT_BAT_DATA);
   }

   private ClientboundSetPassengersPacket mount(LeashRenderer.Leash var1) {
      ArrayList var2 = new ArrayList(1);
      var2.add(var1.getLeashId());
      return new ClientboundSetPassengersPacket(EntityContainer.of(var1.getPivotId(), (Collection)var2));
   }

   private Packets.PacketSupplier move(LeashRenderer.Leash var1) {
      if (!var1.getPosition().isDirty()) {
         return null;
      } else {
         Vector3f var2 = (Vector3f)var1.getPosition().get();
         return NetworkUtils.createPivotTeleport(var1.getPivotId(), var2.add(0.0F, -0.45F, -0.2F, new Vector3f()));
      }
   }

   private ClientboundSetEntityLinkPacket link(LeashRenderer.Leash var1, boolean var2) {
      if (!var2 && !var1.getConnected().isDirty()) {
         return null;
      } else {
         var1.getConnected().clearDirty();
         return new ClientboundSetEntityLinkPacket(EntityContainer.of(var1.getLeashId()), EntityContainer.of((Integer)var1.getConnected().get()));
      }
   }
}
