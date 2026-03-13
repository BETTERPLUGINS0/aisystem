package tntrun.conversation;

import org.bukkit.ChatColor;
import org.bukkit.conversations.Conversable;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationAbandonedEvent;
import org.bukkit.conversations.ConversationAbandonedListener;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;
import tntrun.TNTRun;
import tntrun.arena.Arena;

public class TNTRunConversation implements ConversationAbandonedListener {
   private ConversationFactory conversationFactory;
   private Player player;
   private TNTRun plugin;
   private Arena arena;
   // $FF: synthetic field
   private static volatile int[] $SWITCH_TABLE$tntrun$conversation$ConversationType;

   public TNTRunConversation(TNTRun plugin, Player player, Arena arena, ConversationType conversationType) {
      this.player = player;
      this.plugin = plugin;
      this.arena = arena;
      this.conversationFactory = (new ConversationFactory(this.plugin)).withEscapeSequence("cancel").withTimeout(30).thatExcludesNonPlayersWithMessage("This is only possible in-game, sorry.").addConversationAbandonedListener(this).withFirstPrompt(this.getEntryPrompt(conversationType, player));
   }

   private Prompt getEntryPrompt(ConversationType type, Player player) {
      player.sendMessage(String.valueOf(ChatColor.GRAY) + "Enter 'cancel' anytime to quit the conversation.");
      switch($SWITCH_TABLE$tntrun$conversation$ConversationType()[type.ordinal()]) {
      case 1:
         return new ArenaRewardConversation(this.arena);
      default:
         String var10001 = String.valueOf(ChatColor.GRAY);
         player.sendMessage(var10001 + "[" + String.valueOf(ChatColor.GOLD) + "TNTRun" + String.valueOf(ChatColor.GRAY) + "]" + String.valueOf(ChatColor.RED) + " Unexpected conversation type: " + String.valueOf(type));
         return null;
      }
   }

   public void conversationAbandoned(ConversationAbandonedEvent event) {
      if (!event.gracefulExit()) {
         Conversable var10000 = event.getContext().getForWhom();
         String var10001 = String.valueOf(ChatColor.GRAY);
         var10000.sendRawMessage(var10001 + "[" + String.valueOf(ChatColor.GOLD) + "TNTRun" + String.valueOf(ChatColor.GRAY) + "]" + String.valueOf(ChatColor.RED) + "Conversation aborted...");
      }

   }

   public static void sendErrorMessage(ConversationContext context, String message) {
      Conversable var10000 = context.getForWhom();
      String var10001 = String.valueOf(ChatColor.GRAY);
      var10000.sendRawMessage(var10001 + "[" + String.valueOf(ChatColor.GOLD) + "TNTRun" + String.valueOf(ChatColor.GRAY) + "] " + String.valueOf(ChatColor.RED) + message + ". Please try again...");
   }

   public void begin() {
      Conversation convo = this.conversationFactory.buildConversation(this.player);
      convo.getContext().setSessionData("playerName", this.player.getName());
      convo.begin();
   }

   // $FF: synthetic method
   static int[] $SWITCH_TABLE$tntrun$conversation$ConversationType() {
      int[] var10000 = $SWITCH_TABLE$tntrun$conversation$ConversationType;
      if (var10000 != null) {
         return var10000;
      } else {
         int[] var0 = new int[ConversationType.values().length];

         try {
            var0[ConversationType.ARENAPRIZE.ordinal()] = 1;
         } catch (NoSuchFieldError var1) {
         }

         $SWITCH_TABLE$tntrun$conversation$ConversationType = var0;
         return var0;
      }
   }
}
