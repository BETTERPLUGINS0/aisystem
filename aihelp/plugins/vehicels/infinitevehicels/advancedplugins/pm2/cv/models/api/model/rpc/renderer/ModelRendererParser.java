package advancedplugins.pm2.cv.models.api.model.rpc.renderer;

public interface ModelRendererParser<T extends ModelRenderer> {
   void dispatch(T var1);

   void dispose(T var1);
}
