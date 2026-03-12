package ac.grim.grimac.api.config;

import ac.grim.grimac.api.common.BasicReloadable;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import java.util.List;
import java.util.Map;

public interface ConfigManager extends BasicReloadable {
   String getStringElse(String var1, String var2);

   @Nullable
   String getString(String var1);

   @Nullable
   List<String> getStringList(String var1);

   List<String> getStringListElse(String var1, List<String> var2);

   int getIntElse(String var1, int var2);

   long getLongElse(String var1, long var2);

   double getDoubleElse(String var1, double var2);

   boolean getBooleanElse(String var1, boolean var2);

   @Nullable
   <T> T get(String var1);

   @Nullable
   <T> T getElse(String var1, T var2);

   @Nullable
   <K, V> Map<K, V> getMap(String var1);

   @Nullable
   <K, V> Map<K, V> getMapElse(String var1, Map<K, V> var2);

   @Nullable
   <T> List<T> getList(String var1);

   @Nullable
   <T> List<T> getListElse(String var1, List<T> var2);

   boolean hasLoaded();
}
