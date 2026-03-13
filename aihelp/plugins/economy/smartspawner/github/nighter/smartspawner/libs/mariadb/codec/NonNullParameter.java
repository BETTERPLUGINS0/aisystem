package github.nighter.smartspawner.libs.mariadb.codec;

import github.nighter.smartspawner.libs.mariadb.client.Context;
import github.nighter.smartspawner.libs.mariadb.client.socket.Writer;
import github.nighter.smartspawner.libs.mariadb.plugin.Codec;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Calendar;

public class NonNullParameter<T> extends Parameter<T> {
   public NonNullParameter(Codec<T> codec, T value) {
      super(codec, value);
   }

   public void encodeText(Writer encoder, Context context) throws IOException, SQLException {
      this.codec.encodeText(encoder, context, this.value, (Calendar)null, this.length);
   }

   public boolean isNull() {
      return false;
   }
}
