package com.bergerkiller.bukkit.tc.controller.functions.inputs;

import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import com.bergerkiller.bukkit.common.map.MapCanvas;
import com.bergerkiller.bukkit.common.map.MapFont;
import com.bergerkiller.bukkit.common.map.widgets.MapWidgetButton;
import com.bergerkiller.bukkit.common.math.Matrix4x4;
import com.bergerkiller.bukkit.common.resources.SoundEffect;
import com.bergerkiller.bukkit.common.utils.MathUtil;
import com.bergerkiller.bukkit.tc.controller.MinecartMember;
import com.bergerkiller.bukkit.tc.controller.functions.TransferFunction;
import com.bergerkiller.bukkit.tc.controller.functions.TransferFunctionHost;
import com.bergerkiller.bukkit.tc.controller.functions.ui.MapWidgetTransferFunctionItem;
import org.bukkit.util.Vector;

public class TransferFunctionInputSpeed extends TransferFunctionInput {
   public static final TransferFunction.Serializer<TransferFunctionInputSpeed> SERIALIZER = new TransferFunction.Serializer<TransferFunctionInputSpeed>() {
      public String typeId() {
         return "INPUT-SPEED";
      }

      public String title() {
         return "In: Move Speed";
      }

      public boolean isInput() {
         return true;
      }

      public TransferFunctionInputSpeed createNew(TransferFunctionHost host) {
         TransferFunctionInputSpeed speed = new TransferFunctionInputSpeed();
         speed.updateSource(host);
         return speed;
      }

      public TransferFunctionInputSpeed load(TransferFunctionHost host, ConfigurationNode config) {
         TransferFunctionInputSpeed speed = new TransferFunctionInputSpeed();
         speed.setSourceMode((TransferFunctionInputSpeed.SourceMode)config.getOrDefault("mode", TransferFunctionInputSpeed.SourceMode.TRAIN));
         speed.setOutputMode((TransferFunctionInputSpeed.OutputMode)config.getOrDefault("output", TransferFunctionInputSpeed.OutputMode.SPEED));
         speed.updateSource(host);
         return speed;
      }

      public void save(TransferFunctionHost host, ConfigurationNode config, TransferFunctionInputSpeed function) {
         config.set("mode", function.sourceMode);
         config.set("output", function.outputMode);
      }
   };
   private TransferFunctionInputSpeed.SourceMode sourceMode;
   private TransferFunctionInputSpeed.OutputMode outputMode;

   public TransferFunctionInputSpeed() {
      this.sourceMode = TransferFunctionInputSpeed.SourceMode.TRAIN;
      this.outputMode = TransferFunctionInputSpeed.OutputMode.SPEED;
   }

   public void setSourceMode(TransferFunctionInputSpeed.SourceMode mode) {
      this.sourceMode = mode;
   }

   public void setOutputMode(TransferFunctionInputSpeed.OutputMode mode) {
      this.outputMode = mode;
   }

   public TransferFunction.Serializer<? extends TransferFunction> getSerializer() {
      return SERIALIZER;
   }

   public TransferFunctionInput.ReferencedSource createSource(TransferFunctionHost host) {
      Object result;
      if (this.sourceMode == TransferFunctionInputSpeed.SourceMode.TRAIN) {
         MinecartMember<?> member = host.getMember();
         if (member != null) {
            result = new TransferFunctionInputSpeed.TrainSpeedReferencedSource(member);
         } else {
            result = TransferFunctionInput.ReferencedSource.NONE;
         }
      } else {
         result = new TransferFunctionInputSpeed.AttachmentSpeedReferencedSource();
      }

      if (this.outputMode == TransferFunctionInputSpeed.OutputMode.ACCELERATION) {
         result = new TransferFunctionInputSpeed.AccelerationReferencedSource((TransferFunctionInput.ReferencedSource)result);
      }

      return (TransferFunctionInput.ReferencedSource)result;
   }

   protected TransferFunctionInput cloneInput() {
      return new TransferFunctionInputSpeed();
   }

   public boolean isBooleanOutput() {
      return false;
   }

   public void drawPreview(MapWidgetTransferFunctionItem widget, MapCanvas view) {
      view.draw(MapFont.MINECRAFT, 0, 3, (byte)30, this.outputMode == TransferFunctionInputSpeed.OutputMode.SPEED ? "<Move Speed>" : "<Move Accel.>");
   }

