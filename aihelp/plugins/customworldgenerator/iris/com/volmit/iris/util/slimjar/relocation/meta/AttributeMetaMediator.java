package com.volmit.iris.util.slimjar.relocation.meta;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.UserDefinedFileAttributeView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class AttributeMetaMediator implements MetaMediator {
   @NotNull
   private final UserDefinedFileAttributeView view;

   public AttributeMetaMediator(@NotNull Path var1) {
      this.view = (UserDefinedFileAttributeView)Files.getFileAttributeView(var1, UserDefinedFileAttributeView.class);
   }

   @Nullable
   public String readAttribute(@NotNull String var1) {
      try {
         ByteBuffer var2 = ByteBuffer.allocate(this.view.size(var1));
         this.view.read(var1, var2);
         var2.flip();
         return Charset.defaultCharset().decode(var2).toString();
      } catch (IOException var3) {
         return null;
      }
   }

   public void writeAttribute(@NotNull String var1, @NotNull String var2) {
      try {
         this.view.write(var1, Charset.defaultCharset().encode(var2));
      } catch (IOException var4) {
      }

   }
}
