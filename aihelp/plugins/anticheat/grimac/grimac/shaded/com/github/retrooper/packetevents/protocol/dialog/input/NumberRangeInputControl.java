package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.dialog.input;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTFloat;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTInt;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTNumber;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTString;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.adventure.AdventureSerializer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class NumberRangeInputControl implements InputControl {
   private final int width;
   private final Component label;
   private final String labelFormat;
   private final NumberRangeInputControl.RangeInfo rangeInfo;

   public NumberRangeInputControl(int width, Component label, String labelFormat, NumberRangeInputControl.RangeInfo rangeInfo) {
      this.width = width;
      this.label = label;
      this.labelFormat = labelFormat;
      this.rangeInfo = rangeInfo;
   }

   public static NumberRangeInputControl decode(NBTCompound compound, PacketWrapper<?> wrapper) {
      int width = compound.getNumberTagValueOrDefault("width", 200).intValue();
      Component label = (Component)compound.getOrThrow("label", AdventureSerializer.serializer(wrapper), wrapper);
      String labelFormat = compound.getStringTagValueOrDefault("label_format", "options.generic_value");
      NumberRangeInputControl.RangeInfo rangeInfo = NumberRangeInputControl.RangeInfo.decode(compound, wrapper);
      return new NumberRangeInputControl(width, label, labelFormat, rangeInfo);
   }

   public static void encode(NBTCompound compound, PacketWrapper<?> wrapper, NumberRangeInputControl control) {
      if (control.width != 200) {
         compound.setTag("width", new NBTInt(control.width));
      }

      compound.set("label", control.label, AdventureSerializer.serializer(wrapper), wrapper);
      if (!"options.generic_value".equals(control.labelFormat)) {
         compound.setTag("label_format", new NBTString(control.labelFormat));
      }

      NumberRangeInputControl.RangeInfo.encode(compound, wrapper, control.rangeInfo);
   }

   public InputControlType<?> getType() {
      return InputControlTypes.NUMBER_RANGE;
   }

   public int getWidth() {
      return this.width;
   }

   public Component getLabel() {
      return this.label;
   }

   public String getLabelFormat() {
      return this.labelFormat;
   }

   public NumberRangeInputControl.RangeInfo getRangeInfo() {
      return this.rangeInfo;
   }

   public static final class RangeInfo {
      private final float start;
      private final float end;
      @Nullable
      private final Float initial;
      @Nullable
      private final Float step;

      public RangeInfo(float start, float end, @Nullable Float initial, @Nullable Float step) {
         if (initial != null) {
            float min = Math.min(start, end);
            float max = Math.max(start, end);
            if (initial < min || initial > max) {
               throw new IllegalArgumentException("Initial value " + initial + " is outside of range [" + min + ", " + max + "]");
            }
         }

         this.start = start;
         this.end = end;
         this.initial = initial;
         this.step = step;
      }

      public static NumberRangeInputControl.RangeInfo decode(NBTCompound compound, PacketWrapper<?> wrapper) {
         float start = compound.getNumberTagValueOrThrow("start").floatValue();
         float end = compound.getNumberTagValueOrThrow("end").floatValue();
         NBTNumber initialTag = compound.getNumberTagOrNull("initial");
         Float initial = initialTag != null ? initialTag.getAsFloat() : null;
         NBTNumber stepTag = compound.getNumberTagOrNull("step");
         Float step = stepTag != null ? stepTag.getAsFloat() : null;
         return new NumberRangeInputControl.RangeInfo(start, end, initial, step);
      }

      public static void encode(NBTCompound compound, PacketWrapper<?> wrapper, NumberRangeInputControl.RangeInfo rangeInfo) {
         compound.setTag("start", new NBTFloat(rangeInfo.start));
         compound.setTag("end", new NBTFloat(rangeInfo.end));
         if (rangeInfo.initial != null) {
            compound.setTag("initial", new NBTFloat(rangeInfo.initial));
         }

         if (rangeInfo.step != null) {
            compound.setTag("step", new NBTFloat(rangeInfo.step));
         }

      }

      public float getStart() {
         return this.start;
      }

      public float getEnd() {
         return this.end;
      }

      @Nullable
      public Float getInitial() {
         return this.initial;
      }

      @Nullable
      public Float getStep() {
         return this.step;
      }
   }
}
