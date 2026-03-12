package fr.xephi.authme.libs.com.mysql.cj.xdevapi;

import fr.xephi.authme.libs.com.mysql.cj.conf.PropertySet;
import fr.xephi.authme.libs.com.mysql.cj.exceptions.AssertionFailedException;
import fr.xephi.authme.libs.com.mysql.cj.result.DefaultValueFactory;
import fr.xephi.authme.libs.com.mysql.cj.result.Field;
import fr.xephi.authme.libs.com.mysql.cj.util.StringUtils;
import java.io.IOException;
import java.io.StringReader;

public class DbDocValueFactory extends DefaultValueFactory<DbDoc> {
   public DbDocValueFactory(PropertySet pset) {
      super(pset);
   }

   public DbDoc createFromBytes(byte[] bytes, int offset, int length, Field f) {
      try {
         return JsonParser.parseDoc(new StringReader(StringUtils.toString(bytes, offset, length, f.getEncoding())));
      } catch (IOException var6) {
         throw AssertionFailedException.shouldNotHappen((Exception)var6);
      }
   }

   public DbDoc createFromNull() {
      return null;
   }

   public String getTargetTypeName() {
      return DbDoc.class.getName();
   }
}
