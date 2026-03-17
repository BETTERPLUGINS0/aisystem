package com.bergerkiller.bukkit.tc.attachments.control.effect;

import com.bergerkiller.bukkit.common.events.map.MapKeyEvent;
import com.bergerkiller.bukkit.common.map.MapCanvas;
import com.bergerkiller.bukkit.common.map.MapColorPalette;
import com.bergerkiller.bukkit.common.map.MapFont;
import com.bergerkiller.bukkit.common.map.MapTexture;
import com.bergerkiller.bukkit.common.map.MapPlayerInput.Key;
import com.bergerkiller.bukkit.common.map.widgets.MapWidget;
import com.bergerkiller.bukkit.common.map.widgets.MapWidgetButton;
import com.bergerkiller.bukkit.common.map.widgets.MapWidgetText;
import com.bergerkiller.bukkit.tc.TrainCarts;
import com.bergerkiller.bukkit.tc.attachments.api.Attachment;
import com.bergerkiller.bukkit.tc.attachments.control.effect.midi.MidiChart;
import com.bergerkiller.bukkit.tc.attachments.control.effect.midi.MidiChartParameters;
import com.bergerkiller.bukkit.tc.attachments.control.effect.midi.MidiNote;
import com.bergerkiller.bukkit.tc.attachments.control.effect.midi.MidiTimeSignature;
import com.bergerkiller.bukkit.tc.attachments.ui.MapWidgetMenu;
import com.bergerkiller.bukkit.tc.attachments.ui.MapWidgetNumberBox;
import com.bergerkiller.bukkit.tc.attachments.ui.MapWidgetTooltip;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

public abstract class MidiChartDialog extends MapWidgetMenu {
   private static final MapTexture MIDI_BUTTON_ICONS;
   private MidiChart chart = MidiChart.empty();
   private MidiChart selection;
   private MidiChart pattern;
   private EffectLoop.RunMode midiRunMode;
   private MidiChartDialog.Mode mode;
   private MidiChartDialog.TopMenuButton btnModeNote;
   private MidiChartDialog.TopMenuButton btnModeSelect;
   private MidiChartDialog.TopMenuButton btnModePattern;
   private MidiChartDialog.TopMenuButton prevSelectedButton;
   private MidiChartDialog.MidiPianoRollWidget pianoRoll;
   private EffectLoop.Time duration;
   private volatile int previewCtr;
   private volatile EffectLoop.Time currentPreviewTime;

   public MidiChartDialog() {
      this.selection = MidiChart.empty(this.chart.getParameters());
      this.pattern = MidiChart.empty(this.chart.getParameters());
      this.midiRunMode = EffectLoop.RunMode.ASYNCHRONOUS;
      this.mode = MidiChartDialog.Mode.NOTE;
      this.prevSelectedButton = null;
      this.duration = null;
      this.previewCtr = 0;
      this.currentPreviewTime = null;
      this.setPositionAbsolute(true);
      this.setBounds(5, 5, 118, 116);
      this.setBackgroundColor(MapColorPalette.getColor(16, 16, 128));
   }

   public abstract void onChartChanged(MidiChart var1);

   public abstract Attachment.EffectSink getEffectSink();

   public MidiChartDialog setChart(MidiChart chart) {
      this.chart = chart;
      this.selection = this.selection.withChartParameters(chart.getParameters());
      this.pattern = this.pattern.withChartParameters(chart.getParameters());
      this.stopPreview();
      if (this.pianoRoll != null) {
         this.pianoRoll.invalidate();
      }

      return this;
   }

   public MidiChartDialog setDuration(EffectLoop.Time duration) {
      this.duration = duration;
      return this;
   }

   public MidiChartDialog setMidiRunMode(EffectLoop.RunMode runMode) {
      this.midiRunMode = runMode;
      return this;
   }

   public void setMode(MidiChartDialog.Mode mode) {
      if (this.mode != mode) {
         this.mode = mode;
         this.applyMode();
      }

   }

   private void applyMode() {
      if (this.getDisplay() != null) {
         this.btnModeNote.setSelected(this.mode == MidiChartDialog.Mode.NOTE);
         this.btnModeSelect.setSelected(this.mode == MidiChartDialog.Mode.SELECT);
         this.btnModePattern.setSelected(this.mode == MidiChartDialog.Mode.PATTERN);
         this.pianoRoll.invalidate();
         this.mode.select(this);
      }

   }

   private void setNoteSelect() {
      if (this.selection.isEmpty()) {
         this.selection.addNoteOnBar(0, 0, 1.0D);
      } else {
         while(this.selection.getNotes().size() > 1) {
            this.selection.removeNote((MidiNote)this.selection.getNotes().get(this.selection.getNotes().size() - 1));
         }
      }

      this.pianoRoll.scrollToSelection();
   }

   private void setPatternSelect() {
      if (this.pattern.isEmpty()) {
         this.setNoteSelect();
      } else {
         this.selection.clearNotes();
         this.selection.addChartNotes(this.pattern);
         this.pianoRoll.scrollToSelection();
      }

   }

   private void exitPianoRoll() {
      if (this.prevSelectedButton == null) {
         this.prevSelectedButton = this.btnModeNote;
      }

      this.prevSelectedButton.focus();
   }

   private void stopPreview() {
      ++this.previewCtr;
   }

