package com.volmit.iris.util.inventorygui;

import com.volmit.iris.Iris;
import com.volmit.iris.util.collection.KMap;
import com.volmit.iris.util.collection.KSet;
import com.volmit.iris.util.scheduling.Callback;
import com.volmit.iris.util.scheduling.J;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class UIWindow implements Window, Listener {
   private final Player viewer;
   private final KMap<Integer, Element> elements;
   private WindowDecorator decorator;
   private Callback<Window> eClose;
   private WindowResolution resolution;
   private String title;
   private boolean visible;
   private int viewportPosition;
   private int viewportSize;
   private int highestRow;
   private Inventory inventory;
   private int clickcheck = 0;
   private boolean doubleclicked = false;

   public UIWindow(Player viewer) {
      this.viewer = var1;
      this.elements = new KMap();
      this.setTitle("");
      this.setDecorator(new UIVoidDecorator());
      this.setResolution(WindowResolution.W9_H6);
      this.setViewportHeight(this.clip(3.0D, 1.0D, (double)this.getResolution().getMaxHeight()).intValue());
      this.setViewportPosition(0);
   }

   @EventHandler
   public void on(InventoryClickEvent e) {
      if (var1.getWhoClicked().equals(this.viewer)) {
         if (this.isVisible()) {
            if (this.viewer.getOpenInventory().getTitle().equals(this.title)) {
               if (var1.getClickedInventory() != null) {
                  if (var1.getView().getType().equals(this.getResolution().getType())) {
                     if (var1.getClickedInventory().getType().equals(this.getResolution().getType())) {
                        Element var2 = this.getElement(this.getLayoutPosition(var1.getSlot()), this.getLayoutRow(var1.getSlot()));
                        switch(var1.getAction()) {
                        case CLONE_STACK:
                        case UNKNOWN:
                        case SWAP_WITH_CURSOR:
                        case PLACE_SOME:
                        case PLACE_ONE:
                        case PLACE_ALL:
                        case PICKUP_SOME:
                        case PICKUP_ONE:
                        case PICKUP_HALF:
                        case PICKUP_ALL:
                        case NOTHING:
                        case MOVE_TO_OTHER_INVENTORY:
                        case HOTBAR_SWAP:
                        case HOTBAR_MOVE_AND_READD:
                        case DROP_ONE_SLOT:
                        case DROP_ONE_CURSOR:
                        case DROP_ALL_SLOT:
                        case DROP_ALL_CURSOR:
                        case COLLECT_TO_CURSOR:
                        default:
                           switch(var1.getClick()) {
                           case DOUBLE_CLICK:
                              this.doubleclicked = true;
                              break;
                           case LEFT:
                              ++this.clickcheck;
                              if (this.clickcheck == 1) {
                                 J.s(() -> {
                                    if (this.clickcheck == 1) {
                                       this.clickcheck = 0;
                                       if (var2 != null) {
                                          var2.call(ElementEvent.LEFT, var2);
                                       }
                                    }

                                 });
                              } else if (this.clickcheck == 2) {
                                 J.s(() -> {
                                    if (this.doubleclicked) {
                                       this.doubleclicked = false;
                                    } else {
                                       this.scroll(1);
                                    }

                                    this.clickcheck = 0;
                                 });
                              }
                              break;
                           case RIGHT:
                              if (var2 != null) {
                                 var2.call(ElementEvent.RIGHT, var2);
                              } else {
                                 this.scroll(-1);
                              }
                              break;
                           case SHIFT_LEFT:
                              if (var2 != null) {
                                 var2.call(ElementEvent.SHIFT_LEFT, var2);
                              }
                              break;
                           case SHIFT_RIGHT:
                              if (var2 != null) {
                                 var2.call(ElementEvent.SHIFT_RIGHT, var2);
                              }
                           case SWAP_OFFHAND:
                           case UNKNOWN:
                           case WINDOW_BORDER_RIGHT:
                           case WINDOW_BORDER_LEFT:
                           case NUMBER_KEY:
                           case MIDDLE:
                           case DROP:
                           case CREATIVE:
                           case CONTROL_DROP:
                           }
                        }
                     }

                     var1.setCancelled(true);
                  }
               }
            }
         }
      }
   }

   @EventHandler
   public void on(InventoryCloseEvent e) {
      if (var1.getPlayer().equals(this.viewer)) {
         if (var1.getPlayer().getOpenInventory().getTitle().equals(this.title)) {
            if (this.isVisible()) {
               this.close();
               this.callClosed();
            }

         }
      }
   }

   public WindowDecorator getDecorator() {
      return this.decorator;
   }

   public UIWindow setDecorator(WindowDecorator decorator) {
      this.decorator = var1;
      return this;
   }

   public UIWindow close() {
      this.setVisible(false);
      return this;
   }

   public UIWindow open() {
      this.setVisible(true);
      return this;
   }

   public boolean isVisible() {
      return this.visible;
   }

   public UIWindow setVisible(boolean visible) {
      if (this.isVisible() == var1) {
         return this;
      } else {
         if (var1) {
            Bukkit.getPluginManager().registerEvents(this, Iris.instance);
            if (this.getResolution().getType().equals(InventoryType.CHEST)) {
               this.inventory = Bukkit.createInventory((InventoryHolder)null, this.getViewportHeight() * 9, this.getTitle());
            } else {
               this.inventory = Bukkit.createInventory((InventoryHolder)null, this.getResolution().getType(), this.getTitle());
            }

            this.viewer.openInventory(this.inventory);
            this.visible = var1;
            this.updateInventory();
         } else {
            this.visible = var1;
            HandlerList.unregisterAll(this);
            this.viewer.closeInventory();
         }

         this.visible = var1;
         return this;
      }
   }

   public int getViewportPosition() {
      return this.viewportPosition;
   }

   public UIWindow setViewportPosition(int viewportPosition) {
      this.viewportPosition = var1;
      this.scroll(0);
      this.updateInventory();
      return this;
   }

   public int getMaxViewportPosition() {
      return Math.max(0, this.highestRow - this.getViewportHeight());
   }

   public UIWindow scroll(int direction) {
      this.viewportPosition = (int)this.clip((double)(this.viewportPosition + var1), 0.0D, (double)this.getMaxViewportPosition());
      this.updateInventory();
      return this;
   }

   public int getViewportHeight() {
      return this.viewportSize;
   }

   public UIWindow setViewportHeight(int height) {
      this.viewportSize = (int)this.clip((double)var1, 1.0D, (double)this.getResolution().getMaxHeight());
      if (this.isVisible()) {
         this.reopen();
      }

      return this;
   }

   public String getTitle() {
      return this.title;
   }

   public UIWindow setTitle(String title) {
      this.title = var1;
      if (this.isVisible()) {
         this.reopen();
      }

      return this;
   }

   public UIWindow setElement(int position, int row, Element e) {
      if (var2 > this.highestRow) {
         this.highestRow = var2;
      }

      this.elements.put(this.getRealPosition((int)this.clip((double)var1, (double)(-this.getResolution().getMaxWidthOffset()), (double)this.getResolution().getMaxWidthOffset()), var2), var3);
      this.updateInventory();
      return this;
   }

   public Element getElement(int position, int row) {
      return (Element)this.elements.get(this.getRealPosition((int)this.clip((double)var1, (double)(-this.getResolution().getMaxWidthOffset()), (double)this.getResolution().getMaxWidthOffset()), var2));
   }

   public Player getViewer() {
      return this.viewer;
   }

   public UIWindow onClosed(Callback<Window> window) {
      this.eClose = var1;
      return this;
   }

   public int getViewportSlots() {
      return this.getViewportHeight() * this.getResolution().getWidth();
   }

   public int getLayoutRow(int viewportSlottedPosition) {
      return this.getRow(this.getRealLayoutPosition(var1));
   }

   public int getLayoutPosition(int viewportSlottedPosition) {
      return this.getPosition(var1);
   }

   public int getRealLayoutPosition(int viewportSlottedPosition) {
      return this.getRealPosition(this.getPosition(var1), this.getRow(var1) + this.getViewportPosition());
   }

   public int getRealPosition(int position, int row) {
      return (int)((double)(var2 * this.getResolution().getWidth() + this.getResolution().getMaxWidthOffset()) + this.clip((double)var1, (double)(-this.getResolution().getMaxWidthOffset()), (double)this.getResolution().getMaxWidthOffset()));
   }

   public int getRow(int realPosition) {
      return var1 / this.getResolution().getWidth();
   }

   public int getPosition(int realPosition) {
      return var1 % this.getResolution().getWidth() - this.getResolution().getMaxWidthOffset();
   }

   public Window callClosed() {
      if (this.eClose != null) {
         this.eClose.run(this);
      }

      return this;
   }

   public boolean hasElement(int position, int row) {
      return this.getElement(var1, var2) != null;
   }

   public WindowResolution getResolution() {
      return this.resolution;
   }

   public Double clip(double value, double min, double max) {
      return Math.min(var5, Math.max(var3, var1));
   }

   public Window setResolution(WindowResolution resolution) {
      this.close();
      this.resolution = var1;
      this.setViewportHeight((int)this.clip((double)this.getViewportHeight(), 1.0D, (double)this.getResolution().getMaxHeight()));
      return this;
   }

   public Window clearElements() {
      this.highestRow = 0;
      this.elements.clear();
      this.updateInventory();
      return this;
   }

   public Window updateInventory() {
      if (this.isVisible()) {
         ItemStack[] var1 = this.inventory.getContents();
         KSet var2 = new KSet(new ItemStack[0]);

         for(int var3 = 0; var3 < var1.length; ++var3) {
            ItemStack var4 = var1[var3];
            ItemStack var5 = this.computeItemStack(var3);
            int var6 = this.getLayoutRow(var3);
            int var7 = this.getLayoutPosition(var3);
            if (var5 != null && !this.hasElement(var7, var6)) {
               ItemStack var8 = var5.clone();
               var8.setAmount(var8.getAmount() + 1);
               var2.add(var8);
            }

            if (var4 == null != (var5 == null) || var5 != null && var4 != null && !var4.equals(var5)) {
               this.inventory.setItem(var3, var5);
            }
         }
      }

      return this;
   }

   public ItemStack computeItemStack(int viewportSlot) {
      int var2 = this.getLayoutRow(var1);
      int var3 = this.getLayoutPosition(var1);
      Element var4 = this.hasElement(var3, var2) ? this.getElement(var3, var2) : this.getDecorator().onDecorateBackground(this, var3, var2);
      return var4 != null ? var4.computeItemStack() : null;
   }

   public Window reopen() {
      return this.close().open();
   }
}
