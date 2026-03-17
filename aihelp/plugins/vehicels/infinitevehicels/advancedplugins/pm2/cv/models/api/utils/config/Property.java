package advancedplugins.pm2.cv.models.api.utils.config;

import advancedplugins.pm2.cv.models.api.ModelAPI;
import advancedplugins.pm2.cv.models.api.model.rpc.entity.CullType;
import advancedplugins.pm2.cv.models.api.model.rpc.generator.BaseItemEnum;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public interface Property {
   String getPath();

   Object getDef();

   default int getInt() {
      return ModelAPI.getAPI().getConfigManager().getInt(this);
   }

   default double getDouble() {
      return ModelAPI.getAPI().getConfigManager().getDouble(this);
   }

   default String getString() {
      return ModelAPI.getAPI().getConfigManager().getString(this);
   }

   default boolean getBoolean() {
      return ModelAPI.getAPI().getConfigManager().getBoolean(this);
   }

   default List<String> getStringList() {
      return ModelAPI.getAPI().getConfigManager().getStringList(this);
   }

   default BaseItemEnum getBaseItem() {
      return BaseItemEnum.get(this.getString().toUpperCase(Locale.ENGLISH));
   }

   default Set<BaseItemEnum> getBaseItems() {
      HashSet<BaseItemEnum> set = new HashSet();
      Iterator var2 = this.getStringList().iterator();

      while(var2.hasNext()) {
         String s = (String)var2.next();
         BaseItemEnum item = BaseItemEnum.get(s.toUpperCase(Locale.ENGLISH));
         if (item != null) {
            set.add(item);
         }
      }

      return set;
   }

   default CullType getCullType() {
      return CullType.get(this.getString().toUpperCase(Locale.ENGLISH));
   }
}
