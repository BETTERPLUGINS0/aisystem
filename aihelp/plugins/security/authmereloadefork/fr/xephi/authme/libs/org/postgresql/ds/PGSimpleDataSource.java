package fr.xephi.authme.libs.org.postgresql.ds;

import fr.xephi.authme.libs.org.postgresql.ds.common.BaseDataSource;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.sql.SQLException;
import javax.sql.DataSource;

public class PGSimpleDataSource extends BaseDataSource implements DataSource, Serializable {
   public String getDescription() {
      return "Non-Pooling DataSource from PostgreSQL JDBC Driver 42.7.3";
   }

   private void writeObject(ObjectOutputStream out) throws IOException {
      this.writeBaseObject(out);
   }

   private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
      this.readBaseObject(in);
   }

   public boolean isWrapperFor(Class<?> iface) throws SQLException {
      return iface.isAssignableFrom(this.getClass());
   }

   public <T> T unwrap(Class<T> iface) throws SQLException {
      if (iface.isAssignableFrom(this.getClass())) {
         return iface.cast(this);
      } else {
         throw new SQLException("Cannot unwrap to " + iface.getName());
      }
   }
}
