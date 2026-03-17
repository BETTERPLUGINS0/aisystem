package advancedplugins.pm2.cv.models.api.model.rpc.joint.renderer.renderer;

import java.util.Map;

public interface RenderQueues<T> {
   Map<String, T> getSpawnQueue();

   Map<String, T> getRendered();

   Map<String, T> getDestroyQueue();

   default T getQueued(String id) {
      T queued = this.getSpawnQueue().get(id);
      return queued != null ? queued : this.getRendered().get(id);
   }
}
