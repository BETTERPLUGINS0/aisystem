package advancedplugins.pm2.cv.models.core.model.rpc.joint.renderer;

import advancedplugins.pm2.cv.models.api.ModelAPI;
import advancedplugins.pm2.cv.models.api.model.rpc.IVisualModel;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.renderer.BehaviorRenderer;
import advancedplugins.pm2.cv.models.api.model.rpc.renderer.ModelRenderer;
import advancedplugins.pm2.cv.models.api.nms.NMSHandler;
import lombok.Generated;

public abstract class AbstractBehaviorRenderer implements BehaviorRenderer {
   protected final IVisualModel visualModel;
   protected final NMSHandler nmsHandler = ModelAPI.getNMSHandler();
   protected ModelRenderer modelRenderer;

   public AbstractBehaviorRenderer(IVisualModel var1) {
      this.visualModel = var1;
   }

   @Generated
   public IVisualModel getVisualModel() {
      return this.visualModel;
   }

   @Generated
   public NMSHandler getNmsHandler() {
      return this.nmsHandler;
   }

   @Generated
   public ModelRenderer getModelRenderer() {
      return this.modelRenderer;
   }

   @Generated
   public void setModelRenderer(ModelRenderer var1) {
      this.modelRenderer = var1;
   }
}
