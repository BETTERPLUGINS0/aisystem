package fr.xephi.authme.libs.ch.jalu.injector;

import fr.xephi.authme.libs.ch.jalu.injector.handlers.Handler;
import fr.xephi.authme.libs.ch.jalu.injector.utils.InjectorUtils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class InjectorConfig {
   private List<Handler> handlers = new ArrayList();

   protected InjectorConfig() {
   }

   public void addHandlers(Collection<? extends Handler> handlers) {
      InjectorUtils.checkNotNull(handlers, (String)null);
      this.handlers.addAll(handlers);
   }

   public List<Handler> getHandlers() {
      return this.handlers;
   }
}
