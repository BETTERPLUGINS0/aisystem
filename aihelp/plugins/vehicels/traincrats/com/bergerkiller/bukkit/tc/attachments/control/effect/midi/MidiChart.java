package com.bergerkiller.bukkit.tc.attachments.control.effect.midi;

import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import com.bergerkiller.bukkit.tc.attachments.api.Attachment;
import com.bergerkiller.bukkit.tc.attachments.control.effect.EffectLoop;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.function.Consumer;
import java.util.function.Function;

public final class MidiChart implements Cloneable {
   private final MidiChartParameters chartParams;
   private final List<MidiNote> notes = new ArrayList();
   private int lastIndex = 0;

   public MidiChart(MidiChartParameters chartParams) {
      this.chartParams = chartParams;
   }

   public MidiChartParameters getParameters() {
      return this.chartParams;
   }

   public MidiChart withChartParameters(MidiChartParameters chartParams) {
      MidiChart updated = new MidiChart(chartParams);
      Iterator var3 = this.notes.iterator();

      while(var3.hasNext()) {
         MidiNote note = (MidiNote)var3.next();
         updated.addNote(note);
      }

      return updated;
   }

   public MidiChart withChartParameters(Function<MidiChartParameters, MidiChartParameters> chartParamsChanger) {
      return this.withChartParameters((MidiChartParameters)chartParamsChanger.apply(this.getParameters()));
   }

   public boolean isEmpty() {
      return this.notes.isEmpty();
   }

   public List<MidiNote> getNotes() {
      return this.notes;
   }

   public MidiChart.Bounds getBounds() {
      if (this.isEmpty()) {
         return MidiChart.Bounds.EMPTY;
      } else {
         int minTimeStepIndex = ((MidiNote)this.notes.get(0)).timeStepIndex();
         int maxTimeStepIndex = ((MidiNote)this.notes.get(this.notes.size() - 1)).timeStepIndex();
         int minPitch = Integer.MAX_VALUE;
         int maxPitch = Integer.MIN_VALUE;

         MidiNote note;
         for(Iterator var5 = this.notes.iterator(); var5.hasNext(); maxPitch = Math.max(maxPitch, note.pitchClass())) {
            note = (MidiNote)var5.next();
            minPitch = Math.min(minPitch, note.pitchClass());
         }

         return new MidiChart.Bounds(minTimeStepIndex, maxTimeStepIndex, minPitch, maxPitch);
      }
   }

   public void clearNotes() {
      this.notes.clear();
   }

   public void timeShift(int numTimeSteps) {
      if (numTimeSteps != 0) {
         ListIterator it = this.notes.listIterator();

         while(it.hasNext()) {
            it.set(((MidiNote)it.next()).withTimeShift(numTimeSteps));
         }
      }

   }

   public EffectLoop.Time timeShiftToStart() {
      if (!this.isEmpty()) {
         MidiNote firstNote = (MidiNote)this.getNotes().get(0);
         EffectLoop.Time shifted = EffectLoop.Time.nanos(firstNote.timeStepTimestampNanos);
         this.timeShift(-firstNote.timeStepIndex());
         return shifted;
      } else {
         return EffectLoop.Time.ZERO;
      }
   }

   public void pitchShift(int numPitchClasses) {
      if (numPitchClasses != 0) {
         ListIterator it = this.notes.listIterator();

         while(it.hasNext()) {
            it.set(((MidiNote)it.next()).withPitchShift(numPitchClasses));
         }
      }

   }

   public boolean forNotesInRange(long prevNanos, long currNanos, Consumer<MidiNote> action) {
      int currIndex = this.lastIndex;
      List<MidiNote> notes = this.notes;
      int notesCount = notes.size();
      if (currIndex >= notesCount || ((MidiNote)notes.get(currIndex)).timeStepTimestampNanos > prevNanos) {
         currIndex = 0;
      }

      MidiNote n;
      for(; currIndex < notesCount && (n = (MidiNote)notes.get(currIndex)).timeStepTimestampNanos < currNanos; ++currIndex) {
         if (n.timeStepTimestampNanos >= prevNanos) {
            action.accept(n);
         }
      }

      this.lastIndex = currIndex;
      return currIndex < notesCount;
   }

   public List<MidiNote> getChartVisibleNotes(int startTimeStepIndex, int numTimeSteps) {
      List<MidiNote> result = new ArrayList();
      Iterator var4 = this.notes.iterator();

      while(var4.hasNext()) {
         MidiNote note = (MidiNote)var4.next();
         int offset = note.timeStepIndex() - startTimeStepIndex;
         if (offset >= 0 && offset < numTimeSteps) {
            result.add(note);
         }
      }

      return result;
   }

   public MidiNote update(MidiNote note, Function<MidiNote, MidiNote> operation) {
      MidiNote updated = (MidiNote)operation.apply(note);
      this.removeNote(note);
      this.addNoteDirect(updated);
      return updated;
   }

   public boolean containsNote(MidiNote note) {
      return Collections.binarySearch(this.notes, note) >= 0;
   }

   public boolean containsAllNotes(Collection<MidiNote> notes) {
      Iterator var2 = notes.iterator();

      MidiNote note;
      do {
         if (!var2.hasNext()) {
            return true;
         }

         note = (MidiNote)var2.next();
      } while(this.containsNote(note));

      return false;
   }

   public void removeNote(MidiNote note) {
      int index = Collections.binarySearch(this.notes, note);
      if (index >= 0) {
         this.notes.remove(index);
      }

   }

   public MidiNote addNoteOnBar(int timeStepIndex, int pitchClass, double volume) {
      MidiNote note = new MidiNote(this.chartParams, this.chartParams.timeStep().multiply(timeStepIndex), Attachment.EffectAttachment.EffectOptions.of(volume, this.chartParams.getPitch(pitchClass)));
      this.addNoteDirect(note);
      return note;
   }

