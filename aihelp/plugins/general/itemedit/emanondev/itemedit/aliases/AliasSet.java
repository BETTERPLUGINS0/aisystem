package emanondev.itemedit.aliases;

import emanondev.itemedit.ItemEdit;
import emanondev.itemedit.YMLConfig;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import org.jetbrains.annotations.Nullable;

public abstract class AliasSet<T> implements IAliasSet<T> {
   private static final YMLConfig config = ItemEdit.get().getConfig("aliases.yml");
   private final String path;
   private final HashMap<String, T> map = new HashMap();

   public AliasSet(String path) {
      this.path = path.toLowerCase(Locale.ENGLISH);
   }

   public String getID() {
      return this.path;
   }

   public void reload() {
      this.map.clear();
      Iterator var1 = this.getValues().iterator();

      while(true) {
         while(var1.hasNext()) {
            T value = var1.next();
            String path = this.path + "." + this.getPathName(value);
            String val = config.getString(path, (String)null);
            boolean ok = true;
            if (val == null || val.isEmpty()) {
               val = this.getDefaultName(value);
               ok = false;
            }

            if (val == null) {
               (new NullPointerException(value == null ? "null value" : value + " of " + value.getClass().getSimpleName() + " has null name")).printStackTrace();
            } else {
               if (val.contains(" ") || !val.equals(val.toLowerCase(Locale.ENGLISH))) {
                  val = val.toLowerCase(Locale.ENGLISH).replace(" ", "_");
                  ok = false;
               }

               if (this.map.containsKey(val)) {
                  int index;
                  for(index = 1; this.map.containsKey(val + index); ++index) {
                  }

                  val = val + index;
                  ok = false;
               }

               this.map.put(val, value);
               if (!ok) {
                  config.set(path, val);
                  config.save();
               }
            }
         }

         return;
      }
   }

   public String getDefaultName(T value) {
      return this.getPathName(value);
   }

   /** @deprecated */
   @Deprecated
   public abstract String getName(T var1);

   public String getPathName(T value) {
      return this.getName(value);
   }

   public abstract Collection<T> getValues();

   protected void set(String alias, T obj) {
      if (obj != null && alias != null) {
         if (alias.isEmpty()) {
            throw new IllegalArgumentException();
         } else {
            alias = alias.replace(" ", "_").toLowerCase(Locale.ENGLISH);
            String path = this.path + "." + this.getPathName(obj);
            if (!alias.equals(config.get(path))) {
               if (this.map.containsKey(alias)) {
                  throw new IllegalArgumentException("Alias " + alias + " is already used, check aliases.yml avoid using the same alias for different things");
               } else {
                  this.map.remove(config.get(path));
                  this.map.put(alias, obj);
                  config.set(path, alias);
                  config.save();
               }
            }
         }
      } else {
         throw new NullPointerException();
      }
   }

   public List<String> getAliases() {
      return new ArrayList(this.map.keySet());
   }

   @Nullable
   public T convertAlias(String alias) {
      return this.map.get(alias.toLowerCase(Locale.ENGLISH));
   }
}
