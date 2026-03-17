package advancedplugins.pm2.cv.models.v1_20_R4.parser.vfx;

import advancedplugins.pm2.cv.models.api.model.rpc.entity.data.IEntityData;
import advancedplugins.pm2.cv.models.api.model.rpc.visual.renderer.VisualDisplayRenderer;
import advancedplugins.pm2.cv.models.api.model.rpc.visual.renderer.VisualRendererParser;
import advancedplugins.pm2.cv.models.v1_20_R4.entity.EntityContainer;
import advancedplugins.pm2.cv.models.v1_20_R4.entity.EntityUtils;
import advancedplugins.pm2.cv.models.v1_20_R4.network.utils.NetworkUtils;
import advancedplugins.pm2.cv.models.v1_20_R4.network.utils.Packets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import net.minecraft.network.PacketDataSerializer;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy;
import net.minecraft.network.protocol.game.PacketPlayOutEntityMetadata;
import net.minecraft.network.protocol.game.PacketPlayOutMount;
import net.minecraft.network.protocol.game.PacketPlayOutSpawnEntity;
import net.minecraft.network.syncher.DataWatcherRegistry;
import net.minecraft.network.syncher.DataWatcher.b;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.phys.Vec3D;
import org.bukkit.craftbukkit.v1_20_R3.inventory.CraftItemStack;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class VisualDisplayParser implements VisualRendererParser<VisualDisplayRenderer> {
   public void dispatch(VisualDisplayRenderer renderer) {
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

   public void dispose(VisualDisplayRenderer renderer) {
      IEntityData var2 = var1.getVisual().getOriginal().getData();
      HashSet var3 = new HashSet(var2.getStartTracking());
      var3.addAll(var2.getTracking().keySet());
      var3.addAll(var2.getStopTracking());
      this.remove(var3, var1.getVisualModel());
   }

   public void spawn(Set<UUID> targets, VisualDisplayRenderer.RendererVisualModel vfx) {
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

   public void update(Set<UUID> targets, VisualDisplayRenderer.RendererVisualModel vfx) {
      if (!var1.isEmpty()) {
         Packets var3 = new Packets();
         var3.add(this.teleport(var2));
         var3.add((Packet)this.vfxData(var2, false));
         NetworkUtils.sendBundled(var1, var3);
      }

   }

   public void remove(Set<UUID> targets, VisualDisplayRenderer.RendererVisualModel vfx) {
      if (!var1.isEmpty()) {
         NetworkUtils.send((Set)var1, new PacketPlayOutEntityDestroy(new int[]{var2.getPivotId(), var2.getModelId()}));
      }

   }

   private Packets.PacketSupplier pivotSpawn(VisualDisplayRenderer.RendererVisualModel vfx) {
      return NetworkUtils.createPivotSpawn(var1.getPivotId(), var1.getPivotUuid(), (Vector3f)var1.getOrigin().get());
   }

   private PacketPlayOutEntityMetadata pivotData(VisualDisplayRenderer.RendererVisualModel vfx) {
      return new PacketPlayOutEntityMetadata(var1.getPivotId(), EntityUtils.DEFAULT_AREA_EFFECT_CLOUD_DATA);
   }

   private PacketPlayOutSpawnEntity vfxSpawn(VisualDisplayRenderer.RendererVisualModel vfx) {
      Vector3f var2 = (Vector3f)var1.getOrigin().get();
      return new PacketPlayOutSpawnEntity(var1.getModelId(), var1.getModelUuid(), (double)var2.x, (double)var2.y, (double)var2.z, 0.0F, 0.0F, EntityTypes.af, 0, Vec3D.b, 0.0D);
   }

   private PacketPlayOutEntityMetadata vfxData(VisualDisplayRenderer.RendererVisualModel vfx, boolean spawn) {
      if (!var2 && !var1.isModelDirty()) {
         return null;
      } else {
         ArrayList var3 = new ArrayList(9);
         PacketDataSerializer var4 = NetworkUtils.createByteBuf();
         var4.c(var1.getModelId());
         if (var2) {
            var3.add(new b(0, DataWatcherRegistry.a, (byte)32));
            var3.add(new b(1, DataWatcherRegistry.b, Integer.MAX_VALUE));
            var3.add(new b(8, DataWatcherRegistry.b, 0));
            var3.add(new b(9, DataWatcherRegistry.b, 1));
            var3.add(new b(17, DataWatcherRegistry.d, 4096.0F));
         } else if (var1.isModelDirty()) {
            var3.add(new b(8, DataWatcherRegistry.b, 0));
         }

         var1.getPosition().ifDirty((var1x) -> {
            var3.add(new b(11, DataWatcherRegistry.A, var1x));
         }, var2);
         var1.getScale().ifDirty((var1x) -> {
            var3.add(new b(12, DataWatcherRegistry.A, var1x));
         }, var2);
         var1.getLeftRotation().ifDirty((var1x) -> {
            var3.add(new b(13, DataWatcherRegistry.B, var1x.rotateY(3.1415927F, new Quaternionf())));
         }, var2);
         var1.getModel().ifDirty((var1x) -> {
            var3.add(new b(23, DataWatcherRegistry.h, CraftItemStack.asNMSCopy(var1x)));
         }, var2);
         var1.clearModelDirty();
         return new PacketPlayOutEntityMetadata(var1.getModelId(), var3);
      }
   }

   private Packets.PacketSupplier teleport(VisualDisplayRenderer.RendererVisualModel vfx) {
      return !var1.getOrigin().isDirty() ? null : NetworkUtils.createPivotTeleport(var1.getPivotId(), (Vector3f)var1.getOrigin().get());
   }

   private PacketPlayOutMount mount(VisualDisplayRenderer.RendererVisualModel vfx) {
      PacketDataSerializer var2 = NetworkUtils.createByteBuf();
      var2.c(var1.getPivotId());
      var2.c(1);
      var2.c(var1.getModelId());
      return new PacketPlayOutMount(EntityContainer.of(var1.getPivotId(), var1.getModelId()));
   }
}
