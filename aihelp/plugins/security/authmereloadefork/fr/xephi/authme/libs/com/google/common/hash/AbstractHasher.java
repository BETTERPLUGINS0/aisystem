package fr.xephi.authme.libs.com.google.common.hash;

import fr.xephi.authme.libs.com.google.common.base.Preconditions;
import fr.xephi.authme.libs.com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

@ElementTypesAreNonnullByDefault
@CanIgnoreReturnValue
abstract class AbstractHasher implements Hasher {
   public final Hasher putBoolean(boolean b) {
      return this.putByte((byte)(b ? 1 : 0));
   }

   public final Hasher putDouble(double d) {
      return this.putLong(Double.doubleToRawLongBits(d));
   }

   public final Hasher putFloat(float f) {
      return this.putInt(Float.floatToRawIntBits(f));
   }

   public Hasher putUnencodedChars(CharSequence charSequence) {
      int i = 0;

      for(int len = charSequence.length(); i < len; ++i) {
         this.putChar(charSequence.charAt(i));
      }

      return this;
   }

   public Hasher putString(CharSequence charSequence, Charset charset) {
      return this.putBytes(charSequence.toString().getBytes(charset));
   }

   public Hasher putBytes(byte[] bytes) {
      return this.putBytes(bytes, 0, bytes.length);
   }

   public Hasher putBytes(byte[] bytes, int off, int len) {
      Preconditions.checkPositionIndexes(off, off + len, bytes.length);

      for(int i = 0; i < len; ++i) {
         this.putByte(bytes[off + i]);
      }

      return this;
   }

   public Hasher putBytes(ByteBuffer b) {
      if (b.hasArray()) {
         this.putBytes(b.array(), b.arrayOffset() + b.position(), b.remaining());
         Java8Compatibility.position(b, b.limit());
      } else {
         for(int remaining = b.remaining(); remaining > 0; --remaining) {
            this.putByte(b.get());
         }
      }

      return this;
   }

   public Hasher putShort(short s) {
      this.putByte((byte)s);
      this.putByte((byte)(s >>> 8));
      return this;
   }

   public Hasher putInt(int i) {
      this.putByte((byte)i);
      this.putByte((byte)(i >>> 8));
      this.putByte((byte)(i >>> 16));
      this.putByte((byte)(i >>> 24));
      return this;
   }

   public Hasher putLong(long l) {
      for(int i = 0; i < 64; i += 8) {
         this.putByte((byte)((int)(l >>> i)));
      }

      return this;
   }

   public Hasher putChar(char c) {
      this.putByte((byte)c);
      this.putByte((byte)(c >>> 8));
      return this;
   }

   public <T> Hasher putObject(@ParametricNullness T instance, Funnel<? super T> funnel) {
      funnel.funnel(instance, this);
      return this;
   }
}
