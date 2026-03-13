package com.volmit.iris.util.slimjar.resolver.reader.dependency;

import com.volmit.iris.util.slimjar.resolver.data.DependencyData;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public final class EmptyDependencyDataProvider implements DependencyDataProvider {
   @NotNull
   public DependencyData get() {
      return new DependencyData(List.of(), List.of(), List.of(), List.of());
   }
}
