package tntrun.conversation;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.conversations.BooleanPrompt;
import org.bukkit.conversations.Conversable;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.FixedSetPrompt;
import org.bukkit.conversations.MessagePrompt;
import org.bukkit.conversations.NumericPrompt;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import tntrun.arena.Arena;
import tntrun.utils.Utils;

public class ArenaRewardConversation extends NumericPrompt {
   private Arena arena;
   private boolean isFirstItem = true;
   private String podium;
   private int place;
   private static final String PREFIX;

   static {
      String var10000 = String.valueOf(ChatColor.GRAY);
      PREFIX = var10000 + "[" + String.valueOf(ChatColor.GOLD) + "TNTRun_reloaded" + String.valueOf(ChatColor.GRAY) + "] ";
   }

   public ArenaRewardConversation(Arena arena) {
      this.arena = arena;
   }

   public String getPromptText(ConversationContext context) {
      return String.valueOf(ChatColor.GOLD) + " What position would you like to set a reward for? (1, 2, 3, ...)\n";
   }

   protected boolean isNumberValid(ConversationContext context, Number input) {
      return input.intValue() > 0 && input.intValue() < 100;
   }

   protected String getFailedValidationText(ConversationContext context, Number invalidInput) {
      return "Position must be between 1 and 99.";
   }

   protected Prompt acceptValidatedInput(ConversationContext context, Number position) {
      this.place = (Integer)position;
      String var10001 = String.valueOf(ChatColor.GRAY);
      this.podium = var10001 + "Position " + String.valueOf(ChatColor.GOLD) + this.place + String.valueOf(ChatColor.GRAY) + ": ";
      return new ArenaRewardConversation.ChooseRewardType();
   }

   private class AddDisplayName extends StringPrompt {
      public String getPromptText(ConversationContext context) {
         String var10000 = String.valueOf(ChatColor.GOLD);
         return var10000 + " What display name do you want to attach to the " + context.getSessionData("material").toString() + "?";
      }

      public Prompt acceptInput(ConversationContext context, String message) {
         context.setSessionData("label", message);
         return ArenaRewardConversation.this.new MaterialProcessComplete();
      }
   }

   private class ChooseAmount extends NumericPrompt {
      public String getPromptText(ConversationContext context) {
         return String.valueOf(ChatColor.GOLD) + " How many would you like to reward the player with?";
      }

      protected boolean isNumberValid(ConversationContext context, Number input) {
         return input.intValue() >= 0 && input.intValue() <= 255;
      }

      protected String getFailedValidationText(ConversationContext context, Number invalidInput) {
         return "Amount must be between 0 and 255.";
      }

      protected Prompt acceptValidatedInput(ConversationContext context, Number amount) {
         context.setSessionData("amount", amount.intValue());
         return ArenaRewardConversation.this.new ChooseDisplayName();
      }
   }

   private class ChooseCommand extends StringPrompt {
      public String getPromptText(ConversationContext context) {
         context.getForWhom().sendRawMessage(String.valueOf(ChatColor.GRAY) + "Remember you can include %PLAYER% to apply it to that player.\nExample: 'perm setrank %PLAYER% vip'");
         context.getForWhom().sendRawMessage(String.valueOf(ChatColor.GRAY) + "Enter NONE to delete any existing Command reward.");
         return String.valueOf(ChatColor.GOLD) + " What would you like the Command reward to be?";
      }

      public Prompt acceptInput(ConversationContext context, String message) {
         if (message.equalsIgnoreCase("none")) {
            ArenaRewardConversation.this.arena.getStructureManager().getRewards().deleteCommandReward(ArenaRewardConversation.this.place);
            Conversable var10000 = context.getForWhom();
            String var10001 = ArenaRewardConversation.PREFIX;
            var10000.sendRawMessage(var10001 + ArenaRewardConversation.this.podium + "command reward for " + String.valueOf(ChatColor.GOLD) + ArenaRewardConversation.this.arena.getArenaName() + String.valueOf(ChatColor.GRAY) + " deleted");
            return Prompt.END_OF_CONVERSATION;
         } else {
            String command = message.replace("/", "");
            context.setSessionData("command", command);
            return ArenaRewardConversation.this.new ChooseRunNow();
         }
      }
   }

   private class ChooseDisplayName extends BooleanPrompt {
      public String getPromptText(ConversationContext context) {
         String var10000 = String.valueOf(ChatColor.GOLD);
         return var10000 + " Would you like to add a custom display name?\n" + String.valueOf(ChatColor.GREEN) + "[yes, no]";
      }

      protected Prompt acceptValidatedInput(ConversationContext context, boolean addName) {
         if (addName) {
            return ArenaRewardConversation.this.new AddDisplayName();
         } else {
            context.setSessionData("label", "");
            return ArenaRewardConversation.this.new MaterialProcessComplete();
         }
      }
   }

