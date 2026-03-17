package com.bergerkiller.bukkit.tc.controller.functions.inputs;

import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import com.bergerkiller.bukkit.common.map.MapCanvas;
import com.bergerkiller.bukkit.common.map.MapFont;
import com.bergerkiller.bukkit.tc.attachments.api.Attachment;
import com.bergerkiller.bukkit.tc.attachments.control.CartAttachmentSequencer;
import com.bergerkiller.bukkit.tc.attachments.ui.MapWidgetSelectionBox;
import com.bergerkiller.bukkit.tc.controller.functions.TransferFunction;
import com.bergerkiller.bukkit.tc.controller.functions.TransferFunctionHost;
import com.bergerkiller.bukkit.tc.controller.functions.ui.MapWidgetTransferFunctionItem;
import java.util.function.Function;

public class TransferFunctionInputSequencerPlayState extends TransferFunctionInput {
   public static final TransferFunction.Serializer<TransferFunctionInputSequencerPlayState> SERIALIZER = new TransferFunction.Serializer<TransferFunctionInputSequencerPlayState>() {
      public String typeId() {
         return "INPUT-SEQUENCER-PLAY-STATE";
      }

      public String title() {
         return "In: Play State";
      }

      public boolean isInput() {
         return true;
      }

      public boolean isListed(TransferFunctionHost host) {
         return host.isSequencer();
      }

      public TransferFunctionInputSequencerPlayState createNew(TransferFunctionHost host) {
         TransferFunctionInputSequencerPlayState function = new TransferFunctionInputSequencerPlayState(TransferFunctionInputSequencerPlayState.Mode.SPEED);
         function.updateSource(host);
         return function;
      }

      public TransferFunctionInputSequencerPlayState load(TransferFunctionHost host, ConfigurationNode config) {
         TransferFunctionInputSequencerPlayState.Mode mode = (TransferFunctionInputSequencerPlayState.Mode)config.getOrDefault("mode", TransferFunctionInputSequencerPlayState.Mode.SPEED);
         TransferFunctionInputSequencerPlayState function = new TransferFunctionInputSequencerPlayState(mode);
         function.updateSource(host);
         return function;
      }

      public void save(TransferFunctionHost host, ConfigurationNode config, TransferFunctionInputSequencerPlayState function) {
         config.set("mode", function.getMode());
      }
   };
   private TransferFunctionInputSequencerPlayState.Mode mode;

   public TransferFunctionInputSequencerPlayState(TransferFunctionInputSequencerPlayState.Mode mode) {
      this.mode = mode;
   }

   public TransferFunctionInputSequencerPlayState.Mode getMode() {
      return this.mode;
   }

   public void setMode(TransferFunctionInputSequencerPlayState.Mode mode) {
      this.mode = mode;
   }

   public TransferFunction.Serializer<? extends TransferFunction> getSerializer() {
      return SERIALIZER;
   }

   public TransferFunctionInput.ReferencedSource createSource(TransferFunctionHost host) {
      Attachment attachment = host.getAttachment();
      return attachment instanceof CartAttachmentSequencer ? this.mode.createReferencedSource((CartAttachmentSequencer)attachment) : TransferFunctionInput.ReferencedSource.NONE;
   }

   protected TransferFunctionInput cloneInput() {
      return new TransferFunctionInputSpeed();
   }

   public boolean isBooleanOutput() {
      return false;
   }

   public void drawPreview(MapWidgetTransferFunctionItem widget, MapCanvas view) {
      view.draw(MapFont.MINECRAFT, 0, 3, (byte)30, this.mode.previewTitle());
   }

