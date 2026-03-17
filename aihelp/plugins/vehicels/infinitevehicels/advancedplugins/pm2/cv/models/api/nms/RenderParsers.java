package advancedplugins.pm2.cv.models.api.nms;

import advancedplugins.pm2.cv.models.api.model.rpc.joint.renderer.BehaviorRenderer;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.renderer.BehaviorRendererParser;
import advancedplugins.pm2.cv.models.api.model.rpc.renderer.ModelRenderer;
import advancedplugins.pm2.cv.models.api.model.rpc.renderer.ModelRendererParser;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class RenderParsers {
   private final Map<Predicate<?>, Supplier<ModelRendererParser<?>>> modelParsers = new ConcurrentHashMap();
   private final Map<Predicate<?>, Supplier<BehaviorRendererParser<?>>> behaviorParsers = new ConcurrentHashMap();

   public void registerModelParser(Predicate<ModelRenderer> var1, Supplier<ModelRendererParser<?>> var2) {
      this.modelParsers.put(var1, var2);
   }

   public void registerBehaviorParser(Predicate<BehaviorRenderer> var1, Supplier<BehaviorRendererParser<?>> var2) {
      this.behaviorParsers.put(var1, var2);
   }

   public <T extends ModelRenderer> ModelRendererParser<T> getModelParser(T var1) {
      Iterator var2 = this.modelParsers.entrySet().iterator();

      while(var2.hasNext()) {
         Entry var3 = (Entry)var2.next();
         if (((Predicate)var3.getKey()).test(var1)) {
            return (ModelRendererParser)((Supplier)var3.getValue()).get();
         }
      }

      return null;
   }

   public <T extends BehaviorRenderer> BehaviorRendererParser<T> getBehaviorParser(T var1) {
      Iterator var2 = this.behaviorParsers.entrySet().iterator();

      while(var2.hasNext()) {
         Entry var3 = (Entry)var2.next();
         if (((Predicate)var3.getKey()).test(var1)) {
            return (BehaviorRendererParser)((Supplier)var3.getValue()).get();
         }
      }

      return null;
   }
}
