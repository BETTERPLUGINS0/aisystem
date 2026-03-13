package com.volmit.iris.util.slimjar.relocation.facade;

import com.volmit.iris.util.slimjar.relocation.RelocationRule;
import java.io.File;
import java.util.Collection;
import org.jetbrains.annotations.NotNull;

public interface JarRelocatorFacadeFactory {
   @NotNull
   JarRelocatorFacade createFacade(@NotNull File var1, @NotNull File var2, @NotNull Collection<RelocationRule> var3);
}