   private void preview(MidiChart chart, boolean shiftToStart, boolean ignoreDuration) {
      this.stopPreview();
      if (!chart.isEmpty()) {
         chart = chart.clone();
         EffectLoop.Time shifted = shiftToStart ? chart.timeShiftToStart() : EffectLoop.Time.ZERO;
         MidiScheduledEffectLoop midiEffectLoop = new MidiScheduledEffectLoop();
         int previewId = this.previewCtr;
         midiEffectLoop.setChart(chart);
         midiEffectLoop.setEffectSink(this.getEffectSink());
         ScheduledEffectLoop.SequentialEffectLoop effectLoop = midiEffectLoop.asEffectLoop();
         TrainCarts.plugin.createEffectLoopPlayer().play(effectLoop.withAdvance((base, dt, duration, loop) -> {
            if (this.duration != null && !ignoreDuration) {
               duration = this.duration;
            }

            if (previewId == this.previewCtr && base.advance(dt, duration, loop)) {
               this.currentPreviewTime = EffectLoop.Time.nanos(shifted.nanos + effectLoop.nanosElapsed());
               return true;
            } else {
               this.currentPreviewTime = null;
               return false;
            }
         }), this.midiRunMode);
      }
   }

   public void onAttached() {
      this.btnModeNote = ((<undefinedtype>)this.addWidget(new MidiChartDialog.TopMenuButton(MIDI_BUTTON_ICONS.getView(0, 0, 12, 12).clone()) {
         public void onClick() {
            MidiChartDialog.this.setMode(MidiChartDialog.Mode.NOTE);
            MidiChartDialog.this.pianoRoll.activate();
         }
      })).setTooltip("Place notes");
      this.btnModeNote.setPosition(5, 5);
      this.btnModeSelect = ((<undefinedtype>)this.addWidget(new MidiChartDialog.TopMenuButton(MIDI_BUTTON_ICONS.getView(12, 0, 12, 12).clone()) {
         public void onClick() {
            MidiChartDialog.this.setMode(MidiChartDialog.Mode.SELECT);
            MidiChartDialog.this.pianoRoll.activate();
         }
      })).setTooltip("Select note pattern");
      this.btnModeSelect.setPosition(18, 5);
      this.btnModePattern = ((<undefinedtype>)this.addWidget(new MidiChartDialog.TopMenuButton(MIDI_BUTTON_ICONS.getView(24, 0, 12, 12).clone()) {
         public void onClick() {
            MidiChartDialog.this.setMode(MidiChartDialog.Mode.PATTERN);
            MidiChartDialog.this.pianoRoll.activate();
         }
      })).setTooltip("Place note pattern");
      this.btnModePattern.setPosition(31, 5);
      ((<undefinedtype>)this.addWidget(new MidiChartDialog.TopMenuButton(MIDI_BUTTON_ICONS.getView(36, 0, 12, 12).clone()) {
         public void onClick() {
            MidiChartDialog.this.addWidget(new MidiChartDialog.ConfirmClearDialog() {
               public void onConfirmClear() {
                  MidiChartDialog.this.chart.clearNotes();
                  MidiChartDialog.this.selection.clearNotes();
                  MidiChartDialog.this.pattern.clearNotes();
                  MidiChartDialog.this.pianoRoll.invalidate();
                  MidiChartDialog.this.mode.select(MidiChartDialog.this);
                  MidiChartDialog.this.stopPreview();
                  MidiChartDialog.this.onChartChanged(MidiChartDialog.this.chart);
               }
            });
         }
      })).setTooltip("Clear chart").setPosition(46, 5);
      ((<undefinedtype>)this.addWidget(new MidiChartDialog.TopMenuButton(MIDI_BUTTON_ICONS.getView(48, 0, 12, 12).clone()) {
         private boolean playing = false;

         private void setPlaying(boolean newPlaying) {
            if (this.playing != newPlaying) {
               this.playing = newPlaying;
               if (newPlaying) {
                  this.setIcon(MidiChartDialog.MIDI_BUTTON_ICONS.getView(60, 0, 12, 12).clone());
                  this.setTooltip("Stop playing chart");
               } else {
                  this.setIcon(MidiChartDialog.MIDI_BUTTON_ICONS.getView(48, 0, 12, 12).clone());
                  this.setTooltip("Play chart");
               }
            }

         }

         public void onClick() {
            if (this.playing) {
               MidiChartDialog.this.stopPreview();
               this.setPlaying(false);
            } else {
               MidiChartDialog.this.preview(MidiChartDialog.this.chart, false, false);
               this.setPlaying(true);
            }

         }

         public void onTick() {
            super.onTick();
            this.setPlaying(MidiChartDialog.this.currentPreviewTime != null);
         }
      })).setTooltip("Play chart").setPosition(this.getWidth() - 30, 5);
      ((<undefinedtype>)this.addWidget(new MidiChartDialog.TopMenuButton(MIDI_BUTTON_ICONS.getView(72, 0, 12, 12).clone()) {
         public void onClick() {
            ((<undefinedtype>)MidiChartDialog.this.addWidget(new MidiChartDialog.ChartSettingsDialog() {
               public void onParamsChanged(MidiChartParameters params) {
                  MidiChartDialog.this.setChart(MidiChartDialog.this.chart.withChartParameters(params));
                  MidiChartDialog.this.onChartChanged(MidiChartDialog.this.chart);
               }
            })).setParams(MidiChartDialog.this.chart.getParameters());
         }
      })).setTooltip("Chart settings").setPosition(this.getWidth() - 17, 5);
      this.pianoRoll = (MidiChartDialog.MidiPianoRollWidget)this.addWidget(new MidiChartDialog.MidiPianoRollWidget());
      this.pianoRoll.setBounds(5, 19, this.getWidth() - 10, this.getHeight() - 24);
      this.applyMode();
      super.onAttached();
   }

   public void onDetached() {
      this.stopPreview();
      super.onDetached();
   }

   static {
      MIDI_BUTTON_ICONS = MapTexture.loadPluginResource(TrainCarts.plugin, "com/bergerkiller/bukkit/tc/textures/attachments/midi_buttons.png");
   }

