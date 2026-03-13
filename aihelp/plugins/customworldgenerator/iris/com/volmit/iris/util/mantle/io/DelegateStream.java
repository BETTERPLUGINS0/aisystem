package com.volmit.iris.util.mantle.io;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import org.jetbrains.annotations.NotNull;

public class DelegateStream {
   public static InputStream read(FileChannel channel) {
      var0.position(0L);
      return new DelegateStream.Input(var0);
   }

   public static OutputStream write(FileChannel channel) {
      var0.position(0L);
      return new DelegateStream.Output(var0);
   }

   private static class Input extends InputStream {
      private final InputStream delegate;

      private Input(FileChannel channel) {
         this.delegate = Channels.newInputStream(var1);
      }

      public int available() {
         return this.delegate.available();
      }

      public int read() {
         return this.delegate.read();
      }

      public int read(@NotNull byte[] b, int off, int len) {
         return this.delegate.read(var1, var2, var3);
      }

      @NotNull
      public byte[] readAllBytes() {
         return this.delegate.readAllBytes();
      }

      public int readNBytes(byte[] b, int off, int len) {
         return this.delegate.readNBytes(var1, var2, var3);
      }

      @NotNull
      public byte[] readNBytes(int len) {
         return this.delegate.readNBytes(var1);
      }

      public long skip(long n) {
         return this.delegate.skip(var1);
      }

      public void skipNBytes(long n) {
         this.delegate.skipNBytes(var1);
      }

      public long transferTo(OutputStream out) {
         return this.delegate.transferTo(var1);
      }
   }

   private static class Output extends OutputStream {
      private final FileChannel channel;
      private final OutputStream delegate;

      private Output(FileChannel channel) {
         this.channel = var1;
         this.delegate = Channels.newOutputStream(var1);
      }

      public void write(int b) {
         this.delegate.write(var1);
      }

      public void write(@NotNull byte[] b, int off, int len) {
         this.delegate.write(var1, var2, var3);
      }

      public void flush() {
         this.channel.truncate(this.channel.position());
      }

      public void close() {
         this.channel.force(true);
      }
   }
}
