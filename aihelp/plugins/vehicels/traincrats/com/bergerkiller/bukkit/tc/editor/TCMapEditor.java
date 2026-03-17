package com.bergerkiller.bukkit.tc.editor;

import com.bergerkiller.bukkit.common.BlockLocation;
import com.bergerkiller.bukkit.common.events.map.MapKeyEvent;
import com.bergerkiller.bukkit.common.map.MapBlendMode;
import com.bergerkiller.bukkit.common.map.MapColorPalette;
import com.bergerkiller.bukkit.common.map.MapDisplay;
import com.bergerkiller.bukkit.common.map.MapFont;
import com.bergerkiller.bukkit.common.map.MapSessionMode;
import com.bergerkiller.bukkit.common.map.MapTexture;
import com.bergerkiller.bukkit.common.map.MapFont.Alignment;
import com.bergerkiller.bukkit.common.map.MapPlayerInput.Key;
import com.bergerkiller.bukkit.common.utils.BlockUtil;
import com.bergerkiller.bukkit.common.utils.MaterialUtil;
import com.bergerkiller.bukkit.tc.Util;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.bukkit.Effect;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class TCMapEditor extends MapDisplay {
   Player owner;
   MapTexture background;
   EditedSign sign = new EditedSign();
   ArrayList<MapControl> controls = new ArrayList();
   int selectedIndex = 0;
   RailsTexture texture;
   Block railsBlock = null;

   public Block getRailsBlock() {
      return this.railsBlock;
   }

   public Player getOwner() {
      return this.owner;
   }

   public void addControl(MapControl control) {
      this.controls.add(control);
      if (this.controls.size() == 1) {
         control.setSelected(true);
         this.selectedIndex = 0;
      }

      control.bind(this);
   }

   public void onAttached() {
      this.setGlobal(false);
      this.setSessionMode(MapSessionMode.VIEWING);
      this.setReceiveInputWhenHolding(true);
      this.owner = (Player)this.getOwners().get(0);
      this.texture = new RailsTexture();
      List<Block> signBlocks = new ArrayList();
      BlockLocation searchLocation = this.getCommonMapItem().getCustomData().getBlockLocation("selected");
      if (searchLocation != null) {
         Block searchBlock = searchLocation.getBlock();
         this.railsBlock = searchBlock;
         if ((Boolean)MaterialUtil.ISSIGN.get(searchBlock)) {
            signBlocks.add(searchBlock);
            this.railsBlock = Util.getRailsFromSign(searchBlock);
         } else {
            Util.getSignsFromRails(signBlocks, searchBlock);
         }
      }

      if (!signBlocks.isEmpty()) {
         this.sign.load(BlockUtil.getSign((Block)signBlocks.get(0)));
      }

      this.background = this.loadTexture("com/bergerkiller/bukkit/tc/textures/background.png");
      this.getLayer().setBlendMode(MapBlendMode.NONE);
      this.getLayer().draw(this.background, 0, 0);
      if (this.sign.isValid()) {
         this.getLayer(1).setBlendMode(MapBlendMode.NONE);
         this.getLayer(1).setAlignment(Alignment.MIDDLE);
         this.getLayer(1).draw(MapFont.MINECRAFT, 64, 5, MapColorPalette.getColor(255, 0, 0), this.sign.getName());
         this.sign.initEditor(this);
      } else {
         this.getLayer(1).setBlendMode(MapBlendMode.NONE);
         this.getLayer(1).setAlignment(Alignment.MIDDLE);
         this.getLayer(1).draw(MapFont.MINECRAFT, 64, 5, MapColorPalette.getColor(255, 0, 0), "No sign selected");
      }

   }

   public void playClick() {
      this.owner.getWorld().playEffect(this.owner.getLocation(), Effect.CLICK2, 0);
   }

   public void onDetached() {
      TCMapControl.updateMapItem(this.owner, this.getMapItem(), false);
   }

   public void onKeyPressed(MapKeyEvent event) {
      if (event.getKey() == Key.BACK) {
         TCMapControl.updateMapItem(event.getPlayer(), this.getMapItem(), false);
      }

      if (this.controls.size() > 1) {
         if (event.getKey() == Key.LEFT) {
            ((MapControl)this.controls.get(this.selectedIndex)).setSelected(false);
            if (--this.selectedIndex < 0) {
               this.selectedIndex = this.controls.size() - 1;
            }

            ((MapControl)this.controls.get(this.selectedIndex)).setSelected(true);
            this.playClick();
         } else if (event.getKey() == Key.RIGHT) {
            ((MapControl)this.controls.get(this.selectedIndex)).setSelected(false);
            if (++this.selectedIndex >= this.controls.size()) {
               this.selectedIndex = 0;
            }

            ((MapControl)this.controls.get(this.selectedIndex)).setSelected(true);
            this.playClick();
         }
      }

      if (this.controls.size() > 0 && (event.getKey() == Key.DOWN || event.getKey() == Key.UP || event.getKey() == Key.ENTER)) {
         ((MapControl)this.controls.get(this.selectedIndex)).onKeyPressed(event);
         this.playClick();
      }

   }

   public void onTick() {
      Iterator var1 = this.controls.iterator();

      while(var1.hasNext()) {
         MapControl control = (MapControl)var1.next();
         control.onTick();
      }

   }
}
