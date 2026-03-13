package com.volmit.iris.util.inventorygui;

import com.volmit.iris.Iris;
import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.data.MaterialBlock;
import com.volmit.iris.util.scheduling.Callback;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class UIElement implements Element {
   private final String id;
   private final KList<String> lore;
   private MaterialBlock material;
   private boolean enchanted;
   private String name;
   private double progress;
   private boolean bg;
   private Callback<Element> eLeft;
   private Callback<Element> eRight;
   private Callback<Element> eShiftLeft;
   private Callback<Element> eShiftRight;
   private Callback<Element> eDraggedInto;
   private Callback<Element> eOtherDraggedInto;
   private int count;

   public UIElement(String id) {
      this.id = var1;
      this.lore = new KList();
      this.enchanted = false;
      this.count = 1;
      this.material = new MaterialBlock(Material.AIR);
   }

   public MaterialBlock getMaterial() {
      return this.material;
   }

   public UIElement setMaterial(MaterialBlock material) {
      this.material = var1;
      return this;
   }

   public Double clip(double value, double min, double max) {
      return Math.min(var5, Math.max(var3, var1));
   }

   public boolean isEnchanted() {
      return this.enchanted;
   }

   public UIElement setEnchanted(boolean enchanted) {
      this.enchanted = var1;
      return this;
   }

   public String getId() {
      return this.id;
   }

   public String getName() {
      return this.name;
   }

   public UIElement setName(String name) {
      this.name = var1;
      return this;
   }

   public KList<String> getLore() {
      return this.lore;
   }

   public UIElement onLeftClick(Callback<Element> clicked) {
      this.eLeft = var1;
      return this;
   }

   public UIElement onRightClick(Callback<Element> clicked) {
      this.eRight = var1;
      return this;
   }

   public UIElement onShiftLeftClick(Callback<Element> clicked) {
      this.eShiftLeft = var1;
      return this;
   }

   public UIElement onShiftRightClick(Callback<Element> clicked) {
      this.eShiftRight = var1;
      return this;
   }

   public UIElement onDraggedInto(Callback<Element> into) {
      this.eDraggedInto = var1;
      return this;
   }

   public UIElement onOtherDraggedInto(Callback<Element> other) {
      this.eOtherDraggedInto = var1;
      return this;
   }

   public Element call(ElementEvent event, Element context) {
      try {
         switch(var1) {
         case DRAG_INTO:
            this.eDraggedInto.run(var2);
            return this;
         case LEFT:
            this.eLeft.run(var2);
            return this;
         case OTHER_DRAG_INTO:
            this.eOtherDraggedInto.run(var2);
            return this;
         case RIGHT:
            this.eRight.run(var2);
            return this;
         case SHIFT_LEFT:
            this.eShiftLeft.run(var2);
            return this;
         case SHIFT_RIGHT:
            this.eShiftRight.run(var2);
            return this;
         }
      } catch (NullPointerException var4) {
         Iris.reportError(var4);
      } catch (Throwable var5) {
         Iris.reportError(var5);
         var5.printStackTrace();
      }

      return this;
   }

   public Element addLore(String loreLine) {
      this.getLore().add((Object)var1);
      return this;
   }

   public Element setBackground(boolean bg) {
      this.bg = var1;
      return this;
   }

   public boolean isBackgrond() {
      return this.bg;
   }

   public Element setCount(int c) {
      this.count = this.clip((double)var1, 1.0D, 64.0D).intValue();
      return this;
   }

   public int getCount() {
      return this.count;
   }

   public ItemStack computeItemStack() {
      try {
         ItemStack var1 = new ItemStack(this.getMaterial().getMaterial(), this.getCount(), this.getEffectiveDurability());
         ItemMeta var2 = var1.getItemMeta();
         var2.setDisplayName(this.getName());
         var2.setLore(this.getLore().copy());
         if (this.isEnchanted()) {
            var2.addEnchant(Enchantment.FIRE_ASPECT, 1, true);
         }

         var1.setItemMeta(var2);
         return var1;
      } catch (Throwable var3) {
         Iris.reportError(var3);
         var3.printStackTrace();
         return null;
      }
   }

   public Element setProgress(double progress) {
      this.progress = this.clip(var1, 0.0D, 1.0D);
      return this;
   }

   public double getProgress() {
      return this.progress;
   }

   public short getEffectiveDurability() {
      if (this.getMaterial().getMaterial().getMaxDurability() == 0) {
         return 0;
      } else {
         int var1 = (int)((double)this.getMaterial().getMaterial().getMaxDurability() * (1.0D - this.getProgress()));
         return this.clip((double)var1, 1.0D, (double)(this.getMaterial().getMaterial().getMaxDurability() - 1)).shortValue();
      }
   }
}