   public MidiNote addNote(double timestamp, double volume, double speed) {
      return this.addNote(timestamp, Attachment.EffectAttachment.EffectOptions.of(volume, speed));
   }

   public MidiNote addNote(double timestamp, Attachment.EffectAttachment.EffectOptions options) {
      MidiNote note = new MidiNote(this.chartParams, timestamp, options);
      this.addNoteDirect(note);
      return note;
   }

   public void addChartNotes(MidiChart chart) {
      if (chart.chartParams.equals(this.chartParams)) {
         chart.notes.forEach(this::addNoteDirect);
      } else {
         chart.notes.forEach(this::addNote);
      }

   }

   public void removeChartNotes(MidiChart chart) {
      if (chart.chartParams.equals(this.chartParams)) {
         chart.notes.forEach(this::removeNote);
      } else {
         chart.notes.forEach((n) -> {
            this.removeNote(n.withChartParameters(this.chartParams));
         });
      }

   }

   public boolean toggleChartNotes(MidiChart chart) {
      if (this.containsAllNotes(chart.getNotes())) {
         this.removeChartNotes(chart);
         return false;
      } else {
         this.addChartNotes(chart);
         return true;
      }
   }

   public MidiNote addNote(MidiNote note) {
      note = note.withChartParameters(this.chartParams);
      this.addNoteDirect(note);
      return note;
   }

   private void addNoteDirect(MidiNote note) {
      int index = Collections.binarySearch(this.notes, note);
      if (index >= 0) {
         this.notes.set(index, note);
      } else {
         this.notes.add(-index - 1, note);
      }

   }

   public MidiChart clone() {
      MidiChart copy = new MidiChart(this.chartParams);
      copy.notes.addAll(this.notes);
      return copy;
   }

   public ConfigurationNode toYaml() {
      ConfigurationNode yaml = new ConfigurationNode();
      this.getParameters().toYaml(yaml);
      if (!this.isEmpty()) {
         List<String> notesStr = yaml.getList("notes", String.class);
         Iterator var3 = this.notes.iterator();

         while(var3.hasNext()) {
            MidiNote note = (MidiNote)var3.next();
            notesStr.add(note.toString());
         }
      }

      return yaml;
   }

   public static MidiChart fromYaml(ConfigurationNode config) {
      MidiChart chart = empty(MidiChartParameters.fromYaml(config));
      if (config.contains("notes")) {
         Iterator var2 = config.getList("notes", String.class).iterator();

         while(var2.hasNext()) {
            String noteStr = (String)var2.next();
            MidiNote note = MidiNote.fromString(chart.getParameters(), noteStr);
            if (note != null) {
               chart.addNoteDirect(note);
            }
         }
      }

      return chart;
   }

   public static MidiChart empty() {
      return empty(MidiChartParameters.DEFAULT);
   }

   public static MidiChart empty(MidiChartParameters chartParams) {
      return new MidiChart(chartParams);
   }

   public static MidiChart bergersTune() {
      MidiChart chart = new MidiChart(MidiChartParameters.chromatic(MidiTimeSignature.COMMON, 150));
      chart.addNote(0.0D, 1.0D, 0.6D);
      chart.addNote(0.1D, 1.0D, 0.8D);
      chart.addNote(0.2D, 1.0D, 1.0D);
      chart.addNote(0.3D, 1.0D, 1.2D);
      chart.addNote(0.4D, 1.0D, 1.4D);
      chart.addNote(0.5D, 1.0D, 1.6D);
      chart.addNote(0.6D, 1.0D, 1.8D);
      chart.addNote(0.7D, 1.0D, 2.0D);
      chart.addNote(1.0D, 1.0D, 1.0D);
      chart.addNote(1.2D, 1.0D, 1.2D);
      chart.addNote(1.4D, 1.0D, 1.4D);
      chart.addNote(1.6D, 1.0D, 1.4D);
      chart.addNote(1.7D, 1.0D, 1.2D);
      chart.addNote(1.8D, 1.0D, 1.0D);
      chart.addNote(1.9D, 1.0D, 0.8D);
      chart.addNote(2.0D, 1.0D, 0.9D);
      chart.addNote(2.2D, 1.0D, 1.1D);
      chart.addNote(2.4D, 1.0D, 0.5D);
      chart.addNote(2.6D, 1.0D, 0.7D);
      return chart;
   }

   public static class Bounds {
      public static final MidiChart.Bounds EMPTY = new MidiChart.Bounds(0, 0, 0, 0);
      private final int minTimeStepIndex;
      private final int maxTimeStepIndex;
      private final int minPitchClass;
      private final int maxPitchClass;

      public Bounds(int minTimeStepIndex, int maxTimeStepIndex, int minPitchClass, int maxPitchClass) {
         this.minTimeStepIndex = minTimeStepIndex;
         this.maxTimeStepIndex = maxTimeStepIndex;
         this.minPitchClass = minPitchClass;
         this.maxPitchClass = maxPitchClass;
      }

      public int minTimeStepIndex() {
         return this.minTimeStepIndex;
      }

      public int maxTimeStepIndex() {
         return this.maxTimeStepIndex;
      }

      public int getNumTimeSteps() {
         return this.maxTimeStepIndex - this.minTimeStepIndex + 1;
      }

      public int minPitchClass() {
         return this.minPitchClass;
      }

      public int maxPitchClass() {
         return this.maxPitchClass;
      }

      public int getNumPitchClasses() {
         return this.maxPitchClass - this.minPitchClass + 1;
      }

      public boolean isEmpty() {
         return this == EMPTY;
      }
   }
}
