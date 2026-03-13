package ch.jalu.configme.beanmapper.propertydescription;

import ch.jalu.configme.beanmapper.ConfigMeMapperException;
import ch.jalu.configme.utils.TypeInformation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BeanPropertyDescriptionImpl implements BeanPropertyDescription {
   private final String name;
   private final TypeInformation typeInformation;
   private final Method getter;
   private final Method setter;
   private final BeanPropertyComments comments;

   /** @deprecated */
   @Deprecated
   public BeanPropertyDescriptionImpl(@NotNull String name, @NotNull TypeInformation typeInformation, @NotNull Method getter, @NotNull Method setter) {
      this(name, typeInformation, getter, setter, BeanPropertyComments.EMPTY);
   }

   public BeanPropertyDescriptionImpl(@NotNull String name, @NotNull TypeInformation typeInformation, @NotNull Method getter, @NotNull Method setter, @NotNull BeanPropertyComments comments) {
      this.name = name;
      this.typeInformation = typeInformation;
      this.getter = getter;
      this.setter = setter;
      this.comments = comments;
   }

   @NotNull
   public String getName() {
      return this.name;
   }

   @NotNull
   public TypeInformation getTypeInformation() {
      return this.typeInformation;
   }

   @Nullable
   public Object getValue(@NotNull Object bean) {
      try {
         return this.getter.invoke(bean);
      } catch (InvocationTargetException | IllegalAccessException var3) {
         throw new ConfigMeMapperException("Could not get property '" + this.name + "' from instance '" + bean + "'", var3);
      }
   }

   public void setValue(@NotNull Object bean, @NotNull Object value) {
      try {
         this.setter.invoke(bean, value);
      } catch (InvocationTargetException | IllegalAccessException var4) {
         throw new ConfigMeMapperException("Could not set property '" + this.name + "' to value '" + value + "' on instance '" + bean + "'", var4);
      }
   }

   @NotNull
   public BeanPropertyComments getComments() {
      return this.comments;
   }

   @NotNull
   public String toString() {
      return "Bean property '" + this.name + "' with getter '" + this.getter + "'";
   }
}
