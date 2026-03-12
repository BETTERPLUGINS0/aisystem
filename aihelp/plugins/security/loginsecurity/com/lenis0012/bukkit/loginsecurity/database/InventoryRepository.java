package com.lenis0012.bukkit.loginsecurity.database;

import com.lenis0012.bukkit.loginsecurity.LoginSecurity;
import com.lenis0012.bukkit.loginsecurity.storage.PlayerInventory;
import com.lenis0012.bukkit.loginsecurity.storage.PlayerProfile;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.logging.Level;
import javax.sql.DataSource;
import org.bukkit.Bukkit;

public class InventoryRepository {
   private final LoginSecurity loginSecurity;
   private final DataSource dataSource;

   public InventoryRepository(LoginSecurity loginSecurity, DataSource dataSource) {
      this.loginSecurity = loginSecurity;
      this.dataSource = dataSource;
   }

   public void insert(PlayerProfile profile, PlayerInventory inventory, Consumer<AsyncResult<PlayerInventory>> callback) {
      Bukkit.getScheduler().runTaskAsynchronously(this.loginSecurity, () -> {
         try {
            this.insertBlocking(profile, inventory);
            this.resolveResult(callback, inventory);
         } catch (SQLException var5) {
            this.resolveError(callback, var5);
         }

      });
   }

   public void insertBlocking(PlayerProfile profile, PlayerInventory inventory) throws SQLException {
      Connection connection = this.dataSource.getConnection();

      try {
         PreparedStatement statement = connection.prepareStatement("INSERT INTO ls_inventories(helmet, chestplate, leggings, boots, off_hand, contents) VALUES (?,?,?,?,?,?);", 1);

         try {
            this.prepareInsert(statement, inventory);
            statement.executeUpdate();
            ResultSet keys = statement.getGeneratedKeys();

            try {
               if (!keys.next()) {
                  throw new RuntimeException("No keys were returned after insert");
               }

               inventory.setId(keys.getInt(1));
            } catch (Throwable var13) {
               if (keys != null) {
                  try {
                     keys.close();
                  } catch (Throwable var10) {
                     var13.addSuppressed(var10);
                  }
               }

               throw var13;
            }

            if (keys != null) {
               keys.close();
            }
         } catch (Throwable var14) {
            if (statement != null) {
               try {
                  statement.close();
               } catch (Throwable var9) {
                  var14.addSuppressed(var9);
               }
            }

            throw var14;
         }

         if (statement != null) {
            statement.close();
         }

         profile.setInventoryId(inventory.getId());
         statement = connection.prepareStatement("UPDATE ls_players SET inventory_id=? WHERE id=?;");

         try {
            statement.setInt(1, inventory.getId());
            statement.setInt(2, profile.getId());
            if (statement.executeUpdate() < 1) {
               this.loginSecurity.getLogger().log(Level.WARNING, "Failed to set location id in profile");
               throw new SQLException("Failed set location id in profile");
            }
         } catch (Throwable var12) {
            if (statement != null) {
               try {
                  statement.close();
               } catch (Throwable var11) {
                  var12.addSuppressed(var11);
               }
            }

            throw var12;
         }

         if (statement != null) {
            statement.close();
         }
      } catch (Throwable var15) {
         if (connection != null) {
            try {
               connection.close();
            } catch (Throwable var8) {
               var15.addSuppressed(var8);
            }
         }

         throw var15;
      }

      if (connection != null) {
         connection.close();
      }

   }

   public void findById(int id, Consumer<AsyncResult<PlayerInventory>> callback) {
      Bukkit.getScheduler().runTaskAsynchronously(this.loginSecurity, () -> {
         try {
            PlayerInventory inventory = this.findByIdBlocking(id);
            this.resolveResult(callback, inventory);
         } catch (SQLException var4) {
            this.resolveError(callback, var4);
         }

      });
   }

   public PlayerInventory findByIdBlocking(int id) throws SQLException {
      Connection connection = this.dataSource.getConnection();

      PlayerInventory var5;
      label99: {
         try {
            PreparedStatement statement;
            label101: {
               statement = connection.prepareStatement("SELECT * FROM ls_inventories WHERE id=?;");

               try {
                  statement.setInt(1, id);
                  ResultSet result = statement.executeQuery();

                  label87: {
                     try {
                        if (!result.next()) {
                           var5 = null;
                           break label87;
                        }

                        var5 = this.parseResultSet(result);
                     } catch (Throwable var10) {
                        if (result != null) {
                           try {
                              result.close();
                           } catch (Throwable var9) {
                              var10.addSuppressed(var9);
                           }
                        }

                        throw var10;
                     }

                     if (result != null) {
                        result.close();
                     }
                     break label101;
                  }

                  if (result != null) {
                     result.close();
                  }
               } catch (Throwable var11) {
                  if (statement != null) {
                     try {
                        statement.close();
                     } catch (Throwable var8) {
                        var11.addSuppressed(var8);
                     }
                  }

                  throw var11;
               }

               if (statement != null) {
                  statement.close();
               }
               break label99;
            }

            if (statement != null) {
               statement.close();
            }
         } catch (Throwable var12) {
            if (connection != null) {
               try {
                  connection.close();
               } catch (Throwable var7) {
                  var12.addSuppressed(var7);
               }
            }

            throw var12;
         }

         if (connection != null) {
            connection.close();
         }

         return var5;
      }

      if (connection != null) {
         connection.close();
      }

      return var5;
   }

