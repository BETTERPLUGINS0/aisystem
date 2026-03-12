package fr.xephi.authme.libs.ch.jalu.configme.beanmapper.propertydescription;

import fr.xephi.authme.libs.ch.jalu.configme.beanmapper.ConfigMeMapperException;
import fr.xephi.authme.libs.ch.jalu.configme.beanmapper.ExportName;
import fr.xephi.authme.libs.ch.jalu.configme.utils.TypeInformation;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.ArrayList;
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
import java.util.stream.Collectors;
import javax.annotation.Nullable;

public class BeanDescriptionFactoryImpl implements BeanDescriptionFactory {
   private final Map<Class<?>, List<BeanPropertyDescription>> classProperties = new HashMap();

   public Collection<BeanPropertyDescription> getAllProperties(Class<?> clazz) {
      return (Collection)this.classProperties.computeIfAbsent(clazz, this::collectAllProperties);
   }

   protected List<BeanPropertyDescription> collectAllProperties(Class<?> clazz) {
      List<PropertyDescriptor> descriptors = this.getWritableProperties(clazz);
      List<BeanPropertyDescription> properties = (List)descriptors.stream().map(this::convert).filter(Objects::nonNull).collect(Collectors.toList());
      this.validateProperties(clazz, properties);
      return properties;
   }

   @Nullable
   protected BeanPropertyDescription convert(PropertyDescriptor descriptor) {
      return Boolean.TRUE.equals(descriptor.getValue("transient")) ? null : new BeanPropertyDescriptionImpl(this.getPropertyName(descriptor), this.createTypeInfo(descriptor), descriptor.getReadMethod(), descriptor.getWriteMethod());
   }

   protected void validateProperties(Class<?> clazz, Collection<BeanPropertyDescription> properties) {
      Set<String> names = new HashSet(properties.size());
      properties.forEach((property) -> {
         if (property.getName().isEmpty()) {
            throw new ConfigMeMapperException("Custom name of " + property + " may not be empty");
         } else if (!names.add(property.getName())) {
            throw new ConfigMeMapperException(clazz + " has multiple properties with name '" + property.getName() + "'");
         }
      });
   }

   protected String getPropertyName(PropertyDescriptor descriptor) {
      if (descriptor.getReadMethod().isAnnotationPresent(ExportName.class)) {
         return ((ExportName)descriptor.getReadMethod().getAnnotation(ExportName.class)).value();
      } else {
         return descriptor.getWriteMethod().isAnnotationPresent(ExportName.class) ? ((ExportName)descriptor.getWriteMethod().getAnnotation(ExportName.class)).value() : descriptor.getName();
      }
   }

   protected TypeInformation createTypeInfo(PropertyDescriptor descriptor) {
      return new TypeInformation(descriptor.getWriteMethod().getGenericParameterTypes()[0]);
   }

   protected List<PropertyDescriptor> getWritableProperties(Class<?> clazz) {
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

   protected List<PropertyDescriptor> sortPropertiesList(Class<?> clazz, List<PropertyDescriptor> properties) {
      Map<String, Integer> fieldNameByIndex = this.createFieldNameOrderMap(clazz);
      int maxIndex = fieldNameByIndex.size();
      properties.sort(Comparator.comparing((property) -> {
         Integer index = (Integer)fieldNameByIndex.get(property.getName());
         return index == null ? maxIndex : index;
      }));
      return properties;
   }

   protected Map<String, Integer> createFieldNameOrderMap(Class<?> clazz) {
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

   protected List<Class<?>> collectClassAndAllParents(Class<?> clazz) {
      List<Class<?>> parents = new ArrayList();

      for(Class curClass = clazz; curClass != null && curClass != Object.class; curClass = curClass.getSuperclass()) {
         parents.add(curClass);
      }

      Collections.reverse(parents);
      return parents;
   }
}
