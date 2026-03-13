package com.nisovin.shopkeepers.util.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.Strictness;
import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class JsonUtils {
   private static final ThreadLocal<Gson> GSON = ThreadLocal.withInitial(() -> {
      Gson gson = newGsonBuilder().create();
      return (Gson)Unsafe.assertNonNull(gson);
   });
   private static final ThreadLocal<Gson> GSON_PRETTY = ThreadLocal.withInitial(() -> {
      Gson gson = newGsonBuilder().setPrettyPrinting().create();
      return (Gson)Unsafe.assertNonNull(gson);
   });

   private static GsonBuilder newGsonBuilder() {
      GsonBuilder builder = (new GsonBuilder()).disableHtmlEscaping().serializeSpecialFloatingPointValues().setStrictness(Strictness.LENIENT).registerTypeAdapterFactory(BukkitAwareObjectTypeAdapter.FACTORY);

      assert builder != null;

      return builder;
   }

   public static String toJson(@Nullable Object object) {
      return toJson((Gson)GSON.get(), object);
   }

   public static String toPrettyJson(@Nullable Object object) {
      return toJson((Gson)GSON_PRETTY.get(), object);
   }

   private static String toJson(Gson gson, @Nullable Object object) {
      String json = gson.toJson(Unsafe.nullableAsNonNull(object));
      return (String)Unsafe.assertNonNull(json);
   }

   public static <T> T fromPlainJson(@Nullable String json) throws IllegalArgumentException {
      Gson gson = (Gson)GSON.get();

      try {
         return gson.fromJson((String)Unsafe.nullableAsNonNull(json), Object.class);
      } catch (Exception var3) {
         throw new IllegalArgumentException("Could not deserialize object from Json!", var3);
      }
   }

   @Nullable
   public static <T> T fromJson(@Nullable String json) throws IllegalArgumentException {
      Gson gson = (Gson)GSON.get();
      return BukkitAwareObjectTypeAdapter.fromJson(gson, json);
   }

   private JsonUtils() {
   }
}
