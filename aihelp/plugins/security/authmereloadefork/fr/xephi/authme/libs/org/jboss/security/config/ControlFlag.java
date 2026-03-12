package fr.xephi.authme.libs.org.jboss.security.config;

import fr.xephi.authme.libs.org.jboss.security.PicketBoxMessages;

public class ControlFlag {
   private String flag;
   public static final ControlFlag REQUIRED = new ControlFlag("REQUIRED");
   public static final ControlFlag REQUISITE = new ControlFlag("REQUISITE");
   public static final ControlFlag SUFFICIENT = new ControlFlag("SUFFICIENT");
   public static final ControlFlag OPTIONAL = new ControlFlag("OPTIONAL");

   public ControlFlag(String flag) {
      this.flag = flag;
   }

   public String toString() {
      return this.flag;
   }

   public boolean equals(Object obj) {
      if (!(obj instanceof ControlFlag)) {
         return false;
      } else {
         ControlFlag objControlFlag = (ControlFlag)obj;
         return this.flag.equals(objControlFlag.flag);
      }
   }

   public int hashCode() {
      return this.flag.hashCode();
   }

   public static ControlFlag valueOf(String flag) {
      if ("REQUIRED".equalsIgnoreCase(flag)) {
         return REQUIRED;
      } else if ("REQUISITE".equalsIgnoreCase(flag)) {
         return REQUISITE;
      } else if ("SUFFICIENT".equalsIgnoreCase(flag)) {
         return SUFFICIENT;
      } else if ("OPTIONAL".equalsIgnoreCase(flag)) {
         return OPTIONAL;
      } else {
         throw PicketBoxMessages.MESSAGES.invalidControlFlag(flag);
      }
   }
}
