package advancedplugins.pm2.cv.models.api.model.rpc.joint.renderer;

public interface BehaviorRendererParser<T extends BehaviorRenderer> {
   void sendToClients(T var1);

   void destroy(T var1);
}
