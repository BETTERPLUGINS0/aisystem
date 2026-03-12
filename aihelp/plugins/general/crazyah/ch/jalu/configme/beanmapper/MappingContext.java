package ch.jalu.configme.beanmapper;

import ch.jalu.configme.utils.TypeInformation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface MappingContext {
   @NotNull
   MappingContext createChild(@NotNull String var1, @NotNull TypeInformation var2);

   @NotNull
   TypeInformation getTypeInformation();

   @Nullable
   default TypeInformation getGenericTypeInfoOrFail(int index) {
      TypeInformation genericType = this.getTypeInformation().getGenericType(index);
      if (genericType != null && genericType.getSafeToWriteClass() != null) {
         return this.getTypeInformation().getGenericType(index);
      } else {
         throw new ConfigMeMapperException(this, "The generic type " + index + " is not well defined");
      }
   }

   @NotNull
   String createDescription();

   void registerError(@NotNull String var1);
}
