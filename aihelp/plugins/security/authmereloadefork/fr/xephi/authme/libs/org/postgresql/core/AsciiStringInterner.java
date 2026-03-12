package fr.xephi.authme.libs.org.postgresql.core;

import fr.xephi.authme.libs.org.postgresql.util.internal.Nullness;
import java.io.IOException;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.checkerframework.checker.nullness.qual.Nullable;

final class AsciiStringInterner {
   final ConcurrentMap<AsciiStringInterner.BaseKey, SoftReference<String>> cache = new ConcurrentHashMap(128);
   final ReferenceQueue<String> refQueue = new ReferenceQueue();

   public boolean putString(String val) {
      byte[] copy = val.getBytes(StandardCharsets.UTF_8);
      int hash = hashKey(copy, 0, copy.length);
      if (hash == 0) {
         return false;
      } else {
         AsciiStringInterner.Key key = new AsciiStringInterner.Key(copy, hash);
         this.cache.put(key, new SoftReference(val));
         return true;
      }
   }

   public String getString(byte[] bytes, int offset, int length, Encoding encoding) throws IOException {
      if (length == 0) {
         return "";
      } else {
         int hash = hashKey(bytes, offset, length);
         if (hash == 0) {
            return encoding.decode(bytes, offset, length);
         } else {
            this.cleanQueue();
            AsciiStringInterner.TempKey tempKey = new AsciiStringInterner.TempKey(hash, bytes, offset, length);
            SoftReference<String> ref = (SoftReference)this.cache.get(tempKey);
            if (ref != null) {
               String val = (String)ref.get();
               if (val != null) {
                  return val;
               }
            }

            byte[] copy = Arrays.copyOfRange(bytes, offset, offset + length);
            AsciiStringInterner.Key key = new AsciiStringInterner.Key(copy, hash);
            String value = new String(copy, StandardCharsets.US_ASCII);
            ref = (SoftReference)this.cache.compute(key, (k, v) -> {
               if (v == null) {
                  return new AsciiStringInterner.StringReference(key, value);
               } else {
                  String val = (String)v.get();
                  return (SoftReference)(val != null ? v : new AsciiStringInterner.StringReference(key, value));
               }
            });
            return (String)Nullness.castNonNull((String)ref.get());
         }
      }
   }

   public String getStringIfPresent(byte[] bytes, int offset, int length, Encoding encoding) throws IOException {
      if (length == 0) {
         return "";
      } else {
         int hash = hashKey(bytes, offset, length);
         if (hash == 0) {
            return encoding.decode(bytes, offset, length);
         } else {
            this.cleanQueue();
            AsciiStringInterner.TempKey tempKey = new AsciiStringInterner.TempKey(hash, bytes, offset, length);
            SoftReference<String> ref = (SoftReference)this.cache.get(tempKey);
            if (ref != null) {
               String val = (String)ref.get();
               if (val != null) {
                  return val;
               }
            }

            return new String(bytes, offset, length, StandardCharsets.US_ASCII);
         }
      }
   }

   private void cleanQueue() {
      Reference ref;
      while((ref = this.refQueue.poll()) != null) {
         ((AsciiStringInterner.StringReference)ref).dispose();
      }

   }

   private static int hashKey(byte[] bytes, int offset, int length) {
      int result = 1;
      int i = offset;

      for(int j = offset + length; i < j; ++i) {
         byte b = bytes[i];
         if (b < 0) {
            return 0;
         }

         result = 31 * result + b;
      }

      return result;
   }

   static boolean arrayEquals(byte[] a, int aOffset, int aLength, byte[] b, int bOffset, int bLength) {
      if (aLength != bLength) {
         return false;
      } else {
         for(int i = 0; i < aLength; ++i) {
            if (a[aOffset + i] != b[bOffset + i]) {
               return false;
            }
         }

         return true;
      }
   }

   public String toString() {
      StringBuilder sb = new StringBuilder(32 + 8 * this.cache.size());
      sb.append("AsciiStringInterner [");
      this.cache.forEach((k, v) -> {
         sb.append('\'');
         k.appendString(sb);
         sb.append("', ");
      });
      int length = sb.length();
      if (length > 21) {
         sb.setLength(sb.length() - 2);
      }

      sb.append(']');
      return sb.toString();
   }

   private static final class Key extends AsciiStringInterner.BaseKey {
      final byte[] key;

      Key(byte[] key, int hash) {
         super(hash);
         this.key = key;
      }

      boolean equalsBytes(AsciiStringInterner.BaseKey other) {
         return other.equals(this.key, 0, this.key.length);
      }

      public boolean equals(byte[] other, int offset, int length) {
         return AsciiStringInterner.arrayEquals(this.key, 0, this.key.length, other, offset, length);
      }

      void appendString(StringBuilder sb) {
         for(int i = 0; i < this.key.length; ++i) {
            sb.append((char)this.key[i]);
         }

      }
   }

   private static class TempKey extends AsciiStringInterner.BaseKey {
      final byte[] bytes;
      final int offset;
      final int length;

      TempKey(int hash, byte[] bytes, int offset, int length) {
         super(hash);
         this.bytes = bytes;
         this.offset = offset;
         this.length = length;
      }

      boolean equalsBytes(AsciiStringInterner.BaseKey other) {
         return other.equals(this.bytes, this.offset, this.length);
      }

      public boolean equals(byte[] other, int offset, int length) {
         return AsciiStringInterner.arrayEquals(this.bytes, this.offset, this.length, other, offset, length);
      }

      void appendString(StringBuilder sb) {
         int i = this.offset;

         for(int j = this.offset + this.length; i < j; ++i) {
            sb.append((char)this.bytes[i]);
         }

      }
   }

   private final class StringReference extends SoftReference<String> {
      private final AsciiStringInterner.BaseKey key;

      StringReference(AsciiStringInterner.BaseKey key, String referent) {
         super(referent, AsciiStringInterner.this.refQueue);
         this.key = key;
      }

      void dispose() {
         AsciiStringInterner.this.cache.remove(this.key, this);
      }
   }

   private abstract static class BaseKey {
      private final int hash;

      BaseKey(int hash) {
         this.hash = hash;
      }

      public final int hashCode() {
         return this.hash;
      }

      public final boolean equals(@Nullable Object obj) {
         if (obj == this) {
            return true;
         } else if (!(obj instanceof AsciiStringInterner.BaseKey)) {
            return false;
         } else {
            AsciiStringInterner.BaseKey other = (AsciiStringInterner.BaseKey)obj;
            return this.equalsBytes(other);
         }
      }

      abstract boolean equalsBytes(AsciiStringInterner.BaseKey var1);

      abstract boolean equals(byte[] var1, int var2, int var3);

      abstract void appendString(StringBuilder var1);
   }
}
