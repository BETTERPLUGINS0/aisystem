package advancedplugins.pm2.cv.models.v1_21_R1.parser.behavior;

import advancedplugins.pm2.cv.models.api.model.rpc.entity.data.IEntityData;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.renderer.BehaviorRendererParser;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.renderer.renderer.MountRenderer;
import advancedplugins.pm2.cv.models.api.utils.math.MathUtils;
import advancedplugins.pm2.cv.models.v1_21_R1.entity.EntityRelationship;
import advancedplugins.pm2.cv.models.v1_21_R1.entity.EntityUtils;
import advancedplugins.pm2.cv.models.v1_21_R1.network.utils.PacketBundleProvider;
import advancedplugins.pm2.cv.models.v1_21_R1.network.utils.PacketTransmissionUtility;
import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy;
import net.minecraft.network.protocol.game.PacketPlayOutEntityMetadata;
import net.minecraft.network.protocol.game.PacketPlayOutEntityTeleport;
import net.minecraft.network.protocol.game.PacketPlayOutMount;
import net.minecraft.network.protocol.game.PacketPlayOutSpawnEntity;
import net.minecraft.network.protocol.game.PacketPlayOutUpdateAttributes;
import net.minecraft.network.protocol.game.PacketPlayOutEntity.PacketPlayOutEntityLook;
import net.minecraft.network.syncher.DataWatcherRegistry;
import net.minecraft.network.syncher.DataWatcher.c;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.ai.attributes.AttributeModifiable;
import net.minecraft.world.entity.ai.attributes.GenericAttributes;
import net.minecraft.world.phys.Vec3D;
import org.joml.Vector3f;

public class RideableEntityProcessor implements BehaviorRendererParser<MountRenderer> {
   public void sendToClients(MountRenderer renderer) {
      IEntityData var2 = this.fetchEntityTracking(var1);
      this.performSynchronization(var2.getTracking().keySet(), var1);
      this.initializeForNewObservers(var2.getStartTracking(), var1);
      this.disconnectLostObservers(var2.getStopTracking(), var1);
   }

   public void destroy(MountRenderer renderer) {
      IEntityData var2 = this.fetchVisualTracking(var1);
      Set var3 = this.combineObserverGroups(var2);
      this.disconnectLostObservers(var3, var1);
   }

   private IEntityData fetchEntityTracking(MountRenderer renderer) {
      return var1.getModelRenderer().getActiveModel().getModeledEntity().getBase().getData();
   }

   private IEntityData fetchVisualTracking(MountRenderer renderer) {
      return var1.getVisualModel().getModeledEntity().getBase().getData();
   }

   private Set<UUID> combineObserverGroups(IEntityData tracking) {
      HashSet var2 = new HashSet();
      var2.addAll(var1.getStartTracking());
      var2.addAll(var1.getTracking().keySet());
      var2.addAll(var1.getStopTracking());
      return var2;
   }

   private void initializeForNewObservers(Set<UUID> observers, MountRenderer renderer) {
      if (!var1.isEmpty()) {
         PacketBundleProvider var3 = this.assembleInitializationPackets(var2);
         PacketTransmissionUtility.deliverBundleToMultiple(var1, var3);
      }
   }

