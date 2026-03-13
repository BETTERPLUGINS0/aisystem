package tntrun.parties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import tntrun.TNTRun;
import tntrun.messages.Messages;
import tntrun.utils.Utils;

public class Parties {
   private final TNTRun plugin;
   private Map<String, List<String>> partyMap = new HashMap();
   private Map<String, List<String>> kickedMap = new HashMap();
   private Map<String, List<String>> invitedMap = new HashMap();
   private String partyLeader;

   public Parties(TNTRun plugin) {
      this.plugin = plugin;
   }

   public void handleCommand(Player player, String[] args) {
      String var3 = args[1].toLowerCase();
      byte var4 = -1;
      switch(var3.hashCode()) {
      case -1423461112:
         if (var3.equals("accept")) {
            var4 = 5;
         }
         break;
      case -1352294148:
         if (var3.equals("create")) {
            var4 = 0;
         }
         break;
      case -1183699191:
         if (var3.equals("invite")) {
            var4 = 1;
         }
         break;
      case -840477601:
         if (var3.equals("unkick")) {
            var4 = 4;
         }
         break;
      case 3237038:
         if (var3.equals("info")) {
            var4 = 7;
         }
         break;
      case 3291718:
         if (var3.equals("kick")) {
            var4 = 3;
         }
         break;
      case 102846135:
         if (var3.equals("leave")) {
            var4 = 2;
         }
         break;
      case 1542349558:
         if (var3.equals("decline")) {
            var4 = 6;
         }
      }

      switch(var4) {
      case 0:
         this.createParty(player);
         break;
      case 1:
         this.inviteToParty(player, args[2]);
         break;
      case 2:
         this.leaveParty(player);
         break;
      case 3:
         this.kickFromParty(player, args[2]);
         break;
      case 4:
         this.unkickFromParty(player, args[2]);
         break;
      case 5:
         this.joinParty(player, args[2]);
         break;
      case 6:
         this.declinePartyInvite(player, args[2]);
         break;
      case 7:
         this.displayPartyInfo(player);
         break;
      default:
         Messages.sendMessage(player, "&c Invalid argument supplied");
      }

   }

   private void createParty(Player player) {
      if (this.alreadyInParty(player)) {
         Messages.sendMessage(player, Messages.partyinparty);
      } else {
         this.partyMap.put(player.getName(), new ArrayList());
         Messages.sendMessage(player, Messages.partycreate);
         if (Utils.debug()) {
            this.plugin.getLogger().info("Party created by " + player.getName());
         }

      }
   }

   private void leaveParty(Player player) {
      if (!this.isPartyLeader(player)) {
         if (!this.isPartyMember(player)) {
            Messages.sendMessage(player, Messages.partynotmember);
         } else {
            this.partyMap.entrySet().forEach((e) -> {
               if (((List)e.getValue()).contains(player.getName())) {
                  ((List)e.getValue()).remove(player.getName());
                  String msg = Messages.partyleave.replace("{PLAYER}", player.getName());
                  Messages.sendMessage(player, msg);
                  Messages.sendMessage(Bukkit.getPlayer((String)e.getKey()), msg);
                  if (Utils.debug()) {
                     Logger var10000 = this.plugin.getLogger();
                     String var10001 = player.getName();
                     var10000.info(var10001 + " has left party created by " + (String)e.getKey());
                  }
               }

            });
         }
      } else {
         Iterator var2 = ((List)this.partyMap.get(player.getName())).iterator();

         while(var2.hasNext()) {
            String member = (String)var2.next();
            Messages.sendMessage(Bukkit.getPlayer(member), Messages.partyleaderleave.replace("{PLAYER}", player.getName()));
         }

         this.removeParty(player);
      }
   }

   private void kickFromParty(Player player, String targetName) {
      if (!this.isPartyLeader(player)) {
         Messages.sendMessage(player, Messages.partynotleader);
      } else {
         if (((List)this.partyMap.get(player.getName())).removeIf((list) -> {
            return list.contains(targetName);
         })) {
            ((List)this.kickedMap.computeIfAbsent(player.getName(), (k) -> {
               return new ArrayList();
            })).add(targetName);
            Messages.sendMessage(player, Messages.partykick.replace("{PLAYER}", targetName));
            Messages.sendMessage(Bukkit.getPlayer(targetName), Messages.partykick.replace("{PLAYER}", targetName));
         }

      }
   }

   private void unkickFromParty(Player player, String targetName) {
      if (!this.isPartyLeader(player)) {
         Messages.sendMessage(player, Messages.partynotleader);
      } else {
         if (this.kickedMap.containsKey(player.getName())) {
            ((List)this.kickedMap.get(player.getName())).removeIf((list) -> {
               return list.contains(targetName);
            });
            Messages.sendMessage(player, Messages.partyunkick.replace("{PLAYER}", targetName));
         }

      }
   }

