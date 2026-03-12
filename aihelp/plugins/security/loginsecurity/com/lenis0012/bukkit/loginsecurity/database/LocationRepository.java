package com.lenis0012.bukkit.loginsecurity.database;

import com.lenis0012.bukkit.loginsecurity.LoginSecurity;
import com.lenis0012.bukkit.loginsecurity.storage.PlayerLocation;
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

public class LocationRepository {
   private final LoginSecurity loginSecurity;
   private final DataSource dataSource;

   public LocationRepository(LoginSecurity loginSecurity, DataSource dataSource) {
      this.loginSecurity = loginSecurity;
      this.dataSource = dataSource;
   }

   public void insertLoginLocation(PlayerProfile profile, PlayerLocation location, Consumer<AsyncResult<PlayerLocation>> callback) {
      Bukkit.getScheduler().runTaskAsynchronously(this.loginSecurity, () -> {
         try {
            this.insertLoginLocationBlocking(profile, location);
            this.resolveResult(callback, location);
         } catch (SQLException var5) {
            this.resolveError(callback, var5);
         }

      });
   }

   public void insertLoginLocationBlocking(PlayerProfile profile, PlayerLocation location) throws SQLException {
      Connection connection = this.dataSource.getConnection();

      try {
         PreparedStatement statement = connection.prepareStatement("INSERT INTO ls_locations(world, x, y, z, yaw, pitch) VALUES (?,?,?,?,?,?);", 1);

         try {
            this.prepareInsert(statement, location);
            statement.executeUpdate();
            ResultSet keys = statement.getGeneratedKeys();

            try {
               if (!keys.next()) {
                  throw new SQLException("Could not get ID for new location");
               }

               location.setId(keys.getInt(1));
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

         profile.setLoginLocationId(location.getId());
         statement = connection.prepareStatement("UPDATE ls_players SET location_id=? WHERE id=?;");

         try {
            statement.setInt(1, location.getId());
            statement.setInt(2, profile.getId());
            if (statement.executeUpdate() < 1) {
               this.loginSecurity.getLogger().log(Level.WARNING, "Failed to set inventory id in profile");
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

   public void findById(int id, Consumer<AsyncResult<PlayerLocation>> callback) {
      Bukkit.getScheduler().runTaskAsynchronously(this.loginSecurity, () -> {
         try {
            PlayerLocation location = this.findByIdBlocking(id);
            this.resolveResult(callback, location);
         } catch (SQLException var4) {
            this.resolveError(callback, var4);
         }

      });
   }

   public PlayerLocation findByIdBlocking(int id) throws SQLException {
      Connection connection = this.dataSource.getConnection();

      PlayerLocation var5;
      label99: {
         try {
            PreparedStatement statement;
            label101: {
               statement = connection.prepareStatement("SELECT * FROM ls_locations WHERE id=?;");

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

   public void delete(PlayerLocation serializedLocation) {
      Bukkit.getScheduler().runTaskAsynchronously(this.loginSecurity, () -> {
         this.deleteBlocking(serializedLocation);
      });
   }

   public boolean deleteBlocking(PlayerLocation serializedLocation) {
      try {
         Connection connection = this.dataSource.getConnection();

         boolean var4;
         try {
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM ls_locations WHERE id=?;");

            try {
               preparedStatement.setInt(1, serializedLocation.getId());
               var4 = preparedStatement.executeUpdate() > 0;
            } catch (Throwable var8) {
               if (preparedStatement != null) {
                  try {
                     preparedStatement.close();
                  } catch (Throwable var7) {
                     var8.addSuppressed(var7);
                  }
               }

               throw var8;
            }

            if (preparedStatement != null) {
               preparedStatement.close();
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

         return var4;
      } catch (SQLException var10) {
         this.loginSecurity.getLogger().log(Level.WARNING, "Failed to delete player login location", var10);
         return false;
      }
   }

   public void iterateAllBlocking(SQLConsumer<PlayerLocation> consumer) throws SQLException {
      Connection connection = this.dataSource.getConnection();

      try {
         Statement statement = connection.createStatement();

         try {
            ResultSet result = statement.executeQuery("SELECT * FROM ls_locations;");

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

   public void batchInsert(SQLConsumer<SQLConsumer<PlayerLocation>> callback) throws SQLException {
      Connection connection = this.dataSource.getConnection();

      try {
         PreparedStatement statement = connection.prepareStatement("INSERT INTO ls_locations(world, x, y, z, yaw, pitch) VALUES (?,?,?,?,?,?);");

         try {
            AtomicInteger currentBatchSize = new AtomicInteger();
            callback.accept((location) -> {
               this.prepareInsert(statement, location);
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

   private PlayerLocation parseResultSet(ResultSet result) throws SQLException {
      PlayerLocation location = new PlayerLocation();
      location.setId(result.getInt("id"));
      location.setWorld(result.getString("world"));
      location.setX(result.getDouble("x"));
      location.setY(result.getDouble("y"));
      location.setZ(result.getDouble("z"));
      location.setYaw(result.getInt("yaw"));
      location.setPitch(result.getInt("pitch"));
      return location;
   }

   private void prepareInsert(PreparedStatement statement, PlayerLocation location) throws SQLException {
      statement.setString(1, location.getWorld());
      statement.setDouble(2, location.getX());
      statement.setDouble(3, location.getY());
      statement.setDouble(4, location.getZ());
      statement.setInt(5, location.getYaw());
      statement.setInt(6, location.getPitch());
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
