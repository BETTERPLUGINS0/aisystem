package tntrun.commands.setup;

import java.util.Arrays;
import java.util.HashMap;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import tntrun.TNTRun;
import tntrun.commands.setup.arena.AddSpawn;
import tntrun.commands.setup.arena.Configure;
import tntrun.commands.setup.arena.CreateArena;
import tntrun.commands.setup.arena.DeleteArena;
import tntrun.commands.setup.arena.DeleteSpawnPoints;
import tntrun.commands.setup.arena.DeleteSpectatorSpawn;
import tntrun.commands.setup.arena.DisableArena;
import tntrun.commands.setup.arena.DisableKits;
import tntrun.commands.setup.arena.EnableArena;
import tntrun.commands.setup.arena.EnableKits;
import tntrun.commands.setup.arena.FinishArena;
import tntrun.commands.setup.arena.SetArena;
import tntrun.commands.setup.arena.SetBarColor;
import tntrun.commands.setup.arena.SetCountdown;
import tntrun.commands.setup.arena.SetCurrency;
import tntrun.commands.setup.arena.SetDamage;
import tntrun.commands.setup.arena.SetFee;
import tntrun.commands.setup.arena.SetGameLevelDestroyDelay;
import tntrun.commands.setup.arena.SetLoseLevel;
import tntrun.commands.setup.arena.SetMaxPlayers;
import tntrun.commands.setup.arena.SetMinPlayers;
import tntrun.commands.setup.arena.SetMoneyRewards;
import tntrun.commands.setup.arena.SetRegenerationDelay;
import tntrun.commands.setup.arena.SetReward;
import tntrun.commands.setup.arena.SetSpawn;
import tntrun.commands.setup.arena.SetSpectatorSpawn;
import tntrun.commands.setup.arena.SetTeleport;
import tntrun.commands.setup.arena.SetTimeLimit;
import tntrun.commands.setup.arena.SetVotePercent;
import tntrun.commands.setup.arena.SetupHelp;
import tntrun.commands.setup.kits.AddKit;
import tntrun.commands.setup.kits.DeleteKit;
import tntrun.commands.setup.kits.LinkKit;
import tntrun.commands.setup.kits.UnlinkKit;
import tntrun.commands.setup.lobby.DeleteLobby;
import tntrun.commands.setup.lobby.SetLobby;
import tntrun.commands.setup.other.AddToWhitelist;
import tntrun.commands.setup.other.ForceJoin;
import tntrun.commands.setup.other.GiveDoubleJumps;
import tntrun.commands.setup.other.ResetCachedRank;
import tntrun.commands.setup.other.ResetStats;
import tntrun.commands.setup.other.SetLanguage;
import tntrun.commands.setup.reload.Migrate;
import tntrun.commands.setup.reload.ReloadBars;
import tntrun.commands.setup.reload.ReloadConfig;
import tntrun.commands.setup.reload.ReloadMSG;
import tntrun.commands.setup.reload.ReloadTitles;
import tntrun.commands.setup.selection.Clear;
import tntrun.commands.setup.selection.SetP1;
import tntrun.commands.setup.selection.SetP2;
import tntrun.messages.Messages;
import tntrun.selectionget.PlayerSelection;

public class SetupCommandsHandler implements CommandExecutor {
   private PlayerSelection plselection = new PlayerSelection();
   private HashMap<String, CommandHandlerInterface> commandHandlers = new HashMap();

