package com.bergerkiller.bukkit.tc.attachments.control.effect.midi;

import com.bergerkiller.bukkit.common.utils.ParseUtil;

public class MidiTimeSignature {
   private final int beatsPerMeasure;
   private final int noteValue;
   private final int notesPerMeasure;
   public static final MidiTimeSignature COMMON = of(4, 4);

   public static MidiTimeSignature of(int beatsPerMeasure, int noteValue) {
      return new MidiTimeSignature(beatsPerMeasure, noteValue);
   }

   private MidiTimeSignature(int beatsPerMeasure, int noteValue) {
      if (beatsPerMeasure < 1) {
         throw new IllegalArgumentException("Invalid number of beats per measure: " + beatsPerMeasure);
      } else if (noteValue < 1) {
         throw new IllegalArgumentException("Invalid note value: 1/" + noteValue);
      } else {
         this.beatsPerMeasure = beatsPerMeasure;
         this.noteValue = noteValue;
         this.notesPerMeasure = noteValue * beatsPerMeasure;
      }
   }

   public int beatsPerMeasure() {
      return this.beatsPerMeasure;
   }

   public int notesPerMeasure() {
      return this.notesPerMeasure;
   }

   public int noteValue() {
      return this.noteValue;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof MidiTimeSignature)) {
         return false;
      } else {
         MidiTimeSignature other = (MidiTimeSignature)o;
         return this.beatsPerMeasure == other.beatsPerMeasure && this.noteValue == other.noteValue;
      }
   }

   public String toString() {
      return this.beatsPerMeasure + "/" + this.noteValue;
   }

   public static MidiTimeSignature fromString(String signatureText, MidiTimeSignature defaultSig) {
      int sep;
      if (signatureText != null && (sep = signatureText.indexOf(47)) != -1) {
         String beatsPerMeasureStr = signatureText.substring(0, sep).trim();
         String noteValueStr = signatureText.substring(sep + 1).trim();
         return of(ParseUtil.parseInt(beatsPerMeasureStr, defaultSig.beatsPerMeasure), ParseUtil.parseInt(noteValueStr, defaultSig.noteValue));
      } else {
         return defaultSig;
      }
   }
}
