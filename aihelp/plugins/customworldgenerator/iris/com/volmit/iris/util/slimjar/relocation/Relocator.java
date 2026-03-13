package com.volmit.iris.util.slimjar.relocation;

import com.volmit.iris.util.slimjar.exceptions.RelocatorException;
import java.io.File;
import org.jetbrains.annotations.NotNull;

public interface Relocator {
   void relocate(@NotNull File var1, @NotNull File var2) throws RelocatorException;
}
