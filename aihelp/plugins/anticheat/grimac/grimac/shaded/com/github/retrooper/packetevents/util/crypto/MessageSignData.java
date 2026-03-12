package ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.crypto;

import java.time.Instant;

public class MessageSignData {
   private final SaltSignature saltSignature;
   private final Instant timestamp;
   private boolean signedPreview;

   public MessageSignData(SaltSignature saltSignature, Instant timestamp) {
      this.saltSignature = saltSignature;
      this.timestamp = timestamp;
   }

   public MessageSignData(SaltSignature saltSignature, Instant timestamp, boolean signedPreview) {
      this.saltSignature = saltSignature;
      this.timestamp = timestamp;
      this.signedPreview = signedPreview;
   }

   public SaltSignature getSaltSignature() {
      return this.saltSignature;
   }

   public Instant getTimestamp() {
      return this.timestamp;
   }

   public boolean isSignedPreview() {
      return this.signedPreview;
   }

   public void setSignedPreview(boolean signedPreview) {
      this.signedPreview = signedPreview;
   }
}
