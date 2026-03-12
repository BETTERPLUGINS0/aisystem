package ch.jalu.configme.properties;

import ch.jalu.configme.properties.convertresult.ConvertErrorRecorder;
import ch.jalu.configme.properties.types.PropertyType;
import ch.jalu.configme.resource.PropertyReader;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Map.Entry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MapProperty<V> extends BaseProperty<Map<String, V>> {
   private final PropertyType<V> type;

   public MapProperty(@NotNull String path, @NotNull Map<String, V> defaultValue, @NotNull PropertyType<V> type) {
      super(path, Collections.unmodifiableMap(defaultValue));
      Objects.requireNonNull(type, "type");
      this.type = type;
   }

   @Nullable
   protected Map<String, V> getFromReader(@NotNull PropertyReader reader, @NotNull ConvertErrorRecorder errorRecorder) {
      Object rawObject = reader.getObject(this.getPath());
      if (!(rawObject instanceof Map)) {
         return null;
      } else {
         Map<?, ?> rawMap = (Map)rawObject;
         Map<String, V> map = new LinkedHashMap();
         Iterator var6 = rawMap.entrySet().iterator();

         while(var6.hasNext()) {
            Entry<?, ?> entry = (Entry)var6.next();
            String path = entry.getKey().toString();
            V value = this.type.convert(entry.getValue(), errorRecorder);
            if (value != null) {
               map.put(path, value);
            }
         }

         return this.postProcessMap(map);
      }
   }

   @NotNull
   public Object toExportValue(@NotNull Map<String, V> value) {
      Map<String, Object> exportMap = new LinkedHashMap();
      Iterator var3 = value.entrySet().iterator();

      while(var3.hasNext()) {
         Entry<String, V> entry = (Entry)var3.next();
         exportMap.put(entry.getKey(), this.type.toExportValue(entry.getValue()));
      }

      return exportMap;
   }

   @NotNull
   protected Map<String, V> postProcessMap(@NotNull Map<String, V> constructedMap) {
      return Collections.unmodifiableMap(constructedMap);
   }
}