   private abstract class TopMenuButton extends MapWidget {
      private MapTexture icon;
      private boolean selected = false;
      private boolean buttonDown = false;
      private final MapWidgetTooltip tooltip = new MapWidgetTooltip();

      public TopMenuButton(MapTexture icon) {
         this.icon = icon;
         this.setSize(icon.getWidth(), icon.getHeight());
         this.setFocusable(true);
      }

      public abstract void onClick();

      public MidiChartDialog.TopMenuButton setTooltip(String text) {
         this.tooltip.setText(text);
         return this;
      }

      public MidiChartDialog.TopMenuButton setSelected(boolean selected) {
         if (this.selected != selected) {
            this.selected = selected;
            this.invalidate();
         }

         return this;
      }

      public MidiChartDialog.TopMenuButton setIcon(MapTexture icon) {
         this.icon = icon;
         this.invalidate();
         return this;
      }

      public void onFocus() {
         this.addWidget(this.tooltip);
         MidiChartDialog.this.prevSelectedButton = this;
      }

      public void onBlur() {
         this.removeWidget(this.tooltip);
         this.buttonDown = false;
      }

      public void onDraw() {
         byte edgeColor;
         byte topRim;
         byte background;
         byte bottomRim;
         if (this.isFocused()) {
            edgeColor = 119;
            topRim = MapColorPalette.getColor(216, 76, 178);
            background = MapColorPalette.getColor(186, 65, 153);
            bottomRim = MapColorPalette.getColor(152, 53, 125);
         } else {
            edgeColor = 119;
            topRim = MapColorPalette.getColor(142, 109, 208);
            background = MapColorPalette.getColor(116, 89, 170);
            bottomRim = MapColorPalette.getColor(97, 63, 148);
         }

         if (this.selected || this.buttonDown) {
            byte b = topRim;
            topRim = bottomRim;
            bottomRim = b;
         }

         this.view.fillRectangle(2, 2, this.getWidth() - 4, this.getHeight() - 4, background);
         this.view.drawRectangle(0, 0, this.getWidth(), this.getHeight(), edgeColor);
         this.view.drawLine(1, 1, this.getWidth() - 2, 1, topRim);
         this.view.drawLine(1, 2, 1, this.getHeight() - 3, topRim);
         this.view.drawLine(this.getWidth() - 2, 2, this.getWidth() - 2, this.getHeight() - 3, bottomRim);
         this.view.drawLine(1, this.getHeight() - 2, this.getWidth() - 2, this.getHeight() - 2, bottomRim);
         this.view.draw(this.icon, 0, 0);
      }

      public void onKeyPressed(MapKeyEvent event) {
         if (event.getKey() == Key.ENTER) {
            if (!this.buttonDown) {
               this.buttonDown = true;
               this.invalidate();
               this.onClick();
            }
         } else {
            super.onKeyPressed(event);
         }

      }

      public void onKeyReleased(MapKeyEvent event) {
         if (event.getKey() == Key.ENTER) {
            if (this.buttonDown) {
               this.buttonDown = false;
               this.invalidate();
            }
         } else {
            super.onKeyReleased(event);
         }

      }
   }

   public static enum Mode {
      NOTE((dialog) -> {
         dialog.setNoteSelect();
         dialog.pattern.clearNotes();
      }, (dialog) -> {
         if (dialog.chart.toggleChartNotes(dialog.selection)) {
            dialog.preview(dialog.selection, true, true);
         }

      }),
      SELECT((rec$) -> {
         ((MidiChartDialog)rec$).setNoteSelect();
      }, (dialog) -> {
         if (dialog.pattern.toggleChartNotes(dialog.selection)) {
            dialog.preview(dialog.pattern, true, true);
         } else {
            dialog.stopPreview();
         }

      }),
      PATTERN((rec$) -> {
         ((MidiChartDialog)rec$).setPatternSelect();
      }, (dialog) -> {
         if (dialog.chart.toggleChartNotes(dialog.selection)) {
            dialog.preview(dialog.selection, true, true);
         } else {
            dialog.stopPreview();
         }

      });

      private final Consumer<MidiChartDialog> selectAction;
      private final Consumer<MidiChartDialog> activateAction;

      private Mode(Consumer<MidiChartDialog> selectAction, Consumer<MidiChartDialog> activateAction) {
         this.selectAction = selectAction;
         this.activateAction = activateAction;
      }

      public void select(MidiChartDialog dialog) {
         this.selectAction.accept(dialog);
      }

      public void activate(MidiChartDialog dialog) {
         this.activateAction.accept(dialog);
      }

      // $FF: synthetic method
      private static MidiChartDialog.Mode[] $values() {
         return new MidiChartDialog.Mode[]{NOTE, SELECT, PATTERN};
      }
   }

   private class MidiPianoRollWidget extends MapWidget {
      private int startPitchClass = 0;
      private int startTimeStepIndex = 0;
      private int playVerticalLineX = -1;

      public MidiPianoRollWidget() {
         this.setFocusable(true);
      }

      public void setStartTimeStepIndex(int startTimeStepIndex) {
         if (this.startTimeStepIndex != startTimeStepIndex) {
            this.startTimeStepIndex = startTimeStepIndex;
            this.invalidate();
         }

      }

      public void setStartPitchClass(int startPitchClass) {
         if (this.startPitchClass != startPitchClass) {
            this.startPitchClass = startPitchClass;
            this.invalidate();
         }

      }

      public int getNumTimeSteps() {
         return (this.getWidth() - 6) / 3;
      }

      public int getNumPitchClassesFromMiddle() {
         return this.getHeight() / 4;
      }

