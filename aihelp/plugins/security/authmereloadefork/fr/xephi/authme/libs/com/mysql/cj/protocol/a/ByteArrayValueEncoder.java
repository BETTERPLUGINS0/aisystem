package fr.xephi.authme.libs.com.mysql.cj.protocol.a;

import fr.xephi.authme.libs.com.mysql.cj.BindValue;
import fr.xephi.authme.libs.com.mysql.cj.conf.PropertyKey;
import fr.xephi.authme.libs.com.mysql.cj.conf.PropertySet;
import fr.xephi.authme.libs.com.mysql.cj.conf.RuntimeProperty;
import fr.xephi.authme.libs.com.mysql.cj.exceptions.ExceptionInterceptor;
import fr.xephi.authme.libs.com.mysql.cj.protocol.Message;
import fr.xephi.authme.libs.com.mysql.cj.protocol.ServerSession;
import fr.xephi.authme.libs.com.mysql.cj.util.StringUtils;
import java.nio.charset.StandardCharsets;

public class ByteArrayValueEncoder extends AbstractValueEncoder {
   protected RuntimeProperty<Integer> maxByteArrayAsHex;

   public void init(PropertySet pset, ServerSession serverSess, ExceptionInterceptor excInterceptor) {
      super.init(pset, serverSess, excInterceptor);
      this.maxByteArrayAsHex = pset.getIntegerProperty(PropertyKey.maxByteArrayAsHex);
   }

   public byte[] getBytes(BindValue binding) {
      return binding.escapeBytesIfNeeded() ? this.escapeBytesIfNeeded((byte[])((byte[])binding.getValue())) : (byte[])((byte[])binding.getValue());
   }

   public String getString(BindValue binding) {
      return binding.escapeBytesIfNeeded() && binding.getBinaryLength() <= (long)(Integer)this.maxByteArrayAsHex.getValue() ? StringUtils.toString(this.escapeBytesIfNeeded((byte[])((byte[])binding.getValue())), StandardCharsets.US_ASCII) : "** BYTE ARRAY DATA **";
   }

   public void encodeAsBinary(Message msg, BindValue binding) {
      ((NativePacketPayload)msg).writeBytes(NativeConstants.StringSelfDataType.STRING_LENENC, (byte[])((byte[])binding.getValue()));
   }
}
