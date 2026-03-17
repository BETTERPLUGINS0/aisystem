package advancedplugins.pm2.cv.models.v1_21_R5_spigot.parser.behavior;

import advancedplugins.pm2.cv.models.api.model.rpc.entity.data.IEntityData;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.renderer.BehaviorRendererParser;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.renderer.renderer.MountRenderer;
import advancedplugins.pm2.cv.models.api.utils.math.MathUtils;
import advancedplugins.pm2.cv.models.v1_21_R5_spigot.entity.EntityRelationship;
import advancedplugins.pm2.cv.models.v1_21_R5_spigot.entity.EntityUtils;
import advancedplugins.pm2.cv.models.v1_21_R5_spigot.network.utils.PacketBundleProvider;
import advancedplugins.pm2.cv.models.v1_21_R5_spigot.network.utils.PacketTransmissionUtility;
import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import net.minecraft.network.protocol.game.ClientboundEntityPositionSyncPacket;
import net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy;
import net.minecraft.network.protocol.game.PacketPlayOutEntityMetadata;
import net.minecraft.network.protocol.game.PacketPlayOutMount;
import net.minecraft.network.protocol.game.PacketPlayOutSpawnEntity;
import net.minecraft.network.protocol.game.PacketPlayOutUpdateAttributes;
import net.minecraft.network.protocol.game.PacketPlayOutEntity.PacketPlayOutEntityLook;
import net.minecraft.network.syncher.DataWatcherRegistry;
import net.minecraft.network.syncher.DataWatcher.c;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.PositionMoveRotation;
import net.minecraft.world.entity.ai.attributes.AttributeModifiable;
import net.minecraft.world.entity.ai.attributes.GenericAttributes;
import net.minecraft.world.phys.Vec3D;
import org.joml.Vector3f;

public class RideableEntityProcessor implements BehaviorRendererParser<MountRenderer> {
   public void sendToClients(MountRenderer var1) {
      IEntityData var2 = this.fetchEntityTracking(var1);
      this.performSynchronization(var2.getTracking().keySet(), var1);
      this.initializeForNewObservers(var2.getStartTracking(), var1);
      this.disconnectLostObservers(var2.getStopTracking(), var1);
   }

   public void destroy(MountRenderer var1) {
      IEntityData var2 = this.fetchVisualTracking(var1);
      Set var3 = this.combineObserverGroups(var2);
      this.disconnectLostObservers(var3, var1);
   }

   private IEntityData fetchEntityTracking(MountRenderer var1) {
      return var1.getModelRenderer().getActiveModel().getModeledEntity().getBase().getData();
   }

   private IEntityData fetchVisualTracking(MountRenderer var1) {
      return var1.getVisualModel().getModeledEntity().getBase().getData();
   }

   private Set<UUID> combineObserverGroups(IEntityData var1) {
      HashSet var2 = new HashSet();
      var2.addAll(var1.getStartTracking());
      var2.addAll(var1.getTracking().keySet());
      var2.addAll(var1.getStopTracking());
      return var2;
   }

   private void initializeForNewObservers(Set<UUID> var1, MountRenderer var2) {
      if (!var1.isEmpty()) {
         PacketBundleProvider var3 = this.assembleInitializationPackets(var2);
         PacketTransmissionUtility.deliverBundleToMultiple(var1, var3);
      }
   }

   private PacketBundleProvider assembleInitializationPackets(MountRenderer var1) {
      PacketBundleProvider var2 = new PacketBundleProvider();
      Iterator var3 = var1.getRendered().values().iterator();

      MountRenderer.Mount var4;
      while(var3.hasNext()) {
         var4 = (MountRenderer.Mount)var3.next();
         this.addRideableCreationPackets(var2, var4);
      }

      var3 = var1.getSpawnQueue().values().iterator();

      while(var3.hasNext()) {
         var4 = (MountRenderer.Mount)var3.next();
         this.addRideableCreationPackets(var2, var4);
      }

      return var2;
   }

   private void addRideableCreationPackets(PacketBundleProvider var1, MountRenderer.Mount var2) {
      var1.add((var2x) -> {
         return this.generateControllerSpawn(var2);
      });
      var1.addStaticPacket(this.configureControllerData(var2));
      var1.addStaticPacket(this.generateRideableSpawn(var2));
      var1.addStaticPacket(this.configureRideableData(var2));
      var1.addStaticPacket(this.linkControllerToRideable(var2));
      var1.addStaticPacket(this.attachRiders(var2));
   }

   private void performSynchronization(Set<UUID> var1, MountRenderer var2) {
      if (!var1.isEmpty()) {
         PacketBundleProvider var3 = this.buildSynchronizationPackets(var2);
         PacketTransmissionUtility.deliverBundleToMultiple(var1, var3);
      }
   }

   private PacketBundleProvider buildSynchronizationPackets(MountRenderer var1) {
      PacketBundleProvider var2 = new PacketBundleProvider();
      Iterator var3 = var1.getRendered().values().iterator();

      MountRenderer.Mount var4;
      while(var3.hasNext()) {
         var4 = (MountRenderer.Mount)var3.next();
         this.processRideableUpdates(var2, var4);
      }

      var3 = var1.getSpawnQueue().values().iterator();

      while(var3.hasNext()) {
         var4 = (MountRenderer.Mount)var3.next();
         this.addRideableCreationPackets(var2, var4);
      }

      Map var5 = var1.getDestroyQueue();
      if (!var5.isEmpty()) {
         IntArrayList var6 = this.collectRideableIds(var5.values());
         var2.addStaticPacket(new PacketPlayOutEntityDestroy(var6));
      }

      return var2;
   }

