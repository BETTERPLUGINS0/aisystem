package advancedplugins.pm2.cv.models.v1_21_R1.parser.behavior;

import advancedplugins.pm2.cv.models.api.model.rpc.entity.data.IEntityData;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.renderer.BehaviorRendererParser;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.renderer.renderer.HeldItemRenderer;
import advancedplugins.pm2.cv.models.v1_21_R1.entity.EntityRelationship;
import advancedplugins.pm2.cv.models.v1_21_R1.entity.EntityUtils;
import advancedplugins.pm2.cv.models.v1_21_R1.network.utils.PacketBundleProvider;
import advancedplugins.pm2.cv.models.v1_21_R1.network.utils.PacketTransmissionUtility;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.PacketListenerPlayOut;
import net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy;
import net.minecraft.network.protocol.game.PacketPlayOutEntityMetadata;
import net.minecraft.network.protocol.game.PacketPlayOutEntityTeleport;
import net.minecraft.network.protocol.game.PacketPlayOutMount;
import net.minecraft.network.protocol.game.PacketPlayOutSpawnEntity;
import net.minecraft.network.syncher.DataWatcherRegistry;
import net.minecraft.network.syncher.DataWatcher.c;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.phys.Vec3D;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_21_R1.inventory.CraftItemStack;
import org.joml.Quaternionf;

public class ItemDisplayManager implements BehaviorRendererParser<HeldItemRenderer> {
   private static final byte GLOW_FLAG = 96;
   private static final byte NO_GLOW_FLAG = 32;
   private static final int MAX_AIR_TIME = Integer.MAX_VALUE;
   private static final float MAX_VIEW_RANGE = 4096.0F;

   public void sendToClients(HeldItemRenderer renderer) {
      IEntityData var2 = this.extractEntityData(var1);
      this.refreshActiveViewers(var2.getTracking().keySet(), var1);
      this.initializeNewViewers(var2.getStartTracking(), var1);
      this.cleanupLostViewers(var2.getStopTracking(), var1);
   }

   public void destroy(HeldItemRenderer renderer) {
      IEntityData var2 = this.extractVisualData(var1);
      Set var3 = this.consolidateViewerSets(var2);
      this.cleanupLostViewers(var3, var1);
   }

   private IEntityData extractEntityData(HeldItemRenderer renderer) {
      return var1.getModelRenderer().getActiveModel().getModeledEntity().getBase().getData();
   }

   private IEntityData extractVisualData(HeldItemRenderer renderer) {
      return var1.getVisualModel().getModeledEntity().getBase().getData();
   }

   private Set<UUID> consolidateViewerSets(IEntityData data) {
      HashSet var2 = new HashSet();
      var2.addAll(var1.getStartTracking());
      var2.addAll(var1.getTracking().keySet());
      var2.addAll(var1.getStopTracking());
      return var2;
   }

   private void initializeNewViewers(Set<UUID> viewers, HeldItemRenderer renderer) {
      if (!var1.isEmpty()) {
         PacketBundleProvider var3 = this.createSpawnBundle(var2);
         PacketTransmissionUtility.deliverBundleToMultiple(var1, var3);
      }
   }

