package fr.xephi.authme.libs.ch.jalu.injector.testing.runner;

import fr.xephi.authme.libs.ch.jalu.injector.utils.ReflectionUtils;
import java.util.Iterator;
import java.util.List;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

public class RunBeforeInjectings extends Statement {
   private final Statement next;
   private final List<FrameworkMethod> beforeInjectings;
   private final Object target;

   public RunBeforeInjectings(Statement next, List<FrameworkMethod> beforeInjectings, Object target) {
      this.next = next;
      this.beforeInjectings = beforeInjectings;
      this.target = target;
   }

   public void evaluate() throws Throwable {
      Iterator var1 = this.beforeInjectings.iterator();

      while(var1.hasNext()) {
         FrameworkMethod method = (FrameworkMethod)var1.next();
         ReflectionUtils.invokeMethod(method.getMethod(), this.target);
      }

      this.next.evaluate();
   }
}
