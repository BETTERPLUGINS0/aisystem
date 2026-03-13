package com.volmit.iris.util.slimjar.injector;

import com.volmit.iris.util.slimjar.injector.helper.DownloadHelperFactory;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public final class SimpleDependencyInjectorFactory implements DependencyInjectorFactory {
   @Contract("_ -> new")
   @NotNull
   public DependencyInjector create(@NotNull DownloadHelperFactory var1) {
      return new SimpleDependencyInjector(var1);
   }
}
