package ac.grim.grimac.utils.data.json;

import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import java.util.function.Function;
import java.util.function.IntFunction;

public interface JsonSerializable {
   @NotNull
   JsonElement toJson();

   @NotNull
   static JsonArray serializeArray(@Nullable @NotNull JsonSerializable[] serializableArray) {
      JsonArray array = new JsonArray();
      JsonSerializable[] var2 = serializableArray;
      int var3 = serializableArray.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         JsonSerializable serializable = var2[var4];
         array.add((JsonElement)(serializable == null ? JsonNull.INSTANCE : serializable.toJson()));
      }

      return array;
   }

   @NotNull
   static <T extends JsonSerializable> T[] deserializeArray(JsonArray jsonArray, IntFunction<T[]> newArray, Function<JsonElement, T> constructor) {
      T[] array = (JsonSerializable[])newArray.apply(jsonArray.size());

      for(int i = 0; i < jsonArray.size(); ++i) {
         array[i] = (JsonSerializable)constructor.apply(jsonArray.get(i));
      }

      return array;
   }
}
