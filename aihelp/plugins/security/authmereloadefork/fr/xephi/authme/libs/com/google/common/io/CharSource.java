package fr.xephi.authme.libs.com.google.common.io;

import fr.xephi.authme.libs.com.google.common.annotations.Beta;
import fr.xephi.authme.libs.com.google.common.annotations.GwtIncompatible;
import fr.xephi.authme.libs.com.google.common.base.Ascii;
import fr.xephi.authme.libs.com.google.common.base.Optional;
import fr.xephi.authme.libs.com.google.common.base.Preconditions;
import fr.xephi.authme.libs.com.google.common.base.Splitter;
import fr.xephi.authme.libs.com.google.common.collect.AbstractIterator;
import fr.xephi.authme.libs.com.google.common.collect.ImmutableList;
import fr.xephi.authme.libs.com.google.common.collect.Lists;
import fr.xephi.authme.libs.com.google.common.collect.Streams;
import fr.xephi.authme.libs.com.google.errorprone.annotations.CanIgnoreReturnValue;
import fr.xephi.authme.libs.com.google.errorprone.annotations.MustBeClosed;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.UncheckedIOException;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.function.Consumer;
import java.util.stream.Stream;
import javax.annotation.CheckForNull;

@ElementTypesAreNonnullByDefault
@GwtIncompatible
public abstract class CharSource {
   protected CharSource() {
   }

   @Beta
   public ByteSource asByteSource(Charset charset) {
      return new CharSource.AsByteSource(charset);
   }

   public abstract Reader openStream() throws IOException;

   public BufferedReader openBufferedStream() throws IOException {
      Reader reader = this.openStream();
      return reader instanceof BufferedReader ? (BufferedReader)reader : new BufferedReader(reader);
   }

   @Beta
   @MustBeClosed
   public Stream<String> lines() throws IOException {
      BufferedReader reader = this.openBufferedStream();
      return (Stream)reader.lines().onClose(() -> {
         try {
            reader.close();
         } catch (IOException var2) {
            throw new UncheckedIOException(var2);
         }
      });
   }

   @Beta
   public Optional<Long> lengthIfKnown() {
      return Optional.absent();
   }

   @Beta
   public long length() throws IOException {
      Optional<Long> lengthIfKnown = this.lengthIfKnown();
      if (lengthIfKnown.isPresent()) {
         return (Long)lengthIfKnown.get();
      } else {
         Closer closer = Closer.create();

         long var4;
         try {
            Reader reader = (Reader)closer.register(this.openStream());
            var4 = this.countBySkipping(reader);
         } catch (Throwable var9) {
            throw closer.rethrow(var9);
         } finally {
            closer.close();
         }

         return var4;
      }
   }

   private long countBySkipping(Reader reader) throws IOException {
      long count;
      long read;
      for(count = 0L; (read = reader.skip(Long.MAX_VALUE)) != 0L; count += read) {
      }

      return count;
   }

   @CanIgnoreReturnValue
   public long copyTo(Appendable appendable) throws IOException {
      Preconditions.checkNotNull(appendable);
      Closer closer = Closer.create();

      long var4;
      try {
         Reader reader = (Reader)closer.register(this.openStream());
         var4 = CharStreams.copy(reader, appendable);
      } catch (Throwable var9) {
         throw closer.rethrow(var9);
      } finally {
         closer.close();
      }

      return var4;
   }

   @CanIgnoreReturnValue
   public long copyTo(CharSink sink) throws IOException {
      Preconditions.checkNotNull(sink);
      Closer closer = Closer.create();

      long var5;
      try {
         Reader reader = (Reader)closer.register(this.openStream());
         Writer writer = (Writer)closer.register(sink.openStream());
         var5 = CharStreams.copy(reader, writer);
      } catch (Throwable var10) {
         throw closer.rethrow(var10);
      } finally {
         closer.close();
      }

      return var5;
   }

   public String read() throws IOException {
      Closer closer = Closer.create();

      String var3;
      try {
         Reader reader = (Reader)closer.register(this.openStream());
         var3 = CharStreams.toString(reader);
      } catch (Throwable var7) {
         throw closer.rethrow(var7);
      } finally {
         closer.close();
      }

      return var3;
   }

