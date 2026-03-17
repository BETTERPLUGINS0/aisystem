package advancedplugins.pm2.cv.models.api.model.rpc.visual.renderer;

import advancedplugins.pm2.cv.models.api.model.rpc.visual.Visual;

public interface VisualRenderer {
   Visual getVisual();

   boolean isReady();

   void init();

   void readData();

   void dispatch();

   void dispose();
}
