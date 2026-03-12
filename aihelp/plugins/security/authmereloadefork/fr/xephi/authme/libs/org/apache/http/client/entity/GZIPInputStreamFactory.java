package fr.xephi.authme.libs.org.apache.http.client.entity;

import fr.xephi.authme.libs.org.apache.http.annotation.Contract;
import fr.xephi.authme.libs.org.apache.http.annotation.ThreadingBehavior;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

@Contract(
   threading = ThreadingBehavior.IMMUTABLE
)
public class GZIPInputStreamFactory implements InputStreamFactory {
   private static final GZIPInputStreamFactory INSTANCE = new GZIPInputStreamFactory();

   public static GZIPInputStreamFactory getInstance() {
      return INSTANCE;
   }

   public InputStream create(InputStream inputStream) throws IOException {
      return new GZIPInputStream(inputStream);
   }
}
