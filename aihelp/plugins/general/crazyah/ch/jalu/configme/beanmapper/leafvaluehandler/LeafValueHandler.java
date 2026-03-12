package ch.jalu.configme.beanmapper.leafvaluehandler;

import ch.jalu.configme.utils.TypeInformation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface LeafValueHandler {
   @Nullable
   Object convert(@NotNull TypeInformation var1, @Nullable Object var2);

   @Nullable
   Object toExportValue(@Nullable Object var1);
}
