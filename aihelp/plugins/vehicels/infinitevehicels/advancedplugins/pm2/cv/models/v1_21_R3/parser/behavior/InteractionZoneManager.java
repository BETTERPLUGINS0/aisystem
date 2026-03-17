package advancedplugins.pm2.cv.models.v1_21_R3.parser.behavior;

import advancedplugins.pm2.cv.models.api.model.rpc.entity.BaseEntity;
import advancedplugins.pm2.cv.models.api.model.rpc.entity.data.IEntityData;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.renderer.BehaviorRendererParser;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.renderer.renderer.SubHitboxRenderer;
import advancedplugins.pm2.cv.models.v1_21_R3.entity.EntityRelationship;
import advancedplugins.pm2.cv.models.v1_21_R3.entity.EntityUtils;
import advancedplugins.pm2.cv.models.v1_21_R3.network.utils.PacketBundleProvider;
import advancedplugins.pm2.cv.models.v1_21_R3.network.utils.PacketTransmissionUtility;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
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
import org.bukkit.entity.Entity;
import org.joml.Vector3f;

public class InteractionZoneManager implements BehaviorRendererParser<SubHitboxRenderer> {
   private static final int INFINITE_AIR = Integer.MAX_VALUE;

   public void sendToClients(SubHitboxRenderer renderer) {
      IEntityData var2 = this.extractParticipantData(var1);
      this.maintainActiveParticipants(var2.getTracking().keySet(), var1);
      this.registerNewParticipants(var2.getStartTracking(), var1);
      this.deregisterLostParticipants(var2.getStopTracking(), var1);
   }

   public void destroy(SubHitboxRenderer renderer) {
      IEntityData var2 = this.extractVisualParticipantData(var1);
      Set var3 = this.mergeParticipantSets(var2);
      this.deregisterLostParticipants(var3, var1);
   }

   private IEntityData extractParticipantData(SubHitboxRenderer renderer) {
      return var1.getModelRenderer().getActiveModel().getModeledEntity().getBase().getData();
   }

   private IEntityData extractVisualParticipantData(SubHitboxRenderer renderer) {
      return var1.getVisualModel().getModeledEntity().getBase().getData();
   }

   private Set<UUID> mergeParticipantSets(IEntityData data) {
      HashSet var2 = new HashSet();
      var2.addAll(var1.getStartTracking());
      var2.addAll(var1.getTracking().keySet());
      var2.addAll(var1.getStopTracking());
      return var2;
   }

   private void registerNewParticipants(Set<UUID> participants, SubHitboxRenderer renderer) {
      if (!var1.isEmpty()) {
         PacketBundleProvider var3 = this.createRegistrationBundle(var2);
         BaseEntity var4 = this.extractSourceEntity(var2);
         Entity var5 = (Entity)var4.getOriginal();
         PacketTransmissionUtility.deliverBundleConditionally(var1, var3, (var1x) -> {
            return !var1x.equals(var5);
         });
      }
   }

   private BaseEntity<?> extractSourceEntity(SubHitboxRenderer renderer) {
      return var1.getVisualModel().getModeledEntity().getBase();
   }

   private PacketBundleProvider createRegistrationBundle(SubHitboxRenderer renderer) {
      PacketBundleProvider var2 = new PacketBundleProvider();
      Iterator var3 = var1.getRendered().values().iterator();

      SubHitboxRenderer.SubHitbox var4;
      while(var3.hasNext()) {
         var4 = (SubHitboxRenderer.SubHitbox)var3.next();
         this.constructZonePackets(var2, var4, true);
      }

      var3 = var1.getSpawnQueue().values().iterator();

      while(var3.hasNext()) {
         var4 = (SubHitboxRenderer.SubHitbox)var3.next();
         this.constructZonePackets(var2, var4, true);
      }

      return var2;
   }

   private void constructZonePackets(PacketBundleProvider bundle, SubHitboxRenderer.SubHitbox zone, boolean initial) {
      var1.add((var2x) -> {
         return this.createZoneAnchor(var2);
      });
      var1.addStaticPacket(this.configureZoneAnchor(var2));
      var1.addStaticPacket(this.createInteractionEntity(var2));
      PacketPlayOutEntityMetadata var4 = this.configureInteractionEntity(var2, var3);
      if (var4 != null) {
         var1.addStaticPacket(var4);
      }

      var1.addStaticPacket(this.linkInteractionToAnchor(var2));
   }

   private void maintainActiveParticipants(Set<UUID> participants, SubHitboxRenderer renderer) {
      if (!var1.isEmpty()) {
         PacketBundleProvider var3 = this.buildMaintenanceBundle(var2);
         PacketTransmissionUtility.deliverBundleToMultiple(var1, var3);
      }
   }

