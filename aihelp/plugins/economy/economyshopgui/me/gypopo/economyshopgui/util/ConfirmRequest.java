package me.gypopo.economyshopgui.util;

import java.util.function.Consumer;
import me.gypopo.economyshopgui.EconomyShopGUI;
import me.gypopo.economyshopgui.files.Lang;
import org.bukkit.ChatColor;
import org.bukkit.conversations.BooleanPrompt;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ConfirmRequest {
   public ConfirmRequest(EconomyShopGUI plugin, String q, Player p, Consumer<Boolean> onComplete) {
      ConversationFactory factory = new ConversationFactory(plugin);
      Conversation conv = factory.withFirstPrompt(this.getPrompt(q, onComplete)).withLocalEcho(false).withTimeout(60).withPrefix((context) -> {
         return Lang.SHOP_PREFIX.get().getLegacy() + ChatColor.RESET + " ";
      }).addConversationAbandonedListener((e) -> {
         if (e.gracefulExit()) {
            onComplete.accept((Boolean)e.getContext().getSessionData("input"));
         } else {
            onComplete.accept(false);
         }

      }).buildConversation(p);
      conv.begin();
   }

   private Prompt getPrompt(final String q, Consumer<Boolean> onComplete) {
      return new BooleanPrompt() {
         @Nullable
         protected Prompt acceptValidatedInput(@NotNull ConversationContext context, boolean input) {
            context.setSessionData("input", input);
            return Prompt.END_OF_CONVERSATION;
         }

         public String getPromptText(ConversationContext c) {
            return q;
         }
      };
   }
}
