package fr.xephi.authme.libs.com.mysql.cj;

import fr.xephi.authme.libs.com.mysql.cj.conf.PropertySet;
import fr.xephi.authme.libs.com.mysql.cj.exceptions.ExceptionFactory;
import fr.xephi.authme.libs.com.mysql.cj.exceptions.ExceptionInterceptor;
import fr.xephi.authme.libs.com.mysql.cj.exceptions.WrongArgumentException;
import fr.xephi.authme.libs.com.mysql.cj.protocol.Message;
import fr.xephi.authme.libs.com.mysql.cj.protocol.Protocol;
import fr.xephi.authme.libs.com.mysql.cj.protocol.ServerSession;
import fr.xephi.authme.libs.com.mysql.cj.protocol.ValueEncoder;
import fr.xephi.authme.libs.com.mysql.cj.result.Field;
import java.io.InputStream;
import java.io.Reader;
import java.sql.Blob;
import java.sql.Clob;
import java.util.Calendar;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

public class NativeQueryBindValue implements BindValue {
   protected boolean isNull;
   protected boolean isNational = false;
   protected MysqlType targetType;
   public Object value;
   protected boolean isSet;
   public Calendar calendar;
   protected boolean escapeBytesIfNeeded;
   protected boolean isLoadDataQuery;
   PropertySet pset;
   Protocol<?> protocol;
   ServerSession serverSession;
   ExceptionInterceptor exceptionInterceptor;
   private Field field;
   protected boolean keepOrigNanos;
   protected ValueEncoder valueEncoder;
   protected long scaleOrLength;
   protected long boundBeforeExecutionNum;
   private String name;

   public NativeQueryBindValue(Session sess) {
      this.targetType = MysqlType.NULL;
      this.isSet = false;
      this.escapeBytesIfNeeded = true;
      this.isLoadDataQuery = false;
      this.field = null;
      this.keepOrigNanos = false;
      this.valueEncoder = null;
      this.scaleOrLength = -1L;
      this.boundBeforeExecutionNum = 0L;
      this.pset = sess.getPropertySet();
      this.protocol = ((NativeSession)sess).getProtocol();
      this.serverSession = sess.getServerSession();
      this.exceptionInterceptor = sess.getExceptionInterceptor();
   }

   public NativeQueryBindValue clone() {
      return new NativeQueryBindValue(this);
   }

   protected NativeQueryBindValue(NativeQueryBindValue copyMe) {
      this.targetType = MysqlType.NULL;
      this.isSet = false;
      this.escapeBytesIfNeeded = true;
      this.isLoadDataQuery = false;
      this.field = null;
      this.keepOrigNanos = false;
      this.valueEncoder = null;
      this.scaleOrLength = -1L;
      this.boundBeforeExecutionNum = 0L;
      this.isNull = copyMe.isNull;
      this.targetType = copyMe.targetType;
      if (copyMe.value != null && copyMe.value instanceof byte[]) {
         this.value = new byte[((byte[])((byte[])copyMe.value)).length];
         System.arraycopy(copyMe.value, 0, this.value, 0, ((byte[])((byte[])copyMe.value)).length);
      } else {
         this.value = copyMe.value;
      }

      this.isSet = copyMe.isSet;
      this.pset = copyMe.pset;
      this.protocol = copyMe.protocol;
      this.serverSession = copyMe.serverSession;
      this.calendar = copyMe.calendar;
      this.escapeBytesIfNeeded = copyMe.escapeBytesIfNeeded;
      this.isLoadDataQuery = copyMe.isLoadDataQuery;
      this.isNational = copyMe.isNational;
      this.field = copyMe.field;
      this.keepOrigNanos = copyMe.keepOrigNanos;
      this.valueEncoder = copyMe.valueEncoder;
      this.scaleOrLength = copyMe.scaleOrLength;
      this.boundBeforeExecutionNum = copyMe.boundBeforeExecutionNum;
   }

   private boolean resetToType(MysqlType newTargetType) {
      this.reset();
      return newTargetType != MysqlType.NULL && this.targetType != newTargetType;
   }

   public void setBinding(Object obj, MysqlType type, int numberOfExecutions, AtomicBoolean sendTypesToServer) {
      if (sendTypesToServer != null) {
         sendTypesToServer.compareAndSet(false, this.resetToType(type));
      }

      this.value = obj;
      this.targetType = type;
      this.boundBeforeExecutionNum = (long)numberOfExecutions;
      this.isNull = this.targetType == MysqlType.NULL;
      this.isSet = true;
      this.escapeBytesIfNeeded = true;
      Supplier<ValueEncoder> vc = this.protocol.getValueEncoderSupplier(this.isNull ? null : this.value);
      if (vc != null) {
         this.valueEncoder = (ValueEncoder)vc.get();
         this.valueEncoder.init(this.pset, this.serverSession, this.exceptionInterceptor);
      } else {
         throw (WrongArgumentException)ExceptionFactory.createException(WrongArgumentException.class, Messages.getString("PreparedStatement.67", new Object[]{obj.getClass().getName(), type.name()}), this.exceptionInterceptor);
      }
   }