      public void scrollToSelection() {
         MidiChart.Bounds bounds = MidiChartDialog.this.selection.getBounds();
         if (!bounds.isEmpty()) {
            int safeSpacing = Math.min(2, (this.getNumTimeSteps() - bounds.getNumTimeSteps()) / 2);
            int spacing;
            if (safeSpacing <= 0) {
               this.setStartTimeStepIndex(bounds.minTimeStepIndex());
            } else if ((spacing = this.startTimeStepIndex - bounds.minTimeStepIndex() + safeSpacing) > 0) {
               this.setStartTimeStepIndex(Math.max(0, this.startTimeStepIndex - spacing));
            } else if ((spacing = bounds.maxTimeStepIndex() - (this.startTimeStepIndex + this.getNumTimeSteps() - 1) + safeSpacing) > 0) {
               this.setStartTimeStepIndex(this.startTimeStepIndex + spacing);
            }

            int safeSpacingx = 2;
            if (safeSpacingx <= 0) {
               this.setStartPitchClass((bounds.minPitchClass() + bounds.maxPitchClass()) / 2);
            } else if ((spacing = this.startPitchClass - bounds.minPitchClass() - this.getNumPitchClassesFromMiddle() + 1 + safeSpacingx) > 0) {
               this.setStartPitchClass(this.startPitchClass - spacing);
            } else if ((spacing = bounds.maxPitchClass() - this.startPitchClass - this.getNumPitchClassesFromMiddle() + safeSpacingx) > 0) {
               this.setStartPitchClass(this.startPitchClass + spacing);
            }
         }

      }

      public void timeShiftSelection(int numTimeSteps) {
         if (numTimeSteps >= 0 || MidiChartDialog.this.selection.getBounds().minTimeStepIndex() > 0) {
            MidiChartDialog.this.selection.timeShift(numTimeSteps);
            if (MidiChartDialog.this.mode == MidiChartDialog.Mode.PATTERN) {
               MidiChartDialog.this.pattern.timeShift(numTimeSteps);
            }

            this.scrollToSelection();
            this.invalidate();
         }
      }

      public void pitchShiftSelection(int numPitchClasses) {
         MidiChartDialog.this.selection.pitchShift(numPitchClasses);
         if (MidiChartDialog.this.mode == MidiChartDialog.Mode.PATTERN) {
            MidiChartDialog.this.pattern.pitchShift(numPitchClasses);
         }

         this.scrollToSelection();
         this.invalidate();
      }

      public void onTick() {
         int newVerticalLineX = this.calcChartXFromTime(MidiChartDialog.this.currentPreviewTime);
         if (newVerticalLineX != this.playVerticalLineX) {
            this.playVerticalLineX = newVerticalLineX;
            this.invalidate();
         }

      }

      private int calcChartXFromTime(EffectLoop.Time time) {
         if (time == null) {
            return -1;
         } else {
            long elapsed = time.nanos - MidiChartDialog.this.chart.getParameters().getTimestampNanos(this.startTimeStepIndex);
            if (elapsed < 0L) {
               return -1;
            } else {
               int xPos = 7 + (int)(3L * elapsed / MidiChartDialog.this.chart.getParameters().timeStep().nanos);
               return xPos >= this.getWidth() ? -1 : xPos;
            }
         }
      }

      public void onDraw() {
         int numPitchValues = this.getHeight() / 4 + 1;
         int baseY = this.getHeight() / 2;
         boolean active = this.isActivated();
         Set<Integer> selectedPitchClasses = new HashSet();
         if (active) {
            Iterator var5 = MidiChartDialog.this.selection.getNotes().iterator();

            while(var5.hasNext()) {
               MidiNote selectedNote = (MidiNote)var5.next();
               selectedPitchClasses.add(selectedNote.pitchClass());
            }
         }

         MidiTimeSignature signature = MidiChartDialog.this.chart.getParameters().timeSignature();

         for(int i = -numPitchValues; i <= numPitchValues; ++i) {
            int pitch = i + this.startPitchClass;
            MidiChartDialog.PianoRendering.PianoKey key;
            if (pitch == 0) {
               key = MidiChartDialog.PianoRendering.BLACK_KEY_BASE;
            } else {
               key = MidiChartDialog.PianoRendering.PIANO_KEYS[Math.floorMod(pitch, 12)];
            }

            key.draw(this.view, baseY - i * 2, this.getWidth(), selectedPitchClasses.contains(pitch), (timeStepIndex) -> {
               int adjTimeStepIndex = timeStepIndex + this.startTimeStepIndex;
               if (adjTimeStepIndex % signature.notesPerMeasure() == 0) {
                  return MidiChartDialog.TimeSeparator.MEASURE;
               } else {
                  return adjTimeStepIndex % signature.noteValue() == 0 ? MidiChartDialog.TimeSeparator.BEAT : MidiChartDialog.TimeSeparator.NOTE;
               }
            });
         }

         if (active) {
            this.drawAllNotes(MidiChartDialog.this.selection, (note) -> {
               if (MidiChartDialog.this.chart.containsNote(note)) {
                  return MidiChartDialog.PianoRendering.NOTE_NONE;
               } else {
                  return MidiChartDialog.this.mode == MidiChartDialog.Mode.SELECT && MidiChartDialog.this.pattern.containsNote(note) ? MidiChartDialog.PianoRendering.NOTE_PATTERN_SELECTED : MidiChartDialog.PianoRendering.NOTE_INACTIVE;
               }
            });
         }

         if (MidiChartDialog.this.mode == MidiChartDialog.Mode.SELECT || MidiChartDialog.this.mode == MidiChartDialog.Mode.PATTERN) {
            this.drawAllNotes(MidiChartDialog.this.pattern, (note) -> {
               if (MidiChartDialog.this.chart.containsNote(note)) {
                  return MidiChartDialog.PianoRendering.NOTE_NONE;
               } else {
                  return active && MidiChartDialog.this.selection.containsNote(note) ? MidiChartDialog.PianoRendering.NOTE_NONE : MidiChartDialog.PianoRendering.NOTE_PATTERN_DEFAULT;
               }
            });
         }

         this.drawAllNotes(MidiChartDialog.this.chart, (note) -> {
            if (active) {
               if (MidiChartDialog.this.mode == MidiChartDialog.Mode.SELECT && MidiChartDialog.this.pattern.containsNote(note)) {
                  if (MidiChartDialog.this.selection.containsNote(note)) {
                     return MidiChartDialog.PianoRendering.NOTE_PATTERN_SELECTED;
                  }

                  return MidiChartDialog.PianoRendering.NOTE_PATTERN_OVERLAP;
               }

               if (MidiChartDialog.this.selection.containsNote(note)) {
                  return MidiChartDialog.PianoRendering.NOTE_SELECTED;
               }
            }

            return MidiChartDialog.PianoRendering.NOTE_DEFAULT;
         });
         if (this.playVerticalLineX >= 0) {
            this.view.drawLine(this.playVerticalLineX, 0, this.playVerticalLineX, this.getHeight() - 1, (byte)18);
         }

         if (MidiChartDialog.this.duration != null) {
            int durationVerticalLineX = this.calcChartXFromTime(MidiChartDialog.this.duration);
            if (durationVerticalLineX >= 0) {
               this.view.drawLine(durationVerticalLineX, 0, durationVerticalLineX, this.getHeight() - 1, (byte)34);
            }
         }

      }

