package fr.xephi.authme.libs.ch.jalu.configme.resource;

import java.util.List;
import java.util.Set;
import javax.annotation.Nullable;

public interface PropertyReader {
   boolean contains(String var1);

   Set<String> getKeys(boolean var1);

   Set<String> getChildKeys(String var1);

   @Nullable
   Object getObject(String var1);

   @Nullable
   String getString(String var1);

   @Nullable
   Integer getInt(String var1);

   @Nullable
   Double getDouble(String var1);

   @Nullable
   Boolean getBoolean(String var1);

   @Nullable
   List<?> getList(String var1);
}
