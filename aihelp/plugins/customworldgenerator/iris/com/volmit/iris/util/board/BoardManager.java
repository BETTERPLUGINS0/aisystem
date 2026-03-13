package com.volmit.iris.util.board;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

public class BoardManager {
   private final JavaPlugin plugin;
   private final Map<UUID, Board> scoreboards;
   private final BukkitTask updateTask;
   private BoardSettings boardSettings;

   public BoardManager(JavaPlugin plugin, BoardSettings boardSettings) {
      this.plugin = var1;
      this.boardSettings = var2;
      this.scoreboards = new ConcurrentHashMap();
      this.updateTask = (new BoardUpdateTask(this)).runTaskTimer(var1, 2L, 20L);
      var1.getServer().getOnlinePlayers().forEach(this::setup);
   }

   public void setBoardSettings(BoardSettings boardSettings) {
      this.boardSettings = var1;
      this.scoreboards.values().forEach((var1x) -> {
         var1x.setBoardSettings(var1);
      });
   }

   public boolean hasBoard(Player player) {
      return this.scoreboards.containsKey(var1.getUniqueId());
   }

   public Optional<Board> getBoard(Player player) {
      return Optional.ofNullable((Board)this.scoreboards.get(var1.getUniqueId()));
   }

   public void setup(Player player) {
      Optional.ofNullable((Board)this.scoreboards.remove(var1.getUniqueId())).ifPresent(Board::resetScoreboard);
      if (var1.getScoreboard().equals(Bukkit.getScoreboardManager().getMainScoreboard())) {
         var1.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
      }

      this.scoreboards.put(var1.getUniqueId(), new Board(var1, this.boardSettings));
   }

   public void remove(Player player) {
      Optional.ofNullable((Board)this.scoreboards.remove(var1.getUniqueId())).ifPresent(Board::remove);
   }

   public Map<UUID, Board> getScoreboards() {
      return Collections.unmodifiableMap(this.scoreboards);
   }

   public void onDisable() {
      this.updateTask.cancel();
      this.plugin.getServer().getOnlinePlayers().forEach(this::remove);
      this.scoreboards.clear();
   }
}
