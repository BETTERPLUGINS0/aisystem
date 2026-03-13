package tntrun.arena.structure;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.util.Vector;
import tntrun.arena.Arena;
import tntrun.utils.Utils;

public class StructureManager {
   private Arena arena;
   private GameZone gamezone;
   private String world;
   private Vector p1 = null;
   private Vector p2 = null;
   private int gameleveldestroydelay = 8;
   private LoseLevel loselevel = new LoseLevel();
   private SpectatorSpawn spectatorspawn = new SpectatorSpawn();
   private PlayerSpawn playerspawn = new PlayerSpawn();
   private int minPlayers = 2;
   private int maxPlayers = 15;
   private double votesPercent = 0.75D;
   private int timelimit = 300;
   private int countdown = 10;
   private int startVisibleCountdown = 10;
   private Rewards rewards = new Rewards();
   private StructureManager.TeleportDestination teleportDest;
   private StructureManager.DamageEnabled damageEnabled;
   private boolean punchDamage;
   private boolean kitsEnabled;
   private boolean linkedRandom;
   private List<String> linkedKits;
   private boolean testmode;
   private boolean excludeStats;
   private boolean statsEnabled;
   private int statsMinPlayers;
   private boolean allowDoublejumps;
   private int regenerationdelay;
   private String currency;
   private double fee;
   private boolean finished;
   private List<Vector> additionalSpawnPoints;
   private List<Vector> freeSpawnList;
   private List<String> commandsOnStart;
   private List<String> commandsOnStop;
   private boolean shopEnabled;
   private int maxFinalPositions;
   private boolean enableOnRestart;
   private String menuItem;
   private String menuPvpItem;

   public StructureManager(Arena arena) {
      this.teleportDest = StructureManager.TeleportDestination.PREVIOUS;
      this.damageEnabled = StructureManager.DamageEnabled.NO;
      this.punchDamage = true;
      this.kitsEnabled = false;
      this.linkedRandom = true;
      this.linkedKits = new ArrayList();
      this.testmode = false;
      this.excludeStats = false;
      this.statsEnabled = true;
      this.statsMinPlayers = 0;
      this.allowDoublejumps = true;
      this.regenerationdelay = 60;
      this.fee = 0.0D;
      this.finished = false;
      this.additionalSpawnPoints = new ArrayList();
      this.freeSpawnList = new ArrayList();
      this.commandsOnStart = new ArrayList();
      this.commandsOnStop = new ArrayList();
      this.shopEnabled = true;
      this.maxFinalPositions = 3;
      this.enableOnRestart = true;
      this.arena = arena;
      this.gamezone = new GameZone(arena);
   }

   public String getWorldName() {
      return this.world;
   }

   public World getWorld() {
      return Bukkit.getWorld(this.world);
   }

   public Vector getP1() {
      return this.p1;
   }

   public Vector getP2() {
      return this.p2;
   }

   public GameZone getGameZone() {
      return this.gamezone;
   }

   public int getGameLevelDestroyDelay() {
      return this.gameleveldestroydelay;
   }

   public LoseLevel getLoseLevel() {
      return this.loselevel;
   }

   public Vector getSpectatorSpawnVector() {
      return this.spectatorspawn.getVector();
   }

   public Location getSpectatorSpawn() {
      return this.spectatorspawn.isConfigured() ? new Location(this.getWorld(), this.spectatorspawn.getVector().getX(), this.spectatorspawn.getVector().getY(), this.spectatorspawn.getVector().getZ(), this.spectatorspawn.getYaw(), this.spectatorspawn.getPitch()) : null;
   }

   public Vector getSpawnPointVector() {
      return this.playerspawn.getVector();
   }

   public Location getSpawnPoint() {
      Vector v = this.playerspawn.getVector();
      if (this.hasAdditionalSpawnPoints()) {
         v = this.nextSpawnPoint();
      }

      return new Location(this.getWorld(), v.getX(), v.getY(), v.getZ(), this.playerspawn.getYaw(), this.playerspawn.getPitch());
   }

   public Location getPrimarySpawnPoint() {
      return new Location(this.getWorld(), this.playerspawn.getVector().getX(), this.playerspawn.getVector().getY(), this.playerspawn.getVector().getZ());
   }

   public List<Vector> getAdditionalSpawnPoints() {
      return this.additionalSpawnPoints;
   }

   public int getMaxPlayers() {
      return this.maxPlayers;
   }

   public int getMinPlayers() {
      return this.minPlayers;
   }

   public double getVotePercent() {
      return this.votesPercent;
   }

   public int getTimeLimit() {
      return this.timelimit;
   }

   public int getCountdown() {
      return this.countdown;
   }

