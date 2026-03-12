package fr.xephi.authme.libs.com.google.common.io;

import fr.xephi.authme.libs.com.google.common.annotations.Beta;
import fr.xephi.authme.libs.com.google.common.annotations.GwtIncompatible;
import fr.xephi.authme.libs.com.google.common.base.Preconditions;
import fr.xephi.authme.libs.com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.stream.Stream;

@ElementTypesAreNonnullByDefault
@GwtIncompatible
public abstract class CharSink {
   protected CharSink() {
   }

   public abstract Writer openStream() throws IOException;

   public Writer openBufferedStream() throws IOException {
      Writer writer = this.openStream();
      return writer instanceof BufferedWriter ? (BufferedWriter)writer : new BufferedWriter(writer);
   }

   public void write(CharSequence charSequence) throws IOException {
      Preconditions.checkNotNull(charSequence);
      Closer closer = Closer.create();

      try {
         Writer out = (Writer)closer.register(this.openStream());
         out.append(charSequence);
         out.flush();
      } catch (Throwable var7) {
         throw closer.rethrow(var7);
      } finally {
         closer.close();
      }

   }

   public void writeLines(Iterable<? extends CharSequence> lines) throws IOException {
      this.writeLines(lines, System.getProperty("line.separator"));
   }

   public void writeLines(Iterable<? extends CharSequence> lines, String lineSeparator) throws IOException {
      this.writeLines(lines.iterator(), lineSeparator);
   }

   @Beta
   public void writeLines(Stream<? extends CharSequence> lines) throws IOException {
      this.writeLines(lines, System.getProperty("line.separator"));
   }

   @Beta
   public void writeLines(Stream<? extends CharSequence> lines, String lineSeparator) throws IOException {
      this.writeLines(lines.iterator(), lineSeparator);
   }

   private void writeLines(Iterator<? extends CharSequence> lines, String lineSeparator) throws IOException {
      Preconditions.checkNotNull(lineSeparator);
      Writer out = this.openBufferedStream();

      try {
         while(lines.hasNext()) {
            out.append((CharSequence)lines.next()).append(lineSeparator);
         }
      } catch (Throwable var7) {
         if (out != null) {
            try {
               out.close();
            } catch (Throwable var6) {
               var7.addSuppressed(var6);
            }
         }

         throw var7;
      }

      if (out != null) {
         out.close();
      }

   }

   @CanIgnoreReturnValue
   public long writeFrom(Readable readable) throws IOException {
      Preconditions.checkNotNull(readable);
      Closer closer = Closer.create();

      long var6;
      try {
         Writer out = (Writer)closer.register(this.openStream());
         long written = CharStreams.copy(readable, out);
         out.flush();
         var6 = written;
      } catch (Throwable var11) {
         throw closer.rethrow(var11);
      } finally {
         closer.close();
      }

      return var6;
   }
}
