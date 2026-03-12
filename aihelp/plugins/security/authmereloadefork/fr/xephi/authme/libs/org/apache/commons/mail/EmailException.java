package fr.xephi.authme.libs.org.apache.commons.mail;

import java.io.PrintStream;
import java.io.PrintWriter;

public class EmailException extends Exception {
   private static final long serialVersionUID = 5550674499282474616L;

   public EmailException() {
   }

   public EmailException(String msg) {
      super(msg);
   }

   public EmailException(Throwable rootCause) {
      super(rootCause);
   }

   public EmailException(String msg, Throwable rootCause) {
      super(msg, rootCause);
   }

   public void printStackTrace() {
      this.printStackTrace(System.err);
   }

   public void printStackTrace(PrintStream out) {
      synchronized(out) {
         PrintWriter pw = new PrintWriter(out, false);
         this.printStackTrace(pw);
         pw.flush();
      }
   }

   public void printStackTrace(PrintWriter out) {
      synchronized(out) {
         super.printStackTrace(out);
      }
   }
}