   public int getStartVisibleCountdown() {
      return this.startVisibleCountdown;
   }

   public Rewards getRewards() {
      return this.rewards;
   }

   public StructureManager.TeleportDestination getTeleportDestination() {
      return this.teleportDest;
   }

   public StructureManager.DamageEnabled getDamageEnabled() {
      return this.damageEnabled;
   }

   public boolean isKitsEnabled() {
      return this.kitsEnabled;
   }

   public boolean isPunchDamage() {
      return this.punchDamage;
   }

   public boolean isTestMode() {
      return this.testmode;
   }

   public boolean isArenaStatsEnabled() {
      return this.statsEnabled;
   }

   public int getStatsMinPlayers() {
      return this.statsMinPlayers;
   }

   public boolean isAllowDoublejumps() {
      return this.allowDoublejumps;
   }

   public boolean isShopEnabled() {
      return this.shopEnabled;
   }

   public int getRegenerationDelay() {
      return this.regenerationdelay;
   }

   public double getFee() {
      return this.fee;
   }

   public int getMaxFinalPositions() {
      return this.maxFinalPositions;
   }

   public Material getCurrency() {
      return Material.getMaterial(this.currency);
   }

   public boolean hasFee() {
      return this.fee > 0.0D;
   }

   public boolean isCurrencyEnabled() {
      return Material.getMaterial(this.currency) != null && !Material.getMaterial(this.currency).isAir();
   }

   public String getArenaCost() {
      return !this.isCurrencyEnabled() ? Utils.getFormattedCurrency(String.valueOf(this.fee)) : (int)this.fee + " x " + this.currency;
   }

   public List<String> getCommandsOnStart() {
      return this.commandsOnStart;
   }

   public List<String> getCommandsOnStop() {
      return this.commandsOnStop;
   }

   public boolean hasCommandsOnStart() {
      return !this.commandsOnStart.isEmpty();
   }

   public boolean hasCommandsOnStop() {
      return !this.commandsOnStop.isEmpty();
   }

   public String getMenuItem(boolean pvpEnabled) {
      return pvpEnabled ? this.menuPvpItem : this.menuItem;
   }

   public boolean hasMenuItem(boolean pvpEnabled) {
      return pvpEnabled ? !this.menuPvpItem.isEmpty() : !this.menuItem.isEmpty();
   }

   public boolean isInArenaBounds(Location loc) {
      return loc.toVector().isInAABB(this.getP1(), this.getP2());
   }

   public boolean isArenaBoundsSet() {
      return this.getP1() != null && this.getP2() != null && this.world != null;
   }

   public boolean isArenaConfigured() {
      return this.isArenaConfiguredString().equals("yes");
   }

   public String isArenaConfiguredString() {
      if (this.getP1() != null && this.getP2() != null && this.world != null) {
         if (!this.loselevel.isConfigured()) {
            return "Arena loselevel not set";
         } else {
            return !this.playerspawn.isConfigured() ? "Arena spawnpoint not set" : "yes";
         }
      } else {
         return "Arena bounds not set";
      }
   }

   public boolean isArenaFinished() {
      return this.finished;
   }

   public boolean isSpawnpointSet() {
      return this.playerspawn.isConfigured();
   }

   public boolean isSpectatorSpawnSet() {
      return this.spectatorspawn.isConfigured();
   }

   public boolean isPvpEnabled() {
      return !this.getDamageEnabled().toString().equalsIgnoreCase("no");
   }

   public boolean isEnableOnRestart() {
      return this.enableOnRestart;
   }

   public void setArenaFinished(boolean finished) {
      this.finished = finished;
   }

   public void setArenaPoints(Location loc1, Location loc2) {
      this.world = loc1.getWorld().getName();
      this.p1 = loc1.toVector();
      this.p2 = loc2.toVector();
   }

   public void setGameLevelDestroyDelay(int delay) {
      this.gameleveldestroydelay = delay;
   }

   public boolean setLoseLevel(Location loc1) {
      if (this.isInArenaBounds(loc1)) {
         this.loselevel.setLoseLocation(loc1);
         return true;
      } else {
         return false;
      }
   }

   public boolean setSpawnPoint(Location loc) {
      if (this.isInArenaBounds(loc)) {
         this.playerspawn.setPlayerSpawn(loc);
         return true;
      } else {
         return false;
      }
   }

   public boolean setSpectatorsSpawn(Location loc) {
      if (this.isInArenaBounds(loc)) {
         this.spectatorspawn.setSpectatorSpawn(loc);
         return true;
      } else {
         return false;
      }
   }