   private PacketBundleProvider createSpawnBundle(HeldItemRenderer renderer) {
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

   private void refreshActiveViewers(Set<UUID> viewers, HeldItemRenderer renderer) {
      if (!var1.isEmpty()) {
         PacketBundleProvider var3 = this.createUpdateBundle(var2);
         PacketTransmissionUtility.deliverBundleToMultiple(var1, var3);
      }
   }

   private PacketBundleProvider createUpdateBundle(HeldItemRenderer renderer) {
      PacketBundleProvider var2 = new PacketBundleProvider();
      var2.addStaticPacket(this.repositionAnchor(var1));
      Iterator var3 = var1.getRendered().values().iterator();

      while(var3.hasNext()) {
         HeldItemRenderer.Item var4 = (HeldItemRenderer.Item)var3.next();
         PacketPlayOutEntityMetadata var5 = this.configureItemProperties(var4, false);
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
         var2.addStaticPacket(new PacketPlayOutEntityDestroy(var10));
      }

      if (var1.getPassengers().isDirty()) {
         var2.addStaticPacket(this.createPassengerAttachment(var1));
         var1.getPassengers().clearDirty();
      }

      return var2;
   }

   private void cleanupLostViewers(Set<UUID> viewers, HeldItemRenderer renderer) {
      if (!var1.isEmpty()) {
         IntArrayList var3 = this.compileAllEntityIds(var2);
         PacketTransmissionUtility.broadcastToPlayers(var1, new PacketPlayOutEntityDestroy(var3));
      }
   }

   private IntArrayList compileAllEntityIds(HeldItemRenderer renderer) {
      IntArrayList var2 = this.extractItemIdentifiers(var1.getRendered().values());
      var2.add(var1.getId());
      return var2;
   }

   private IntArrayList extractItemIdentifiers(Collection<HeldItemRenderer.Item> items) {
      return IntArrayList.toList(var1.stream().mapToInt(HeldItemRenderer.Item::getId));
   }

   private Location extractOriginLocation(HeldItemRenderer renderer) {
      return var1.getVisualModel().getModeledEntity().getBase().getLocation();
   }

   private PacketPlayOutSpawnEntity generateAnchorEntity(Location origin, HeldItemRenderer renderer) {
      return new PacketPlayOutSpawnEntity(var2.getId(), var2.getUuid(), var1.getX(), var1.getY(), var1.getZ(), 0.0F, 0.0F, EntityTypes.d, 0, Vec3D.b, 0.0D);
   }

   private PacketPlayOutEntityMetadata configureAnchorProperties(HeldItemRenderer renderer) {
      return new PacketPlayOutEntityMetadata(var1.getId(), EntityUtils.DEFAULT_AREA_EFFECT_CLOUD_DATA);
   }

   private PacketPlayOutSpawnEntity generateItemEntity(Location origin, HeldItemRenderer.Item item) {
      return new PacketPlayOutSpawnEntity(var2.getId(), var2.getUuid(), var1.getX(), var1.getY(), var1.getZ(), 0.0F, 0.0F, EntityTypes.ah, 0, Vec3D.b, 0.0D);
   }

   private PacketPlayOutEntityMetadata configureItemProperties(HeldItemRenderer.Item item, boolean initialSetup) {
      if (!this.shouldUpdateItemData(var1, var2)) {
         return null;
      } else {
         ArrayList var3 = this.buildItemProperties(var1, var2);
         var1.clearDirty();
         return new PacketPlayOutEntityMetadata(var1.getId(), var3);
      }
   }

   private boolean shouldUpdateItemData(HeldItemRenderer.Item item, boolean initialSetup) {
      return var2 || var1.isDirty();
   }

   private ArrayList<c<?>> buildItemProperties(HeldItemRenderer.Item item, boolean initialSetup) {
      ArrayList var3 = new ArrayList(11);
      if (var2) {
         var3.add(new c(1, DataWatcherRegistry.b, Integer.MAX_VALUE));
         var3.add(new c(8, DataWatcherRegistry.b, 0));
         var3.add(new c(9, DataWatcherRegistry.b, 1));
         var3.add(new c(17, DataWatcherRegistry.d, 4096.0F));
      } else if (var1.isTransformDirty()) {
         var3.add(new c(8, DataWatcherRegistry.b, 0));
      }

      var1.getGlowing().ifDirty((var1x) -> {
         var3.add(new c(0, DataWatcherRegistry.a, Byte.valueOf((byte)(var1x ? 96 : 32))));
      }, var2);
      var1.getGlowColor().ifDirty((var1x) -> {
         var3.add(new c(22, DataWatcherRegistry.b, var1x));
      }, var2);
      var1.getPosition().ifDirty((var1x) -> {
         var3.add(new c(11, DataWatcherRegistry.D, var1x));
      }, var2);
      var1.getScale().ifDirty((var1x) -> {
         var3.add(new c(12, DataWatcherRegistry.D, var1x));
      }, var2);
      var1.getRotation().ifDirty((var1x) -> {
         Quaternionf var2 = var1x.rotateY(3.1415927F, new Quaternionf());
         var3.add(new c(13, DataWatcherRegistry.E, var2));
      }, var2);
      var1.getModel().ifDirty((var1x) -> {
         var3.add(new c(23, DataWatcherRegistry.h, CraftItemStack.asNMSCopy(var1x)));
      }, var2);
      var1.getDisplay().ifDirty((var1x) -> {
         var3.add(new c(24, DataWatcherRegistry.a, var1x == null ? 0 : (byte)var1x.ordinal()));
      }, var2);
      return var3;
   }

   private Packet<PacketListenerPlayOut> repositionAnchor(HeldItemRenderer renderer) {
      Location var2 = this.extractOriginLocation(var1);
      int var3 = var1.getId();
      RegistryFriendlyByteBuf var4 = PacketTransmissionUtility.allocateBufferWithData((var2x) -> {
         var2x.c(var3);
         var2x.a(var2.getX());
         var2x.a(var2.getY());
         var2x.a(var2.getZ());
         var2x.a(0.0F);
         var2x.a(0.0F);
         var2x.a(false);
      });
      return PacketTransmissionUtility.instantiatePacket(PacketPlayOutEntityTeleport.class, var4);
   }

   private EntityRelationship createEntityRelation(int entityId, Location pos) {
      EntityRelationship var3 = EntityRelationship.of(var1);
      var3.o(var2.getX(), var2.getY(), var2.getZ());
      return var3;
   }

   private PacketPlayOutMount createPassengerAttachment(HeldItemRenderer renderer) {
      return new PacketPlayOutMount(EntityRelationship.of(var1.getId(), (Collection)var1.getPassengers()));
   }
}
