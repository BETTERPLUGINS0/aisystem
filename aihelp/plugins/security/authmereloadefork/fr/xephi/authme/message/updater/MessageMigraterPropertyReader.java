package fr.xephi.authme.message.updater;

import fr.xephi.authme.libs.ch.jalu.configme.exception.ConfigMeException;
import fr.xephi.authme.libs.ch.jalu.configme.resource.PropertyReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.yaml.snakeyaml.Yaml;

final class MessageMigraterPropertyReader implements PropertyReader {
   private static final Charset CHARSET;
   private Map<String, Object> root;

   private MessageMigraterPropertyReader(Map<String, Object> valuesMap) {
      this.root = valuesMap;
   }

   public static MessageMigraterPropertyReader loadFromFile(File file) {
      try {
         FileInputStream is = new FileInputStream(file);

         MessageMigraterPropertyReader var2;
         try {
            var2 = loadFromStream(is);
         } catch (Throwable var5) {
            try {
               is.close();
            } catch (Throwable var4) {
               var5.addSuppressed(var4);
            }

            throw var5;
         }

         is.close();
         return var2;
      } catch (IOException var6) {
         throw new IllegalStateException("Error while reading file '" + file + "'", var6);
      }
   }

   public static MessageMigraterPropertyReader loadFromStream(InputStream inputStream) {
      Map<String, Object> valuesMap = readStreamToMap(inputStream);
      return new MessageMigraterPropertyReader(valuesMap);
   }

   public boolean contains(String path) {
      return this.getObject(path) != null;
   }

   public Set<String> getKeys(boolean b) {
      throw new UnsupportedOperationException();
   }

   public Set<String> getChildKeys(String s) {
      throw new UnsupportedOperationException();
   }

   public Object getObject(String path) {
      if (path.isEmpty()) {
         return this.root.get("");
      } else {
         Object node = this.root;
         String[] keys = path.split("\\.");
         String[] var4 = keys;
         int var5 = keys.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            String key = var4[var6];
            node = getIfIsMap(key, node);
            if (node == null) {
               return null;
            }
         }

         return node;
      }
   }

   public String getString(String path) {
      Object o = this.getObject(path);
      return o instanceof String ? (String)o : null;
   }

   public Integer getInt(String path) {
      throw new UnsupportedOperationException();
   }

   public Double getDouble(String path) {
      throw new UnsupportedOperationException();
   }

   public Boolean getBoolean(String path) {
      throw new UnsupportedOperationException();
   }

   public List<?> getList(String path) {
      throw new UnsupportedOperationException();
   }

   private static Map<String, Object> readStreamToMap(InputStream inputStream) {
      try {
         InputStreamReader isr = new InputStreamReader(inputStream, CHARSET);

         Object var3;
         try {
            Object obj = (new Yaml()).load(isr);
            var3 = obj == null ? new HashMap() : (Map)obj;
         } catch (Throwable var5) {
            try {
               isr.close();
            } catch (Throwable var4) {
               var5.addSuppressed(var4);
            }

            throw var5;
         }

         isr.close();
         return (Map)var3;
      } catch (IOException var6) {
         throw new ConfigMeException("Could not read stream", var6);
      } catch (ClassCastException var7) {
         throw new ConfigMeException("Top-level is not a map", var7);
      }
   }

   private static Object getIfIsMap(String key, Object value) {
      return value instanceof Map ? ((Map)value).get(key) : null;
   }

   static {
      CHARSET = StandardCharsets.UTF_8;
   }
}