   private PacketBundleProvider assembleInitializationPackets(MountRenderer renderer) {
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

   private void addRideableCreationPackets(PacketBundleProvider packets, MountRenderer.Mount rideable) {
      var1.add((var2x) -> {
         return this.generateControllerSpawn(var2);
      });
      var1.addStaticPacket(this.configureControllerData(var2));
      var1.addStaticPacket(this.generateRideableSpawn(var2));
      var1.addStaticPacket(this.configureRideableData(var2));
      var1.addStaticPacket(this.linkControllerToRideable(var2));
      var1.addStaticPacket(this.attachRiders(var2));
   }

   private void performSynchronization(Set<UUID> observers, MountRenderer renderer) {
      if (!var1.isEmpty()) {
         PacketBundleProvider var3 = this.buildSynchronizationPackets(var2);
         PacketTransmissionUtility.deliverBundleToMultiple(var1, var3);
      }
   }

   private PacketBundleProvider buildSynchronizationPackets(MountRenderer renderer) {
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

   private void processRideableUpdates(PacketBundleProvider packets, MountRenderer.Mount rideable) {
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

   private IntArrayList collectRideableIds(Collection<MountRenderer.Mount> rideables) {
      IntArrayList var2 = new IntArrayList(var1.size() * 2);
      var2.addAll(IntArrayList.toList(var1.stream().mapToInt(MountRenderer.Mount::getPivotId)));
      var2.addAll(IntArrayList.toList(var1.stream().mapToInt(MountRenderer.Mount::getMountId)));
      return var2;
   }

   private void disconnectLostObservers(Set<UUID> observers, MountRenderer renderer) {
      if (!var1.isEmpty()) {
         IntArrayList var3 = this.gatherAllEntityIds(var2.getRendered().values());
         PacketTransmissionUtility.broadcastToPlayers(var1, new PacketPlayOutEntityDestroy(var3));
      }
   }

   private IntArrayList gatherAllEntityIds(Collection<MountRenderer.Mount> rideables) {
      IntArrayList var2 = new IntArrayList(var1.size() * 2);
      var2.addAll(IntArrayList.toList(var1.stream().mapToInt(MountRenderer.Mount::getPivotId)));
      var2.addAll(IntArrayList.toList(var1.stream().mapToInt(MountRenderer.Mount::getMountId)));
      return var2;
   }

   private PacketPlayOutSpawnEntity generateControllerSpawn(MountRenderer.Mount rideable) {
      Vector3f var2 = (Vector3f)var1.getPosition().get();
      return new PacketPlayOutSpawnEntity(var1.getPivotId(), var1.getPivotUuid(), (double)var2.x, (double)var2.y, (double)var2.z, 0.0F, 0.0F, EntityTypes.b, 0, Vec3D.b, 0.0D);
   }

   private PacketPlayOutEntityMetadata configureControllerData(MountRenderer.Mount rideable) {
      return new PacketPlayOutEntityMetadata(var1.getPivotId(), EntityUtils.DEFAULT_AREA_EFFECT_CLOUD_DATA);
   }

   private PacketPlayOutSpawnEntity generateRideableSpawn(MountRenderer.Mount rideable) {
      Vector3f var2 = (Vector3f)var1.getPosition().get();
      float var3 = MathUtils.byteToRot((Byte)var1.getYaw().get());
      return new PacketPlayOutSpawnEntity(var1.getMountId(), var1.getMountUuid(), (double)var2.x, (double)var2.y, (double)var2.z, 0.0F, var3, EntityTypes.d, 0, Vec3D.b, 0.0D);
   }

   private PacketPlayOutEntityMetadata configureRideableData(MountRenderer.Mount rideable) {
      return new PacketPlayOutEntityMetadata(var1.getMountId(), EntityUtils.DEFAULT_ARMOR_STAND_DATA);
   }

   private PacketPlayOutMount linkControllerToRideable(MountRenderer.Mount rideable) {
      return new PacketPlayOutMount(EntityRelationship.of(var1.getPivotId(), var1.getMountId()));
   }

   private PacketPlayOutMount attachRiders(MountRenderer.Mount rideable) {
      return new PacketPlayOutMount(EntityRelationship.of(var1.getMountId(), (Collection)var1.getPassengers()));
   }

   private PacketPlayOutEntityTeleport relocateController(MountRenderer.Mount rideable) {
      Vector3f var2 = (Vector3f)var1.getPosition().get();
      int var3 = var1.getPivotId();
      RegistryFriendlyByteBuf var4 = PacketTransmissionUtility.allocateBufferWithData((var2x) -> {
         var2x.c(var3);
         var2x.a((double)var2.x);
         var2x.a((double)var2.y);
         var2x.a((double)var2.z);
         var2x.a(0.0F);
         var2x.a(0.0F);
         var2x.a(false);
      });
      return (PacketPlayOutEntityTeleport)PacketTransmissionUtility.instantiatePacket(PacketPlayOutEntityTeleport.class, var4);
   }

   private PacketPlayOutEntityLook rotateRideable(MountRenderer.Mount rideable) {
      return new PacketPlayOutEntityLook(var1.getMountId(), (Byte)var1.getYaw().get(), (byte)0, false);
   }

   private PacketPlayOutEntityMetadata updateVitality(MountRenderer.Mount rideable) {
      return new PacketPlayOutEntityMetadata(var1.getMountId(), List.of(new c(9, DataWatcherRegistry.d, (Float)var1.getHealth().get())));
   }

   private PacketPlayOutUpdateAttributes updateMaxVitality(MountRenderer.Mount rideable) {
      AttributeModifiable var2 = this.createVitalityAttribute((Float)var1.getMaxHealth().get());
      return new PacketPlayOutUpdateAttributes(var1.getMountId(), Lists.newArrayList(new AttributeModifiable[]{var2}));
   }

   private AttributeModifiable createVitalityAttribute(float maxHealth) {
      AttributeModifiable var2 = new AttributeModifiable(GenericAttributes.s, (var0) -> {
      });
      var2.a((double)var1);
      return var2;
   }
}
