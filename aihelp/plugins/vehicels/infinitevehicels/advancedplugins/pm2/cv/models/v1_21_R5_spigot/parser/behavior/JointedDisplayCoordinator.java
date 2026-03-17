package advancedplugins.pm2.cv.models.v1_21_R5_spigot.parser.behavior;

import advancedplugins.pm2.cv.models.api.model.rpc.entity.data.IEntityData;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.renderer.BehaviorRendererParser;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.renderer.renderer.SegmentRenderer;
import advancedplugins.pm2.cv.models.v1_21_R5_spigot.entity.EntityRelationship;
import advancedplugins.pm2.cv.models.v1_21_R5_spigot.entity.EntityUtils;
import advancedplugins.pm2.cv.models.v1_21_R5_spigot.network.utils.PacketBundleProvider;
import advancedplugins.pm2.cv.models.v1_21_R5_spigot.network.utils.PacketTransmissionUtility;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import net.minecraft.network.protocol.game.ClientboundEntityPositionSyncPacket;
import net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy;
import net.minecraft.network.protocol.game.PacketPlayOutEntityMetadata;
import net.minecraft.network.protocol.game.PacketPlayOutMount;
import net.minecraft.network.protocol.game.PacketPlayOutSpawnEntity;
import net.minecraft.network.syncher.DataWatcherRegistry;
import net.minecraft.network.syncher.DataWatcher.c;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.PositionMoveRotation;
import net.minecraft.world.phys.Vec3D;
import org.bukkit.craftbukkit.v1_21_R5.inventory.CraftItemStack;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class JointedDisplayCoordinator implements BehaviorRendererParser<SegmentRenderer> {
   private final Set<Runnable> deferredOperations = new HashSet();
   private static final int MAX_AIR_TICKS = Integer.MAX_VALUE;
   private static final float MAX_RENDER_DISTANCE = 4096.0F;
   private static final float HIDDEN_RENDER_DISTANCE = 0.0F;
   private static final byte GLOWING_MASK = 96;
   private static final byte STANDARD_MASK = 32;

   public void sendToClients(SegmentRenderer var1) {
      IEntityData var2 = this.retrieveObserverData(var1);
      this.synchronizeExistingObservers(var2.getTracking().keySet(), var1);
      this.initializeNewObservers(var2.getStartTracking(), var1);
      this.terminateDisconnectedObservers(var2.getStopTracking(), var1);
      this.executeDeferredOperations();
   }

   public void destroy(SegmentRenderer var1) {
      IEntityData var2 = this.retrieveVisualObserverData(var1);
      Set var3 = this.consolidateObservers(var2);
      this.terminateDisconnectedObservers(var3, var1);
   }

   private IEntityData retrieveObserverData(SegmentRenderer var1) {
      return var1.getModelRenderer().getActiveModel().getModeledEntity().getBase().getData();
   }

   private IEntityData retrieveVisualObserverData(SegmentRenderer var1) {
      return var1.getVisualModel().getModeledEntity().getBase().getData();
   }

   private Set<UUID> consolidateObservers(IEntityData var1) {
      HashSet var2 = new HashSet();
      var2.addAll(var1.getStartTracking());
      var2.addAll(var1.getTracking().keySet());
      var2.addAll(var1.getStopTracking());
      return var2;
   }

   private void executeDeferredOperations() {
      this.deferredOperations.forEach(Runnable::run);
      this.deferredOperations.clear();
   }

   private void initializeNewObservers(Set<UUID> var1, SegmentRenderer var2) {
      if (!var1.isEmpty()) {
         PacketBundleProvider var3 = new PacketBundleProvider();
         this.populatePivotGroups(var3, var2.getRendered());
         this.populatePivotGroups(var3, var2.getSpawnQueue());
         PacketTransmissionUtility.deliverBundleToMultiple(var1, var3);
      }
   }

   private void populatePivotGroups(PacketBundleProvider var1, Map<String, SegmentRenderer.Pivot> var2) {
      Iterator var3 = var2.values().iterator();

      while(var3.hasNext()) {
         SegmentRenderer.Pivot var4 = (SegmentRenderer.Pivot)var3.next();
         this.assembleCompletePivotStructure(var1, var4);
         this.schedulePivotCleanup(var4);
      }

   }

   private void assembleCompletePivotStructure(PacketBundleProvider var1, SegmentRenderer.Pivot var2) {
      var1.add((var2x) -> {
         return this.generatePivotAnchor(var2);
      });
      var1.addStaticPacket(this.configurePivotAnchor(var2));
      Iterator var3 = var2.getRendered().values().iterator();

      SegmentRenderer.Joint var4;
      while(var3.hasNext()) {
         var4 = (SegmentRenderer.Joint)var3.next();
         var1.addStaticPacket(this.createJointDisplay(var2, var4));
         var1.addStaticPacket(this.configureJointDisplay(var4, true, false));
      }

      var3 = var2.getSpawnQueue().values().iterator();

      while(var3.hasNext()) {
         var4 = (SegmentRenderer.Joint)var3.next();
         var1.addStaticPacket(this.createJointDisplay(var2, var4));
         var1.addStaticPacket(this.configureJointDisplay(var4, true, false));
      }

      var1.addStaticPacket(this.establishPivotHierarchy(var2));
   }

   private void schedulePivotCleanup(SegmentRenderer.Pivot var1) {
      Set var10000 = this.deferredOperations;
      Objects.requireNonNull(var1);
      var10000.add(var1::clearDirty);
   }

   private void synchronizeExistingObservers(Set<UUID> var1, SegmentRenderer var2) {
      if (!var1.isEmpty()) {
         PacketBundleProvider var3 = this.buildSynchronizationPackets(var2);
         PacketTransmissionUtility.deliverBundleToMultiple(var1, var3);
      }
   }

   private PacketBundleProvider buildSynchronizationPackets(SegmentRenderer var1) {
      PacketBundleProvider var2 = new PacketBundleProvider();
      IntArrayList var3 = new IntArrayList();
      Iterator var4 = var1.getRendered().values().iterator();

      while(var4.hasNext()) {
         SegmentRenderer.Pivot var5 = (SegmentRenderer.Pivot)var4.next();
         this.processActivePivot(var2, var5, var3);
      }

      this.populatePivotGroups(var2, var1.getSpawnQueue());
      var1.getDestroyQueue().forEach((var1x, var2x) -> {
         var3.add(var2x.getId());
         var3.addAll(var2x.getPassengers());
      });
      if (!var3.isEmpty()) {
         var2.addStaticPacket(new PacketPlayOutEntityDestroy(var3));
      }

      return var2;
   }

   private void processActivePivot(PacketBundleProvider var1, SegmentRenderer.Pivot var2, IntArrayList var3) {
      ClientboundEntityPositionSyncPacket var4 = this.relocatePivotAnchor(var2);
      if (var4 != null) {
         var1.addStaticPacket(var4);
      }

      var2.getRendered().values().forEach((var2x) -> {
         PacketPlayOutEntityMetadata var3 = this.configureJointDisplay(var2x, false, false);
         if (var3 != null) {
            var1.addStaticPacket(var3);
         }

      });
      var2.getSpawnQueue().values().forEach((var3x) -> {
         var1.addStaticPacket(this.createJointDisplay(var2, var3x));
         var1.addStaticPacket(this.configureJointDisplay(var3x, true, false));
      });
      var2.getDestroyQueue().values().forEach((var1x) -> {
         var3.add(var1x.getId());
      });
      if (var2.getPassengers().isDirty()) {
         var1.addStaticPacket(this.establishPivotHierarchy(var2));
      }

      this.schedulePivotCleanup(var2);
   }

   private void terminateDisconnectedObservers(Set<UUID> var1, SegmentRenderer var2) {
      if (!var1.isEmpty()) {
         IntArrayList var3 = this.collectAllEntityIds(var2.getRendered().values());
         PacketTransmissionUtility.broadcastToPlayers(var1, new PacketPlayOutEntityDestroy(var3));
      }
   }

   private IntArrayList collectAllEntityIds(Collection<SegmentRenderer.Pivot> var1) {
      IntArrayList var2 = new IntArrayList();
      var1.forEach((var1x) -> {
         var2.add(var1x.getId());
         var2.addAll(var1x.getPassengers());
      });
      return var2;
   }

   private PacketPlayOutSpawnEntity generatePivotAnchor(SegmentRenderer.Pivot var1) {
      Vector3f var2 = (Vector3f)var1.getPosition().get();
      return new PacketPlayOutSpawnEntity(var1.getId(), var1.getUuid(), (double)var2.x, (double)var2.y, (double)var2.z, 0.0F, 0.0F, EntityTypes.e, 0, Vec3D.c, 0.0D);
   }

   private PacketPlayOutEntityMetadata configurePivotAnchor(SegmentRenderer.Pivot var1) {
      return new PacketPlayOutEntityMetadata(var1.getId(), EntityUtils.DEFAULT_AREA_EFFECT_CLOUD_DATA);
   }

   private PacketPlayOutMount establishPivotHierarchy(SegmentRenderer.Pivot var1) {
      return new PacketPlayOutMount(EntityRelationship.of(var1.getId(), (Collection)var1.getPassengers()));
   }

   private ClientboundEntityPositionSyncPacket relocatePivotAnchor(SegmentRenderer.Pivot var1) {
      if (!var1.getPosition().isDirty()) {
         return null;
      } else {
         this.deferredOperations.add(() -> {
            var1.getPosition().clearDirty();
         });
         Vector3f var2 = (Vector3f)var1.getPosition().get();
         EntityRelationship var3 = EntityRelationship.of(var1.getId());
         var3.o((double)var2.x, (double)var2.y, (double)var2.z);
         return new ClientboundEntityPositionSyncPacket(var3.ar(), PositionMoveRotation.a(var3), var3.aK());
      }
   }

   private PacketPlayOutSpawnEntity createJointDisplay(SegmentRenderer.Pivot var1, SegmentRenderer.Joint var2) {
      Vector3f var3 = (Vector3f)var1.getPosition().get();
      return new PacketPlayOutSpawnEntity(var2.getId(), var2.getUuid(), (double)var3.x, (double)var3.y, (double)var3.z, 0.0F, 0.0F, EntityTypes.at, 0, Vec3D.c, 0.0D);
   }

   private PacketPlayOutEntityMetadata configureJointDisplay(SegmentRenderer.Joint var1, boolean var2, boolean var3) {
      if (!this.shouldUpdateJoint(var1, var2, var3)) {
         return null;
      } else {
         ArrayList var4 = this.buildJointProperties(var1, var2, var3);
         this.scheduleJointCleanup(var1);
         return new PacketPlayOutEntityMetadata(var1.getId(), var4);
      }
   }

   private boolean shouldUpdateJoint(SegmentRenderer.Joint var1, boolean var2, boolean var3) {
      return var2 || var3 || var1.isDirty();
   }

   private ArrayList<c<?>> buildJointProperties(SegmentRenderer.Joint var1, boolean var2, boolean var3) {
      ArrayList var4 = new ArrayList(13);
      if (var2) {
         var4.add(new c(1, DataWatcherRegistry.b, Integer.MAX_VALUE));
         var4.add(new c(8, DataWatcherRegistry.b, 0));
      } else if (var1.isTransformDirty() || var3) {
         var4.add(new c(8, DataWatcherRegistry.b, 0));
      }

      boolean var5 = var2 || var3;
      var1.getStep().ifDirty((var1x) -> {
         var4.add(new c(9, DataWatcherRegistry.b, var1x ? 0 : 1));
      }, var5);
      var1.getGlowing().ifDirty((var1x) -> {
         var4.add(new c(0, DataWatcherRegistry.a, Byte.valueOf((byte)(var1x ? 96 : 32))));
      }, var5);
      var1.getGlowColor().ifDirty((var1x) -> {
         var4.add(new c(22, DataWatcherRegistry.b, var1x));
      }, var5);
      var1.getBrightness().ifDirty((var1x) -> {
         var4.add(new c(16, DataWatcherRegistry.b, var1x));
      }, var5);
      var1.getPosition().ifDirty((var1x) -> {
         var4.add(new c(11, DataWatcherRegistry.H, var1x));
      }, var5);
      var1.getScale().ifDirty((var1x) -> {
         var4.add(new c(12, DataWatcherRegistry.H, var1x));
      }, var5);
      var1.getLeftRotation().ifDirty((var1x) -> {
         Quaternionf var2 = var1x.rotateY(3.1415927F, new Quaternionf());
         var4.add(new c(13, DataWatcherRegistry.I, var2));
      }, var5);
      var1.getRightRotation().ifDirty((var1x) -> {
         var4.add(new c(14, DataWatcherRegistry.I, var1x));
      }, var5);
      var1.getVisibility().ifDirty((var1x) -> {
         var4.add(new c(17, DataWatcherRegistry.d, var1x ? 4096.0F : 0.0F));
      }, var5);
      var1.getModel().ifDirty((var1x) -> {
         var4.add(new c(23, DataWatcherRegistry.h, CraftItemStack.asNMSCopy(var1x)));
      }, var5);
      var1.getDisplay().ifDirty((var1x) -> {
         var4.add(new c(24, DataWatcherRegistry.a, var1x == null ? 0 : (byte)var1x.ordinal()));
      }, var5);
      return var4;
   }

   private void scheduleJointCleanup(SegmentRenderer.Joint var1) {
      Set var10000 = this.deferredOperations;
      Objects.requireNonNull(var1);
      var10000.add(var1::clearDirty);
   }
}
