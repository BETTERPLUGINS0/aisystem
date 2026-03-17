package me.PM2.infinitevehicles.xseries.reflection.jvm;

import java.util.Set;
import org.jetbrains.annotations.NotNull;

public interface NamedReflectiveHandle {
   @NotNull
   Set<String> getPossibleNames();
}
