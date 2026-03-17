package me.PM2.infinitevehicles.commands.lib.expiringmap;

import java.util.concurrent.TimeUnit;

public final class ExpiringValue<V> {
   private static final long UNSET_DURATION = -1L;
   private final V value;
   private final ExpirationPolicy expirationPolicy;
   private final long duration;
   private final TimeUnit timeUnit;

   public ExpiringValue(V var1) {
      this(var1, -1L, (TimeUnit)null, (ExpirationPolicy)null);
   }

   public ExpiringValue(V var1, ExpirationPolicy var2) {
      this(var1, -1L, (TimeUnit)null, var2);
   }

   public ExpiringValue(V var1, long var2, TimeUnit var4) {
      this(var1, var2, var4, (ExpirationPolicy)null);
      if (var4 == null) {
         throw new NullPointerException();
      }
   }

   public ExpiringValue(V var1, ExpirationPolicy var2, long var3, TimeUnit var5) {
      this(var1, var3, var5, var2);
      if (var5 == null) {
         throw new NullPointerException();
      }
   }

   private ExpiringValue(V var1, long var2, TimeUnit var4, ExpirationPolicy var5) {
      this.value = var1;
      this.expirationPolicy = var5;
      this.duration = var2;
      this.timeUnit = var4;
   }

   public V getValue() {
      return this.value;
   }

   public ExpirationPolicy getExpirationPolicy() {
      return this.expirationPolicy;
   }

   public long getDuration() {
      return this.duration;
   }

   public TimeUnit getTimeUnit() {
      return this.timeUnit;
   }

   public int hashCode() {
      return this.value != null ? this.value.hashCode() : 0;
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (var1 != null && this.getClass() == var1.getClass()) {
         boolean var10000;
         label28: {
            ExpiringValue var2 = (ExpiringValue)var1;
            if (this.value != null) {
               if (!this.value.equals(var2.value)) {
                  break label28;
               }
            } else if (var2.value != null) {
               break label28;
            }

            if (this.expirationPolicy == var2.expirationPolicy && this.duration == var2.duration && this.timeUnit == var2.timeUnit) {
               var10000 = true;
               return var10000;
            }
         }

         var10000 = false;
         return var10000;
      } else {
         return false;
      }
   }

   public String toString() {
      return "ExpiringValue{value=" + this.value + ", expirationPolicy=" + this.expirationPolicy + ", duration=" + this.duration + ", timeUnit=" + this.timeUnit + '}';
   }
}
