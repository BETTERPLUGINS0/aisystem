package com.volmit.iris.util.slimjar.injector;

import com.volmit.iris.util.slimjar.injector.helper.DownloadHelper;
import com.volmit.iris.util.slimjar.injector.helper.DownloadHelperFactory;
import com.volmit.iris.util.slimjar.injector.loader.Injectable;
import com.volmit.iris.util.slimjar.logging.LogDispatcher;
import com.volmit.iris.util.slimjar.logging.ProcessLogger;
import com.volmit.iris.util.slimjar.resolver.ResolutionResult;
import com.volmit.iris.util.slimjar.resolver.data.Dependency;
import com.volmit.iris.util.slimjar.resolver.data.DependencyData;
import java.net.MalformedURLException;
import java.util.Collection;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Stream;
import org.jetbrains.annotations.NotNull;

public final class SimpleDependencyInjector implements DependencyInjector {
   private static final ProcessLogger LOGGER = LogDispatcher.getMediatingLogger();
   private final DownloadHelperFactory downloadHelperFactory;

   public SimpleDependencyInjector(DownloadHelperFactory var1) {
      this.downloadHelperFactory = var1;
   }

   public void inject(@NotNull Injectable var1, @NotNull DependencyData var2, @NotNull Map<String, ResolutionResult> var3) {
      DownloadHelper var4 = this.downloadHelperFactory.create(var2, var3);
      this.injectDependencies(var1, var4, var2.dependencies());
   }

   private void injectDependencies(@NotNull Injectable var1, @NotNull DownloadHelper var2, @NotNull Collection<Dependency> var3) {
      Consumer var4 = (var1x) -> {
         try {
            var1.inject(var1x.toURI().toURL());
            LOGGER.info("Loaded library %s", var1x.toPath().normalize());
         } catch (MalformedURLException var3) {
         }

      };
      Stream var5 = var2.fetch(var3);
      if (var1.isThreadSafe()) {
         var5.forEach(var4);
      } else {
         var5.toList().forEach(var4);
      }

   }
}
