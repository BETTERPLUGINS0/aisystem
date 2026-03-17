package advancedplugins.pm2.cv.models.core.model.rpc.visual.renderer;

import advancedplugins.pm2.cv.models.api.ModelAPI;
import advancedplugins.pm2.cv.models.api.model.rpc.visual.Visual;
import advancedplugins.pm2.cv.models.api.model.rpc.visual.renderer.VisualDisplayRenderer;
import advancedplugins.pm2.cv.models.api.model.rpc.visual.renderer.VisualRendererParser;
import advancedplugins.pm2.cv.models.api.nms.entity.EntityHandler;
import advancedplugins.pm2.cv.models.api.utils.data.tracker.DataTracker;
import advancedplugins.pm2.cv.models.api.utils.data.tracker.UpdateDataTracker;
import java.util.UUID;
import lombok.Generated;
import org.bukkit.inventory.ItemStack;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class VisualDisplayRendererImpl implements VisualDisplayRenderer {
   private final Visual visual;
   private final EntityHandler entityHandler;
   private final VisualRendererParser<VisualDisplayRenderer> parser;
   private VisualDisplayRendererImpl.RendererVisualModelImpl rendererVisualModel;
   private boolean respawnRequired;
   private boolean initialized;

   public VisualDisplayRendererImpl(Visual var1) {
      this.visual = var1;
      this.entityHandler = ModelAPI.getEntityHandler();
      this.parser = ModelAPI.getNMSHandler().getVFXRendererParser(this);
      (new RuntimeException()).printStackTrace();
   }

   public void init() {
      this.rendererVisualModel = new VisualDisplayRendererImpl.RendererVisualModelImpl(this.entityHandler.getNextEntityId(), UUID.randomUUID(), this.entityHandler.getNextEntityId(), UUID.randomUUID());
      this.rendererVisualModel.getOrigin().set(this.visual.getPosition().getOrigin());
      this.rendererVisualModel.getPosition().set(this.calculatePosition());
      this.rendererVisualModel.getLeftRotation().set(this.calculateRotation());
      this.rendererVisualModel.getScale().set(this.visual.getPosition().getScale());
      this.rendererVisualModel.getModel().set(this.visual.isVisible() ? this.visual.getModel() : null);
      this.initialized = true;
      this.respawnRequired = true;
   }

   public void readData() {
      if (this.initialized) {
         this.rendererVisualModel.getOrigin().set(this.visual.getPosition().getOrigin());
         this.rendererVisualModel.getPosition().set(this.calculatePosition());
         this.rendererVisualModel.getLeftRotation().set(this.calculateRotation());
         this.rendererVisualModel.getScale().set(this.visual.getPosition().getScale());
         if (!this.visual.isVisible()) {
            this.rendererVisualModel.getModel().set((Object)null);
         } else {
            this.rendererVisualModel.getModel().set(this.visual.getModel());
            if (this.visual.getModelTracker().isDirty()) {
               this.visual.getModelTracker().clearDirty();
               this.rendererVisualModel.getModel().markDirty();
            }
         }
      }

   }

   public void dispatch() {
      if (this.initialized) {
         this.parser.dispatch(this);
      }

   }

   public void dispose() {
      if (this.initialized) {
         this.parser.dispose(this);
      }

   }

   public VisualDisplayRenderer.RendererVisualModel getVisualModel() {
      return this.rendererVisualModel;
   }

   private Vector3f calculatePosition() {
      float var1 = this.visual.getPosition().getYaw();
      float var2 = this.visual.getPosition().getPitch();
      Vector3f var3 = this.visual.getPosition().getXYZ();
      return (new Vector3f(var3)).rotateX(var2 * 0.017453292F).rotateY(-var1 * 0.017453292F);
   }

   private Quaternionf calculateRotation() {
      float var1 = this.visual.getPosition().getYaw();
      float var2 = this.visual.getPosition().getPitch();
      Vector3f var3 = this.visual.getPosition().getRotation();
      return (new Quaternionf()).rotateY((180.0F - var1) * 0.017453292F).rotateX(-var2 * 0.017453292F).rotateZYX(var3.z, var3.y, var3.x);
   }

   public boolean isReady() {
      return this.initialized;
   }

   @Generated
   public Visual getVisual() {
      return this.visual;
   }

   @Generated
   public EntityHandler getEntityHandler() {
      return this.entityHandler;
   }

   @Generated
   public VisualRendererParser<VisualDisplayRenderer> getParser() {
      return this.parser;
   }

   @Generated
   public VisualDisplayRendererImpl.RendererVisualModelImpl getRendererVisualModel() {
      return this.rendererVisualModel;
   }

   @Generated
   public boolean isRespawnRequired() {
      return this.respawnRequired;
   }

   @Generated
   public boolean isInitialized() {
      return this.initialized;
   }

   @Generated
   public void setRendererVisualModel(VisualDisplayRendererImpl.RendererVisualModelImpl var1) {
      this.rendererVisualModel = var1;
   }

   @Generated
   public void setRespawnRequired(boolean var1) {
      this.respawnRequired = var1;
   }

   @Generated
   public void setInitialized(boolean var1) {
      this.initialized = var1;
   }

   public static class RendererVisualModelImpl implements VisualDisplayRenderer.RendererVisualModel {
      private final int pivotId;
      private final UUID pivotUuid;
      private final int modelId;
      private final UUID modelUuid;
      private final DataTracker<Vector3f> origin = new UpdateDataTracker(new Vector3f(), Vector3f::set);
      private final DataTracker<Vector3f> position = new UpdateDataTracker(new Vector3f(), Vector3f::set);
      private final DataTracker<Quaternionf> leftRotation = new UpdateDataTracker(new Quaternionf(), Quaternionf::set);
      private final DataTracker<Vector3f> scale = new UpdateDataTracker(new Vector3f(), Vector3f::set);
      private final DataTracker<ItemStack> model = new DataTracker();

      public RendererVisualModelImpl(int var1, UUID var2, int var3, UUID var4) {
         this.pivotId = var1;
         this.pivotUuid = var2;
         this.modelId = var3;
         this.modelUuid = var4;
         (new RuntimeException()).printStackTrace();
      }

      @Generated
      public int getPivotId() {
         return this.pivotId;
      }

      @Generated
      public UUID getPivotUuid() {
         return this.pivotUuid;
      }

      @Generated
      public int getModelId() {
         return this.modelId;
      }

      @Generated
      public UUID getModelUuid() {
         return this.modelUuid;
      }

      @Generated
      public DataTracker<Vector3f> getOrigin() {
         return this.origin;
      }

      @Generated
      public DataTracker<Vector3f> getPosition() {
         return this.position;
      }

      @Generated
      public DataTracker<Quaternionf> getLeftRotation() {
         return this.leftRotation;
      }

      @Generated
      public DataTracker<Vector3f> getScale() {
         return this.scale;
      }

      @Generated
      public DataTracker<ItemStack> getModel() {
         return this.model;
      }
   }
}
