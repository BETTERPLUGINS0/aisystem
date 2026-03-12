package fr.xephi.authme.libs.com.mysql.cj.xdevapi;

import fr.xephi.authme.libs.com.mysql.cj.conf.PropertySet;
import fr.xephi.authme.libs.com.mysql.cj.exceptions.DataReadException;
import fr.xephi.authme.libs.com.mysql.cj.protocol.ColumnDefinition;
import fr.xephi.authme.libs.com.mysql.cj.result.BigDecimalValueFactory;
import fr.xephi.authme.libs.com.mysql.cj.result.BooleanValueFactory;
import fr.xephi.authme.libs.com.mysql.cj.result.ByteValueFactory;
import fr.xephi.authme.libs.com.mysql.cj.result.DoubleValueFactory;
import fr.xephi.authme.libs.com.mysql.cj.result.IntegerValueFactory;
import fr.xephi.authme.libs.com.mysql.cj.result.LongValueFactory;
import fr.xephi.authme.libs.com.mysql.cj.result.SqlDateValueFactory;
import fr.xephi.authme.libs.com.mysql.cj.result.SqlTimeValueFactory;
import fr.xephi.authme.libs.com.mysql.cj.result.SqlTimestampValueFactory;
import fr.xephi.authme.libs.com.mysql.cj.result.StringValueFactory;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.TimeZone;

public class RowImpl implements Row {
   private fr.xephi.authme.libs.com.mysql.cj.result.Row row;
   private ColumnDefinition metadata;
   private TimeZone defaultTimeZone;
   private PropertySet pset;

   public RowImpl(fr.xephi.authme.libs.com.mysql.cj.result.Row row, ColumnDefinition metadata, TimeZone defaultTimeZone, PropertySet pset) {
      this.row = row;
      this.metadata = metadata;
      this.defaultTimeZone = defaultTimeZone;
      this.pset = pset;
   }

   private int fieldNameToIndex(String fieldName) {
      int idx = this.metadata.findColumn(fieldName, true, 0);
      if (idx == -1) {
         throw new DataReadException("Invalid column");
      } else {
         return idx;
      }
   }

   public BigDecimal getBigDecimal(String fieldName) {
      return this.getBigDecimal(this.fieldNameToIndex(fieldName));
   }

   public BigDecimal getBigDecimal(int pos) {
      return (BigDecimal)this.row.getValue(pos, new BigDecimalValueFactory(this.pset));
   }

   public boolean getBoolean(String fieldName) {
      return this.getBoolean(this.fieldNameToIndex(fieldName));
   }

   public boolean getBoolean(int pos) {
      Boolean res = (Boolean)this.row.getValue(pos, new BooleanValueFactory(this.pset));
      return res == null ? false : res;
   }

   public byte getByte(String fieldName) {
      return this.getByte(this.fieldNameToIndex(fieldName));
   }

   public byte getByte(int pos) {
      Byte res = (Byte)this.row.getValue(pos, new ByteValueFactory(this.pset));
      return res == null ? 0 : res;
   }

   public Date getDate(String fieldName) {
      return this.getDate(this.fieldNameToIndex(fieldName));
   }

   public Date getDate(int pos) {
      return (Date)this.row.getValue(pos, new SqlDateValueFactory(this.pset, (Calendar)null, this.defaultTimeZone));
   }

   public DbDoc getDbDoc(String fieldName) {
      return this.getDbDoc(this.fieldNameToIndex(fieldName));
   }

   public DbDoc getDbDoc(int pos) {
      return (DbDoc)this.row.getValue(pos, new DbDocValueFactory(this.pset));
   }

   public double getDouble(String fieldName) {
      return this.getDouble(this.fieldNameToIndex(fieldName));
   }

   public double getDouble(int pos) {
      Double res = (Double)this.row.getValue(pos, new DoubleValueFactory(this.pset));
      return res == null ? 0.0D : res;
   }

   public int getInt(String fieldName) {
      return this.getInt(this.fieldNameToIndex(fieldName));
   }

   public int getInt(int pos) {
      Integer res = (Integer)this.row.getValue(pos, new IntegerValueFactory(this.pset));
      return res == null ? 0 : res;
   }

   public long getLong(String fieldName) {
      return this.getLong(this.fieldNameToIndex(fieldName));
   }

   public long getLong(int pos) {
      Long res = (Long)this.row.getValue(pos, new LongValueFactory(this.pset));
      return res == null ? 0L : res;
   }

   public String getString(String fieldName) {
      return this.getString(this.fieldNameToIndex(fieldName));
   }

   public String getString(int pos) {
      return (String)this.row.getValue(pos, new StringValueFactory(this.pset));
   }

   public Time getTime(String fieldName) {
      return this.getTime(this.fieldNameToIndex(fieldName));
   }

   public Time getTime(int pos) {
      return (Time)this.row.getValue(pos, new SqlTimeValueFactory(this.pset, (Calendar)null, this.defaultTimeZone));
   }

   public Timestamp getTimestamp(String fieldName) {
      return this.getTimestamp(this.fieldNameToIndex(fieldName));
   }

   public Timestamp getTimestamp(int pos) {
      return (Timestamp)this.row.getValue(pos, new SqlTimestampValueFactory(this.pset, (Calendar)null, this.defaultTimeZone, this.defaultTimeZone));
   }
}
