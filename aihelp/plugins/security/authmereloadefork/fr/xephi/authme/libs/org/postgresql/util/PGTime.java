package fr.xephi.authme.libs.org.postgresql.util;

import java.sql.Time;
import java.util.Calendar;
import org.checkerframework.checker.nullness.qual.Nullable;

public class PGTime extends Time {
   private static final long serialVersionUID = 3592492258676494276L;
   @Nullable
   private Calendar calendar;

   public PGTime(long time) {
      this(time, (Calendar)null);
   }

   public PGTime(long time, @Nullable Calendar calendar) {
      super(time);
      this.calendar = calendar;
   }

   public void setCalendar(@Nullable Calendar calendar) {
      this.calendar = calendar;
   }

   @Nullable
   public Calendar getCalendar() {
      return this.calendar;
   }

   public int hashCode() {
      int prime = true;
      int result = super.hashCode();
      result = 31 * result + (this.calendar == null ? 0 : this.calendar.hashCode());
      return result;
   }

   public boolean equals(@Nullable Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         if (!super.equals(o)) {
            return false;
         } else {
            PGTime pgTime = (PGTime)o;
            return this.calendar != null ? this.calendar.equals(pgTime.calendar) : pgTime.calendar == null;
         }
      } else {
         return false;
      }
   }

   public Object clone() {
      PGTime clone = (PGTime)super.clone();
      Calendar calendar = this.getCalendar();
      if (calendar != null) {
         clone.setCalendar((Calendar)calendar.clone());
      }

      return clone;
   }
}
