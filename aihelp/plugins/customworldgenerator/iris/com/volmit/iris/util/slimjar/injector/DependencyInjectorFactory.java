package com.volmit.iris.util.slimjar.injector;

import com.volmit.iris.util.slimjar.injector.helper.DownloadHelperFactory;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface DependencyInjectorFactory {
   @Contract("_ -> new")
   @NotNull
   DependencyInjector create(@NotNull DownloadHelperFactory var1);
}
