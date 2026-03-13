package me.ag4.playershop.api;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import me.ag4.playershop.PlayerShop;

public class SQLite {
   private static final String URL = "jdbc:sqlite:" + PlayerShop.getInstance().getDataFolder().getPath() + "/database.db";

   public static void initializeDatabase() {
      String createTableSQL = "CREATE TABLE IF NOT EXISTS kits (uuid TEXT PRIMARY KEY,kit TEXT NOT NULL);";

      try {
         Connection conn = DriverManager.getConnection(URL);

         try {
            Statement stmt = conn.createStatement();

            try {
               stmt.execute(createTableSQL);
            } catch (Throwable var7) {
               if (stmt != null) {
                  try {
                     stmt.close();
                  } catch (Throwable var6) {
                     var7.addSuppressed(var6);
                  }
               }

               throw var7;
            }

            if (stmt != null) {
               stmt.close();
            }
         } catch (Throwable var8) {
            if (conn != null) {
               try {
                  conn.close();
               } catch (Throwable var5) {
                  var8.addSuppressed(var5);
               }
            }

            throw var8;
         }

         if (conn != null) {
            conn.close();
         }
      } catch (SQLException var9) {
         var9.printStackTrace();
      }

   }
}
