package fr.xephi.authme.libs.com.mysql.cj.protocol.a;

import fr.xephi.authme.libs.com.mysql.cj.BindValue;
import fr.xephi.authme.libs.com.mysql.cj.Messages;
import fr.xephi.authme.libs.com.mysql.cj.exceptions.ExceptionFactory;
import fr.xephi.authme.libs.com.mysql.cj.exceptions.WrongArgumentException;
import fr.xephi.authme.libs.com.mysql.cj.protocol.InternalTime;
import fr.xephi.authme.libs.com.mysql.cj.protocol.Message;
import fr.xephi.authme.libs.com.mysql.cj.util.StringUtils;
import fr.xephi.authme.libs.com.mysql.cj.util.TimeUtil;
import java.time.Duration;

public class DurationValueEncoder extends AbstractValueEncoder {
   public String getString(BindValue binding) {
      StringBuilder sb;
      switch(binding.getMysqlType()) {
      case NULL:
         return "null";
      case TIME:
         sb = new StringBuilder("'");
         sb.append(TimeUtil.getDurationString(this.adjustDuration(Duration.ofNanos(((Duration)binding.getValue()).toNanos()), binding.getField())));
         sb.append("'");
         return sb.toString();
      case CHAR:
      case VARCHAR:
      case TINYTEXT:
      case TEXT:
      case MEDIUMTEXT:
      case LONGTEXT:
         sb = new StringBuilder("'");
         sb.append(TimeUtil.getDurationString((Duration)binding.getValue()));
         sb.append("'");
         return sb.toString();
      default:
         throw (WrongArgumentException)ExceptionFactory.createException(WrongArgumentException.class, Messages.getString("PreparedStatement.67", new Object[]{binding.getValue().getClass().getName(), binding.getMysqlType().toString()}), this.exceptionInterceptor);
      }
   }

   public void encodeAsBinary(Message msg, BindValue binding) {
      Duration x = (Duration)binding.getValue();
      NativePacketPayload intoPacket = (NativePacketPayload)msg;
      switch(binding.getMysqlType()) {
      case TIME:
         this.writeTime(msg, InternalTime.from(this.adjustDuration(Duration.ofNanos(x.toNanos()), binding.getField())));
         return;
      case CHAR:
      case VARCHAR:
      case TINYTEXT:
      case TEXT:
      case MEDIUMTEXT:
      case LONGTEXT:
         intoPacket.writeBytes(NativeConstants.StringSelfDataType.STRING_LENENC, StringUtils.getBytes(TimeUtil.getDurationString(x), (String)this.charEncoding.getValue()));
         return;
      default:
         throw (WrongArgumentException)ExceptionFactory.createException(WrongArgumentException.class, Messages.getString("PreparedStatement.67", new Object[]{binding.getValue().getClass().getName(), binding.getMysqlType().toString()}), this.exceptionInterceptor);
      }
   }

   public void encodeAsQueryAttribute(Message msg, BindValue binding) {
      this.writeTime(msg, InternalTime.from((Duration)binding.getValue()));
   }
}
