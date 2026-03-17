package advancedplugins.pm2.cv.models.v1_20_R4.parser.behavior;

import advancedplugins.pm2.cv.models.api.model.rpc.entity.data.IEntityData;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.renderer.BehaviorRendererParser;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.renderer.renderer.HeldItemRenderer;
import advancedplugins.pm2.cv.models.v1_20_R4.entity.EntityContainer;
import advancedplugins.pm2.cv.models.v1_20_R4.entity.EntityUtils;
import advancedplugins.pm2.cv.models.v1_20_R4.network.utils.NetworkUtils;
import advancedplugins.pm2.cv.models.v1_20_R4.network.utils.Packets;
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
import net.minecraft.network.protocol.game.PacketPlayOutEntityTeleport;
import net.minecraft.network.protocol.game.PacketPlayOutMount;
import net.minecraft.network.protocol.game.PacketPlayOutSpawnEntity;
import net.minecraft.network.syncher.DataWatcherRegistry;
import net.minecraft.network.syncher.DataWatcher.b;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.phys.Vec3D;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_20_R3.inventory.CraftItemStack;
import org.joml.Quaternionf;

public class HeldItemParser implements BehaviorRendererParser<HeldItemRenderer> {
   public void sendToClients(HeldItemRenderer renderer) {
      IEntityData var2 = var1.getModelRenderer().getActiveModel().getModeledEntity().getBase().getData();
      this.update(var2.getTracking().keySet(), var1);
      this.spawn(var2.getStartTracking(), var1);
      this.remove(var2.getStopTracking(), var1);
   }

   public void destroy(HeldItemRenderer renderer) {
      IEntityData var2 = var1.getVisualModel().getModeledEntity().getBase().getData();
      HashSet var3 = new HashSet(var2.getStartTracking());
      var3.addAll(var2.getTracking().keySet());
      var3.addAll(var2.getStopTracking());
      this.remove(var3, var1);
   }

   private void spawn(Set<UUID> targets, HeldItemRenderer renderer) {
      if (!var1.isEmpty()) {
         Packets var3 = new Packets();
         Location var4 = var2.getVisualModel().getModeledEntity().getBase().getLocation();
         var3.add((Packet)this.pivotSpawn(var4, var2));
         var3.add((Packet)this.pivotData(var2));
         Iterator var5 = var2.getRendered().values().iterator();

         HeldItemRenderer.Item var6;
         while(var5.hasNext()) {
            var6 = (HeldItemRenderer.Item)var5.next();
            var3.add((Packet)this.itemSpawn(var4, var6));
            var3.add((Packet)this.itemData(var6, true));
         }

         var5 = var2.getSpawnQueue().values().iterator();

         while(var5.hasNext()) {
            var6 = (HeldItemRenderer.Item)var5.next();
            var3.add((Packet)this.itemSpawn(var4, var6));
            var3.add((Packet)this.itemData(var6, true));
         }

         var3.add((Packet)this.mount(var2));
         NetworkUtils.sendBundled(var1, var3);
      }

   }

   private void update(Set<UUID> targets, HeldItemRenderer renderer) {
      if (!var1.isEmpty()) {
         Packets var3 = new Packets();
         var3.add((Packet)this.teleport(var2));
         Iterator var4 = var2.getRendered().values().iterator();

         while(var4.hasNext()) {
            HeldItemRenderer.Item var5 = (HeldItemRenderer.Item)var4.next();
            var3.add((Packet)this.itemData(var5, false));
         }

         Location var8 = var2.getVisualModel().getModeledEntity().getBase().getLocation();
         Iterator var6 = var2.getSpawnQueue().values().iterator();

         while(var6.hasNext()) {
            HeldItemRenderer.Item var7 = (HeldItemRenderer.Item)var6.next();
            var3.add((Packet)this.itemSpawn(var8, var7));
            var3.add((Packet)this.itemData(var7, true));
         }

         Map var9 = var2.getDestroyQueue();
         if (!var9.isEmpty()) {
            var3.add((Packet)(new PacketPlayOutEntityDestroy(IntArrayList.toList(var9.values().stream().mapToInt(HeldItemRenderer.Item::getId)))));
         }

         if (var2.getPassengers().isDirty()) {
            var3.add((Packet)this.mount(var2));
            var2.getPassengers().clearDirty();
         }

         NetworkUtils.sendBundled(var1, var3);
      }

   }

