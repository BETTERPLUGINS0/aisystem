package advancedplugins.pm2.cv.models.v1_21_R4.parser.behavior;

import advancedplugins.pm2.cv.models.api.model.rpc.entity.data.IEntityData;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.renderer.BehaviorRendererParser;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.renderer.renderer.NameTagRenderer;
import advancedplugins.pm2.cv.models.v1_21_R4.entity.EntityContainer;
import advancedplugins.pm2.cv.models.v1_21_R4.entity.EntityUtils;
import advancedplugins.pm2.cv.models.v1_21_R4.network.utils.NetworkUtils;
import advancedplugins.pm2.cv.models.v1_21_R4.network.utils.Packets;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.chat.IChatBaseComponent.ChatSerializer;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy;
import net.minecraft.network.protocol.game.PacketPlayOutEntityMetadata;
import net.minecraft.network.protocol.game.PacketPlayOutMount;
import net.minecraft.network.protocol.game.PacketPlayOutSpawnEntity;
import net.minecraft.network.syncher.DataWatcherRegistry;
import net.minecraft.network.syncher.DataWatcher.c;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.phys.Vec3D;
import org.joml.Vector3f;

public class NameTagParser implements BehaviorRendererParser<NameTagRenderer> {
   public void sendToClients(NameTagRenderer renderer) {
      IEntityData var2 = var1.getModelRenderer().getActiveModel().getModeledEntity().getBase().getData();
      this.update(var2.getTracking().keySet(), var1);
      this.spawn(var2.getStartTracking(), var1);
      this.remove(var2.getStopTracking(), var1);
   }

   public void destroy(NameTagRenderer renderer) {
      IEntityData var2 = var1.getVisualModel().getModeledEntity().getBase().getData();
      HashSet var3 = new HashSet(var2.getStartTracking());
      var3.addAll(var2.getTracking().keySet());
      var3.addAll(var2.getStopTracking());
      this.remove(var3, var1);
   }

   private void spawn(Set<UUID> targets, NameTagRenderer renderer) {
      if (!var1.isEmpty()) {
         Packets var3 = new Packets();
         Iterator var4 = var2.getRendered().values().iterator();

         NameTagRenderer.NameTag var5;
         while(var4.hasNext()) {
            var5 = (NameTagRenderer.NameTag)var4.next();
            var3.add(this.pivotSpawn(var5));
            var3.add((Packet)this.pivotData(var5, true));
            var3.add((Packet)this.tagSpawn(var5));
            var3.add((Packet)this.tagData(var5, true));
            var3.add((Packet)this.pivotMount(var5));
         }

         var4 = var2.getSpawnQueue().values().iterator();

         while(var4.hasNext()) {
            var5 = (NameTagRenderer.NameTag)var4.next();
            var3.add(this.pivotSpawn(var5));
            var3.add((Packet)this.pivotData(var5, true));
            var3.add((Packet)this.tagSpawn(var5));
            var3.add((Packet)this.tagData(var5, true));
            var3.add((Packet)this.pivotMount(var5));
         }

         NetworkUtils.sendBundled(var1, var3);
      }

   }

   private void update(Set<UUID> targets, NameTagRenderer renderer) {
      if (!var1.isEmpty()) {
         Packets var3 = new Packets();
         Iterator var4 = var2.getRendered().values().iterator();

         NameTagRenderer.NameTag var5;
         while(var4.hasNext()) {
            var5 = (NameTagRenderer.NameTag)var4.next();
            var3.add((Packet)this.tagData(var5, false));
            if (var5.getPosition().isDirty()) {
               var3.add(this.pivotMove(var5));
               var5.getPosition().clearDirty();
            }
         }

         var4 = var2.getSpawnQueue().values().iterator();

         while(var4.hasNext()) {
            var5 = (NameTagRenderer.NameTag)var4.next();
            var3.add(this.pivotSpawn(var5));
            var3.add((Packet)this.pivotData(var5, true));
            var3.add((Packet)this.tagSpawn(var5));
            var3.add((Packet)this.tagData(var5, true));
            var3.add((Packet)this.pivotMount(var5));
         }

         Map var6 = var2.getDestroyQueue();
         if (!var6.isEmpty()) {
            IntArrayList var7 = new IntArrayList(var6.size() * 2);
            var7.addAll(IntArrayList.toList(var6.values().stream().mapToInt(NameTagRenderer.NameTag::getPivotId)));
            var7.addAll(IntArrayList.toList(var6.values().stream().mapToInt(NameTagRenderer.NameTag::getTagId)));
            var3.add((Packet)(new PacketPlayOutEntityDestroy(var7)));
         }

         NetworkUtils.sendBundled(var1, var3);
      }

   }

