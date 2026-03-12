package fr.xephi.authme.libs.ch.jalu.configme.beanmapper.propertydescription;

import fr.xephi.authme.libs.ch.jalu.configme.beanmapper.ConfigMeMapperException;
import fr.xephi.authme.libs.ch.jalu.configme.utils.TypeInformation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javax.annotation.Nullable;

public class BeanPropertyDescriptionImpl implements BeanPropertyDescription {
   private final String name;
   private final TypeInformation typeInformation;
   private final Method getter;
   private final Method setter;

   public BeanPropertyDescriptionImpl(String name, TypeInformation typeInformation, Method getter, Method setter) {
      this.name = name;
      this.typeInformation = typeInformation;
      this.getter = getter;
      this.setter = setter;
   }

   public String getName() {
      return this.name;
   }

   public TypeInformation getTypeInformation() {
      return this.typeInformation;
   }

   @Nullable
   public Object getValue(Object bean) {
      try {
         return this.getter.invoke(bean);
      } catch (InvocationTargetException | IllegalAccessException var3) {
         throw new ConfigMeMapperException("Could not get property '" + this.name + "' from instance '" + bean + "'", var3);
      }
   }

   public void setValue(Object bean, Object value) {
      try {
         this.setter.invoke(bean, value);
      } catch (InvocationTargetException | IllegalAccessException var4) {
         throw new ConfigMeMapperException("Could not set property '" + this.name + "' to value '" + value + "' on instance '" + bean + "'", var4);
      }
   }

   public String toString() {
      return "Bean property '" + this.name + "' with getter '" + this.getter + "'";
   }
}
