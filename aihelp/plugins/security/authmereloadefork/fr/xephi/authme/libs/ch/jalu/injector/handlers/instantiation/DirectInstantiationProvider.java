package fr.xephi.authme.libs.ch.jalu.injector.handlers.instantiation;

import fr.xephi.authme.libs.ch.jalu.injector.context.ResolutionContext;
import fr.xephi.authme.libs.ch.jalu.injector.handlers.Handler;
import fr.xephi.authme.libs.ch.jalu.injector.utils.InjectorUtils;
import javax.annotation.Nullable;

public abstract class DirectInstantiationProvider implements Handler {
   public final Resolution<?> resolve(ResolutionContext context) {
      Class<?> clazz = context.getIdentifier().getTypeAsClass();
      return InjectorUtils.canInstantiate(clazz) ? this.safeGet(clazz) : null;
   }

   @Nullable
   protected abstract <T> Resolution<T> safeGet(Class<T> var1);
}
