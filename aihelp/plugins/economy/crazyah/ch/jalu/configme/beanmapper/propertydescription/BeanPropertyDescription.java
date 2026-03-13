package ch.jalu.configme.beanmapper.propertydescription;

import ch.jalu.configme.utils.TypeInformation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface BeanPropertyDescription {
   @NotNull
   String getName();

   @NotNull
   TypeInformation getTypeInformation();

   void setValue(@NotNull Object var1, @NotNull Object var2);

   @Nullable
   Object getValue(@NotNull Object var1);

   @NotNull
   BeanPropertyComments getComments();
}