   private class ChooseMaterial extends StringPrompt {
      public String getPromptText(ConversationContext context) {
         context.getForWhom().sendRawMessage(String.valueOf(ChatColor.GRAY) + "Enter NONE to delete any existing Material reward.");
         return String.valueOf(ChatColor.GOLD) + " What Material do you want to reward the player with?";
      }

      public Prompt acceptInput(ConversationContext context, String message) {
         if (message.equalsIgnoreCase("none")) {
            ArenaRewardConversation.this.arena.getStructureManager().getRewards().deleteMaterialReward(ArenaRewardConversation.this.place);
            Conversable var10000 = context.getForWhom();
            String var10001 = ArenaRewardConversation.PREFIX;
            var10000.sendRawMessage(var10001 + ArenaRewardConversation.this.podium + "material reward for " + String.valueOf(ChatColor.GOLD) + ArenaRewardConversation.this.arena.getArenaName() + String.valueOf(ChatColor.GRAY) + " deleted");
            return Prompt.END_OF_CONVERSATION;
         } else {
            Material material = Material.getMaterial(message.toUpperCase());
            if (material == null) {
               TNTRunConversation.sendErrorMessage(context, "This is not a valid material");
               return this;
            } else {
               context.setSessionData("material", message.toUpperCase());
               return ArenaRewardConversation.this.new ChooseAmount();
            }
         }
      }
   }

   private class ChooseMoney extends NumericPrompt {
      public String getPromptText(ConversationContext context) {
         return String.valueOf(ChatColor.GOLD) + " How much money would you like to reward the player with?";
      }

      protected boolean isNumberValid(ConversationContext context, Number input) {
         return input.intValue() >= 0;
      }

      protected String getFailedValidationText(ConversationContext context, Number invalidInput) {
         return "Amount of money must be at least zero";
      }

      protected Prompt acceptValidatedInput(ConversationContext context, Number amount) {
         context.setSessionData("amount", amount.intValue());
         return ArenaRewardConversation.this.new MoneyProcessComplete();
      }
   }

   private class ChooseRewardType extends FixedSetPrompt {
      public ChooseRewardType() {
         super(new String[]{"material", "command", "xp", "money"});
      }

      public String getPromptText(ConversationContext context) {
         String var10000 = String.valueOf(ChatColor.GOLD);
         return var10000 + " What type of reward would you like to set?\n" + String.valueOf(ChatColor.GREEN) + this.formatFixedSet();
      }

      protected Prompt acceptValidatedInput(ConversationContext context, String choice) {
         String var3;
         switch((var3 = choice.toLowerCase()).hashCode()) {
         case 3832:
            if (var3.equals("xp")) {
               return ArenaRewardConversation.this.new ChooseXP();
            }
            break;
         case 104079552:
            if (var3.equals("money")) {
               return ArenaRewardConversation.this.new ChooseMoney();
            }
            break;
         case 299066663:
            if (var3.equals("material")) {
               return ArenaRewardConversation.this.new ChooseMaterial();
            }
            break;
         case 950394699:
            if (var3.equals("command")) {
               return ArenaRewardConversation.this.new ChooseCommand();
            }
         }

         return null;
      }
   }

   private class ChooseRunNow extends BooleanPrompt {
      public String getPromptText(ConversationContext arg0) {
         String var10000 = String.valueOf(ChatColor.GOLD);
         return var10000 + " Would you like to run this command now? (to test)\n" + String.valueOf(ChatColor.GREEN) + "[yes, no]";
      }

      protected Prompt acceptValidatedInput(ConversationContext context, boolean runNow) {
         if (runNow) {
            Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), context.getSessionData("command").toString().replace("%PLAYER%", context.getSessionData("playerName").toString()));
         }

