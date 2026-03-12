package com.nisovin.shopkeepers.types;

import com.nisovin.shopkeepers.api.types.TypeRegistry;
import com.nisovin.shopkeepers.util.java.StringUtils;
import com.nisovin.shopkeepers.util.java.Validate;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public abstract class AbstractTypeRegistry<T extends AbstractType> implements TypeRegistry<T> {
   private final Map<String, T> registeredTypes = new LinkedHashMap();
   private final Collection<? extends T> registeredTypesView;

   protected AbstractTypeRegistry() {
      this.registeredTypesView = Collections.unmodifiableCollection(this.registeredTypes.values());
   }

   public void register(@NonNull T type) {
      Validate.notNull(type, (String)"type is null");
      String identifier = type.getIdentifier();

      assert identifier != null && !identifier.isEmpty();

      boolean var10000 = !this.registeredTypes.containsKey(identifier);
      String var10001 = this.getTypeName();
      Validate.isTrue(var10000, "A " + var10001 + " with this identifier is already registered: " + identifier);
      this.registeredTypes.put(identifier, type);
   }

   public void registerAll(Collection<? extends T> types) {
      Validate.notNull(types, (String)"types is null");
      types.forEach(this::register);
   }

   protected abstract String getTypeName();

   public Collection<? extends T> getRegisteredTypes() {
      return this.registeredTypesView;
   }

   @Nullable
   public T get(String identifier) {
      return (AbstractType)this.registeredTypes.get(identifier);
   }

   @Nullable
   public T match(String identifier) {
      Validate.notNull(identifier, (String)"identifier is null");
      String normalized = StringUtils.normalize(identifier);
      Iterator var3 = this.registeredTypesView.iterator();

      AbstractType type;
      do {
         if (!var3.hasNext()) {
            return null;
         }

         type = (AbstractType)var3.next();
      } while(!type.matches(normalized));

      return type;
   }

   public void clearAll() {
      this.registeredTypes.clear();
   }
}
