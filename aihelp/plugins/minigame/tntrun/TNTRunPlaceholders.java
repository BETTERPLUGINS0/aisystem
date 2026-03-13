package tntrun;

import java.util.HashSet;
import java.util.StringJoiner;
import java.util.stream.Stream;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import tntrun.arena.Arena;
import tntrun.messages.Messages;
import tntrun.utils.FormattingCodesParser;
import tntrun.utils.Utils;

public class TNTRunPlaceholders extends PlaceholderExpansion {
   private final TNTRun plugin;

   public TNTRunPlaceholders(TNTRun plugin) {
      this.plugin = plugin;
   }

   public boolean canRegister() {
      return true;
   }

   public boolean persist() {
      return true;
   }

   public String getAuthor() {
      return this.plugin.getDescription().getAuthors().toString();
   }

   public String getVersion() {
      return this.plugin.getDescription().getVersion();
   }

   public String getIdentifier() {
      return "tntrun";
   }

   public String onRequest(OfflinePlayer p, String identifier) {
      if (identifier.equals("version")) {
         return String.valueOf(this.plugin.getDescription().getVersion());
      } else if (identifier.equals("arena_count")) {
         return String.valueOf(this.plugin.amanager.getArenas().size());
      } else if (identifier.equals("pvp_arena_count")) {
         return String.valueOf(this.plugin.amanager.getPvpArenas().size());
      } else if (identifier.equals("nopvp_arena_count")) {
         return String.valueOf(this.plugin.amanager.getNonPvpArenas().size());
      } else if (identifier.equals("player_count")) {
         return String.valueOf(Utils.playerCount());
      } else if (identifier.equals("spectator_count")) {
         return String.valueOf(Utils.spectatorCount());
      } else if (identifier.equals("pvp_player_count")) {
         return String.valueOf(Utils.pvpPlayerCount());
      } else if (identifier.equals("nopvp_player_count")) {
         return String.valueOf(Utils.nonPvpPlayerCount());
      } else {
         Arena arena;
         if (identifier.startsWith("allplayers")) {
            arena = this.getArenaFromPlaceholder(identifier, 2);
            return arena != null ? this.getNames(arena.getPlayersManager().getAllParticipantsCopy()) : null;
         } else if (identifier.startsWith("players")) {
            arena = this.getArenaFromPlaceholder(identifier, 2);
            return arena != null ? this.getNames(arena.getPlayersManager().getPlayersCopy()) : null;
         } else if (identifier.startsWith("spectators")) {
            arena = this.getArenaFromPlaceholder(identifier, 2);
            return arena != null ? this.getNames(arena.getPlayersManager().getSpectatorsCopy()) : null;
         } else if (identifier.startsWith("maxplayers")) {
            arena = this.getArenaFromPlaceholder(identifier, 2);
            return arena != null ? String.valueOf(arena.getStructureManager().getMaxPlayers()) : null;
         } else if (identifier.startsWith("minplayers")) {
            arena = this.getArenaFromPlaceholder(identifier, 2);
            return arena != null ? String.valueOf(arena.getStructureManager().getMinPlayers()) : null;
         } else if (identifier.startsWith("player_count")) {
            arena = this.getArenaFromPlaceholder(identifier, 3);
            return arena != null ? String.valueOf(arena.getPlayersManager().getPlayersCount()) : null;
         } else if (identifier.startsWith("spectator_count")) {
            arena = this.getArenaFromPlaceholder(identifier, 3);
            return arena != null ? String.valueOf(arena.getPlayersManager().getSpectatorsCount()) : null;
         } else if (identifier.startsWith("status")) {
            arena = this.getArenaFromPlaceholder(identifier, 2);
            return arena != null ? arena.getStatusManager().getArenaStatusMesssage() : null;
         } else if (identifier.startsWith("seconds_remaining")) {
            arena = this.getArenaFromPlaceholder(identifier, 3);
            return arena != null ? String.valueOf(arena.getGameHandler().getTimeRemaining() / 20) : null;
         } else if (identifier.startsWith("time_remaining")) {
            arena = this.getArenaFromPlaceholder(identifier, 3);
            return arena != null ? Utils.getFormattedTime(arena.getGameHandler().getTimeRemaining() / 20) : null;
         } else if (identifier.startsWith("pvp_status")) {
            arena = this.getArenaFromPlaceholder(identifier, 3);
            if (arena == null) {
               return null;
            } else {
               return arena.getStructureManager().isPvpEnabled() ? "Enabled" : "Disabled";
            }
         } else if (identifier.startsWith("damage_status")) {
            arena = this.getArenaFromPlaceholder(identifier, 3);
            return arena != null ? Utils.getTitleCase(arena.getStructureManager().getDamageEnabled().toString()) : null;
         } else if (identifier.startsWith("joinfee")) {
            arena = this.getArenaFromPlaceholder(identifier, 2);
            return arena != null ? String.valueOf(arena.getStructureManager().getFee()) : null;
         } else if (!identifier.startsWith("currency")) {
            if (!identifier.startsWith("leaderboard") && !identifier.startsWith("lb")) {
               if (p == null) {
                  return "";
               } else {
                  String uuid = this.plugin.getStats().getPlayerUUID(p);
                  if (identifier.equals("played")) {
                     return String.valueOf(this.plugin.getStats().getPlayedGames(uuid));
                  } else if (identifier.equals("wins")) {
                     return String.valueOf(this.plugin.getStats().getWins(uuid));
                  } else if (identifier.equals("losses")) {
                     return String.valueOf(this.plugin.getStats().getLosses(uuid));
                  } else if (identifier.equals("winstreak")) {
                     return String.valueOf(this.plugin.getPData().getWinStreak(p));
                  } else {
                     Arena arena;
                     if (identifier.equals("current_arena")) {
                        arena = this.plugin.amanager.getPlayerArena(p.getName());
                        return arena != null ? arena.getArenaName() : FormattingCodesParser.parseFormattingCodes(Messages.playernotinarena);
                     } else if (identifier.equals("doublejumps")) {
                        arena = this.plugin.amanager.getPlayerArena(p.getName());
                        return arena != null ? String.valueOf(arena.getPlayerHandler().getDoubleJumps(p.getName())) : String.valueOf(this.getUncachedDoubleJumps(p));
                     } else if (identifier.startsWith("position")) {
                        String[] temp = identifier.split("_");
                        if (!this.isValidType(temp[1])) {
                           return null;
                        } else {
                           int pos = this.plugin.getStats().getPosition(uuid, temp[1]);
                           return pos > 0 ? String.valueOf(pos) : "";
                        }
                     } else {
                        return null;
                     }
                  }
               }
            } else if (!this.isValidLeaderboardIdentifier(identifier)) {
               return null;
            } else {
               String[] temp = identifier.split("_");
               String type = temp[1];
               String entry = temp[2];
               int pos = Integer.parseInt(temp[3]);
               return this.plugin.getStats().getLeaderboardPosition(pos, type, entry);
            }
         } else {
            arena = this.getArenaFromPlaceholder(identifier, 2);
            return arena != null && arena.getStructureManager().isCurrencyEnabled() ? arena.getStructureManager().getCurrency().toString() : null;
         }
      }
   }

