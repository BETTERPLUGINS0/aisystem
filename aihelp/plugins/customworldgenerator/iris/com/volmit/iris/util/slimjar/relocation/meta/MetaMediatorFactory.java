package com.volmit.iris.util.slimjar.relocation.meta;

import java.nio.file.Path;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface MetaMediatorFactory {
   @Contract("_ -> new")
   @NotNull
   MetaMediator create(@NotNull Path var1);
}
