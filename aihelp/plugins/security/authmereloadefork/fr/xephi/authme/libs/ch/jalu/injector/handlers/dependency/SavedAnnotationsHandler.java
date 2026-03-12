package fr.xephi.authme.libs.ch.jalu.injector.handlers.dependency;

import fr.xephi.authme.libs.ch.jalu.injector.context.ResolutionContext;
import fr.xephi.authme.libs.ch.jalu.injector.exceptions.InjectorException;
import fr.xephi.authme.libs.ch.jalu.injector.handlers.Handler;
import fr.xephi.authme.libs.ch.jalu.injector.handlers.instantiation.Resolution;
import fr.xephi.authme.libs.ch.jalu.injector.handlers.instantiation.SimpleResolution;
import fr.xephi.authme.libs.ch.jalu.injector.utils.InjectorUtils;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class SavedAnnotationsHandler implements Handler {
   private Map<Class<?>, Object> storedValues = new HashMap();

   public Resolution<?> resolve(ResolutionContext context) {
      Iterator var3 = context.getIdentifier().getAnnotations().iterator();

      Object o;
      Annotation annotation;
      do {
         if (!var3.hasNext()) {
            return null;
         }

         annotation = (Annotation)var3.next();
      } while((o = this.storedValues.get(annotation.annotationType())) == null);

      return new SimpleResolution(o);
   }

   public void onAnnotation(Class<? extends Annotation> annotation, Object object) {
      InjectorUtils.checkNotNull(object, "Object may not be null");
      if (this.storedValues.containsKey(annotation)) {
         throw new InjectorException("Value already registered for @" + annotation.getSimpleName());
      } else {
         this.storedValues.put(annotation, object);
      }
   }
}
