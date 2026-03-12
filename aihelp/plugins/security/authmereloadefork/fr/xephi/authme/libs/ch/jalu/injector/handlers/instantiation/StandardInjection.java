package fr.xephi.authme.libs.ch.jalu.injector.handlers.instantiation;

import fr.xephi.authme.libs.ch.jalu.injector.context.ObjectIdentifier;
import fr.xephi.authme.libs.ch.jalu.injector.context.StandardResolutionType;
import fr.xephi.authme.libs.ch.jalu.injector.utils.InjectorUtils;
import fr.xephi.authme.libs.ch.jalu.injector.utils.ReflectionUtils;
import java.lang.annotation.Annotation;
import java.lang.ref.SoftReference;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class StandardInjection<T> implements Resolution<T> {
   private final Constructor<T> constructor;
   private final List<Field> fields;
   private SoftReference<List<ObjectIdentifier>> dependencies;

   public StandardInjection(Constructor<T> constructor, List<Field> fields) {
      this.constructor = constructor;
      this.fields = fields;
   }

   public List<ObjectIdentifier> getDependencies() {
      List<ObjectIdentifier> depList = this.dependencies == null ? null : (List)this.dependencies.get();
      if (depList == null) {
         List<ObjectIdentifier> constructorDeps = this.buildConstructorDependencies();
         List<ObjectIdentifier> fieldDeps = this.buildFieldDependencies();
         depList = new ArrayList(constructorDeps.size() + fieldDeps.size());
         ((List)depList).addAll(constructorDeps);
         ((List)depList).addAll(fieldDeps);
         this.dependencies = new SoftReference(depList);
      }

      return (List)depList;
   }

   public T instantiateWith(Object... values) {
      InjectorUtils.checkArgument(values.length == this.constructor.getParameterTypes().length + this.fields.size(), "Number of values does not correspond to the expected number");
      int constructorParams = this.constructor.getParameterTypes().length;
      List<Object> constructorValues = Arrays.asList(values).subList(0, constructorParams);
      T instance = ReflectionUtils.newInstance(this.constructor, constructorValues.toArray());

      for(int i = 0; i < this.fields.size(); ++i) {
         ReflectionUtils.setField((Field)this.fields.get(i), instance, values[i + constructorParams]);
      }

      return instance;
   }

   public boolean isInstantiation() {
      return true;
   }

   private List<ObjectIdentifier> buildConstructorDependencies() {
      Type[] parameters = this.constructor.getGenericParameterTypes();
      Annotation[][] annotations = this.constructor.getParameterAnnotations();
      List<ObjectIdentifier> dependencies = new ArrayList(parameters.length);

      for(int i = 0; i < parameters.length; ++i) {
         dependencies.add(new ObjectIdentifier(StandardResolutionType.SINGLETON, parameters[i], annotations[i]));
      }

      return dependencies;
   }

   private List<ObjectIdentifier> buildFieldDependencies() {
      return (List)this.fields.stream().map((f) -> {
         return new ObjectIdentifier(StandardResolutionType.SINGLETON, f.getGenericType(), f.getAnnotations());
      }).collect(Collectors.toList());
   }
}
