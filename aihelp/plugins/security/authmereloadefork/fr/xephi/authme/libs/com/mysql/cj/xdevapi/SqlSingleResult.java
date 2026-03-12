package fr.xephi.authme.libs.com.mysql.cj.xdevapi;

import fr.xephi.authme.libs.com.mysql.cj.conf.PropertySet;
import fr.xephi.authme.libs.com.mysql.cj.protocol.ColumnDefinition;
import fr.xephi.authme.libs.com.mysql.cj.protocol.ProtocolEntity;
import fr.xephi.authme.libs.com.mysql.cj.result.RowList;
import java.util.TimeZone;
import java.util.function.Supplier;

public class SqlSingleResult extends RowResultImpl implements SqlResult {
   public SqlSingleResult(ColumnDefinition metadata, TimeZone defaultTimeZone, RowList rows, Supplier<ProtocolEntity> completer, PropertySet pset) {
      super(metadata, defaultTimeZone, rows, completer, pset);
   }
}