   public SetupCommandsHandler(TNTRun plugin) {
      this.commandHandlers.put("setp1", new SetP1(this.plselection));
      this.commandHandlers.put("setp2", new SetP2(this.plselection));
      this.commandHandlers.put("clear", new Clear(this.plselection));
      this.commandHandlers.put("setlobby", new SetLobby(plugin));
      this.commandHandlers.put("deletelobby", new DeleteLobby(plugin));
      this.commandHandlers.put("reloadmsg", new ReloadMSG(plugin));
      this.commandHandlers.put("reloadbars", new ReloadBars(plugin));
      this.commandHandlers.put("reloadconfig", new ReloadConfig(plugin));
      this.commandHandlers.put("reloadtitles", new ReloadTitles(plugin));
      this.commandHandlers.put("create", new CreateArena(plugin));
      this.commandHandlers.put("delete", new DeleteArena(plugin));
      this.commandHandlers.put("setarena", new SetArena(plugin, this.plselection));
      this.commandHandlers.put("setgameleveldestroydelay", new SetGameLevelDestroyDelay(plugin));
      this.commandHandlers.put("setregenerationdelay", new SetRegenerationDelay(plugin));
      this.commandHandlers.put("setloselevel", new SetLoseLevel(plugin));
      this.commandHandlers.put("setspawn", new SetSpawn(plugin));
      this.commandHandlers.put("addspawn", new AddSpawn(plugin));
      this.commandHandlers.put("deletespawnpoints", new DeleteSpawnPoints(plugin));
      this.commandHandlers.put("setspectate", new SetSpectatorSpawn(plugin));
      this.commandHandlers.put("deletespectate", new DeleteSpectatorSpawn(plugin));
      this.commandHandlers.put("setmaxplayers", new SetMaxPlayers(plugin));
      this.commandHandlers.put("setminplayers", new SetMinPlayers(plugin));
      this.commandHandlers.put("setvotepercent", new SetVotePercent(plugin));
      this.commandHandlers.put("setcountdown", new SetCountdown(plugin));
      this.commandHandlers.put("setmoneyreward", new SetMoneyRewards(plugin));
      this.commandHandlers.put("settimelimit", new SetTimeLimit(plugin));
      this.commandHandlers.put("setteleport", new SetTeleport(plugin));
      this.commandHandlers.put("setdamage", new SetDamage(plugin));
      this.commandHandlers.put("finish", new FinishArena(plugin));
      this.commandHandlers.put("disable", new DisableArena(plugin));
      this.commandHandlers.put("enable", new EnableArena(plugin));
      this.commandHandlers.put("enablekits", new EnableKits(plugin));
      this.commandHandlers.put("disablekits", new DisableKits(plugin));
      this.commandHandlers.put("addkit", new AddKit(plugin));
      this.commandHandlers.put("deletekit", new DeleteKit(plugin));
      this.commandHandlers.put("linkkit", new LinkKit(plugin));
      this.commandHandlers.put("unlinkkit", new UnlinkKit(plugin));
      this.commandHandlers.put("setbarcolor", new SetBarColor(plugin));
      this.commandHandlers.put("setbarcolour", new SetBarColor(plugin));
      this.commandHandlers.put("setreward", new SetReward(plugin));
      this.commandHandlers.put("setfee", new SetFee(plugin));
      this.commandHandlers.put("setcurrency", new SetCurrency(plugin));
      this.commandHandlers.put("setlanguage", new SetLanguage(plugin));
      this.commandHandlers.put("addtowhitelist", new AddToWhitelist(plugin));
      this.commandHandlers.put("configure", new Configure(plugin));
      this.commandHandlers.put("migrate", new Migrate(plugin));
      this.commandHandlers.put("help", new SetupHelp(plugin));
      this.commandHandlers.put("resetstats", new ResetStats(plugin));
      this.commandHandlers.put("resetcachedrank", new ResetCachedRank(plugin));
      this.commandHandlers.put("givedoublejumps", new GiveDoubleJumps(plugin));
      this.commandHandlers.put("forcejoin", new ForceJoin(plugin));
   }

   public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
      if (!(sender instanceof Player)) {
         sender.sendMessage("Player is expected");
         return true;
      } else {
         Player player = (Player)sender;
         if (!player.hasPermission("tntrun.setup")) {
            Messages.sendMessage(player, Messages.nopermission);
            return true;
         } else if (args.length > 0 && this.commandHandlers.containsKey(args[0])) {
            CommandHandlerInterface commandh = (CommandHandlerInterface)this.commandHandlers.get(args[0]);
            if (args.length - 1 < commandh.getMinArgsLength()) {
               Messages.sendMessage(player, "&c ERROR: Please use &6/tr cmds&c to view required arguments for all game commands");
               return false;
            } else {
               boolean result = commandh.handleCommand(player, (String[])Arrays.copyOfRange(args, 1, args.length));
               return result;
            }
         } else {
            Messages.sendMessage(player, "&c ERROR: Please use &6/tr cmds&c to view all valid game commands");
            return false;
         }
      }
   }
}
