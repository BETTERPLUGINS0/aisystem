package fr.xephi.authme.libs.org.postgresql.ds;

import fr.xephi.authme.libs.org.postgresql.ds.common.BaseDataSource;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.sql.SQLException;
import javax.sql.ConnectionPoolDataSource;
import javax.sql.PooledConnection;

public class PGConnectionPoolDataSource extends BaseDataSource implements ConnectionPoolDataSource, Serializable {
   private boolean defaultAutoCommit = true;

   public String getDescription() {
      return "ConnectionPoolDataSource from PostgreSQL JDBC Driver 42.7.3";
   }

   public PooledConnection getPooledConnection() throws SQLException {
      return new PGPooledConnection(this.getConnection(), this.defaultAutoCommit);
   }

   public PooledConnection getPooledConnection(String user, String password) throws SQLException {
      return new PGPooledConnection(this.getConnection(user, password), this.defaultAutoCommit);
   }

   public boolean isDefaultAutoCommit() {
      return this.defaultAutoCommit;
   }

   public void setDefaultAutoCommit(boolean defaultAutoCommit) {
      this.defaultAutoCommit = defaultAutoCommit;
   }

   private void writeObject(ObjectOutputStream out) throws IOException {
      this.writeBaseObject(out);
      out.writeBoolean(this.defaultAutoCommit);
   }

   private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
      this.readBaseObject(in);
      this.defaultAutoCommit = in.readBoolean();
   }
}
