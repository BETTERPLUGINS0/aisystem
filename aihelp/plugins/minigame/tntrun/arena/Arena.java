package tntrun.arena;

import java.io.File;
import tntrun.TNTRun;
import tntrun.arena.handlers.ArenaEconomy;
import tntrun.arena.handlers.GameHandler;
import tntrun.arena.handlers.PlayerHandler;
import tntrun.arena.handlers.ScoreboardHandler;
import tntrun.arena.status.PlayersManager;
import tntrun.arena.status.StatusManager;
import tntrun.arena.structure.StructureManager;

public class Arena {
   public TNTRun plugin;
   private String arenaname;
   private ArenaEconomy arenaeco;
   private File arenafile;
   private GameHandler arenagh;
   private PlayerHandler arenaph;
   private ScoreboardHandler arenasb;
   private StatusManager statusManager = new StatusManager(this);
   private StructureManager structureManager = new StructureManager(this);
   private PlayersManager playersManager = new PlayersManager();

   public Arena(String name, TNTRun plugin) {
      this.arenaname = name;
      this.plugin = plugin;
      this.arenagh = new GameHandler(plugin, this);
      this.arenaph = new PlayerHandler(plugin, this);
      String var10003 = String.valueOf(plugin.getDataFolder());
      this.arenafile = new File(var10003 + File.separator + "arenas" + File.separator + this.arenaname + ".yml");
      this.arenasb = new ScoreboardHandler(plugin, this);
      this.arenaeco = new ArenaEconomy(plugin, this);
   }

   public String getArenaName() {
      return this.arenaname;
   }

   public File getArenaFile() {
      return this.arenafile;
   }

   public GameHandler getGameHandler() {
      return this.arenagh;
   }

   public PlayerHandler getPlayerHandler() {
      return this.arenaph;
   }

   public ScoreboardHandler getScoreboardHandler() {
      return this.arenasb;
   }

   public StatusManager getStatusManager() {
      return this.statusManager;
   }

   public StructureManager getStructureManager() {
      return this.structureManager;
   }

   public PlayersManager getPlayersManager() {
      return this.playersManager;
   }

   public ArenaEconomy getArenaEconomy() {
      return this.arenaeco;
   }
}
