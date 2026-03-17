package com.bergerkiller.bukkit.tc.attachments.ui.block;

import com.bergerkiller.bukkit.common.events.map.MapKeyEvent;
import com.bergerkiller.bukkit.common.map.MapTexture;
import com.bergerkiller.bukkit.common.map.MapPlayerInput.Key;
import com.bergerkiller.bukkit.common.map.widgets.MapWidget;
import com.bergerkiller.bukkit.common.map.widgets.MapWidgetTabView.Tab;
import com.bergerkiller.bukkit.common.resources.SoundEffect;
import com.bergerkiller.bukkit.common.utils.WorldUtil;
import com.bergerkiller.bukkit.common.wrappers.BlockData;
import com.bergerkiller.bukkit.tc.TrainCarts;
import com.bergerkiller.bukkit.tc.attachments.ui.MapWidgetBlinkyButton;
import com.bergerkiller.bukkit.tc.attachments.ui.MapWidgetVerticalNavigableList;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public abstract class MapWidgetBlockDataSelector extends MapWidget implements BlockDataSelector {
   private final MapWidgetVerticalNavigableList blockOptions;
   private final MapWidgetBlockStateListTooltip blockStateListTooltip;
   private final MapWidgetBlockDataVariantList variantList;
   private final MapWidgetBlockGrid blockSelector;

   public MapWidgetBlockDataSelector() {
      this.setBounds(0, 0, 100, 103);
      this.setRetainChildWidgets(true);
      this.blockOptions = (MapWidgetVerticalNavigableList)this.addWidget(new MapWidgetVerticalNavigableList() {
         public void onLastItemDown(MapKeyEvent event) {
            MapWidgetBlockDataSelector.this.activateBlockGrid();
         }

         public void onNavigated(MapKeyEvent event, Tab tab) {
            if (tab.getIndex() == 0) {
               MapWidgetBlockDataSelector.this.blockStateListTooltip.setVisible(true);
            } else {
               MapWidgetBlockDataSelector.this.blockStateListTooltip.setVisible(false);
            }

         }
      });
      this.blockOptions.setBounds(0, 0, 100, 18);
      this.blockStateListTooltip = (MapWidgetBlockStateListTooltip)this.addWidget(new MapWidgetBlockStateListTooltip());
      this.blockStateListTooltip.setBounds(0, 18, 128, 0);
      this.variantList = new MapWidgetBlockDataVariantList() {
         public void onSelectedBlockDataChanged(BlockData blockData) {
            MapWidgetBlockDataSelector.this.blockStateListTooltip.setSelectedBlockData(blockData);
            MapWidgetBlockDataSelector.this.variantList.setSelectedBlockData(blockData);
            MapWidgetBlockDataSelector.this.onSelectedBlockDataChanged(blockData);
         }

         public void onKeyPressed(MapKeyEvent event) {
            if (event.getKey() == Key.ENTER) {
               MapWidgetBlockDataSelector.this.activateBlockGrid();
            } else {
               super.onKeyPressed(event);
            }

         }

         public boolean onItemDrop(Player player, ItemStack item) {
            return MapWidgetBlockDataSelector.this.onItemDrop(player, item);
         }
      };
      this.variantList.setPosition(0, 0);
      this.blockOptions.addTab().addWidget(this.variantList);
      this.blockOptions.setSelectedIndex(0);
      Tab var1 = this.blockOptions.addTab(new Tab() {
         private final MapTexture bg_texture;

         {
            this.bg_texture = MapTexture.loadPluginResource(TrainCarts.plugin, "com/bergerkiller/bukkit/tc/textures/attachments/item_options_bg.png");
         }

         public void onDraw() {
            this.view.draw(this.bg_texture, 0, 0);
         }
      });
      this.blockSelector = ((<undefinedtype>)this.addWidget(new MapWidgetBlockGrid() {
         public void onSelectedBlockDataChanged(BlockData blockData) {
            MapWidgetBlockDataSelector.this.variantList.setSelectedBlockData(blockData);
            MapWidgetBlockDataSelector.this.blockStateListTooltip.setSelectedBlockData(blockData);
            MapWidgetBlockDataSelector.this.onSelectedBlockDataChanged(blockData);
         }

         public void onKeyPressed(MapKeyEvent event) {
            if (event.getKey() != Key.BACK && event.getKey() != Key.ENTER) {
               super.onKeyPressed(event);
            } else {
               MapWidgetBlockDataSelector.this.blockStateListTooltip.setVisible(true);
               MapWidgetBlockDataSelector.this.variantList.focus();
            }

         }

         public boolean onItemDrop(Player player, ItemStack item) {
            return MapWidgetBlockDataSelector.this.onItemDrop(player, item);
         }

         public void onBlockInteract(PlayerInteractEvent event) {
            MapWidgetBlockDataSelector.this.onBlockInteract(event);
         }
      })).setDimensions(6, 4);
      this.blockSelector.addAllBlocks();
      this.blockSelector.setPosition(0, 20);
   }

   public void showBrightnessButton() {
      MapWidgetBlinkyButton brightnessButton = new MapWidgetBlinkyButton() {
         public void onClick() {
            MapWidgetBlockDataSelector.this.onBrightnessClicked();
         }
      };
      brightnessButton.setSize(14, 14);
      brightnessButton.setIcon("attachments/item_brightness.png");
      brightnessButton.setTooltip("Block Brightness");
      brightnessButton.setPosition(42, 2);
      this.blockOptions.getTab(1).addWidget(brightnessButton);
   }

   private void activateBlockGrid() {
      this.blockStateListTooltip.setVisible(false);
      this.blockOptions.setSelectedIndex(0);
      this.blockSelector.activate();
   }

   public BlockData getSelectedBlockData() {
      return this.variantList.getSelectedBlockData();
   }

   public MapWidgetBlockDataSelector setSelectedBlockData(BlockData blockData) {
      this.blockSelector.setSelectedBlockData(blockData);
      this.blockStateListTooltip.setSelectedBlockData(blockData);
      this.variantList.setSelectedBlockData(blockData);
      return this;
   }

   public boolean onItemDrop(Player player, ItemStack item) {
      BlockData data = BlockData.fromItemStack(item);
      if (data != null && data != BlockData.AIR) {
         this.setSelectedBlockData(data);
         this.display.playSound(SoundEffect.CLICK_WOOD);
         this.onSelectedBlockDataChanged(data);
         return true;
      } else {
         return false;
      }
   }

   public void onBlockInteract(PlayerInteractEvent event) {
      if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
         Block block = event.getClickedBlock();
         if (block != null) {
            BlockData data = WorldUtil.getBlockData(block);
            this.setSelectedBlockData(data);
            this.display.playSound(SoundEffect.CLICK_WOOD);
            this.onSelectedBlockDataChanged(data);
            event.setUseInteractedBlock(Result.DENY);
         }
      }

   }

   public void onBrightnessClicked() {
   }
}
