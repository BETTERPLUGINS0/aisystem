package me.gypopo.economyshopgui.objects.inventorys;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.regex.Pattern;
import me.gypopo.economyshopgui.EconomyShopGUI;
import me.gypopo.economyshopgui.files.Lang;
import me.gypopo.economyshopgui.methodes.SendMessage;
import me.gypopo.economyshopgui.objects.ShopInventory;
import me.gypopo.economyshopgui.objects.layouts.SimpleCard;
import org.bukkit.ChatColor;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationAbandonedEvent;
import org.bukkit.conversations.ConversationAbandonedListener;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.conversations.ConversationPrefix;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;

public class CustomizeLayout extends ShopInventory {
   private ConversationFactory factory;
   private Conversation conv;
   private final SimpleCard card = new SimpleCard();
   private final Pattern namePattern = Pattern.compile("[a-zA-Z0-9]+");
   private final EconomyShopGUI plugin = EconomyShopGUI.getInstance();
   private final ArrayList<String> files = new ArrayList();
   private final Player player;
   private final Consumer<SimpleCard> onComplete;
   private final Prompt prompt = new StringPrompt() {
      public String getPromptText(ConversationContext c) {
         switch(CustomizeLayout.Input.valueOf((String)c.getSessionData("input")).ordinal()) {
         case 1:
            CustomizeLayout.this.player.sendTitle("§bEnter layout title", "", 5, 80, 5);
            return "§aEnter a title for this layout.\n§aExample: ModernLayout";
         case 2:
            CustomizeLayout.this.player.sendTitle("§bEnter description", "", 5, 80, 5);
            return "§aEnter a description for this layout.\n§aExample: Modern shop layout with almost every in-game item, designed for 1.21 survival based servers.";
         case 3:
            CustomizeLayout.this.player.sendTitle("§bEnter tags", "", 5, 80, 5);
            return "§aTags can be used to easily identify this layout, use the format of §f#<tag>§a, use commas(',') to specify multiple tags.\n§aExample: #modern, #allItems, #cheap";
         default:
            return null;
         }
      }

      public Prompt acceptInput(ConversationContext c, String s) {
         c.setSessionData("result", s);
         return Prompt.END_OF_CONVERSATION;
      }
   };

   public CustomizeLayout(Player p, Consumer<SimpleCard> onComplete) {
      this.onComplete = onComplete;
      this.player = p;
      this.startConversation(p, CustomizeLayout.Input.TITLE);
   }

   private void startConversation(Player p, CustomizeLayout.Input input) {
      this.factory = new ConversationFactory(EconomyShopGUI.getInstance());
      this.conv = this.factory.withFirstPrompt(this.prompt).addConversationAbandonedListener(new CustomizeLayout.ConvAbandon()).withEscapeSequence("exit").withLocalEcho(false).withTimeout(60).withPrefix(new CustomizeLayout.ConvPrefix()).buildConversation(p);
      this.conv.getContext().setSessionData("input", input.name());
      this.conv.begin();
   }

   private static enum Input {
      GUIDELINES,
      TITLE,
      DESC,
      TAGS;

      // $FF: synthetic method
      private static CustomizeLayout.Input[] $values() {
         return new CustomizeLayout.Input[]{GUIDELINES, TITLE, DESC, TAGS};
      }
   }

   private final class ConvAbandon implements ConversationAbandonedListener {
      private ConvAbandon() {
      }

      public void conversationAbandoned(ConversationAbandonedEvent event) {
         Player p = (Player)event.getContext().getForWhom();
         if (!event.gracefulExit()) {
            SendMessage.chatToPlayer(p, "§cExited the conversation.");
         } else {
            String result = (String)event.getContext().getSessionData("result");
            switch(CustomizeLayout.Input.valueOf((String)event.getContext().getSessionData("input")).ordinal()) {
            case 1:
               if (result.length() < 6) {
                  SendMessage.chatToPlayer(p, "§cTitle must be longer as 6 characters");
                  CustomizeLayout.this.startConversation(p, CustomizeLayout.Input.TITLE);
               } else if (result.length() > 32) {
                  SendMessage.chatToPlayer(p, "§cInvalid title, title cannot be longer as 32 characters");
                  CustomizeLayout.this.startConversation(p, CustomizeLayout.Input.TITLE);
               } else {
                  CustomizeLayout.this.card.setTitle(result);
                  CustomizeLayout.this.startConversation(p, CustomizeLayout.Input.DESC);
               }

               return;
            case 2:
               if (result.length() < 60) {
                  SendMessage.chatToPlayer(p, "§cDescription must be longer as 60 characters, please write a descriptive explanation of your layout");
                  CustomizeLayout.this.startConversation(p, CustomizeLayout.Input.DESC);
                  return;
               } else {
                  CustomizeLayout.this.card.setDescription(result);
                  CustomizeLayout.this.startConversation(p, CustomizeLayout.Input.TAGS);
                  return;
               }
            case 3:
               List<String> tags = new ArrayList();
               if (result.equalsIgnoreCase("skip")) {
                  SendMessage.chatToPlayer(p, "§c§lAtleast 1 tag is required!");
                  CustomizeLayout.this.startConversation(p, CustomizeLayout.Input.TAGS);
                  return;
               }

               String[] var5 = result.replace(" ", "").split(",");
               int var6 = var5.length;

               for(int var7 = 0; var7 < var6; ++var7) {
                  String tag = var5[var7];
                  if (tag.startsWith("#")) {
                     tag = tag.substring(1);
                  }

                  if (!CustomizeLayout.this.namePattern.matcher(tag).matches()) {
                     SendMessage.chatToPlayer(p, "§c§lInvalid tag for " + tag + ", contains invalid characters");
                     CustomizeLayout.this.startConversation(p, CustomizeLayout.Input.TAGS);
                     return;
                  }

                  if (tag.length() > 10) {
                     SendMessage.chatToPlayer(p, "§c§lInvalid tag for " + tag + ", tag cannot be longer as 10 characters");
                     CustomizeLayout.this.startConversation(p, CustomizeLayout.Input.TAGS);
                     return;
                  }

                  tags.add(tag);
               }

               CustomizeLayout.this.card.setTags(tags);
            default:
               CustomizeLayout.this.onComplete.accept(CustomizeLayout.this.card);
            }
         }

      }

      // $FF: synthetic method
      ConvAbandon(Object x1) {
         this();
      }
   }

   private final class ConvPrefix implements ConversationPrefix {
      private ConvPrefix() {
      }

      public String getPrefix(ConversationContext context) {
         return Lang.SHOP_PREFIX.get().getLegacy() + ChatColor.RESET + " ";
      }

      // $FF: synthetic method
      ConvPrefix(Object x1) {
         this();
      }
   }
}
