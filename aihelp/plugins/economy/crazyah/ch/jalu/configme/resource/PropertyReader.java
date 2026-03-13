package ch.jalu.configme.resource;

import java.util.List;
import java.util.Set;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface PropertyReader {
   boolean contains(@NotNull String var1);

   @NotNull
   Set<String> getKeys(boolean var1);

   @NotNull
   Set<String> getChildKeys(@NotNull String var1);

   @Nullable
   Object getObject(@NotNull String var1);

   @Nullable
   String getString(@NotNull String var1);

   @Nullable
   Integer getInt(@NotNull String var1);

   @Nullable
   Double getDouble(@NotNull String var1);

   @Nullable
   Boolean getBoolean(@NotNull String var1);

   @Nullable
   List<?> getList(@NotNull String var1);
}