      private void drawAllNotes(MidiChart chart, Function<MidiNote, MidiChartDialog.NoteColors> colorsFunc) {
         int numTimeSteps = this.getNumTimeSteps();
         Iterator var4 = chart.getChartVisibleNotes(this.startTimeStepIndex, numTimeSteps).iterator();

         while(var4.hasNext()) {
            MidiNote note = (MidiNote)var4.next();
            this.drawNote(note, colorsFunc);
         }

      }

      private void drawNote(MidiNote note, Function<MidiNote, MidiChartDialog.NoteColors> colorsFunc) {
         int baseY = this.getHeight() / 2;
         int noteX = 7 + (note.timeStepIndex() - this.startTimeStepIndex) * 3;
         int noteY = baseY - (note.pitchClass() - this.startPitchClass) * 2;
         if (noteY >= -1 && noteY < this.getHeight()) {
            MidiChartDialog.NoteColors colors = (MidiChartDialog.NoteColors)colorsFunc.apply(note);
            if (colors != MidiChartDialog.PianoRendering.NOTE_NONE) {
               colors.draw(this.view, noteX, noteY);
            }
         }

      }

      public void onFocus() {
         this.activate();
      }

      public void onKeyPressed(MapKeyEvent event) {
         if (event.getKey() == Key.LEFT) {
            this.timeShiftSelection(-1);
         } else if (event.getKey() == Key.RIGHT) {
            this.timeShiftSelection(1);
         } else if (event.getKey() == Key.UP) {
            this.pitchShiftSelection(1);
         } else if (event.getKey() == Key.DOWN) {
            this.pitchShiftSelection(-1);
         } else if (event.getKey() == Key.ENTER) {
            MidiChartDialog.this.mode.activate(MidiChartDialog.this);
            this.invalidate();
            MidiChartDialog.this.onChartChanged(MidiChartDialog.this.chart);
         } else if (event.getKey() == Key.BACK) {
            MidiChartDialog.this.exitPianoRoll();
         }

      }
   }

   @FunctionalInterface
   private interface FindTimeSeparatorFunc {
      MidiChartDialog.TimeSeparator find(int var1);
   }

   private static enum TimeSeparator {
      BACKGROUND,
      NOTE,
      BEAT,
      MEASURE;

      // $FF: synthetic method
      private static MidiChartDialog.TimeSeparator[] $values() {
         return new MidiChartDialog.TimeSeparator[]{BACKGROUND, NOTE, BEAT, MEASURE};
      }
   }

   public static class NoteColors {
      public final byte TOP;
      public final byte BTM;

      private NoteColors(MidiChartDialog.NoteColors.Builder builder) {
         this.TOP = builder.TOP;
         this.BTM = builder.BTM;
      }

      public void draw(MapCanvas view, int x, int y) {
         view.writePixel(x, y, this.TOP);
         view.writePixel(x + 1, y, this.TOP);
         view.writePixel(x, y + 1, this.BTM);
         view.writePixel(x + 1, y + 1, this.BTM);
      }

      public static MidiChartDialog.NoteColors.Builder builder() {
         return new MidiChartDialog.NoteColors.Builder();
      }

      // $FF: synthetic method
      NoteColors(MidiChartDialog.NoteColors.Builder x0, Object x1) {
         this(x0);
      }

      public static class Builder {
         public byte TOP = 0;
         public byte BTM = 0;

         public MidiChartDialog.NoteColors.Builder top(int r, int g, int b) {
            this.TOP = MapColorPalette.getColor(r, g, b);
            return this;
         }

         public MidiChartDialog.NoteColors.Builder btm(int r, int g, int b) {
            this.BTM = MapColorPalette.getColor(r, g, b);
            return this;
         }

         public MidiChartDialog.NoteColors build() {
            return new MidiChartDialog.NoteColors(this);
         }
      }
   }

