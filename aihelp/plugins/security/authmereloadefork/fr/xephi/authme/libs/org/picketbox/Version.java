package fr.xephi.authme.libs.org.picketbox;

public final class Version {
   public static final String VERSION = getVersionString();

   private Version() {
   }

   static String getVersionString() {
      return "4.0.21.Final";
   }

   public static void main(String[] args) {
      System.out.print(VERSION);
   }
}