         return ArenaRewardConversation.this.new CommandProcessComplete();
      }
   }

   private class ChooseXP extends NumericPrompt {
      public String getPromptText(ConversationContext context) {
         return String.valueOf(ChatColor.GOLD) + " How much XP would you like to reward the player with?";
      }

      protected boolean isNumberValid(ConversationContext context, Number input) {
         return input.intValue() >= 0 && input.intValue() <= 10000;
      }

      protected String getFailedValidationText(ConversationContext context, Number invalidInput) {
         return "Amount must be between 0 and 10,000.";
      }

      protected Prompt acceptValidatedInput(ConversationContext context, Number amount) {
         context.setSessionData("amount", amount.intValue());
         return ArenaRewardConversation.this.new XPProcessComplete();
      }
   }

   private class CommandProcessComplete extends BooleanPrompt {
      public String getPromptText(ConversationContext context) {
         String var10000 = String.valueOf(ChatColor.GOLD);
         return var10000 + " Reward saved - would you like to add another Command?\n" + String.valueOf(ChatColor.GREEN) + "[yes, no]";
      }

      protected Prompt acceptValidatedInput(ConversationContext context, boolean nextCommand) {
         ArenaRewardConversation.this.arena.getStructureManager().getRewards().setCommandReward(context.getSessionData("command").toString(), ArenaRewardConversation.this.isFirstItem, ArenaRewardConversation.this.place);
         Conversable var10000 = context.getForWhom();
         String var10001 = ArenaRewardConversation.PREFIX;
         var10000.sendRawMessage(var10001 + ArenaRewardConversation.this.podium + "command reward for " + String.valueOf(ChatColor.GOLD) + ArenaRewardConversation.this.arena.getArenaName() + String.valueOf(ChatColor.GRAY) + " was set to /" + String.valueOf(ChatColor.GOLD) + String.valueOf(context.getSessionData("command")));
         if (nextCommand) {
            ArenaRewardConversation.this.isFirstItem = false;
            return ArenaRewardConversation.this.new ChooseCommand();
         } else {
            ArenaRewardConversation.this.isFirstItem = true;
            return Prompt.END_OF_CONVERSATION;
         }
      }
   }

   private class MaterialProcessComplete extends BooleanPrompt {
      public String getPromptText(ConversationContext context) {
         String var10000 = String.valueOf(ChatColor.GOLD);
         return var10000 + " Reward saved - would you like to add another Material?\n" + String.valueOf(ChatColor.GREEN) + "[yes, no]";
      }

      protected Prompt acceptValidatedInput(ConversationContext context, boolean nextMaterial) {
         ArenaRewardConversation.this.arena.getStructureManager().getRewards().setMaterialReward(context.getSessionData("material").toString(), context.getSessionData("amount").toString(), context.getSessionData("label").toString(), ArenaRewardConversation.this.isFirstItem, ArenaRewardConversation.this.place);
         Conversable var10000;
         String var10001;
         if (ArenaRewardConversation.this.isFirstItem) {
            var10000 = context.getForWhom();
            var10001 = ArenaRewardConversation.PREFIX;
            var10000.sendRawMessage(var10001 + ArenaRewardConversation.this.podium + "material reward for " + String.valueOf(ChatColor.GOLD) + ArenaRewardConversation.this.arena.getArenaName() + String.valueOf(ChatColor.GRAY) + " set to " + String.valueOf(ChatColor.GOLD) + String.valueOf(context.getSessionData("amount")) + String.valueOf(ChatColor.GRAY) + " x " + String.valueOf(ChatColor.GOLD) + String.valueOf(context.getSessionData("material")));
         } else {
            var10000 = context.getForWhom();
            var10001 = ArenaRewardConversation.PREFIX;
            var10000.sendRawMessage(var10001 + ArenaRewardConversation.this.podium + String.valueOf(ChatColor.GOLD) + String.valueOf(context.getSessionData("amount")) + String.valueOf(ChatColor.GRAY) + " x " + String.valueOf(ChatColor.GOLD) + String.valueOf(context.getSessionData("material")) + String.valueOf(ChatColor.GRAY) + " added to " + ArenaRewardConversation.this.podium + "material reward for " + String.valueOf(ChatColor.GOLD) + ArenaRewardConversation.this.arena.getArenaName());
         }

         if (nextMaterial) {
            ArenaRewardConversation.this.isFirstItem = false;
            return ArenaRewardConversation.this.new ChooseMaterial();
         } else {
            ArenaRewardConversation.this.isFirstItem = true;
            return Prompt.END_OF_CONVERSATION;
         }
      }
   }

   private class MoneyProcessComplete extends MessagePrompt {
      public String getPromptText(ConversationContext context) {
         ArenaRewardConversation.this.arena.getStructureManager().getRewards().setMoneyReward(Integer.parseInt(context.getSessionData("amount").toString()), ArenaRewardConversation.this.place);
         String var10000 = ArenaRewardConversation.PREFIX;
         return var10000 + ArenaRewardConversation.this.podium + "money reward for " + String.valueOf(ChatColor.GOLD) + ArenaRewardConversation.this.arena.getArenaName() + String.valueOf(ChatColor.GRAY) + " was set to " + String.valueOf(ChatColor.GOLD) + Utils.getFormattedCurrency(context.getSessionData("amount").toString());
      }

      protected Prompt getNextPrompt(ConversationContext context) {
         return Prompt.END_OF_CONVERSATION;
      }
   }

   private class XPProcessComplete extends MessagePrompt {
      public String getPromptText(ConversationContext context) {
         ArenaRewardConversation.this.arena.getStructureManager().getRewards().setXPReward(Integer.parseInt(context.getSessionData("amount").toString()), ArenaRewardConversation.this.place);
         String var10000 = ArenaRewardConversation.PREFIX;
         return var10000 + ArenaRewardConversation.this.podium + "XP reward for " + String.valueOf(ChatColor.GOLD) + ArenaRewardConversation.this.arena.getArenaName() + String.valueOf(ChatColor.GRAY) + " was set to " + String.valueOf(ChatColor.GOLD) + String.valueOf(context.getSessionData("amount"));
      }

      protected Prompt getNextPrompt(ConversationContext context) {
         return Prompt.END_OF_CONVERSATION;
      }
   }
}
