package me.SuperRonanCraft.BetterRTP.references.database;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import lombok.NonNull;
import me.SuperRonanCraft.BetterRTP.BetterRTP;
import me.SuperRonanCraft.BetterRTP.versions.AsyncHandler;

public abstract class SQLite {
   private static final String db_file_name = "database";
   List<String> tables;
   private boolean loaded;
   public String addMissingColumns = "ALTER TABLE %table% ADD COLUMN %column% %type%";
   private final SQLite.DATABASE_TYPE type;

   public SQLite(SQLite.DATABASE_TYPE type) {
      this.type = type;
   }

   public abstract List<String> getTables();

   public Connection getSQLConnection() {
      return this.getLocal();
   }

   private Connection getLocal() {
      File dataFolder = new File(BetterRTP.getInstance().getDataFolder().getPath() + File.separator + "data", "database.db");
      if (!dataFolder.exists()) {
         try {
            dataFolder.getParentFile().mkdir();
            dataFolder.createNewFile();
         } catch (IOException var5) {
            BetterRTP.getInstance().getLogger().log(Level.SEVERE, "File write error: " + dataFolder.getPath());
            var5.printStackTrace();
         }
      }

      try {
         Class.forName("org.sqlite.JDBC");
         return DriverManager.getConnection("jdbc:sqlite:" + dataFolder);
      } catch (SQLException var3) {
         BetterRTP.getInstance().getLogger().log(Level.SEVERE, "SQLite exception on initialize", var3);
      } catch (ClassNotFoundException var4) {
         BetterRTP.getInstance().getLogger().log(Level.SEVERE, "You need the SQLite JBDC library. Google it Ronan...");
      }

      return null;
   }

   public void load() {
      this.loaded = false;
      this.tables = this.getTables();
      if (this.tables.isEmpty()) {
         this.loaded = true;
      } else {
         AsyncHandler.async(() -> {
            Connection connection = this.getSQLConnection();

            try {
               Statement s = connection.createStatement();
               Iterator var3 = this.tables.iterator();

               while(var3.hasNext()) {
                  String table = (String)var3.next();
                  s.executeUpdate(this.getCreateTable(table));
                  Enum[] var5 = this.getColumns(this.type);
                  int var6 = var5.length;

                  for(int var7 = 0; var7 < var6; ++var7) {
                     Enum c = var5[var7];

                     try {
                        String _name = this.getColumnName(this.type, c);
                        String _type = this.getColumnType(this.type, c);
                        s.executeUpdate(this.addMissingColumns.replace("%table%", table).replace("%column%", _name).replace("%type%", _type));
                     } catch (SQLException var20) {
                     }
                  }

                  BetterRTP.debug("Database " + this.type.name() + ":" + table + " configured and loaded!");
               }

               s.close();
            } catch (SQLException var21) {
               var21.printStackTrace();
            } finally {
               if (connection != null) {
                  try {
                     connection.close();
                  } catch (SQLException var19) {
                     var19.printStackTrace();
                  }
               }

            }

            this.initialize();
            this.loaded = true;
         });
      }
   }

   private String getCreateTable(String table) {
      String str = "CREATE TABLE IF NOT EXISTS `" + table + "` (";
      Enum<?>[] columns = this.getColumns(this.type);
      Enum[] var4 = columns;
      int var5 = columns.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         Enum<?> c = var4[var6];
         String _name = this.getColumnName(this.type, c);
         String _type = this.getColumnType(this.type, c);
         str = str.concat("`" + _name + "` " + _type);
         if (c.equals(columns[columns.length - 1])) {
            str = str.concat(")");
         } else {
            str = str.concat(", ");
         }
      }

