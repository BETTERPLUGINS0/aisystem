package fr.xephi.authme.libs.com.mysql.cj.protocol.a;

import fr.xephi.authme.libs.com.mysql.cj.BindValue;
import fr.xephi.authme.libs.com.mysql.cj.Messages;
import fr.xephi.authme.libs.com.mysql.cj.exceptions.ExceptionFactory;
import fr.xephi.authme.libs.com.mysql.cj.exceptions.WrongArgumentException;
import fr.xephi.authme.libs.com.mysql.cj.protocol.InternalTime;
import fr.xephi.authme.libs.com.mysql.cj.protocol.Message;
import fr.xephi.authme.libs.com.mysql.cj.util.StringUtils;
import fr.xephi.authme.libs.com.mysql.cj.util.TimeUtil;
import java.time.LocalTime;

public class LocalTimeValueEncoder extends AbstractValueEncoder {
   public String getString(BindValue binding) {
      StringBuilder sb;
      switch(binding.getMysqlType()) {
      case NULL:
         return "null";
      case TIME:
         sb = new StringBuilder("'");
         sb.append(this.adjustLocalTime(LocalTime.ofNanoOfDay(((LocalTime)binding.getValue()).toNanoOfDay()), binding.getField()).format(TimeUtil.TIME_FORMATTER_WITH_OPTIONAL_MICROS));
         sb.append("'");
         return sb.toString();
      case CHAR:
      case VARCHAR:
      case TINYTEXT:
      case TEXT:
      case MEDIUMTEXT:
      case LONGTEXT:
         sb = new StringBuilder("'");
         sb.append(((LocalTime)binding.getValue()).format((Boolean)this.sendFractionalSeconds.getValue() && ((LocalTime)binding.getValue()).getNano() > 0 ? TimeUtil.TIME_FORMATTER_WITH_NANOS_NO_OFFSET : TimeUtil.TIME_FORMATTER_NO_FRACT_NO_OFFSET));
         sb.append("'");
         return sb.toString();
      default:
         throw (WrongArgumentException)ExceptionFactory.createException(WrongArgumentException.class, Messages.getString("PreparedStatement.67", new Object[]{binding.getValue().getClass().getName(), binding.getMysqlType().toString()}), this.exceptionInterceptor);
      }
   }

   public void encodeAsBinary(Message msg, BindValue binding) {
      NativePacketPayload intoPacket = (NativePacketPayload)msg;
      switch(binding.getMysqlType()) {
      case TIME:
         this.writeTime(msg, InternalTime.from(this.adjustLocalTime(LocalTime.ofNanoOfDay(((LocalTime)binding.getValue()).toNanoOfDay()), binding.getField())));
         return;
      case CHAR:
      case VARCHAR:
      case TINYTEXT:
      case TEXT:
      case MEDIUMTEXT:
      case LONGTEXT:
         intoPacket.writeBytes(NativeConstants.StringSelfDataType.STRING_LENENC, StringUtils.getBytes(((LocalTime)binding.getValue()).format((Boolean)this.sendFractionalSeconds.getValue() && ((LocalTime)binding.getValue()).getNano() > 0 ? TimeUtil.TIME_FORMATTER_WITH_NANOS_NO_OFFSET : TimeUtil.TIME_FORMATTER_NO_FRACT_NO_OFFSET), (String)this.charEncoding.getValue()));
         return;
      default:
         throw (WrongArgumentException)ExceptionFactory.createException(WrongArgumentException.class, Messages.getString("PreparedStatement.67", new Object[]{binding.getValue().getClass().getName(), binding.getMysqlType().toString()}), this.exceptionInterceptor);
      }
   }

   public void encodeAsQueryAttribute(Message msg, BindValue binding) {
      this.writeTime(msg, InternalTime.from((LocalTime)binding.getValue()));
   }
}
