package ch.jalu.configme.beanmapper.propertydescription;

import ch.jalu.configme.Comment;
import ch.jalu.configme.beanmapper.ConfigMeMapperException;
import ch.jalu.configme.beanmapper.ExportName;
import ch.jalu.configme.utils.TypeInformation;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BeanDescriptionFactoryImpl implements BeanDescriptionFactory {
   private final Map<Class<?>, List<BeanPropertyDescription>> classProperties = new HashMap();

   @NotNull
   public Collection<BeanPropertyDescription> getAllProperties(@NotNull Class<?> clazz) {
      return (Collection)this.classProperties.computeIfAbsent(clazz, this::collectAllProperties);
   }

   @NotNull
   protected List<BeanPropertyDescription> collectAllProperties(@NotNull Class<?> clazz) {
      List<PropertyDescriptor> descriptors = this.getWritableProperties(clazz);
      List<BeanPropertyDescription> properties = (List)descriptors.stream().map(this::convert).filter(Objects::nonNull).collect(Collectors.toList());
      this.validateProperties(clazz, properties);
      return properties;
   }

   @Nullable
   protected BeanPropertyDescription convert(@NotNull PropertyDescriptor descriptor) {
      if (Boolean.TRUE.equals(descriptor.getValue("transient"))) {
         return null;
      } else {
         Field field = this.tryGetField(descriptor.getWriteMethod().getDeclaringClass(), descriptor.getName());
         BeanPropertyComments comments = this.getComments(field);
         return new BeanPropertyDescriptionImpl(this.getPropertyName(descriptor, field), this.createTypeInfo(descriptor), descriptor.getReadMethod(), descriptor.getWriteMethod(), comments);
      }
   }

   @NotNull
   protected BeanPropertyComments getComments(@Nullable Field field) {
      Comment comment = field == null ? null : (Comment)field.getAnnotation(Comment.class);
      if (comment != null) {
         UUID uniqueId = comment.repeat() ? null : UUID.randomUUID();
         return new BeanPropertyComments(Arrays.asList(comment.value()), uniqueId);
      } else {
         return BeanPropertyComments.EMPTY;
      }
   }

   @Nullable
   protected Field tryGetField(@NotNull Class<?> clazz, @NotNull String name) {
      try {
         return clazz.getDeclaredField(name);
      } catch (NoSuchFieldException var4) {
         return null;
      }
   }

   protected void validateProperties(@NotNull Class<?> clazz, @NotNull Collection<BeanPropertyDescription> properties) {
      Set<String> names = new HashSet(properties.size());
      properties.forEach((property) -> {
         if (property.getName().isEmpty()) {
            throw new ConfigMeMapperException("Custom name of " + property + " may not be empty");
         } else if (!names.add(property.getName())) {
            throw new ConfigMeMapperException(clazz + " has multiple properties with name '" + property.getName() + "'");
         }
      });
   }

   @NotNull
   protected String getPropertyName(@NotNull PropertyDescriptor descriptor, @Nullable Field field) {
      if (field != null && field.isAnnotationPresent(ExportName.class)) {
         return ((ExportName)field.getAnnotation(ExportName.class)).value();
      } else if (descriptor.getReadMethod().isAnnotationPresent(ExportName.class)) {
         return ((ExportName)descriptor.getReadMethod().getAnnotation(ExportName.class)).value();
      } else {
         return descriptor.getWriteMethod().isAnnotationPresent(ExportName.class) ? ((ExportName)descriptor.getWriteMethod().getAnnotation(ExportName.class)).value() : descriptor.getName();
      }
   }

   @NotNull
   protected TypeInformation createTypeInfo(@NotNull PropertyDescriptor descriptor) {
      return new TypeInformation(descriptor.getWriteMethod().getGenericParameterTypes()[0]);
   }

   @NotNull
   protected List<PropertyDescriptor> getWritableProperties(@NotNull Class<?> clazz) {
      PropertyDescriptor[] descriptors;
      try {
         descriptors = Introspector.getBeanInfo(clazz).getPropertyDescriptors();
      } catch (IntrospectionException var8) {
         throw new IllegalStateException(var8);
      }

      List<PropertyDescriptor> writableProperties = new ArrayList(descriptors.length);
      PropertyDescriptor[] var4 = descriptors;
      int var5 = descriptors.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         PropertyDescriptor descriptor = var4[var6];
         if (descriptor.getWriteMethod() != null && descriptor.getReadMethod() != null) {
            writableProperties.add(descriptor);
         }
      }

      return this.sortPropertiesList(clazz, writableProperties);
   }

   @NotNull
   protected List<PropertyDescriptor> sortPropertiesList(@NotNull Class<?> clazz, @NotNull List<PropertyDescriptor> properties) {
      Map<String, Integer> fieldNameByIndex = this.createFieldNameOrderMap(clazz);
      int maxIndex = fieldNameByIndex.size();
      properties.sort(Comparator.comparing((property) -> {
         Integer index = (Integer)fieldNameByIndex.get(property.getName());
         return index == null ? maxIndex : index;
      }));
      return properties;
   }

   @NotNull
   protected Map<String, Integer> createFieldNameOrderMap(@NotNull Class<?> clazz) {
      Map<String, Integer> nameByIndex = new HashMap();
      int i = 0;
      Iterator var4 = this.collectClassAndAllParents(clazz).iterator();

      while(var4.hasNext()) {
         Class currentClass = (Class)var4.next();
         Field[] var6 = currentClass.getDeclaredFields();
         int var7 = var6.length;

         for(int var8 = 0; var8 < var7; ++var8) {
            Field field = var6[var8];
            nameByIndex.put(field.getName(), i);
            ++i;
         }
      }

      return nameByIndex;
   }

   @NotNull
   protected List<Class<?>> collectClassAndAllParents(@NotNull Class<?> clazz) {
      List<Class<?>> parents = new ArrayList();

      for(Class curClass = clazz; curClass != null && curClass != Object.class; curClass = curClass.getSuperclass()) {
         parents.add(curClass);
      }

      Collections.reverse(parents);
      return parents;
   }
}
