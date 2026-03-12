package fr.xephi.authme.settings;

import fr.xephi.authme.libs.ch.jalu.configme.properties.BaseProperty;
import fr.xephi.authme.libs.ch.jalu.configme.properties.convertresult.ConvertErrorRecorder;
import fr.xephi.authme.libs.ch.jalu.configme.resource.PropertyReader;
import fr.xephi.authme.libs.com.google.common.collect.Sets;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class EnumSetProperty<E extends Enum<E>> extends BaseProperty<Set<E>> {
   private final Class<E> enumClass;

   @SafeVarargs
   public EnumSetProperty(Class<E> enumClass, String path, E... values) {
      super(path, Sets.newHashSet((Object[])values));
      this.enumClass = enumClass;
   }

   protected Set<E> getFromReader(PropertyReader reader, ConvertErrorRecorder errorRecorder) {
      Object entry = reader.getObject(this.getPath());
      return entry instanceof Collection ? (Set)((Collection)entry).stream().map((val) -> {
         return this.toEnum(String.valueOf(val));
      }).filter((e) -> {
         return e != null;
      }).collect(Collectors.toCollection(LinkedHashSet::new)) : null;
   }

   private E toEnum(String str) {
      Enum[] var2 = (Enum[])this.enumClass.getEnumConstants();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         E e = var2[var4];
         if (str.equalsIgnoreCase(e.name())) {
            return e;
         }
      }

      return null;
   }

   public Object toExportValue(Set<E> value) {
      return value.stream().map(Enum::name).collect(Collectors.toList());
   }
}