   public byte[] getByteValue() {
      return this.valueEncoder != null ? this.valueEncoder.getBytes(this) : null;
   }

   public void reset() {
      this.isNull = false;
      this.targetType = MysqlType.NULL;
      this.value = null;
      this.isSet = false;
      this.calendar = null;
      this.escapeBytesIfNeeded = true;
      this.isLoadDataQuery = false;
      this.isNational = false;
      this.field = null;
      this.keepOrigNanos = false;
      this.valueEncoder = null;
      this.scaleOrLength = -1L;
   }

   public boolean isNull() {
      return this.isNull;
   }

   public void setNull(boolean isNull) {
      this.isNull = isNull;
      if (isNull) {
         this.targetType = MysqlType.NULL;
      }

      this.isSet = true;
   }

   public boolean isStream() {
      return this.value instanceof InputStream || this.value instanceof Reader || this.value instanceof Clob || this.value instanceof Blob;
   }

   public boolean isNational() {
      return this.isNational;
   }

   public void setIsNational(boolean isNational) {
      this.isNational = isNational;
   }

   public Object getValue() {
      return this.value;
   }

   public Field getField() {
      return this.field;
   }

   public void setField(Field field) {
      this.field = field;
   }

   public boolean keepOrigNanos() {
      return this.keepOrigNanos;
   }

   public void setKeepOrigNanos(boolean value) {
      this.keepOrigNanos = value;
   }

   public MysqlType getMysqlType() {
      return this.targetType;
   }

   public void setMysqlType(MysqlType type) {
      this.targetType = type;
   }

   public boolean escapeBytesIfNeeded() {
      return this.escapeBytesIfNeeded;
   }

   public void setEscapeBytesIfNeeded(boolean val) {
      this.escapeBytesIfNeeded = val;
   }

   public boolean isSet() {
      return this.isSet;
   }

   public Calendar getCalendar() {
      return this.calendar;
   }

   public void setCalendar(Calendar cal) {
      this.calendar = cal;
   }

   public int getFieldType() {
      switch(this.targetType) {
      case NULL:
         return 6;
      case DECIMAL:
      case DECIMAL_UNSIGNED:
         return 246;
      case DOUBLE:
      case DOUBLE_UNSIGNED:
         return 5;
      case BIGINT:
      case BIGINT_UNSIGNED:
         return 8;
      case BIT:
      case BOOLEAN:
      case TINYINT:
      case TINYINT_UNSIGNED:
         return 1;
      case BINARY:
      case VARBINARY:
      case CHAR:
      case VARCHAR:
         return 253;
      case FLOAT:
      case FLOAT_UNSIGNED:
         return 4;
      case SMALLINT:
      case SMALLINT_UNSIGNED:
      case MEDIUMINT:
      case MEDIUMINT_UNSIGNED:
         return 2;
      case INT:
      case INT_UNSIGNED:
      case YEAR:
         return 3;
      case DATE:
         return 10;
      case TIME:
         return 11;
      case TIMESTAMP:
         return 7;
      case DATETIME:
         return 12;
      case BLOB:
      case TEXT:
         return 252;
      case TINYBLOB:
      case TINYTEXT:
         return 249;
      case MEDIUMBLOB:
      case MEDIUMTEXT:
         return 250;
      case LONGBLOB:
      case LONGTEXT:
         return 251;
      default:
         return 253;
      }
   }

   public long getTextLength() {
      return this.valueEncoder == null ? -1L : this.valueEncoder.getTextLength(this);
   }

   public long getBinaryLength() {
      return this.valueEncoder == null ? -1L : this.valueEncoder.getBinaryLength(this);
   }

   public long getBoundBeforeExecutionNum() {
      return this.boundBeforeExecutionNum;
   }

   public String getString() {
      return this.valueEncoder == null ? "** NOT SPECIFIED **" : this.valueEncoder.getString(this);
   }

   public long getScaleOrLength() {
      return this.scaleOrLength;
   }

   public void setScaleOrLength(long scaleOrLength) {
      this.scaleOrLength = scaleOrLength;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public void writeAsText(Message intoMessage) {
      this.valueEncoder.encodeAsText(intoMessage, this);
   }

   public void writeAsBinary(Message intoMessage) {
      this.valueEncoder.encodeAsBinary(intoMessage, this);
   }

   public void writeAsQueryAttribute(Message intoMessage) {
      this.valueEncoder.encodeAsQueryAttribute(intoMessage, this);
   }
}
