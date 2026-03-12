package fr.xephi.authme.libs.ch.jalu.configme.beanmapper;

import fr.xephi.authme.libs.ch.jalu.configme.utils.TypeInformation;

public interface MappingContext {
   MappingContext createChild(String var1, TypeInformation var2);

   TypeInformation getTypeInformation();

   default TypeInformation getGenericTypeInfoOrFail(int index) {
      TypeInformation genericType = this.getTypeInformation().getGenericType(index);
      if (genericType != null && genericType.getSafeToWriteClass() != null) {
         return this.getTypeInformation().getGenericType(index);
      } else {
         throw new ConfigMeMapperException(this, "The generic type " + index + " is not well defined");
      }
   }

   String createDescription();

   void registerError(String var1);
}
