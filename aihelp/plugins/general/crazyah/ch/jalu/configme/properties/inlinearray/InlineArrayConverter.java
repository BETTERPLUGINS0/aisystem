package ch.jalu.configme.properties.inlinearray;

import org.jetbrains.annotations.NotNull;

public interface InlineArrayConverter<T> {
   @NotNull
   T[] fromString(String var1);

   @NotNull
   String toExportValue(@NotNull T[] var1);
}
