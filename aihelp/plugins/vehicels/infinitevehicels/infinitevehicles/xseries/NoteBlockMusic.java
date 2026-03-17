package me.PM2.infinitevehicles.xseries;

import com.google.common.base.Strings;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;
import org.bukkit.Instrument;
import org.bukkit.Location;
import org.bukkit.Note;
import org.bukkit.Note.Tone;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class NoteBlockMusic {
   private static final Map<String, Instrument> INSTRUMENTS = new HashMap(50);
   private static final Map<Instrument, XSound> INSTRUMENT_TO_SOUND = new EnumMap(Instrument.class);

   private NoteBlockMusic() {
   }

   @NotNull
   public static XSound getSoundFromInstrument(@NotNull Instrument var0) {
      return (XSound)INSTRUMENT_TO_SOUND.get(var0);
   }

   @Nullable
   public static Tone getNoteTone(char var0) {
      switch(var0) {
      case 'A':
         return Tone.A;
      case 'B':
         return Tone.B;
      case 'C':
         return Tone.C;
      case 'D':
         return Tone.D;
      case 'E':
         return Tone.E;
      case 'F':
         return Tone.F;
      case 'G':
         return Tone.G;
      default:
         return null;
      }
   }

   public static void testMusic(@NotNull Player var0) {
      Objects.requireNonNull(var0);
      playMusic(var0, var0::getLocation, "PIANO,D,2,100 PIANO,B#1 200 PIANO,F 250 PIANO,E 250 PIANO,B 200 PIANO,A 100 PIANO,B 100 PIANO,E");
   }

   public static void fromFile(@NotNull Player var0, @NotNull Supplier<Location> var1, @NotNull Path var2) {
      try {
         BufferedReader var3 = Files.newBufferedReader(var2, StandardCharsets.UTF_8);

         String var4;
         try {
            while((var4 = var3.readLine()) != null) {
               var4 = var4.trim();
               if (!var4.isEmpty() && !var4.startsWith("#")) {
                  parseInstructions(var4).play(var0, var1, true);
               }
            }
         } catch (Throwable var7) {
            if (var3 != null) {
               try {
                  var3.close();
               } catch (Throwable var6) {
                  var7.addSuppressed(var6);
               }
            }

            throw var7;
         }

         if (var3 != null) {
            var3.close();
         }
      } catch (IOException var8) {
         var8.printStackTrace();
      }

   }

   public static void playMusic(@NotNull Player var0, @NotNull Supplier<Location> var1, @Nullable String var2) {
      if (!Strings.isNullOrEmpty(var2)) {
         NoteBlockMusic.Sequence var3 = parseInstructions(var2);
         var3.play(var0, var1, true);
      }
   }

   public static NoteBlockMusic.Sequence parseInstructions(@NotNull CharSequence var0) {
      return (new NoteBlockMusic.InstructionBuilder(var0)).sequence;
   }

   private static void sleep(long var0) {
      try {
         Thread.sleep(var0);
      } catch (InterruptedException var3) {
         Thread.currentThread().interrupt();
      }

   }

   @Nullable
   public static Note parseNote(@NotNull String var0) {
      Tone var1 = getNoteTone((char)(var0.charAt(0) & 95));
      if (var1 == null) {
         return null;
      } else {
         int var2 = var0.length();
         char var3 = ' ';
         int var4 = 0;
         if (var2 > 1) {
            var3 = var0.charAt(1);
            if (isDigit(var3)) {
               var4 = var3 - 48;
            } else if (var2 > 2) {
               char var5 = var0.charAt(2);
               if (isDigit(var5)) {
                  var4 = var5 - 48;
               }
            }

            if (var4 < 0 || var4 > 2) {
               var4 = 0;
            }
         }

         return var3 == '#' ? Note.sharp(var4, var1) : (var3 == '_' ? Note.flat(var4, var1) : Note.natural(var4, var1));
      }
   }

   private static boolean isDigit(char var0) {
      return var0 >= '0' && var0 <= '9';
   }

   public static float noteToPitch(@NotNull Note var0) {
      return (float)Math.pow(2.0D, ((double)var0.getId() - 12.0D) / 12.0D);
   }

   @NotNull
   public static BukkitTask playAscendingNote(@NotNull Plugin var0, @NotNull final Player var1, @NotNull final Entity var2, @NotNull final Instrument var3, final int var4, int var5) {
      Objects.requireNonNull(var1, "Cannot play note from null player");
      Objects.requireNonNull(var2, "Cannot play note to null entity");
      if (var4 <= 0) {
         throw new IllegalArgumentException("Note ascend level cannot be lower than 1");
      } else if (var4 > 7) {
         throw new IllegalArgumentException("Note ascend level cannot be greater than 7");
      } else if (var5 <= 0) {
         throw new IllegalArgumentException("Delay ticks must be at least 1");
      } else {
         return (new BukkitRunnable() {
            int repeating = var4;

            public void run() {
               var1.playNote(var2.getLocation(), var3, Note.natural(1, Tone.values()[var4 - this.repeating]));
               if (this.repeating-- == 0) {
                  this.cancel();
               }

            }
         }).runTaskTimerAsynchronously(var0, 0L, (long)var5);
      }
   }

   static {
      INSTRUMENT_TO_SOUND.put(Instrument.PIANO, XSound.BLOCK_NOTE_BLOCK_HARP);
      INSTRUMENT_TO_SOUND.put(Instrument.BASS_DRUM, XSound.BLOCK_NOTE_BLOCK_BASEDRUM);
      INSTRUMENT_TO_SOUND.put(Instrument.SNARE_DRUM, XSound.BLOCK_NOTE_BLOCK_SNARE);
      INSTRUMENT_TO_SOUND.put(Instrument.STICKS, XSound.BLOCK_NOTE_BLOCK_HAT);
      INSTRUMENT_TO_SOUND.put(Instrument.BASS_GUITAR, XSound.BLOCK_NOTE_BLOCK_BASS);
      INSTRUMENT_TO_SOUND.put(Instrument.FLUTE, XSound.BLOCK_NOTE_BLOCK_FLUTE);
      INSTRUMENT_TO_SOUND.put(Instrument.BELL, XSound.BLOCK_NOTE_BLOCK_BELL);
      INSTRUMENT_TO_SOUND.put(Instrument.GUITAR, XSound.BLOCK_NOTE_BLOCK_GUITAR);
      INSTRUMENT_TO_SOUND.put(Instrument.CHIME, XSound.BLOCK_NOTE_BLOCK_CHIME);
      INSTRUMENT_TO_SOUND.put(Instrument.XYLOPHONE, XSound.BLOCK_NOTE_BLOCK_XYLOPHONE);
      INSTRUMENT_TO_SOUND.put(Instrument.IRON_XYLOPHONE, XSound.BLOCK_NOTE_BLOCK_IRON_XYLOPHONE);
      INSTRUMENT_TO_SOUND.put(Instrument.COW_BELL, XSound.BLOCK_NOTE_BLOCK_COW_BELL);
      INSTRUMENT_TO_SOUND.put(Instrument.DIDGERIDOO, XSound.BLOCK_NOTE_BLOCK_DIDGERIDOO);
      INSTRUMENT_TO_SOUND.put(Instrument.BIT, XSound.BLOCK_NOTE_BLOCK_BIT);
      INSTRUMENT_TO_SOUND.put(Instrument.BANJO, XSound.BLOCK_NOTE_BLOCK_BANJO);
      INSTRUMENT_TO_SOUND.put(Instrument.PLING, XSound.BLOCK_NOTE_BLOCK_PLING);
      INSTRUMENTS.put("HARP", Instrument.PIANO);
      INSTRUMENTS.put("BASEDRUM", Instrument.BASS_DRUM);
      INSTRUMENTS.put("BASE_DRUM", Instrument.BASS_DRUM);
      INSTRUMENTS.put("SNARE", Instrument.SNARE_DRUM);
      INSTRUMENTS.put("BASS", Instrument.BASS_GUITAR);
      INSTRUMENTS.put("COWBELL", Instrument.COW_BELL);
      Instrument[] var0 = Instrument.values();
      int var1 = var0.length;

      for(int var2 = 0; var2 < var1; ++var2) {
         Instrument var3 = var0[var2];
         String var4 = var3.name();
         INSTRUMENTS.put(var4, var3);
         StringBuilder var5 = new StringBuilder(String.valueOf(var4.charAt(0)));
         int var6 = var4.indexOf(95);
         if (var6 != -1) {
            var5.append(var4.charAt(var6 + 1));
         }

         if (INSTRUMENTS.putIfAbsent(var5.toString(), var3) != null) {
            for(int var7 = 0; var7 < var4.length(); ++var7) {
               char var8 = var4.charAt(var7);
               if (var8 == '_') {
                  ++var7;
               } else {
                  var5.append(var8);
                  if (INSTRUMENTS.putIfAbsent(var5.toString(), var3) == null) {
                     break;
                  }
               }
            }
         }
      }

   }

   public static class Sequence extends NoteBlockMusic.Instruction {
      public Collection<NoteBlockMusic.Instruction> instructions = new ArrayList(16);

      public Sequence() {
         super(1, 0, 0);
      }

      public Sequence(NoteBlockMusic.Instruction var1) {
         super(1, 0, 0);
         this.instructions.add(var1);
      }

      public Sequence(int var1, int var2, int var3) {
         super(var1, var2, var3);
      }

      public void play(Player var1, Supplier<Location> var2, boolean var3) {
         for(int var4 = this.restatement; var4 > 0; --var4) {
            Iterator var5 = this.instructions.iterator();

            while(var5.hasNext()) {
               NoteBlockMusic.Instruction var6 = (NoteBlockMusic.Instruction)var5.next();
               var6.play(var1, var2, var3);
            }

            if (this.restatementFermata > 0) {
               NoteBlockMusic.sleep((long)this.restatementFermata);
            }
         }

         if (this.fermata > 0) {
            NoteBlockMusic.sleep((long)this.fermata);
         }

      }

      public String toString() {
         StringBuilder var1 = new StringBuilder(200 + this.instructions.size() * 100);
         var1.append("Sequence:{restatement=").append(this.restatement).append(", restatementFermata=").append(this.restatementFermata).append(", fermata=").append(this.fermata).append(", instructions[");
         int var2 = 0;
         int var3 = this.instructions.size();
         Iterator var4 = this.instructions.iterator();

         while(var4.hasNext()) {
            NoteBlockMusic.Instruction var5 = (NoteBlockMusic.Instruction)var4.next();
            var1.append(var5);
            ++var2;
            if (var2 < var3) {
               var1.append(", ");
            }
         }

         var1.append("]}");
         return var1.toString();
      }

      public void addInstruction(NoteBlockMusic.Instruction var1) {
         var1.parent = this;
         this.instructions.add(var1);
      }

      public long getEstimatedLength() {
         long var1 = (long)this.restatement * (long)this.restatementFermata;

         NoteBlockMusic.Instruction var4;
         for(Iterator var3 = this.instructions.iterator(); var3.hasNext(); var1 += var4.getEstimatedLength()) {
            var4 = (NoteBlockMusic.Instruction)var3.next();
         }

         return var1;
      }
   }

   private static final class InstructionBuilder {
      @NotNull
      final CharSequence script;
      final int len;
      final StringBuilder instrumentBuilder = new StringBuilder(10);
      final StringBuilder pitchBuiler = new StringBuilder(3);
      final StringBuilder volumeBuilder = new StringBuilder(3);
      final StringBuilder restatementBuilder = new StringBuilder(10);
      final StringBuilder restatementDelayBuilder = new StringBuilder(10);
      final StringBuilder fermataBuilder = new StringBuilder(10);
      int i;
      boolean isSequence;
      boolean isBuilding;
      NoteBlockMusic.Sequence sequence = new NoteBlockMusic.Sequence();
      NoteBlockMusic.InstructionParserPhase phase;
      StringBuilder currentBuilder;

      public InstructionBuilder(@NotNull CharSequence var1) {
         this.phase = NoteBlockMusic.InstructionParserPhase.NEUTRAL;
         this.script = var1;

         for(this.len = var1.length(); this.i < this.len; ++this.i) {
            char var2 = var1.charAt(this.i);
            switch(var2) {
            case ' ':
               if (this.isBuilding) {
                  this.isBuilding = false;
                  switch(this.phase.ordinal()) {
                  case 2:
                  case 5:
                     this.phase = NoteBlockMusic.InstructionParserPhase.FERMATA;
                     this.currentBuilder = this.fermataBuilder;
                  case 3:
                  case 4:
                  default:
                     continue;
                  case 6:
                     this.buildAndAddInstruction();
                     this.prepareHandlers();
                  }
               }
               continue;
            case '(':
               NoteBlockMusic.Sequence var3 = new NoteBlockMusic.Sequence();
               var3.parent = this.sequence;
               this.sequence = var3;
               continue;
            case ')':
               if (this.sequence.parent == null) {
                  this.err("Cannot find start of the sequence for sequence at: " + this.i);
               }

               this.buildAndAddInstruction();
               this.sequence = this.sequence.parent;
               this.prepareHandlers();
               this.phase = NoteBlockMusic.InstructionParserPhase.END_SEQ;
               this.isSequence = true;
               continue;
            case ',':
               switch(this.phase.ordinal()) {
               case 1:
                  this.currentBuilder = this.pitchBuiler;
                  break;
               case 2:
               case 3:
                  this.currentBuilder = this.restatementBuilder;
                  break;
               case 4:
                  this.currentBuilder = this.restatementDelayBuilder;
                  break;
               default:
                  this.err("Unexpected phase '" + this.phase + "' at index: " + this.i);
               }

               this.isBuilding = false;
               this.phase = this.phase.next();
               continue;
            case ':':
               if (this.phase == NoteBlockMusic.InstructionParserPhase.NOTE) {
                  this.currentBuilder = this.volumeBuilder;
               } else {
                  this.err("Unexpected ':' pitch-volume separator at " + this.i + " with current phase: " + this.phase);
               }
               continue;
            }

            if (this.phase == NoteBlockMusic.InstructionParserPhase.NEUTRAL || this.canBuildInstructionInPhase() && NoteBlockMusic.InstructionParserPhase.INSTRUMENT.checkup(var2) != 0) {
               this.currentBuilder = this.instrumentBuilder;
               if (this.phase == NoteBlockMusic.InstructionParserPhase.FERMATA) {
                  this.buildAndAddInstruction();
                  this.prepareHandlers();
               }

               this.phase = NoteBlockMusic.InstructionParserPhase.INSTRUMENT;
            }

            this.isBuilding = true;
            if ((var2 = this.phase.checkup(var2)) == 0) {
               this.err("Unexpected char at index " + this.i + " with phase " + this.phase + ": " + var1.charAt(this.i));
            }

            this.currentBuilder.append(var2);
         }

         this.buildAndAddInstruction();
         this.sequence = this.getRoot();
      }

      private NoteBlockMusic.Instruction buildInstruction() {
         int var1 = this.fermataBuilder.length() == 0 ? 0 : Integer.parseInt(this.fermataBuilder.toString());
         int var2 = this.restatementBuilder.length() == 0 ? 1 : Integer.parseInt(this.restatementBuilder.toString());
         int var3 = this.restatementDelayBuilder.length() == 0 ? 0 : Integer.parseInt(this.restatementDelayBuilder.toString());
         Object var4;
         if (this.isSequence) {
            var4 = new NoteBlockMusic.Sequence(var2, var3, var1);
         } else {
            String var5 = this.instrumentBuilder.toString();
            Instrument var7 = (Instrument)NoteBlockMusic.INSTRUMENTS.get(var5);
            XSound var6;
            if (var7 == null) {
               var6 = (XSound)XSound.matchXSound(var5).orElse((Object)null);
            } else {
               var6 = NoteBlockMusic.getSoundFromInstrument(var7);
            }

            String var8 = this.pitchBuiler.toString();
            Note var10 = NoteBlockMusic.parseNote(var8);
            float var9;
            if (var10 == null) {
               var9 = Float.parseFloat(var8);
            } else {
               var9 = NoteBlockMusic.noteToPitch(var10);
            }

            float var11 = 5.0F;
            if (this.volumeBuilder.length() != 0) {
               var11 = Float.parseFloat(this.volumeBuilder.toString());
            }

            var4 = new NoteBlockMusic.Sound(var6, var9, var11, var2, var3, var1);
         }

         return (NoteBlockMusic.Instruction)var4;
      }

      private void prepareHandlers() {
         this.instrumentBuilder.setLength(0);
         this.pitchBuiler.setLength(0);
         this.volumeBuilder.setLength(0);
         this.restatementBuilder.setLength(0);
         this.restatementDelayBuilder.setLength(0);
         this.fermataBuilder.setLength(0);
         this.phase = NoteBlockMusic.InstructionParserPhase.NEUTRAL;
         this.isBuilding = false;
         this.isSequence = false;
      }

      private boolean canBuildInstructionInPhase() {
         switch(this.phase.ordinal()) {
         case 4:
         case 5:
         case 6:
            return true;
         default:
            return false;
         }
      }

      private void buildAndAddInstruction() {
         this.sequence.addInstruction(this.buildInstruction());
      }

      private NoteBlockMusic.Sequence getRoot() {
         NoteBlockMusic.Sequence var1;
         for(var1 = this.sequence; var1.parent != null; var1 = var1.parent) {
         }

         return var1;
      }

      private String illustrateError() {
         return '\n' + this.script.toString() + '\n' + Strings.repeat(" ", this.i) + '^';
      }

      private void err(String var1) {
         throw new IllegalStateException(var1 + this.illustrateError());
      }
   }

   public abstract static class Instruction {
      @Nullable
      public NoteBlockMusic.Sequence parent;
      public int restatement;
      public int restatementFermata;
      public int fermata;

      public Instruction(int var1, int var2, int var3) {
         this.restatement = var1;
         this.restatementFermata = var2;
         this.fermata = var3;
      }

      public abstract void play(Player var1, Supplier<Location> var2, boolean var3);

      public long getEstimatedLength() {
         return (long)this.restatement * (long)this.restatementFermata;
      }
   }

   public static class Sound extends NoteBlockMusic.Instruction {
      public XSound sound;
      public float volume;
      public float pitch;

      public Sound(Instrument var1, Note var2, float var3, int var4, int var5, int var6) {
         super(var4, var5, var6);
         this.sound = NoteBlockMusic.getSoundFromInstrument(var1);
         this.pitch = NoteBlockMusic.noteToPitch(var2);
         this.volume = var3;
      }

      public Sound(XSound var1, float var2, float var3, int var4, int var5, int var6) {
         super(var4, var5, var6);
         this.sound = var1;
         this.pitch = var2;
         this.volume = var3;
      }

      public void setSound(Instrument var1) {
         this.sound = NoteBlockMusic.getSoundFromInstrument(var1);
      }

      public void setPitch(Note var1) {
         this.pitch = NoteBlockMusic.noteToPitch(var1);
      }

      public void play(Player var1, Supplier<Location> var2, boolean var3) {
         org.bukkit.Sound var4 = (org.bukkit.Sound)this.sound.get();

         for(int var5 = this.restatement; var5 > 0; --var5) {
            Location var6 = (Location)var2.get();
            if (var4 != null) {
               if (var3) {
                  var6.getWorld().playSound(var6, var4, this.volume, this.pitch);
               } else {
                  var1.playSound(var6, var4, this.volume, this.pitch);
               }
            }

            if (this.restatementFermata > 0) {
               NoteBlockMusic.sleep((long)this.restatementFermata);
            }
         }

         if (this.fermata > 0) {
            NoteBlockMusic.sleep((long)this.fermata);
         }

      }

      public String toString() {
         return "Sound:{sound=" + this.sound + ", pitch=" + this.pitch + ", volume=" + this.volume + ", restatement=" + this.restatement + ", restatementFermata=" + this.restatementFermata + ", fermata=" + this.fermata + '}';
      }
   }

   private static enum InstructionParserPhase {
      NEUTRAL {
         protected NoteBlockMusic.InstructionParserPhase next() {
            return INSTRUMENT;
         }

         protected char checkup(char var1) {
            throw new AssertionError("Checkup should not be performed on NEUTRAL instruction parser phase");
         }
      },
      INSTRUMENT {
         protected NoteBlockMusic.InstructionParserPhase next() {
            return NOTE;
         }

         protected char checkup(char var1) {
            if (var1 >= 'a' && var1 <= 'z') {
               return (char)(var1 & 95);
            } else {
               return (var1 < 'A' || var1 > 'Z') && var1 != '_' && var1 != '-' ? '\u0000' : var1;
            }
         }
      },
      NOTE {
         protected NoteBlockMusic.InstructionParserPhase next() {
            return RESTATEMENT;
         }

         protected char checkup(char var1) {
            if (var1 >= 'a' && var1 <= 'z') {
               return (char)(var1 & 95);
            } else {
               return (var1 < 'A' || var1 > 'Z') && !NoteBlockMusic.isDigit(var1) && var1 != '.' && var1 != '_' && var1 != '#' ? '\u0000' : var1;
            }
         }
      },
      END_SEQ {
         protected NoteBlockMusic.InstructionParserPhase next() {
            return RESTATEMENT;
         }

         protected char checkup(char var1) {
            return '\u0000';
         }
      },
      RESTATEMENT {
         protected NoteBlockMusic.InstructionParserPhase next() {
            return RESTATEMENT_DELAY;
         }

         protected char checkup(char var1) {
            return NoteBlockMusic.isDigit(var1) ? var1 : '\u0000';
         }
      },
      RESTATEMENT_DELAY {
         protected NoteBlockMusic.InstructionParserPhase next() {
            return FERMATA;
         }

         protected char checkup(char var1) {
            return NoteBlockMusic.isDigit(var1) ? var1 : '\u0000';
         }
      },
      FERMATA {
         protected NoteBlockMusic.InstructionParserPhase next() {
            return NEUTRAL;
         }

         protected char checkup(char var1) {
            return NoteBlockMusic.isDigit(var1) ? var1 : '\u0000';
         }
      };

      private InstructionParserPhase() {
      }

      protected abstract NoteBlockMusic.InstructionParserPhase next();

      protected abstract char checkup(char var1);

      // $FF: synthetic method
      private static NoteBlockMusic.InstructionParserPhase[] $values() {
         return new NoteBlockMusic.InstructionParserPhase[]{NEUTRAL, INSTRUMENT, NOTE, END_SEQ, RESTATEMENT, RESTATEMENT_DELAY, FERMATA};
      }

      // $FF: synthetic method
      InstructionParserPhase(Object var3) {
         this();
      }
   }
}
