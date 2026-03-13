package tntrun.datahandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import me.clip.placeholderapi.PlaceholderAPI;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Criteria;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.bukkit.scoreboard.Team.Option;
import org.bukkit.scoreboard.Team.OptionStatus;
import tntrun.TNTRun;
import tntrun.utils.FormattingCodesParser;

public class ScoreboardManager {
   private TNTRun plugin;
   private final String PLUGIN_NAME = "TNTRun";
   private HashSet<String> lobbyScoreboards = new HashSet();
   private Map<String, Scoreboard> scoreboardMap = new HashMap();
   private Map<String, Scoreboard> prejoinScoreboards = new HashMap();
   private List<String> codes = List.of(new String[]{"a", "b", "c", "d", "e", "f", "1", "2", "3", "4", "5", "6", "7", "8", "9"});

   public ScoreboardManager(TNTRun plugin) {
      this.plugin = plugin;
      this.updateScoreboardList();
   }

   private void updateScoreboardList() {
      if (this.plugin.getConfig().getBoolean("scoreboard.displaydoublejumps")) {
         List<String> ps = this.plugin.getConfig().getStringList("scoreboard.playing");
         if (ps.stream().noneMatch((s) -> {
            return s.contains("{DJ}");
         })) {
            ps.add("&e ");
            ps.add("&fDouble Jumps: &6&l{DJ}");
            this.plugin.getConfig().set("scoreboard.playing", ps);
            this.plugin.saveConfig();
         }

         List<String> ws = this.plugin.getConfig().getStringList("scoreboard.waiting");
         if (ws.stream().noneMatch((s) -> {
            return s.contains("{DJ}");
         })) {
            ws.add("&e ");
            ws.add("&fDouble Jumps: &6&l{DJ}");
            this.plugin.getConfig().set("scoreboard.waiting", ws);
            this.plugin.saveConfig();
         }

      }
   }

   private Scoreboard buildScoreboard() {
      FileConfiguration config = this.plugin.getConfig();
      Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
      if (config.getBoolean("special.UseScoreboard")) {
         Objective o = scoreboard.registerNewObjective("TNTRun", Criteria.DUMMY, "TNTRun");
         o.setDisplaySlot(DisplaySlot.SIDEBAR);
         String var10002 = ChatColor.GOLD.toString();
         String header = FormattingCodesParser.parseFormattingCodes(config.getString("scoreboard.header", var10002 + String.valueOf(ChatColor.BOLD) + "TNTRUN"));
         o.setDisplayName(header);
      }

      return scoreboard;
   }

   private Scoreboard getPlayerScoreboard(Player player) {
      if (this.scoreboardMap.containsKey(player.getName())) {
         return (Scoreboard)this.scoreboardMap.get(player.getName());
      } else {
         Scoreboard scoreboard = this.buildScoreboard();
         if (this.plugin.getConfig().getBoolean("disablecollisions")) {
            Team team = scoreboard.registerNewTeam("TNTRun");
            team.setOption(Option.COLLISION_RULE, OptionStatus.NEVER);
            team.addEntry(player.getName());
         }

         this.scoreboardMap.put(player.getName(), scoreboard);
         return scoreboard;
      }
   }

   private boolean isPlaceholderString(String s) {
      return StringUtils.substringBetween(s, "%") != null && !StringUtils.substringBetween(s, "%").isEmpty();
   }

   public String getPlaceholderString(String s, OfflinePlayer player) {
      if (this.plugin.isPlaceholderAPI() && this.isPlaceholderString(s)) {
         String[] parts = s.split("%");
         parts[1] = PlaceholderAPI.setPlaceholders(player, "%" + parts[1] + "%");
         StringJoiner value = new StringJoiner("");
         String[] var5 = parts;
         int var6 = parts.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            String ss = var5[var7];
            value.add(ss);
         }

         return value.toString();
      } else {
         return s;
      }
   }

   public Scoreboard resetScoreboard(Player player) {
      Scoreboard scoreboard = this.getPlayerScoreboard(player);
      Iterator var3 = (new ArrayList(scoreboard.getEntries())).iterator();

      while(var3.hasNext()) {
         String entry = (String)var3.next();
         scoreboard.resetScores(entry);
      }

      this.scoreboardMap.put(player.getName(), scoreboard);
      return scoreboard;
   }

   public void removeScoreboardFromMap(Player player) {
      this.scoreboardMap.remove(player.getName());
   }

   public void storePrejoinScoreboard(Player player) {
      this.prejoinScoreboards.putIfAbsent(player.getName(), player.getScoreboard());
   }

   public void restorePrejoinScoreboard(Player player) {
      if (this.prejoinScoreboards.get(player.getName()) != null) {
         player.setScoreboard((Scoreboard)this.prejoinScoreboards.remove(player.getName()));
      }

   }

   public boolean hasLobbyScoreboard(Player player) {
      return this.lobbyScoreboards.contains(player.getName());
   }

   public void addLobbyScoreboard(String playerName) {
      this.lobbyScoreboards.add(playerName);
   }

   public void removeLobbyScoreboard(String playerName) {
      this.lobbyScoreboards.remove(playerName);
   }

   public String getTeamEntry(Scoreboard scoreboard, int size, String s) {
      Object var10001 = this.codes.get(size);
      String entry = ChatColor.translateAlternateColorCodes('&', "&" + (String)var10001);
      String teamname = "t" + size;
      Team team = scoreboard.getTeam(teamname);
      if (team == null) {
         team = scoreboard.registerNewTeam(teamname);
      }

      team.setSuffix(s);
      team.addEntry(entry);
      return entry;
   }
}
