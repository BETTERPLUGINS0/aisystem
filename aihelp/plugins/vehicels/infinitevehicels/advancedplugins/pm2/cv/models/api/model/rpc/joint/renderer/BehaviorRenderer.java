package advancedplugins.pm2.cv.models.api.model.rpc.joint.renderer;

import advancedplugins.pm2.cv.models.api.model.rpc.IVisualModel;
import advancedplugins.pm2.cv.models.api.model.rpc.renderer.ModelRenderer;
import advancedplugins.pm2.cv.models.api.nms.RenderParsers;

public interface BehaviorRenderer {
   ModelRenderer getModelRenderer();

   void setModelRenderer(ModelRenderer var1);

   IVisualModel getVisualModel();

   void initialize();

   void readJointData();

   void sendToClient(RenderParsers var1);

   void destroy(RenderParsers var1);
}