   @CheckForNull
   public String readFirstLine() throws IOException {
      Closer closer = Closer.create();

      String var3;
      try {
         BufferedReader reader = (BufferedReader)closer.register(this.openBufferedStream());
         var3 = reader.readLine();
      } catch (Throwable var7) {
         throw closer.rethrow(var7);
      } finally {
         closer.close();
      }

      return var3;
   }

   public ImmutableList<String> readLines() throws IOException {
      Closer closer = Closer.create();

      try {
         BufferedReader reader = (BufferedReader)closer.register(this.openBufferedStream());
         ArrayList result = Lists.newArrayList();

         String line;
         while((line = reader.readLine()) != null) {
            result.add(line);
         }

         ImmutableList var5 = ImmutableList.copyOf((Collection)result);
         return var5;
      } catch (Throwable var9) {
         throw closer.rethrow(var9);
      } finally {
         closer.close();
      }
   }

   @ParametricNullness
   @Beta
   @CanIgnoreReturnValue
   public <T> T readLines(LineProcessor<T> processor) throws IOException {
      Preconditions.checkNotNull(processor);
      Closer closer = Closer.create();

      Object var4;
      try {
         Reader reader = (Reader)closer.register(this.openStream());
         var4 = CharStreams.readLines(reader, processor);
      } catch (Throwable var8) {
         throw closer.rethrow(var8);
      } finally {
         closer.close();
      }

      return var4;
   }

   @Beta
   public void forEachLine(Consumer<? super String> action) throws IOException {
      try {
         Stream lines = this.lines();

         try {
            lines.forEachOrdered(action);
         } catch (Throwable var6) {
            if (lines != null) {
               try {
                  lines.close();
               } catch (Throwable var5) {
                  var6.addSuppressed(var5);
               }
            }

            throw var6;
         }

         if (lines != null) {
            lines.close();
         }

      } catch (UncheckedIOException var7) {
         throw var7.getCause();
      }
   }

   public boolean isEmpty() throws IOException {
      Optional<Long> lengthIfKnown = this.lengthIfKnown();
      if (lengthIfKnown.isPresent()) {
         return (Long)lengthIfKnown.get() == 0L;
      } else {
         Closer closer = Closer.create();

         boolean var4;
         try {
            Reader reader = (Reader)closer.register(this.openStream());
            var4 = reader.read() == -1;
         } catch (Throwable var8) {
            throw closer.rethrow(var8);
         } finally {
            closer.close();
         }

         return var4;
      }
   }

   public static CharSource concat(Iterable<? extends CharSource> sources) {
      return new CharSource.ConcatenatedCharSource(sources);
   }

   public static CharSource concat(Iterator<? extends CharSource> sources) {
      return concat((Iterable)ImmutableList.copyOf(sources));
   }

   public static CharSource concat(CharSource... sources) {
      return concat((Iterable)ImmutableList.copyOf((Object[])sources));
   }

   public static CharSource wrap(CharSequence charSequence) {
      return (CharSource)(charSequence instanceof String ? new CharSource.StringCharSource((String)charSequence) : new CharSource.CharSequenceCharSource(charSequence));
   }

   public static CharSource empty() {
      return CharSource.EmptyCharSource.INSTANCE;
   }

   private static final class ConcatenatedCharSource extends CharSource {
      private final Iterable<? extends CharSource> sources;

      ConcatenatedCharSource(Iterable<? extends CharSource> sources) {
         this.sources = (Iterable)Preconditions.checkNotNull(sources);
      }

      public Reader openStream() throws IOException {
         return new MultiReader(this.sources.iterator());
      }

      public boolean isEmpty() throws IOException {
         Iterator var1 = this.sources.iterator();

         CharSource source;
         do {
            if (!var1.hasNext()) {
               return true;
            }

            source = (CharSource)var1.next();
         } while(source.isEmpty());

         return false;
      }

      public Optional<Long> lengthIfKnown() {
         long result = 0L;

         Optional lengthIfKnown;
         for(Iterator var3 = this.sources.iterator(); var3.hasNext(); result += (Long)lengthIfKnown.get()) {
            CharSource source = (CharSource)var3.next();
            lengthIfKnown = source.lengthIfKnown();
            if (!lengthIfKnown.isPresent()) {
               return Optional.absent();
            }
         }

         return Optional.of(result);
      }

