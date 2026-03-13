package github.nighter.smartspawner.libs.mariadb.codec;

import github.nighter.smartspawner.libs.mariadb.client.Context;
import github.nighter.smartspawner.libs.mariadb.client.socket.Writer;
import github.nighter.smartspawner.libs.mariadb.plugin.Codec;
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

   public void encodeBinary(Writer encoder, Context context) throws IOException, SQLException {
      this.codec.encodeBinary(encoder, context, this.value, this.cal, this.length);
   }
}
