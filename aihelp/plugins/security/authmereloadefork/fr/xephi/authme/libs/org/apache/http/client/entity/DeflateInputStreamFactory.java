package fr.xephi.authme.libs.org.apache.http.client.entity;

import fr.xephi.authme.libs.org.apache.http.annotation.Contract;
import fr.xephi.authme.libs.org.apache.http.annotation.ThreadingBehavior;
import java.io.IOException;
import java.io.InputStream;

@Contract(
   threading = ThreadingBehavior.IMMUTABLE
)
public class DeflateInputStreamFactory implements InputStreamFactory {
   private static final DeflateInputStreamFactory INSTANCE = new DeflateInputStreamFactory();

   public static DeflateInputStreamFactory getInstance() {
      return INSTANCE;
   }

   public InputStream create(InputStream inputStream) throws IOException {
      return new DeflateInputStream(inputStream);
   }
}
