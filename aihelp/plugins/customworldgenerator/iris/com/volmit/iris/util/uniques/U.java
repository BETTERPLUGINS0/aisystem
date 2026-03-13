package com.volmit.iris.util.uniques;

import java.io.File;

public class U {
   public static void main(String[] a) {
      UniqueRenderer var1 = new UniqueRenderer("helloworld", 2560, 1440);
      var1.writeCollectionFrames(new File("collection"), 1, 1024);
      System.exit(0);
   }
}
