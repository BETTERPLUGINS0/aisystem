package github.nighter.smartspawner;

import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

public final class Scheduler {
   private static final Plugin plugin = SmartSpawner.getInstance();
   private static final boolean isFolia;

   public static Scheduler.Task runTask(Runnable runnable) {
      if (isFolia) {
         try {
            ScheduledTask task = Bukkit.getGlobalRegionScheduler().run(plugin, (scheduledTask) -> {
               runnable.run();
            });
            return new Scheduler.Task(task);
         } catch (Exception var2) {
            plugin.getLogger().log(Level.SEVERE, "Error scheduling task in Folia", var2);
            return new Scheduler.Task((Object)null);
         }
      } else {
         return new Scheduler.Task(Bukkit.getScheduler().runTask(plugin, runnable));
      }
   }

   public static Scheduler.Task runTaskAsync(Runnable runnable) {
      if (isFolia) {
         try {
            ScheduledTask task = Bukkit.getAsyncScheduler().runNow(plugin, (scheduledTask) -> {
               runnable.run();
            });
            return new Scheduler.Task(task);
         } catch (Exception var2) {
            plugin.getLogger().log(Level.SEVERE, "Error scheduling async task in Folia", var2);
            return new Scheduler.Task((Object)null);
         }
      } else {
         return new Scheduler.Task(Bukkit.getScheduler().runTaskAsynchronously(plugin, runnable));
      }
   }

   public static Scheduler.Task runTaskLater(Runnable runnable, long delayTicks) {
      if (isFolia) {
         try {
            ScheduledTask task = Bukkit.getGlobalRegionScheduler().runDelayed(plugin, (scheduledTask) -> {
               runnable.run();
            }, delayTicks < 1L ? 1L : delayTicks);
            return new Scheduler.Task(task);
         } catch (Exception var4) {
            plugin.getLogger().log(Level.SEVERE, "Error scheduling delayed task in Folia", var4);
            return new Scheduler.Task((Object)null);
         }
      } else {
         return new Scheduler.Task(Bukkit.getScheduler().runTaskLater(plugin, runnable, delayTicks));
      }
   }