   private void remove(Set<UUID> targets, HeldItemRenderer renderer) {
      if (!var1.isEmpty()) {
         Map var3 = var2.getRendered();
         IntArrayList var4 = IntArrayList.toList(var3.values().stream().mapToInt(HeldItemRenderer.Item::getId));
         var4.add(var2.getId());
         NetworkUtils.send((Set)var1, new PacketPlayOutEntityDestroy(var4));
      }

   }

   private PacketPlayOutSpawnEntity pivotSpawn(Location location, HeldItemRenderer renderer) {
      return new PacketPlayOutSpawnEntity(var2.getId(), var2.getUuid(), var1.getX(), var1.getY(), var1.getZ(), 0.0F, 0.0F, EntityTypes.d, 0, Vec3D.b, 0.0D);
   }

   private PacketPlayOutEntityMetadata pivotData(HeldItemRenderer renderer) {
      return new PacketPlayOutEntityMetadata(var1.getId(), EntityUtils.DEFAULT_AREA_EFFECT_CLOUD_DATA);
   }

   private PacketPlayOutSpawnEntity itemSpawn(Location location, HeldItemRenderer.Item renderer) {
      return new PacketPlayOutSpawnEntity(var2.getId(), var2.getUuid(), var1.getX(), var1.getY(), var1.getZ(), 0.0F, 0.0F, EntityTypes.af, 0, Vec3D.b, 0.0D);
   }

   private PacketPlayOutEntityMetadata itemData(HeldItemRenderer.Item renderer, boolean spawn) {
      if (!var2 && !var1.isDirty()) {
         return null;
      } else {
         ArrayList var3 = new ArrayList(11);
         if (var2) {
            var3.add(new b(1, DataWatcherRegistry.b, Integer.MAX_VALUE));
            var3.add(new b(8, DataWatcherRegistry.b, 0));
            var3.add(new b(9, DataWatcherRegistry.b, 1));
            var3.add(new b(17, DataWatcherRegistry.d, 4096.0F));
         } else if (var1.isTransformDirty()) {
            var3.add(new b(8, DataWatcherRegistry.b, 0));
         }

         var1.getGlowing().ifDirty((var1x) -> {
            var3.add(new b(0, DataWatcherRegistry.a, (byte)(var1x ? 96 : 32)));
         }, var2);
         var1.getGlowColor().ifDirty((var1x) -> {
            var3.add(new b(22, DataWatcherRegistry.b, var1x));
         }, var2);
         var1.getPosition().ifDirty((var1x) -> {
            var3.add(new b(11, DataWatcherRegistry.A, var1x));
         }, var2);
         var1.getScale().ifDirty((var1x) -> {
            var3.add(new b(12, DataWatcherRegistry.A, var1x));
         }, var2);
         var1.getRotation().ifDirty((var1x) -> {
            var3.add(new b(13, DataWatcherRegistry.B, var1x.rotateY(3.1415927F, new Quaternionf())));
         }, var2);
         var1.getModel().ifDirty((var1x) -> {
            var3.add(new b(23, DataWatcherRegistry.h, CraftItemStack.asNMSCopy(var1x)));
         }, var2);
         var1.getDisplay().ifDirty((var1x) -> {
            var3.add(new b(24, DataWatcherRegistry.a, var1x == null ? 0 : (byte)var1x.ordinal()));
         }, var2);
         var1.clearDirty();
         return new PacketPlayOutEntityMetadata(var1.getId(), var3);
      }
   }

   private PacketPlayOutEntityTeleport teleport(HeldItemRenderer renderer) {
      Location var2 = var1.getVisualModel().getModeledEntity().getBase().getLocation();
      EntityContainer var3 = EntityContainer.of(var1.getId());
      var3.p(var2.getX(), var2.getY(), var2.getZ());
      return new PacketPlayOutEntityTeleport(var3);
   }

   private PacketPlayOutMount mount(HeldItemRenderer renderer) {
      return new PacketPlayOutMount(EntityContainer.of(var1.getId(), (Collection)var1.getPassengers()));
   }
}
