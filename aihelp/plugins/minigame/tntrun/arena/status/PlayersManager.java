package tntrun.arena.status;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import org.bukkit.entity.Player;

public class PlayersManager {
   private HashMap<String, Player> players = new HashMap();
   private HashMap<String, Player> spectators = new HashMap();
   private List<String> spectatoronly = new ArrayList();
   private String winner;

   public boolean isInArena(String name) {
      return this.players.containsKey(name) || this.spectators.containsKey(name);
   }

   public int getPlayersCount() {
      return this.players.size();
   }

   public int getSpectatorsCount() {
      return this.spectators.size();
   }

   public HashSet<Player> getAllParticipantsCopy() {
      HashSet<Player> p = new HashSet();
      p.addAll(this.players.values());
      p.addAll(this.spectators.values());
      return p;
   }

   public Collection<Player> getPlayers() {
      return Collections.unmodifiableCollection(this.players.values());
   }

   public HashSet<Player> getPlayersCopy() {
      return new HashSet(this.players.values());
   }

   public void add(Player player) {
      this.players.put(player.getName(), player);
   }

   public void remove(Player player) {
      this.players.remove(player.getName());
   }

   public boolean isSpectator(String name) {
      return this.spectators.containsKey(name);
   }

   public void addSpectator(Player player) {
      this.spectators.put(player.getName(), player);
   }

   public void removeSpectator(String name) {
      this.spectators.remove(name);
   }

   public Collection<Player> getSpectators() {
      return Collections.unmodifiableCollection(this.spectators.values());
   }

   public HashSet<Player> getSpectatorsCopy() {
      return new HashSet(this.spectators.values());
   }

   public void setWinner(String playername) {
      this.winner = playername;
   }

   public boolean isWinner(String playername) {
      return playername.equals(this.winner);
   }

   public void addSpectatorOnly(String name) {
      if (!this.isSpectatorOnly(name)) {
         this.spectatoronly.add(name);
      }

   }

   public void removeSpectatorOnly(String name) {
      List var10000 = this.spectatoronly;
      Objects.requireNonNull(name);
      var10000.removeIf(name::equals);
   }

   public boolean isSpectatorOnly(String name) {
      return this.spectatoronly.contains(name);
   }
}
