package ac.grim.grimac.shaded.com.github.retrooper.packetevents.util;

public class KnownPack {
   private final String namespace;
   private final String id;
   private final String version;

   public KnownPack(String namespace, String id, String version) {
      this.namespace = namespace;
      this.id = id;
      this.version = version;
   }

   public String getNamespace() {
      return this.namespace;
   }

   public String getId() {
      return this.id;
   }

   public String getVersion() {
      return this.version;
   }

   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      } else if (!(obj instanceof KnownPack)) {
         return false;
      } else {
         KnownPack knownPack = (KnownPack)obj;
         if (!this.namespace.equals(knownPack.namespace)) {
            return false;
         } else {
            return !this.id.equals(knownPack.id) ? false : this.version.equals(knownPack.version);
         }
      }
   }

   public int hashCode() {
      int result = this.namespace.hashCode();
      result = 31 * result + this.id.hashCode();
      result = 31 * result + this.version.hashCode();
      return result;
   }

   public String toString() {
      return "KnownPack{namespace='" + this.namespace + '\'' + ", id='" + this.id + '\'' + ", version='" + this.version + '\'' + '}';
   }
}
