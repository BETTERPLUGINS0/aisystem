package advancedplugins.pm2.cv.models.v1_21_R3.parser.behavior;

import advancedplugins.pm2.cv.models.api.model.rpc.entity.data.IEntityData;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.renderer.BehaviorRendererParser;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.renderer.renderer.NameTagRenderer;
import advancedplugins.pm2.cv.models.v1_21_R3.entity.EntityRelationship;
import advancedplugins.pm2.cv.models.v1_21_R3.entity.EntityUtils;
import advancedplugins.pm2.cv.models.v1_21_R3.network.utils.PacketBundleProvider;
import advancedplugins.pm2.cv.models.v1_21_R3.network.utils.PacketTransmissionUtility;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import net.minecraft.network.chat.IChatBaseComponent;
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
import org.bukkit.entity.Display.Billboard;
import org.joml.Vector3f;

public class TextLabelController implements BehaviorRendererParser<NameTagRenderer> {
   private static final float DEFAULT_VIEW_DISTANCE = 4096.0F;
   private static final float HIDDEN_VIEW_DISTANCE = 0.0F;

   public void sendToClients(NameTagRenderer renderer) {
      IEntityData var2 = this.extractViewerInfo(var1);
      this.refreshExistingViewers(var2.getTracking().keySet(), var1);
      this.registerNewViewers(var2.getStartTracking(), var1);
      this.unregisterLostViewers(var2.getStopTracking(), var1);
   }

   public void destroy(NameTagRenderer renderer) {
      IEntityData var2 = this.extractVisualViewerInfo(var1);
      Set var3 = this.aggregateAllViewers(var2);
      this.unregisterLostViewers(var3, var1);
   }

   private IEntityData extractViewerInfo(NameTagRenderer renderer) {
      return var1.getModelRenderer().getActiveModel().getModeledEntity().getBase().getData();
   }

   private IEntityData extractVisualViewerInfo(NameTagRenderer renderer) {
      return var1.getVisualModel().getModeledEntity().getBase().getData();
   }

   private Set<UUID> aggregateAllViewers(IEntityData data) {
      HashSet var2 = new HashSet();
      var2.addAll(var1.getStartTracking());
      var2.addAll(var1.getTracking().keySet());
      var2.addAll(var1.getStopTracking());
      return var2;
   }

   private void registerNewViewers(Set<UUID> viewers, NameTagRenderer renderer) {
      if (!var1.isEmpty()) {
         PacketBundleProvider var3 = this.compileRegistrationPackets(var2);
         PacketTransmissionUtility.deliverBundleToMultiple(var1, var3);
      }
   }

   private PacketBundleProvider compileRegistrationPackets(NameTagRenderer renderer) {
      PacketBundleProvider var2 = new PacketBundleProvider();
      Iterator var3 = var1.getRendered().values().iterator();

      NameTagRenderer.NameTag var4;
      while(var3.hasNext()) {
         var4 = (NameTagRenderer.NameTag)var3.next();
         this.assembleLabelPackets(var2, var4, true);
      }

      var3 = var1.getSpawnQueue().values().iterator();

      while(var3.hasNext()) {
         var4 = (NameTagRenderer.NameTag)var3.next();
         this.assembleLabelPackets(var2, var4, true);
      }

      return var2;
   }

   private void assembleLabelPackets(PacketBundleProvider packets, NameTagRenderer.NameTag label, boolean initial) {
      var1.add(PacketTransmissionUtility.generateAnchorSpawn(var2.getPivotId(), var2.getPivotUuid(), (Vector3f)var2.getPosition().get()));
      PacketPlayOutEntityMetadata var4 = this.configureHolderProperties(var2, var3);
      if (var4 != null) {
         var1.addStaticPacket(var4);
      }

      var1.addStaticPacket(this.createDisplaySpawn(var2));
      var1.addStaticPacket(this.configureDisplayProperties(var2, var3));
      var1.addStaticPacket(this.linkDisplayToHolder(var2));
   }

   private void refreshExistingViewers(Set<UUID> viewers, NameTagRenderer renderer) {
      if (!var1.isEmpty()) {
         PacketBundleProvider var3 = this.buildUpdatePackets(var2);
         PacketTransmissionUtility.deliverBundleToMultiple(var1, var3);
      }
   }

   private PacketBundleProvider buildUpdatePackets(NameTagRenderer renderer) {
      PacketBundleProvider var2 = new PacketBundleProvider();
      Iterator var3 = var1.getRendered().values().iterator();

      NameTagRenderer.NameTag var4;
      while(var3.hasNext()) {
         var4 = (NameTagRenderer.NameTag)var3.next();
         PacketPlayOutEntityMetadata var5 = this.configureDisplayProperties(var4, false);
         if (var5 != null) {
            var2.addStaticPacket(var5);
         }

         if (var4.getPosition().isDirty()) {
            var2.add(PacketTransmissionUtility.generateAnchorRelocation(var4.getPivotId(), (Vector3f)var4.getPosition().get()));
            var4.getPosition().clearDirty();
         }
      }

      var3 = var1.getSpawnQueue().values().iterator();

      while(var3.hasNext()) {
         var4 = (NameTagRenderer.NameTag)var3.next();
         this.assembleLabelPackets(var2, var4, true);
      }

      Map var6 = var1.getDestroyQueue();
      if (!var6.isEmpty()) {
         IntArrayList var7 = this.collectLabelEntityIds(var6.values());
         var2.addStaticPacket(new PacketPlayOutEntityDestroy(var7));
      }

      return var2;
   }

