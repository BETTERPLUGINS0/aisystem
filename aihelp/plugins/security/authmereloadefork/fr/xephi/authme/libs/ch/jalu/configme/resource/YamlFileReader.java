package fr.xephi.authme.libs.ch.jalu.configme.resource;

import fr.xephi.authme.libs.ch.jalu.configme.exception.ConfigMeException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.error.YAMLException;

public class YamlFileReader implements PropertyReader {
   private final Path path;
   private final Charset charset;
   @Nullable
   private final Map<String, Object> root;

   public YamlFileReader(Path path) {
      this(path, StandardCharsets.UTF_8, true);
   }

   public YamlFileReader(Path path, Charset charset) {
      this(path, charset, true);
   }

   public YamlFileReader(Path path, Charset charset, boolean splitDotPaths) {
      this.path = path;
      this.charset = charset;
      this.root = this.loadFile(splitDotPaths);
   }

   /** @deprecated */
   @Deprecated
   public YamlFileReader(File file) {
      this(file.toPath(), StandardCharsets.UTF_8);
   }

   public Object getObject(String path) {
      if (path.isEmpty()) {
         return this.root;
      } else {
         Object node = this.root;
         String[] keys = path.split("\\.");
         String[] var4 = keys;
         int var5 = keys.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            String key = var4[var6];
            node = getEntryIfIsMap(key, node);
            if (node == null) {
               return null;
            }
         }

         return node;
      }
   }

   public String getString(String path) {
      return (String)this.getTypedObject(path, String.class);
   }

   public Integer getInt(String path) {
      Number n = (Number)this.getTypedObject(path, Number.class);
      return n == null ? null : n.intValue();
   }

   public Double getDouble(String path) {
      Number n = (Number)this.getTypedObject(path, Number.class);
      return n == null ? null : n.doubleValue();
   }

   public Boolean getBoolean(String path) {
      return (Boolean)this.getTypedObject(path, Boolean.class);
   }

   public List<?> getList(String path) {
      return (List)this.getTypedObject(path, List.class);
   }

   public boolean contains(String path) {
      return this.getObject(path) != null;
   }

   public Set<String> getKeys(boolean onlyLeafNodes) {
      if (this.root == null) {
         return Collections.emptySet();
      } else {
         Set<String> allKeys = new LinkedHashSet();
         this.collectKeysIntoSet("", this.root, allKeys, onlyLeafNodes);
         return allKeys;
      }
   }

   public Set<String> getChildKeys(String path) {
      Object object = this.getObject(path);
      if (object instanceof Map) {
         String pathPrefix = path.isEmpty() ? "" : path + ".";
         return (Set)((Map)object).keySet().stream().map((childPath) -> {
            return pathPrefix + childPath;
         }).collect(Collectors.toCollection(LinkedHashSet::new));
      } else {
         return Collections.emptySet();
      }
   }

   private void collectKeysIntoSet(String path, Map<String, Object> map, Set<String> result, boolean onlyLeafNodes) {
      Iterator var5 = map.entrySet().iterator();

      while(var5.hasNext()) {
         Entry<String, Object> entry = (Entry)var5.next();
         String childPath = path.isEmpty() ? (String)entry.getKey() : path + "." + (String)entry.getKey();
         if (!onlyLeafNodes || isLeafValue(entry.getValue())) {
            result.add(childPath);
         }

         if (entry.getValue() instanceof Map) {
            this.collectKeysIntoSet(childPath, (Map)entry.getValue(), result, onlyLeafNodes);
         }
      }

   }

   private static boolean isLeafValue(Object o) {
      return !(o instanceof Map) || ((Map)o).isEmpty();
   }

   protected Map<String, Object> loadFile(boolean splitDotPaths) {
      try {
         InputStream is = Files.newInputStream(this.path);
         Throwable var3 = null;

         Map var7;
         try {
            InputStreamReader isr = new InputStreamReader(is, this.charset);
            Throwable var5 = null;

            try {
               Map<Object, Object> rootMap = (Map)(new Yaml()).load(isr);
               var7 = this.normalizeMap(rootMap, splitDotPaths);
            } catch (Throwable var36) {
               var5 = var36;
               throw var36;
            } finally {
               if (isr != null) {
                  if (var5 != null) {
                     try {
                        isr.close();
                     } catch (Throwable var35) {
                        var5.addSuppressed(var35);
                     }
                  } else {
                     isr.close();
                  }
               }

            }
         } catch (Throwable var38) {
            var3 = var38;
            throw var38;
         } finally {
            if (is != null) {
               if (var3 != null) {
                  try {
                     is.close();
                  } catch (Throwable var34) {
                     var3.addSuppressed(var34);
                  }
               } else {
                  is.close();
               }
            }

         }

         return var7;
      } catch (IOException var40) {
         throw new ConfigMeException("Could not read file '" + this.path + "'", var40);
      } catch (ClassCastException var41) {
         throw new ConfigMeException("Top-level is not a map in '" + this.path + "'", var41);
      } catch (YAMLException var42) {
         throw new ConfigMeException("YAML error while trying to load file '" + this.path + "'", var42);
      }
   }

   @Nullable
   protected Map<String, Object> normalizeMap(@Nullable Map<Object, Object> map, boolean splitDotPaths) {
      return (new MapNormalizer(splitDotPaths)).normalizeMap(map);
   }

   /** @deprecated */
   @Deprecated
   protected final File getFile() {
      return this.path.toFile();
   }

   protected final Path getPath() {
      return this.path;
   }

   /** @deprecated */
   @Nullable
   @Deprecated
   protected final Map<String, Object> getRoot() {
      return this.root;
   }

   @Nullable
   protected <T> T getTypedObject(String path, Class<T> clazz) {
      Object value = this.getObject(path);
      return clazz.isInstance(value) ? clazz.cast(value) : null;
   }

   @Nullable
   private static Object getEntryIfIsMap(String key, Object value) {
      return value instanceof Map ? ((Map)value).get(key) : null;
   }
}
