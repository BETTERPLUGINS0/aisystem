package com.nisovin.shopkeepers.ui.villager;

import com.nisovin.shopkeepers.ui.lib.AbstractUIType;
import com.nisovin.shopkeepers.ui.lib.ViewProvider;
import org.bukkit.entity.AbstractVillager;

public abstract class AbstractVillagerViewProvider extends ViewProvider {
   protected AbstractVillagerViewProvider(AbstractUIType uiType, AbstractVillager villager) {
      super(uiType, new VillagerViewContext(villager));
   }

   public VillagerViewContext getContext() {
      return (VillagerViewContext)super.getContext();
   }

   public AbstractVillager getVillager() {
      return this.getContext().getObject();
   }
}
