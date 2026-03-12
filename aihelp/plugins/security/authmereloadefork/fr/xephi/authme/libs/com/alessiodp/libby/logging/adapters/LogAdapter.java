package fr.xephi.authme.libs.com.alessiodp.libby.logging.adapters;

import fr.xephi.authme.libs.com.alessiodp.libby.logging.LogLevel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface LogAdapter {
   void log(@NotNull LogLevel var1, @Nullable String var2);

   void log(@NotNull LogLevel var1, @Nullable String var2, @Nullable Throwable var3);
}
