package emanondev.itemtag;

import com.google.gson.Gson;
import java.util.Arrays;
import java.util.List;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface TagItem {
   boolean hasBooleanTag(@NotNull String var1);

   boolean hasIntegerTag(@NotNull String var1);

   boolean hasDoubleTag(@NotNull String var1);

   boolean hasStringTag(@NotNull String var1);

   boolean hasStringListTag(@NotNull String var1);

   void removeTag(@NotNull String var1);

   void setTag(@NotNull String var1, boolean var2);

   void setTag(@NotNull String var1, @Nullable String var2);

   default void setTag(@NotNull String key, @Nullable List<String> value) {
      if (value == null) {
         this.removeTag(key);
      } else {
         this.setTag(key, String.join("%%;%%", value));
      }

   }

   void setTag(@NotNull String var1, int var2);

   void setTag(@NotNull String var1, double var2);

   @Nullable
   Boolean getBoolean(@NotNull String var1);

   @Nullable
   String getString(@NotNull String var1);

   @Nullable
   default List<String> getStringList(@NotNull String key) {
      String value = this.getString(key);
      return value == null ? null : Arrays.asList(value.split("%%;%%"));
   }

   @Nullable
   Integer getInteger(@NotNull String var1);

   @Nullable
   @Contract("_,!null->!null")
   default Integer getInteger(@NotNull String key, @Nullable Integer defaultValue) {
      Integer value = this.getInteger(key);
      return value == null ? defaultValue : value;
   }

   @Nullable
   Double getDouble(@NotNull String var1);

   @Nullable
   @Contract("_,!null->!null")
   default Boolean getBoolean(@NotNull String key, @Nullable Boolean defaultValue) {
      Boolean value = this.getBoolean(key);
      return value == null ? defaultValue : value;
   }

   @Nullable
   @Contract("_,!null->!null")
   default Double getDouble(@NotNull String key, @Nullable Double defaultValue) {
      Double value = this.getDouble(key);
      return value == null ? defaultValue : value;
   }

   @Nullable
   @Contract("_,!null->!null")
   default String getString(@NotNull String key, @Nullable String defaultValue) {
      String value = this.getString(key);
      return value == null ? defaultValue : value;
   }

   @Nullable
   @Contract("_,!null->!null")
   default List<String> getStringList(@NotNull String key, @Nullable List<String> defaultValue) {
      List<String> value = this.getStringList(key);
      return value == null ? defaultValue : value;
   }

   boolean isValid();

   ItemStack getItem();

   default boolean hasObject(@NotNull String key) {
      return this.hasStringTag(key);
   }

   default <T> T getObject(@NotNull String key, @NotNull Class<T> clazz) {
      if (!this.hasObject(key)) {
         return null;
      } else {
         String json = this.getString(key);
         return json != null && !json.isEmpty() ? (new Gson()).fromJson(json, clazz) : null;
      }
   }

   default void setObject(@NotNull String key, @Nullable Object object) {
      if (object == null) {
         this.removeTag(key);
      } else {
         this.setTag(key, (new Gson()).toJson(object));
      }

   }
}