   public void openDialog(final TransferFunction.Dialog dialog) {
      super.openDialog(dialog);
      dialog.addLabel(5, 20, (byte)18, "Speed of:");
      ((<undefinedtype>)dialog.addWidget(new MapWidgetButton() {
         public void onAttached() {
            this.updateText();
            super.onAttached();
         }

         public void onActivate() {
            this.display.playSound(SoundEffect.CLICK);
            TransferFunctionInputSpeed.this.sourceMode = TransferFunctionInputSpeed.SourceMode.values()[(TransferFunctionInputSpeed.this.sourceMode.ordinal() + 1) % TransferFunctionInputSpeed.SourceMode.values().length];
            TransferFunctionInputSpeed.this.updateSource(dialog.getHost());
            this.updateText();
            dialog.markChanged();
         }

         private void updateText() {
            this.setText(TransferFunctionInputSpeed.this.sourceMode.name());
         }
      })).setBounds(5, 27, 70, 13);
      dialog.addLabel(5, 43, (byte)18, "Output:");
      ((<undefinedtype>)dialog.addWidget(new MapWidgetButton() {
         public void onAttached() {
            this.updateText();
            super.onAttached();
         }

         public void onActivate() {
            this.display.playSound(SoundEffect.CLICK);
            TransferFunctionInputSpeed.this.outputMode = TransferFunctionInputSpeed.OutputMode.values()[(TransferFunctionInputSpeed.this.outputMode.ordinal() + 1) % TransferFunctionInputSpeed.SourceMode.values().length];
            TransferFunctionInputSpeed.this.updateSource(dialog.getHost());
            this.updateText();
            dialog.markChanged();
         }

         private void updateText() {
            this.setText(TransferFunctionInputSpeed.this.outputMode.name());
         }
      })).setBounds(5, 50, 70, 13);
   }

   public static enum OutputMode {
      SPEED,
      ACCELERATION;

      // $FF: synthetic method
      private static TransferFunctionInputSpeed.OutputMode[] $values() {
         return new TransferFunctionInputSpeed.OutputMode[]{SPEED, ACCELERATION};
      }
   }

   public static enum SourceMode {
      TRAIN,
      ATTACHMENT;

      // $FF: synthetic method
      private static TransferFunctionInputSpeed.SourceMode[] $values() {
         return new TransferFunctionInputSpeed.SourceMode[]{TRAIN, ATTACHMENT};
      }
   }

   private static class TrainSpeedReferencedSource extends TransferFunctionInput.ReferencedSource {
      private final MinecartMember<?> member;

      public TrainSpeedReferencedSource(MinecartMember<?> member) {
         this.member = member;
      }

      public void onTick() {
         this.value = this.member.isUnloaded() ? 0.0D : this.member.getRealSpeedLimited();
      }

      public boolean equals(Object o) {
         return o instanceof TransferFunctionInputSpeed.TrainSpeedReferencedSource;
      }
   }

   private static class AttachmentSpeedReferencedSource extends TransferFunctionInput.ReferencedSource {
      private final Vector prevPosition = new Vector();
      private boolean first = true;

      public AttachmentSpeedReferencedSource() {
      }

      public void onTransform(Matrix4x4 transform) {
         if (this.first) {
            this.first = false;
            MathUtil.setVector(this.prevPosition, transform.toVector());
            this.value = 0.0D;
         } else {
            Vector newPosition = transform.toVector();
            this.value = newPosition.distance(this.prevPosition);
            MathUtil.setVector(this.prevPosition, newPosition);
         }

      }

      public boolean equals(Object o) {
         return o instanceof TransferFunctionInputSpeed.AttachmentSpeedReferencedSource;
      }
   }

   private static class AccelerationReferencedSource extends TransferFunctionInput.ReferencedSource {
      private final TransferFunctionInput.ReferencedSource base;
      private double prevValue = Double.NaN;

      public AccelerationReferencedSource(TransferFunctionInput.ReferencedSource base) {
         this.base = base;
      }

      public void onTick() {
         this.base.onTick();
         double prevValue = this.prevValue;
         double newValue = this.base.value();
         this.value = Double.isNaN(prevValue) ? 0.0D : newValue - prevValue;
         this.prevValue = newValue;
      }

      public boolean equals(Object o) {
         return o instanceof TransferFunctionInputSpeed.AccelerationReferencedSource ? ((TransferFunctionInputSpeed.AccelerationReferencedSource)o).base.equals(this.base) : false;
      }
   }
}
