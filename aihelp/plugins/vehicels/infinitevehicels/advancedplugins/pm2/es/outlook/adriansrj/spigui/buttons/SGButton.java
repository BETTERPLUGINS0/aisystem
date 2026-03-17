package es.outlook.adriansrj.spigui.buttons;

import org.bukkit.inventory.ItemStack;

public class SGButton {
   private SGButtonListener listener;
   private ItemStack icon;

   public SGButton(ItemStack var1) {
      this.icon = var1;
   }

   public void setListener(SGButtonListener var1) {
      this.listener = var1;
   }

   public SGButton withListener(SGButtonListener var1) {
      this.listener = var1;
      return this;
   }

   public SGButtonListener getListener() {
      return this.listener;
   }

   public ItemStack getIcon() {
      return this.icon;
   }

   public void setIcon(ItemStack var1) {
      this.icon = var1;
   }
}
