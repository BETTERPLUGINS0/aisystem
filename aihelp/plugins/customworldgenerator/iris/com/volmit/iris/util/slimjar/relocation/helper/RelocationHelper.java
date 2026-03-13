package com.volmit.iris.util.slimjar.relocation.helper;

import com.volmit.iris.util.slimjar.resolver.data.Dependency;
import java.io.File;
import org.jetbrains.annotations.NotNull;

public interface RelocationHelper {
   @NotNull
   File relocate(@NotNull Dependency var1, @NotNull File var2);
}
