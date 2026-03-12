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
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class SqlTimeValueEncoder extends AbstractValueEncoder {
   private SimpleDateFormat tdf;

   public String getString(BindValue binding) {
      if (binding.isNull()) {
         return "null";
      } else {
         Time x = this.adjustTime((Time)binding.getValue());
         switch(binding.getMysqlType()) {
         case DATE:
            Date d = new Date(x.getTime());
            return binding.getCalendar() != null ? TimeUtil.getSimpleDateFormat("''yyyy-MM-dd''", binding.getCalendar()).format(d) : TimeUtil.getSimpleDateFormat((SimpleDateFormat)null, "''yyyy-MM-dd''", this.serverSession.getDefaultTimeZone()).format(d);
         case TIME:
         case CHAR:
         case VARCHAR:
         case TINYTEXT:
         case TEXT:
         case MEDIUMTEXT:
         case LONGTEXT:
            String formatStr = this.serverSession.getCapabilities().serverSupportsFracSecs() && (Boolean)this.sendFractionalSeconds.getValue() && (Boolean)this.sendFractionalSecondsForTime.getValue() && TimeUtil.hasFractionalSeconds(x) ? "''HH:mm:ss.SSS''" : "''HH:mm:ss''";
            return binding.getCalendar() != null ? TimeUtil.getSimpleDateFormat(formatStr, binding.getCalendar()).format(x) : TimeUtil.getSimpleDateFormat(this.tdf, formatStr, this.serverSession.getDefaultTimeZone()).format(x);
         case DATETIME:
         case TIMESTAMP:
            Timestamp ts = new Timestamp(x.getTime());
            if (!(Boolean)this.sendFractionalSecondsForTime.getValue()) {
               ts = TimeUtil.truncateFractionalSeconds(ts);
            }

            StringBuffer buf = new StringBuffer();
            buf.append(binding.getCalendar() != null ? TimeUtil.getSimpleDateFormat("''yyyy-MM-dd HH:mm:ss", binding.getCalendar()).format(x) : TimeUtil.getSimpleDateFormat((SimpleDateFormat)null, "''yyyy-MM-dd HH:mm:ss", this.serverSession.getDefaultTimeZone()).format(x));
            if (this.serverSession.getCapabilities().serverSupportsFracSecs() && ts.getNanos() > 0) {
               buf.append('.');
               buf.append(TimeUtil.formatNanos(ts.getNanos(), 6));
            }

            buf.append('\'');
            return buf.toString();
         case YEAR:
            Calendar cal = Calendar.getInstance();
            cal.setTime((java.util.Date)binding.getValue());
            return String.valueOf(cal.get(1));
         default:
            throw (WrongArgumentException)ExceptionFactory.createException(WrongArgumentException.class, Messages.getString("PreparedStatement.67", new Object[]{binding.getValue().getClass().getName(), binding.getMysqlType().toString()}), this.exceptionInterceptor);
         }
      }
   }

   public void encodeAsBinary(Message msg, BindValue binding) {
      NativePacketPayload intoPacket = (NativePacketPayload)msg;
      Calendar calendar = binding.getCalendar();
      Time x;
      switch(binding.getMysqlType()) {
      case DATE:
         if (calendar == null) {
            calendar = Calendar.getInstance(this.serverSession.getDefaultTimeZone(), Locale.US);
         }

         calendar.setTime((java.util.Date)binding.getValue());
         calendar.set(11, 0);
         calendar.set(12, 0);
         calendar.set(13, 0);
         this.writeDate(msg, InternalDate.from(calendar));
         return;
      case TIME:
         x = this.adjustTime((Time)binding.getValue());
         if (calendar == null) {
            calendar = Calendar.getInstance(this.serverSession.getDefaultTimeZone(), Locale.US);
         }

         calendar.setTime(x);
         this.writeTime(msg, InternalTime.from(calendar, (int)TimeUnit.MILLISECONDS.toNanos((long)calendar.get(14))));
         return;
      case CHAR:
      case VARCHAR:
      case TINYTEXT:
      case TEXT:
      case MEDIUMTEXT:
      case LONGTEXT:
         x = this.adjustTime((Time)binding.getValue());
         String formatStr = this.serverSession.getCapabilities().serverSupportsFracSecs() && (Boolean)this.sendFractionalSeconds.getValue() && (Boolean)this.sendFractionalSecondsForTime.getValue() && TimeUtil.hasFractionalSeconds(x) ? "HH:mm:ss.SSS" : "HH:mm:ss";
         intoPacket.writeBytes(NativeConstants.StringSelfDataType.STRING_LENENC, StringUtils.getBytes(binding.getCalendar() != null ? TimeUtil.getSimpleDateFormat(formatStr, binding.getCalendar()).format(x) : TimeUtil.getSimpleDateFormat(this.tdf, formatStr, this.serverSession.getDefaultTimeZone()).format(x), (String)this.charEncoding.getValue()));
         return;
      case DATETIME:
      case TIMESTAMP:
         Timestamp ts = new Timestamp(((Time)binding.getValue()).getTime());
         if (!this.serverSession.getCapabilities().serverSupportsFracSecs() || !(Boolean)this.sendFractionalSeconds.getValue() || !(Boolean)this.sendFractionalSecondsForTime.getValue()) {
            ts = TimeUtil.truncateFractionalSeconds(ts);
         }

         if (calendar == null) {
            calendar = Calendar.getInstance(this.serverSession.getDefaultTimeZone(), Locale.US);
         }

         calendar.setTime(ts);
         this.writeDateTime(msg, InternalTimestamp.from(calendar, ts.getNanos()));
         return;
      case YEAR:
         Calendar cal = Calendar.getInstance();
         cal.setTime((java.util.Date)binding.getValue());
         intoPacket.writeInteger(NativeConstants.IntegerDataType.INT4, (long)cal.get(1));
         return;
      default:
         throw (WrongArgumentException)ExceptionFactory.createException(WrongArgumentException.class, Messages.getString("PreparedStatement.67", new Object[]{binding.getValue().getClass().getName(), binding.getMysqlType().toString()}), this.exceptionInterceptor);
      }
   }

   public void encodeAsQueryAttribute(Message msg, BindValue binding) {
      Time x = (Time)binding.getValue();
      Calendar calendar = Calendar.getInstance(this.serverSession.getDefaultTimeZone(), Locale.US);
      calendar.setTime(x);
      this.writeTime(msg, InternalTime.from(calendar, (int)TimeUnit.MILLISECONDS.toNanos((long)calendar.get(14))));
   }
}
