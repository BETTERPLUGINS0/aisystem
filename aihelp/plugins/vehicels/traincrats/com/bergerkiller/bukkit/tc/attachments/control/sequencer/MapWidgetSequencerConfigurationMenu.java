package com.bergerkiller.bukkit.tc.attachments.control.sequencer;

import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import com.bergerkiller.bukkit.common.map.widgets.MapWidget;
import com.bergerkiller.bukkit.tc.TrainCarts;
import com.bergerkiller.bukkit.tc.attachments.api.Attachment;
import com.bergerkiller.bukkit.tc.attachments.api.AttachmentSelector;
import com.bergerkiller.bukkit.tc.attachments.control.effect.EffectLoop;
import com.bergerkiller.bukkit.tc.attachments.ui.MapWidgetScroller;
import com.bergerkiller.bukkit.tc.controller.functions.TransferFunctionHost;
import java.util.Iterator;
import java.util.List;

public abstract class MapWidgetSequencerConfigurationMenu extends MapWidgetScroller {
   public MapWidgetSequencerEffectGroup startGroup;
   public MapWidgetSequencerEffectGroup loopGroup;
   public MapWidgetSequencerEffectGroup stopGroup;
   protected int effectSelButtonIndex = 1;
   private final EffectLoop.Player previewEffectLoopPlayer;

   public MapWidgetSequencerConfigurationMenu() {
      this.setScrollPadding(15);
      this.previewEffectLoopPlayer = TrainCarts.plugin.getEffectLoopPlayerController().createPlayer(20);
   }

   public abstract ConfigurationNode getConfig();

   public abstract List<String> getEffectNames(AttachmentSelector<Attachment.EffectAttachment> var1);

   public abstract TransferFunctionHost getTransferFunctionHost();

   public abstract Attachment.EffectSink createEffectSink(AttachmentSelector<Attachment.EffectAttachment> var1);

   public abstract SequencerPlayStatus getPlayStatus();

   public abstract void startPlaying();

   public abstract void stopPlaying();

   public EffectLoop.Player getPreviewEffectLoopPlayer() {
      return this.previewEffectLoopPlayer;
   }

   public void onAttached() {
      ((MapWidgetSequencerTopHeader)this.addContainerWidget(new MapWidgetSequencerTopHeader())).setSize(this.getWidth(), 7);
      this.startGroup = (MapWidgetSequencerEffectGroup)this.addContainerWidget(new MapWidgetSequencerEffectGroup(this, SequencerMode.START));
      this.loopGroup = (MapWidgetSequencerEffectGroup)this.addContainerWidget(new MapWidgetSequencerEffectGroup(this, SequencerMode.LOOP));
      this.stopGroup = (MapWidgetSequencerEffectGroup)this.addContainerWidget(new MapWidgetSequencerEffectGroup(this, SequencerMode.STOP));
      this.recalculateContainerSize();
   }

   public void recalculateContainerSize() {
      int y = 0;
      Iterator var2 = this.getContainer().getWidgets().iterator();

      while(var2.hasNext()) {
         MapWidget w = (MapWidget)var2.next();
         w.setPosition(0, y);
         y += w.getHeight() + 2;
         if (w instanceof MapWidgetSequencerTopHeader) {
            ++y;
         }
      }

      super.recalculateContainerSize();
   }
}
