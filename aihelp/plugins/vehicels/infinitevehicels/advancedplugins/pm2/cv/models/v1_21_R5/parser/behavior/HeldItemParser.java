package advancedplugins.pm2.cv.models.v1_21_R5.parser.behavior;

import advancedplugins.pm2.cv.models.api.model.rpc.entity.data.IEntityData;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.renderer.BehaviorRendererParser;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.renderer.renderer.HeldItemRenderer;
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
import net.minecraft.network.protocol.game.ClientboundEntityPositionSyncPacket;
import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.network.protocol.game.ClientboundSetPassengersPacket;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData.DataValue;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PositionMoveRotation;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Location;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.joml.Quaternionf;

public class HeldItemParser implements BehaviorRendererParser<HeldItemRenderer> {
   public void sendToClients(HeldItemRenderer var1) {
      IEntityData var2 = var1.getModelRenderer().getActiveModel().getModeledEntity().getBase().getData();
      this.update(var2.getTracking().keySet(), var1);
      this.spawn(var2.getStartTracking(), var1);
      this.remove(var2.getStopTracking(), var1);
   }

   public void destroy(HeldItemRenderer var1) {
      IEntityData var2 = var1.getVisualModel().getModeledEntity().getBase().getData();
      HashSet var3 = new HashSet(var2.getStartTracking());
      var3.addAll(var2.getTracking().keySet());
      var3.addAll(var2.getStopTracking());
      this.remove(var3, var1);
   }

   private void spawn(Set<UUID> var1, HeldItemRenderer var2) {
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

   private void update(Set<UUID> var1, HeldItemRenderer var2) {
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
            var3.add((Packet)(new ClientboundRemoveEntitiesPacket(IntArrayList.toList(var9.values().stream().mapToInt(HeldItemRenderer.Item::getId)))));
         }

         if (var2.getPassengers().isDirty()) {
            var3.add((Packet)this.mount(var2));
            var2.getPassengers().clearDirty();
         }

         NetworkUtils.sendBundled(var1, var3);
      }

   }

   private void remove(Set<UUID> var1, HeldItemRenderer var2) {
      if (!var1.isEmpty()) {
         Map var3 = var2.getRendered();
         IntArrayList var4 = IntArrayList.toList(var3.values().stream().mapToInt(HeldItemRenderer.Item::getId));
         var4.add(var2.getId());
         NetworkUtils.send((Set)var1, new ClientboundRemoveEntitiesPacket(var4));
      }

   }

   private ClientboundAddEntityPacket pivotSpawn(Location var1, HeldItemRenderer var2) {
      return new ClientboundAddEntityPacket(var2.getId(), var2.getUuid(), var1.getX(), var1.getY(), var1.getZ(), 0.0F, 0.0F, EntityType.ARMOR_STAND, 0, Vec3.ZERO, 0.0D);
   }

   private ClientboundSetEntityDataPacket pivotData(HeldItemRenderer var1) {
      return new ClientboundSetEntityDataPacket(var1.getId(), EntityUtils.DEFAULT_AREA_EFFECT_CLOUD_DATA);
   }

   private ClientboundAddEntityPacket itemSpawn(Location var1, HeldItemRenderer.Item var2) {
      return new ClientboundAddEntityPacket(var2.getId(), var2.getUuid(), var1.getX(), var1.getY(), var1.getZ(), 0.0F, 0.0F, EntityType.ITEM_DISPLAY, 0, Vec3.ZERO, 0.0D);
   }

   private ClientboundSetEntityDataPacket itemData(HeldItemRenderer.Item var1, boolean var2) {
      if (!var2 && !var1.isDirty()) {
         return null;
      } else {
         ArrayList var3 = new ArrayList(11);
         if (var2) {
            var3.add(new DataValue(1, EntityDataSerializers.INT, Integer.MAX_VALUE));
            var3.add(new DataValue(8, EntityDataSerializers.INT, 0));
            var3.add(new DataValue(9, EntityDataSerializers.INT, 1));
            var3.add(new DataValue(17, EntityDataSerializers.FLOAT, 4096.0F));
         } else if (var1.isTransformDirty()) {
            var3.add(new DataValue(8, EntityDataSerializers.INT, 0));
         }

         var1.getGlowing().ifDirty((var1x) -> {
            var3.add(new DataValue(0, EntityDataSerializers.BYTE, (byte)(var1x ? 96 : 32)));
         }, var2);
         var1.getGlowColor().ifDirty((var1x) -> {
            var3.add(new DataValue(22, EntityDataSerializers.INT, var1x));
         }, var2);
         var1.getPosition().ifDirty((var1x) -> {
            var3.add(new DataValue(11, EntityDataSerializers.VECTOR3, var1x));
         }, var2);
         var1.getScale().ifDirty((var1x) -> {
            var3.add(new DataValue(12, EntityDataSerializers.VECTOR3, var1x));
         }, var2);
         var1.getRotation().ifDirty((var1x) -> {
            var3.add(new DataValue(13, EntityDataSerializers.QUATERNION, var1x.rotateY(3.1415927F, new Quaternionf())));
         }, var2);
         var1.getModel().ifDirty((var1x) -> {
            var3.add(new DataValue(23, EntityDataSerializers.ITEM_STACK, CraftItemStack.asNMSCopy(var1x)));
         }, var2);
         var1.getDisplay().ifDirty((var1x) -> {
            var3.add(new DataValue(24, EntityDataSerializers.BYTE, var1x == null ? 0 : (byte)var1x.ordinal()));
         }, var2);
         var1.clearDirty();
         return new ClientboundSetEntityDataPacket(var1.getId(), var3);
      }
   }

   private ClientboundEntityPositionSyncPacket teleport(HeldItemRenderer var1) {
      Location var2 = var1.getVisualModel().getModeledEntity().getBase().getLocation();
      EntityContainer var3 = EntityContainer.of(var1.getId());
      var3.setPosRaw(var2.getX(), var2.getY(), var2.getZ());
      return new ClientboundEntityPositionSyncPacket(var3.getId(), PositionMoveRotation.of(var3), var3.onGround());
   }

   private ClientboundSetPassengersPacket mount(HeldItemRenderer var1) {
      return new ClientboundSetPassengersPacket(EntityContainer.of(var1.getId(), (Collection)var1.getPassengers()));
   }
}
