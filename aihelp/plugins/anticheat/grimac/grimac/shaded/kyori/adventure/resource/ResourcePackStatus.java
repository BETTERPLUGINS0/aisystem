package ac.grim.grimac.shaded.kyori.adventure.resource;

public enum ResourcePackStatus {
   ACCEPTED(true),
   DECLINED(false),
   INVALID_URL(false),
   FAILED_DOWNLOAD(false),
   DOWNLOADED(true),
   FAILED_RELOAD(false),
   DISCARDED(false),
   SUCCESSFULLY_LOADED(false);

   private final boolean intermediate;

   private ResourcePackStatus(final boolean intermediate) {
      this.intermediate = intermediate;
   }

   public boolean intermediate() {
      return this.intermediate;
   }

   // $FF: synthetic method
   private static ResourcePackStatus[] $values() {
      return new ResourcePackStatus[]{ACCEPTED, DECLINED, INVALID_URL, FAILED_DOWNLOAD, DOWNLOADED, FAILED_RELOAD, DISCARDED, SUCCESSFULLY_LOADED};
   }
}