   private Arena getArenaFromPlaceholder(String identifier, int length) {
      String[] temp = identifier.split("_");
      return temp.length != length ? null : this.plugin.amanager.getArenaByName(temp[length - 1]);
   }

   private boolean isValidLeaderboardIdentifier(String identifier) {
      String[] temp = identifier.split("_");
      if (temp.length != 4) {
         return false;
      } else if (Utils.isNumber(temp[3]) && Integer.parseInt(temp[3]) >= 1) {
         Stream var10000 = Stream.of("player", "score", "rank");
         String var10001 = temp[2];
         temp[2].getClass();
         if (!var10000.anyMatch(var10001::equalsIgnoreCase)) {
            return false;
         } else {
            return this.isValidType(temp[1]);
         }
      } else {
         return false;
      }
   }

   private boolean isValidType(String type) {
      Stream var10000 = Stream.of("wins", "played", "losses");
      type.getClass();
      return var10000.anyMatch(type::equalsIgnoreCase);
   }

   private String getNames(HashSet<Player> playerSet) {
      StringJoiner names = new StringJoiner(", ");
      playerSet.stream().forEach((player) -> {
         names.add(player.getName());
      });
      return names.toString();
   }

   private int getUncachedDoubleJumps(OfflinePlayer p) {
      boolean free = this.plugin.getConfig().getBoolean("freedoublejumps.enabled");
      return free ? Utils.getAllowedDoubleJumps((Player)p, this.plugin.getConfig().getInt("freedoublejumps.amount", 0)) : this.plugin.getPData().getDoubleJumpsFromFile(p);
   }
}
