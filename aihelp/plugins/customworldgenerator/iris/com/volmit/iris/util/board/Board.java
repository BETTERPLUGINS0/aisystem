package com.volmit.iris.util.board;

import com.volmit.iris.util.format.C;
import java.util.Collections;
import java.util.List;
import java.util.function.UnaryOperator;
import java.util.stream.IntStream;
import lombok.Generated;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Criteria;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class Board {
   private static final String[] CACHED_ENTRIES = new String[C.values().length];
   private static final UnaryOperator<String> APPLY_COLOR_TRANSLATION = (var0) -> {
      return C.translateAlternateColorCodes('&', var0);
   };
   private final Player player;
   private final Objective objective;
   private BoardSettings boardSettings;
   private boolean ready;

   public Board(@NonNull final Player player, final BoardSettings boardSettings) {
      if (var1 == null) {
         throw new NullPointerException("player is marked non-null but is null");
      } else {
         this.player = var1;
         this.boardSettings = var2;
         Objective var3 = this.getScoreboard().getObjective("board");
         this.objective = var3 == null ? this.getScoreboard().registerNewObjective("board", Criteria.DUMMY, "Iris") : var3;
         this.objective.setDisplaySlot(DisplaySlot.SIDEBAR);
         Team var4 = this.getScoreboard().getTeam("board");
         var4 = var4 == null ? this.getScoreboard().registerNewTeam("board") : var4;
         var4.setAllowFriendlyFire(true);
         var4.setCanSeeFriendlyInvisibles(false);
         var4.setPrefix("");
         var4.setSuffix("");
         this.ready = true;
      }
   }

   public Scoreboard getScoreboard() {
      return this.player != null ? this.player.getScoreboard() : null;
   }

   public void remove() {
      this.resetScoreboard();
   }

   public void update() {
      if (this.ready) {
         if (!this.player.isOnline()) {
            this.remove();
         } else if (this.boardSettings != null) {
            List var1 = this.boardSettings.getBoardProvider().getLines(this.player);
            var1.replaceAll(APPLY_COLOR_TRANSLATION);
            if (this.boardSettings.getScoreDirection() == ScoreDirection.UP) {
               Collections.reverse(var1);
            }

            String var2 = this.boardSettings.getBoardProvider().getTitle(this.player);
            if (var2.length() > 32) {
               Bukkit.getLogger().warning("The title " + var2 + " is over 32 characters in length, substringing to prevent errors.");
               var2 = var2.substring(0, 32);
            }

            this.objective.setDisplayName(C.translateAlternateColorCodes('&', var2));
            if (this.getScoreboard().getEntries().size() != var1.size()) {
               this.getScoreboard().getEntries().forEach(this::removeEntry);
            }

            for(int var3 = 0; var3 < var1.size(); ++var3) {
               String var4 = (String)var1.get(var3);
               BoardEntry var5 = BoardEntry.translateToEntry(var4);
               Team var6 = this.getScoreboard().getTeam(CACHED_ENTRIES[var3]);
               if (var6 == null) {
                  var6 = this.getScoreboard().registerNewTeam(CACHED_ENTRIES[var3]);
                  var6.addEntry(var6.getName());
               }

               var6.setPrefix(var5.getPrefix());
               var6.setSuffix(var5.getSuffix());
               switch(this.boardSettings.getScoreDirection()) {
               case UP:
                  this.objective.getScore(var6.getName()).setScore(1 + var3);
                  break;
               case DOWN:
                  this.objective.getScore(var6.getName()).setScore(15 - var3);
               }
            }

         }
      }
   }

   public void removeEntry(String id) {
      this.getScoreboard().resetScores(var1);
   }

   public void resetScoreboard() {
      this.ready = false;
      this.player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
   }

   @Generated
   public void setBoardSettings(final BoardSettings boardSettings) {
      this.boardSettings = var1;
   }

   static {
      IntStream.range(0, 15).forEach((var0) -> {
         String[] var10000 = CACHED_ENTRIES;
         String var10002 = C.values()[var0].toString();
         var10000[var0] = var10002 + String.valueOf(C.RESET);
      });
   }
}
