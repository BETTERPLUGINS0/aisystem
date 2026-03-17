package advancedplugins.pm2.cv.models.v1_21_R4.parser.behavior;

import advancedplugins.pm2.cv.models.api.model.rpc.entity.BaseEntity;
import advancedplugins.pm2.cv.models.api.model.rpc.entity.data.IEntityData;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.renderer.BehaviorRendererParser;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.renderer.renderer.SubHitboxRenderer;
import advancedplugins.pm2.cv.models.v1_21_R4.entity.EntityContainer;
import advancedplugins.pm2.cv.models.v1_21_R4.entity.EntityUtils;
import advancedplugins.pm2.cv.models.v1_21_R4.network.utils.NetworkUtils;
import advancedplugins.pm2.cv.models.v1_21_R4.network.utils.Packets;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy;
import net.minecraft.network.protocol.game.PacketPlayOutEntityMetadata;
import net.minecraft.network.protocol.game.PacketPlayOutMount;
import net.minecraft.network.protocol.game.PacketPlayOutSpawnEntity;
import net.minecraft.network.syncher.DataWatcherRegistry;
import net.minecraft.network.syncher.DataWatcher.c;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.phys.Vec3D;
import org.joml.Vector3f;

public class SubHitboxParser implements BehaviorRendererParser<SubHitboxRenderer> {
   public void sendToClients(SubHitboxRenderer renderer) {
      IEntityData var2 = var1.getModelRenderer().getActiveModel().getModeledEntity().getBase().getData();
      this.update(var2.getTracking().keySet(), var1);
      this.spawn(var2.getStartTracking(), var1);
      this.remove(var2.getStopTracking(), var1);
   }

   public void destroy(SubHitboxRenderer renderer) {
      IEntityData var2 = var1.getVisualModel().getModeledEntity().getBase().getData();
      HashSet var3 = new HashSet(var2.getStartTracking());
      var3.addAll(var2.getTracking().keySet());
      var3.addAll(var2.getStopTracking());
      this.remove(var3, var1);
   }

   private void spawn(Set<UUID> targets, SubHitboxRenderer renderer) {
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

   private void update(Set<UUID> targets, SubHitboxRenderer renderer) {
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
            var3.add((Packet)(new PacketPlayOutEntityDestroy(var7)));
         }

         NetworkUtils.sendBundled(var1, var3);
      }

   }

   private void remove(Set<UUID> targets, SubHitboxRenderer renderer) {
      if (!var1.isEmpty()) {
         Collection var3 = var2.getRendered().values();
         IntArrayList var4 = new IntArrayList(var3.size() * 2);
         var4.addAll(IntArrayList.toList(var3.stream().mapToInt(SubHitboxRenderer.SubHitbox::getPivotId)));
         var4.addAll(IntArrayList.toList(var3.stream().mapToInt(SubHitboxRenderer.SubHitbox::getHitboxId)));
         NetworkUtils.send((Set)var1, new PacketPlayOutEntityDestroy(var4));
      }

   }

   private Packets.PacketSupplier pivotSpawn(SubHitboxRenderer.SubHitbox renderer) {
      return NetworkUtils.createPivotSpawn(var1.getPivotId(), var1.getPivotUuid(), (Vector3f)var1.getPosition().get());
   }

   private PacketPlayOutEntityMetadata pivotData(SubHitboxRenderer.SubHitbox renderer) {
      return new PacketPlayOutEntityMetadata(var1.getPivotId(), EntityUtils.DEFAULT_AREA_EFFECT_CLOUD_DATA);
   }

   private Packets.PacketSupplier pivotMove(SubHitboxRenderer.SubHitbox renderer) {
      return NetworkUtils.createPivotTeleport(var1.getPivotId(), (Vector3f)var1.getPosition().get());
   }

   private PacketPlayOutMount pivotMount(SubHitboxRenderer.SubHitbox renderer) {
      return new PacketPlayOutMount(EntityContainer.of(var1.getPivotId(), var1.getHitboxId()));
   }

   private PacketPlayOutSpawnEntity hitboxSpawn(SubHitboxRenderer.SubHitbox renderer) {
      Vector3f var2 = (Vector3f)var1.getPosition().get();
      return new PacketPlayOutSpawnEntity(var1.getHitboxId(), var1.getHitboxUuid(), (double)var2.x, (double)var2.y, (double)var2.z, 0.0F, 0.0F, EntityTypes.ap, 0, Vec3D.c, 0.0D);
   }

   private PacketPlayOutEntityMetadata hitboxData(SubHitboxRenderer.SubHitbox renderer, boolean spawn) {
      if (!var2 && !var1.isDirty()) {
         return null;
      } else {
         ArrayList var3 = new ArrayList(4);
         if (var2) {
            var3.add(new c(1, DataWatcherRegistry.b, Integer.MAX_VALUE));
            var3.add(new c(10, DataWatcherRegistry.k, false));
         }

         var1.getWidth().ifDirty((var1x) -> {
            var3.add(new c(8, DataWatcherRegistry.d, var1x));
         }, var2);
         var1.getHeight().ifDirty((var1x) -> {
            var3.add(new c(9, DataWatcherRegistry.d, var1x));
         }, var2);
         var1.clearDirty();
         return new PacketPlayOutEntityMetadata(var1.getHitboxId(), var3);
      }
   }
}