   private static final class PianoRendering {
      public static final MidiChartDialog.PianoRendering.PianoKeyColors COLORS_BLACK_KEY_IDLE = MidiChartDialog.PianoRendering.PianoKeyColors.builder().key_top(27, 40, 54).key_btm(13, 13, 13).grid_bg(27, 40, 54).grid_note(69, 75, 95).grid_beat(13, 13, 13).grid_measure(152, 108, 72).build();
      public static final MidiChartDialog.PianoRendering.PianoKeyColors COLORS_BLACK_KEY_PRESSED = MidiChartDialog.PianoRendering.PianoKeyColors.builder().key_top(25, 57, 112).key_btm(36, 82, 159).grid_bg(25, 57, 112).grid_note(78, 77, 160).grid_beat(15, 13, 48).grid_measure(119, 93, 96).build();
      public static final MidiChartDialog.PianoRendering.PianoKeyColors COLORS_BLACK_BASE_KEY_IDLE = MidiChartDialog.PianoRendering.PianoKeyColors.builder().key_top(64, 43, 53).key_btm(48, 32, 40).grid_bg(48, 32, 40).grid_note(135, 84, 84).grid_beat(25, 25, 25).grid_measure(125, 53, 36).build();
      public static final MidiChartDialog.PianoRendering.PianoKeyColors COLORS_BLACK_BASE_KEY_PRESSED = MidiChartDialog.PianoRendering.PianoKeyColors.builder().key_top(94, 40, 27).key_btm(135, 67, 39).grid_bg(94, 40, 27).grid_note(138, 108, 112).grid_beat(48, 32, 40).grid_measure(186, 132, 88).build();
      public static final MidiChartDialog.PianoRendering.PianoKeyColors COLORS_WHITE_KEY_IDLE = MidiChartDialog.PianoRendering.PianoKeyColors.builder().key_top(220, 220, 220).key_btm(255, 255, 255).grid_bg(38, 63, 75).grid_note(84, 92, 116).grid_beat(18, 21, 30).grid_measure(153, 127, 76).build();
      public static final MidiChartDialog.PianoRendering.PianoKeyColors COLORS_WHITE_KEY_PRESSED = MidiChartDialog.PianoRendering.PianoKeyColors.builder().key_top(25, 93, 131).key_btm(44, 109, 186).grid_bg(25, 93, 131).grid_note(44, 109, 186).grid_beat(32, 42, 100).grid_measure(150, 154, 64).build();
      public static final MidiChartDialog.PianoRendering.PianoKey BLACK_KEY;
      public static final MidiChartDialog.PianoRendering.PianoKey BLACK_KEY_BASE;
      public static final MidiChartDialog.PianoRendering.PianoKey WHITE_KEY;
      public static final MidiChartDialog.PianoRendering.PianoKey WHITE_KEY_HALF_TOP;
      public static final MidiChartDialog.PianoRendering.PianoKey WHITE_KEY_HALF_BTM;
      public static final MidiChartDialog.PianoRendering.PianoKey[] PIANO_KEYS;
      public static final MidiChartDialog.NoteColors NOTE_NONE;
      public static final MidiChartDialog.NoteColors NOTE_DEFAULT;
      public static final MidiChartDialog.NoteColors NOTE_SELECTED;
      public static final MidiChartDialog.NoteColors NOTE_INACTIVE;
      public static final MidiChartDialog.NoteColors NOTE_PATTERN_DEFAULT;
      public static final MidiChartDialog.NoteColors NOTE_PATTERN_SELECTED;
      public static final MidiChartDialog.NoteColors NOTE_PATTERN_OVERLAP;

      static {
         BLACK_KEY = new MidiChartDialog.PianoRendering.BlackPianoKey(COLORS_BLACK_KEY_IDLE, COLORS_BLACK_KEY_PRESSED);
         BLACK_KEY_BASE = new MidiChartDialog.PianoRendering.BlackPianoKey(COLORS_BLACK_BASE_KEY_IDLE, COLORS_BLACK_BASE_KEY_PRESSED);
         WHITE_KEY = new MidiChartDialog.PianoRendering.PianoKey(COLORS_WHITE_KEY_IDLE, COLORS_WHITE_KEY_PRESSED) {
            public void drawKey(MapCanvas view, int y, MidiChartDialog.PianoRendering.PianoKeyColors colors) {
               view.drawLine(4, y - 1, 5, y - 1, colors.KEY_TOP);
               view.fillRectangle(0, y, 6, 2, colors.KEY_BTM);
               view.drawLine(4, y + 2, 5, y + 2, colors.KEY_BTM);
            }
         };
         WHITE_KEY_HALF_TOP = new MidiChartDialog.PianoRendering.PianoKey(COLORS_WHITE_KEY_IDLE, COLORS_WHITE_KEY_PRESSED) {
            public void drawKey(MapCanvas view, int y, MidiChartDialog.PianoRendering.PianoKeyColors colors) {
               view.drawLine(4, y - 1, 5, y - 1, colors.KEY_TOP);
               view.fillRectangle(0, y, 6, 2, colors.KEY_BTM);
            }
         };
         WHITE_KEY_HALF_BTM = new MidiChartDialog.PianoRendering.PianoKey(COLORS_WHITE_KEY_IDLE, COLORS_WHITE_KEY_PRESSED) {
            public void drawKey(MapCanvas view, int y, MidiChartDialog.PianoRendering.PianoKeyColors colors) {
               view.drawLine(0, y, 5, y, colors.KEY_TOP);
               view.drawLine(0, y + 1, 5, y + 1, colors.KEY_BTM);
               view.drawLine(4, y + 2, 5, y + 2, colors.KEY_BTM);
            }
         };
         PIANO_KEYS = new MidiChartDialog.PianoRendering.PianoKey[]{BLACK_KEY, WHITE_KEY, BLACK_KEY, WHITE_KEY, BLACK_KEY, WHITE_KEY_HALF_BTM, WHITE_KEY_HALF_TOP, BLACK_KEY, WHITE_KEY, BLACK_KEY, WHITE_KEY_HALF_BTM, WHITE_KEY_HALF_TOP};
         NOTE_NONE = MidiChartDialog.NoteColors.builder().build();
         NOTE_DEFAULT = MidiChartDialog.NoteColors.builder().top(255, 64, 64).btm(220, 55, 55).build();
         NOTE_SELECTED = MidiChartDialog.NoteColors.builder().top(213, 219, 92).btm(183, 188, 79).build();
         NOTE_INACTIVE = MidiChartDialog.NoteColors.builder().top(211, 217, 220).btm(199, 199, 199).build();
         NOTE_PATTERN_DEFAULT = MidiChartDialog.NoteColors.builder().top(54, 168, 176).btm(36, 161, 161).build();
         NOTE_PATTERN_SELECTED = MidiChartDialog.NoteColors.builder().top(77, 238, 250).btm(66, 205, 215).build();
         NOTE_PATTERN_OVERLAP = MidiChartDialog.NoteColors.builder().top(25, 204, 127).btm(56, 178, 127).build();
      }

