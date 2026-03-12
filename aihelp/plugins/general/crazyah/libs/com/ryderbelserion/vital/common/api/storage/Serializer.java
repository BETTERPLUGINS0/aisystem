package libs.com.ryderbelserion.vital.common.api.storage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.annotations.Expose;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import libs.com.ryderbelserion.vital.common.api.Provider;
import org.jetbrains.annotations.NotNull;

public class Serializer<T> {
   private static final Map<String, Lock> locks = new HashMap();
   private final GsonBuilder builder = Provider.getApi().getBuilder() == null ? (new GsonBuilder()).disableHtmlEscaping().enableComplexMapKeySerialization() : Provider.getApi().getBuilder();
   private final File file;
   private final T clazz;
   private Gson gson = null;

   public Serializer(@NotNull File file, @NotNull T clazz) {
      this.file = file;
      this.clazz = clazz;
   }

   public final Serializer<T> withoutExposeAnnotation() {
      this.builder.excludeFieldsWithoutExposeAnnotation();
      return this;
   }

   public final Serializer<T> setPrettyPrinting() {
      this.builder.setPrettyPrinting();
      return this;
   }

   public final Serializer<T> withoutModifiers(int... modifiers) {
      this.builder.excludeFieldsWithModifiers(modifiers);
      return this;
   }

   public final Serializer<T> registerAdapters(@NotNull Type type, @NotNull Object object) {
      this.builder.registerTypeAdapter(type, object);
      return this;
   }

   public final Serializer<T> load() {
      if (this.gson == null) {
         this.gson = this.builder.create();
      }

      if (!this.exists()) {
         try {
            this.file.createNewFile();
            this.write();
         } catch (IOException var2) {
            var2.printStackTrace();
         }
      } else {
         this.read();
      }

      return this;
   }

   public final boolean exists() {
      return this.file.exists();
   }

   public void write() {
      Object lock;
      if (locks.containsKey(this.file.getName())) {
         lock = (Lock)locks.get(this.file.getName());
      } else {
         locks.put(this.file.getName(), lock = (new ReentrantReadWriteLock()).writeLock());
      }

      CompletableFuture.runAsync(() -> {
         try {
            FileWriter writer = new FileWriter(this.file);

            try {
               writer.write(this.gson.toJson(this.clazz));
            } catch (Throwable var11) {
               try {
                  writer.close();
               } catch (Throwable var10) {
                  var11.addSuppressed(var10);
               }

               throw var11;
            }

            writer.close();
         } catch (IOException var12) {
            var12.printStackTrace();
         } finally {
            lock.unlock();
            locks.remove(this.file.getName());
         }

      });
   }

   public void read() {
      JsonObject object = (JsonObject)CompletableFuture.supplyAsync(() -> {
         try {
            FileReader reader = new FileReader(this.file);

            JsonObject var2;
            try {
               var2 = JsonParser.parseReader(reader).getAsJsonObject();
            } catch (Throwable var5) {
               try {
                  reader.close();
               } catch (Throwable var4) {
                  var5.addSuppressed(var4);
               }

               throw var5;
            }

            reader.close();
            return var2;
         } catch (IOException var6) {
            var6.printStackTrace();
            return null;
         }
      }).join();
      if (object == null) {
         Provider.getApi().getComponentLogger().warn("Cannot read from file as object is null, File: {}", this.file.getName());
      } else {
         Field[] var2 = this.clazz.getClass().getDeclaredFields();
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            Field field = var2[var4];
            if (field.isAnnotationPresent(Expose.class)) {
               field.setAccessible(true);
               JsonElement jsonElement = object.get(field.getName());
               if (jsonElement != null) {
                  try {
                     field.set((Object)null, this.gson.fromJson(jsonElement, field.getType()));
                  } catch (IllegalAccessException var8) {
                     var8.printStackTrace();
                  }
               }
            }
         }

      }
   }
}
