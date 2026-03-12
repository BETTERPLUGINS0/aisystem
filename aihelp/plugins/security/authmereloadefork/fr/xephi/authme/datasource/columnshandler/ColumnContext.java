package fr.xephi.authme.datasource.columnshandler;

import fr.xephi.authme.settings.Settings;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ColumnContext {
   private final Settings settings;
   private final Map<DataSourceColumn<?>, String> columnNames = new ConcurrentHashMap();
   private final boolean hasDefaultSupport;

   public ColumnContext(Settings settings, boolean hasDefaultSupport) {
      this.settings = settings;
      this.hasDefaultSupport = hasDefaultSupport;
   }

   public String getName(DataSourceColumn<?> column) {
      return (String)this.columnNames.computeIfAbsent(column, (k) -> {
         return (String)this.settings.getProperty(k.getNameProperty());
      });
   }

   public boolean hasDefaultSupport() {
      return this.hasDefaultSupport;
   }
}