   private IntArrayList collectLabelEntityIds(Collection<NameTagRenderer.NameTag> labels) {
      IntArrayList var2 = new IntArrayList(var1.size() * 2);
      var2.addAll(IntArrayList.toList(var1.stream().mapToInt(NameTagRenderer.NameTag::getPivotId)));
      var2.addAll(IntArrayList.toList(var1.stream().mapToInt(NameTagRenderer.NameTag::getTagId)));
      return var2;
   }

   private void unregisterLostViewers(Set<UUID> viewers, NameTagRenderer renderer) {
      if (!var1.isEmpty()) {
         IntArrayList var3 = this.gatherAllLabelIds(var2.getRendered().values());
         PacketTransmissionUtility.broadcastToPlayers(var1, new PacketPlayOutEntityDestroy(var3));
      }
   }

   private IntArrayList gatherAllLabelIds(Collection<NameTagRenderer.NameTag> labels) {
      IntArrayList var2 = new IntArrayList(var1.size() * 2);
      var2.addAll(IntArrayList.toList(var1.stream().mapToInt(NameTagRenderer.NameTag::getPivotId)));
      var2.addAll(IntArrayList.toList(var1.stream().mapToInt(NameTagRenderer.NameTag::getTagId)));
      return var2;
   }

   private PacketPlayOutEntityMetadata configureHolderProperties(NameTagRenderer.NameTag label, boolean initial) {
      if (!this.shouldUpdateHolder(var1, var2)) {
         return null;
      } else {
         List var3 = var2 ? EntityUtils.DEFAULT_AREA_EFFECT_CLOUD_DATA : List.of();
         var1.clearDirty();
         return new PacketPlayOutEntityMetadata(var1.getPivotId(), var3);
      }
   }

   private boolean shouldUpdateHolder(NameTagRenderer.NameTag label, boolean initial) {
      return var2 || var1.isDirty();
   }

   private PacketPlayOutSpawnEntity createDisplaySpawn(NameTagRenderer.NameTag label) {
      Vector3f var2 = (Vector3f)var1.getPosition().get();
      return new PacketPlayOutSpawnEntity(var1.getTagId(), var1.getTagUuid(), (double)var2.x, (double)var2.y, (double)var2.z, 0.0F, 0.0F, EntityTypes.bu, 0, Vec3D.c, 0.0D);
   }

   private PacketPlayOutEntityMetadata configureDisplayProperties(NameTagRenderer.NameTag label, boolean initial) {
      if (!this.shouldUpdateDisplay(var1, var2)) {
         return null;
      } else {
         ArrayList var3 = this.buildDisplayProperties(var1, var2);
         var1.clearDirty();
         return new PacketPlayOutEntityMetadata(var1.getTagId(), var3);
      }
   }

   private boolean shouldUpdateDisplay(NameTagRenderer.NameTag label, boolean initial) {
      return var2 || var1.isDirty();
   }

   private ArrayList<c<?>> buildDisplayProperties(NameTagRenderer.NameTag label, boolean initial) {
      ArrayList var3 = new ArrayList(7);
      var1.getVisibility().ifDirty((var1x) -> {
         var3.add(new c(17, DataWatcherRegistry.d, var1x ? 4096.0F : 0.0F));
      }, var2);
      var1.getScale().ifDirty((var1x) -> {
         var3.add(new c(12, DataWatcherRegistry.D, var1x));
      }, var2);
      var1.getTextOpacity().ifDirty((var1x) -> {
         var3.add(new c(26, DataWatcherRegistry.a, var1x));
      }, var2);
      var1.getBackgroundColor().ifDirty((var1x) -> {
         var3.add(new c(25, DataWatcherRegistry.b, var1x));
      }, var2);
      var1.getStyle().ifDirty((var1x) -> {
         var3.add(new c(27, DataWatcherRegistry.a, var1x));
      }, var2);
      var1.getBillboard().ifDirty((var2x) -> {
         byte var3x = this.convertBillboardType(var2x);
         var3.add(new c(15, DataWatcherRegistry.a, var3x));
      }, var2);
      var1.getJsonString().ifDirty((var1x) -> {
         if (var1x == null) {
            var3.add(new c(23, DataWatcherRegistry.f, IChatBaseComponent.i()));
         }

      }, var2);
      return var3;
   }

   private byte convertBillboardType(Billboard billboard) {
      switch(var1) {
      case FIXED:
         return 0;
      case VERTICAL:
         return 1;
      case HORIZONTAL:
         return 2;
      case CENTER:
         return 3;
      default:
         throw new RuntimeException("Unknown billboard type: " + String.valueOf(var1));
      }
   }

   private ClientboundEntityPositionSyncPacket repositionHolder(NameTagRenderer.NameTag label) {
      Vector3f var2 = (Vector3f)var1.getPosition().get();
      EntityRelationship var3 = EntityRelationship.of(var1.getPivotId());
      var3.o((double)var2.x, (double)var2.y, (double)var2.z);
      return new ClientboundEntityPositionSyncPacket(var3.ar(), PositionMoveRotation.a(var3), var3.aJ());
   }

   private PacketPlayOutMount linkDisplayToHolder(NameTagRenderer.NameTag label) {
      return new PacketPlayOutMount(EntityRelationship.of(var1.getPivotId(), var1.getTagId()));
   }
}