   public void iterateAllBlocking(SQLConsumer<PlayerInventory> consumer) throws SQLException {
      Connection connection = this.dataSource.getConnection();

      try {
         Statement statement = connection.createStatement();

         try {
            ResultSet result = statement.executeQuery("SELECT * FROM ls_inventories;");

            try {
               while(result.next()) {
                  consumer.accept(this.parseResultSet(result));
               }
            } catch (Throwable var10) {
               if (result != null) {
                  try {
                     result.close();
                  } catch (Throwable var9) {
                     var10.addSuppressed(var9);
                  }
               }

               throw var10;
            }

            if (result != null) {
               result.close();
            }
         } catch (Throwable var11) {
            if (statement != null) {
               try {
                  statement.close();
               } catch (Throwable var8) {
                  var11.addSuppressed(var8);
               }
            }

            throw var11;
         }

         if (statement != null) {
            statement.close();
         }
      } catch (Throwable var12) {
         if (connection != null) {
            try {
               connection.close();
            } catch (Throwable var7) {
               var12.addSuppressed(var7);
            }
         }

         throw var12;
      }

      if (connection != null) {
         connection.close();
      }

   }

   public void batchInsert(SQLConsumer<SQLConsumer<PlayerInventory>> callback) throws SQLException {
      Connection connection = this.dataSource.getConnection();

      try {
         PreparedStatement statement = connection.prepareStatement("INSERT INTO ls_inventories(helmet, chestplate, leggings, boots, off_hand, contents) VALUES (?,?,?,?,?,?);");

         try {
            AtomicInteger currentBatchSize = new AtomicInteger();
            callback.accept((inventory) -> {
               this.prepareInsert(statement, inventory);
               statement.addBatch();
               if (currentBatchSize.incrementAndGet() >= 1000) {
                  statement.executeBatch();
                  currentBatchSize.set(0);
               }

            });
            if (currentBatchSize.get() > 0) {
               statement.executeBatch();
            }
         } catch (Throwable var8) {
            if (statement != null) {
               try {
                  statement.close();
               } catch (Throwable var7) {
                  var8.addSuppressed(var7);
               }
            }

            throw var8;
         }

         if (statement != null) {
            statement.close();
         }
      } catch (Throwable var9) {
         if (connection != null) {
            try {
               connection.close();
            } catch (Throwable var6) {
               var9.addSuppressed(var6);
            }
         }

         throw var9;
      }

      if (connection != null) {
         connection.close();
      }

   }

   private void prepareInsert(PreparedStatement statement, PlayerInventory inventory) throws SQLException {
      statement.setString(1, inventory.getHelmet());
      statement.setString(2, inventory.getChestplate());
      statement.setString(3, inventory.getLeggings());
      statement.setString(4, inventory.getBoots());
      statement.setString(5, inventory.getOffHand());
      statement.setString(6, inventory.getContents());
   }

   private PlayerInventory parseResultSet(ResultSet result) throws SQLException {
      PlayerInventory inventory = new PlayerInventory();
      inventory.setId(result.getInt("id"));
      inventory.setHelmet(result.getString("helmet"));
      inventory.setChestplate(result.getString("chestplate"));
      inventory.setLeggings(result.getString("leggings"));
      inventory.setBoots(result.getString("boots"));
      inventory.setOffHand(result.getString("off_hand"));
      inventory.setContents(result.getString("contents"));
      return inventory;
   }

   private <T> void resolveResult(Consumer<AsyncResult<T>> callback, T result) {
      Bukkit.getScheduler().runTask(this.loginSecurity, () -> {
         callback.accept(new AsyncResult(true, result, (Exception)null));
      });
   }

   private <T> void resolveError(Consumer<AsyncResult<T>> callback, Exception error) {
      Bukkit.getScheduler().runTask(this.loginSecurity, () -> {
         callback.accept(new AsyncResult(false, (Object)null, error));
      });
   }
}
