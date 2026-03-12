package fr.xephi.authme.libs.ch.jalu.configme.properties.types;

import fr.xephi.authme.libs.ch.jalu.configme.beanmapper.DefaultMapper;
import fr.xephi.authme.libs.ch.jalu.configme.beanmapper.Mapper;
import fr.xephi.authme.libs.ch.jalu.configme.properties.convertresult.ConvertErrorRecorder;
import fr.xephi.authme.libs.ch.jalu.configme.utils.TypeInformation;

public class BeanPropertyType<B> implements PropertyType<B> {
   private final TypeInformation beanType;
   private final Mapper mapper;

   public BeanPropertyType(TypeInformation beanType, Mapper mapper) {
      this.beanType = beanType;
      this.mapper = mapper;
   }

   public static <B> BeanPropertyType<B> of(Class<B> type, Mapper mapper) {
      return new BeanPropertyType(new TypeInformation(type), mapper);
   }

   public static <B> BeanPropertyType<B> of(Class<B> type) {
      return of(type, DefaultMapper.getInstance());
   }

   public B convert(Object object, ConvertErrorRecorder errorRecorder) {
      return this.mapper.convertToBean(object, this.beanType, errorRecorder);
   }

   public Object toExportValue(B value) {
      return this.mapper.toExportValue(value);
   }
}
