package xyz.xenondevs.particle;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import xyz.xenondevs.particle.utils.ReflectionUtils;

public class ParticleMappings {
   private static final Map<String, String> mappings = new HashMap();

   private static void processMapping(JsonObject object, double version) {
      if (!(version < object.get("min").getAsDouble()) && !(version > object.get("max").getAsDouble())) {
         String name = object.get("name").getAsString();
         JsonArray mappingsArray = object.get("mappings").getAsJsonArray();
         String bestMatch = null;
         double lastVersion = 0.0D;

         for(int i = 0; i < mappingsArray.size(); ++i) {
            JsonObject mapping = mappingsArray.get(i).getAsJsonObject();
            double from = mapping.get("from").getAsDouble();
            if (version >= from && from > lastVersion) {
               bestMatch = mapping.get("value").getAsString();
            }
         }

         if (bestMatch != null) {
            mappings.put(name, bestMatch);
         }

      }
   }

   public static Class<?> getMappedClass(String name) {
      return !mappings.containsKey(name) ? null : ReflectionUtils.getNMSClass((String)mappings.get(name));
   }

   public static Method getMappedMethod(Class<?> targetClass, String name, Class<?>... parameterTypes) {
      return !mappings.containsKey(name) ? null : ReflectionUtils.getMethodOrNull(targetClass, (String)mappings.get(name), parameterTypes);
   }

   public static Field getMappedField(Class targetClass, String name, boolean declared) {
      return !mappings.containsKey(name) ? null : ReflectionUtils.getFieldOrNull(targetClass, (String)mappings.get(name), declared);
   }

   static {
      double version = ReflectionUtils.MINECRAFT_VERSION;

      try {
         InputStreamReader reader = new InputStreamReader((InputStream)Objects.requireNonNull(ReflectionUtils.getResourceStreamSafe("mappings.json")));

         try {
            JsonArray array = version < 18.0D ? (new JsonParser()).parse(reader).getAsJsonArray() : JsonParser.parseReader(reader).getAsJsonArray();

            for(int i = 0; i < array.size(); ++i) {
               JsonObject object = array.get(i).getAsJsonObject();
               processMapping(object, version);
            }
         } catch (Throwable var7) {
            try {
               reader.close();
            } catch (Throwable var6) {
               var7.addSuppressed(var6);
            }

            throw var7;
         }

         reader.close();
      } catch (Exception var8) {
         throw new RuntimeException("Could not load mappings", var8);
      }
   }
}
