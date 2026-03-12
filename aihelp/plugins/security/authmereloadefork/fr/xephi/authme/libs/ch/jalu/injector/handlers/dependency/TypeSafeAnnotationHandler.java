package fr.xephi.authme.libs.ch.jalu.injector.handlers.dependency;

import fr.xephi.authme.libs.ch.jalu.injector.context.ResolutionContext;
import fr.xephi.authme.libs.ch.jalu.injector.handlers.Handler;
import fr.xephi.authme.libs.ch.jalu.injector.handlers.instantiation.Resolution;
import java.lang.annotation.Annotation;
import java.util.Iterator;
import javax.annotation.Nullable;

public abstract class TypeSafeAnnotationHandler<T extends Annotation> implements Handler {
   public final Resolution<?> resolve(ResolutionContext context) throws Exception {
      Class<T> type = this.getAnnotationType();
      Iterator var3 = context.getIdentifier().getAnnotations().iterator();

      Annotation annotation;
      do {
         if (!var3.hasNext()) {
            return null;
         }

         annotation = (Annotation)var3.next();
      } while(!type.isInstance(annotation));

      return this.resolveValueSafely(context, (Annotation)type.cast(annotation));
   }

   protected abstract Class<T> getAnnotationType();

   @Nullable
   protected abstract Resolution<?> resolveValueSafely(ResolutionContext var1, T var2) throws Exception;
}
