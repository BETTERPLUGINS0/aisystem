package fr.xephi.authme.libs.com.mysql.cj.protocol.a;

import fr.xephi.authme.libs.com.mysql.cj.BindValue;
import fr.xephi.authme.libs.com.mysql.cj.Messages;
import fr.xephi.authme.libs.com.mysql.cj.exceptions.ExceptionFactory;
import fr.xephi.authme.libs.com.mysql.cj.exceptions.WrongArgumentException;
import fr.xephi.authme.libs.com.mysql.cj.protocol.InternalDate;
import fr.xephi.authme.libs.com.mysql.cj.protocol.InternalTime;
import fr.xephi.authme.libs.com.mysql.cj.protocol.InternalTimestamp;
import fr.xephi.authme.libs.com.mysql.cj.protocol.Message;
import fr.xephi.authme.libs.com.mysql.cj.util.StringUtils;
import fr.xephi.authme.libs.com.mysql.cj.util.TimeUtil;
import java.time.LocalDateTime;

public class LocalDateTimeValueEncoder extends AbstractValueEncoder {
   public String getString(BindValue binding) {
      StringBuilder sb;
      switch(binding.getMysqlType()) {
      case NULL:
         return "null";
      case DATE:
         sb = new StringBuilder("'");
         sb.append(((LocalDateTime)binding.getValue()).format(TimeUtil.DATE_FORMATTER));
         sb.append("'");
         return sb.toString();
      case TIME:
         sb = new StringBuilder("'");
         sb.append(this.adjustLocalDateTime(LocalDateTime.of(((LocalDateTime)binding.getValue()).toLocalDate(), ((LocalDateTime)binding.getValue()).toLocalTime()), binding.getField()).toLocalTime().format(TimeUtil.TIME_FORMATTER_WITH_OPTIONAL_MICROS));
         sb.append("'");
         return sb.toString();
      case DATETIME:
      case TIMESTAMP:
         sb = new StringBuilder("'");
         sb.append(this.adjustLocalDateTime(LocalDateTime.of(((LocalDateTime)binding.getValue()).toLocalDate(), ((LocalDateTime)binding.getValue()).toLocalTime()), binding.getField()).format(TimeUtil.DATETIME_FORMATTER_WITH_OPTIONAL_MICROS));
         sb.append("'");
         return sb.toString();
      case YEAR:
         return String.valueOf(((LocalDateTime)binding.getValue()).getYear());
      case CHAR:
      case VARCHAR:
      case TINYTEXT:
      case TEXT:
      case MEDIUMTEXT:
      case LONGTEXT:
         sb = new StringBuilder("'");
         sb.append(((LocalDateTime)binding.getValue()).format((Boolean)this.sendFractionalSeconds.getValue() && ((LocalDateTime)binding.getValue()).getNano() > 0 ? TimeUtil.DATETIME_FORMATTER_WITH_NANOS_NO_OFFSET : TimeUtil.DATETIME_FORMATTER_NO_FRACT_NO_OFFSET));
         sb.append("'");
         return sb.toString();
      default:
         throw (WrongArgumentException)ExceptionFactory.createException(WrongArgumentException.class, Messages.getString("PreparedStatement.67", new Object[]{binding.getValue().getClass().getName(), binding.getMysqlType().toString()}), this.exceptionInterceptor);
      }
   }

   public void encodeAsBinary(Message msg, BindValue binding) {
      LocalDateTime ldt = (LocalDateTime)binding.getValue();
      NativePacketPayload intoPacket = (NativePacketPayload)msg;
      switch(binding.getMysqlType()) {
      case DATE:
         this.writeDate(msg, InternalDate.from(this.adjustLocalDateTime(LocalDateTime.of(ldt.toLocalDate(), ldt.toLocalTime()), binding.getField()).toLocalDate()));
         return;
      case TIME:
         this.writeTime(msg, InternalTime.from(this.adjustLocalDateTime(LocalDateTime.of(ldt.toLocalDate(), ldt.toLocalTime()), binding.getField())));
         return;
      case DATETIME:
      case TIMESTAMP:
         this.writeDateTime(msg, InternalTimestamp.from(this.adjustLocalDateTime(LocalDateTime.of(ldt.toLocalDate(), ldt.toLocalTime()), binding.getField())));
         return;
      case YEAR:
         intoPacket.writeInteger(NativeConstants.IntegerDataType.INT4, (long)ldt.getYear());
         break;
      case CHAR:
      case VARCHAR:
      case TINYTEXT:
      case TEXT:
      case MEDIUMTEXT:
      case LONGTEXT:
         intoPacket.writeBytes(NativeConstants.StringSelfDataType.STRING_LENENC, StringUtils.getBytes(ldt.format((Boolean)this.sendFractionalSeconds.getValue() && ldt.getNano() > 0 ? TimeUtil.DATETIME_FORMATTER_WITH_NANOS_NO_OFFSET : TimeUtil.DATETIME_FORMATTER_NO_FRACT_NO_OFFSET), (String)this.charEncoding.getValue()));
         break;
      default:
         throw (WrongArgumentException)ExceptionFactory.createException(WrongArgumentException.class, Messages.getString("PreparedStatement.67", new Object[]{binding.getValue().getClass().getName(), binding.getMysqlType().toString()}), this.exceptionInterceptor);
      }

   }

   public void encodeAsQueryAttribute(Message msg, BindValue binding) {
      this.writeDateTime(msg, InternalTimestamp.from((LocalDateTime)binding.getValue()));
   }
}
