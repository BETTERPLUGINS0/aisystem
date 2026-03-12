package com.lenis0012.bukkit.loginsecurity.database;

import com.lenis0012.bukkit.loginsecurity.LoginSecurity;
import com.lenis0012.bukkit.loginsecurity.storage.PlayerProfile;
import com.lenis0012.bukkit.loginsecurity.util.UserIdMode;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import javax.sql.DataSource;
import org.bukkit.Bukkit;

public class ProfileRepository {
   private final LoginSecurity loginSecurity;
   private final DataSource dataSource;

   public ProfileRepository(LoginSecurity loginSecurity, DataSource dataSource) {
      this.loginSecurity = loginSecurity;
      this.dataSource = dataSource;
   }

   public void insert(PlayerProfile profile, Consumer<AsyncResult<PlayerProfile>> callback) {
      Bukkit.getScheduler().runTaskAsynchronously(this.loginSecurity, () -> {
         try {
            this.insertBlocking(profile);
            this.resolveResult(callback, profile);
         } catch (SQLException var4) {
            this.resolveError(callback, var4);
         }

      });
   }

   public void insertBlocking(PlayerProfile profile) throws SQLException {
      Connection connection = this.dataSource.getConnection();

      try {
         PreparedStatement statement = connection.prepareStatement("INSERT INTO ls_players(uuid_mode,unique_user_id,last_name,ip_address,password,hashing_algorithm,location_id,inventory_id,last_login,registration_date,optlock) VALUES(?,?,?,?,?,?,?,?,?,?,?);", 1);

         try {
            this.prepareInsert(statement, profile);
            statement.executeUpdate();
            ResultSet keys = statement.getGeneratedKeys();

            try {
               if (!keys.next()) {
                  throw new RuntimeException("No keys were returned after insert");
               }

               profile.setId(keys.getInt(1));
            } catch (Throwable var10) {
               if (keys != null) {
                  try {
                     keys.close();
                  } catch (Throwable var9) {
                     var10.addSuppressed(var9);
                  }
               }

               throw var10;
            }

            if (keys != null) {
               keys.close();
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

   public void update(PlayerProfile profile, Consumer<AsyncResult<PlayerProfile>> callback) {
      Bukkit.getScheduler().runTaskAsynchronously(this.loginSecurity, () -> {
         try {
            this.updateBlocking(profile);
            this.resolveResult(callback, profile);
         } catch (SQLException var4) {
            this.resolveError(callback, var4);
         }

      });
   }

   public void updateBlocking(PlayerProfile profile) throws SQLException {
      Connection connection = this.dataSource.getConnection();

      try {
         PreparedStatement statement = connection.prepareStatement("UPDATE ls_players SET last_name=?,ip_address=?,password=?,hashing_algorithm=?,location_id=?,inventory_id=?,last_login=?,optlock=? WHERE id=?;");

         try {
            this.prepareUpdate(statement, profile);
            statement.executeUpdate();
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

   public void delete(PlayerProfile profile, Consumer<AsyncResult<PlayerProfile>> callback) {
      Bukkit.getScheduler().runTaskAsynchronously(this.loginSecurity, () -> {
         try {
            this.deleteBlocking(profile);
            this.resolveResult(callback, profile);
         } catch (SQLException var4) {
            this.resolveError(callback, var4);
         }

      });
   }

   public void deleteBlocking(PlayerProfile profile) throws SQLException {
      Connection connection = this.dataSource.getConnection();

      try {
         PreparedStatement statement = connection.prepareStatement("DELETE FROM ls_players WHERE id=?;");

         try {
            statement.setInt(1, profile.getId());
            statement.executeUpdate();
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

   public void findByUniqueId(UUID unqiueId, Consumer<AsyncResult<PlayerProfile>> callback) {
      Bukkit.getScheduler().runTaskAsynchronously(this.loginSecurity, () -> {
         try {
            PlayerProfile profile = this.findByUniqueUserIdBlocking(unqiueId);
            this.resolveResult(callback, profile);
         } catch (SQLException var4) {
            this.resolveError(callback, var4);
         }

      });
   }

   public PlayerProfile findByUniqueUserIdBlocking(UUID uniqueId) throws SQLException {
      Connection connection = this.dataSource.getConnection();

      PlayerProfile var5;
      label99: {
         try {
            PreparedStatement statement;
            label101: {
               statement = connection.prepareStatement("SELECT * FROM ls_players WHERE unique_user_id=?");

               try {
                  statement.setString(1, uniqueId.toString());
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

   public void findByLastName(String lastName, Consumer<AsyncResult<PlayerProfile>> callback) {
      Bukkit.getScheduler().runTaskAsynchronously(this.loginSecurity, () -> {
         try {
            PlayerProfile profile = this.findByLastNameBlocking(lastName);
            this.resolveResult(callback, profile);
         } catch (SQLException var4) {
            this.resolveError(callback, var4);
         }

      });
   }

   public PlayerProfile findByLastNameBlocking(String lastName) throws SQLException {
      Connection connection = this.dataSource.getConnection();

      PlayerProfile var5;
      label99: {
         try {
            PreparedStatement statement;
            label101: {
               statement = connection.prepareStatement("SELECT * FROM ls_players WHERE last_name=?;");

               try {
                  statement.setString(1, lastName);
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

   public void iterateAllBlocking(SQLConsumer<PlayerProfile> consumer) throws SQLException {
      Connection connection = this.dataSource.getConnection();

      try {
         Statement statement = connection.createStatement();

         try {
            ResultSet result = statement.executeQuery("SELECT * FROM ls_players;");

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

   public void batchInsert(SQLConsumer<SQLConsumer<PlayerProfile>> callback) throws SQLException {
      Connection connection = this.dataSource.getConnection();

      try {
         PreparedStatement statement = connection.prepareStatement("INSERT INTO ls_players(uuid_mode,unique_user_id,last_name,ip_address,password,hashing_algorithm,location_id,inventory_id,last_login,registration_date,optlock) VALUES(?,?,?,?,?,?,?,?,?,?,?);");

         try {
            AtomicInteger currentBatchSize = new AtomicInteger();
            callback.accept((profile) -> {
               this.prepareInsert(statement, profile);
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

   private void prepareInsert(PreparedStatement statement, PlayerProfile profile) throws SQLException {
      statement.setString(1, profile.getUniqueIdMode().getId());
      statement.setString(2, profile.getUniqueUserId());
      statement.setString(3, profile.getLastName());
      statement.setString(4, profile.getIpAddress());
      statement.setString(5, profile.getPassword());
      statement.setInt(6, profile.getHashingAlgorithm());
      if (profile.getLoginLocationId() == null) {
         statement.setNull(7, 4);
      } else {
         statement.setInt(7, profile.getLoginLocationId());
      }

      if (profile.getInventoryId() == null) {
         statement.setNull(8, 4);
      } else {
         statement.setInt(8, profile.getInventoryId());
      }

      statement.setTimestamp(9, Timestamp.from(Instant.now()));
      statement.setDate(10, new Date(System.currentTimeMillis()));
      statement.setLong(11, 1L);
   }

   private void prepareUpdate(PreparedStatement statement, PlayerProfile profile) throws SQLException {
      statement.setString(1, profile.getLastName());
      statement.setString(2, profile.getIpAddress());
      statement.setString(3, profile.getPassword());
      statement.setInt(4, profile.getHashingAlgorithm());
      if (profile.getLoginLocationId() == null) {
         statement.setNull(5, 4);
      } else {
         statement.setInt(5, profile.getLoginLocationId());
      }

      if (profile.getInventoryId() == null) {
         statement.setNull(6, 4);
      } else {
         statement.setInt(6, profile.getInventoryId());
      }

      statement.setTimestamp(7, Timestamp.from(Instant.now()));
      statement.setLong(8, profile.getVersion() + 1L);
      statement.setInt(9, profile.getId());
   }

   private PlayerProfile parseResultSet(ResultSet result) throws SQLException {
      PlayerProfile profile = new PlayerProfile();
      profile.setId(result.getInt("id"));
      profile.setUniqueIdMode(UserIdMode.fromId(result.getString("uuid_mode")));
      profile.setUniqueUserId(result.getString("unique_user_id"));
      profile.setLastName(result.getString("last_name"));
      profile.setIpAddress(result.getString("ip_address"));
      profile.setPassword(result.getString("password"));
      profile.setHashingAlgorithm(result.getInt("hashing_algorithm"));
      profile.setLoginLocationId((Integer)result.getObject("location_id"));
      profile.setInventoryId((Integer)result.getObject("inventory_id"));
      profile.setLastLogin(result.getTimestamp("last_login"));
      profile.setRegistrationDate(result.getDate("registration_date"));
      profile.setVersion(result.getLong("optlock"));
      return profile;
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
