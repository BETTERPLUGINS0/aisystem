package advancedplugins.pm2.cv.models.v1_21_R10.parser.behavior;

import advancedplugins.pm2.cv.models.api.model.rpc.entity.data.IEntityData;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.renderer.BehaviorRendererParser;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.renderer.renderer.HeldItemRenderer;
import advancedplugins.pm2.cv.models.v1_21_R10.entity.EntityRelationship;
import advancedplugins.pm2.cv.models.v1_21_R10.entity.EntityUtils;
import advancedplugins.pm2.cv.models.v1_21_R10.network.utils.PacketBundleProvider;
import advancedplugins.pm2.cv.models.v1_21_R10.network.utils.PacketTransmissionUtility;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
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
import org.bukkit.craftbukkit.v1_21_R7.inventory.CraftItemStack;
import org.joml.Quaternionf;

public class ItemDisplayManager implements BehaviorRendererParser<HeldItemRenderer> {
   private static final byte GLOW_FLAG = 96;
   private static final byte NO_GLOW_FLAG = 32;
   private static final int MAX_AIR_TIME = Integer.MAX_VALUE;
   private static final float MAX_VIEW_RANGE = 4096.0F;

   public void sendToClients(HeldItemRenderer var1) {
      IEntityData var2 = this.extractEntityData(var1);
      this.refreshActiveViewers(var2.getTracking().keySet(), var1);
      this.initializeNewViewers(var2.getStartTracking(), var1);
      this.cleanupLostViewers(var2.getStopTracking(), var1);
   }

   public void destroy(HeldItemRenderer var1) {
      IEntityData var2 = this.extractVisualData(var1);
      Set var3 = this.consolidateViewerSets(var2);
      this.cleanupLostViewers(var3, var1);
   }

   private IEntityData extractEntityData(HeldItemRenderer var1) {
      return var1.getModelRenderer().getActiveModel().getModeledEntity().getBase().getData();
   }

   private IEntityData extractVisualData(HeldItemRenderer var1) {
      return var1.getVisualModel().getModeledEntity().getBase().getData();
   }

   private Set<UUID> consolidateViewerSets(IEntityData var1) {
      HashSet var2 = new HashSet();
      var2.addAll(var1.getStartTracking());
      var2.addAll(var1.getTracking().keySet());
      var2.addAll(var1.getStopTracking());
      return var2;
   }

   private void initializeNewViewers(Set<UUID> var1, HeldItemRenderer var2) {
      if (!var1.isEmpty()) {
         PacketBundleProvider var3 = this.createSpawnBundle(var2);
         PacketTransmissionUtility.deliverBundleToMultiple(var1, var3);
      }
   }

   private PacketBundleProvider createSpawnBundle(HeldItemRenderer var1) {
      PacketBundleProvider var2 = new PacketBundleProvider();
      Location var3 = this.extractOriginLocation(var1);
      var2.addStaticPacket(this.generateAnchorEntity(var3, var1));
      var2.addStaticPacket(this.configureAnchorProperties(var1));
      Iterator var4 = var1.getRendered().values().iterator();

      HeldItemRenderer.Item var5;
      while(var4.hasNext()) {
         var5 = (HeldItemRenderer.Item)var4.next();
         var2.addStaticPacket(this.generateItemEntity(var3, var5));
         var2.addStaticPacket(this.configureItemProperties(var5, true));
      }

      var4 = var1.getSpawnQueue().values().iterator();

      while(var4.hasNext()) {
         var5 = (HeldItemRenderer.Item)var4.next();
         var2.addStaticPacket(this.generateItemEntity(var3, var5));
         var2.addStaticPacket(this.configureItemProperties(var5, true));
      }

      var2.addStaticPacket(this.createPassengerAttachment(var1));
      return var2;
   }

   private void refreshActiveViewers(Set<UUID> var1, HeldItemRenderer var2) {
      if (!var1.isEmpty()) {
         PacketBundleProvider var3 = this.createUpdateBundle(var2);
         PacketTransmissionUtility.deliverBundleToMultiple(var1, var3);
      }
   }

   private PacketBundleProvider createUpdateBundle(HeldItemRenderer var1) {
      PacketBundleProvider var2 = new PacketBundleProvider();
      var2.addStaticPacket(this.repositionAnchor(var1));
      Iterator var3 = var1.getRendered().values().iterator();

      while(var3.hasNext()) {
         HeldItemRenderer.Item var4 = (HeldItemRenderer.Item)var3.next();
         ClientboundSetEntityDataPacket var5 = this.configureItemProperties(var4, false);
         if (var5 != null) {
            var2.addStaticPacket(var5);
         }
      }

      Location var6 = this.extractOriginLocation(var1);
      Iterator var7 = var1.getSpawnQueue().values().iterator();

      while(var7.hasNext()) {
         HeldItemRenderer.Item var9 = (HeldItemRenderer.Item)var7.next();
         var2.addStaticPacket(this.generateItemEntity(var6, var9));
         var2.addStaticPacket(this.configureItemProperties(var9, true));
      }

      Map var8 = var1.getDestroyQueue();
      if (!var8.isEmpty()) {
         IntArrayList var10 = this.extractItemIdentifiers(var8.values());
         var2.addStaticPacket(new ClientboundRemoveEntitiesPacket(var10));
      }

      if (var1.getPassengers().isDirty()) {
         var2.addStaticPacket(this.createPassengerAttachment(var1));
         var1.getPassengers().clearDirty();
      }

      return var2;
   }

