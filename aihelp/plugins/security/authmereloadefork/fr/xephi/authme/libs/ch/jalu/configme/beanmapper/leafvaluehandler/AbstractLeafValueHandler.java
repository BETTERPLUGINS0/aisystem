package fr.xephi.authme.libs.ch.jalu.configme.beanmapper.leafvaluehandler;

import fr.xephi.authme.libs.ch.jalu.configme.utils.TypeInformation;

public abstract class AbstractLeafValueHandler implements LeafValueHandler {
   public Object convert(TypeInformation typeInformation, Object value) {
      return this.convert(typeInformation.getSafeToWriteClass(), value);
   }

   protected abstract Object convert(Class<?> var1, Object var2);
}
