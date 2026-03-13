package tntrun.arena.handlers;

import java.util.Iterator;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import tntrun.TNTRun;
import tntrun.arena.Arena;
import tntrun.utils.FormattingCodesParser;

public class ScoreboardHandler {
   private final TNTRun plugin;
   private int playingtask;
   private Arena arena;

   public ScoreboardHandler(TNTRun plugin, Arena arena) {
      this.plugin = plugin;
      this.arena = arena;
   }

   public void createWaitingScoreBoard() {
      if (this.plugin.getConfig().getBoolean("special.UseScoreboard")) {
         Iterator var2 = this.arena.getPlayersManager().getAllParticipantsCopy().iterator();

         while(var2.hasNext()) {
            Player player = (Player)var2.next();
            this.updateWaitingScoreboard(player);
         }

      }
   }

   public void updateWaitingScoreboard(Player player) {
      Scoreboard scoreboard = this.plugin.getScoreboardManager().resetScoreboard(player);
      Objective o = scoreboard.getObjective(DisplaySlot.SIDEBAR);
      int size = this.plugin.getConfig().getStringList("scoreboard.waiting").size();

      for(Iterator var6 = this.plugin.getConfig().getStringList("scoreboard.waiting").iterator(); var6.hasNext(); --size) {
         String s = (String)var6.next();
         s = this.plugin.getScoreboardManager().getPlaceholderString(s, player);
         s = FormattingCodesParser.parseFormattingCodes(s).replace("{ARENA}", this.arena.getArenaName());
         s = s.replace("{PS}", this.arena.getPlayersManager().getPlayersCount().makeConcatWithConstants<invokedynamic>(this.arena.getPlayersManager().getPlayersCount()));
         s = s.replace("{MPS}", this.arena.getStructureManager().getMaxPlayers().makeConcatWithConstants<invokedynamic>(this.arena.getStructureManager().getMaxPlayers()));
         s = s.replace("{COUNT}", this.arena.getGameHandler().count.makeConcatWithConstants<invokedynamic>(this.arena.getGameHandler().count));
         s = s.replace("{VOTES}", this.arena.getPlayerHandler().getVotesRequired(this.arena).makeConcatWithConstants<invokedynamic>(this.arena.getPlayerHandler().getVotesRequired(this.arena)));
         s = s.replace("{DJ}", this.arena.getPlayerHandler().getDoubleJumps(player.getName()).makeConcatWithConstants<invokedynamic>(this.arena.getPlayerHandler().getDoubleJumps(player.getName())));
         s = s.replace("{MIN}", String.valueOf(this.arena.getStructureManager().getMinPlayers()));
         o.getScore(this.plugin.getScoreboardManager().getTeamEntry(scoreboard, size, s)).setScore(size);
      }

      player.setScoreboard(scoreboard);
   }

   public void removeScoreboard(Player player) {
      if (this.plugin.getConfig().getBoolean("special.UseScoreboard")) {
         this.plugin.getScoreboardManager().removeScoreboardFromMap(player);
         player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
      }
   }

   public void createPlayingScoreBoard() {
      if (this.plugin.getConfig().getBoolean("special.UseScoreboard")) {
         this.playingtask = Bukkit.getScheduler().scheduleSyncRepeatingTask(this.plugin, new Runnable() {
            public void run() {
               Iterator var2 = ScoreboardHandler.this.arena.getPlayersManager().getPlayers().iterator();

               Player player;
               while(var2.hasNext()) {
                  player = (Player)var2.next();
                  ScoreboardHandler.this.updatePlayingScoreboard(player);
               }

               if (!ScoreboardHandler.this.plugin.getConfig().getBoolean("scoreboard.removefromspectators")) {
                  var2 = ScoreboardHandler.this.arena.getPlayersManager().getSpectators().iterator();

                  while(var2.hasNext()) {
                     player = (Player)var2.next();
                     ScoreboardHandler.this.updatePlayingScoreboard(player);
                  }
               }

            }
         }, 0L, 20L);
      }
   }

   private void updatePlayingScoreboard(Player player) {
      Scoreboard scoreboard = this.plugin.getScoreboardManager().resetScoreboard(player);
      Objective o = scoreboard.getObjective(DisplaySlot.SIDEBAR);
      int size = this.plugin.getConfig().getStringList("scoreboard.playing").size();

      for(Iterator var6 = this.plugin.getConfig().getStringList("scoreboard.playing").iterator(); var6.hasNext(); --size) {
         String s = (String)var6.next();
         s = this.plugin.getScoreboardManager().getPlaceholderString(s, player);
         s = FormattingCodesParser.parseFormattingCodes(s).replace("{ARENA}", this.arena.getArenaName());
         s = s.replace("{PS}", this.arena.getPlayersManager().getPlayersCount().makeConcatWithConstants<invokedynamic>(this.arena.getPlayersManager().getPlayersCount()));
         s = s.replace("{MPS}", this.arena.getStructureManager().getMaxPlayers().makeConcatWithConstants<invokedynamic>(this.arena.getStructureManager().getMaxPlayers()));
         s = s.replace("{LOST}", this.arena.getGameHandler().lostPlayers.makeConcatWithConstants<invokedynamic>(this.arena.getGameHandler().lostPlayers));
         int var10002 = this.arena.getGameHandler().getTimeRemaining();
         s = s.replace("{LIMIT}", (var10002 / 20).makeConcatWithConstants<invokedynamic>(var10002 / 20));
         s = s.replace("{DJ}", this.arena.getPlayerHandler().getDoubleJumps(player.getName()).makeConcatWithConstants<invokedynamic>(this.arena.getPlayerHandler().getDoubleJumps(player.getName())));
         s = s.replace("{MIN}", String.valueOf(this.arena.getStructureManager().getMinPlayers()));
         o.getScore(this.plugin.getScoreboardManager().getTeamEntry(scoreboard, size, s)).setScore(size);
      }

   }

   public int getPlayingTask() {
      return this.playingtask;
   }
}
