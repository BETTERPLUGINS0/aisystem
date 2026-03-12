package fr.xephi.authme.libs.ch.jalu.injector.testing.runner;

import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.TestClass;
import org.mockito.InjectMocks;
import org.mockito.Mockito;

public class DelayedInjectionRunnerValidator extends RunListener {
   private final RunNotifier notifier;
   private final TestClass testClass;

   public DelayedInjectionRunnerValidator(RunNotifier notifier, TestClass testClass) {
      this.notifier = notifier;
      this.testClass = testClass;
   }

   public void testFinished(Description description) throws Exception {
      try {
         Mockito.validateMockitoUsage();
         if (!this.testClass.getAnnotatedFields(InjectMocks.class).isEmpty()) {
            throw new IllegalStateException("Do not use @InjectMocks with the DelayedInjectionRunner: use @InjectDelayed or change runner");
         }
      } catch (Exception var3) {
         this.notifier.fireTestFailure(new Failure(description, var3));
      }

   }
}
