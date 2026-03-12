package com.nisovin.shopkeepers.config.lib.value;

import com.nisovin.shopkeepers.util.java.Validate;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.checkerframework.checker.nullness.qual.Nullable;

public class ValueTypeRegistry {
   private final Map<Type, ValueType<?>> byType = new HashMap();
   private final List<ValueTypeProvider> providers = new ArrayList();

   public <T> void register(Type type, ValueType<? extends T> valueType) {
      Validate.notNull(type, (String)"type is null");
      Validate.notNull(valueType, (String)"valueType is null");
      this.byType.put(type, valueType);
   }

   public boolean hasCachedValueType(Type type) {
      return this.byType.containsKey(type);
   }

   public void register(TypePattern typePattern, ValueType<?> valueType) {
      Validate.notNull(typePattern, (String)"typePattern is null");
      Validate.notNull(valueType, (String)"valueType is null");
      this.register(ValueTypeProviders.forTypePattern(typePattern, (type) -> {
         return valueType;
      }));
   }

   public void register(ValueTypeProvider valueTypeProvider) {
      Validate.notNull(valueTypeProvider, (String)"valueTypeProvider is null");
      this.providers.add(valueTypeProvider);
   }

   @Nullable
   public <T> ValueType<T> getValueType(Type type) {
      ValueType<T> valueType = (ValueType)this.byType.get(type);
      if (valueType == null) {
         Iterator var3 = this.providers.iterator();

         while(var3.hasNext()) {
            ValueTypeProvider provider = (ValueTypeProvider)var3.next();
            valueType = provider.get(type);
            if (valueType != null) {
               this.register(type, valueType);
               break;
            }
         }
      }

      return valueType;
   }
}
