package com.volmit.iris.util.slimjar.injector;

import com.volmit.iris.util.slimjar.injector.loader.Injectable;
import com.volmit.iris.util.slimjar.resolver.ResolutionResult;
import com.volmit.iris.util.slimjar.resolver.data.DependencyData;
import java.util.Map;
import org.jetbrains.annotations.NotNull;

public interface DependencyInjector {
   void inject(@NotNull Injectable var1, @NotNull DependencyData var2, @NotNull Map<String, ResolutionResult> var3);
}
