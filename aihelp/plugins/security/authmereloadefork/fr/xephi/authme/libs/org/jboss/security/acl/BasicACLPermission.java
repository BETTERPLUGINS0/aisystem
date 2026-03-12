package fr.xephi.authme.libs.org.jboss.security.acl;

public enum BasicACLPermission implements BitMaskPermission {
   CREATE(1),
   READ(2),
   UPDATE(4),
   DELETE(8);

   private int mask;

   private BasicACLPermission(int mask) {
      this.mask = mask;
   }

   public int getMaskValue() {
      return this.mask;
   }

   public String toBinaryString() {
      return Integer.toBinaryString(this.mask);
   }
}
