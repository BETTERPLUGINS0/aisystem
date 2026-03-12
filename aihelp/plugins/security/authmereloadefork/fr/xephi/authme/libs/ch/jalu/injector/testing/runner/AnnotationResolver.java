package fr.xephi.authme.libs.ch.jalu.injector.testing.runner;

import fr.xephi.authme.libs.ch.jalu.injector.context.ResolutionContext;
import fr.xephi.authme.libs.ch.jalu.injector.handlers.Handler;
import fr.xephi.authme.libs.ch.jalu.injector.handlers.instantiation.Resolution;
import fr.xephi.authme.libs.ch.jalu.injector.handlers.instantiation.SimpleResolution;
import fr.xephi.authme.libs.ch.jalu.injector.testing.InjectDelayed;
import fr.xephi.authme.libs.ch.jalu.injector.utils.ReflectionUtils;
import fr.xephi.authme.libs.javax.inject.Inject;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.annotation.Nullable;
import org.junit.runners.model.FrameworkField;
import org.junit.runners.model.TestClass;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;

public class AnnotationResolver implements Handler {
   private final TestClass testClass;
   private final Object target;
   private final Set<Class<? extends Annotation>> ignoredAnnotations;

   public AnnotationResolver(TestClass testClass, Object target) {
      this(testClass, target, Inject.class, InjectMocks.class, Mock.class, Spy.class, InjectDelayed.class);
   }

   @SafeVarargs
   public AnnotationResolver(TestClass testClass, Object target, Class<? extends Annotation>... ignoredAnnotations) {
      this.testClass = testClass;
      this.target = target;
      this.ignoredAnnotations = Collections.unmodifiableSet(new HashSet(Arrays.asList(ignoredAnnotations)));
   }

   public Resolution<?> resolve(ResolutionContext context) {
      Class<?> clazz = context.getIdentifier().getTypeAsClass();
      Iterator var3 = context.getIdentifier().getAnnotations().iterator();

      Object o;
      do {
         if (!var3.hasNext()) {
            return null;
         }

         Annotation annotation = (Annotation)var3.next();
         o = this.resolveByAnnotation(annotation.annotationType(), clazz);
      } while(o == null);

      return new SimpleResolution(o);
   }

   @Nullable
   private Object resolveByAnnotation(Class<? extends Annotation> annotation, Class<?> type) {
      if (!this.ignoredAnnotations.contains(annotation)) {
         List<FrameworkField> fields = this.testClass.getAnnotatedFields(annotation);
         Iterator var4 = fields.iterator();

         while(var4.hasNext()) {
            FrameworkField field = (FrameworkField)var4.next();
            if (type.isAssignableFrom(field.getType())) {
               return ReflectionUtils.getFieldValue(field.getField(), this.target);
            }
         }
      }

      return null;
   }
}