   private void cleanupLostViewers(Set<UUID> var1, HeldItemRenderer var2) {
      if (!var1.isEmpty()) {
         IntArrayList var3 = this.compileAllEntityIds(var2);
         PacketTransmissionUtility.broadcastToPlayers(var1, new ClientboundRemoveEntitiesPacket(var3));
      }
   }

   private IntArrayList compileAllEntityIds(HeldItemRenderer var1) {
      IntArrayList var2 = this.extractItemIdentifiers(var1.getRendered().values());
      var2.add(var1.getId());
      return var2;
   }

   private IntArrayList extractItemIdentifiers(Collection<HeldItemRenderer.Item> var1) {
      return IntArrayList.toList(var1.stream().mapToInt(HeldItemRenderer.Item::getId));
   }

   private Location extractOriginLocation(HeldItemRenderer var1) {
      return var1.getVisualModel().getModeledEntity().getBase().getLocation();
   }

   private ClientboundAddEntityPacket generateAnchorEntity(Location var1, HeldItemRenderer var2) {
      return new ClientboundAddEntityPacket(var2.getId(), var2.getUuid(), var1.getX(), var1.getY(), var1.getZ(), 0.0F, 0.0F, EntityType.ARMOR_STAND, 0, Vec3.ZERO, 0.0D);
   }

   private ClientboundSetEntityDataPacket configureAnchorProperties(HeldItemRenderer var1) {
      return new ClientboundSetEntityDataPacket(var1.getId(), EntityUtils.DEFAULT_AREA_EFFECT_CLOUD_DATA);
   }

   private ClientboundAddEntityPacket generateItemEntity(Location var1, HeldItemRenderer.Item var2) {
      return new ClientboundAddEntityPacket(var2.getId(), var2.getUuid(), var1.getX(), var1.getY(), var1.getZ(), 0.0F, 0.0F, EntityType.ITEM_DISPLAY, 0, Vec3.ZERO, 0.0D);
   }

   private ClientboundSetEntityDataPacket configureItemProperties(HeldItemRenderer.Item var1, boolean var2) {
      if (!this.shouldUpdateItemData(var1, var2)) {
         return null;
      } else {
         ArrayList var3 = this.buildItemProperties(var1, var2);
         var1.clearDirty();
         return new ClientboundSetEntityDataPacket(var1.getId(), var3);
      }
   }

   private boolean shouldUpdateItemData(HeldItemRenderer.Item var1, boolean var2) {
      return var2 || var1.isDirty();
   }

   private ArrayList<DataValue<?>> buildItemProperties(HeldItemRenderer.Item var1, boolean var2) {
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
         var3.add(new DataValue(0, EntityDataSerializers.BYTE, Byte.valueOf((byte)(var1x ? 96 : 32))));
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
         Quaternionf var2 = var1x.rotateY(3.1415927F, new Quaternionf());
         var3.add(new DataValue(13, EntityDataSerializers.QUATERNION, var2));
      }, var2);
      var1.getModel().ifDirty((var1x) -> {
         var3.add(new DataValue(23, EntityDataSerializers.ITEM_STACK, CraftItemStack.asNMSCopy(var1x)));
      }, var2);
      var1.getDisplay().ifDirty((var1x) -> {
         var3.add(new DataValue(24, EntityDataSerializers.BYTE, var1x == null ? 0 : (byte)var1x.ordinal()));
      }, var2);
      return var3;
   }

   private ClientboundEntityPositionSyncPacket repositionAnchor(HeldItemRenderer var1) {
      Location var2 = this.extractOriginLocation(var1);
      EntityRelationship var3 = this.createEntityRelation(var1.getId(), var2);
      return new ClientboundEntityPositionSyncPacket(var3.getId(), PositionMoveRotation.of(var3), var3.onGround());
   }

   private EntityRelationship createEntityRelation(int var1, Location var2) {
      EntityRelationship var3 = EntityRelationship.of(var1);
      var3.setPosRaw(var2.getX(), var2.getY(), var2.getZ());
      return var3;
   }

   private ClientboundSetPassengersPacket createPassengerAttachment(HeldItemRenderer var1) {
      return new ClientboundSetPassengersPacket(EntityRelationship.of(var1.getId(), (Collection)var1.getPassengers()));
   }
}
