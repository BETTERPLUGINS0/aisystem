package com.nisovin.shopkeepers.ui.villager;

import com.nisovin.shopkeepers.lang.Messages;
import com.nisovin.shopkeepers.text.Text;
import com.nisovin.shopkeepers.ui.lib.ViewContext;
import com.nisovin.shopkeepers.util.java.Validate;
import org.bukkit.entity.AbstractVillager;

public class VillagerViewContext implements ViewContext {
   private final AbstractVillager villager;

   public VillagerViewContext(AbstractVillager villager) {
      Validate.notNull(villager, (String)"villager is null");
      this.villager = villager;
   }

   public String getName() {
      return "Villager " + String.valueOf(this.villager.getUniqueId());
   }

   public AbstractVillager getObject() {
      return this.villager;
   }

   public boolean isValid() {
      return this.villager.isValid();
   }

   public Text getNoLongerValidMessage() {
      return Messages.villagerNoLongerExists;
   }
}
