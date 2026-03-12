package fr.xephi.authme.libs.com.mysql.cj.protocol.a;

import fr.xephi.authme.libs.com.mysql.cj.BindValue;
import fr.xephi.authme.libs.com.mysql.cj.Messages;
import fr.xephi.authme.libs.com.mysql.cj.MysqlType;
import fr.xephi.authme.libs.com.mysql.cj.exceptions.ExceptionFactory;
import fr.xephi.authme.libs.com.mysql.cj.exceptions.WrongArgumentException;
import fr.xephi.authme.libs.com.mysql.cj.protocol.InternalDate;
import fr.xephi.authme.libs.com.mysql.cj.protocol.InternalTime;
import fr.xephi.authme.libs.com.mysql.cj.protocol.InternalTimestamp;
import fr.xephi.authme.libs.com.mysql.cj.protocol.Message;
import fr.xephi.authme.libs.com.mysql.cj.util.StringUtils;
import fr.xephi.authme.libs.com.mysql.cj.util.TimeUtil;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.util.Calendar;
import java.util.Locale;

public class OffsetDateTimeValueEncoder extends AbstractValueEncoder {
   public String getString(BindValue binding) {
      OffsetDateTime odt = (OffsetDateTime)binding.getValue();
      StringBuilder sb;
      switch(binding.getMysqlType()) {
      case NULL:
         return "null";
      case DATE:
         sb = new StringBuilder("'");
         sb.append(odt.atZoneSameInstant(this.serverSession.getDefaultTimeZone().toZoneId()).toLocalDate().format(TimeUtil.DATE_FORMATTER));
         sb.append("'");
         return sb.toString();
      case TIME:
         sb = new StringBuilder("'");
         sb.append(this.adjustLocalTime(((OffsetDateTime)binding.getValue()).atZoneSameInstant(this.serverSession.getDefaultTimeZone().toZoneId()).toLocalTime(), binding.getField()).format(TimeUtil.TIME_FORMATTER_WITH_OPTIONAL_MICROS));
         sb.append("'");
         return sb.toString();
      case DATETIME:
      case TIMESTAMP:
         Timestamp x = this.adjustTimestamp(Timestamp.valueOf(((OffsetDateTime)binding.getValue()).atZoneSameInstant(this.serverSession.getDefaultTimeZone().toZoneId()).toLocalDateTime()), binding.getField(), binding.keepOrigNanos());
         StringBuffer buf = new StringBuffer();
         buf.append(TimeUtil.getSimpleDateFormat((SimpleDateFormat)null, "''yyyy-MM-dd HH:mm:ss", binding.getMysqlType() == MysqlType.TIMESTAMP && (Boolean)this.preserveInstants.getValue() ? this.serverSession.getSessionTimeZone() : this.serverSession.getDefaultTimeZone()).format(x));
         if (this.serverSession.getCapabilities().serverSupportsFracSecs() && x.getNanos() > 0) {
            buf.append('.');
            buf.append(TimeUtil.formatNanos(x.getNanos(), 6));
         }

         buf.append('\'');
         return buf.toString();
      case YEAR:
         return String.valueOf(odt.atZoneSameInstant(this.serverSession.getDefaultTimeZone().toZoneId()).getYear());
      case CHAR:
      case VARCHAR:
      case TINYTEXT:
      case TEXT:
      case MEDIUMTEXT:
      case LONGTEXT:
         sb = new StringBuilder("'");
         sb.append(odt.format((Boolean)this.sendFractionalSeconds.getValue() && odt.getNano() > 0 ? TimeUtil.DATETIME_FORMATTER_WITH_NANOS_WITH_OFFSET : TimeUtil.DATETIME_FORMATTER_NO_FRACT_WITH_OFFSET));
         sb.append("'");
         return sb.toString();
      default:
         throw (WrongArgumentException)ExceptionFactory.createException(WrongArgumentException.class, Messages.getString("PreparedStatement.67", new Object[]{binding.getValue().getClass().getName(), binding.getMysqlType().toString()}), this.exceptionInterceptor);
      }
   }

   public void encodeAsBinary(Message msg, BindValue binding) {
      OffsetDateTime odt = (OffsetDateTime)binding.getValue();
      NativePacketPayload intoPacket = (NativePacketPayload)msg;
      switch(binding.getMysqlType()) {
      case DATE:
         this.writeDate(msg, InternalDate.from(odt.atZoneSameInstant(this.serverSession.getDefaultTimeZone().toZoneId()).toLocalDate()));
         return;
      case TIME:
         this.writeTime(msg, InternalTime.from(this.adjustLocalTime(((OffsetDateTime)binding.getValue()).atZoneSameInstant(this.serverSession.getDefaultTimeZone().toZoneId()).toLocalTime(), binding.getField())));
         return;
      case DATETIME:
      case TIMESTAMP:
         Timestamp ts = this.adjustTimestamp(Timestamp.valueOf(((OffsetDateTime)binding.getValue()).atZoneSameInstant(this.serverSession.getDefaultTimeZone().toZoneId()).toLocalDateTime()), binding.getField(), binding.keepOrigNanos());
         Calendar calendar = Calendar.getInstance(binding.getMysqlType() == MysqlType.TIMESTAMP && (Boolean)this.preserveInstants.getValue() ? this.serverSession.getSessionTimeZone() : this.serverSession.getDefaultTimeZone(), Locale.US);
         calendar.setTime(ts);
         this.writeDateTime(msg, InternalTimestamp.from(calendar, ts.getNanos()));
         return;
      case YEAR:
         intoPacket.writeInteger(NativeConstants.IntegerDataType.INT4, (long)odt.atZoneSameInstant(this.serverSession.getDefaultTimeZone().toZoneId()).getYear());
         return;
      case CHAR:
      case VARCHAR:
      case TINYTEXT:
      case TEXT:
      case MEDIUMTEXT:
      case LONGTEXT:
         intoPacket.writeBytes(NativeConstants.StringSelfDataType.STRING_LENENC, StringUtils.getBytes(odt.format((Boolean)this.sendFractionalSeconds.getValue() && odt.getNano() > 0 ? TimeUtil.DATETIME_FORMATTER_WITH_NANOS_WITH_OFFSET : TimeUtil.DATETIME_FORMATTER_NO_FRACT_WITH_OFFSET), (String)this.charEncoding.getValue()));
         return;
      default:
         throw (WrongArgumentException)ExceptionFactory.createException(WrongArgumentException.class, Messages.getString("PreparedStatement.67", new Object[]{binding.getValue().getClass().getName(), binding.getMysqlType().toString()}), this.exceptionInterceptor);
      }
   }

   public void encodeAsQueryAttribute(Message msg, BindValue binding) {
      this.writeDateTimeWithOffset(msg, InternalTimestamp.from((OffsetDateTime)binding.getValue()));
   }
}