   private PacketBundleProvider buildMaintenanceBundle(SubHitboxRenderer renderer) {
      PacketBundleProvider var2 = new PacketBundleProvider();
      Iterator var3 = var1.getRendered().values().iterator();

      SubHitboxRenderer.SubHitbox var4;
      while(var3.hasNext()) {
         var4 = (SubHitboxRenderer.SubHitbox)var3.next();
         PacketPlayOutEntityMetadata var5 = this.configureInteractionEntity(var4, false);
         if (var5 != null) {
            var2.addStaticPacket(var5);
         }

         if (var4.getPosition().isDirty()) {
            var2.add((var2x) -> {
               return this.repositionZoneAnchor(var4);
            });
            var4.getPosition().clearDirty();
         }
      }

      var3 = var1.getSpawnQueue().values().iterator();

      while(var3.hasNext()) {
         var4 = (SubHitboxRenderer.SubHitbox)var3.next();
         this.constructZonePackets(var2, var4, true);
      }

      Map var6 = var1.getDestroyQueue();
      if (!var6.isEmpty()) {
         IntArrayList var7 = this.collectZoneEntityIds(var6.values());
         var2.addStaticPacket(new PacketPlayOutEntityDestroy(var7));
      }

      return var2;
   }

   private IntArrayList collectZoneEntityIds(Collection<SubHitboxRenderer.SubHitbox> zones) {
      IntArrayList var2 = new IntArrayList(var1.size() * 2);
      var2.addAll(IntArrayList.toList(var1.stream().mapToInt(SubHitboxRenderer.SubHitbox::getPivotId)));
      var2.addAll(IntArrayList.toList(var1.stream().mapToInt(SubHitboxRenderer.SubHitbox::getHitboxId)));
      return var2;
   }

   private void deregisterLostParticipants(Set<UUID> participants, SubHitboxRenderer renderer) {
      if (!var1.isEmpty()) {
         IntArrayList var3 = this.gatherAllZoneIds(var2.getRendered().values());
         PacketTransmissionUtility.broadcastToPlayers(var1, new PacketPlayOutEntityDestroy(var3));
      }
   }

   private IntArrayList gatherAllZoneIds(Collection<SubHitboxRenderer.SubHitbox> zones) {
      IntArrayList var2 = new IntArrayList(var1.size() * 2);
      var2.addAll(IntArrayList.toList(var1.stream().mapToInt(SubHitboxRenderer.SubHitbox::getPivotId)));
      var2.addAll(IntArrayList.toList(var1.stream().mapToInt(SubHitboxRenderer.SubHitbox::getHitboxId)));
      return var2;
   }

   private PacketPlayOutSpawnEntity createZoneAnchor(SubHitboxRenderer.SubHitbox zone) {
      Vector3f var2 = (Vector3f)var1.getPosition().get();
      return new PacketPlayOutSpawnEntity(var1.getPivotId(), var1.getPivotUuid(), (double)var2.x, (double)var2.y, (double)var2.z, 0.0F, 0.0F, EntityTypes.d, 0, Vec3D.c, 0.0D);
   }

   private PacketPlayOutEntityMetadata configureZoneAnchor(SubHitboxRenderer.SubHitbox zone) {
      return new PacketPlayOutEntityMetadata(var1.getPivotId(), EntityUtils.DEFAULT_AREA_EFFECT_CLOUD_DATA);
   }

   private ClientboundEntityPositionSyncPacket repositionZoneAnchor(SubHitboxRenderer.SubHitbox zone) {
      Vector3f var2 = (Vector3f)var1.getPosition().get();
      EntityRelationship var3 = EntityRelationship.of(var1.getPivotId());
      var3.o((double)var2.x, (double)var2.y, (double)var2.z);
      return new ClientboundEntityPositionSyncPacket(var3.ar(), PositionMoveRotation.a(var3), var3.aJ());
   }

   private PacketPlayOutMount linkInteractionToAnchor(SubHitboxRenderer.SubHitbox zone) {
      return new PacketPlayOutMount(EntityRelationship.of(var1.getPivotId(), var1.getHitboxId()));
   }

   private PacketPlayOutSpawnEntity createInteractionEntity(SubHitboxRenderer.SubHitbox zone) {
      Vector3f var2 = (Vector3f)var1.getPosition().get();
      return new PacketPlayOutSpawnEntity(var1.getHitboxId(), var1.getHitboxUuid(), (double)var2.x, (double)var2.y, (double)var2.z, 0.0F, 0.0F, EntityTypes.ao, 0, Vec3D.c, 0.0D);
   }

   private PacketPlayOutEntityMetadata configureInteractionEntity(SubHitboxRenderer.SubHitbox zone, boolean initial) {
      if (!this.shouldUpdateInteraction(var1, var2)) {
         return null;
      } else {
         ArrayList var3 = this.buildInteractionProperties(var1, var2);
         var1.clearDirty();
         return new PacketPlayOutEntityMetadata(var1.getHitboxId(), var3);
      }
   }

   private boolean shouldUpdateInteraction(SubHitboxRenderer.SubHitbox zone, boolean initial) {
      return var2 || var1.isDirty();
   }

   private ArrayList<c<?>> buildInteractionProperties(SubHitboxRenderer.SubHitbox zone, boolean initial) {
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
      return var3;
   }
}
