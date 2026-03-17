package advancedplugins.pm2.cv.models.api.model.rpc.renderer;

import advancedplugins.pm2.cv.models.api.model.rpc.IVisualModel;
import advancedplugins.pm2.cv.models.api.nms.RenderParsers;

public interface ModelRenderer {
   IVisualModel getActiveModel();

   boolean isReady();

   void init();

   void readModelData();

   void dispatch(RenderParsers var1);

   void dispose(RenderParsers var1);

   boolean pollFirstSpawn();
}
