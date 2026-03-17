package advancedplugins.pm2.cv.models.api.utils.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GSONUtils {
   public static void ifPresent(JsonElement var0, String var1, Consumer<JsonElement> var2) {
      if (var0 instanceof JsonObject) {
         JsonObject var3 = (JsonObject)var0;
         if (var3.has(var1)) {
            var2.accept(var3.get(var1));
         }
      }

   }

   public static void ifArray(JsonElement var0, String var1, Consumer<JsonElement> var2) {
      if (var0 instanceof JsonObject) {
         JsonObject var3 = (JsonObject)var0;
         if (var3.has(var1)) {
            JsonElement var4 = var3.get(var1);
            if (var4 instanceof JsonArray) {
               JsonArray var5 = (JsonArray)var4;
               var5.forEach(var2);
            }
         }
      }

   }

   public static void ifArray(JsonElement var0, String var1, BiConsumer<Integer, JsonElement> var2) {
      if (var0 instanceof JsonObject) {
         JsonObject var3 = (JsonObject)var0;
         if (var3.has(var1)) {
            JsonElement var4 = var3.get(var1);
            if (var4 instanceof JsonArray) {
               JsonArray var5 = (JsonArray)var4;

               for(int var6 = 0; var6 < var5.size(); ++var6) {
                  var2.accept(var6, var5.get(var6));
               }
            }
         }
      }

   }

   @Nullable
   public static <T> T get(JsonElement var0, String var1, Function<JsonElement, T> var2) {
      if (var0 instanceof JsonObject) {
         JsonObject var3 = (JsonObject)var0;
         if (!var3.has(var1)) {
            return null;
         } else {
            JsonElement var4 = var3.get(var1);
            if (var4.isJsonNull()) {
               return null;
            } else {
               try {
                  return var2.apply(var4);
               } catch (Exception var6) {
                  return null;
               }
            }
         }
      } else {
         return null;
      }
   }

   @NotNull
   public static <T> T get(JsonElement var0, String var1, Function<JsonElement, T> var2, @NotNull T var3) {
      Object var4 = get(var0, var1, var2);
      return var4 == null ? var3 : var4;
   }

   @NotNull
   public static <T> T get(JsonElement var0, Function<JsonElement, T> var1, @NotNull T var2) {
      try {
         return var1.apply(var0);
      } catch (Exception var4) {
         return var2;
      }
   }

   public static Float[] getAsFloatArray(JsonElement var0) {
      if (!var0.isJsonArray()) {
         return null;
      } else {
         JsonArray var1 = var0.getAsJsonArray();
         return (Float[])var1.asList().stream().map(JsonElement::getAsFloat).toArray((var0x) -> {
            return new Float[var0x];
         });
      }
   }

   public static UUID getAsUUID(JsonElement var0) {
      return UUID.fromString(var0.getAsString());
   }
}
