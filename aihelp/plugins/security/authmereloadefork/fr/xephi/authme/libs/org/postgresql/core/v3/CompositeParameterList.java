package fr.xephi.authme.libs.org.postgresql.core.v3;

import fr.xephi.authme.libs.org.postgresql.core.ParameterList;
import fr.xephi.authme.libs.org.postgresql.util.ByteStreamWriter;
import fr.xephi.authme.libs.org.postgresql.util.GT;
import fr.xephi.authme.libs.org.postgresql.util.PSQLException;
import fr.xephi.authme.libs.org.postgresql.util.PSQLState;
import java.io.InputStream;
import java.sql.SQLException;
import org.checkerframework.checker.index.qual.NonNegative;
import org.checkerframework.checker.index.qual.Positive;
import org.checkerframework.checker.nullness.qual.Nullable;

class CompositeParameterList implements V3ParameterList {
   @Positive
   private final int total;
   private final SimpleParameterList[] subparams;
   private final int[] offsets;

   CompositeParameterList(SimpleParameterList[] subparams, int[] offsets) {
      this.subparams = subparams;
      this.offsets = offsets;
      this.total = offsets[offsets.length - 1] + subparams[offsets.length - 1].getInParameterCount();
   }

   private int findSubParam(@Positive int index) throws SQLException {
      if (index >= 1 && index <= this.total) {
         for(int i = this.offsets.length - 1; i >= 0; --i) {
            if (this.offsets[i] < index) {
               return i;
            }
         }

         throw new IllegalArgumentException("I am confused; can't find a subparam for index " + index);
      } else {
         throw new PSQLException(GT.tr("The column index is out of range: {0}, number of columns: {1}.", index, this.total), PSQLState.INVALID_PARAMETER_VALUE);
      }
   }

   public void registerOutParameter(@Positive int index, int sqlType) {
   }

   public int getDirection(int i) {
      return 0;
   }

   @NonNegative
   public int getParameterCount() {
      return this.total;
   }

   @NonNegative
   public int getInParameterCount() {
      return this.total;
   }

   @NonNegative
   public int getOutParameterCount() {
      return 0;
   }

   public int[] getTypeOIDs() {
      int[] oids = new int[this.total];

      for(int i = 0; i < this.offsets.length; ++i) {
         int[] subOids = this.subparams[i].getTypeOIDs();
         System.arraycopy(subOids, 0, oids, this.offsets[i], subOids.length);
      }

      return oids;
   }

   public void setIntParameter(@Positive int index, int value) throws SQLException {
      int sub = this.findSubParam(index);
      this.subparams[sub].setIntParameter(index - this.offsets[sub], value);
   }

   public void setLiteralParameter(@Positive int index, String value, int oid) throws SQLException {
      int sub = this.findSubParam(index);
      this.subparams[sub].setStringParameter(index - this.offsets[sub], value, oid);
   }

   public void setStringParameter(@Positive int index, String value, int oid) throws SQLException {
      int sub = this.findSubParam(index);
      this.subparams[sub].setStringParameter(index - this.offsets[sub], value, oid);
   }

   public void setBinaryParameter(@Positive int index, byte[] value, int oid) throws SQLException {
      int sub = this.findSubParam(index);
      this.subparams[sub].setBinaryParameter(index - this.offsets[sub], value, oid);
   }

   public void setBytea(@Positive int index, byte[] data, int offset, @NonNegative int length) throws SQLException {
      int sub = this.findSubParam(index);
      this.subparams[sub].setBytea(index - this.offsets[sub], data, offset, length);
   }

   public void setBytea(@Positive int index, InputStream stream, @NonNegative int length) throws SQLException {
      int sub = this.findSubParam(index);
      this.subparams[sub].setBytea(index - this.offsets[sub], stream, length);
   }

   public void setBytea(@Positive int index, InputStream stream) throws SQLException {
      int sub = this.findSubParam(index);
      this.subparams[sub].setBytea(index - this.offsets[sub], stream);
   }

   public void setBytea(@Positive int index, ByteStreamWriter writer) throws SQLException {
      int sub = this.findSubParam(index);
      this.subparams[sub].setBytea(index - this.offsets[sub], writer);
   }

   public void setText(@Positive int index, InputStream stream) throws SQLException {
      int sub = this.findSubParam(index);
      this.subparams[sub].setText(index - this.offsets[sub], stream);
   }

   public void setNull(@Positive int index, int oid) throws SQLException {
      int sub = this.findSubParam(index);
      this.subparams[sub].setNull(index - this.offsets[sub], oid);
   }

   public String toString(@Positive int index, boolean standardConformingStrings) {
      try {
         int sub = this.findSubParam(index);
         return this.subparams[sub].toString(index - this.offsets[sub], standardConformingStrings);
      } catch (SQLException var4) {
         throw new IllegalStateException(var4.getMessage());
      }
   }

   public ParameterList copy() {
      SimpleParameterList[] copySub = new SimpleParameterList[this.subparams.length];

      for(int sub = 0; sub < this.subparams.length; ++sub) {
         copySub[sub] = (SimpleParameterList)this.subparams[sub].copy();
      }

      return new CompositeParameterList(copySub, this.offsets);
   }

   public void clear() {
      SimpleParameterList[] var1 = this.subparams;
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         SimpleParameterList subparam = var1[var3];
         subparam.clear();
      }

   }

   @Nullable
   public SimpleParameterList[] getSubparams() {
      return this.subparams;
   }

   public void checkAllParametersSet() throws SQLException {
      SimpleParameterList[] var1 = this.subparams;
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         SimpleParameterList subparam = var1[var3];
         subparam.checkAllParametersSet();
      }

   }

   @Nullable
   public byte[][] getEncoding() {
      return null;
   }

   @Nullable
   public byte[] getFlags() {
      return null;
   }

   @Nullable
   public int[] getParamTypes() {
      return null;
   }

   @Nullable
   public Object[] getValues() {
      return null;
   }

   public void appendAll(ParameterList list) throws SQLException {
   }

   public void convertFunctionOutParameters() {
      SimpleParameterList[] var1 = this.subparams;
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         SimpleParameterList subparam = var1[var3];
         subparam.convertFunctionOutParameters();
      }

   }
}
