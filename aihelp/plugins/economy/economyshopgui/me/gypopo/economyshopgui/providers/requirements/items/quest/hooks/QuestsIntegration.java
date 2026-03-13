package me.gypopo.economyshopgui.providers.requirements.items.quest.hooks;

import com.leonardobishop.quests.bukkit.BukkitQuestsPlugin;
import com.leonardobishop.quests.bukkit.menu.itemstack.QItemStack;
import com.leonardobishop.quests.common.plugin.Quests;
import com.leonardobishop.quests.common.quest.Quest;
import me.gypopo.economyshopgui.EconomyShopGUI;
import me.gypopo.economyshopgui.files.Lang;
import me.gypopo.economyshopgui.methodes.SendMessage;
import me.gypopo.economyshopgui.providers.requirements.items.quest.QuestManager;
import me.gypopo.economyshopgui.providers.requirements.items.quest.QuestProvider;
import org.bukkit.entity.Player;

public class QuestsIntegration implements QuestProvider {
   private final Quests questsAPI;

   public QuestsIntegration(EconomyShopGUI plugin, QuestManager manager) {
      this.questsAPI = (Quests)plugin.getServer().getPluginManager().getPlugin("Quests");
   }

   public void init(EconomyShopGUI plugin, QuestManager manager) {
      if (!this.isReady()) {
         SendMessage.infoMessage(this.getName() + " found, waiting...");
         plugin.runTaskLater(() -> {
            if (plugin.getServer().getPluginManager().isPluginEnabled("Quests")) {
               SendMessage.infoMessage(Lang.ENABLED_PLUGIN_HOOK.get().replace("%plugin%", this.getName()));
            } else {
               SendMessage.warnMessage(Lang.FAILED_PLUGIN_INTEGRATION.get().replace("%plugin%", this.getName()));
            }

            manager.onLoad();
         }, 5L);
      } else {
         SendMessage.infoMessage(Lang.ENABLED_PLUGIN_HOOK.get().replace("%plugin%", this.getName()));
      }

   }

   public boolean isReady() {
      com.leonardobishop.quests.common.quest.QuestManager qManager = this.questsAPI.getQuestManager();
      return qManager != null && !qManager.getQuests().isEmpty();
   }

   public String getName() {
      return "Quests by LMBishop";
   }

   public boolean isLoaded(String questID) {
      return this.questsAPI.getQuestManager().getQuestById(questID) != null;
   }

   public String getQuestName(String questID) {
      Quest quest = this.questsAPI.getQuestManager().getQuestById(questID);
      QItemStack qItem = ((BukkitQuestsPlugin)this.questsAPI).getQItemStackRegistry().getQuestItemStack(quest);
      return qItem.getName();
   }

   public boolean hasCompleted(Player p, String questID) {
      Quest quest = this.questsAPI.getQuestManager().getQuestById(questID);
      return this.questsAPI.getPlayerManager().getPlayer(p.getUniqueId()).getQuestProgressFile().getQuestProgress(quest).isCompleted();
   }
}
