package fr.xephi.authme.libs.org.mariadb.jdbc.codec;

import fr.xephi.authme.libs.org.mariadb.jdbc.client.Context;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.socket.Writer;
import fr.xephi.authme.libs.org.mariadb.jdbc.plugin.Codec;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Calendar;

public class ParameterWithCal<T> extends Parameter<T> {
   private final Calendar cal;

   public ParameterWithCal(Codec<T> codec, T value, Calendar cal) {
      super(codec, value);
      this.cal = cal;
   }

   public void encodeText(Writer encoder, Context context) throws IOException, SQLException {
      this.codec.encodeText(encoder, context, this.value, this.cal, this.length);
   }

   public void encodeBinary(Writer encoder) throws IOException, SQLException {
      this.codec.encodeBinary(encoder, this.value, this.cal, this.length);
   }
}
