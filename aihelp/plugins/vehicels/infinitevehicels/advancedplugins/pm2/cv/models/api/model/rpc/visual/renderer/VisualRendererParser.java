package advancedplugins.pm2.cv.models.api.model.rpc.visual.renderer;

public interface VisualRendererParser<T extends VisualRenderer> {
   void dispatch(T var1);

   void dispose(T var1);
}
