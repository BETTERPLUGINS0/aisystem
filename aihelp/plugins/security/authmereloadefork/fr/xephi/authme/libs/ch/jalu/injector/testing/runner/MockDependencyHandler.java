package fr.xephi.authme.libs.ch.jalu.injector.testing.runner;

import fr.xephi.authme.libs.ch.jalu.injector.Injector;
import fr.xephi.authme.libs.ch.jalu.injector.context.ResolutionContext;
import fr.xephi.authme.libs.ch.jalu.injector.exceptions.InjectorException;
import fr.xephi.authme.libs.ch.jalu.injector.handlers.Handler;
import fr.xephi.authme.libs.ch.jalu.injector.handlers.instantiation.Resolution;
import fr.xephi.authme.libs.ch.jalu.injector.handlers.instantiation.SimpleResolution;
import fr.xephi.authme.libs.ch.jalu.injector.testing.InjectDelayed;
import fr.xephi.authme.libs.ch.jalu.injector.utils.ReflectionUtils;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.junit.runners.model.FrameworkField;
import org.junit.runners.model.TestClass;
import org.mockito.Mock;

public class MockDependencyHandler implements Handler {
   private final TestClass testClass;
   private final Object target;
   private boolean areMocksRegistered;
   private Set<Class<?>> fieldsToInject;

   public MockDependencyHandler(TestClass testClass, Object target) {
      this.testClass = testClass;
      this.target = target;
   }

   public Resolution<?> resolve(ResolutionContext context) {
      Injector injector = context.getInjector();
      if (!this.areMocksRegistered) {
         this.registerAllMocks(injector);
         this.areMocksRegistered = true;
      }

      Class<?> type = context.getIdentifier().getTypeAsClass();
      Object object = injector.getIfAvailable(type);
      if (object != null) {
         return new SimpleResolution(object);
      } else if (this.fieldsToInject.contains(type)) {
         return null;
      } else {
         throw new InjectorException("No mock found for '" + type + "'. All dependencies of @InjectDelayed must be provided as @Mock or @InjectDelayed fields");
      }
   }

   private void registerAllMocks(Injector injector) {
      Iterator var2 = this.testClass.getAnnotatedFields(Mock.class).iterator();

      FrameworkField frameworkField;
      while(var2.hasNext()) {
         frameworkField = (FrameworkField)var2.next();
         Class clazz = frameworkField.getType();
         injector.register(clazz, ReflectionUtils.getFieldValue(frameworkField.getField(), this.target));
      }

      this.fieldsToInject = new HashSet();
      var2 = this.testClass.getAnnotatedFields(InjectDelayed.class).iterator();

      while(var2.hasNext()) {
         frameworkField = (FrameworkField)var2.next();
         this.fieldsToInject.add(frameworkField.getType());
      }

   }
}
