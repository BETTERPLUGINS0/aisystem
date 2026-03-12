package fr.xephi.authme.command.executable.authme.debug;

import fr.xephi.authme.ConsoleLogger;
import fr.xephi.authme.datasource.DataSource;
import fr.xephi.authme.datasource.MySQL;
import fr.xephi.authme.datasource.SqlDataSourceUtils;
import fr.xephi.authme.libs.ch.jalu.configme.properties.Property;
import fr.xephi.authme.libs.com.google.common.annotations.VisibleForTesting;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.output.ConsoleLoggerFactory;
import fr.xephi.authme.permission.DebugSectionPermissions;
import fr.xephi.authme.permission.PermissionNode;
import fr.xephi.authme.settings.Settings;
import fr.xephi.authme.settings.properties.DatabaseSettings;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.annotation.PostConstruct;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

class MySqlDefaultChanger implements DebugSection {
   private static final String NOT_NULL_SUFFIX;
   private static final String DEFAULT_VALUE_SUFFIX;
   private ConsoleLogger logger = ConsoleLoggerFactory.get(MySqlDefaultChanger.class);
   @Inject
   private Settings settings;
   @Inject
   private DataSource dataSource;
   private MySQL mySql;

   @PostConstruct
   void setMySqlField() {
      this.mySql = (MySQL)DebugSectionUtils.castToTypeOrNull(DebugSectionUtils.unwrapSourceFromCacheDataSource(this.dataSource), MySQL.class);
   }

   public String getName() {
      return "mysqldef";
   }

   public String getDescription() {
      return "Add or remove the default value of MySQL columns";
   }

   public PermissionNode getRequiredPermission() {
      return DebugSectionPermissions.MYSQL_DEFAULT_CHANGER;
   }

   public void execute(CommandSender sender, List<String> arguments) {
      if (this.mySql == null) {
         sender.sendMessage("Defaults can be changed for the MySQL data source only.");
      } else {
         MySqlDefaultChanger.Operation operation = (MySqlDefaultChanger.Operation)matchToEnum(arguments, 0, MySqlDefaultChanger.Operation.class);
         MySqlDefaultChanger.Columns column = (MySqlDefaultChanger.Columns)matchToEnum(arguments, 1, MySqlDefaultChanger.Columns.class);
         if (operation == MySqlDefaultChanger.Operation.DETAILS) {
            this.showColumnDetails(sender);
         } else if (operation != null && column != null) {
            sender.sendMessage(ChatColor.BLUE + "[AuthMe] MySQL change '" + column + "'");

            try {
               Connection con = this.getConnection(this.mySql);

               try {
                  switch(operation.ordinal()) {
                  case 0:
                     this.changeColumnToNotNullWithDefault(sender, column, con);
                     break;
                  case 1:
                     this.removeNotNullAndDefault(sender, column, con);
                     break;
                  default:
                     throw new IllegalStateException("Unknown operation '" + operation + "'");
                  }
               } catch (Throwable var9) {
                  if (con != null) {
                     try {
                        con.close();
                     } catch (Throwable var8) {
                        var9.addSuppressed(var8);
                     }
                  }

                  throw var9;
               }

               if (con != null) {
                  con.close();
               }
            } catch (IllegalStateException | SQLException var10) {
               this.logger.logException("Failed to perform MySQL default altering operation:", var10);
            }
         } else {
            this.displayUsageHints(sender);
         }

      }
   }

   private void changeColumnToNotNullWithDefault(CommandSender sender, MySqlDefaultChanger.Columns column, Connection con) throws SQLException {
      String tableName = (String)this.settings.getProperty(DatabaseSettings.MYSQL_TABLE);
      String columnName = (String)this.settings.getProperty(column.getColumnNameProperty());
      String sql = String.format("UPDATE %s SET %s = ? WHERE %s IS NULL;", tableName, columnName, columnName);
      PreparedStatement pst = con.prepareStatement(sql);

      int updatedRows;
      try {
         pst.setObject(1, column.getDefaultValue());
         updatedRows = pst.executeUpdate();
      } catch (Throwable var14) {
         if (pst != null) {
            try {
               pst.close();
            } catch (Throwable var12) {
               var14.addSuppressed(var12);
            }
         }

         throw var14;
      }

      if (pst != null) {
         pst.close();
      }

      sender.sendMessage("Replaced NULLs with default value ('" + column.getDefaultValue() + "'), modifying " + updatedRows + " entries");
      Statement st = con.createStatement();

      try {
         st.execute(String.format("ALTER TABLE %s MODIFY %s %s", tableName, columnName, column.getNotNullDefinition()));
         sender.sendMessage("Changed column '" + columnName + "' to have NOT NULL constraint");
      } catch (Throwable var13) {
         if (st != null) {
            try {
               st.close();
            } catch (Throwable var11) {
               var13.addSuppressed(var11);
            }
         }

         throw var13;
      }

      if (st != null) {
         st.close();
      }

      this.logger.info("Changed MySQL column '" + columnName + "' to be NOT NULL, as initiated by '" + sender.getName() + "'");
   }

