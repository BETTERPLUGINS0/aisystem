package fr.xephi.authme.libs.ch.jalu.injector.testing.runner;

import fr.xephi.authme.libs.ch.jalu.injector.Injector;
import fr.xephi.authme.libs.ch.jalu.injector.InjectorBuilder;
import fr.xephi.authme.libs.ch.jalu.injector.handlers.Handler;
import fr.xephi.authme.libs.ch.jalu.injector.handlers.postconstruct.PostConstructMethodInvoker;
import fr.xephi.authme.libs.ch.jalu.injector.utils.ReflectionUtils;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.junit.runners.model.FrameworkField;
import org.junit.runners.model.Statement;
import org.junit.runners.model.TestClass;

public class RunDelayedInjects extends Statement {
   private final Statement next;
   private TestClass testClass;
   private Object target;
   private List<FrameworkField> fields;

   public RunDelayedInjects(Statement next, TestClass testClass, Object target, List<FrameworkField> fields) {
      this.next = next;
      this.testClass = testClass;
      this.target = target;
      this.fields = fields;
   }

   public void evaluate() throws Throwable {
      Injector injector = this.getInjector();
      Iterator var2 = this.fields.iterator();

      while(var2.hasNext()) {
         FrameworkField frameworkField = (FrameworkField)var2.next();
         Field field = frameworkField.getField();
         if (ReflectionUtils.getFieldValue(field, this.target) != null) {
            throw new IllegalStateException("Field with @InjectDelayed must be null on startup. Field '" + field.getName() + "' is not null");
         }

         Object object = injector.getSingleton(field.getType());
         ReflectionUtils.setField(field, this.target, object);
      }

      this.testClass = null;
      this.target = null;
      this.fields = null;
      this.next.evaluate();
   }

   protected Injector getInjector() {
      List<Handler> instantiationProviders = InjectorBuilder.createInstantiationProviders("");
      return (new InjectorBuilder()).addHandlers(new AnnotationResolver(this.testClass, this.target), new MockDependencyHandler(this.testClass, this.target), new PostConstructMethodInvoker()).addHandlers((Collection)instantiationProviders).create();
   }
}
