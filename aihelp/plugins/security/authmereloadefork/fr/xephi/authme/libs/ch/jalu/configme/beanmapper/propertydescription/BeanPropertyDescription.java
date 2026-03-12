package fr.xephi.authme.libs.ch.jalu.configme.beanmapper.propertydescription;

import fr.xephi.authme.libs.ch.jalu.configme.utils.TypeInformation;
import javax.annotation.Nullable;

public interface BeanPropertyDescription {
   String getName();

   TypeInformation getTypeInformation();

   void setValue(Object var1, Object var2);

   @Nullable
   Object getValue(Object var1);
}
