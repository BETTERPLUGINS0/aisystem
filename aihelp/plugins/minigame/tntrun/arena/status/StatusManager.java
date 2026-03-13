package tntrun.arena.status;

import java.util.Iterator;
import org.bukkit.entity.Player;
import tntrun.arena.Arena;
import tntrun.messages.Messages;
import tntrun.utils.FormattingCodesParser;

public class StatusManager {
   private Arena arena;
   private boolean enabled = false;
   private boolean starting = false;
   private boolean running = false;
   private boolean regenerating = false;

   public StatusManager(Arena arena) {
      this.arena = arena;
   }

   public boolean isArenaEnabled() {
      return this.enabled;
   }

   public boolean enableArena() {
      if (this.arena.getStructureManager().isArenaConfigured()) {
         this.enabled = true;
         this.arena.getGameHandler().startArenaAntiLeaveHandler();
         this.arena.plugin.getSignEditor().modifySigns(this.arena.getArenaName());
         return true;
      } else {
         return false;
      }
   }

   public void disableArena() {
      this.enabled = false;
      Iterator var2 = this.arena.getPlayersManager().getPlayersCopy().iterator();

      while(var2.hasNext()) {
         Player player = (Player)var2.next();
         this.arena.getPlayerHandler().leavePlayer(player, Messages.arenadisabling, "");
      }

      if (this.arena.getStatusManager().isArenaRunning()) {
         this.arena.getGameHandler().stopArena();
      }

      if (this.arena.getStatusManager().isArenaStarting()) {
         this.arena.getGameHandler().stopArenaCountdown();
      }

      this.arena.getGameHandler().stopArenaAntiLeaveHandler();
      this.arena.getStructureManager().getGameZone().regenNow();
      this.arena.plugin.getSignEditor().modifySigns(this.arena.getArenaName());
   }

   public boolean isArenaStarting() {
      return this.starting;
   }

   public void setStarting(boolean starting) {
      this.starting = starting;
   }

   public boolean isArenaRunning() {
      return this.running;
   }

   public void setRunning(boolean running) {
      this.running = running;
   }

   public boolean isArenaRegenerating() {
      return this.regenerating;
   }

   public void setRegenerating(boolean regenerating) {
      this.regenerating = regenerating;
   }

   public String getArenaStatusMesssage() {
      String message = Messages.arenawaiting;
      if (this.isArenaRunning()) {
         message = Messages.arenarunning;
      } else if (this.isArenaRegenerating()) {
         message = Messages.arenaregenerating;
      } else if (this.isArenaStarting()) {
         message = Messages.arenastarting;
      } else if (!this.isArenaEnabled()) {
         message = Messages.arenadisabled;
      }

      return FormattingCodesParser.parseFormattingCodes(this.getFormattedMessage(message));
   }

   public String getFormattedMessage(String message) {
      return message.replace("{ARENA}", this.arena.getArenaName()).replace("{PS}", String.valueOf(this.arena.getPlayersManager().getPlayersCount())).replace("{MPS}", String.valueOf(this.arena.getStructureManager().getMaxPlayers()));
   }

   public String getArenaStatus() {
      if (this.isArenaRunning()) {
         return "Running";
      } else if (this.isArenaRegenerating()) {
         return "Regenerating";
      } else if (this.isArenaStarting()) {
         return "Starting";
      } else {
         return !this.isArenaEnabled() ? "Disabled" : "Waiting";
      }
   }
}
