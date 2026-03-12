package fr.xephi.authme.libs.com.google.common.hash;

import fr.xephi.authme.libs.com.google.common.annotations.Beta;
import fr.xephi.authme.libs.com.google.common.base.Preconditions;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.Iterator;
import javax.annotation.CheckForNull;

@ElementTypesAreNonnullByDefault
@Beta
public final class Funnels {
   private Funnels() {
   }

   public static Funnel<byte[]> byteArrayFunnel() {
      return Funnels.ByteArrayFunnel.INSTANCE;
   }

   public static Funnel<CharSequence> unencodedCharsFunnel() {
      return Funnels.UnencodedCharsFunnel.INSTANCE;
   }

   public static Funnel<CharSequence> stringFunnel(Charset charset) {
      return new Funnels.StringCharsetFunnel(charset);
   }

   public static Funnel<Integer> integerFunnel() {
      return Funnels.IntegerFunnel.INSTANCE;
   }

   public static <E> Funnel<Iterable<? extends E>> sequentialFunnel(Funnel<E> elementFunnel) {
      return new Funnels.SequentialFunnel(elementFunnel);
   }

   public static Funnel<Long> longFunnel() {
      return Funnels.LongFunnel.INSTANCE;
   }

   public static OutputStream asOutputStream(PrimitiveSink sink) {
      return new Funnels.SinkAsStream(sink);
   }

   private static class SinkAsStream extends OutputStream {
      final PrimitiveSink sink;

      SinkAsStream(PrimitiveSink sink) {
         this.sink = (PrimitiveSink)Preconditions.checkNotNull(sink);
      }

      public void write(int b) {
         this.sink.putByte((byte)b);
      }

      public void write(byte[] bytes) {
         this.sink.putBytes(bytes);
      }

      public void write(byte[] bytes, int off, int len) {
         this.sink.putBytes(bytes, off, len);
      }

      public String toString() {
         String var1 = String.valueOf(this.sink);
         return (new StringBuilder(24 + String.valueOf(var1).length())).append("Funnels.asOutputStream(").append(var1).append(")").toString();
      }
   }

   private static enum LongFunnel implements Funnel<Long> {
      INSTANCE;

      public void funnel(Long from, PrimitiveSink into) {
         into.putLong(from);
      }

      public String toString() {
         return "Funnels.longFunnel()";
      }

      // $FF: synthetic method
      private static Funnels.LongFunnel[] $values() {
         return new Funnels.LongFunnel[]{INSTANCE};
      }
   }

   private static class SequentialFunnel<E> implements Funnel<Iterable<? extends E>>, Serializable {
      private final Funnel<E> elementFunnel;

      SequentialFunnel(Funnel<E> elementFunnel) {
         this.elementFunnel = (Funnel)Preconditions.checkNotNull(elementFunnel);
      }

      public void funnel(Iterable<? extends E> from, PrimitiveSink into) {
         Iterator var3 = from.iterator();

         while(var3.hasNext()) {
            E e = var3.next();
            this.elementFunnel.funnel(e, into);
         }

      }

      public String toString() {
         String var1 = String.valueOf(this.elementFunnel);
         return (new StringBuilder(26 + String.valueOf(var1).length())).append("Funnels.sequentialFunnel(").append(var1).append(")").toString();
      }

      public boolean equals(@CheckForNull Object o) {
         if (o instanceof Funnels.SequentialFunnel) {
            Funnels.SequentialFunnel<?> funnel = (Funnels.SequentialFunnel)o;
            return this.elementFunnel.equals(funnel.elementFunnel);
         } else {
            return false;
         }
      }

      public int hashCode() {
         return Funnels.SequentialFunnel.class.hashCode() ^ this.elementFunnel.hashCode();
      }
   }

   private static enum IntegerFunnel implements Funnel<Integer> {
      INSTANCE;

      public void funnel(Integer from, PrimitiveSink into) {
         into.putInt(from);
      }

      public String toString() {
         return "Funnels.integerFunnel()";
      }

      // $FF: synthetic method
      private static Funnels.IntegerFunnel[] $values() {
         return new Funnels.IntegerFunnel[]{INSTANCE};
      }
   }

   private static class StringCharsetFunnel implements Funnel<CharSequence>, Serializable {
      private final Charset charset;

      StringCharsetFunnel(Charset charset) {
         this.charset = (Charset)Preconditions.checkNotNull(charset);
      }

      public void funnel(CharSequence from, PrimitiveSink into) {
         into.putString(from, this.charset);
      }

      public String toString() {
         String var1 = this.charset.name();
         return (new StringBuilder(22 + String.valueOf(var1).length())).append("Funnels.stringFunnel(").append(var1).append(")").toString();
      }

      public boolean equals(@CheckForNull Object o) {
         if (o instanceof Funnels.StringCharsetFunnel) {
            Funnels.StringCharsetFunnel funnel = (Funnels.StringCharsetFunnel)o;
            return this.charset.equals(funnel.charset);
         } else {
            return false;
         }
      }

      public int hashCode() {
         return Funnels.StringCharsetFunnel.class.hashCode() ^ this.charset.hashCode();
      }

      Object writeReplace() {
         return new Funnels.StringCharsetFunnel.SerializedForm(this.charset);
      }

      private static class SerializedForm implements Serializable {
         private final String charsetCanonicalName;
         private static final long serialVersionUID = 0L;

         SerializedForm(Charset charset) {
            this.charsetCanonicalName = charset.name();
         }

         private Object readResolve() {
            return Funnels.stringFunnel(Charset.forName(this.charsetCanonicalName));
         }
      }
   }

   private static enum UnencodedCharsFunnel implements Funnel<CharSequence> {
      INSTANCE;

      public void funnel(CharSequence from, PrimitiveSink into) {
         into.putUnencodedChars(from);
      }

      public String toString() {
         return "Funnels.unencodedCharsFunnel()";
      }

      // $FF: synthetic method
      private static Funnels.UnencodedCharsFunnel[] $values() {
         return new Funnels.UnencodedCharsFunnel[]{INSTANCE};
      }
   }

   private static enum ByteArrayFunnel implements Funnel<byte[]> {
      INSTANCE;

      public void funnel(byte[] from, PrimitiveSink into) {
         into.putBytes(from);
      }

      public String toString() {
         return "Funnels.byteArrayFunnel()";
      }

      // $FF: synthetic method
      private static Funnels.ByteArrayFunnel[] $values() {
         return new Funnels.ByteArrayFunnel[]{INSTANCE};
      }
   }
}
