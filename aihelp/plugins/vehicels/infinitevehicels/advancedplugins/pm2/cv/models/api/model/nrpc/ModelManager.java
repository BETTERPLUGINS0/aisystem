package advancedplugins.pm2.cv.models.api.model.nrpc;

import advancedplugins.pm2.cv.models.api.utils.archive.AbstractArchive;

public class ModelManager extends AbstractArchive<AbstractModel> {
   public void register(AbstractModel var1) {
      this.register(var1.getName(), var1);
   }
}
