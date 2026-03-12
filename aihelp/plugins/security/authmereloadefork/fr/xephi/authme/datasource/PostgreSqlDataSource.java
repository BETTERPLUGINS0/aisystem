package fr.xephi.authme.datasource;

import fr.xephi.authme.ConsoleLogger;
import fr.xephi.authme.data.auth.PlayerAuth;
import fr.xephi.authme.datasource.columnshandler.AuthMeColumnsHandler;
import fr.xephi.authme.datasource.mysqlextensions.MySqlExtension;
import fr.xephi.authme.datasource.mysqlextensions.MySqlExtensionsFactory;
import fr.xephi.authme.libs.com.google.common.annotations.VisibleForTesting;
import fr.xephi.authme.libs.com.zaxxer.hikari.HikariDataSource;
import fr.xephi.authme.libs.com.zaxxer.hikari.pool.HikariPool;
import fr.xephi.authme.output.ConsoleLoggerFactory;
import fr.xephi.authme.settings.Settings;
import fr.xephi.authme.settings.properties.DatabaseSettings;
import fr.xephi.authme.settings.properties.HooksSettings;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class PostgreSqlDataSource extends AbstractSqlDataSource {
   private final ConsoleLogger logger = ConsoleLoggerFactory.get(PostgreSqlDataSource.class);
   private String host;
   private String port;
   private String username;
   private String password;
   private String database;
   private String tableName;
   private int poolSize;
   private int maxLifetime;
   private List<String> columnOthers;
   private Columns col;
   private MySqlExtension sqlExtension;
   private HikariDataSource ds;

   public PostgreSqlDataSource(Settings settings, MySqlExtensionsFactory extensionsFactory) throws SQLException {
      this.setParameters(settings, extensionsFactory);

      try {
         this.setConnectionArguments();
      } catch (RuntimeException var5) {
         if (var5 instanceof IllegalArgumentException) {
            this.logger.warning("Invalid database arguments! Please check your configuration!");
            this.logger.warning("If this error persists, please report it to the developer!");
         }

         if (var5 instanceof HikariPool.PoolInitializationException) {
            this.logger.warning("Can't initialize database connection! Please check your configuration!");
            this.logger.warning("If this error persists, please report it to the developer!");
         }

         this.logger.warning("Can't use the Hikari Connection Pool! Please, report this error to the developer!");
         throw var5;
      }

      try {
         this.checkTablesAndColumns();
      } catch (SQLException var4) {
         this.closeConnection();
         this.logger.logException("Can't initialize the PostgreSQL database:", var4);
         this.logger.warning("Please check your database settings in the config.yml file!");
         throw var4;
      }
   }

   @VisibleForTesting
   PostgreSqlDataSource(Settings settings, HikariDataSource hikariDataSource, MySqlExtensionsFactory extensionsFactory) {
      this.ds = hikariDataSource;
      this.setParameters(settings, extensionsFactory);
   }

   private void setParameters(Settings settings, MySqlExtensionsFactory extensionsFactory) {
      this.host = (String)settings.getProperty(DatabaseSettings.MYSQL_HOST);
      this.port = (String)settings.getProperty(DatabaseSettings.MYSQL_PORT);
      this.username = (String)settings.getProperty(DatabaseSettings.MYSQL_USERNAME);
      this.password = (String)settings.getProperty(DatabaseSettings.MYSQL_PASSWORD);
      this.database = (String)settings.getProperty(DatabaseSettings.MYSQL_DATABASE);
      this.tableName = (String)settings.getProperty(DatabaseSettings.MYSQL_TABLE);
      this.columnOthers = (List)settings.getProperty(HooksSettings.MYSQL_OTHER_USERNAME_COLS);
      this.col = new Columns(settings);
      this.columnsHandler = AuthMeColumnsHandler.createForMySql(this::getConnection, settings);
      this.sqlExtension = extensionsFactory.buildExtension(this.col);
      this.poolSize = (Integer)settings.getProperty(DatabaseSettings.MYSQL_POOL_SIZE);
      this.maxLifetime = (Integer)settings.getProperty(DatabaseSettings.MYSQL_CONNECTION_MAX_LIFETIME);
   }

   private void setConnectionArguments() {
      this.ds = new HikariDataSource();
      this.ds.setPoolName("AuthMePostgreSQLPool");
      this.ds.setMaximumPoolSize(this.poolSize);
      this.ds.setMaxLifetime((long)(this.maxLifetime * 1000));
      this.ds.setDriverClassName("fr.xephi.authme.libs.org.postgresql.Driver");
      this.ds.setJdbcUrl(this.getJdbcUrl(this.host, this.port, this.database));
      this.ds.setUsername(this.username);
      this.ds.setPassword(this.password);
      this.ds.addDataSourceProperty("reWriteBatchedInserts", "true");
      this.ds.addDataSourceProperty("cachePrepStmts", "true");
      this.ds.addDataSourceProperty("preparedStatementCacheQueries", "275");
      this.logger.info("Connection arguments loaded, Hikari ConnectionPool ready!");
   }

   public void reload() {
      if (this.ds != null) {
         this.ds.close();
      }

      this.setConnectionArguments();
      this.logger.info("Hikari ConnectionPool arguments reloaded!");
   }

   private Connection getConnection() throws SQLException {
      return this.ds.getConnection();
   }

   private void checkTablesAndColumns() throws SQLException {
      Connection con = this.getConnection();

      try {
         Statement st = con.createStatement();

         try {
            String sql = "CREATE TABLE IF NOT EXISTS " + this.tableName + " (" + this.col.ID + " BIGSERIAL,PRIMARY KEY (" + this.col.ID + "));";
            st.executeUpdate(sql);
            DatabaseMetaData md = con.getMetaData();
            if (this.isColumnMissing(md, this.col.NAME)) {
               st.executeUpdate("ALTER TABLE " + this.tableName + " ADD COLUMN " + this.col.NAME + " VARCHAR(255) NOT NULL UNIQUE;");
            }

            if (this.isColumnMissing(md, this.col.REAL_NAME)) {
               st.executeUpdate("ALTER TABLE " + this.tableName + " ADD COLUMN " + this.col.REAL_NAME + " VARCHAR(255) NOT NULL;");
            }

            if (this.isColumnMissing(md, this.col.PASSWORD)) {
               st.executeUpdate("ALTER TABLE " + this.tableName + " ADD COLUMN " + this.col.PASSWORD + " VARCHAR(255) NOT NULL;");
            }

            if (!this.col.SALT.isEmpty() && this.isColumnMissing(md, this.col.SALT)) {
               st.executeUpdate("ALTER TABLE " + this.tableName + " ADD COLUMN " + this.col.SALT + " VARCHAR(255);");
            }

            if (this.isColumnMissing(md, this.col.LAST_IP)) {
               st.executeUpdate("ALTER TABLE " + this.tableName + " ADD COLUMN " + this.col.LAST_IP + " VARCHAR(40);");
            } else {
               MySqlMigrater.migrateLastIpColumn(st, md, this.tableName, this.col);
            }

            if (this.isColumnMissing(md, this.col.LAST_LOGIN)) {
               st.executeUpdate("ALTER TABLE " + this.tableName + " ADD COLUMN " + this.col.LAST_LOGIN + " BIGINT;");
            } else {
               MySqlMigrater.migrateLastLoginColumn(st, md, this.tableName, this.col);
            }

            if (this.isColumnMissing(md, this.col.REGISTRATION_DATE)) {
               MySqlMigrater.addRegistrationDateColumn(st, this.tableName, this.col);
            }

            if (this.isColumnMissing(md, this.col.REGISTRATION_IP)) {
               st.executeUpdate("ALTER TABLE " + this.tableName + " ADD COLUMN " + this.col.REGISTRATION_IP + " VARCHAR(40);");
            }

            if (this.isColumnMissing(md, this.col.LASTLOC_X)) {
               st.executeUpdate("ALTER TABLE " + this.tableName + " ADD COLUMN " + this.col.LASTLOC_X + " DOUBLE PRECISION NOT NULL DEFAULT '0.0' , ADD " + this.col.LASTLOC_Y + " DOUBLE PRECISION NOT NULL DEFAULT '0.0' , ADD " + this.col.LASTLOC_Z + " DOUBLE PRECISION NOT NULL DEFAULT '0.0';");
            }

            if (this.isColumnMissing(md, this.col.LASTLOC_WORLD)) {
               st.executeUpdate("ALTER TABLE " + this.tableName + " ADD COLUMN " + this.col.LASTLOC_WORLD + " VARCHAR(255) NOT NULL DEFAULT 'world';");
            }

            if (this.isColumnMissing(md, this.col.LASTLOC_YAW)) {
               st.executeUpdate("ALTER TABLE " + this.tableName + " ADD COLUMN " + this.col.LASTLOC_YAW + " FLOAT;");
            }

            if (this.isColumnMissing(md, this.col.LASTLOC_PITCH)) {
               st.executeUpdate("ALTER TABLE " + this.tableName + " ADD COLUMN " + this.col.LASTLOC_PITCH + " FLOAT;");
            }

            if (this.isColumnMissing(md, this.col.EMAIL)) {
               st.executeUpdate("ALTER TABLE " + this.tableName + " ADD COLUMN " + this.col.EMAIL + " VARCHAR(255);");
            }

            if (this.isColumnMissing(md, this.col.IS_LOGGED)) {
               st.executeUpdate("ALTER TABLE " + this.tableName + " ADD COLUMN " + this.col.IS_LOGGED + " SMALLINT NOT NULL DEFAULT '0';");
            }

            if (this.isColumnMissing(md, this.col.HAS_SESSION)) {
               st.executeUpdate("ALTER TABLE " + this.tableName + " ADD COLUMN " + this.col.HAS_SESSION + " SMALLINT NOT NULL DEFAULT '0';");
            }

            if (this.isColumnMissing(md, this.col.TOTP_KEY)) {
               st.executeUpdate("ALTER TABLE " + this.tableName + " ADD COLUMN " + this.col.TOTP_KEY + " VARCHAR(32);");
            } else if (SqlDataSourceUtils.getColumnSize(md, this.tableName, this.col.TOTP_KEY) != 32) {
               st.executeUpdate("ALTER TABLE " + this.tableName + " ALTER COLUMN " + this.col.TOTP_KEY + " TYPE VARCHAR(32);");
            }

            if (!this.col.PLAYER_UUID.isEmpty() && this.isColumnMissing(md, this.col.PLAYER_UUID)) {
               st.executeUpdate("ALTER TABLE " + this.tableName + " ADD COLUMN " + this.col.PLAYER_UUID + " VARCHAR(36)");
            }
         } catch (Throwable var7) {
            if (st != null) {
               try {
                  st.close();
               } catch (Throwable var6) {
                  var7.addSuppressed(var6);
               }
            }

            throw var7;
         }

         if (st != null) {
            st.close();
         }
      } catch (Throwable var8) {
         if (con != null) {
            try {
               con.close();
            } catch (Throwable var5) {
               var8.addSuppressed(var5);
            }
         }

         throw var8;
      }

      if (con != null) {
         con.close();
      }

      this.logger.info("PostgreSQL setup finished");
   }

   private boolean isColumnMissing(DatabaseMetaData metaData, String columnName) throws SQLException {
      ResultSet rs = metaData.getColumns((String)null, (String)null, this.tableName, columnName.toLowerCase(Locale.ROOT));

      boolean var4;
      try {
         var4 = !rs.next();
      } catch (Throwable var7) {
         if (rs != null) {
            try {
               rs.close();
            } catch (Throwable var6) {
               var7.addSuppressed(var6);
            }
         }

         throw var7;
      }

      if (rs != null) {
         rs.close();
      }

      return var4;
   }

   public PlayerAuth getAuth(String user) {
      String sql = "SELECT * FROM " + this.tableName + " WHERE " + this.col.NAME + "=?;";

      try {
         Connection con = this.getConnection();

         PlayerAuth var8;
         label114: {
            try {
               PreparedStatement pst;
               label106: {
                  pst = con.prepareStatement(sql);

                  try {
                     pst.setString(1, user.toLowerCase(Locale.ROOT));
                     ResultSet rs = pst.executeQuery();

                     label89: {
                        try {
                           if (rs.next()) {
                              int id = rs.getInt(this.col.ID);
                              PlayerAuth auth = this.buildAuthFromResultSet(rs);
                              this.sqlExtension.extendAuth(auth, id, con);
                              var8 = auth;
                              break label89;
                           }
                        } catch (Throwable var12) {
                           if (rs != null) {
                              try {
                                 rs.close();
                              } catch (Throwable var11) {
                                 var12.addSuppressed(var11);
                              }
                           }

                           throw var12;
                        }

                        if (rs != null) {
                           rs.close();
                        }
                        break label106;
                     }

                     if (rs != null) {
                        rs.close();
                     }
                  } catch (Throwable var13) {
                     if (pst != null) {
                        try {
                           pst.close();
                        } catch (Throwable var10) {
                           var13.addSuppressed(var10);
                        }
                     }

                     throw var13;
                  }

                  if (pst != null) {
                     pst.close();
                  }
                  break label114;
               }

               if (pst != null) {
                  pst.close();
               }
            } catch (Throwable var14) {
               if (con != null) {
                  try {
                     con.close();
                  } catch (Throwable var9) {
                     var14.addSuppressed(var9);
                  }
               }

               throw var14;
            }

            if (con != null) {
               con.close();
            }

            return null;
         }

         if (con != null) {
            con.close();
         }

         return var8;
      } catch (SQLException var15) {
         SqlDataSourceUtils.logSqlException(var15);
         return null;
      }
   }

   public boolean saveAuth(PlayerAuth auth) {
      super.saveAuth(auth);

      try {
         Connection con = this.getConnection();

         boolean var13;
         try {
            if (!this.columnOthers.isEmpty()) {
               Iterator var3 = this.columnOthers.iterator();

               while(var3.hasNext()) {
                  String column = (String)var3.next();
                  PreparedStatement pst = con.prepareStatement("UPDATE " + this.tableName + " SET " + column + "=? WHERE " + this.col.NAME + "=?;");

                  try {
                     pst.setString(1, auth.getRealName());
                     pst.setString(2, auth.getNickname());
                     pst.executeUpdate();
                  } catch (Throwable var10) {
                     if (pst != null) {
                        try {
                           pst.close();
                        } catch (Throwable var9) {
                           var10.addSuppressed(var9);
                        }
                     }

                     throw var10;
                  }

                  if (pst != null) {
                     pst.close();
                  }
               }
            }

            this.sqlExtension.saveAuth(auth, con);
            var13 = true;
         } catch (Throwable var11) {
            if (con != null) {
               try {
                  con.close();
               } catch (Throwable var8) {
                  var11.addSuppressed(var8);
               }
            }

            throw var11;
         }

         if (con != null) {
            con.close();
         }

         return var13;
      } catch (SQLException var12) {
         SqlDataSourceUtils.logSqlException(var12);
         return false;
      }
   }

   String getJdbcUrl(String host, String port, String database) {
      return "jdbc:postgresql://" + host + ":" + port + "/" + database;
   }

   public Set<String> getRecordsToPurge(long until) {
      Set<String> list = new HashSet();
      String select = "SELECT " + this.col.NAME + " FROM " + this.tableName + " WHERE GREATEST( COALESCE(" + this.col.LAST_LOGIN + ", 0), COALESCE(" + this.col.REGISTRATION_DATE + ", 0)) < ?;";

      try {
         Connection con = this.getConnection();

         try {
            PreparedStatement selectPst = con.prepareStatement(select);

            try {
               selectPst.setLong(1, until);
               ResultSet rs = selectPst.executeQuery();

               try {
                  while(rs.next()) {
                     list.add(rs.getString(this.col.NAME));
                  }
               } catch (Throwable var13) {
                  if (rs != null) {
                     try {
                        rs.close();
                     } catch (Throwable var12) {
                        var13.addSuppressed(var12);
                     }
                  }

                  throw var13;
               }

               if (rs != null) {
                  rs.close();
               }
            } catch (Throwable var14) {
               if (selectPst != null) {
                  try {
                     selectPst.close();
                  } catch (Throwable var11) {
                     var14.addSuppressed(var11);
                  }
               }

               throw var14;
            }

            if (selectPst != null) {
               selectPst.close();
            }
         } catch (Throwable var15) {
            if (con != null) {
               try {
                  con.close();
               } catch (Throwable var10) {
                  var15.addSuppressed(var10);
               }
            }

            throw var15;
         }

         if (con != null) {
            con.close();
         }
      } catch (SQLException var16) {
         SqlDataSourceUtils.logSqlException(var16);
      }

      return list;
   }

   public boolean removeAuth(String user) {
      user = user.toLowerCase(Locale.ROOT);
      String sql = "DELETE FROM " + this.tableName + " WHERE " + this.col.NAME + "=?;";

      try {
         Connection con = this.getConnection();

         boolean var5;
         try {
            PreparedStatement pst = con.prepareStatement(sql);

            try {
               this.sqlExtension.removeAuth(user, con);
               pst.setString(1, user.toLowerCase(Locale.ROOT));
               pst.executeUpdate();
               var5 = true;
            } catch (Throwable var9) {
               if (pst != null) {
                  try {
                     pst.close();
                  } catch (Throwable var8) {
                     var9.addSuppressed(var8);
                  }
               }

               throw var9;
            }

            if (pst != null) {
               pst.close();
            }
         } catch (Throwable var10) {
            if (con != null) {
               try {
                  con.close();
               } catch (Throwable var7) {
                  var10.addSuppressed(var7);
               }
            }

            throw var10;
         }

         if (con != null) {
            con.close();
         }

         return var5;
      } catch (SQLException var11) {
         SqlDataSourceUtils.logSqlException(var11);
         return false;
      }
   }

   public void closeConnection() {
      if (this.ds != null && !this.ds.isClosed()) {
         this.ds.close();
      }

   }

   public void purgeRecords(Collection<String> toPurge) {
      String sql = "DELETE FROM " + this.tableName + " WHERE " + this.col.NAME + "=?;";

      try {
         Connection con = this.getConnection();

         try {
            PreparedStatement pst = con.prepareStatement(sql);

            try {
               Iterator var5 = toPurge.iterator();

               while(var5.hasNext()) {
                  String name = (String)var5.next();
                  pst.setString(1, name.toLowerCase(Locale.ROOT));
                  pst.executeUpdate();
               }
            } catch (Throwable var9) {
               if (pst != null) {
                  try {
                     pst.close();
                  } catch (Throwable var8) {
                     var9.addSuppressed(var8);
                  }
               }

               throw var9;
            }

            if (pst != null) {
               pst.close();
            }
         } catch (Throwable var10) {
            if (con != null) {
               try {
                  con.close();
               } catch (Throwable var7) {
                  var10.addSuppressed(var7);
               }
            }

            throw var10;
         }

         if (con != null) {
            con.close();
         }
      } catch (SQLException var11) {
         SqlDataSourceUtils.logSqlException(var11);
      }

   }

   public DataSourceType getType() {
      return DataSourceType.POSTGRESQL;
   }

   public List<PlayerAuth> getAllAuths() {
      ArrayList auths = new ArrayList();

      try {
         Connection con = this.getConnection();

         try {
            Statement st = con.createStatement();

            try {
               ResultSet rs = st.executeQuery("SELECT * FROM " + this.tableName);

               try {
                  while(rs.next()) {
                     PlayerAuth auth = this.buildAuthFromResultSet(rs);
                     this.sqlExtension.extendAuth(auth, rs.getInt(this.col.ID), con);
                     auths.add(auth);
                  }
               } catch (Throwable var10) {
                  if (rs != null) {
                     try {
                        rs.close();
                     } catch (Throwable var9) {
                        var10.addSuppressed(var9);
                     }
                  }

                  throw var10;
               }

               if (rs != null) {
                  rs.close();
               }
            } catch (Throwable var11) {
               if (st != null) {
                  try {
                     st.close();
                  } catch (Throwable var8) {
                     var11.addSuppressed(var8);
                  }
               }

               throw var11;
            }

            if (st != null) {
               st.close();
            }
         } catch (Throwable var12) {
            if (con != null) {
               try {
                  con.close();
               } catch (Throwable var7) {
                  var12.addSuppressed(var7);
               }
            }

            throw var12;
         }

         if (con != null) {
            con.close();
         }
      } catch (SQLException var13) {
         SqlDataSourceUtils.logSqlException(var13);
      }

      return auths;
   }

   public List<String> getLoggedPlayersWithEmptyMail() {
      List<String> players = new ArrayList();
      String sql = "SELECT " + this.col.REAL_NAME + " FROM " + this.tableName + " WHERE " + this.col.IS_LOGGED + " = 1 AND (" + this.col.EMAIL + " = 'your@email.com' OR " + this.col.EMAIL + " IS NULL);";

      try {
         Connection con = this.getConnection();

         try {
            Statement st = con.createStatement();

            try {
               ResultSet rs = st.executeQuery(sql);

               try {
                  while(rs.next()) {
                     players.add(rs.getString(1));
                  }
               } catch (Throwable var11) {
                  if (rs != null) {
                     try {
                        rs.close();
                     } catch (Throwable var10) {
                        var11.addSuppressed(var10);
                     }
                  }

                  throw var11;
               }

               if (rs != null) {
                  rs.close();
               }
            } catch (Throwable var12) {
               if (st != null) {
                  try {
                     st.close();
                  } catch (Throwable var9) {
                     var12.addSuppressed(var9);
                  }
               }

               throw var12;
            }

            if (st != null) {
               st.close();
            }
         } catch (Throwable var13) {
            if (con != null) {
               try {
                  con.close();
               } catch (Throwable var8) {
                  var13.addSuppressed(var8);
               }
            }

            throw var13;
         }

         if (con != null) {
            con.close();
         }
      } catch (SQLException var14) {
         SqlDataSourceUtils.logSqlException(var14);
      }

      return players;
   }

   public List<PlayerAuth> getRecentlyLoggedInPlayers() {
      List<PlayerAuth> players = new ArrayList();
      String sql = "SELECT * FROM " + this.tableName + " ORDER BY " + this.col.LAST_LOGIN + " DESC LIMIT 10;";

      try {
         Connection con = this.getConnection();

         try {
            Statement st = con.createStatement();

            try {
               ResultSet rs = st.executeQuery(sql);

               try {
                  while(rs.next()) {
                     players.add(this.buildAuthFromResultSet(rs));
                  }
               } catch (Throwable var11) {
                  if (rs != null) {
                     try {
                        rs.close();
                     } catch (Throwable var10) {
                        var11.addSuppressed(var10);
                     }
                  }

                  throw var11;
               }

               if (rs != null) {
                  rs.close();
               }
            } catch (Throwable var12) {
               if (st != null) {
                  try {
                     st.close();
                  } catch (Throwable var9) {
                     var12.addSuppressed(var9);
                  }
               }

               throw var12;
            }

            if (st != null) {
               st.close();
            }
         } catch (Throwable var13) {
            if (con != null) {
               try {
                  con.close();
               } catch (Throwable var8) {
                  var13.addSuppressed(var8);
               }
            }

            throw var13;
         }

         if (con != null) {
            con.close();
         }
      } catch (SQLException var14) {
         SqlDataSourceUtils.logSqlException(var14);
      }

      return players;
   }

   public boolean setTotpKey(String user, String totpKey) {
      String sql = "UPDATE " + this.tableName + " SET " + this.col.TOTP_KEY + " = ? WHERE " + this.col.NAME + " = ?";

      try {
         Connection con = this.getConnection();

         boolean var6;
         try {
            PreparedStatement pst = con.prepareStatement(sql);

            try {
               pst.setString(1, totpKey);
               pst.setString(2, user.toLowerCase(Locale.ROOT));
               pst.executeUpdate();
               var6 = true;
            } catch (Throwable var10) {
               if (pst != null) {
                  try {
                     pst.close();
                  } catch (Throwable var9) {
                     var10.addSuppressed(var9);
                  }
               }

               throw var10;
            }

            if (pst != null) {
               pst.close();
            }
         } catch (Throwable var11) {
            if (con != null) {
               try {
                  con.close();
               } catch (Throwable var8) {
                  var11.addSuppressed(var8);
               }
            }

            throw var11;
         }

         if (con != null) {
            con.close();
         }

         return var6;
      } catch (SQLException var12) {
         SqlDataSourceUtils.logSqlException(var12);
         return false;
      }
   }

   private PlayerAuth buildAuthFromResultSet(ResultSet row) throws SQLException {
      String salt = this.col.SALT.isEmpty() ? null : row.getString(this.col.SALT);
      int group = this.col.GROUP.isEmpty() ? -1 : row.getInt(this.col.GROUP);
      return PlayerAuth.builder().name(row.getString(this.col.NAME)).realName(row.getString(this.col.REAL_NAME)).password(row.getString(this.col.PASSWORD), salt).totpKey(row.getString(this.col.TOTP_KEY)).lastLogin(SqlDataSourceUtils.getNullableLong(row, this.col.LAST_LOGIN)).lastIp(row.getString(this.col.LAST_IP)).email(row.getString(this.col.EMAIL)).registrationDate(row.getLong(this.col.REGISTRATION_DATE)).registrationIp(row.getString(this.col.REGISTRATION_IP)).groupId(group).locWorld(row.getString(this.col.LASTLOC_WORLD)).locX(row.getDouble(this.col.LASTLOC_X)).locY(row.getDouble(this.col.LASTLOC_Y)).locZ(row.getDouble(this.col.LASTLOC_Z)).locYaw(row.getFloat(this.col.LASTLOC_YAW)).locPitch(row.getFloat(this.col.LASTLOC_PITCH)).build();
   }
}
