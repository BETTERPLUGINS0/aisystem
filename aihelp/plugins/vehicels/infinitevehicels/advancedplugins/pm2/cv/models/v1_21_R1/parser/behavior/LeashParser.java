package advancedplugins.pm2.cv.models.v1_21_R1.parser.behavior;

import advancedplugins.pm2.cv.models.api.model.rpc.entity.data.IEntityData;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.renderer.BehaviorRendererParser;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.renderer.renderer.LeashRenderer;
import advancedplugins.pm2.cv.models.v1_21_R1.entity.EntityContainer;
import advancedplugins.pm2.cv.models.v1_21_R1.entity.EntityUtils;
import advancedplugins.pm2.cv.models.v1_21_R1.network.utils.NetworkUtils;
import advancedplugins.pm2.cv.models.v1_21_R1.network.utils.Packets;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.PacketPlayOutAttachEntity;
import net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy;
import net.minecraft.network.protocol.game.PacketPlayOutEntityMetadata;
import net.minecraft.network.protocol.game.PacketPlayOutMount;
import net.minecraft.network.protocol.game.PacketPlayOutSpawnEntity;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.phys.Vec3D;
import org.joml.Vector3f;

public class LeashParser implements BehaviorRendererParser<LeashRenderer> {
   public void sendToClients(LeashRenderer renderer) {
      IEntityData var2 = var1.getModelRenderer().getActiveModel().getModeledEntity().getBase().getData();
      this.update(var2.getTracking().keySet(), var1);
      this.spawn(var2.getStartTracking(), var1);
      HashSet var3 = new HashSet(var2.getStopTracking());
      var3.removeAll(var2.getTracking().keySet());
      this.remove(var3, var1);
   }

   public void destroy(LeashRenderer renderer) {
      IEntityData var2 = var1.getVisualModel().getModeledEntity().getBase().getData();
      HashSet var3 = new HashSet(var2.getStartTracking());
      var3.addAll(var2.getTracking().keySet());
      var3.addAll(var2.getStopTracking());
      this.remove(var3, var1);
   }

   private void spawn(Set<UUID> targets, LeashRenderer renderer) {
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

   private void update(Set<UUID> targets, LeashRenderer renderer) {
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
            var3.add((Packet)(new PacketPlayOutEntityDestroy(IntArrayList.toList(var6.values().stream().mapMultiToInt((var0, var1x) -> {
               var1x.accept(var0.getLeashId());
               var1x.accept(var0.getPivotId());
            })))));
         }

         NetworkUtils.sendBundled(var1, var3);
      }

   }

   private void remove(Set<UUID> targets, LeashRenderer renderer) {
      if (!var1.isEmpty()) {
         Map var3 = var2.getRendered();
         NetworkUtils.send((Set)var1, new PacketPlayOutEntityDestroy(IntArrayList.toList(var3.values().stream().mapMultiToInt((var0, var1x) -> {
            var1x.accept(var0.getLeashId());
            var1x.accept(var0.getPivotId());
         }))));
      }

   }

   private Packets.PacketSupplier pivotSpawn(LeashRenderer.Leash renderer) {
      Vector3f var2 = (Vector3f)var1.getPosition().get();
      return NetworkUtils.createPivotSpawn(var1.getPivotId(), var1.getPivotUUID(), var2.add(0.0F, -0.45F, -0.2F, new Vector3f()));
   }

   private PacketPlayOutSpawnEntity leashSpawn(LeashRenderer.Leash renderer) {
      Vector3f var2 = (Vector3f)var1.getPosition().get();
      return new PacketPlayOutSpawnEntity(var1.getLeashId(), var1.getLeastUUID(), (double)var2.x, (double)var2.y - 0.45D, (double)var2.z - 0.2D, 0.0F, 0.0F, EntityTypes.g, 0, Vec3D.b, 0.0D);
   }

   private PacketPlayOutEntityMetadata pivotData(LeashRenderer.Leash renderer) {
      return new PacketPlayOutEntityMetadata(var1.getPivotId(), EntityUtils.DEFAULT_AREA_EFFECT_CLOUD_DATA);
   }

   private PacketPlayOutEntityMetadata leashData(LeashRenderer.Leash renderer) {
      return new PacketPlayOutEntityMetadata(var1.getLeashId(), EntityUtils.DEFAULT_BAT_DATA);
   }

   private PacketPlayOutMount mount(LeashRenderer.Leash renderer) {
      ArrayList var2 = new ArrayList(1);
      var2.add(var1.getLeashId());
      return new PacketPlayOutMount(EntityContainer.of(var1.getPivotId(), (Collection)var2));
   }

   private Packets.PacketSupplier move(LeashRenderer.Leash renderer) {
      if (!var1.getPosition().isDirty()) {
         return null;
      } else {
         Vector3f var2 = (Vector3f)var1.getPosition().get();
         return NetworkUtils.createPivotTeleport(var1.getPivotId(), var2.add(0.0F, -0.45F, -0.2F, new Vector3f()));
      }
   }

   private PacketPlayOutAttachEntity link(LeashRenderer.Leash renderer, boolean spawn) {
      if (!var2 && !var1.getConnected().isDirty()) {
         return null;
      } else {
         var1.getConnected().clearDirty();
         return new PacketPlayOutAttachEntity(EntityContainer.of(var1.getLeashId()), EntityContainer.of((Integer)var1.getConnected().get()));
      }
   }
}