      public long length() throws IOException {
         long result = 0L;

         CharSource source;
         for(Iterator var3 = this.sources.iterator(); var3.hasNext(); result += source.length()) {
            source = (CharSource)var3.next();
         }

         return result;
      }

      public String toString() {
         String var1 = String.valueOf(this.sources);
         return (new StringBuilder(19 + String.valueOf(var1).length())).append("CharSource.concat(").append(var1).append(")").toString();
      }
   }

   private static final class EmptyCharSource extends CharSource.StringCharSource {
      private static final CharSource.EmptyCharSource INSTANCE = new CharSource.EmptyCharSource();

      private EmptyCharSource() {
         super("");
      }

      public String toString() {
         return "CharSource.empty()";
      }
   }

   private static class StringCharSource extends CharSource.CharSequenceCharSource {
      protected StringCharSource(String seq) {
         super(seq);
      }

      public Reader openStream() {
         return new StringReader((String)this.seq);
      }

      public long copyTo(Appendable appendable) throws IOException {
         appendable.append(this.seq);
         return (long)this.seq.length();
      }

      public long copyTo(CharSink sink) throws IOException {
         Preconditions.checkNotNull(sink);
         Closer closer = Closer.create();

         long var4;
         try {
            Writer writer = (Writer)closer.register(sink.openStream());
            writer.write((String)this.seq);
            var4 = (long)this.seq.length();
         } catch (Throwable var9) {
            throw closer.rethrow(var9);
         } finally {
            closer.close();
         }

         return var4;
      }
   }

   private static class CharSequenceCharSource extends CharSource {
      private static final Splitter LINE_SPLITTER = Splitter.onPattern("\r\n|\n|\r");
      protected final CharSequence seq;

      protected CharSequenceCharSource(CharSequence seq) {
         this.seq = (CharSequence)Preconditions.checkNotNull(seq);
      }

      public Reader openStream() {
         return new CharSequenceReader(this.seq);
      }

      public String read() {
         return this.seq.toString();
      }

      public boolean isEmpty() {
         return this.seq.length() == 0;
      }

      public long length() {
         return (long)this.seq.length();
      }

      public Optional<Long> lengthIfKnown() {
         return Optional.of((long)this.seq.length());
      }

      private Iterator<String> linesIterator() {
         return new AbstractIterator<String>() {
            Iterator<String> lines;

            {
               this.lines = CharSource.CharSequenceCharSource.LINE_SPLITTER.split(CharSequenceCharSource.this.seq).iterator();
            }

            @CheckForNull
            protected String computeNext() {
               if (this.lines.hasNext()) {
                  String next = (String)this.lines.next();
                  if (this.lines.hasNext() || !next.isEmpty()) {
                     return next;
                  }
               }

               return (String)this.endOfData();
            }
         };
      }

      public Stream<String> lines() {
         return Streams.stream(this.linesIterator());
      }

      @CheckForNull
      public String readFirstLine() {
         Iterator<String> lines = this.linesIterator();
         return lines.hasNext() ? (String)lines.next() : null;
      }

      public ImmutableList<String> readLines() {
         return ImmutableList.copyOf(this.linesIterator());
      }

      @ParametricNullness
      public <T> T readLines(LineProcessor<T> processor) throws IOException {
         Iterator lines = this.linesIterator();

         while(lines.hasNext() && processor.processLine((String)lines.next())) {
         }

         return processor.getResult();
      }

      public String toString() {
         String var1 = Ascii.truncate(this.seq, 30, "...");
         return (new StringBuilder(17 + String.valueOf(var1).length())).append("CharSource.wrap(").append(var1).append(")").toString();
      }
   }

   private final class AsByteSource extends ByteSource {
      final Charset charset;

      AsByteSource(Charset charset) {
         this.charset = (Charset)Preconditions.checkNotNull(charset);
      }

      public CharSource asCharSource(Charset charset) {
         return charset.equals(this.charset) ? CharSource.this : super.asCharSource(charset);
      }

      public InputStream openStream() throws IOException {
         return new ReaderInputStream(CharSource.this.openStream(), this.charset, 8192);
      }

      public String toString() {
         String var1 = CharSource.this.toString();
         String var2 = String.valueOf(this.charset);
         return (new StringBuilder(15 + String.valueOf(var1).length() + String.valueOf(var2).length())).append(var1).append(".asByteSource(").append(var2).append(")").toString();
      }
   }
}
