package advancedplugins.pm2.cv.models.v1_21_R10.parser.behavior;

import advancedplugins.pm2.cv.models.api.model.rpc.entity.data.IEntityData;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.renderer.BehaviorRendererParser;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.renderer.renderer.LeashRenderer;
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
import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityLinkPacket;
import net.minecraft.network.protocol.game.ClientboundSetPassengersPacket;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public class ConnectionLinkHandler implements BehaviorRendererParser<LeashRenderer> {
   private static final float ANCHOR_Y_OFFSET = -0.45F;
   private static final float ANCHOR_Z_OFFSET = -0.2F;

   public void sendToClients(LeashRenderer var1) {
      IEntityData var2 = this.retrieveActiveData(var1);
      this.synchronizeActiveConnections(var2.getTracking().keySet(), var1);
      this.establishNewConnections(var2.getStartTracking(), var1);
      Set var3 = this.filterDisconnectedViewers(var2);
      this.terminateConnections(var3, var1);
   }

   public void destroy(LeashRenderer var1) {
      IEntityData var2 = this.retrieveVisualData(var1);
      Set var3 = this.mergeViewerGroups(var2);
      this.terminateConnections(var3, var1);
   }

   private IEntityData retrieveActiveData(LeashRenderer var1) {
      return var1.getModelRenderer().getActiveModel().getModeledEntity().getBase().getData();
   }

   private IEntityData retrieveVisualData(LeashRenderer var1) {
      return var1.getVisualModel().getModeledEntity().getBase().getData();
   }

   private Set<UUID> filterDisconnectedViewers(IEntityData var1) {
      HashSet var2 = new HashSet(var1.getStopTracking());
      var2.removeAll(var1.getTracking().keySet());
      return var2;
   }

   private Set<UUID> mergeViewerGroups(IEntityData var1) {
      HashSet var2 = new HashSet();
      var2.addAll(var1.getStartTracking());
      var2.addAll(var1.getTracking().keySet());
      var2.addAll(var1.getStopTracking());
      return var2;
   }

   private void establishNewConnections(Set<UUID> var1, LeashRenderer var2) {
      if (!var1.isEmpty()) {
         PacketBundleProvider var3 = this.assembleConnectionPackets(var2);
         PacketTransmissionUtility.deliverBundleToMultiple(var1, var3);
      }
   }

   private PacketBundleProvider assembleConnectionPackets(LeashRenderer var1) {
      PacketBundleProvider var2 = new PacketBundleProvider();
      Iterator var3 = var1.getRendered().values().iterator();

      LeashRenderer.Leash var4;
      while(var3.hasNext()) {
         var4 = (LeashRenderer.Leash)var3.next();
         this.constructLinkPackets(var2, var4, true);
      }

      var3 = var1.getSpawnQueue().values().iterator();

      while(var3.hasNext()) {
         var4 = (LeashRenderer.Leash)var3.next();
         this.constructLinkPackets(var2, var4, true);
      }

      return var2;
   }

   private void constructLinkPackets(PacketBundleProvider var1, LeashRenderer.Leash var2, boolean var3) {
      Vector3f var4 = this.calculateAnchorPosition(var2);
      var1.add(PacketTransmissionUtility.generateAnchorSpawn(var2.getPivotId(), var2.getPivotUUID(), var4));
      var1.addStaticPacket(this.applyAnchorConfiguration(var2));
      var1.addStaticPacket(this.createAttachmentEntity(var2));
      var1.addStaticPacket(this.applyAttachmentConfiguration(var2));
      var1.addStaticPacket(this.establishHierarchy(var2));
      ClientboundSetEntityLinkPacket var5 = this.defineConnection(var2, var3);
      if (var5 != null) {
         var1.addStaticPacket(var5);
      }

   }

   private void synchronizeActiveConnections(Set<UUID> var1, LeashRenderer var2) {
      if (!var1.isEmpty()) {
         PacketBundleProvider var3 = this.buildUpdatePackets(var2);
         PacketTransmissionUtility.deliverBundleToMultiple(var1, var3);
      }
   }

   private PacketBundleProvider buildUpdatePackets(LeashRenderer var1) {
      PacketBundleProvider var2 = new PacketBundleProvider();
      Iterator var3 = var1.getRendered().values().iterator();

      LeashRenderer.Leash var4;
      while(var3.hasNext()) {
         var4 = (LeashRenderer.Leash)var3.next();
         ClientboundSetEntityLinkPacket var5 = this.defineConnection(var4, false);
         if (var5 != null) {
            var2.addStaticPacket(var5);
         }

         if (var4.getPosition().isDirty()) {
            Vector3f var6 = this.calculateAnchorPosition(var4);
            var2.add(PacketTransmissionUtility.generateAnchorRelocation(var4.getPivotId(), var6));
         }
      }

      var3 = var1.getSpawnQueue().values().iterator();

      while(var3.hasNext()) {
         var4 = (LeashRenderer.Leash)var3.next();
         this.constructLinkPackets(var2, var4, true);
      }

      Map var7 = var1.getDestroyQueue();
      if (!var7.isEmpty()) {
         IntArrayList var8 = this.collectLinkEntityIds(var7.values());
         var2.addStaticPacket(new ClientboundRemoveEntitiesPacket(var8));
      }

      return var2;
   }

   private void terminateConnections(Set<UUID> var1, LeashRenderer var2) {
      if (!var1.isEmpty()) {
         IntArrayList var3 = this.collectAllEntityIds(var2.getRendered().values());
         PacketTransmissionUtility.broadcastToPlayers(var1, new ClientboundRemoveEntitiesPacket(var3));
      }
   }

   private IntArrayList collectLinkEntityIds(Collection<LeashRenderer.Leash> var1) {
      return IntArrayList.toList(var1.stream().mapMultiToInt((var0, var1x) -> {
         var1x.accept(var0.getLeashId());
         var1x.accept(var0.getPivotId());
      }));
   }

   private IntArrayList collectAllEntityIds(Collection<LeashRenderer.Leash> var1) {
      return IntArrayList.toList(var1.stream().mapMultiToInt((var0, var1x) -> {
         var1x.accept(var0.getLeashId());
         var1x.accept(var0.getPivotId());
      }));
   }

   private Vector3f calculateAnchorPosition(LeashRenderer.Leash var1) {
      Vector3f var2 = (Vector3f)var1.getPosition().get();
      return var2.add(0.0F, -0.45F, -0.2F, new Vector3f());
   }

   private ClientboundAddEntityPacket createAttachmentEntity(LeashRenderer.Leash var1) {
      Vector3f var2 = (Vector3f)var1.getPosition().get();
      double var3 = (double)(var2.y + -0.45F);
      double var5 = (double)(var2.z + -0.2F);
      return new ClientboundAddEntityPacket(var1.getLeashId(), var1.getLeastUUID(), (double)var2.x, var3, var5, 0.0F, 0.0F, EntityType.BAT, 0, Vec3.ZERO, 0.0D);
   }

   private ClientboundSetEntityDataPacket applyAnchorConfiguration(LeashRenderer.Leash var1) {
      return new ClientboundSetEntityDataPacket(var1.getPivotId(), EntityUtils.DEFAULT_AREA_EFFECT_CLOUD_DATA);
   }

   private ClientboundSetEntityDataPacket applyAttachmentConfiguration(LeashRenderer.Leash var1) {
      return new ClientboundSetEntityDataPacket(var1.getLeashId(), EntityUtils.DEFAULT_BAT_DATA);
   }

   private ClientboundSetPassengersPacket establishHierarchy(LeashRenderer.Leash var1) {
      ArrayList var2 = new ArrayList(1);
      var2.add(var1.getLeashId());
      return new ClientboundSetPassengersPacket(EntityRelationship.of(var1.getPivotId(), (Collection)var2));
   }

   private ClientboundSetEntityLinkPacket defineConnection(LeashRenderer.Leash var1, boolean var2) {
      if (!var2 && !var1.getConnected().isDirty()) {
         return null;
      } else {
         var1.getConnected().clearDirty();
         return new ClientboundSetEntityLinkPacket(EntityRelationship.of(var1.getLeashId()), EntityRelationship.of((Integer)var1.getConnected().get()));
      }
   }
}