   private void remove(Set<UUID> targets, NameTagRenderer renderer) {
      if (!var1.isEmpty()) {
         Collection var3 = var2.getRendered().values();
         IntArrayList var4 = new IntArrayList(var3.size() * 2);
         var4.addAll(IntArrayList.toList(var3.stream().mapToInt(NameTagRenderer.NameTag::getPivotId)));
         var4.addAll(IntArrayList.toList(var3.stream().mapToInt(NameTagRenderer.NameTag::getTagId)));
         NetworkUtils.send((Set)var1, new PacketPlayOutEntityDestroy(var4));
      }

   }

   private Packets.PacketSupplier pivotSpawn(NameTagRenderer.NameTag renderer) {
      return NetworkUtils.createPivotSpawn(var1.getPivotId(), var1.getPivotUuid(), (Vector3f)var1.getPosition().get());
   }

   private PacketPlayOutEntityMetadata pivotData(NameTagRenderer.NameTag renderer, boolean spawn) {
      if (!var2 && !var1.isDirty()) {
         return null;
      } else {
         List var3 = List.of();
         if (var2) {
            var3 = EntityUtils.DEFAULT_AREA_EFFECT_CLOUD_DATA;
         }

         var1.clearDirty();
         return new PacketPlayOutEntityMetadata(var1.getPivotId(), var3);
      }
   }

   private PacketPlayOutSpawnEntity tagSpawn(NameTagRenderer.NameTag renderer) {
      Vector3f var2 = (Vector3f)var1.getPosition().get();
      return new PacketPlayOutSpawnEntity(var1.getTagId(), var1.getTagUuid(), (double)var2.x, (double)var2.y, (double)var2.z, 0.0F, 0.0F, EntityTypes.bw, 0, Vec3D.c, 0.0D);
   }

   private PacketPlayOutEntityMetadata tagData(NameTagRenderer.NameTag renderer, boolean spawn) {
      if (!var2 && !var1.isDirty()) {
         return null;
      } else {
         ArrayList var3 = new ArrayList(7);
         var1.getVisibility().ifDirty((var1x) -> {
            var3.add(new c(17, DataWatcherRegistry.d, var1x ? 4096.0F : 0.0F));
         }, var2);
         var1.getScale().ifDirty((var1x) -> {
            var3.add(new c(12, DataWatcherRegistry.H, var1x));
         }, var2);
         var1.getTextOpacity().ifDirty((var1x) -> {
            var3.add(new c(26, DataWatcherRegistry.a, var1x));
         }, var2);
         var1.getBackgroundColor().ifDirty((var1x) -> {
            var3.add(new c(25, DataWatcherRegistry.b, var1x));
         }, var2);
         var1.getStyle().ifDirty((var1x) -> {
            var3.add(new c(27, DataWatcherRegistry.a, var1x));
         }, var2);
         var1.getBillboard().ifDirty((var1x) -> {
            byte var2;
            switch(var1x) {
            case FIXED:
               var2 = 0;
               break;
            case VERTICAL:
               var2 = 1;
               break;
            case HORIZONTAL:
               var2 = 2;
               break;
            case CENTER:
               var2 = 3;
               break;
            default:
               throw new RuntimeException((String)null, (Throwable)null);
            }

            var3.add(new c(15, DataWatcherRegistry.a, var2));
         }, var2);
         var1.getJsonString().ifDirty((var1x) -> {
            if (var1x == null) {
               var3.add(new c(23, DataWatcherRegistry.f, IChatBaseComponent.i()));
            } else {
               var3.add(new c(23, DataWatcherRegistry.f, ChatSerializer.a(var1x, MinecraftServer.getServer().ba())));
            }

         }, var2);
         var1.clearDirty();
         return new PacketPlayOutEntityMetadata(var1.getTagId(), var3);
      }
   }

   private Packets.PacketSupplier pivotMove(NameTagRenderer.NameTag renderer) {
      return NetworkUtils.createPivotTeleport(var1.getPivotId(), (Vector3f)var1.getPosition().get());
   }

   private PacketPlayOutMount pivotMount(NameTagRenderer.NameTag renderer) {
      return new PacketPlayOutMount(EntityContainer.of(var1.getPivotId(), var1.getTagId()));
   }
}
