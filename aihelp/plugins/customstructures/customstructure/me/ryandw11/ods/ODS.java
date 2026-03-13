package me.ryandw11.ods;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import me.ryandw11.ods.compression.Compressor;
import me.ryandw11.ods.compression.GZIPCompression;
import me.ryandw11.ods.compression.ZLIBCompression;
import me.ryandw11.ods.exception.ODSException;
import me.ryandw11.ods.serializer.Serializable;
import me.ryandw11.ods.tags.ByteTag;
import me.ryandw11.ods.tags.CharTag;
import me.ryandw11.ods.tags.DoubleTag;
import me.ryandw11.ods.tags.FloatTag;
import me.ryandw11.ods.tags.IntTag;
import me.ryandw11.ods.tags.ListTag;
import me.ryandw11.ods.tags.LongTag;
import me.ryandw11.ods.tags.ObjectTag;
import me.ryandw11.ods.tags.ShortTag;
import me.ryandw11.ods.tags.StringTag;

public class ODS {
   protected static boolean ignoreInvalidCustomTags = false;
   private static final List<Tag<?>> customTags = new ArrayList();
   private static final Map<String, Compressor> compressorMap = new HashMap();

   private ODS() {
   }

   public static Tag<?> wrap(Object o) {
      return wrap("", o);
   }

   public static Tag<?> wrap(String name, Object o) {
      if (o instanceof Byte) {
         return new ByteTag(name, (Byte)o);
      } else if (o instanceof Character) {
         return new CharTag(name, (Character)o);
      } else if (o instanceof Double) {
         return new DoubleTag(name, (Double)o);
      } else if (o instanceof Float) {
         return new FloatTag(name, (Float)o);
      } else if (o instanceof Integer) {
         return new IntTag(name, (Integer)o);
      } else if (o instanceof Long) {
         return new LongTag(name, (Long)o);
      } else if (o instanceof Short) {
         return new ShortTag(name, (Short)o);
      } else if (o instanceof String) {
         return new StringTag(name, (String)o);
      } else {
         return (Tag)(o instanceof List ? wrap(name, (List)o) : serialize(name, o));
      }
   }

   public static <T> T unwrap(Tag<T> tag) {
      if (tag instanceof ObjectTag) {
         try {
            ObjectTag objTag = (ObjectTag)tag;
            String clazzName = (String)objTag.getTag("ODS_TAG").getValue();
            if (clazzName == null) {
               throw new RuntimeException("Cannot unwrap object: TagObject is not a serialized object!");
            }

            return deserialize(tag, Class.forName(clazzName));
         } catch (ClassNotFoundException var3) {
            var3.printStackTrace();
         }
      }

      return tag.getValue();
   }

   public static <T> ListTag<?> wrap(String name, List<T> list) {
      List<Tag<?>> output = new ArrayList();
      Iterator var3 = list.iterator();

      while(var3.hasNext()) {
         T t = var3.next();
         output.add(wrap(t));
      }

      return new ListTag(name, output);
   }

   public static <T> List<Tag<?>> wrap(List<T> list) {
      List<Tag<?>> output = new ArrayList();
      Iterator var2 = list.iterator();

      while(var2.hasNext()) {
         T t = var2.next();
         output.add(wrap(t));
      }

      return output;
   }

   public static <T> List<T> unwrapList(List<Tag<T>> tags) {
      List<T> output = new ArrayList();
      Iterator var2 = tags.iterator();

      while(var2.hasNext()) {
         Tag<T> tag = (Tag)var2.next();
         output.add(unwrap(tag));
      }

      return output;
   }

   public static <T> List<T> unwrapListTag(ListTag<? extends Tag<T>> list) {
      List<T> output = new ArrayList();
      Iterator var2 = list.getValue().iterator();

      while(var2.hasNext()) {
         Tag<?> tag = (Tag)var2.next();
         output.add(unwrap(tag));
      }

      return output;
   }

   public static ObjectTag serialize(String key, Object obj) {
      ObjectTag objectTag = new ObjectTag(key);

      try {
         Class<?> clazz = obj.getClass();
         objectTag.addTag(new StringTag("ODS_TAG", clazz.getName()));
         Field[] var4 = clazz.getDeclaredFields();
         int var5 = var4.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            Field f = var4[var6];
            if (f.getAnnotation(Serializable.class) != null) {
               f.setAccessible(true);
               objectTag.addTag(wrap(f.getName(), f.get(obj)));
               f.setAccessible(false);
            }
         }
      } catch (IllegalAccessException var8) {
         var8.printStackTrace();
      }

      if (objectTag.getValue().size() < 2) {
         throw new RuntimeException("Cannot serialize object: No serializable fields detected!");
      } else {
         return objectTag;
      }
   }

   public static <T> T deserialize(Tag<?> tag, Class<T> mainClazz) {
      if (!(tag instanceof ObjectTag)) {
         throw new RuntimeException("Cannot deserialize tag: Tag is not an ObjectTag!");
      } else {
         ObjectTag objectTag = (ObjectTag)tag;
         if (!objectTag.hasTag("ODS_TAG")) {
            throw new RuntimeException("Cannot deserialize tag: This tag was not serialized!");
         } else {
            Object obj;
            try {
               obj = mainClazz.getConstructor().newInstance();
            } catch (Exception var10) {
               var10.printStackTrace();
               return null;
            }

            Field[] var4 = mainClazz.getDeclaredFields();
            int var5 = var4.length;

            for(int var6 = 0; var6 < var5; ++var6) {
               Field f = var4[var6];
               if (f.getAnnotation(Serializable.class) != null) {
                  try {
                     f.setAccessible(true);
                     f.set(obj, unwrap(objectTag.getTag(f.getName())));
                     f.setAccessible(false);
                  } catch (IllegalAccessException var9) {
                     var9.printStackTrace();
                  }
               }
            }

            return obj;
         }
      }
   }

   public static void registerCustomTag(Tag<?> tag) {
      if (tag.getID() > -1 && tag.getID() < 15) {
         throw new ODSException("Invalid Tag ID. ID cannot be 0 - 15");
      } else {
         customTags.add(tag);
      }
   }

   public static void registerCompression(String name, Compressor compressor) {
      compressorMap.put(name, compressor);
   }

   public static Compressor getCompressor(String name) {
      return (Compressor)compressorMap.get(name);
   }

   public static String getCompressorName(Compressor compressor) {
      Iterator var1 = compressorMap.entrySet().iterator();

      Entry entry;
      do {
         if (!var1.hasNext()) {
            return null;
         }

         entry = (Entry)var1.next();
      } while(((Compressor)entry.getValue()).getClass() != compressor.getClass());

      return (String)entry.getKey();
   }

   public static List<Tag<?>> getCustomTags() {
      return customTags;
   }

   public static void allowUndefinedTags(boolean value) {
      ignoreInvalidCustomTags = value;
   }

   static {
      compressorMap.put("GZIP", new GZIPCompression());
      compressorMap.put("ZLIB", new ZLIBCompression());
   }
}
