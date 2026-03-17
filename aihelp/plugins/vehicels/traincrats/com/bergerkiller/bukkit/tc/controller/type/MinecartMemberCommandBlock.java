package com.bergerkiller.bukkit.tc.controller.type;

import com.bergerkiller.bukkit.common.entity.type.CommonMinecartCommandBlock;
import com.bergerkiller.bukkit.tc.TrainCarts;
import com.bergerkiller.bukkit.tc.controller.MinecartMember;
import com.bergerkiller.bukkit.tc.controller.persistence.CommandPersistentCartAttribute;

public class MinecartMemberCommandBlock extends MinecartMember<CommonMinecartCommandBlock> {
   public MinecartMemberCommandBlock(TrainCarts plugin) {
      super(plugin);
      this.addPersistentCartAttribute(new CommandPersistentCartAttribute());
   }

   public void onActivatorUpdate(boolean activated) {
      ((CommonMinecartCommandBlock)this.getEntity()).activate(this.getBlock(), activated);
   }
}
