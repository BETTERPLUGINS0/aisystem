package fr.xephi.authme.libs.org.apache.http.impl.cookie;

import fr.xephi.authme.libs.org.apache.http.annotation.Contract;
import fr.xephi.authme.libs.org.apache.http.annotation.ThreadingBehavior;
import fr.xephi.authme.libs.org.apache.http.conn.util.PublicSuffixList;
import java.io.IOException;
import java.io.Reader;

/** @deprecated */
@Deprecated
@Contract(
   threading = ThreadingBehavior.IMMUTABLE
)
public class PublicSuffixListParser {
   private final PublicSuffixFilter filter;
   private final fr.xephi.authme.libs.org.apache.http.conn.util.PublicSuffixListParser parser;

   PublicSuffixListParser(PublicSuffixFilter filter) {
      this.filter = filter;
      this.parser = new fr.xephi.authme.libs.org.apache.http.conn.util.PublicSuffixListParser();
   }

   public void parse(Reader reader) throws IOException {
      PublicSuffixList suffixList = this.parser.parse(reader);
      this.filter.setPublicSuffixes(suffixList.getRules());
      this.filter.setExceptions(suffixList.getExceptions());
   }
}
