package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.util;

import org.jspecify.annotations.NullMarked;

@NullMarked
public class NbtCodecException extends RuntimeException {
   private static final boolean DEBUG_TRACES = Boolean.getBoolean("packetevents.debug.nbt-codec-trace");

   public NbtCodecException(String message) {
      super(message);
   }

   public NbtCodecException(String message, Throwable cause) {
      super(message, cause);
   }

   public NbtCodecException(Throwable cause) {
      super(cause);
   }

   public synchronized Throwable fillInStackTrace() {
      return (Throwable)(DEBUG_TRACES ? super.fillInStackTrace() : this);
   }
}
