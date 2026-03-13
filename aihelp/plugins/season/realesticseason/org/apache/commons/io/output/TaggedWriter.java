package org.apache.commons.io.output;

import java.io.IOException;
import java.io.Serializable;
import java.io.Writer;
import java.util.UUID;
import org.apache.commons.io.TaggedIOException;

public class TaggedWriter extends ProxyWriter {
   private final Serializable tag = UUID.randomUUID();

   public TaggedWriter(Writer var1) {
      super(var1);
   }

   public boolean isCauseOf(Exception var1) {
      return TaggedIOException.isTaggedWith(var1, this.tag);
   }

   public void throwIfCauseOf(Exception var1) {
      TaggedIOException.throwCauseIfTaggedWith(var1, this.tag);
   }

   protected void handleIOException(IOException var1) {
      throw new TaggedIOException(var1, this.tag);
   }
}
