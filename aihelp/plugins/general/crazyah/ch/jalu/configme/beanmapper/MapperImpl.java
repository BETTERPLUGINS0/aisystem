package ch.jalu.configme.beanmapper;

import ch.jalu.configme.beanmapper.leafvaluehandler.LeafValueHandler;
import ch.jalu.configme.beanmapper.leafvaluehandler.StandardLeafValueHandlers;
import ch.jalu.configme.beanmapper.propertydescription.BeanDescriptionFactory;
import ch.jalu.configme.beanmapper.propertydescription.BeanDescriptionFactoryImpl;
import ch.jalu.configme.beanmapper.propertydescription.BeanPropertyComments;
import ch.jalu.configme.beanmapper.propertydescription.BeanPropertyDescription;
import ch.jalu.configme.properties.convertresult.ConvertErrorRecorder;
import ch.jalu.configme.properties.convertresult.ValueWithComments;
import ch.jalu.configme.utils.TypeInformation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MapperImpl implements Mapper {
   public static final Object RETURN_NULL = new Object();
   private final BeanDescriptionFactory beanDescriptionFactory;
   private final LeafValueHandler leafValueHandler;

   public MapperImpl() {
      this(new BeanDescriptionFactoryImpl(), StandardLeafValueHandlers.getDefaultLeafValueHandler());
   }

   public MapperImpl(@NotNull BeanDescriptionFactory beanDescriptionFactory, @NotNull LeafValueHandler leafValueHandler) {
      this.beanDescriptionFactory = beanDescriptionFactory;
      this.leafValueHandler = leafValueHandler;
   }

   @NotNull
   protected final BeanDescriptionFactory getBeanDescriptionFactory() {
      return this.beanDescriptionFactory;
   }

   @NotNull
   protected final LeafValueHandler getLeafValueHandler() {
      return this.leafValueHandler;
   }

   @NotNull
   protected MappingContext createRootMappingContext(@NotNull TypeInformation beanType, @NotNull ConvertErrorRecorder errorRecorder) {
      return MappingContextImpl.createRoot(beanType, errorRecorder);
   }

   @Nullable
   public Object toExportValue(@Nullable Object value) {
      return this.toExportValue(value, new HashSet());
   }

   @Nullable
   protected Object toExportValue(@Nullable Object value, @NotNull Set<UUID> knownUniqueComments) {
      Object simpleValue = this.leafValueHandler.toExportValue(value);
      if (simpleValue == null && value != null) {
         simpleValue = this.createExportValueForSpecialTypes(value, knownUniqueComments);
         if (simpleValue != null) {
            return unwrapReturnNull(simpleValue);
         } else {
            Map<String, Object> mappedBean = new LinkedHashMap();
            Iterator var5 = this.beanDescriptionFactory.getAllProperties(value.getClass()).iterator();

            while(true) {
               BeanPropertyDescription property;
               Object exportValueOfProperty;
               do {
                  if (!var5.hasNext()) {
                     return mappedBean;
                  }

                  property = (BeanPropertyDescription)var5.next();
                  exportValueOfProperty = this.toExportValue(property.getValue(value), knownUniqueComments);
               } while(exportValueOfProperty == null);

               BeanPropertyComments propComments = property.getComments();
               if (!propComments.getComments().isEmpty() && (propComments.getUuid() == null || !knownUniqueComments.contains(propComments.getUuid()))) {
                  knownUniqueComments.add(propComments.getUuid());
                  exportValueOfProperty = new ValueWithComments(exportValueOfProperty, propComments.getComments(), propComments.getUuid());
               }

               mappedBean.put(property.getName(), exportValueOfProperty);
            }
         }
      } else {
         return unwrapReturnNull(simpleValue);
      }
   }

   @Nullable
   protected Object createExportValueForSpecialTypes(@Nullable Object value, @NotNull Set<UUID> knownUniqueComments) {
      if (value instanceof Collection) {
         return ((Collection)value).stream().map((entryx) -> {
            return this.toExportValue(entryx, knownUniqueComments);
         }).collect(Collectors.toList());
      } else if (!(value instanceof Map)) {
         if (value instanceof Optional) {
            Optional<?> optional = (Optional)value;
            return optional.map((v) -> {
               return this.toExportValue(v, knownUniqueComments);
            }).orElse(RETURN_NULL);
         } else {
            return null;
         }
      } else {
         Map<Object, Object> result = new LinkedHashMap();
         Iterator var4 = ((Map)value).entrySet().iterator();

         while(var4.hasNext()) {
            Entry<?, ?> entry = (Entry)var4.next();
            result.put(entry.getKey(), this.toExportValue(entry.getValue(), knownUniqueComments));
         }

         return result;
      }
   }

   @Nullable
   protected static Object unwrapReturnNull(@Nullable Object o) {
      return o == RETURN_NULL ? null : o;
   }

   @Nullable
   public Object convertToBean(@Nullable Object value, @NotNull TypeInformation beanType, @NotNull ConvertErrorRecorder errorRecorder) {
      return value == null ? null : this.convertValueForType(this.createRootMappingContext(beanType, errorRecorder), value);
   }

   @Nullable
   protected Object convertValueForType(@NotNull MappingContext context, @Nullable Object value) {
      Class<?> rawClass = context.getTypeInformation().getSafeToWriteClass();
      if (rawClass == null) {
         throw new ConfigMeMapperException(context, "Cannot determine required type");
      } else {
         Object result = this.leafValueHandler.convert(context.getTypeInformation(), value);
         if (result != null) {
            return result;
         } else {
            result = this.handleSpecialTypes(context, value);
            return result != null ? result : this.createBean(context, value);
         }
      }
   }

   @Nullable
   protected Object handleSpecialTypes(@NotNull MappingContext context, @Nullable Object value) {
      Class<?> rawClass = context.getTypeInformation().getSafeToWriteClass();
      if (Collection.class.isAssignableFrom(rawClass)) {
         return this.createCollection(context, value);
      } else if (Map.class.isAssignableFrom(rawClass)) {
         return this.createMap(context, value);
      } else {
         return Optional.class.isAssignableFrom(rawClass) ? this.createOptional(context, value) : null;
      }
   }

   @Nullable
   protected Collection createCollection(@NotNull MappingContext context, @Nullable Object value) {
      if (value instanceof Iterable) {
         TypeInformation entryType = context.getGenericTypeInfoOrFail(0);
         Collection result = this.createCollectionMatchingType(context);
         int index = 0;
         Iterator var6 = ((Iterable)value).iterator();

         while(var6.hasNext()) {
            Object entry = var6.next();
            Object convertedEntry = this.convertValueForType(context.createChild("[" + index + "]", entryType), entry);
            if (convertedEntry == null) {
               context.registerError("Cannot convert value at index " + index);
            } else {
               result.add(convertedEntry);
            }
         }

         return result;
      } else {
         return null;
      }
   }

   @NotNull
   protected Collection createCollectionMatchingType(@NotNull MappingContext mappingContext) {
      Class<?> collectionType = mappingContext.getTypeInformation().getSafeToWriteClass();
      if (collectionType.isAssignableFrom(ArrayList.class)) {
         return new ArrayList();
      } else if (collectionType.isAssignableFrom(LinkedHashSet.class)) {
         return new LinkedHashSet();
      } else {
         throw new ConfigMeMapperException(mappingContext, "Unsupported collection type '" + collectionType + "'");
      }
   }

   @Nullable
   protected Map createMap(@NotNull MappingContext context, @Nullable Object value) {
      if (value instanceof Map) {
         if (context.getGenericTypeInfoOrFail(0).getSafeToWriteClass() != String.class) {
            throw new ConfigMeMapperException(context, "The key type of maps may only be of String type");
         } else {
            TypeInformation mapValueType = context.getGenericTypeInfoOrFail(1);
            Map<String, ?> entries = (Map)value;
            Map result = this.createMapMatchingType(context);
            Iterator var6 = entries.entrySet().iterator();

            while(var6.hasNext()) {
               Entry<String, ?> entry = (Entry)var6.next();
               Object mappedValue = this.convertValueForType(context.createChild("[k=" + (String)entry.getKey() + "]", mapValueType), entry.getValue());
               if (mappedValue == null) {
                  context.registerError("Cannot map value for key " + (String)entry.getKey());
               } else {
                  result.put(entry.getKey(), mappedValue);
               }
            }

            return result;
         }
      } else {
         return null;
      }
   }

   @NotNull
   protected Map createMapMatchingType(@NotNull MappingContext mappingContext) {
      Class<?> mapType = mappingContext.getTypeInformation().getSafeToWriteClass();
      if (mapType.isAssignableFrom(LinkedHashMap.class)) {
         return new LinkedHashMap();
      } else if (mapType.isAssignableFrom(TreeMap.class)) {
         return new TreeMap();
      } else {
         throw new ConfigMeMapperException(mappingContext, "Unsupported map type '" + mapType + "'");
      }
   }

   @NotNull
   protected Object createOptional(@NotNull MappingContext context, @Nullable Object value) {
      MappingContext childContext = context.createChild("[v]", context.getGenericTypeInfoOrFail(0));
      Object result = this.convertValueForType(childContext, value);
      return Optional.ofNullable(result);
   }

   @Nullable
   protected Object createBean(@NotNull MappingContext context, @Nullable Object value) {
      if (!(value instanceof Map)) {
         return null;
      } else {
         Collection<BeanPropertyDescription> properties = this.beanDescriptionFactory.getAllProperties(context.getTypeInformation().getSafeToWriteClass());
         if (properties.isEmpty()) {
            return null;
         } else {
            Map<?, ?> entries = (Map)value;
            Object bean = this.createBeanMatchingType(context);
            Iterator var6 = properties.iterator();

            while(var6.hasNext()) {
               BeanPropertyDescription property = (BeanPropertyDescription)var6.next();
               Object result = this.convertValueForType(context.createChild(property.getName(), property.getTypeInformation()), entries.get(property.getName()));
               if (result == null) {
                  if (property.getValue(bean) == null) {
                     return null;
                  }

                  context.registerError("No value found, fallback to field default value");
               } else {
                  property.setValue(bean, result);
               }
            }

            return bean;
         }
      }
   }

   @NotNull
   protected Object createBeanMatchingType(@NotNull MappingContext mappingContext) {
      Class clazz = mappingContext.getTypeInformation().getSafeToWriteClass();

      try {
         return clazz.getDeclaredConstructor().newInstance();
      } catch (ReflectiveOperationException var4) {
         throw new ConfigMeMapperException(mappingContext, "Could not create object of type '" + clazz.getName() + "'. It is required to have a default constructor", var4);
      }
   }
}