      return str;
   }

   private Enum<?>[] getColumns(SQLite.DATABASE_TYPE type) {
      switch(type) {
      case CHUNK_DATA:
         return DatabaseChunkData.COLUMNS.values();
      case PLAYERS:
         return DatabasePlayers.COLUMNS.values();
      case QUEUE:
         return DatabaseQueue.COLUMNS.values();
      case COOLDOWN:
      default:
         return DatabaseCooldowns.COLUMNS.values();
      }
   }

   private String getColumnName(SQLite.DATABASE_TYPE type, Enum<?> column) {
      switch(type) {
      case CHUNK_DATA:
         return ((DatabaseChunkData.COLUMNS)column).name;
      case PLAYERS:
         return ((DatabasePlayers.COLUMNS)column).name;
      case QUEUE:
         return ((DatabaseQueue.COLUMNS)column).name;
      case COOLDOWN:
      default:
         return ((DatabaseCooldowns.COLUMNS)column).name;
      }
   }

   private String getColumnType(SQLite.DATABASE_TYPE type, Enum<?> column) {
      switch(type) {
      case CHUNK_DATA:
         return ((DatabaseChunkData.COLUMNS)column).type;
      case PLAYERS:
         return ((DatabasePlayers.COLUMNS)column).type;
      case QUEUE:
         return ((DatabaseQueue.COLUMNS)column).type;
      case COOLDOWN:
      default:
         return ((DatabaseCooldowns.COLUMNS)column).type;
      }
   }

   protected boolean sqlUpdate(String statement, @NonNull List<Object> params) {
      if (params == null) {
         throw new NullPointerException("params is marked non-null but is null");
      } else {
         Connection conn = null;
         PreparedStatement ps = null;
         boolean success = true;

         try {
            conn = this.getSQLConnection();
            ps = conn.prepareStatement(statement);
            Iterator<Object> it = params.iterator();

            for(int paramIndex = 1; it.hasNext(); ++paramIndex) {
               ps.setObject(paramIndex, it.next());
            }

            ps.executeUpdate();
         } catch (SQLException var11) {
            BetterRTP.getInstance().getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), var11);
            success = false;
         } finally {
            this.close(ps, (ResultSet)null, conn);
         }

         return success;
      }
   }

   boolean sqlUpdate(List<String> statement1, List<List<Object>> params1) {
      Connection conn = null;
      PreparedStatement ps = null;
      boolean success = true;

      try {
         conn = this.getSQLConnection();

         for(int i = 0; i < statement1.size(); ++i) {
            String statement = (String)statement1.get(i);
            List<Object> params = (List)params1.get(i);
            if (ps == null) {
               ps = conn.prepareStatement(statement);
            } else {
               ps.addBatch(statement);
            }

            if (params != null) {
               Iterator<Object> it = params.iterator();

               for(int paramIndex = 1; it.hasNext(); ++paramIndex) {
                  ps.setObject(paramIndex, it.next());
               }
            }
         }

         assert ps != null;

         ps.executeUpdate();
         ps.close();
      } catch (SQLException var14) {
         BetterRTP.getInstance().getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), var14);
         success = false;
         var14.printStackTrace();
      } finally {
         this.close(ps, (ResultSet)null, conn);
      }

      return success;
   }

   public void initialize() {
      Connection conn = null;
      PreparedStatement ps = null;
      ResultSet rs = null;

      try {
         conn = this.getSQLConnection();
         ps = conn.prepareStatement("SELECT * FROM " + (String)this.tables.get(0) + " WHERE " + this.getColumnName(this.type, this.getColumns(this.type)[0]) + " = 0");
         rs = ps.executeQuery();
      } catch (SQLException var8) {
         BetterRTP.getInstance().getLogger().log(Level.SEVERE, "Unable to retrieve connection", var8);
      } finally {
         this.close(ps, rs, conn);
      }

   }

   protected void close(PreparedStatement ps, ResultSet rs, Connection conn) {
      try {
         if (ps != null) {
            ps.close();
         }

         if (conn != null) {
            conn.close();
         }

         if (rs != null) {
            rs.close();
         }
      } catch (SQLException var5) {
         Error.close(BetterRTP.getInstance(), var5);
      }

   }

   public boolean isLoaded() {
      return this.loaded;
   }

   public static enum DATABASE_TYPE {
      PLAYERS,
      COOLDOWN,
      QUEUE,
      CHUNK_DATA;

      // $FF: synthetic method
      private static SQLite.DATABASE_TYPE[] $values() {
         return new SQLite.DATABASE_TYPE[]{PLAYERS, COOLDOWN, QUEUE, CHUNK_DATA};
      }
   }
}