   private void processRideableUpdates(PacketBundleProvider var1, MountRenderer.Mount var2) {
      if (var2.getPosition().isDirty()) {
         var1.add((var2x) -> {
            return this.relocateController(var2);
         });
         var2.getPosition().clearDirty();
      }

      if (var2.getYaw().isDirty()) {
         var1.addStaticPacket(this.rotateRideable(var2));
         var2.getYaw().clearDirty();
      }

      if (var2.getHealth().isDirty()) {
         var1.addStaticPacket(this.updateVitality(var2));
         var2.getHealth().clearDirty();
      }

      if (var2.getMaxHealth().isDirty()) {
         var1.addStaticPacket(this.updateMaxVitality(var2));
         var2.getMaxHealth().clearDirty();
      }

      if (var2.getPassengers().isDirty()) {
         var1.addStaticPacket(this.attachRiders(var2));
         var2.getPassengers().clearDirty();
      }

   }

   private IntArrayList collectRideableIds(Collection<MountRenderer.Mount> var1) {
      IntArrayList var2 = new IntArrayList(var1.size() * 2);
      var2.addAll(IntArrayList.toList(var1.stream().mapToInt(MountRenderer.Mount::getPivotId)));
      var2.addAll(IntArrayList.toList(var1.stream().mapToInt(MountRenderer.Mount::getMountId)));
      return var2;
   }

   private void disconnectLostObservers(Set<UUID> var1, MountRenderer var2) {
      if (!var1.isEmpty()) {
         IntArrayList var3 = this.gatherAllEntityIds(var2.getRendered().values());
         PacketTransmissionUtility.broadcastToPlayers(var1, new PacketPlayOutEntityDestroy(var3));
      }
   }

   private IntArrayList gatherAllEntityIds(Collection<MountRenderer.Mount> var1) {
      IntArrayList var2 = new IntArrayList(var1.size() * 2);
      var2.addAll(IntArrayList.toList(var1.stream().mapToInt(MountRenderer.Mount::getPivotId)));
      var2.addAll(IntArrayList.toList(var1.stream().mapToInt(MountRenderer.Mount::getMountId)));
      return var2;
   }

   private PacketPlayOutSpawnEntity generateControllerSpawn(MountRenderer.Mount var1) {
      Vector3f var2 = (Vector3f)var1.getPosition().get();
      return new PacketPlayOutSpawnEntity(var1.getPivotId(), var1.getPivotUuid(), (double)var2.x, (double)var2.y, (double)var2.z, 0.0F, 0.0F, EntityTypes.e, 0, Vec3D.c, 0.0D);
   }

   private PacketPlayOutEntityMetadata configureControllerData(MountRenderer.Mount var1) {
      return new PacketPlayOutEntityMetadata(var1.getPivotId(), EntityUtils.DEFAULT_AREA_EFFECT_CLOUD_DATA);
   }

   private PacketPlayOutSpawnEntity generateRideableSpawn(MountRenderer.Mount var1) {
      Vector3f var2 = (Vector3f)var1.getPosition().get();
      float var3 = MathUtils.byteToRot((Byte)var1.getYaw().get());
      return new PacketPlayOutSpawnEntity(var1.getMountId(), var1.getMountUuid(), (double)var2.x, (double)var2.y, (double)var2.z, 0.0F, var3, EntityTypes.g, 0, Vec3D.c, 0.0D);
   }

   private PacketPlayOutEntityMetadata configureRideableData(MountRenderer.Mount var1) {
      return new PacketPlayOutEntityMetadata(var1.getMountId(), EntityUtils.DEFAULT_ARMOR_STAND_DATA);
   }

   private PacketPlayOutMount linkControllerToRideable(MountRenderer.Mount var1) {
      return new PacketPlayOutMount(EntityRelationship.of(var1.getPivotId(), var1.getMountId()));
   }

   private PacketPlayOutMount attachRiders(MountRenderer.Mount var1) {
      return new PacketPlayOutMount(EntityRelationship.of(var1.getMountId(), (Collection)var1.getPassengers()));
   }

   private ClientboundEntityPositionSyncPacket relocateController(MountRenderer.Mount var1) {
      Vector3f var2 = (Vector3f)var1.getPosition().get();
      EntityRelationship var3 = EntityRelationship.of(var1.getPivotId());
      var3.o((double)var2.x, (double)var2.y, (double)var2.z);
      return new ClientboundEntityPositionSyncPacket(var3.ar(), PositionMoveRotation.a(var3), var3.aK());
   }

   private PacketPlayOutEntityLook rotateRideable(MountRenderer.Mount var1) {
      return new PacketPlayOutEntityLook(var1.getMountId(), (Byte)var1.getYaw().get(), (byte)0, false);
   }

   private PacketPlayOutEntityMetadata updateVitality(MountRenderer.Mount var1) {
      return new PacketPlayOutEntityMetadata(var1.getMountId(), List.of(new c(9, DataWatcherRegistry.d, (Float)var1.getHealth().get())));
   }

   private PacketPlayOutUpdateAttributes updateMaxVitality(MountRenderer.Mount var1) {
      AttributeModifiable var2 = this.createVitalityAttribute((Float)var1.getMaxHealth().get());
      return new PacketPlayOutUpdateAttributes(var1.getMountId(), Lists.newArrayList(new AttributeModifiable[]{var2}));
   }

   private AttributeModifiable createVitalityAttribute(float var1) {
      AttributeModifiable var2 = new AttributeModifiable(GenericAttributes.t, (var0) -> {
      });
      var2.a((double)var1);
      return var2;
   }
}
