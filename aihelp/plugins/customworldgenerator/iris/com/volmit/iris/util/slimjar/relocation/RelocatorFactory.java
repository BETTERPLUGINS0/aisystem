package com.volmit.iris.util.slimjar.relocation;

import java.util.Collection;
import org.jetbrains.annotations.NotNull;

public interface RelocatorFactory {
   @NotNull
   Relocator create(@NotNull Collection<RelocationRule> var1);
}
