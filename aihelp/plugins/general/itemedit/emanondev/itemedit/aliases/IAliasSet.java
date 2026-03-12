package emanondev.itemedit.aliases;

import java.util.Collection;
import java.util.List;

public interface IAliasSet<T> {
   String getID();

   void reload();

   String getDefaultName(T var1);

   /** @deprecated */
   @Deprecated
   String getName(T var1);

   String getPathName(T var1);

   Collection<T> getValues();

   List<String> getAliases();

   T convertAlias(String var1);
}
