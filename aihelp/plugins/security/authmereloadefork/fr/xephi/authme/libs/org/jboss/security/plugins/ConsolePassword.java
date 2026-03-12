package fr.xephi.authme.libs.org.jboss.security.plugins;

import fr.xephi.authme.libs.org.jboss.security.PicketBoxMessages;
import java.io.CharArrayWriter;
import java.io.IOException;

public class ConsolePassword {
   public char[] toCharArray() throws IOException {
      System.err.print(PicketBoxMessages.MESSAGES.enterPasswordMessage());
      CharArrayWriter writer = new CharArrayWriter();

      int b;
      while((b = System.in.read()) >= 0 && b != 13 && b != 10) {
         writer.write(b);
      }

      char[] password = writer.toCharArray();
      writer.reset();

      for(int n = 0; n < password.length; ++n) {
         writer.write(0);
      }

      return password;
   }

   public static void main(String[] args) throws IOException {
      ConsolePassword cp = new ConsolePassword();
      System.out.println(new String(cp.toCharArray()));
   }
}
