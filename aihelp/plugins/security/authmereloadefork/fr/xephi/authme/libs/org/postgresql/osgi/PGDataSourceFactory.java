package fr.xephi.authme.libs.org.postgresql.osgi;

import fr.xephi.authme.libs.org.postgresql.ds.common.BaseDataSource;
import fr.xephi.authme.libs.org.postgresql.jdbc2.optional.ConnectionPool;
import fr.xephi.authme.libs.org.postgresql.jdbc2.optional.PoolingDataSource;
import fr.xephi.authme.libs.org.postgresql.jdbc2.optional.SimpleDataSource;
import fr.xephi.authme.libs.org.postgresql.util.GT;
import fr.xephi.authme.libs.org.postgresql.util.PSQLException;
import fr.xephi.authme.libs.org.postgresql.util.PSQLState;
import fr.xephi.authme.libs.org.postgresql.util.internal.Nullness;
import fr.xephi.authme.libs.org.postgresql.xa.PGXADataSource;
import java.sql.Driver;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Properties;
import java.util.Map.Entry;
import javax.sql.ConnectionPoolDataSource;
import javax.sql.DataSource;
import javax.sql.XADataSource;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.osgi.service.jdbc.DataSourceFactory;

public class PGDataSourceFactory implements DataSourceFactory {
   private void configureBaseDataSource(BaseDataSource ds, Properties props) throws SQLException {
      if (props.containsKey("url")) {
         ds.setUrl((String)Nullness.castNonNull(props.getProperty("url")));
      }

      if (props.containsKey("serverName")) {
         ds.setServerName((String)Nullness.castNonNull(props.getProperty("serverName")));
      }

      if (props.containsKey("portNumber")) {
         ds.setPortNumber(Integer.parseInt((String)Nullness.castNonNull(props.getProperty("portNumber"))));
      }

      if (props.containsKey("databaseName")) {
         ds.setDatabaseName(props.getProperty("databaseName"));
      }

      if (props.containsKey("user")) {
         ds.setUser(props.getProperty("user"));
      }

      if (props.containsKey("password")) {
         ds.setPassword(props.getProperty("password"));
      }

      Iterator var3 = props.entrySet().iterator();

      while(var3.hasNext()) {
         Entry<Object, Object> entry = (Entry)var3.next();
         ds.setProperty((String)entry.getKey(), (String)entry.getValue());
      }

   }

   public Driver createDriver(Properties props) throws SQLException {
      if (props != null && !props.isEmpty()) {
         throw new PSQLException(GT.tr("Unsupported properties: {0}", props.stringPropertyNames()), PSQLState.INVALID_PARAMETER_VALUE);
      } else {
         return new fr.xephi.authme.libs.org.postgresql.Driver();
      }
   }

   private DataSource createPoolingDataSource(Properties props) throws SQLException {
      PoolingDataSource dataSource = new PoolingDataSource();
      String maxPoolSize;
      if (props.containsKey("initialPoolSize")) {
         maxPoolSize = (String)Nullness.castNonNull(props.getProperty("initialPoolSize"));
         dataSource.setInitialConnections(Integer.parseInt(maxPoolSize));
      }

      if (props.containsKey("maxPoolSize")) {
         maxPoolSize = (String)Nullness.castNonNull(props.getProperty("maxPoolSize"));
         dataSource.setMaxConnections(Integer.parseInt(maxPoolSize));
      }

      if (props.containsKey("dataSourceName")) {
         dataSource.setDataSourceName((String)Nullness.castNonNull(props.getProperty("dataSourceName")));
      }

      this.configureBaseDataSource(dataSource, props);
      return dataSource;
   }

   private DataSource createSimpleDataSource(Properties props) throws SQLException {
      SimpleDataSource dataSource = new SimpleDataSource();
      this.configureBaseDataSource(dataSource, props);
      return dataSource;
   }

   public DataSource createDataSource(Properties props) throws SQLException {
      Properties props = new PGDataSourceFactory.SingleUseProperties(props);
      return !props.containsKey("initialPoolSize") && !props.containsKey("minPoolSize") && !props.containsKey("maxPoolSize") && !props.containsKey("maxIdleTime") && !props.containsKey("maxStatements") ? this.createSimpleDataSource(props) : this.createPoolingDataSource(props);
   }

   public ConnectionPoolDataSource createConnectionPoolDataSource(Properties props) throws SQLException {
      Properties props = new PGDataSourceFactory.SingleUseProperties(props);
      ConnectionPool dataSource = new ConnectionPool();
      this.configureBaseDataSource(dataSource, props);
      return dataSource;
   }

   public XADataSource createXADataSource(Properties props) throws SQLException {
      Properties props = new PGDataSourceFactory.SingleUseProperties(props);
      PGXADataSource dataSource = new PGXADataSource();
      this.configureBaseDataSource(dataSource, props);
      return dataSource;
   }

   private static class SingleUseProperties extends Properties {
      private static final long serialVersionUID = 1L;

      SingleUseProperties(Properties initialProperties) {
         if (initialProperties != null) {
            this.putAll(initialProperties);
         }

      }

      @Nullable
      public String getProperty(String key) {
         String value = super.getProperty(key);
         this.remove(key);
         return value;
      }
   }
}