      private static class PianoKeyColors {
         public final byte KEY_TOP;
         public final byte KEY_BTM;
         public final byte[] GRID_COLORS;

         private PianoKeyColors(MidiChartDialog.PianoRendering.PianoKeyColors.Builder builder) {
            this.KEY_TOP = builder.KEY_TOP;
            this.KEY_BTM = builder.KEY_BTM;
            this.GRID_COLORS = new byte[]{builder.GRID_BG, builder.GRID_NOTE, builder.GRID_BEAT, builder.GRID_MEASURE};
         }

         public byte getGridColor(MidiChartDialog.TimeSeparator sep) {
            return this.GRID_COLORS[sep.ordinal()];
         }

         public static MidiChartDialog.PianoRendering.PianoKeyColors.Builder builder() {
            return new MidiChartDialog.PianoRendering.PianoKeyColors.Builder();
         }

         // $FF: synthetic method
         PianoKeyColors(MidiChartDialog.PianoRendering.PianoKeyColors.Builder x0, Object x1) {
            this(x0);
         }

         public static class Builder {
            public byte KEY_TOP;
            public byte KEY_BTM;
            public byte GRID_BG;
            public byte GRID_NOTE;
            public byte GRID_BEAT;
            public byte GRID_MEASURE;

            public MidiChartDialog.PianoRendering.PianoKeyColors.Builder key_top(int r, int g, int b) {
               this.KEY_TOP = MapColorPalette.getColor(r, g, b);
               return this;
            }

            public MidiChartDialog.PianoRendering.PianoKeyColors.Builder key_btm(int r, int g, int b) {
               this.KEY_BTM = MapColorPalette.getColor(r, g, b);
               return this;
            }

            public MidiChartDialog.PianoRendering.PianoKeyColors.Builder grid_bg(int r, int g, int b) {
               this.GRID_BG = MapColorPalette.getColor(r, g, b);
               return this;
            }

            public MidiChartDialog.PianoRendering.PianoKeyColors.Builder grid_note(int r, int g, int b) {
               this.GRID_NOTE = MapColorPalette.getColor(r, g, b);
               return this;
            }

            public MidiChartDialog.PianoRendering.PianoKeyColors.Builder grid_beat(int r, int g, int b) {
               this.GRID_BEAT = MapColorPalette.getColor(r, g, b);
               return this;
            }

            public MidiChartDialog.PianoRendering.PianoKeyColors.Builder grid_measure(int r, int g, int b) {
               this.GRID_MEASURE = MapColorPalette.getColor(r, g, b);
               return this;
            }

            public MidiChartDialog.PianoRendering.PianoKeyColors build() {
               return new MidiChartDialog.PianoRendering.PianoKeyColors(this);
            }
         }
      }

      private static class BlackPianoKey extends MidiChartDialog.PianoRendering.PianoKey {
         public BlackPianoKey(MidiChartDialog.PianoRendering.PianoKeyColors colors_idle, MidiChartDialog.PianoRendering.PianoKeyColors colors_pressed) {
            super(colors_idle, colors_pressed);
         }

         public void drawKey(MapCanvas view, int y, MidiChartDialog.PianoRendering.PianoKeyColors colors) {
            view.drawLine(0, y, 3, y, colors.KEY_TOP);
            view.drawLine(0, y + 1, 3, y + 1, colors.KEY_BTM);
         }
      }

      private abstract static class PianoKey {
         private final MidiChartDialog.PianoRendering.PianoKeyColors colors_idle;
         private final MidiChartDialog.PianoRendering.PianoKeyColors colors_pressed;

         public PianoKey(MidiChartDialog.PianoRendering.PianoKeyColors colors_idle, MidiChartDialog.PianoRendering.PianoKeyColors colors_pressed) {
            this.colors_idle = colors_idle;
            this.colors_pressed = colors_pressed;
         }

         public abstract void drawKey(MapCanvas var1, int var2, MidiChartDialog.PianoRendering.PianoKeyColors var3);

         public final void draw(MapCanvas view, int y, int w, boolean pressed, MidiChartDialog.FindTimeSeparatorFunc timeSepFunc) {
            MidiChartDialog.PianoRendering.PianoKeyColors colors = pressed ? this.colors_pressed : this.colors_idle;
            this.drawKey(view, y, colors);
            byte bgColor = colors.getGridColor(MidiChartDialog.TimeSeparator.BACKGROUND);
            view.drawLine(6, y, w - 1, y, bgColor);
            view.drawLine(6, y + 1, w - 1, y + 1, bgColor);
            int timeStepIndex = 0;

            for(int x = 6; x < w; x += 3) {
               byte bgColor = colors.getGridColor(timeSepFunc.find(timeStepIndex));
               view.writePixel(x, y, bgColor);
               view.writePixel(x, y + 1, bgColor);
               ++timeStepIndex;
            }

         }
      }
   }

