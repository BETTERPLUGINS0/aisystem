package fr.xephi.authme.libs.com.mysql.cj.protocol.a;

import fr.xephi.authme.libs.com.mysql.cj.BindValue;
import fr.xephi.authme.libs.com.mysql.cj.Messages;
import fr.xephi.authme.libs.com.mysql.cj.conf.PropertySet;
import fr.xephi.authme.libs.com.mysql.cj.exceptions.CJException;
import fr.xephi.authme.libs.com.mysql.cj.exceptions.ExceptionFactory;
import fr.xephi.authme.libs.com.mysql.cj.exceptions.ExceptionInterceptor;
import fr.xephi.authme.libs.com.mysql.cj.exceptions.WrongArgumentException;
import fr.xephi.authme.libs.com.mysql.cj.protocol.InternalDate;
import fr.xephi.authme.libs.com.mysql.cj.protocol.InternalTime;
import fr.xephi.authme.libs.com.mysql.cj.protocol.InternalTimestamp;
import fr.xephi.authme.libs.com.mysql.cj.protocol.Message;
import fr.xephi.authme.libs.com.mysql.cj.protocol.ServerSession;
import fr.xephi.authme.libs.com.mysql.cj.util.StringUtils;
import fr.xephi.authme.libs.com.mysql.cj.util.TimeUtil;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class StringValueEncoder extends AbstractValueEncoder {
   private CharsetEncoder charsetEncoder;

   public void init(PropertySet pset, ServerSession serverSess, ExceptionInterceptor excInterceptor) {
      super.init(pset, serverSess, excInterceptor);
      if (this.serverSession.getCharsetSettings().getRequiresEscapingEncoder()) {
         this.charsetEncoder = Charset.forName((String)this.charEncoding.getValue()).newEncoder();
      }

   }

   public byte[] getBytes(BindValue binding) {
      switch(binding.getMysqlType()) {
      case NULL:
         return StringUtils.getBytes("null");
      case CHAR:
      case ENUM:
      case SET:
      case VARCHAR:
      case TINYTEXT:
      case TEXT:
      case MEDIUMTEXT:
      case LONGTEXT:
      case JSON:
      case BINARY:
      case GEOMETRY:
      case VARBINARY:
      case TINYBLOB:
      case BLOB:
      case MEDIUMBLOB:
      case LONGBLOB:
         String x = (String)binding.getValue();
         if (binding.isNational() && !((String)this.charEncoding.getValue()).equalsIgnoreCase("UTF-8") && !((String)this.charEncoding.getValue()).equalsIgnoreCase("utf8")) {
            StringBuilder buf = new StringBuilder((int)((double)x.length() * 1.1D + 4.0D));
            buf.append("_utf8");
            StringUtils.escapeString(buf, x, this.serverSession.useAnsiQuotedIdentifiers(), (CharsetEncoder)null);
            return StringUtils.getBytes(buf.toString(), "UTF-8");
         } else {
            int stringLength = x.length();
            if (this.serverSession.isNoBackslashEscapesSet()) {
               if (!this.isEscapeNeededForString(x, stringLength)) {
                  StringBuilder quotedString = new StringBuilder(x.length() + 2);
                  quotedString.append('\'');
                  quotedString.append(x);
                  quotedString.append('\'');
                  StringUtils.getBytes(quotedString.toString(), (String)this.charEncoding.getValue());
               }

               return this.escapeBytesIfNeeded(StringUtils.getBytes(x, (String)this.charEncoding.getValue()));
            } else {
               if (this.isEscapeNeededForString(x, stringLength)) {
                  String escString = StringUtils.escapeString(new StringBuilder((int)((double)x.length() * 1.1D)), x, this.serverSession.useAnsiQuotedIdentifiers(), this.charsetEncoder).toString();
                  return StringUtils.getBytes(escString, (String)this.charEncoding.getValue());
               }

               return StringUtils.getBytesWrapped(x, '\'', '\'', (String)this.charEncoding.getValue());
            }
         }
      default:
         return StringUtils.getBytes(this.getString(binding), (String)this.charEncoding.getValue());
      }
   }

   public String getString(BindValue binding) {
      String x = (String)binding.getValue();
      StringBuilder sb;
      Object dt;
      switch(binding.getMysqlType()) {
      case NULL:
         return "null";
      case CHAR:
      case ENUM:
      case SET:
      case VARCHAR:
      case TINYTEXT:
      case TEXT:
      case MEDIUMTEXT:
      case LONGTEXT:
      case JSON:
      case BINARY:
      case GEOMETRY:
      case VARBINARY:
      case TINYBLOB:
      case BLOB:
      case MEDIUMBLOB:
      case LONGBLOB:
         sb = new StringBuilder("'");
         sb.append(x);
         sb.append("'");
         return sb.toString();
      case BOOLEAN:
      case BIT:
         Boolean b = null;
         if (!"true".equalsIgnoreCase(x) && !"Y".equalsIgnoreCase(x)) {
            if (!"false".equalsIgnoreCase(x) && !"N".equalsIgnoreCase(x)) {
               if (!x.matches("-?\\d+\\.?\\d*")) {
                  throw (WrongArgumentException)ExceptionFactory.createException(WrongArgumentException.class, Messages.getString("PreparedStatement.66", new Object[]{x}), this.exceptionInterceptor);
               }

               b = !x.matches("-?[0]+[.]*[0]*");
            } else {
               b = false;
            }
         } else {
            b = true;
         }

         return String.valueOf(b ? 1 : 0);
      case TINYINT:
      case TINYINT_UNSIGNED:
      case SMALLINT:
      case SMALLINT_UNSIGNED:
      case MEDIUMINT:
      case MEDIUMINT_UNSIGNED:
      case INT:
         return String.valueOf(Integer.parseInt(x));
      case INT_UNSIGNED:
      case BIGINT:
         return String.valueOf(Long.parseLong(x));
      case BIGINT_UNSIGNED:
         return String.valueOf((new BigInteger(x)).longValue());
      case FLOAT:
      case FLOAT_UNSIGNED:
         return StringUtils.fixDecimalExponent(Float.toString(Float.parseFloat(x)));
      case DOUBLE:
      case DOUBLE_UNSIGNED:
         return StringUtils.fixDecimalExponent(Double.toString(Double.parseDouble(x)));
      case DECIMAL:
      case DECIMAL_UNSIGNED:
         return this.getScaled(new BigDecimal(x), binding.getScaleOrLength()).toPlainString();
      case DATE:
         dt = TimeUtil.parseToDateTimeObject(x, binding.getMysqlType());
         if (dt instanceof LocalDate) {
            sb = new StringBuilder("'");
            sb.append(((LocalDate)dt).format(TimeUtil.DATE_FORMATTER));
            sb.append("'");
            return sb.toString();
         } else {
            if (dt instanceof LocalDateTime) {
               sb = new StringBuilder("'");
               sb.append(((LocalDateTime)dt).format(TimeUtil.DATE_FORMATTER));
               sb.append("'");
               return sb.toString();
            }

            throw (WrongArgumentException)ExceptionFactory.createException(WrongArgumentException.class, Messages.getString("PreparedStatement.67", new Object[]{dt.getClass().getName(), binding.getMysqlType().toString()}), this.exceptionInterceptor);
         }
      case DATETIME:
      case TIMESTAMP:
         dt = TimeUtil.parseToDateTimeObject(x, binding.getMysqlType());
         if (dt instanceof LocalDate) {
            sb = new StringBuilder("'");
            sb.append(LocalDateTime.of((LocalDate)dt, TimeUtil.DEFAULT_TIME).format(TimeUtil.DATETIME_FORMATTER_WITH_OPTIONAL_MICROS));
            sb.append("'");
            return sb.toString();
         } else {
            if (dt instanceof LocalDateTime) {
               sb = new StringBuilder("'");
               sb.append(this.adjustLocalDateTime((LocalDateTime)dt, binding.getField()).format(TimeUtil.DATETIME_FORMATTER_WITH_OPTIONAL_MICROS));
               sb.append("'");
               return sb.toString();
            }

            throw (WrongArgumentException)ExceptionFactory.createException(WrongArgumentException.class, Messages.getString("PreparedStatement.67", new Object[]{dt.getClass().getName(), binding.getMysqlType().toString()}), this.exceptionInterceptor);
         }
      case TIME:
         dt = TimeUtil.parseToDateTimeObject(x, binding.getMysqlType());
         if (dt instanceof LocalTime) {
            sb = new StringBuilder("'");
            sb.append(this.adjustLocalTime((LocalTime)dt, binding.getField()).format(TimeUtil.TIME_FORMATTER_WITH_OPTIONAL_MICROS));
            sb.append("'");
            return sb.toString();
         } else if (dt instanceof LocalDateTime) {
            sb = new StringBuilder("'");
            sb.append(this.adjustLocalTime(((LocalDateTime)dt).toLocalTime(), binding.getField()).format(TimeUtil.TIME_FORMATTER_WITH_OPTIONAL_MICROS));
            sb.append("'");
            return sb.toString();
         } else {
            if (dt instanceof Duration) {
               sb = new StringBuilder("'");
               sb.append(TimeUtil.getDurationString(this.adjustDuration((Duration)dt, binding.getField())));
               sb.append("'");
               return sb.toString();
            }

            throw (WrongArgumentException)ExceptionFactory.createException(WrongArgumentException.class, Messages.getString("PreparedStatement.67", new Object[]{dt.getClass().getName(), binding.getMysqlType().toString()}), this.exceptionInterceptor);
         }
      case YEAR:
         dt = TimeUtil.parseToDateTimeObject(x, binding.getMysqlType());
         if (dt instanceof LocalDate) {
            return String.valueOf(((LocalDate)dt).getYear());
         } else {
            if (dt instanceof LocalDateTime) {
               return String.valueOf(((LocalDateTime)dt).getYear());
            }

            throw (WrongArgumentException)ExceptionFactory.createException(WrongArgumentException.class, Messages.getString("PreparedStatement.67", new Object[]{dt.getClass().getName(), binding.getMysqlType().toString()}), this.exceptionInterceptor);
         }
      default:
         throw (WrongArgumentException)ExceptionFactory.createException(WrongArgumentException.class, Messages.getString("PreparedStatement.67", new Object[]{binding.getValue().getClass().getName(), binding.getMysqlType().toString()}), this.exceptionInterceptor);
      }
   }

   public void encodeAsQueryAttribute(Message msg, BindValue binding) {
      NativePacketPayload intoPacket = (NativePacketPayload)msg;
      String x = (String)binding.getValue();
      intoPacket.writeBytes(NativeConstants.StringSelfDataType.STRING_LENENC, StringUtils.getBytes(x, (String)this.charEncoding.getValue()));
   }

   public void encodeAsBinary(Message msg, BindValue binding) {
      NativePacketPayload intoPacket = (NativePacketPayload)msg;
      String x = (String)binding.getValue();
      Object dt;
      switch(binding.getMysqlType()) {
      case CHAR:
      case ENUM:
      case SET:
      case VARCHAR:
      case TINYTEXT:
      case TEXT:
      case MEDIUMTEXT:
      case LONGTEXT:
      case JSON:
      case BINARY:
      case GEOMETRY:
      case VARBINARY:
      case TINYBLOB:
      case BLOB:
      case MEDIUMBLOB:
      case LONGBLOB:
         if (binding.isNational() && !((String)this.charEncoding.getValue()).equalsIgnoreCase("UTF-8") && !((String)this.charEncoding.getValue()).equalsIgnoreCase("utf8")) {
            throw ExceptionFactory.createException(Messages.getString("ServerPreparedStatement.31"), this.exceptionInterceptor);
         }

         try {
            intoPacket.writeBytes(NativeConstants.StringSelfDataType.STRING_LENENC, StringUtils.getBytes(x, (String)this.charEncoding.getValue()));
            return;
         } catch (CJException var8) {
            throw ExceptionFactory.createException((String)(Messages.getString("ServerPreparedStatement.31") + (String)this.charEncoding.getValue() + "'"), (Throwable)var8, (ExceptionInterceptor)this.exceptionInterceptor);
         }
      case BOOLEAN:
      case BIT:
         Boolean b = null;
         if (!"true".equalsIgnoreCase(x) && !"Y".equalsIgnoreCase(x)) {
            if (!"false".equalsIgnoreCase(x) && !"N".equalsIgnoreCase(x)) {
               if (!x.matches("-?\\d+\\.?\\d*")) {
                  throw (WrongArgumentException)ExceptionFactory.createException(WrongArgumentException.class, Messages.getString("PreparedStatement.66", new Object[]{x}), this.exceptionInterceptor);
               }

               b = !x.matches("-?[0]+[.]*[0]*");
            } else {
               b = false;
            }
         } else {
            b = true;
         }

         intoPacket.writeInteger(NativeConstants.IntegerDataType.INT1, b ? 1L : 0L);
         return;
      case TINYINT:
      case TINYINT_UNSIGNED:
         intoPacket.writeInteger(NativeConstants.IntegerDataType.INT1, Long.parseLong(x));
         return;
      case SMALLINT:
      case SMALLINT_UNSIGNED:
      case MEDIUMINT:
      case MEDIUMINT_UNSIGNED:
         intoPacket.writeInteger(NativeConstants.IntegerDataType.INT2, Long.parseLong(x));
         return;
      case INT:
      case INT_UNSIGNED:
         intoPacket.writeInteger(NativeConstants.IntegerDataType.INT4, Long.parseLong(x));
         return;
      case BIGINT:
      case BIGINT_UNSIGNED:
         intoPacket.writeInteger(NativeConstants.IntegerDataType.INT8, Long.parseLong(x));
         return;
      case FLOAT:
      case FLOAT_UNSIGNED:
         intoPacket.writeInteger(NativeConstants.IntegerDataType.INT4, (long)Float.floatToIntBits(Float.parseFloat(x)));
         return;
      case DOUBLE:
      case DOUBLE_UNSIGNED:
         intoPacket.writeInteger(NativeConstants.IntegerDataType.INT8, Double.doubleToLongBits(Double.parseDouble(x)));
         return;
      case DECIMAL:
      case DECIMAL_UNSIGNED:
         BigDecimal bd = this.getScaled(new BigDecimal(x), binding.getScaleOrLength());
         intoPacket.writeBytes(NativeConstants.StringSelfDataType.STRING_LENENC, StringUtils.getBytes(bd.toPlainString(), (String)this.charEncoding.getValue()));
         return;
      case DATE:
         dt = TimeUtil.parseToDateTimeObject(x, binding.getMysqlType());
         if (dt instanceof LocalDate) {
            this.writeDate(msg, InternalDate.from((LocalDate)dt));
            return;
         }

         if (dt instanceof LocalDateTime) {
            this.writeDateTime(msg, InternalTimestamp.from((LocalDateTime)dt));
            return;
         }
         break;
      case DATETIME:
      case TIMESTAMP:
         dt = TimeUtil.parseToDateTimeObject(x, binding.getMysqlType());
         if (dt instanceof LocalDate) {
            this.writeDateTime(msg, InternalTimestamp.from((LocalDate)dt));
            return;
         }

         if (dt instanceof LocalDateTime) {
            this.writeDateTime(msg, InternalTimestamp.from((LocalDateTime)dt));
            return;
         }
         break;
      case TIME:
         dt = TimeUtil.parseToDateTimeObject(x, binding.getMysqlType());
         if (dt instanceof LocalTime) {
            this.writeTime(msg, InternalTime.from((LocalTime)dt));
            return;
         }

         if (dt instanceof Duration) {
            this.writeTime(msg, InternalTime.from(this.adjustDuration(Duration.ofNanos(((Duration)binding.getValue()).toNanos()), binding.getField())));
            return;
         }
         break;
      case YEAR:
         dt = TimeUtil.parseToDateTimeObject(x, binding.getMysqlType());
         if (dt instanceof LocalDate) {
            intoPacket.writeInteger(NativeConstants.IntegerDataType.INT4, (long)((LocalDate)dt).getYear());
            return;
         }

         if (dt instanceof LocalDateTime) {
            intoPacket.writeInteger(NativeConstants.IntegerDataType.INT4, (long)((LocalDateTime)dt).getYear());
            return;
         }
      }

      throw (WrongArgumentException)ExceptionFactory.createException(WrongArgumentException.class, Messages.getString("PreparedStatement.67", new Object[]{binding.getValue().getClass().getName(), binding.getMysqlType().toString()}), this.exceptionInterceptor);
   }

   private boolean isEscapeNeededForString(String x, int stringLength) {
      int i = 0;

      while(i < stringLength) {
         char c = x.charAt(i);
         switch(c) {
         case '\u0000':
         case '\n':
         case '\r':
         case '\u001a':
         case '"':
         case '\'':
         case '\\':
            return true;
         default:
            ++i;
         }
      }

      return false;
   }
}
