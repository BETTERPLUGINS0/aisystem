package com.volmit.iris.util.slimjar.relocation.meta;

import com.volmit.iris.util.slimjar.exceptions.RelocatorException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface MetaMediator {
   @Nullable
   String readAttribute(@NotNull String var1) throws RelocatorException;

   void writeAttribute(@NotNull String var1, @NotNull String var2) throws RelocatorException;
}
