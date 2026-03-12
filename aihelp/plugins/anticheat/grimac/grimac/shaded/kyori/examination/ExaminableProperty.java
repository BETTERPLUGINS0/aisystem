package ac.grim.grimac.shaded.kyori.examination;

import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;

public abstract class ExaminableProperty {
   private ExaminableProperty() {
   }

   @NotNull
   public abstract String name();

   @NotNull
   public abstract <R> R examine(@NotNull final Examiner<? extends R> examiner);

   public String toString() {
      return "ExaminableProperty{" + this.name() + "}";
   }

   @NotNull
   public static ExaminableProperty of(@NotNull final String name, @Nullable final Object value) {
      return new ExaminableProperty() {
         @NotNull
         public String name() {
            return name;
         }

         @NotNull
         public <R> R examine(@NotNull final Examiner<? extends R> examiner) {
            return examiner.examine(value);
         }
      };
   }

   @NotNull
   public static ExaminableProperty of(@NotNull final String name, @Nullable final String value) {
      return new ExaminableProperty() {
         @NotNull
         public String name() {
            return name;
         }

         @NotNull
         public <R> R examine(@NotNull final Examiner<? extends R> examiner) {
            return examiner.examine(value);
         }
      };
   }

   @NotNull
   public static ExaminableProperty of(@NotNull final String name, final boolean value) {
      return new ExaminableProperty() {
         @NotNull
         public String name() {
            return name;
         }

         @NotNull
         public <R> R examine(@NotNull final Examiner<? extends R> examiner) {
            return examiner.examine(value);
         }
      };
   }

   @NotNull
   public static ExaminableProperty of(@NotNull final String name, final boolean[] value) {
      return new ExaminableProperty() {
         @NotNull
         public String name() {
            return name;
         }

         @NotNull
         public <R> R examine(@NotNull final Examiner<? extends R> examiner) {
            return examiner.examine(value);
         }
      };
   }

   @NotNull
   public static ExaminableProperty of(@NotNull final String name, final byte value) {
      return new ExaminableProperty() {
         @NotNull
         public String name() {
            return name;
         }

         @NotNull
         public <R> R examine(@NotNull final Examiner<? extends R> examiner) {
            return examiner.examine(value);
         }
      };
   }

   @NotNull
   public static ExaminableProperty of(@NotNull final String name, final byte[] value) {
      return new ExaminableProperty() {
         @NotNull
         public String name() {
            return name;
         }

         @NotNull
         public <R> R examine(@NotNull final Examiner<? extends R> examiner) {
            return examiner.examine(value);
         }
      };
   }

   @NotNull
   public static ExaminableProperty of(@NotNull final String name, final char value) {
      return new ExaminableProperty() {
         @NotNull
         public String name() {
            return name;
         }

         @NotNull
         public <R> R examine(@NotNull final Examiner<? extends R> examiner) {
            return examiner.examine(value);
         }
      };
   }

   @NotNull
   public static ExaminableProperty of(@NotNull final String name, final char[] value) {
      return new ExaminableProperty() {
         @NotNull
         public String name() {
            return name;
         }

         @NotNull
         public <R> R examine(@NotNull final Examiner<? extends R> examiner) {
            return examiner.examine(value);
         }
      };
   }

   @NotNull
   public static ExaminableProperty of(@NotNull final String name, final double value) {
      return new ExaminableProperty() {
         @NotNull
         public String name() {
            return name;
         }

         @NotNull
         public <R> R examine(@NotNull final Examiner<? extends R> examiner) {
            return examiner.examine(value);
         }
      };
   }

   @NotNull
   public static ExaminableProperty of(@NotNull final String name, final double[] value) {
      return new ExaminableProperty() {
         @NotNull
         public String name() {
            return name;
         }

         @NotNull
         public <R> R examine(@NotNull final Examiner<? extends R> examiner) {
            return examiner.examine(value);
         }
      };
   }

   @NotNull
   public static ExaminableProperty of(@NotNull final String name, final float value) {
      return new ExaminableProperty() {
         @NotNull
         public String name() {
            return name;
         }

         @NotNull
         public <R> R examine(@NotNull final Examiner<? extends R> examiner) {
            return examiner.examine(value);
         }
      };
   }

   @NotNull
   public static ExaminableProperty of(@NotNull final String name, final float[] value) {
      return new ExaminableProperty() {
         @NotNull
         public String name() {
            return name;
         }

         @NotNull
         public <R> R examine(@NotNull final Examiner<? extends R> examiner) {
            return examiner.examine(value);
         }
      };
   }

   @NotNull
   public static ExaminableProperty of(@NotNull final String name, final int value) {
      return new ExaminableProperty() {
         @NotNull
         public String name() {
            return name;
         }

         @NotNull
         public <R> R examine(@NotNull final Examiner<? extends R> examiner) {
            return examiner.examine(value);
         }
      };
   }

   @NotNull
   public static ExaminableProperty of(@NotNull final String name, final int[] value) {
      return new ExaminableProperty() {
         @NotNull
         public String name() {
            return name;
         }

         @NotNull
         public <R> R examine(@NotNull final Examiner<? extends R> examiner) {
            return examiner.examine(value);
         }
      };
   }

   @NotNull
   public static ExaminableProperty of(@NotNull final String name, final long value) {
      return new ExaminableProperty() {
         @NotNull
         public String name() {
            return name;
         }

         @NotNull
         public <R> R examine(@NotNull final Examiner<? extends R> examiner) {
            return examiner.examine(value);
         }
      };
   }

   @NotNull
   public static ExaminableProperty of(@NotNull final String name, final long[] value) {
      return new ExaminableProperty() {
         @NotNull
         public String name() {
            return name;
         }

         @NotNull
         public <R> R examine(@NotNull final Examiner<? extends R> examiner) {
            return examiner.examine(value);
         }
      };
   }

   @NotNull
   public static ExaminableProperty of(@NotNull final String name, final short value) {
      return new ExaminableProperty() {
         @NotNull
         public String name() {
            return name;
         }

         @NotNull
         public <R> R examine(@NotNull final Examiner<? extends R> examiner) {
            return examiner.examine(value);
         }
      };
   }

   @NotNull
   public static ExaminableProperty of(@NotNull final String name, final short[] value) {
      return new ExaminableProperty() {
         @NotNull
         public String name() {
            return name;
         }

         @NotNull
         public <R> R examine(@NotNull final Examiner<? extends R> examiner) {
            return examiner.examine(value);
         }
      };
   }

   // $FF: synthetic method
   ExaminableProperty(Object x0) {
      this();
   }
}
