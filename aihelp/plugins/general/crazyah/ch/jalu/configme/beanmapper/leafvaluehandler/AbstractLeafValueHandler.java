package ch.jalu.configme.beanmapper.leafvaluehandler;

import ch.jalu.configme.utils.TypeInformation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractLeafValueHandler implements LeafValueHandler {
   @Nullable
   public Object convert(@NotNull TypeInformation typeInformation, @Nullable Object value) {
      return this.convert(typeInformation.getSafeToWriteClass(), value);
   }

   @Nullable
   protected abstract Object convert(@Nullable Class<?> var1, @Nullable Object var2);
}
