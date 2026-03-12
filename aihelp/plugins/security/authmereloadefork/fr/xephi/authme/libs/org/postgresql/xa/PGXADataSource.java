package fr.xephi.authme.libs.org.postgresql.xa;

import fr.xephi.authme.libs.org.postgresql.core.BaseConnection;
import fr.xephi.authme.libs.org.postgresql.ds.common.BaseDataSource;
import java.sql.Connection;
import java.sql.SQLException;
import javax.naming.Reference;
import javax.sql.XAConnection;
import javax.sql.XADataSource;
import org.checkerframework.checker.nullness.qual.Nullable;

public class PGXADataSource extends BaseDataSource implements XADataSource {
   public XAConnection getXAConnection() throws SQLException {
      return this.getXAConnection(this.getUser(), this.getPassword());
   }

   public XAConnection getXAConnection(@Nullable String user, @Nullable String password) throws SQLException {
      Connection con = super.getConnection(user, password);
      return new PGXAConnection((BaseConnection)con);
   }

   public String getDescription() {
      return "XA-enabled DataSource from PostgreSQL JDBC Driver 42.7.3";
   }

   protected Reference createReference() {
      return new Reference(this.getClass().getName(), PGXADataSourceFactory.class.getName(), (String)null);
   }
}
