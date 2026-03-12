package fr.xephi.authme.service;

import fr.xephi.authme.AuthMe;
import fr.xephi.authme.initialization.SettingsDependent;
import fr.xephi.authme.libs.com.github.Anon8281.universalScheduler.UniversalRunnable;
import fr.xephi.authme.libs.com.github.Anon8281.universalScheduler.UniversalScheduler;
import fr.xephi.authme.libs.com.github.Anon8281.universalScheduler.scheduling.tasks.MyScheduledTask;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.settings.Settings;
import fr.xephi.authme.settings.properties.PluginSettings;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import org.bukkit.BanEntry;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.BanList.Type;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class BukkitService implements SettingsDependent {
   public static final int TICKS_PER_SECOND = 20;
   public static final int TICKS_PER_MINUTE = 1200;
   private static final boolean isFolia;
   private final AuthMe authMe;
   private boolean useAsyncTasks;

   @Inject
   BukkitService(AuthMe authMe, Settings settings) {
      this.authMe = authMe;
      this.reload(settings);
   }

   public void scheduleSyncDelayedTask(Runnable task) {
      this.runTask(task);
   }

   public void scheduleSyncDelayedTask(Runnable task, long delay) {
      if (isFolia) {
         this.runTaskLater(task, delay);
      } else {
         Bukkit.getScheduler().runTaskLater(this.authMe, task, delay);
      }

   }

   public void scheduleSyncTaskFromOptionallyAsyncTask(Runnable task) {
      if (Bukkit.isPrimaryThread()) {
         this.runTask(task);
      } else {
         this.scheduleSyncDelayedTask(task);
      }

   }

   public void runTask(Runnable task) {
      if (isFolia) {
         AuthMe.getScheduler().runTask(task);
      } else {
         Bukkit.getScheduler().runTask(this.authMe, task);
      }

   }

   public void runTask(Entity entity, Runnable task) {
      if (isFolia) {
         AuthMe.getScheduler().runTask(entity, task);
      } else {
         Bukkit.getScheduler().runTask(this.authMe, task);
      }

   }

   public void runTask(Location location, Runnable task) {
      AuthMe.getScheduler().runTask(location, task);
   }

   public void runTaskIfFolia(Runnable task) {
      if (isFolia) {
         this.runTask(task);
      } else {
         task.run();
      }

   }

   public void runTaskIfFolia(Entity entity, Runnable task) {
      if (isFolia) {
         this.runTask(entity, task);
      } else {
         task.run();
      }

   }

   public void runTaskIfFolia(Location location, Runnable task) {
      if (isFolia) {
         this.runTask(location, task);
      } else {
         task.run();
      }

   }

   public MyScheduledTask runTaskLater(Runnable task, long delay) {
      return AuthMe.getScheduler().runTaskLater(task, delay);
   }

   public MyScheduledTask runTaskLater(Entity entity, Runnable task, long delay) {
      return AuthMe.getScheduler().runTaskLater(entity, task, delay);
   }

   public void runTaskOptionallyAsync(Runnable task) {
      if (this.useAsyncTasks) {
         this.runTaskAsynchronously(task);
      } else {
         this.runTask(task);
      }

   }

   public void runTaskAsynchronously(Runnable task) {
      if (isFolia) {
         AuthMe.getScheduler().runTaskAsynchronously(task);
      } else {
         Bukkit.getScheduler().runTaskAsynchronously(this.authMe, task);
      }

   }

   public MyScheduledTask runTaskTimerAsynchronously(UniversalRunnable task, long delay, long period) {
      return task.runTaskTimerAsynchronously(this.authMe, delay, period);
   }

   public MyScheduledTask runTaskTimer(UniversalRunnable task, long delay, long period) {
      return task.runTaskTimer(this.authMe, delay, period);
   }

   public int broadcastMessage(String message) {
      return Bukkit.broadcastMessage(message);
   }

   public Player getPlayerExact(String name) {
      return this.authMe.getServer().getPlayerExact(name);
   }

   public OfflinePlayer getOfflinePlayer(String name) {
      return this.authMe.getServer().getOfflinePlayer(name);
   }

   public Set<OfflinePlayer> getBannedPlayers() {
      return Bukkit.getBannedPlayers();
   }

   public OfflinePlayer[] getOfflinePlayers() {
      return Bukkit.getOfflinePlayers();
   }

   public Collection<Player> getOnlinePlayers() {
      return Bukkit.getOnlinePlayers();
   }

   public void callEvent(Event event) {
      Bukkit.getPluginManager().callEvent(event);
   }

   public <E extends Event> E createAndCallEvent(Function<Boolean, E> eventSupplier) {
      E event = (Event)eventSupplier.apply(this.useAsyncTasks);
      this.callEvent(event);
      return event;
   }

   public PotionEffect createBlindnessEffect(int timeoutInTicks) {
      return new PotionEffect(PotionEffectType.BLINDNESS, timeoutInTicks, 2);
   }

   public World getWorld(String name) {
      return Bukkit.getWorld(name);
   }

   public boolean dispatchCommand(CommandSender sender, String commandLine) {
      return Bukkit.dispatchCommand(sender, commandLine);
   }

   public boolean dispatchConsoleCommand(String commandLine) {
      return Bukkit.dispatchCommand(Bukkit.getConsoleSender(), commandLine);
   }

   public void reload(Settings settings) {
      this.useAsyncTasks = (Boolean)settings.getProperty(PluginSettings.USE_ASYNC_TASKS);
   }

   public void sendBungeeMessage(Player player, byte[] bytes) {
      player.sendPluginMessage(this.authMe, "BungeeCord", bytes);
   }

   public void sendVelocityMessage(Player player, byte[] bytes) {
      if (player != null) {
         player.sendPluginMessage(this.authMe, "authmevelocity:main", bytes);
      } else {
         Bukkit.getServer().sendPluginMessage(this.authMe, "authmevelocity:main", bytes);
      }

   }

   public BanEntry banIp(String ip, String reason, Date expires, String source) {
      return Bukkit.getServer().getBanList(Type.IP).addBan(ip, reason, expires, source);
   }

   public Optional<Boolean> isBungeeCordConfiguredForSpigot() {
      try {
         YamlConfiguration spigotConfig = Bukkit.spigot().getConfig();
         return Optional.of(spigotConfig.getBoolean("settings.bungeecord"));
      } catch (NoSuchMethodError var2) {
         return Optional.empty();
      }
   }

   public String getIp() {
      return Bukkit.getServer().getIp();
   }

   static {
      isFolia = UniversalScheduler.isFolia;
   }
}
