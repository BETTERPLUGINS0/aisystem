package advancedplugins.pm2.cv.models.api.utils.data.io;

import advancedplugins.pm2.cv.models.api.ModelAPI;
import advancedplugins.pm2.cv.models.api.utils.data.ItemUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Supplier;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

public class SavedData extends HashMap<String, Object> {
   public static final NamespacedKey DATA_KEY;

   public static SavedData parse(String var0) {
      return (SavedData)gson().fromJson(var0, SavedData.class);
   }

   private static Gson gson() {
      return ModelAPI.getAPI().getGson();
   }

   public void putBoolean(String var1, Boolean var2) {
      this.put(var1, var2);
   }

   public Boolean getBoolean(String var1, Boolean var2) {
      String var3 = this.getAsString(var1);
      return var3 == null ? var2 : Boolean.parseBoolean(var3);
   }

   public Boolean getBoolean(String var1) {
      return this.getBoolean(var1, false);
   }

   public void putByte(String var1, Byte var2) {
      this.put(var1, var2);
   }

   public Byte getByte(String var1, Byte var2) {
      try {
         String var3 = this.getAsString(var1);
         return var3 == null ? var2 : Byte.parseByte(var3);
      } catch (NumberFormatException var4) {
         this.wrapException(var1, var4);
         return var2;
      }
   }

   public Byte getByte(String var1) {
      return this.getByte(var1, (byte)0);
   }

   public void putInt(String var1, Integer var2) {
      this.put(var1, var2);
   }

   public Integer getInt(String var1, Integer var2) {
      try {
         String var3 = this.getAsString(var1);
         return var3 == null ? var2 : (int)Double.parseDouble(var3);
      } catch (NumberFormatException var4) {
         this.wrapException(var1, var4);
         return var2;
      }
   }

   public Integer getInt(String var1) {
      return this.getInt(var1, 0);
   }

   public void putFloat(String var1, Float var2) {
      this.put(var1, var2);
   }

   public Float getFloat(String var1, Float var2) {
      try {
         String var3 = this.getAsString(var1);
         return var3 == null ? var2 : Float.parseFloat(var3);
      } catch (NumberFormatException var4) {
         this.wrapException(var1, var4);
         return var2;
      }
   }

   public Float getFloat(String var1) {
      return this.getFloat(var1, 0.0F);
   }

   public void putDouble(String var1, Double var2) {
      this.put(var1, var2);
   }

   public Double getDouble(String var1, Double var2) {
      try {
         String var3 = this.getAsString(var1);
         return var3 == null ? var2 : Double.parseDouble(var3);
      } catch (NumberFormatException var4) {
         this.wrapException(var1, var4);
         return var2;
      }
   }

   public Double getDouble(String var1) {
      return this.getDouble(var1, 0.0D);
   }

   public void putString(String var1, String var2) {
      this.put(var1, var2);
   }

   public String getString(String var1, String var2) {
      return this.getOrDefaultAsString(var1, var2);
   }

   public String getString(String var1) {
      return this.getString(var1, (String)null);
   }

   public void putUUID(String var1, UUID var2) {
      this.put(var1, var2);
   }

   public UUID getUUID(String var1, UUID var2) {
      String var3 = this.getAsString(var1);
      return var3 == null ? var2 : UUID.fromString(var3);
   }

   public UUID getUUID(String var1) {
      return this.getUUID(var1, (UUID)null);
   }

   public void putList(String var1, Collection<?> var2) {
      this.put(var1, var2);
   }

   public <T> List<T> getList(String var1) {
      String var2 = this.getAsString(var1);
      return var2 == null ? List.of() : (List)gson().fromJson(var2, (new TypeToken<Object>(this) {
      }).getType());
   }

   public <T> List<T> getList(String var1, Class<T> var2) {
      String var3 = gson().toJson(this.get(var1));
      return var3 == null ? List.of() : (List)gson().fromJson(var3, TypeToken.getParameterized(List.class, new Type[]{var2}));
   }

   public void putData(String var1, SavedData var2) {
      if (this == var2) {
         throw new RuntimeException("Cannot add data: Attempting to add self to self.");
      } else {
         this.put(var1, var2);
      }
   }

   public Optional<SavedData> getData(String var1) {
      Object var2 = this.get(var1);
      if (var2 instanceof Map) {
         SavedData var3 = new SavedData();
         var3.putAll((Map)var2);
         return Optional.of(var3);
      } else {
         return Optional.empty();
      }
   }

   public void putItemStack(String var1, @Nullable ItemStack var2) {
      if (var2 != null) {
         this.putString(var1, ItemUtils.encodeItemStackToString(var2));
      }

   }

   public ItemStack getItemStack(String var1) {
      return this.getItemStack(var1, (ItemStack)null);
   }

   public ItemStack getItemStack(String var1, ItemStack var2) {
      String var3 = this.getAsString(var1);
      if (var3 == null) {
         return var2;
      } else {
         try {
            return ItemUtils.decodeItemStack(var3);
         } catch (Throwable var5) {
            var5.printStackTrace();
            return var2;
         }
      }
   }

   public <T> void saveIfExist(String var1, Supplier<T> var2, SavedData.DataSaver<T> var3) {
      Object var4 = var2.get();
      if (var4 != null) {
         var3.save(this, var1, var4);
      }

   }

   public <T> void loadIfExist(String var1, SavedData.DataLoader<T> var2, Consumer<T> var3) {
      Object var4 = var2.load(this, var1);
      if (var4 != null) {
         var3.accept(var4);
      }

   }

   public String toString() {
      return gson().toJson(this);
   }

   private void wrapException(String var1, Exception var2) {
      throw new RuntimeException("An error occurred while reading the value of " + var1, var2);
   }

   private String getAsString(String var1) {
      return this.getOrDefaultAsString(var1, (String)null);
   }

   private String getOrDefaultAsString(String var1, String var2) {
      Object var3 = this.get(var1);
      return var3 == null ? var2 : var3.toString();
   }

   static {
      DATA_KEY = new NamespacedKey(ModelAPI.PLUGIN, "model_data");
   }

   @FunctionalInterface
   public interface DataSaver<S> {
      void save(SavedData var1, String var2, S var3);
   }

   @FunctionalInterface
   public interface DataLoader<S> {
      S load(SavedData var1, String var2);
   }
}
