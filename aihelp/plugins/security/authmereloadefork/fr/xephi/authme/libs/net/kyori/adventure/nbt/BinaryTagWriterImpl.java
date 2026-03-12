package fr.xephi.authme.libs.net.kyori.adventure.nbt;

import java.io.BufferedOutputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map.Entry;
import org.jetbrains.annotations.NotNull;

final class BinaryTagWriterImpl implements BinaryTagIO.Writer {
   static final BinaryTagIO.Writer INSTANCE = new BinaryTagWriterImpl();

   public void write(@NotNull final CompoundBinaryTag tag, @NotNull final Path path, @NotNull final BinaryTagIO.Compression compression) throws IOException {
      OutputStream os = Files.newOutputStream(path);

      try {
         this.write(tag, os, compression);
      } catch (Throwable var8) {
         if (os != null) {
            try {
               os.close();
            } catch (Throwable var7) {
               var8.addSuppressed(var7);
            }
         }

         throw var8;
      }

      if (os != null) {
         os.close();
      }

   }

   public void write(@NotNull final CompoundBinaryTag tag, @NotNull final OutputStream output, @NotNull final BinaryTagIO.Compression compression) throws IOException {
      DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(compression.compress(IOStreamUtil.closeShield(output))));

      try {
         this.write(tag, dos);
      } catch (Throwable var8) {
         try {
            dos.close();
         } catch (Throwable var7) {
            var8.addSuppressed(var7);
         }

         throw var8;
      }

      dos.close();
   }

   public void write(@NotNull final CompoundBinaryTag tag, @NotNull final DataOutput output) throws IOException {
      output.writeByte(BinaryTagTypes.COMPOUND.id());
      output.writeUTF("");
      BinaryTagTypes.COMPOUND.write(tag, output);
   }

   public void writeNamed(@NotNull final Entry<String, CompoundBinaryTag> tag, @NotNull final Path path, @NotNull final BinaryTagIO.Compression compression) throws IOException {
      OutputStream os = Files.newOutputStream(path);

      try {
         this.writeNamed(tag, os, compression);
      } catch (Throwable var8) {
         if (os != null) {
            try {
               os.close();
            } catch (Throwable var7) {
               var8.addSuppressed(var7);
            }
         }

         throw var8;
      }

      if (os != null) {
         os.close();
      }

   }

   public void writeNamed(@NotNull final Entry<String, CompoundBinaryTag> tag, @NotNull final OutputStream output, @NotNull final BinaryTagIO.Compression compression) throws IOException {
      DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(compression.compress(IOStreamUtil.closeShield(output))));

      try {
         this.writeNamed(tag, dos);
      } catch (Throwable var8) {
         try {
            dos.close();
         } catch (Throwable var7) {
            var8.addSuppressed(var7);
         }

         throw var8;
      }

      dos.close();
   }

   public void writeNamed(@NotNull final Entry<String, CompoundBinaryTag> tag, @NotNull final DataOutput output) throws IOException {
      output.writeByte(BinaryTagTypes.COMPOUND.id());
      output.writeUTF((String)tag.getKey());
      BinaryTagTypes.COMPOUND.write((CompoundBinaryTag)tag.getValue(), output);
   }
}