   public boolean addSpawnPoint(Location loc) {
      if (this.isInArenaBounds(loc)) {
         this.additionalSpawnPoints.add(loc.toVector());
         return true;
      } else {
         return false;
      }
   }

   public void removeSpectatorsSpawn() {
      this.spectatorspawn.remove();
   }

   public void removeAdditionalSpawnPoints() {
      this.additionalSpawnPoints.clear();
   }

   public void setMaxPlayers(int maxplayers) {
      this.maxPlayers = maxplayers;
   }

   public void setMinPlayers(int minplayers) {
      this.minPlayers = minplayers;
   }

   public void setVotePercent(double votepercent) {
      this.votesPercent = votepercent;
   }

   public void setTimeLimit(int timelimit) {
      this.timelimit = timelimit;
   }

   public void setCountdown(int countdown) {
      this.countdown = countdown;
   }

   public void setStartVisibleCountdown(int start) {
      this.startVisibleCountdown = start > 0 ? start : 0;
   }

   public void togglePunchDamage() {
      this.punchDamage = !this.punchDamage;
   }

   public void toggleTestMode() {
      this.testmode = !this.testmode;
   }

   public void toggleArenaStats() {
      this.statsEnabled = !this.statsEnabled;
   }

   public void toggleShopEnabled() {
      this.shopEnabled = !this.shopEnabled;
   }

   public void setStatsMinPlayers(int amount) {
      this.statsMinPlayers = amount > 0 ? amount : 0;
   }

   public void setTeleportDestination(StructureManager.TeleportDestination teleportDest) {
      this.teleportDest = teleportDest;
   }

   public void setDamageEnabled(StructureManager.DamageEnabled damageEnabled) {
      this.damageEnabled = damageEnabled;
   }

   public void enableKits(boolean kitsEnabled) {
      this.kitsEnabled = kitsEnabled;
   }

   public void linkKit(String kitName) {
      this.linkedKits.add(kitName);
   }

   public void unlinkKit(String kitName) {
      this.linkedKits.remove(kitName);
   }

   public List<String> getLinkedKits() {
      return this.linkedKits;
   }

   public boolean hasLinkedKits() {
      return this.linkedKits.size() > 0;
   }

   public boolean isRandomKit() {
      return this.linkedRandom;
   }

   public void setRegenerationDelay(int regendelay) {
      this.regenerationdelay = regendelay;
   }

   public void setFee(double fee) {
      this.fee = fee;
   }

   public void setMaxFinalPositions(int size) {
      this.maxFinalPositions = size > 0 ? size : 1;
   }

   public void setCurrency(Material currency) {
      if (currency.isAir()) {
         this.currency = null;
      } else {
         this.currency = currency.toString();
      }
   }

   public boolean hasAdditionalSpawnPoints() {
      return this.additionalSpawnPoints != null && !this.additionalSpawnPoints.isEmpty();
   }

   private Vector nextSpawnPoint() {
      if (this.freeSpawnList.isEmpty()) {
         this.freeSpawnList.add(this.playerspawn.getVector());
         this.freeSpawnList.addAll(this.additionalSpawnPoints);
      }

      return (Vector)this.freeSpawnList.remove(0);
   }

   public List<Vector> getFreeSpawnList() {
      return this.freeSpawnList;
   }

   public void saveToConfig() {
      YamlConfiguration config = new YamlConfiguration();

      try {
         config.set("world", this.world);
         config.set("p1", this.p1);
         config.set("p2", this.p2);
      } catch (Exception var7) {
      }

      config.set("gameleveldestroydelay", this.gameleveldestroydelay);

      try {
         this.loselevel.saveToConfig(config);
      } catch (Exception var6) {
      }

      try {
         this.playerspawn.saveToConfig(config);
      } catch (Exception var5) {
      }

      try {
         this.spectatorspawn.saveToConfig(config);
      } catch (Exception var4) {
      }

      config.set("maxPlayers", this.maxPlayers);
      config.set("minPlayers", this.minPlayers);
      config.set("votePercent", this.votesPercent);
      config.set("timelimit", this.timelimit);
      config.set("countdown", this.countdown);
      config.set("startVisibleCountdown", this.startVisibleCountdown);
      config.set("teleportto", this.teleportDest.toString());
      config.set("damageenabled", this.damageEnabled.toString());
      config.set("kits.enabled", this.kitsEnabled);
      config.set("kits.linked", this.linkedKits);
      config.set("kits.randomLinkedKit", this.linkedRandom);
      config.set("punchDamage", this.punchDamage);
      config.set("testmode", this.testmode);
      config.set("excludeStats", (Object)null);
      config.set("stats.enabled", this.statsEnabled);
      config.set("stats.minPlayers", this.statsMinPlayers);
      config.set("allowDoublejumps", this.allowDoublejumps);
      config.set("regenerationdelay", this.regenerationdelay);
      config.set("joinfee", this.fee);
      config.set("currency", this.currency);
      config.set("finished", this.finished);
      config.set("spawnpoints", this.additionalSpawnPoints);
      config.set("commandsOnStart", this.getCommandsOnStart());
      config.set("commandsOnStop", this.getCommandsOnStop());
      config.set("commandOnStart", (Object)null);
      config.set("commandOnStop", (Object)null);
      config.set("displayfinalpositions", this.maxFinalPositions);
      config.set("enableOnRestart", this.enableOnRestart);
      config.set("shop.enabled", this.shopEnabled);
      config.set("menu.item", this.menuItem);
      config.set("menu.pvpitem", this.menuPvpItem);
      this.rewards.saveToConfig(config);

      try {
         config.save(this.arena.getArenaFile());
      } catch (IOException var3) {
         var3.printStackTrace();
      }

   }

