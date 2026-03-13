package tntrun.api;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.entity.Player;
import tntrun.TNTRun;

public class PartyAPI {
   private static final String[] CREATE = new String[]{"party", "create"};
   private static final String[] LEAVE = new String[]{"party", "leave"};

   public static void createParty(Player player) {
      TNTRun.getInstance().getParties().handleCommand(player, CREATE);
   }

   public static void leaveParty(Player player) {
      TNTRun.getInstance().getParties().handleCommand(player, LEAVE);
   }

   public static void joinParty(Player player, String target) {
      List<String> join = new ArrayList(List.of("party", "accept", target));
      TNTRun.getInstance().getParties().handleCommand(player, (String[])join.toArray((x$0) -> {
         return new String[x$0];
      }));
   }

   public static void inviteToParty(Player player, String target) {
      List<String> invite = new ArrayList(List.of("party", "invite", target));
      TNTRun.getInstance().getParties().handleCommand(player, (String[])invite.toArray((x$0) -> {
         return new String[x$0];
      }));
   }
}