   private void removeNotNullAndDefault(CommandSender sender, MySqlDefaultChanger.Columns column, Connection con) throws SQLException {
      String tableName = (String)this.settings.getProperty(DatabaseSettings.MYSQL_TABLE);
      String columnName = (String)this.settings.getProperty(column.getColumnNameProperty());
      Statement st = con.createStatement();

      try {
         st.execute(String.format("ALTER TABLE %s MODIFY %s %s", tableName, columnName, column.getNullableDefinition()));
         sender.sendMessage("Changed column '" + columnName + "' to allow nulls");
      } catch (Throwable var14) {
         if (st != null) {
            try {
               st.close();
            } catch (Throwable var12) {
               var14.addSuppressed(var12);
            }
         }

         throw var14;
      }

      if (st != null) {
         st.close();
      }

      String sql = String.format("UPDATE %s SET %s = NULL WHERE %s = ?;", tableName, columnName, columnName);
      PreparedStatement pst = con.prepareStatement(sql);

      int updatedRows;
      try {
         pst.setObject(1, column.getDefaultValue());
         updatedRows = pst.executeUpdate();
      } catch (Throwable var13) {
         if (pst != null) {
            try {
               pst.close();
            } catch (Throwable var11) {
               var13.addSuppressed(var11);
            }
         }

         throw var13;
      }

      if (pst != null) {
         pst.close();
      }

      sender.sendMessage("Replaced default value ('" + column.getDefaultValue() + "') to be NULL, modifying " + updatedRows + " entries");
      this.logger.info("Changed MySQL column '" + columnName + "' to allow NULL, as initiated by '" + sender.getName() + "'");
   }

   private void showColumnDetails(CommandSender sender) {
      sender.sendMessage(ChatColor.BLUE + "MySQL column details");
      String tableName = (String)this.settings.getProperty(DatabaseSettings.MYSQL_TABLE);

      try {
         Connection con = this.getConnection(this.mySql);

         try {
            DatabaseMetaData metaData = con.getMetaData();
            MySqlDefaultChanger.Columns[] var5 = MySqlDefaultChanger.Columns.values();
            int var6 = var5.length;

            for(int var7 = 0; var7 < var6; ++var7) {
               MySqlDefaultChanger.Columns col = var5[var7];
               String columnName = (String)this.settings.getProperty(col.getColumnNameProperty());
               String isNullText = SqlDataSourceUtils.isNotNullColumn(metaData, tableName, columnName) ? "NOT NULL" : "nullable";
               Object defaultValue = SqlDataSourceUtils.getColumnDefaultValue(metaData, tableName, columnName);
               String defaultText = defaultValue == null ? "no default" : "default: '" + defaultValue + "'";
               sender.sendMessage(this.formatColumnWithMetadata(col, metaData, tableName) + " (" + columnName + "): " + isNullText + ", " + defaultText);
            }
         } catch (Throwable var14) {
            if (con != null) {
               try {
                  con.close();
               } catch (Throwable var13) {
                  var14.addSuppressed(var13);
               }
            }

            throw var14;
         }

         if (con != null) {
            con.close();
         }
      } catch (SQLException var15) {
         this.logger.logException("Failed while showing column details:", var15);
         sender.sendMessage("Failed while showing column details. See log for info");
      }

   }

   private void displayUsageHints(CommandSender sender) {
      sender.sendMessage(ChatColor.BLUE + "MySQL column changer");
      sender.sendMessage("Adds or removes a NOT NULL constraint for a column.");
      sender.sendMessage("Examples: add a NOT NULL constraint with");
      sender.sendMessage(" /authme debug mysqldef add <column>");
      sender.sendMessage("Remove one with /authme debug mysqldef remove <column>");
      sender.sendMessage("Available columns: " + this.constructColumnListWithMetadata());
      sender.sendMessage(" " + NOT_NULL_SUFFIX + ": not-null, " + DEFAULT_VALUE_SUFFIX + ": has default. See /authme debug mysqldef details");
   }