   public void openDialog(final TransferFunction.Dialog dialog) {
      super.openDialog(dialog);
      ((<undefinedtype>)dialog.addWidget(new MapWidgetSelectionBox() {
         private boolean loading = false;

         public void onAttached() {
            this.loading = true;
            TransferFunctionInputSequencerPlayState.Mode[] var1 = TransferFunctionInputSequencerPlayState.Mode.values();
            int var2 = var1.length;

            for(int var3 = 0; var3 < var2; ++var3) {
               TransferFunctionInputSequencerPlayState.Mode mode = var1[var3];
               this.addItem(mode.title());
               if (mode == TransferFunctionInputSequencerPlayState.this.mode) {
                  this.setSelectedIndex(this.getItemCount() - 1);
               }
            }

            super.onAttached();
            this.loading = false;
         }

         public void onSelectedItemChanged() {
            if (!this.loading && this.getSelectedIndex() >= 0 && this.getSelectedIndex() < TransferFunctionInputSequencerPlayState.Mode.values().length) {
               TransferFunctionInputSequencerPlayState.this.setMode(TransferFunctionInputSequencerPlayState.Mode.values()[this.getSelectedIndex()]);
               TransferFunctionInputSequencerPlayState.this.updateSource(dialog.getHost());
               dialog.markChanged();
            }

         }
      })).setBounds(4, 18, dialog.getWidth() - 8, 11);
   }

   public static enum Mode {
      VOLUME("Volume", "<Play Volume>", TransferFunctionInputSequencerPlayState.EffectOptionsVolumeReferencedSource::new),
      SPEED("Speed", "<Play Speed>", TransferFunctionInputSequencerPlayState.EffectOptionsSpeedReferencedSource::new),
      PROGRESSION("Progression", "<Play Progress>", TransferFunctionInputSequencerPlayState.ProgressionReferencedSource::new);

      private final String title;
      private final String previewTitle;
      private final Function<CartAttachmentSequencer, TransferFunctionInput.ReferencedSource> sourceFactory;

      private Mode(String title, String previewTitle, Function<CartAttachmentSequencer, TransferFunctionInput.ReferencedSource> sourceFactory) {
         this.title = title;
         this.previewTitle = previewTitle;
         this.sourceFactory = sourceFactory;
      }

      public String title() {
         return this.title;
      }

      public String previewTitle() {
         return this.previewTitle;
      }

      public TransferFunctionInput.ReferencedSource createReferencedSource(CartAttachmentSequencer sequencer) {
         return (TransferFunctionInput.ReferencedSource)this.sourceFactory.apply(sequencer);
      }

      // $FF: synthetic method
      private static TransferFunctionInputSequencerPlayState.Mode[] $values() {
         return new TransferFunctionInputSequencerPlayState.Mode[]{VOLUME, SPEED, PROGRESSION};
      }
   }

   private static class ProgressionReferencedSource extends TransferFunctionInput.ReferencedSource {
      private final CartAttachmentSequencer sequencer;

      public ProgressionReferencedSource(CartAttachmentSequencer sequencer) {
         this.sequencer = sequencer;
      }

      public void onTick() {
         this.value = this.sequencer.getProgression();
      }

      public boolean isTickedDuringPlay() {
         return true;
      }

      public boolean equals(Object o) {
         return o instanceof TransferFunctionInputSequencerPlayState.ProgressionReferencedSource;
      }
   }

   private static class EffectOptionsSpeedReferencedSource extends TransferFunctionInput.ReferencedSource {
      private final CartAttachmentSequencer sequencer;

      public EffectOptionsSpeedReferencedSource(CartAttachmentSequencer sequencer) {
         this.sequencer = sequencer;
      }

      public void onTick() {
         this.value = this.sequencer.getCurrentPlayOptions().speed();
      }

      public boolean isTickedDuringPlay() {
         return true;
      }

      public boolean equals(Object o) {
         return o instanceof TransferFunctionInputSequencerPlayState.EffectOptionsSpeedReferencedSource;
      }
   }

   private static class EffectOptionsVolumeReferencedSource extends TransferFunctionInput.ReferencedSource {
      private final CartAttachmentSequencer sequencer;

      public EffectOptionsVolumeReferencedSource(CartAttachmentSequencer sequencer) {
         this.sequencer = sequencer;
      }

      public void onTick() {
         this.value = this.sequencer.getCurrentPlayOptions().volume();
      }

      public boolean isTickedDuringPlay() {
         return true;
      }

      public boolean equals(Object o) {
         return o instanceof TransferFunctionInputSequencerPlayState.EffectOptionsVolumeReferencedSource;
      }
   }
}
