package fr.xephi.authme.libs.ch.jalu.injector.testing;

import fr.xephi.authme.libs.ch.jalu.injector.testing.runner.DelayedInjectionRunnerValidator;
import fr.xephi.authme.libs.ch.jalu.injector.testing.runner.RunBeforeInjectings;
import fr.xephi.authme.libs.ch.jalu.injector.testing.runner.RunDelayedInjects;
import java.util.List;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkField;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;
import org.mockito.MockitoAnnotations;

public class DelayedInjectionRunner extends BlockJUnit4ClassRunner {
   public DelayedInjectionRunner(Class<?> clazz) throws InitializationError {
      super(clazz);
   }

   public Statement withBefores(FrameworkMethod method, Object target, Statement statement) {
      MockitoAnnotations.initMocks(target);
      statement = super.withBefores(method, target, statement);
      statement = this.withDelayedInjects(target, statement);
      return this.withBeforeInjectings(target, statement);
   }

   public void run(RunNotifier notifier) {
      notifier.addListener(new DelayedInjectionRunnerValidator(notifier, this.getTestClass()));
      super.run(notifier);
   }

   private Statement withBeforeInjectings(Object target, Statement statement) {
      List<FrameworkMethod> beforeInjectings = this.getTestClass().getAnnotatedMethods(BeforeInjecting.class);
      return (Statement)(beforeInjectings.isEmpty() ? statement : new RunBeforeInjectings(statement, beforeInjectings, target));
   }

   private Statement withDelayedInjects(Object target, Statement statement) {
      List<FrameworkField> delayedFields = this.getTestClass().getAnnotatedFields(InjectDelayed.class);
      return delayedFields.isEmpty() ? statement : this.createDelayedInjectsStatement(target, statement, delayedFields);
   }

   protected Statement createDelayedInjectsStatement(Object target, Statement statement, List<FrameworkField> delayedFields) {
      return new RunDelayedInjects(statement, this.getTestClass(), target, delayedFields);
   }
}
