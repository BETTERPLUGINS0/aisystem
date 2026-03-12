package emanondev.itemedit;

import emanondev.itemedit.utility.ItemUtils;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.bukkit.Bukkit;
import org.bukkit.Keyed;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ParsedItem {
   private final String type;
   private final Map<String, Object> components;
   private int amount;

   public ParsedItem(ItemStack itemStack) {
      this(itemStack.getType(), ItemUtils.getMeta(itemStack));
      this.amount = itemStack.getAmount();
   }

   private ParsedItem(Material mat, ItemMeta meta) {
      this(mat, meta.getAsString());
   }

   private ParsedItem(Material mat, String raw) {
      this.components = new LinkedHashMap();
      int index = raw.indexOf("{");
      if (index == -1) {
         this.type = mat.getKey().toString();
      } else {
         this.type = mat.getKey().toString();
         raw = raw.substring(index);
         this.components.putAll((Map)this.eatMap(raw, 0, 0).payload);
      }
   }

   public static Map<String, Object> loadMap(Map<String, Object> data, String path) {
      data.putIfAbsent(path, new LinkedHashMap());
      return (Map)data.get(path);
   }

   public static List<Object> loadList(Map<String, Object> data, String path) {
      data.putIfAbsent(path, new ArrayList());
      return (List)data.get(path);
   }

   public static Map<String, Object> getMap(Map<String, Object> data, String path) {
      return (Map)data.getOrDefault(path, (Object)null);
   }

   public static List<Object> getListOfRaw(Map<String, Object> data, String path) {
      return (List)data.getOrDefault(path, (Object)null);
   }

   public static List<Map<String, Object>> getListOfMap(Map<String, Object> data, String path) {
      return (List)data.getOrDefault(path, (Object)null);
   }

   public static List<Object> loadListOfRaw(Map<String, Object> data, String path) {
      data.putIfAbsent(path, new ArrayList());
      return (List)data.get(path);
   }

   public static List<Map<String, Object>> loadListOfMap(Map<String, Object> data, String path) {
      data.putIfAbsent(path, new ArrayList());
      return (List)data.get(path);
   }

   public static NamespacedKey readNamespacedKey(Map<String, Object> data, String path, NamespacedKey defValue) {
      String text = readString(data, path, (String)null);
      return text == null ? defValue : new NamespacedKey(text.split(":")[0], text.split(":")[1]);
   }

   public static NamespacedKey readNamespacedKey(Map<String, Object> data, String path) {
      return readNamespacedKey(data, path, (NamespacedKey)null);
   }

   public static String readString(Map<String, Object> data, String path, String defValue) {
      return !data.containsKey(path) ? defValue : (String)data.get(path);
   }

   public static String readString(Map<String, Object> data, String path) {
      return readString(data, path, (String)null);
   }

   public static Boolean readBoolean(Map<String, Object> data, String path, Boolean defValue) {
      Integer value = readInt(data, path);
      return value == null ? defValue : value != 0;
   }

   public static Boolean readBoolean(Map<String, Object> data, String path) {
      return readBoolean(data, path, (Boolean)null);
   }

   public static Integer readInt(Map<String, Object> data, String path) {
      return readInt(data, path, (Integer)null);
   }

   public static Integer readInt(Map<String, Object> data, String path, Integer defValue) {
      if (!data.containsKey(path)) {
         return defValue;
      } else {
         String value = (String)data.get(path);
         if (value.endsWith("b")) {
            value = value.substring(0, value.length() - 1);
         }

         return Integer.parseInt(value);
      }
   }

   public static Double readDouble(Map<String, Object> data, String path) {
      return readDouble(data, path, (Double)null);
   }

   public static Double readDouble(Map<String, Object> data, String path, Double defValue) {
      if (!data.containsKey(path)) {
         return defValue;
      } else {
         String value = (String)data.get(path);
         if (value.endsWith("f")) {
            value = value.substring(0, value.length() - 1);
         }

         return Double.parseDouble(value);
      }
   }

   public static Float readFloat(Map<String, Object> data, String path) {
      return readFloat(data, path, (Float)null);
   }

   public static Float readFloat(Map<String, Object> data, String path, Float defValue) {
      Double value = readDouble(data, path, (Double)null);
      return value == null ? defValue : value.floatValue();
   }

   public static void setValue(Map<String, Object> data, String path, String value) {
      if (value == null) {
         data.remove(path);
      } else if (value.startsWith("\"") && value.endsWith("\"")) {
         data.put(path, value);
      } else {
         data.put(path, "\"" + value + "\"");
      }

   }

   public static void setValue(Map<String, Object> data, String path, Boolean value) {
      if (value == null) {
         data.remove(path);
      } else {
         data.put(path, String.valueOf(value ? 1 : 0));
      }

   }

   public static void setValue(Map<String, Object> data, String path, Integer value) {
      if (value == null) {
         data.remove(path);
      } else {
         data.put(path, value.toString());
      }

   }

   public static void setValue(Map<String, Object> data, String path, Double value) {
      if (value == null) {
         data.remove(path);
      } else {
         data.put(path, value.toString());
      }

   }

   public static void setValue(Map<String, Object> data, String path, Float value) {
      if (value == null) {
         data.remove(path);
      } else {
         data.put(path, value.toString());
      }

   }

   public static void setValue(Map<String, Object> data, String path, NamespacedKey value) {
      if (value == null) {
         data.remove(path);
      } else {
         data.put(path, value.toString());
      }

   }

   public static void setValue(Map<String, Object> data, String path, Keyed value) {
      if (value == null) {
         data.remove(path);
      } else {
         data.put(path, value.getKey().toString());
      }

   }

   public void set(@Nullable String value, String... paths) {
      Map<String, Object> map = this.components;

      for(int i = 0; i < paths.length - 1; ++i) {
         if (!map.containsKey(paths[i]) || !(map.get(paths[i]) instanceof Map)) {
            if (value == null) {
               return;
            }

            map.put(paths[i], new LinkedHashMap());
         }

         map = (Map)map.get(paths[i]);
      }

      if (value == null) {
         map.remove(paths[paths.length - 1]);
      } else {
         map.put(paths[paths.length - 1], value);
      }

   }

   public void set(@NotNull Map<String, Object> value, String... paths) {
      Map<String, Object> map = this.components;

      for(int i = 0; i < paths.length - 1; ++i) {
         if (!map.containsKey(paths[i]) || !(map.get(paths[i]) instanceof Map)) {
            map.put(paths[i], new LinkedHashMap());
         }

         map = (Map)map.get(paths[i]);
      }

      this.fixValue(value);
      map.put(paths[paths.length - 1], value);
   }

   public void remove(String... paths) {
      Map<String, Object> map = this.components;

      for(int i = 0; i < paths.length - 1; ++i) {
         if (!map.containsKey(paths[i]) || !(map.get(paths[i]) instanceof Map)) {
            return;
         }

         map = (Map)map.get(paths[i]);
      }

      map.remove(paths[paths.length - 1], new LinkedHashMap());
   }

   public void loadEmptyMap(String... paths) {
      Map<String, Object> map = this.components;

      for(int i = 0; i < paths.length - 1; ++i) {
         if (!map.containsKey(paths[i]) && map.get(paths[i]) instanceof Map) {
            map.put(paths[i], new LinkedHashMap());
         }

         map = (Map)map.get(paths[i]);
      }

      if (!map.containsKey(paths[paths.length - 1])) {
         map.put(paths[paths.length - 1], new LinkedHashMap());
      }

   }

   public void set(@NotNull List<Map<String, Object>> value, String... paths) {
      Map<String, Object> map = this.components;

      for(int i = 0; i < paths.length - 1; ++i) {
         if (!map.containsKey(paths[i]) || !(map.get(paths[i]) instanceof Map)) {
            map.put(paths[i], new LinkedHashMap());
         }

         map = (Map)map.get(paths[i]);
      }

      this.fixValue(value);
      map.put(paths[paths.length - 1], value);
   }

   private void fixValue(Map<String, Object> value) {
      value.forEach((k, v) -> {
         if (v instanceof Map) {
            this.fixValue((Map)v);
         } else if (v instanceof Number) {
            value.put(k, v.toString());
         } else if (v instanceof NamespacedKey) {
            value.put(k, v.toString());
         } else if (v instanceof Boolean) {
            value.put(k, (Boolean)v ? "1b" : "0b");
         } else if (v instanceof List) {
            this.fixValue((List)v);
         }

      });
   }

   private void fixValue(List<Map<String, Object>> value) {
      value.forEach(this::fixValue);
   }

   public String load(@Nullable String defValue, String... paths) {
      String raw = this.read(paths);
      if (raw == null && defValue != null) {
         this.set(defValue, paths);
         return defValue;
      } else {
         return raw;
      }
   }

   public Double load(Double defValue, String... paths) {
      Double raw = this.readDouble((Double)null, (String[])paths);
      if (raw == null && defValue != null) {
         this.set(defValue, paths);
         return defValue;
      } else {
         return raw;
      }
   }

   public Float load(Float defValue, String... paths) {
      Float raw = this.readFloat((Float)null, (String[])paths);
      if (raw == null && defValue != null) {
         this.set(defValue, paths);
         return defValue;
      } else {
         return raw;
      }
   }

   public Long load(Long defValue, String... paths) {
      Long raw = this.readLong((Long)null, paths);
      if (raw == null && defValue != null) {
         this.set(defValue, paths);
         return defValue;
      } else {
         return raw;
      }
   }

   public Integer load(Integer defValue, String... paths) {
      Integer raw = this.readInteger((Integer)null, paths);
      if (raw == null && defValue != null) {
         this.set(defValue, paths);
         return defValue;
      } else {
         return raw;
      }
   }

   public Byte load(Byte defValue, String... paths) {
      Byte raw = this.readByte((Byte)null, paths);
      if (raw == null && defValue != null) {
         this.set(defValue, paths);
         return defValue;
      } else {
         return raw;
      }
   }

   public Boolean load(Boolean defValue, String... paths) {
      Boolean raw = this.readBoolean((Boolean)null, (String[])paths);
      if (raw == null && defValue != null) {
         this.set(defValue, paths);
         return defValue;
      } else {
         return raw;
      }
   }

   public NamespacedKey load(NamespacedKey defValue, String... paths) {
      NamespacedKey raw = this.readNamespacedKey((NamespacedKey)null, (String[])paths);
      if (raw == null && defValue != null) {
         this.set(defValue, paths);
         return defValue;
      } else {
         return raw;
      }
   }

   public void set(Double value, String... paths) {
      this.set(value == null ? null : String.valueOf(value), paths);
   }

   public void set(Float value, String... paths) {
      this.set(value == null ? null : value + "f", paths);
   }

   public void set(Long value, String... paths) {
      this.set(value == null ? null : String.valueOf(value), paths);
   }

   public void set(Integer value, String... paths) {
      this.set(value == null ? null : String.valueOf(value), paths);
   }

   public void set(Byte value, String... paths) {
      this.set(value == null ? null : value + "b", paths);
   }

   public void set(Boolean value, String... paths) {
      this.set(value == null ? null : (value ? "1b" : "0b"), paths);
   }

   public void set(NamespacedKey value, String... paths) {
      this.set(value == null ? null : value.toString(), paths);
   }

   private String read(String[] paths) {
      Map<String, Object> map = this.components;

      for(int i = 0; i < paths.length - 1; ++i) {
         if (!map.containsKey(paths[i]) || !(map.get(paths[i]) instanceof Map)) {
            return null;
         }

         map = (Map)map.get(paths[i]);
      }

      return map.get(paths[paths.length - 1]) instanceof String ? (String)map.get(paths[paths.length - 1]) : null;
   }

   public Map<String, Object> readMap(String... paths) {
      Map<String, Object> map = this.components;

      for(int i = 0; i < paths.length - 1; ++i) {
         if (!map.containsKey(paths[i]) || !(map.get(paths[i]) instanceof Map)) {
            return null;
         }

         map = (Map)map.get(paths[i]);
      }

      return map.get(paths[paths.length - 1]) instanceof Map ? (Map)map.get(paths[paths.length - 1]) : null;
   }

   public List<Map<String, Object>> readList(String... paths) {
      Map<String, Object> map = this.components;

      for(int i = 0; i < paths.length - 1; ++i) {
         if (!map.containsKey(paths[i]) || !(map.get(paths[i]) instanceof Map)) {
            return null;
         }

         map = (Map)map.get(paths[i]);
      }

      return map.get(paths[paths.length - 1]) instanceof List ? (List)map.get(paths[paths.length - 1]) : null;
   }

   public Integer readInteger(Integer defValue, String... paths) {
      String value = this.read(paths);
      if (value == null) {
         return defValue;
      } else {
         if (value.endsWith("b") || value.endsWith("f")) {
            value = value.substring(0, value.length() - 1);
         }

         return Integer.valueOf(value);
      }
   }

   public String readString(String defValue, String... paths) {
      String value = this.read(paths);
      return value == null ? defValue : value;
   }

   public Double readDouble(Double defValue, String... paths) {
      String value = this.read(paths);
      if (value == null) {
         return defValue;
      } else {
         if (value.endsWith("b") || value.endsWith("f")) {
            value = value.substring(0, value.length() - 1);
         }

         return Double.valueOf(value);
      }
   }

   public Float readFloat(Float defValue, String... paths) {
      String value = this.read(paths);
      if (value == null) {
         return defValue;
      } else {
         if (value.endsWith("b") || value.endsWith("f")) {
            value = value.substring(0, value.length() - 1);
         }

         return Float.valueOf(value);
      }
   }

   public Long readLong(Long defValue, String... paths) {
      String value = this.read(paths);
      if (value == null) {
         return defValue;
      } else {
         if (value.endsWith("b") || value.endsWith("f")) {
            value = value.substring(0, value.length() - 1);
         }

         return Long.valueOf(value);
      }
   }

   public Byte readByte(Byte defValue, String... paths) {
      String value = this.read(paths);
      if (value == null) {
         return defValue;
      } else {
         if (value.endsWith("b") || value.endsWith("f")) {
            value = value.substring(0, value.length() - 1);
         }

         return Byte.valueOf(value);
      }
   }

   public NamespacedKey readNamespacedKey(NamespacedKey defValue, String... paths) {
      String value = this.read(paths);
      if (value == null) {
         return defValue;
      } else {
         String[] split = value.split(":");
         return split.length != 2 ? defValue : new NamespacedKey(split[0], split[1]);
      }
   }

   public Boolean readBoolean(Boolean defValue, String... paths) {
      return this.readInteger(!defValue ? 0 : 1, paths) != 0;
   }

   public Map<String, Object> getMap() {
      return this.components;
   }

   public ItemStack toItemStack() {
      ItemStack item = Bukkit.getItemFactory().createItemStack(this.toString());
      item.setAmount(this.amount);
      return item;
   }

   public ItemMeta toItemMeta() {
      return Bukkit.getItemFactory().createItemStack(this.toString()).getItemMeta();
   }

   public String toString() {
      StringBuilder text = new StringBuilder(this.type);
      if (this.components.isEmpty()) {
         return text.toString();
      } else {
         text.append("[");
         this.components.forEach((key, value) -> {
            text.append(key).append("=").append(this.writeComponent(value)).append(",");
         });
         return text.substring(0, text.length() - 1) + "]";
      }
   }

   private boolean needBrackets(String text) {
      Pattern pattern = Pattern.compile("^[-_.0-9a-zA-Z]+$");
      Matcher matcher = pattern.matcher(text);
      return !matcher.matches();
   }

   private String writeComponent(Object value) {
      if (value instanceof String) {
         String text = (String)value;
         if (text.startsWith("'")) {
            return text;
         } else {
            return this.needBrackets(text) ? "\"" + text + "\"" : text;
         }
      } else {
         StringBuilder text;
         if (value instanceof List) {
            List<Object> list = (List)value;
            if (list.isEmpty()) {
               return "[]";
            } else {
               text = new StringBuilder("[");
               list.forEach((el) -> {
                  text.append(this.writeComponent(el)).append(",");
               });
               return text.substring(0, text.length() - 1) + "]";
            }
         } else if (value instanceof Map) {
            Map<String, Object> map = (Map)value;
            if (map.isEmpty()) {
               return "{}";
            } else {
               text = new StringBuilder("{");
               map.forEach((key, val) -> {
                  text.append(this.needBrackets(key) ? "\"" + key + "\"" : key).append(":").append(this.writeComponent(val)).append(",");
               });
               return text.substring(0, text.length() - 1) + "}";
            }
         } else {
            ItemEdit.get().log(value.getClass().getSimpleName() + " " + value);
            throw new RuntimeException();
         }
      }
   }

   private ParsedItem.EatResult eatList(String raw, int depth, int index) {
      List<Object> value = new ArrayList();
      ++index;

      while(raw.charAt(index) != ']') {
         ParsedItem.EatResult tmp;
         switch(raw.charAt(index)) {
         case '"':
            tmp = this.eatString(raw, depth + 1, index);
            break;
         case '\'':
            tmp = this.eatTextComponent(raw, depth + 1, index);
            break;
         case '[':
            tmp = this.eatList(raw, depth + 1, index);
            break;
         case '{':
            tmp = this.eatMap(raw, depth + 1, index);
            break;
         default:
            tmp = this.eatRawValue(raw, 1, index);
         }

         value.add(tmp.payload);
         index = tmp.newIndex;
         if (raw.charAt(index) == ',') {
            ++index;
         }
      }

      return new ParsedItem.EatResult(index + 1, value);
   }

   private ParsedItem.EatResult eatMap(String raw, int depth, int index) {
      Map<String, Object> map = new LinkedHashMap();
      ++index;

      while(raw.charAt(index) != '}') {
         String key;
         ParsedItem.EatResult res;
         if (raw.charAt(index) == '"') {
            res = this.eatString(raw, depth + 1, index);
            key = (String)res.payload;
            index = res.newIndex;
         } else {
            res = this.eatRawValue(raw, depth + 1, index);
            key = (String)res.payload;
            index = res.newIndex;
         }

         ++index;
         ParsedItem.EatResult tmp;
         switch(raw.charAt(index)) {
         case '"':
            tmp = this.eatString(raw, depth + 1, index);
            break;
         case '\'':
            tmp = this.eatTextComponent(raw, depth + 1, index);
            break;
         case '[':
            tmp = this.eatList(raw, depth + 1, index);
            break;
         case '{':
            tmp = this.eatMap(raw, depth + 1, index);
            break;
         default:
            tmp = this.eatRawValue(raw, 1, index);
         }

         Object value = tmp.payload;
         index = tmp.newIndex;
         map.put(key.toString(), value);
         if (raw.charAt(index) == ',') {
            ++index;
         }
      }

      return new ParsedItem.EatResult(index + 1, map);
   }

   private ParsedItem.EatResult eatString(String raw, int depth, int index) {
      StringBuilder value = new StringBuilder();
      ++index;

      while(raw.charAt(index) != '"') {
         if (raw.charAt(index) == '\\') {
            value.append("\\");
            ++index;
         }

         value.append(raw.charAt(index));
         ++index;
      }

      return new ParsedItem.EatResult(index + 1, value.toString());
   }

   private ParsedItem.EatResult eatTextComponent(String raw, int depth, int index) {
      StringBuilder value = new StringBuilder("'");
      ++index;

      while(raw.charAt(index) != '\'') {
         if (raw.charAt(index) == '\\') {
            value.append("\\");
            ++index;
         }

         value.append(raw.charAt(index));
         ++index;
      }

      value.append('\'');
      return new ParsedItem.EatResult(index + 1, value.toString());
   }

   private ParsedItem.EatResult eatRawValue(String raw, int depth, int index) {
      StringBuilder value;
      for(value = new StringBuilder(); raw.charAt(index) != ',' && raw.charAt(index) != ']' && raw.charAt(index) != '}' && raw.charAt(index) != ':' && raw.charAt(index) != '='; ++index) {
         value.append(raw.charAt(index));
      }

      return new ParsedItem.EatResult(index, value.toString());
   }

   public List<Map<String, Object>> loadEmptyList(String... paths) {
      Map<String, Object> map = this.components;

      for(int i = 0; i < paths.length - 1; ++i) {
         if (!map.containsKey(paths[i]) && map.get(paths[i]) instanceof Map) {
            map.put(paths[i], new LinkedHashMap());
         }

         map = (Map)map.get(paths[i]);
      }

      if (!map.containsKey(paths[paths.length - 1])) {
         map.put(paths[paths.length - 1], new ArrayList());
      }

      return (List)map.get(paths[paths.length - 1]);
   }

   private static class EatResult {
      private final int newIndex;
      private final Object payload;

      private EatResult(int newIndex, Object payload) {
         this.newIndex = newIndex;
         this.payload = payload;
      }

      // $FF: synthetic method
      EatResult(int x0, Object x1, Object x2) {
         this(x0, x1);
      }
   }
}
