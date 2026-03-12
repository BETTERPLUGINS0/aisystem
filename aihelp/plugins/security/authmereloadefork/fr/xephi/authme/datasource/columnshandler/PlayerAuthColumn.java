package fr.xephi.authme.datasource.columnshandler;

import fr.xephi.authme.data.auth.PlayerAuth;
import fr.xephi.authme.libs.ch.jalu.configme.properties.Property;
import fr.xephi.authme.libs.ch.jalu.datasourcecolumns.ColumnType;
import fr.xephi.authme.libs.ch.jalu.datasourcecolumns.DependentColumn;
import java.util.function.Function;

public class PlayerAuthColumn<T> extends DataSourceColumn<T> implements DependentColumn<T, ColumnContext, PlayerAuth> {
   private final Function<PlayerAuth, T> playerAuthGetter;

   PlayerAuthColumn(ColumnType<T> type, Property<String> nameProperty, boolean isOptional, boolean useDefaultForNull, Function<PlayerAuth, T> playerAuthGetter) {
      super(type, nameProperty, isOptional, useDefaultForNull);
      this.playerAuthGetter = playerAuthGetter;
   }

   public T getValueFromDependent(PlayerAuth auth) {
      return this.playerAuthGetter.apply(auth);
   }
}
