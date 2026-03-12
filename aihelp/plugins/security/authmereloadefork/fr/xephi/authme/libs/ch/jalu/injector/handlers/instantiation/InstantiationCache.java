package fr.xephi.authme.libs.ch.jalu.injector.handlers.instantiation;

import fr.xephi.authme.libs.ch.jalu.injector.context.ResolutionContext;
import fr.xephi.authme.libs.ch.jalu.injector.context.StandardResolutionType;
import fr.xephi.authme.libs.ch.jalu.injector.handlers.Handler;
import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.Nullable;

public class InstantiationCache implements Handler {
   protected Map<Class, WeakReference<Resolution>> entries = new ConcurrentHashMap();

   public Resolution<?> resolve(ResolutionContext context) {
      return this.getInstantiation(context);
   }

   public <T> T postProcess(T object, ResolutionContext context, Resolution<?> resolution) {
      if (this.shouldCacheMethod(context) && this.getInstantiation(context) == null) {
         this.entries.put(context.getIdentifier().getTypeAsClass(), new WeakReference(resolution));
      }

      return null;
   }

   @Nullable
   private <T> Resolution<? extends T> getInstantiation(ResolutionContext context) {
      WeakReference<Resolution> instantiation = (WeakReference)this.entries.get(context.getIdentifier().getTypeAsClass());
      return instantiation == null ? null : (Resolution)instantiation.get();
   }

   protected boolean shouldCacheMethod(ResolutionContext context) {
      return context.getIdentifier().getResolutionType() == StandardResolutionType.REQUEST_SCOPED;
   }
}
