package me.casperge.realisticseasons.seasonevent.buildin;

import me.casperge.realisticseasons.utils.JavaUtils;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;

public interface DefaultEvent {
   void enable(World var1);

   void disable(World var1);

   boolean isEnabled(World var1);

   DefaultEventType getType();

   static ItemStack randomItemFromList(RandomItemStack[] stacks) {
      return stacks[JavaUtils.getRandom().nextInt(stacks.length)].generateStack();
   }
}