   private String constructColumnListWithMetadata() {
      try {
         Connection con = this.getConnection(this.mySql);

         String var12;
         try {
            DatabaseMetaData metaData = con.getMetaData();
            String tableName = (String)this.settings.getProperty(DatabaseSettings.MYSQL_TABLE);
            List<String> formattedColumns = new ArrayList(MySqlDefaultChanger.Columns.values().length);
            MySqlDefaultChanger.Columns[] var5 = MySqlDefaultChanger.Columns.values();
            int var6 = var5.length;
            int var7 = 0;

            while(true) {
               if (var7 >= var6) {
                  var12 = String.join(ChatColor.RESET + ", ", formattedColumns);
                  break;
               }

               MySqlDefaultChanger.Columns col = var5[var7];
               formattedColumns.add(this.formatColumnWithMetadata(col, metaData, tableName));
               ++var7;
            }
         } catch (Throwable var10) {
            if (con != null) {
               try {
                  con.close();
               } catch (Throwable var9) {
                  var10.addSuppressed(var9);
               }
            }

            throw var10;
         }

         if (con != null) {
            con.close();
         }

         return var12;
      } catch (SQLException var11) {
         this.logger.logException("Failed to construct column list:", var11);
         return ChatColor.RED + "An error occurred! Please see the console for details.";
      }
   }

   private String formatColumnWithMetadata(MySqlDefaultChanger.Columns column, DatabaseMetaData metaData, String tableName) throws SQLException {
      String columnName = (String)this.settings.getProperty(column.getColumnNameProperty());
      boolean isNotNull = SqlDataSourceUtils.isNotNullColumn(metaData, tableName, columnName);
      boolean hasDefaultValue = SqlDataSourceUtils.getColumnDefaultValue(metaData, tableName, columnName) != null;
      return column.name() + (isNotNull ? NOT_NULL_SUFFIX : "") + (hasDefaultValue ? DEFAULT_VALUE_SUFFIX : "");
   }

   @VisibleForTesting
   Connection getConnection(MySQL mySql) {
      try {
         Method method = MySQL.class.getDeclaredMethod("getConnection");
         method.setAccessible(true);
         return (Connection)method.invoke(mySql);
      } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException var3) {
         throw new IllegalStateException("Could not get MySQL connection", var3);
      }
   }

   private static <E extends Enum<E>> E matchToEnum(List<String> arguments, int index, Class<E> clazz) {
      if (arguments.size() <= index) {
         return null;
      } else {
         String str = (String)arguments.get(index);
         return (Enum)Arrays.stream((Enum[])clazz.getEnumConstants()).filter((e) -> {
            return e.name().equalsIgnoreCase(str);
         }).findFirst().orElse((Object)null);
      }
   }

   static {
      NOT_NULL_SUFFIX = ChatColor.DARK_AQUA + "@" + ChatColor.RESET;
      DEFAULT_VALUE_SUFFIX = ChatColor.GOLD + "#" + ChatColor.RESET;
   }

   private static enum Operation {
      ADD,
      REMOVE,
      DETAILS;

      // $FF: synthetic method
      private static MySqlDefaultChanger.Operation[] $values() {
         return new MySqlDefaultChanger.Operation[]{ADD, REMOVE, DETAILS};
      }
   }

   static enum Columns {
      LASTLOGIN(DatabaseSettings.MYSQL_COL_LASTLOGIN, "BIGINT", "BIGINT NOT NULL DEFAULT 0", 0L),
      LASTIP(DatabaseSettings.MYSQL_COL_LAST_IP, "VARCHAR(40) CHARACTER SET ascii COLLATE ascii_bin", "VARCHAR(40) CHARACTER SET ascii COLLATE ascii_bin NOT NULL DEFAULT '127.0.0.1'", "127.0.0.1"),
      EMAIL(DatabaseSettings.MYSQL_COL_EMAIL, "VARCHAR(255)", "VARCHAR(255) NOT NULL DEFAULT 'your@email.com'", "your@email.com");

      private final Property<String> columnNameProperty;
      private final String nullableDefinition;
      private final String notNullDefinition;
      private final Object defaultValue;

      private Columns(Property<String> param3, String param4, String param5, Object param6) {
         this.columnNameProperty = columnNameProperty;
         this.nullableDefinition = nullableDefinition;
         this.notNullDefinition = notNullDefinition;
         this.defaultValue = defaultValue;
      }

      Property<String> getColumnNameProperty() {
         return this.columnNameProperty;
      }

      String getNullableDefinition() {
         return this.nullableDefinition;
      }

      String getNotNullDefinition() {
         return this.notNullDefinition;
      }

      Object getDefaultValue() {
         return this.defaultValue;
      }

      // $FF: synthetic method
      private static MySqlDefaultChanger.Columns[] $values() {
         return new MySqlDefaultChanger.Columns[]{LASTLOGIN, LASTIP, EMAIL};
      }
   }
}