   private abstract static class ConfirmClearDialog extends MapWidgetMenu {
      public ConfirmClearDialog() {
         this.setBounds(10, 22, 98, 58);
         this.setBackgroundColor(MapColorPalette.getColor(135, 33, 33));
      }

      public abstract void onConfirmClear();

      public void onAttached() {
         super.onAttached();
         this.addWidget((new MapWidgetText()).setText("Are you sure you\nwant to clear\nthis chart?").setBounds(5, 5, 80, 30));
         this.addWidget((new MapWidgetButton() {
            public void onActivate() {
               ConfirmClearDialog.this.close();
            }
         }).setText("No").setBounds(10, 40, 36, 13));
         this.addWidget((new MapWidgetButton() {
            public void onActivate() {
               ConfirmClearDialog.this.close();
               ConfirmClearDialog.this.onConfirmClear();
            }
         }).setText("Yes").setBounds(52, 40, 36, 13));
      }
   }

   private abstract static class ChartSettingsDialog extends MapWidgetMenu {
      private MidiChartParameters params;

      public ChartSettingsDialog() {
         this.params = MidiChartParameters.DEFAULT;
         this.setBounds(10, 22, 98, 88);
         this.setBackgroundColor(MapColorPalette.getColor(138, 152, 180));
      }

      public abstract void onParamsChanged(MidiChartParameters var1);

      public MidiChartParameters getParams() {
         return this.params;
      }

      public MidiChartDialog.ChartSettingsDialog setParams(MidiChartParameters params) {
         this.params = params;
         this.invalidate();
         return this;
      }

      public void onAttached() {
         super.onAttached();
         int num_x_offset = true;
         int y_pos = 12;
         MapWidgetText label = new MapWidgetText();
         label.setFont(MapFont.TINY);
         label.setText("- Time Signature -");
         label.setPosition(15, 5);
         label.setColor(MapColorPalette.getColor(115, 108, 18));
         this.addWidget(label);
         this.addWidget((new MapWidgetNumberBox() {
            public void onAttached() {
               this.setRange(1.0D, 16.0D);
               this.setIncrement(1.0D);
               this.setInitialValue((double)ChartSettingsDialog.this.params.timeSignature().beatsPerMeasure());
               super.onAttached();
            }

            public void onResetValue() {
               this.setValue(4.0D);
            }

            public void onValueChangeEnd() {
               ChartSettingsDialog.this.params = ChartSettingsDialog.this.params.withTimeSignature(MidiTimeSignature.of((int)this.getValue(), ChartSettingsDialog.this.params.timeSignature().noteValue()));
               ChartSettingsDialog.this.onParamsChanged(ChartSettingsDialog.this.params);
            }
         }).setBounds(40, y_pos, this.getWidth() - 40, 13));
         this.addLabel(5, y_pos + 1, "Beats per");
         this.addLabel(5, y_pos + 7, "measure");
         int y_pos = y_pos + 16;
         ((<undefinedtype>)this.addWidget(new MapWidgetNumberBox() {
            public void onAttached() {
               this.setRange(1.0D, 16.0D);
               this.setIncrement(1.0D);
               this.setTextPrefix("1/");
               this.setInitialValue((double)ChartSettingsDialog.this.params.timeSignature().noteValue());
               super.onAttached();
            }

            public void onResetValue() {
               this.setValue(4.0D);
            }

            public void onValueChangeEnd() {
               ChartSettingsDialog.this.params = ChartSettingsDialog.this.params.withTimeSignature(MidiTimeSignature.of(ChartSettingsDialog.this.params.timeSignature().beatsPerMeasure(), (int)this.getValue()));
               ChartSettingsDialog.this.onParamsChanged(ChartSettingsDialog.this.params);
            }
         })).setBounds(40, y_pos, this.getWidth() - 40, 13);
         this.addLabel(5, y_pos + 4, "Note value");
         y_pos += 20;
         ((<undefinedtype>)this.addWidget(new MapWidgetNumberBox() {
            public void onAttached() {
               this.setRange(1.0D, 10000.0D);
               this.setIncrement(1.0D);
               this.setInitialValue((double)ChartSettingsDialog.this.params.bpm());
               super.onAttached();
            }

            public void onResetValue() {
               this.setValue(120.0D);
            }

            public void onValueChangeEnd() {
               ChartSettingsDialog.this.params = ChartSettingsDialog.this.params.withBPM((int)this.getValue());
               ChartSettingsDialog.this.onParamsChanged(ChartSettingsDialog.this.params);
            }
         })).setBounds(40, y_pos, this.getWidth() - 40, 13);
         this.addLabel(5, y_pos + 1, "Beats per");
         this.addLabel(5, y_pos + 7, "minute");
         y_pos += 17;
         ((<undefinedtype>)this.addWidget(new MapWidgetNumberBox() {
            public void onAttached() {
               this.setRange(1.0D, 192.0D);
               this.setIncrement(1.0D);
               this.setInitialValue((double)ChartSettingsDialog.this.params.pitchClasses());
               super.onAttached();
            }

            public void onResetValue() {
               this.setValue(12.0D);
            }

            public void onValueChangeEnd() {
               ChartSettingsDialog.this.params = ChartSettingsDialog.this.params.withPitchClasses((int)this.getValue());
               ChartSettingsDialog.this.onParamsChanged(ChartSettingsDialog.this.params);
            }
         })).setBounds(40, y_pos, this.getWidth() - 40, 13);
         this.addLabel(5, y_pos + 1, "Pitch");
         this.addLabel(5, y_pos + 7, "classes");
      }
   }
}
