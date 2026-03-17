package advancedplugins.pm2.cv.models.core.data;

import advancedplugins.pm2.cv.models.api.ModelAPI;
import advancedplugins.pm2.cv.models.api.utils.data.io.SavedData;
import com.google.gson.Gson;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import org.bukkit.Location;
import org.jetbrains.annotations.Nullable;

public class DataUpdater {
   @Nullable
   public static SavedData convertToSavedData(@Nullable Location var0, String var1) {
      if (var1 != null && !var1.isBlank()) {
         Gson var2 = ModelAPI.getAPI().getGson();
         return (SavedData)var2.fromJson(var1, SavedData.class);
      } else {
         return null;
      }
   }

   public static boolean tryUpdate(@Nullable SavedData var0) {
      if (var0 == null) {
         return false;
      } else {
         Consumer var1 = (var0x) -> {
            List var1 = var0x.getList("models", SavedData.class);
            Iterator var2 = var1.iterator();

            while(var2.hasNext()) {
               SavedData var3 = (SavedData)var2.next();
               var3.putBoolean("hitbox_visible", true);
               var3.putBoolean("shadow_visible", true);
               var3.putBoolean("main_hitbox", true);
            }

            var0x.putList("models", var1);
         };
         var1.accept(var0);
         return true;
      }
   }
}
