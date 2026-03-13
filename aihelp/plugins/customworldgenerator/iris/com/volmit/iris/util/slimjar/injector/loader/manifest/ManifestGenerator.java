package com.volmit.iris.util.slimjar.injector.loader.manifest;

import com.volmit.iris.util.slimjar.exceptions.InjectorException;
import org.jetbrains.annotations.NotNull;

public interface ManifestGenerator {
   @NotNull
   ManifestGenerator attribute(@NotNull String var1, @NotNull String var2);

   void generate() throws InjectorException;
}
