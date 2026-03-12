package me.gypopo.economyshopgui.providers.requirements.items.quest;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import me.gypopo.economyshopgui.EconomyShopGUI;
import me.gypopo.economyshopgui.methodes.SendMessage;
import me.gypopo.economyshopgui.providers.requirements.items.quest.hooks.QuestsCIntegration;
import me.gypopo.economyshopgui.providers.requirements.items.quest.hooks.QuestsIntegration;
import me.gypopo.economyshopgui.util.exceptions.ItemRequirementLoadException;
import org.bukkit.plugin.PluginManager;

public class QuestManager {
   private final QuestProvider provider;
   private final Map<QuestRequirement, String> requirements = new HashMap();

   public QuestManager(EconomyShopGUI plugin) {
      PluginManager pm = plugin.getServer().getPluginManager();
      this.provider = this.initProvider(plugin, pm);
   }

   private QuestProvider initProvider(EconomyShopGUI plugin, PluginManager pm) {
      if (pm.getPlugin("Quests") != null) {
         try {
            Class.forName("me.pikamug.quests.Quests");
            return new QuestsCIntegration(plugin, this);
         } catch (ClassNotFoundException var4) {
            return new QuestsIntegration(plugin, this);
         }
      } else {
         return null;
      }
   }

   public QuestRequirement addRequirement(QuestRequirement requirement, String path) {
      this.requirements.put(requirement, path);
      return requirement;
   }

   public QuestProvider getProvider() throws ItemRequirementLoadException {
      if (this.provider != null) {
         return this.provider;
      } else {
         throw new ItemRequirementLoadException("Failed to enable integration with supported Quest plugin");
      }
   }

   public void onLoad() {
      if (!this.provider.isReady()) {
         SendMessage.errorMessage("Failed to enable quest integration with '" + this.provider.getName() + "'");
      }

      Iterator var1 = this.requirements.keySet().iterator();

      while(var1.hasNext()) {
         QuestRequirement requirement = (QuestRequirement)var1.next();
         String path = (String)this.requirements.get(requirement);
         if (!this.provider.isReady()) {
            SendMessage.warnMessage("Failed to load quest item requirement: Quest integration failed to load");
            SendMessage.errorShops(path.split("\\.")[0], path.split("\\.", 2)[1]);
         } else if (!this.provider.isLoaded(requirement.getQuestID())) {
            SendMessage.warnMessage("Failed to load quest item requirement: Quest using ID '" + requirement.getQuestID() + "' does not exist in " + this.provider.getName());
            SendMessage.errorShops(path.split("\\.")[0], path.split("\\.", 2)[1]);
         } else {
            requirement.onLoad();
         }
      }

   }
}
