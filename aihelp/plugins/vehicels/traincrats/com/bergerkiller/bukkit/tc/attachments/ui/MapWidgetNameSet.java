package com.bergerkiller.bukkit.tc.attachments.ui;

import com.bergerkiller.bukkit.common.events.map.MapKeyEvent;
import com.bergerkiller.bukkit.common.map.MapCanvas;
import com.bergerkiller.bukkit.common.map.MapColorPalette;
import com.bergerkiller.bukkit.common.map.MapFont;
import com.bergerkiller.bukkit.common.map.MapFont.Alignment;
import com.bergerkiller.bukkit.common.map.MapPlayerInput.Key;
import com.bergerkiller.bukkit.common.map.widgets.MapWidget;
import com.bergerkiller.bukkit.common.map.widgets.MapWidgetButton;
import com.bergerkiller.bukkit.common.map.widgets.MapWidgetSubmitText;
import com.bergerkiller.bukkit.common.map.widgets.MapWidgetText;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public abstract class MapWidgetNameSet extends MapWidget {
   private static final int ROW_HEIGHT = 11;
   private static final int HSCROLL_DELAY_TICKS = 60;
   private static final int HSCROLL_HOLD_TICKS = 30;
   private static final int HSCROLL_PIXEL_STEPS = 2;
   private final List<MapWidgetNameSet.ListItem> items = new ArrayList();
   private final Set<String> uniqueItemNames = new LinkedHashSet();
   private int scrollOffset = 0;
   private int selectedIndex = 0;
   private int horScrollTicks = 0;
   private int numTicksOfNoScroll = 0;
   private String newNameDialogTitle = "Add a new item";
   private String newNameText = "+++ NEW +++";
   MapWidgetSubmitText newItemDialog;

   public MapWidgetNameSet() {
      this.setFocusable(true);
   }

   public abstract void onItemAdded(String var1);

   public abstract void onItemRemoved(String var1);

   public MapWidgetNameSet addItem(String item) {
      if (this.uniqueItemNames.add(item)) {
         this.items.add(new MapWidgetNameSet.ListItem(item));
         this.invalidate();
      }

      return this;
   }

   public MapWidgetNameSet setItems(Collection<String> items) {
      this.items.clear();
      this.uniqueItemNames.clear();
      Iterator var2 = items.iterator();

      while(var2.hasNext()) {
         String item = (String)var2.next();
         this.addItem(item);
      }

      return this;
   }

   public Set<String> getItems() {
      return Collections.unmodifiableSet(this.uniqueItemNames);
   }

   public MapWidgetNameSet setNewItemDescription(String title) {
      this.newNameDialogTitle = title;
      if (this.newItemDialog != null) {
         this.newItemDialog.setDescription(title);
      }

      return this;
   }

   public MapWidgetNameSet setNewItemText(String text) {
      this.newNameText = text;
      this.invalidate();
      return this;
   }

   public void onAttached() {
      this.newItemDialog = ((<undefinedtype>)this.addWidget(new MapWidgetSubmitText() {
         public void onAccept(String text) {
            text = text.trim();
            if (text.isEmpty()) {
               this.onCancel();
            } else {
               MapWidgetNameSet.ListItem newItem = new MapWidgetNameSet.ListItem(text);
               if (MapWidgetNameSet.this.items.contains(newItem)) {
                  MapWidgetNameSet.this.addWidget(new MapWidgetNameSet.ItemAlreadyAddedDialog());
               } else {
                  MapWidgetNameSet.this.uniqueItemNames.add(newItem.name);
                  MapWidgetNameSet.this.items.add(newItem);
                  MapWidgetNameSet.this.selectedIndex = MapWidgetNameSet.this.items.size();
                  MapWidgetNameSet.this.scrollToSelection();
                  MapWidgetNameSet.this.invalidate();
                  MapWidgetNameSet.this.onItemAdded(newItem.name);
               }

            }
         }
      })).setDescription(this.newNameDialogTitle);
   }

   public void onDraw() {
      int numVisibleItems = this.calcNumItems();
      byte gridColor = this.isFocused() ? 122 : 119;
      this.view.drawRectangle(0, 0, this.getWidth(), numVisibleItems * 11, (byte)gridColor);

      for(int i = 0; i < numVisibleItems; ++i) {
         int index = this.scrollOffset + i;
         boolean isNewIcon = index >= this.items.size();
         boolean isSelected = index == this.selectedIndex && this.isActivated();
         byte bgColor;
         if (isNewIcon) {
            bgColor = isSelected ? MapColorPalette.getColor(0, 160, 0) : MapColorPalette.getColor(0, 64, 0);
         } else if (isSelected) {
            bgColor = MapColorPalette.getColor(128, 128, 128);
         } else {
            bgColor = (index & 1) == 1 ? MapColorPalette.getColor(32, 32, 32) : MapColorPalette.getColor(64, 64, 64);
         }

         this.view.fillRectangle(1, i * 11 + 1, this.getWidth() - 2, 10, bgColor);
         this.view.drawLine(1, (i + 1) * 11, this.getWidth() - 2, (i + 1) * 11, (byte)gridColor);
         if (isNewIcon) {
            this.view.setAlignment(Alignment.MIDDLE);
            this.view.draw(MapFont.MINECRAFT, this.getWidth() / 2, i * 11 + 2, (byte)18, this.newNameText);
            break;
         }

         MapWidgetNameSet.ListItem listItem = (MapWidgetNameSet.ListItem)this.items.get(index);
         this.view.setAlignment(Alignment.LEFT);
         this.view.getView(2, i * 11 + 2, this.getWidth() - 3, 8).draw(MapFont.MINECRAFT, -listItem.horOffset, 0, (byte)34, listItem.name);
      }

   }

   public void onKeyPressed(MapKeyEvent event) {
      if (this.isActivated() && event.getKey() != Key.BACK) {
         if (event.getKey() == Key.ENTER) {
            if (this.selectedIndex < this.items.size()) {
               this.addWidget(new MapWidgetNameSet.ConfirmItemDeleteDialog() {
                  public void onConfirmDelete() {
                     this.invalidate();
                     String nameRemoved = ((MapWidgetNameSet.ListItem)MapWidgetNameSet.this.items.remove(MapWidgetNameSet.this.selectedIndex)).name;
                     MapWidgetNameSet.this.uniqueItemNames.remove(nameRemoved);
                     MapWidgetNameSet.this.onItemRemoved(nameRemoved);
                  }
               });
            } else {
               this.newItemDialog.activate();
            }
         } else if (event.getKey() == Key.UP) {
            if (this.selectedIndex > 0) {
               --this.selectedIndex;
               this.scrollToSelection();
               this.invalidate();
            }
         } else if (event.getKey() == Key.DOWN && this.selectedIndex < this.items.size()) {
            ++this.selectedIndex;
            this.scrollToSelection();
            this.invalidate();
         }
      } else {
         super.onKeyPressed(event);
      }

   }

   public void onTick() {
      if (this.numTicksOfNoScroll > 0) {
         if (++this.numTicksOfNoScroll > 30) {
            this.resetHScroll();
         }
      } else if (++this.horScrollTicks >= 60) {
         int numVisibleItems = this.calcNumItems();
         int textViewWidth = this.getWidth() - 3;
         boolean scrolled = false;

         for(int i = 0; i < numVisibleItems; ++i) {
            int index = this.scrollOffset + i;
            if (index < this.items.size()) {
               scrolled |= ((MapWidgetNameSet.ListItem)this.items.get(index)).scrollLeft(this.view, textViewWidth);
            }
         }

         if (scrolled) {
            this.invalidate();
         } else {
            this.numTicksOfNoScroll = 1;
         }
      }

   }

   private void resetHScroll() {
      if (this.horScrollTicks > 60) {
         boolean changed = false;
         Iterator var2 = this.items.iterator();

         while(var2.hasNext()) {
            MapWidgetNameSet.ListItem item = (MapWidgetNameSet.ListItem)var2.next();
            if (item.horOffset > 0) {
               item.horOffset = 0;
               changed = true;
            }
         }

         if (changed) {
            this.invalidate();
         }
      }

      this.horScrollTicks = 0;
      this.numTicksOfNoScroll = 0;
   }

   private void scrollToSelection() {
      int numItems = this.calcNumItems();
      if (this.selectedIndex < this.scrollOffset) {
         this.scrollOffset = this.selectedIndex;
      } else if (this.selectedIndex - numItems + 1 > this.scrollOffset) {
         this.scrollOffset = this.selectedIndex - numItems + 1;
      }

   }

   private int calcNumItems() {
      return (this.getHeight() - 1) / 11;
   }

   private static class ListItem {
      public final String name;
      private int width = -1;
      public int horOffset = 0;

      public ListItem(String name) {
         this.name = name;
      }

      public boolean scrollLeft(MapCanvas view, int textViewWidth) {
         int cutOff = this.getWidth(view) - textViewWidth - this.horOffset;
         if (cutOff > 0) {
            this.horOffset += Math.min(cutOff, 2);
            return true;
         } else {
            return false;
         }
      }

      public int getWidth(MapCanvas view) {
         int w = this.width;
         if (this.width == -1) {
            this.width = w = view.calcFontSize(MapFont.MINECRAFT, this.name).width;
         }

         return w;
      }

      public int hashCode() {
         return this.name.hashCode();
      }

      public boolean equals(Object o) {
         return ((MapWidgetNameSet.ListItem)o).name.equals(this.name);
      }
   }

   private static class ConfirmItemDeleteDialog extends MapWidgetMenu {
      public ConfirmItemDeleteDialog() {
         this.setBackgroundColor(MapColorPalette.getColor(135, 33, 33));
         this.setSize(90, 40);
      }

      public void onAttached() {
         super.onAttached();
         this.setPosition((this.parent.getWidth() - this.getWidth()) / 2, (this.parent.getHeight() - this.getHeight()) / 2);
         this.addWidget((new MapWidgetText()).setText("Delete this item?").setBounds(5, 5, 80, 30));
         this.addWidget((new MapWidgetButton() {
            public void onActivate() {
               ConfirmItemDeleteDialog.this.close();
            }
         }).setText("No").setBounds(6, 21, 36, 13));
         this.addWidget((new MapWidgetButton() {
            public void onActivate() {
               ConfirmItemDeleteDialog.this.close();
               ConfirmItemDeleteDialog.this.onConfirmDelete();
            }
         }).setText("Yes").setBounds(48, 21, 36, 13));
      }

      public void onConfirmDelete() {
      }
   }

   private static class ItemAlreadyAddedDialog extends MapWidgetMenu {
      public ItemAlreadyAddedDialog() {
         this.setBackgroundColor(MapColorPalette.getColor(135, 33, 33));
         this.setSize(90, 46);
      }

      public void onAttached() {
         super.onAttached();
         this.setPosition((this.parent.getWidth() - this.getWidth()) / 2, (this.parent.getHeight() - this.getHeight()) / 2);
         this.addWidget((new MapWidgetText()).setText("This item was\nalready added!").setBounds(5, 5, 80, 30));
         this.addWidget((new MapWidgetButton() {
            public void onActivate() {
               ItemAlreadyAddedDialog.this.close();
            }
         }).setText("OK").setBounds(27, 27, 36, 13));
      }

      public void onConfirmDelete() {
      }
   }
}
