package advancedplugins.pm2.cv.api.vehicle.configuration.data;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

public final class DataParsers {
   private static final Map<String, DataParser> BY_IDENTIFIER = new HashMap();
   private static final Map<Class<?>, DataParser> BY_TYPE = new HashMap();

   public static DataParser getParser(@NotNull String var0) {
      return (DataParser)BY_IDENTIFIER.get(var0.trim().toLowerCase());
   }

   public static DataParser getParser(@NotNull Class<?> var0) {
      Iterator var1 = BY_TYPE.entrySet().iterator();

      Entry var2;
      do {
         if (!var1.hasNext()) {
            return null;
         }

         var2 = (Entry)var1.next();
      } while(!((Class)var2.getKey()).isAssignableFrom(var0));

      return (DataParser)var2.getValue();
   }

   public static DataParser matchParser(@NotNull ConfigurationSection var0) {
      return DataParser.getParser(var0);
   }

   public static void register(@NotNull DataParser var0) {
      BY_IDENTIFIER.put(var0.getIdentifier().toLowerCase().trim(), var0);
      BY_TYPE.put(var0.getType(), var0);
   }

   static {
      register(new MaterialParser());
      register(new ItemStackParser());
      register(new BlockDataParser());
      register(new DustOptionsParser());
      register(new HeadTextureParser());
      register(new BannerStyleParser());
   }
}