   private void inviteToParty(Player player, String targetName) {
      if (!this.isPartyLeader(player)) {
         Messages.sendMessage(player, Messages.partynotleader);
      } else if (targetName.equalsIgnoreCase(player.getName())) {
         Messages.sendMessage(player, Messages.partyinviteself);
      } else if (Bukkit.getPlayer(targetName) == null) {
         Messages.sendMessage(player, Messages.playernotonline.replace("{PLAYER}", targetName));
      } else {
         ((List)this.invitedMap.computeIfAbsent(player.getName(), (k) -> {
            return new ArrayList();
         })).add(targetName);
         Messages.sendMessage(Bukkit.getPlayer(targetName), Messages.partyinvite.replace("{PLAYER}", player.getName()));
         Utils.displayPartyInvite(player, targetName);
      }
   }

   private void joinParty(Player player, String leaderName) {
      String playerName = player.getName();
      if (this.alreadyInParty(player)) {
         Messages.sendMessage(player, Messages.partyinparty);
      } else if (!this.partyExists(leaderName)) {
         Messages.sendMessage(player, Messages.partynotexist);
      } else if (this.isKicked(leaderName, playerName)) {
         Messages.sendMessage(player, Messages.partyban);
      } else if (!this.isInvited(leaderName, playerName)) {
         Messages.sendMessage(player, Messages.partynoinvite);
      } else {
         ((List)this.partyMap.computeIfAbsent(leaderName, (k) -> {
            return new ArrayList();
         })).add(playerName);
         ((List)this.invitedMap.get(leaderName)).removeIf((list) -> {
            return list.contains(playerName);
         });
         String msg = Messages.partyjoin.replace("{PLAYER}", playerName);
         Messages.sendMessage(Bukkit.getPlayer(leaderName), msg);
         Messages.sendMessage(player, msg);
         if (Utils.debug()) {
            this.plugin.getLogger().info(playerName + " has joined party created by " + leaderName);
         }

      }
   }

   private void declinePartyInvite(Player player, String leaderName) {
      String playerName = player.getName();
      if (((List)this.invitedMap.get(leaderName)).removeIf((list) -> {
         return list.contains(playerName);
      })) {
         String msg = Messages.partydecline.replace("{PLAYER}", playerName);
         Messages.sendMessage(player, msg);
         Messages.sendMessage(Bukkit.getPlayer(leaderName), msg);
         if (Utils.debug()) {
            this.plugin.getLogger().info(playerName + " has declined the party invitation from " + leaderName);
         }
      }

   }

   public boolean isPartyLeader(Player player) {
      return this.partyMap.containsKey(player.getName());
   }

   private boolean isPartyMember(Player player) {
      return this.partyMap.values().stream().anyMatch((list) -> {
         return list.contains(player.getName());
      });
   }

   private boolean alreadyInParty(Player player) {
      return this.isPartyLeader(player) || this.isPartyMember(player);
   }

   private boolean isKicked(String playerName, String targetName) {
      return this.kickedMap.containsKey(playerName) ? ((List)this.kickedMap.get(playerName)).contains(targetName) : false;
   }

   private boolean isInvited(String playerName, String targetName) {
      return this.invitedMap.containsKey(playerName) ? ((List)this.invitedMap.get(playerName)).contains(targetName) : false;
   }

   private void removeParty(Player player) {
      this.partyMap.remove(player.getName());
      this.invitedMap.remove(player.getName());
      Messages.sendMessage(player, Messages.partyleaderleave.replace("{PLAYER}", player.getName()));
      if (Utils.debug()) {
         this.plugin.getLogger().info("Party leader " + player.getName() + " has left party");
      }

   }

   public List<String> getPartyMembers(String playerName) {
      return (List)this.partyMap.get(playerName);
   }

   private boolean partyExists(String playerName) {
      return this.partyMap.containsKey(playerName);
   }

   private String getPartyLeader(Player player) {
      if (this.isPartyLeader(player)) {
         return player.getName();
      } else {
         this.partyMap.entrySet().forEach((e) -> {
            if (((List)e.getValue()).contains(player.getName())) {
               this.partyLeader = (String)e.getKey();
            }
         });
         return this.partyLeader != null ? this.partyLeader : "unknown";
      }
   }

   private void displayPartyInfo(Player player) {
      if (!this.alreadyInParty(player)) {
         Messages.sendMessage(player, Messages.partynotmember);
      } else {
         String leader = this.getPartyLeader(player);
         Messages.sendMessage(player, " Party leader: " + leader);
         int var10001 = this.getPartyMembers(leader).size();
         Messages.sendMessage(player, " Party size: " + (var10001 + 1));
         List var3 = this.getPartyMembers(leader);
         Messages.sendMessage(player, " Party members: " + var3.toString());
      }
   }
}