   public void loadFromConfig() {
      FileConfiguration config = YamlConfiguration.loadConfiguration(this.arena.getArenaFile());
      this.world = config.getString("world", (String)null);
      this.p1 = config.getVector("p1", (Vector)null);
      this.p2 = config.getVector("p2", (Vector)null);
      this.gameleveldestroydelay = config.getInt("gameleveldestroydelay", this.gameleveldestroydelay);
      this.loselevel.loadFromConfig(config);
      this.playerspawn.loadFromConfig(config);
      this.spectatorspawn.loadFromConfig(config);
      this.maxPlayers = config.getInt("maxPlayers", this.maxPlayers);
      this.minPlayers = config.getInt("minPlayers", this.minPlayers);
      this.votesPercent = config.getDouble("votePercent", this.votesPercent);
      this.timelimit = config.getInt("timelimit", this.timelimit);
      this.countdown = config.getInt("countdown", this.countdown);
      this.startVisibleCountdown = config.getInt("startVisibleCountdown", this.startVisibleCountdown);
      this.teleportDest = StructureManager.TeleportDestination.valueOf(config.getString("teleportto", StructureManager.TeleportDestination.PREVIOUS.toString()));
      this.damageEnabled = StructureManager.DamageEnabled.valueOf(config.getString("damageenabled", StructureManager.DamageEnabled.NO.toString()));
      this.rewards.loadFromConfig(config);
      this.kitsEnabled = config.getBoolean("kits.enabled");
      this.linkedKits = config.getStringList("kits.linked");
      this.linkedRandom = config.getBoolean("kits.randomLinkedKit", true);
      this.punchDamage = config.getBoolean("punchDamage", true);
      this.testmode = config.getBoolean("testmode");
      this.excludeStats = config.getBoolean("excludeStats");
      this.statsEnabled = this.excludeStats ? false : config.getBoolean("stats.enabled", true);
      this.statsMinPlayers = config.getInt("stats.minPlayers", this.statsMinPlayers);
      this.allowDoublejumps = config.getBoolean("allowDoublejumps", true);
      this.regenerationdelay = config.getInt("regenerationdelay", this.regenerationdelay);
      this.fee = config.getDouble("joinfee", this.fee);
      this.currency = config.getString("currency", (String)null);
      this.finished = config.getBoolean("finished");
      if (!this.finished && this.arena.getStructureManager().isArenaConfigured()) {
         this.finished = true;
      }

      this.additionalSpawnPoints = config.getList("spawnpoints", new ArrayList());
      if (config.isSet("commandOnStart")) {
         this.commandsOnStart = (List)(config.getString("commandOnStart", "").isEmpty() ? new ArrayList() : List.of(config.getString("commandOnStart")));
      } else {
         this.commandsOnStart = config.getStringList("commandsOnStart");
      }

      if (config.isSet("commandOnStop")) {
         this.commandsOnStop = (List)(config.getString("commandOnStop", "").isEmpty() ? new ArrayList() : List.of(config.getString("commandOnStop")));
      } else {
         this.commandsOnStop = config.getStringList("commandsOnStop");
      }

      this.maxFinalPositions = config.getInt("displayfinalpositions", this.maxFinalPositions);
      this.enableOnRestart = config.getBoolean("enableOnRestart", true);
      this.shopEnabled = config.getBoolean("shop.enabled", true);
      this.menuItem = config.getString("menu.item", "");
      this.menuPvpItem = config.getString("menu.pvpitem", "");
   }

   public static enum DamageEnabled {
      YES,
      ZERO,
      NO;
   }

   public static enum TeleportDestination {
      PREVIOUS,
      LOBBY;
   }
}
