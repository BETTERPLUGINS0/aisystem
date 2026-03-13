package libs.com.ryderbelserion.vital.paper.modules;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import libs.com.ryderbelserion.vital.paper.modules.interfaces.IPaperModule;
import org.jetbrains.annotations.NotNull;

public class ModuleLoader {
   private final List<IPaperModule> modules = new ArrayList();
   private final EventRegistry registry;

   public ModuleLoader(@NotNull EventRegistry registry) {
      this.registry = registry;
   }

   public void load() {
      this.modules.forEach((module) -> {
         if (module.isEnabled()) {
            this.registry.addEvent(module);
            module.enable();
         } else {
            module.disable();
         }
      });
   }

   public void reload() {
      this.modules.forEach((module) -> {
         if (module.isEnabled()) {
            this.registry.addEvent(module);
            module.reload();
         } else {
            module.disable();
         }

      });
   }

   public void unload(boolean purge) {
      this.modules.forEach((module) -> {
         if (module.isEnabled()) {
            module.disable();
         }

      });
      if (purge) {
         this.modules.clear();
      }

   }

   public void unload() {
      this.unload(false);
   }

   public void addModule(@NotNull IPaperModule module) {
      if (!this.hasModule(module)) {
         this.modules.add(module);
      }
   }

   public void removeModule(@NotNull IPaperModule module) {
      if (this.hasModule(module)) {
         this.modules.remove(module);
      }
   }

   @NotNull
   public final List<IPaperModule> getModules() {
      return Collections.unmodifiableList(this.modules);
   }

   @NotNull
   public final EventRegistry getRegistry() {
      return this.registry;
   }

   private boolean hasModule(@NotNull IPaperModule module) {
      String name = module.getName();
      boolean hasModule = false;
      Iterator var4 = this.modules.iterator();

      while(var4.hasNext()) {
         IPaperModule key = (IPaperModule)var4.next();
         if (name.equals(key.getName())) {
            hasModule = true;
         }
      }

      return hasModule;
   }
}
