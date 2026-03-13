package com.volmit.iris.util.slimjar.relocation.helper;

import com.volmit.iris.util.slimjar.relocation.Relocator;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface RelocationHelperFactory {
   @NotNull
   RelocationHelper create(@NotNull Relocator var1);
}
