package com.volmit.iris.util.slimjar.relocation.meta;

import java.nio.file.Path;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public final class AttributeMetaMediatorFactory implements MetaMediatorFactory {
   @Contract("_ -> new")
   @NotNull
   public MetaMediator create(@NotNull Path var1) {
      return new AttributeMetaMediator(var1);
   }
}
