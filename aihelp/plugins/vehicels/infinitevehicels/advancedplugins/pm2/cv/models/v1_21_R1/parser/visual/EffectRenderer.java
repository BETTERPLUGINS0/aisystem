package advancedplugins.pm2.cv.models.v1_21_R1.parser.visual;

import advancedplugins.pm2.cv.models.api.model.rpc.entity.data.IEntityData;
import advancedplugins.pm2.cv.models.api.model.rpc.visual.renderer.VisualDisplayRenderer;
import advancedplugins.pm2.cv.models.api.model.rpc.visual.renderer.VisualRendererParser;
import advancedplugins.pm2.cv.models.v1_21_R1.entity.EntityRelationship;
import advancedplugins.pm2.cv.models.v1_21_R1.entity.EntityUtils;
import advancedplugins.pm2.cv.models.v1_21_R1.network.utils.PacketBundleProvider;
import advancedplugins.pm2.cv.models.v1_21_R1.network.utils.PacketTransmissionUtility;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy;
import net.minecraft.network.protocol.game.PacketPlayOutEntityMetadata;
import net.minecraft.network.protocol.game.PacketPlayOutMount;
import net.minecraft.network.protocol.game.PacketPlayOutSpawnEntity;
import net.minecraft.network.syncher.DataWatcherRegistry;
import net.minecraft.network.syncher.DataWatcher.c;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.phys.Vec3D;
import org.bukkit.craftbukkit.v1_21_R1.inventory.CraftItemStack;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class EffectRenderer implements VisualRendererParser<VisualDisplayRenderer> {
   private static final byte STANDARD_FLAGS = 32;
   private static final int INFINITE_LIFETIME = Integer.MAX_VALUE;
   private static final float MAX_VISIBILITY_RANGE = 4096.0F;

   public void dispatch(VisualDisplayRenderer renderer) {
      IEntityData var2 = this.extractViewerData(var1);
      if (var1.isRespawnRequired()) {
         this.establishEffect(var2.getTracking().keySet(), var1.getVisualModel());
         var1.setRespawnRequired(false);
      } else {
         this.refreshEffect(var2.getTracking().keySet(), var1.getVisualModel());
         this.establishEffect(var2.getStartTracking(), var1.getVisualModel());
         this.terminateEffect(var2.getStopTracking(), var1.getVisualModel());
      }

   }

   public void dispose(VisualDisplayRenderer renderer) {
      IEntityData var2 = this.extractViewerData(var1);
      Set var3 = this.consolidateViewers(var2);
      this.terminateEffect(var3, var1.getVisualModel());
   }

   private IEntityData extractViewerData(VisualDisplayRenderer renderer) {
      return var1.getVisual().getOriginal().getData();
   }

   private Set<UUID> consolidateViewers(IEntityData data) {
      HashSet var2 = new HashSet();
      var2.addAll(var1.getStartTracking());
      var2.addAll(var1.getTracking().keySet());
      var2.addAll(var1.getStopTracking());
      return var2;
   }

   public void establishEffect(Set<UUID> viewers, VisualDisplayRenderer.RendererVisualModel effect) {
      if (!var1.isEmpty()) {
         PacketBundleProvider var3 = this.createInitializationBundle(var2);
         PacketTransmissionUtility.deliverBundleToMultiple(var1, var3);
      }
   }

   private PacketBundleProvider createInitializationBundle(VisualDisplayRenderer.RendererVisualModel effect) {
      PacketBundleProvider var2 = new PacketBundleProvider();
      var2.add(PacketTransmissionUtility.generateAnchorSpawn(var1.getPivotId(), var1.getPivotUuid(), (Vector3f)var1.getOrigin().get()));
      var2.addStaticPacket(this.configureAnchor(var1));
      var2.addStaticPacket(this.createDisplayEntity(var1));
      var2.addStaticPacket(this.configureDisplay(var1, true));
      var2.addStaticPacket(this.establishHierarchy(var1));
      return var2;
   }

   public void refreshEffect(Set<UUID> viewers, VisualDisplayRenderer.RendererVisualModel effect) {
      if (!var1.isEmpty()) {
         PacketBundleProvider var3 = this.createUpdateBundle(var2);
         PacketTransmissionUtility.deliverBundleToMultiple(var1, var3);
      }
   }

   private PacketBundleProvider createUpdateBundle(VisualDisplayRenderer.RendererVisualModel effect) {
      PacketBundleProvider var2 = new PacketBundleProvider();
      if (var1.getOrigin().isDirty()) {
         var2.add(PacketTransmissionUtility.generateAnchorRelocation(var1.getPivotId(), (Vector3f)var1.getOrigin().get()));
      }

      PacketPlayOutEntityMetadata var3 = this.configureDisplay(var1, false);
      if (var3 != null) {
         var2.addStaticPacket(var3);
      }

      return var2;
   }

   public void terminateEffect(Set<UUID> viewers, VisualDisplayRenderer.RendererVisualModel effect) {
      if (!var1.isEmpty()) {
         int[] var3 = new int[]{var2.getPivotId(), var2.getModelId()};
         PacketTransmissionUtility.broadcastToPlayers(var1, new PacketPlayOutEntityDestroy(var3));
      }
   }

   private PacketPlayOutEntityMetadata configureAnchor(VisualDisplayRenderer.RendererVisualModel effect) {
      return new PacketPlayOutEntityMetadata(var1.getPivotId(), EntityUtils.DEFAULT_AREA_EFFECT_CLOUD_DATA);
   }

   private PacketPlayOutSpawnEntity createDisplayEntity(VisualDisplayRenderer.RendererVisualModel effect) {
      Vector3f var2 = (Vector3f)var1.getOrigin().get();
      return new PacketPlayOutSpawnEntity(var1.getModelId(), var1.getModelUuid(), (double)var2.x, (double)var2.y, (double)var2.z, 0.0F, 0.0F, EntityTypes.ah, 0, Vec3D.b, 0.0D);
   }

   private PacketPlayOutEntityMetadata configureDisplay(VisualDisplayRenderer.RendererVisualModel effect, boolean initial) {
      if (!this.shouldUpdateDisplay(var1, var2)) {
         return null;
      } else {
         ArrayList var3 = this.buildDisplayProperties(var1, var2);
         var1.clearModelDirty();
         return new PacketPlayOutEntityMetadata(var1.getModelId(), var3);
      }
   }

   private boolean shouldUpdateDisplay(VisualDisplayRenderer.RendererVisualModel effect, boolean initial) {
      return var2 || var1.isModelDirty();
   }

   private ArrayList<c<?>> buildDisplayProperties(VisualDisplayRenderer.RendererVisualModel effect, boolean initial) {
      ArrayList var3 = new ArrayList(9);
      if (var2) {
         this.addInitialProperties(var3);
      } else if (var1.isModelDirty()) {
         var3.add(new c(8, DataWatcherRegistry.b, 0));
      }

      this.addConditionalProperties(var3, var1, var2);
      return var3;
   }

   private void addInitialProperties(ArrayList<c<?>> properties) {
      var1.add(new c(0, DataWatcherRegistry.a, (byte)32));
      var1.add(new c(1, DataWatcherRegistry.b, Integer.MAX_VALUE));
      var1.add(new c(8, DataWatcherRegistry.b, 0));
      var1.add(new c(9, DataWatcherRegistry.b, 1));
      var1.add(new c(17, DataWatcherRegistry.d, 4096.0F));
   }

   private void addConditionalProperties(ArrayList<c<?>> properties, VisualDisplayRenderer.RendererVisualModel effect, boolean initial) {
      var2.getPosition().ifDirty((var1x) -> {
         var1.add(new c(11, DataWatcherRegistry.D, var1x));
      }, var3);
      var2.getScale().ifDirty((var1x) -> {
         var1.add(new c(12, DataWatcherRegistry.D, var1x));
      }, var3);
      var2.getLeftRotation().ifDirty((var1x) -> {
         Quaternionf var2 = var1x.rotateY(3.1415927F, new Quaternionf());
         var1.add(new c(13, DataWatcherRegistry.E, var2));
      }, var3);
      var2.getModel().ifDirty((var1x) -> {
         var1.add(new c(23, DataWatcherRegistry.h, CraftItemStack.asNMSCopy(var1x)));
      }, var3);
   }

   private PacketPlayOutMount establishHierarchy(VisualDisplayRenderer.RendererVisualModel effect) {
      return new PacketPlayOutMount(EntityRelationship.of(var1.getPivotId(), var1.getModelId()));
   }
}
