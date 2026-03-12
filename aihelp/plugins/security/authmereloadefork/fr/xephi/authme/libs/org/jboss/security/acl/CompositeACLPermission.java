package fr.xephi.authme.libs.org.jboss.security.acl;

public class CompositeACLPermission implements BitMaskPermission {
   private int mask = 0;

   public CompositeACLPermission(int mask) {
      this.mask = mask;
   }

   public CompositeACLPermission(BasicACLPermission... permissions) {
      BasicACLPermission[] arr$ = permissions;
      int len$ = permissions.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         BasicACLPermission basicPermission = arr$[i$];
         this.mask |= basicPermission.getMaskValue();
      }

   }

   public int getMaskValue() {
      return this.mask;
   }

   public boolean equals(Object obj) {
      if (obj instanceof BitMaskPermission) {
         return this.mask == ((BitMaskPermission)obj).getMaskValue();
      } else {
         return false;
      }
   }

   public int hashCode() {
      return this.mask;
   }

   public String toString() {
      if (this.mask == 0) {
         return "NO PERMISSION";
      } else {
         StringBuffer buffer = new StringBuffer();
         BasicACLPermission[] arr$ = BasicACLPermission.values();
         int len$ = arr$.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            BasicACLPermission permission = arr$[i$];
            if ((permission.getMaskValue() & this.mask) != 0) {
               buffer.append(permission.toString() + ",");
            }
         }

         return buffer.substring(0, buffer.lastIndexOf(","));
      }
   }

   public String toBinaryString() {
      return Integer.toBinaryString(this.mask);
   }
}
