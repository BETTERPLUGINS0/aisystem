package fr.xephi.authme.libs.org.postgresql.shaded.com.ongres.scram.common.util;

public abstract class AbstractStringWritable implements StringWritable {
   public String toString() {
      return this.writeTo(new StringBuffer()).toString();
   }
}
