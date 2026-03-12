package fr.xephi.authme.datasource.columnshandler;

import fr.xephi.authme.libs.ch.jalu.configme.properties.Property;
import fr.xephi.authme.libs.ch.jalu.datasourcecolumns.Column;
import fr.xephi.authme.libs.ch.jalu.datasourcecolumns.ColumnType;

public class DataSourceColumn<T> implements Column<T, ColumnContext> {
   private final ColumnType<T> columnType;
   private final Property<String> nameProperty;
   private final boolean isOptional;
   private final boolean useDefaultForNull;

   DataSourceColumn(ColumnType<T> type, Property<String> nameProperty, boolean isOptional, boolean useDefaultForNull) {
      this.columnType = type;
      this.nameProperty = nameProperty;
      this.isOptional = isOptional;
      this.useDefaultForNull = useDefaultForNull;
   }

   public Property<String> getNameProperty() {
      return this.nameProperty;
   }

   public String resolveName(ColumnContext columnContext) {
      return columnContext.getName(this);
   }

   public ColumnType<T> getType() {
      return this.columnType;
   }

   public boolean isColumnUsed(ColumnContext columnContext) {
      return !this.isOptional || !this.resolveName(columnContext).isEmpty();
   }

   public boolean useDefaultForNullValue(ColumnContext columnContext) {
      return this.useDefaultForNull && columnContext.hasDefaultSupport();
   }
}
