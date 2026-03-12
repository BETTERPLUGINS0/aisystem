package fr.xephi.authme.libs.org.postgresql.core;

import fr.xephi.authme.libs.org.postgresql.util.ByteStreamWriter;
import java.io.InputStream;
import java.sql.SQLException;
import org.checkerframework.checker.index.qual.NonNegative;
import org.checkerframework.checker.index.qual.Positive;
import org.checkerframework.checker.nullness.qual.Nullable;

public interface ParameterList {
   void registerOutParameter(@Positive int var1, int var2) throws SQLException;

   @NonNegative
   int getParameterCount();

   @NonNegative
   int getInParameterCount();

   @NonNegative
   int getOutParameterCount();

   int[] getTypeOIDs();

   void setIntParameter(@Positive int var1, int var2) throws SQLException;

   void setLiteralParameter(@Positive int var1, String var2, int var3) throws SQLException;

   void setStringParameter(@Positive int var1, String var2, int var3) throws SQLException;

   void setBytea(@Positive int var1, byte[] var2, @NonNegative int var3, @NonNegative int var4) throws SQLException;

   void setBytea(@Positive int var1, InputStream var2, @NonNegative int var3) throws SQLException;

   void setBytea(@Positive int var1, InputStream var2) throws SQLException;

   void setBytea(@Positive int var1, ByteStreamWriter var2) throws SQLException;

   void setText(@Positive int var1, InputStream var2) throws SQLException;

   void setBinaryParameter(@Positive int var1, byte[] var2, int var3) throws SQLException;

   void setNull(@Positive int var1, int var2) throws SQLException;

   ParameterList copy();

   void clear();

   String toString(@Positive int var1, boolean var2);

   void appendAll(ParameterList var1) throws SQLException;

   @Nullable
   Object[] getValues();
}