   public static Scheduler.Task runTaskLaterAsync(Runnable runnable, long delayTicks) {
      if (isFolia) {
         try {
            long delayMs = delayTicks * 50L;
            ScheduledTask task = Bukkit.getAsyncScheduler().runDelayed(plugin, (scheduledTask) -> {
               runnable.run();
            }, delayMs, TimeUnit.MILLISECONDS);
            return new Scheduler.Task(task);
         } catch (Exception var6) {
            plugin.getLogger().log(Level.SEVERE, "Error scheduling delayed async task in Folia", var6);
            return new Scheduler.Task((Object)null);
         }
      } else {
         return new Scheduler.Task(Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, runnable, delayTicks));
      }
   }

   public static Scheduler.Task runTaskTimer(Runnable runnable, long delayTicks, long periodTicks) {
      if (isFolia) {
         try {
            ScheduledTask task = Bukkit.getGlobalRegionScheduler().runAtFixedRate(plugin, (scheduledTask) -> {
               runnable.run();
            }, delayTicks < 1L ? 1L : delayTicks, periodTicks);
            return new Scheduler.Task(task);
         } catch (Exception var6) {
            plugin.getLogger().log(Level.SEVERE, "Error scheduling timer task in Folia", var6);
            return new Scheduler.Task((Object)null);
         }
      } else {
         return new Scheduler.Task(Bukkit.getScheduler().runTaskTimer(plugin, runnable, delayTicks, periodTicks));
      }
   }

   public static Scheduler.Task runTaskTimerAsync(Runnable runnable, long delayTicks, long periodTicks) {
      if (isFolia) {
         try {
            long delayMs = delayTicks * 50L;
            long periodMs = periodTicks * 50L;
            ScheduledTask task = Bukkit.getAsyncScheduler().runAtFixedRate(plugin, (scheduledTask) -> {
               runnable.run();
            }, delayMs, periodMs, TimeUnit.MILLISECONDS);
            return new Scheduler.Task(task);
         } catch (Exception var10) {
            plugin.getLogger().log(Level.SEVERE, "Error scheduling timer async task in Folia", var10);
            return new Scheduler.Task((Object)null);
         }
      } else {
         return new Scheduler.Task(Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, runnable, delayTicks, periodTicks));
      }
   }

   public static Scheduler.Task runEntityTask(Entity entity, Runnable runnable) {
      if (isFolia && entity != null) {
         try {
            ScheduledTask task = entity.getScheduler().run(plugin, (scheduledTask) -> {
               runnable.run();
            }, (Runnable)null);
            return new Scheduler.Task(task);
         } catch (Exception var3) {
            plugin.getLogger().log(Level.WARNING, "Error scheduling entity task in Folia, falling back to global scheduler", var3);
            return runTask(runnable);
         }
      } else {
         return runTask(runnable);
      }
   }

   public static Scheduler.Task runEntityTaskLater(Entity entity, Runnable runnable, long delayTicks) {
      if (isFolia && entity != null) {
         try {
            ScheduledTask task = entity.getScheduler().runDelayed(plugin, (scheduledTask) -> {
               runnable.run();
            }, (Runnable)null, delayTicks < 1L ? 1L : delayTicks);
            return new Scheduler.Task(task);
         } catch (Exception var5) {
            plugin.getLogger().log(Level.WARNING, "Error scheduling delayed entity task in Folia, falling back to global scheduler", var5);
            return runTaskLater(runnable, delayTicks);
         }
      } else {
         return runTaskLater(runnable, delayTicks);
      }
   }

   public static Scheduler.Task runEntityTaskTimer(Entity entity, Runnable runnable, long delayTicks, long periodTicks) {
      if (isFolia && entity != null) {
         try {
            ScheduledTask task = entity.getScheduler().runAtFixedRate(plugin, (scheduledTask) -> {
               runnable.run();
            }, (Runnable)null, delayTicks < 1L ? 1L : delayTicks, periodTicks);
            return new Scheduler.Task(task);
         } catch (Exception var7) {
            plugin.getLogger().log(Level.WARNING, "Error scheduling timer entity task in Folia, falling back to global scheduler", var7);
            return runTaskTimer(runnable, delayTicks, periodTicks);
         }
      } else {
         return runTaskTimer(runnable, delayTicks, periodTicks);
      }
   }

   public static Scheduler.Task runLocationTask(Location location, Runnable runnable) {
      if (isFolia && location != null && location.getWorld() != null) {
         try {
            ScheduledTask task = Bukkit.getRegionScheduler().run(plugin, location, (scheduledTask) -> {
               runnable.run();
            });
            return new Scheduler.Task(task);
         } catch (Exception var3) {
            plugin.getLogger().log(Level.WARNING, "Error scheduling location task in Folia, falling back to global scheduler", var3);
            return runTask(runnable);
         }
      } else {
         return runTask(runnable);
      }
   }

   public static Scheduler.Task runChunkTask(World world, int chunkX, int chunkZ, Runnable runnable) {
      if (isFolia && world != null) {
         try {
            ScheduledTask task = Bukkit.getRegionScheduler().run(plugin, world, chunkX, chunkZ, (scheduledTask) -> {
               runnable.run();
            });
            return new Scheduler.Task(task);
         } catch (Exception var5) {
            plugin.getLogger().log(Level.WARNING, "Error scheduling location task in Folia, falling back to global scheduler", var5);
            return runTask(runnable);
         }
      } else {
         return runTask(runnable);
      }
   }

   public static Scheduler.Task runLocationTaskLater(Location location, Runnable runnable, long delayTicks) {
      if (isFolia && location != null && location.getWorld() != null) {
         try {
            ScheduledTask task = Bukkit.getRegionScheduler().runDelayed(plugin, location, (scheduledTask) -> {
               runnable.run();
            }, delayTicks < 1L ? 1L : delayTicks);
            return new Scheduler.Task(task);
         } catch (Exception var5) {
            plugin.getLogger().log(Level.WARNING, "Error scheduling delayed location task in Folia, falling back to global scheduler", var5);
            return runTaskLater(runnable, delayTicks);
         }
      } else {
         return runTaskLater(runnable, delayTicks);
      }
   }

   public static Scheduler.Task runLocationTaskTimer(Location location, Runnable runnable, long delayTicks, long periodTicks) {
      if (isFolia && location != null && location.getWorld() != null) {
         try {
            ScheduledTask task = Bukkit.getRegionScheduler().runAtFixedRate(plugin, location, (scheduledTask) -> {
               runnable.run();
            }, delayTicks < 1L ? 1L : delayTicks, periodTicks);
            return new Scheduler.Task(task);
         } catch (Exception var7) {
            plugin.getLogger().log(Level.WARNING, "Error scheduling timer location task in Folia, falling back to global scheduler", var7);
            return runTaskTimer(runnable, delayTicks, periodTicks);
         }
      } else {
         return runTaskTimer(runnable, delayTicks, periodTicks);
      }
   }

   public static Scheduler.Task runWorldTask(Location location, Runnable runnable) {
      if (isFolia && location != null && location.getWorld() != null) {
         try {
            ScheduledTask task = Bukkit.getRegionScheduler().run(plugin, location, (scheduledTask) -> {
               runnable.run();
            });
            return new Scheduler.Task(task);
         } catch (Exception var3) {
            plugin.getLogger().log(Level.WARNING, "Error scheduling world task in Folia, falling back to global scheduler", var3);
            return runTask(runnable);
         }
      } else {
         return runTask(runnable);
      }
   }

   public static Scheduler.Task runWorldTaskLater(Location location, Runnable runnable, long delayTicks) {
      if (isFolia && location != null && location.getWorld() != null) {
         try {
            ScheduledTask task = Bukkit.getRegionScheduler().runDelayed(plugin, location, (scheduledTask) -> {
               runnable.run();
            }, delayTicks < 1L ? 1L : delayTicks);
            return new Scheduler.Task(task);
         } catch (Exception var5) {
            plugin.getLogger().log(Level.WARNING, "Error scheduling delayed world task in Folia, falling back to global scheduler", var5);
            return runTaskLater(runnable, delayTicks);
         }
      } else {
         return runTaskLater(runnable, delayTicks);
      }
   }

   public static <T> CompletableFuture<T> supplySync(Supplier<T> supplier) {
      CompletableFuture future = new CompletableFuture();

      try {
         if (isFolia) {
            Bukkit.getGlobalRegionScheduler().run(plugin, (task) -> {
               try {
                  future.complete(supplier.get());
               } catch (Throwable var4) {
                  future.completeExceptionally(var4);
                  plugin.getLogger().log(Level.SEVERE, "Error while executing sync task", var4);
               }

            });
         } else {
            Bukkit.getScheduler().runTask(plugin, () -> {
               try {
                  future.complete(supplier.get());
               } catch (Throwable var3) {
                  future.completeExceptionally(var3);
                  plugin.getLogger().log(Level.SEVERE, "Error while executing sync task", var3);
               }

            });
         }
      } catch (Throwable var3) {
         future.completeExceptionally(var3);
      }

      return future;
   }

   public static <T> CompletableFuture<T> supplyAsync(Supplier<T> supplier) {
      CompletableFuture future = new CompletableFuture();

      try {
         if (isFolia) {
            Bukkit.getAsyncScheduler().runNow(plugin, (task) -> {
               try {
                  future.complete(supplier.get());
               } catch (Throwable var4) {
                  future.completeExceptionally(var4);
                  plugin.getLogger().log(Level.SEVERE, "Error while executing async task", var4);
               }

            });
         } else {
            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
               try {
                  future.complete(supplier.get());
               } catch (Throwable var3) {
                  future.completeExceptionally(var3);
                  plugin.getLogger().log(Level.SEVERE, "Error while executing async task", var3);
               }

            });
         }
      } catch (Throwable var3) {
         future.completeExceptionally(var3);
      }

      return future;
   }

   static {
      boolean foliaDetected = false;

      try {
         Class.forName("io.papermc.paper.threadedregions.RegionizedServer");
         foliaDetected = true;
         plugin.getLogger().info("Folia detected! Using region-based threading system.");
      } catch (ClassNotFoundException var2) {
         plugin.getLogger().info("Running on standard Paper server.");
      } catch (Exception var3) {
         plugin.getLogger().warning("Unexpected error while detecting server type: " + var3.getMessage());
      }

      isFolia = foliaDetected;
   }

   public static class Task {
      private final Object task;

      Task(Object task) {
         this.task = task;
      }

      public void cancel() {
         if (this.task != null) {
            try {
               if (Scheduler.isFolia) {
                  if (this.task instanceof ScheduledTask) {
                     ((ScheduledTask)this.task).cancel();
                  }
               } else if (this.task instanceof BukkitTask) {
                  ((BukkitTask)this.task).cancel();
               }
            } catch (Exception var2) {
               Scheduler.plugin.getLogger().log(Level.WARNING, "Failed to cancel task", var2);
            }

         }
      }

      public Object getTask() {
         return this.task;
      }

      public boolean isCancelled() {
         if (this.task == null) {
            return true;
         } else {
            try {
               if (Scheduler.isFolia) {
                  if (this.task instanceof ScheduledTask) {
                     return ((ScheduledTask)this.task).isCancelled();
                  }
               } else if (this.task instanceof BukkitTask) {
                  return ((BukkitTask)this.task).isCancelled();
               }
            } catch (Exception var2) {
            }

            return true;
         }
      }
   }
}
