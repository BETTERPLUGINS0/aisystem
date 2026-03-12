package fr.xephi.authme.libs.org.postgresql.core.v3;

import fr.xephi.authme.libs.org.postgresql.core.Field;
import fr.xephi.authme.libs.org.postgresql.core.NativeQuery;
import fr.xephi.authme.libs.org.postgresql.core.Oid;
import fr.xephi.authme.libs.org.postgresql.core.ParameterList;
import fr.xephi.authme.libs.org.postgresql.core.Query;
import fr.xephi.authme.libs.org.postgresql.core.SqlCommand;
import fr.xephi.authme.libs.org.postgresql.jdbc.PgResultSet;
import java.lang.ref.PhantomReference;
import java.nio.charset.StandardCharsets;
import java.util.BitSet;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.checkerframework.checker.nullness.qual.Nullable;

class SimpleQuery implements Query {
   private static final Logger LOGGER = Logger.getLogger(SimpleQuery.class.getName());
   @Nullable
   private Map<String, Integer> resultSetColumnNameIndexMap;
   private final NativeQuery nativeQuery;
   @Nullable
   private final TypeTransferModeRegistry transferModeRegistry;
   @Nullable
   private String statementName;
   @Nullable
   private byte[] encodedStatementName;
   @Nullable
   private Field[] fields;
   private boolean needUpdateFieldFormats;
   private boolean hasBinaryFields;
   private boolean portalDescribed;
   private boolean statementDescribed;
   private final boolean sanitiserDisabled;
   @Nullable
   private PhantomReference<?> cleanupRef;
   @Nullable
   private int[] preparedTypes;
   @Nullable
   private BitSet unspecifiedParams;
   private short deallocateEpoch;
   @Nullable
   private Integer cachedMaxResultRowSize;
   static final SimpleParameterList NO_PARAMETERS = new SimpleParameterList(0, (TypeTransferModeRegistry)null);

   SimpleQuery(SimpleQuery src) {
      this(src.nativeQuery, src.transferModeRegistry, src.sanitiserDisabled);
   }

   SimpleQuery(NativeQuery query, @Nullable TypeTransferModeRegistry transferModeRegistry, boolean sanitiserDisabled) {
      this.nativeQuery = query;
      this.transferModeRegistry = transferModeRegistry;
      this.sanitiserDisabled = sanitiserDisabled;
   }

   public ParameterList createParameterList() {
      return this.nativeQuery.bindPositions.length == 0 ? NO_PARAMETERS : new SimpleParameterList(this.getBindCount(), this.transferModeRegistry);
   }

   public String toString(@Nullable ParameterList parameters) {
      return this.nativeQuery.toString(parameters);
   }

   public String toString() {
      return this.toString((ParameterList)null);
   }

   public void close() {
      this.unprepare();
   }

   @Nullable
   public SimpleQuery[] getSubqueries() {
      return null;
   }

   public int getMaxResultRowSize() {
      if (this.cachedMaxResultRowSize != null) {
         return this.cachedMaxResultRowSize;
      } else if (!this.statementDescribed) {
         throw new IllegalStateException("Cannot estimate result row size on a statement that is not described");
      } else {
         int maxResultRowSize = 0;
         if (this.fields != null) {
            Field[] var2 = this.fields;
            int var3 = var2.length;

            for(int var4 = 0; var4 < var3; ++var4) {
               Field f = var2[var4];
               int fieldLength = f.getLength();
               if (fieldLength < 1 || fieldLength >= 65535) {
                  maxResultRowSize = -1;
                  break;
               }

               maxResultRowSize += fieldLength;
            }
         }

         this.cachedMaxResultRowSize = maxResultRowSize;
         return maxResultRowSize;
      }
   }

   public String getNativeSql() {
      return this.nativeQuery.nativeSql;
   }

   void setStatementName(String statementName, short deallocateEpoch) {
      assert statementName != null : "statement name should not be null";

      this.statementName = statementName;
      this.encodedStatementName = statementName.getBytes(StandardCharsets.UTF_8);
      this.deallocateEpoch = deallocateEpoch;
   }

   void setPrepareTypes(int[] paramTypes) {
      for(int i = 0; i < paramTypes.length; ++i) {
         int paramType = paramTypes[i];
         if (paramType == 0) {
            if (this.unspecifiedParams == null) {
               this.unspecifiedParams = new BitSet();
            }

            this.unspecifiedParams.set(i);
         }
      }

      if (this.preparedTypes == null) {
         this.preparedTypes = (int[])paramTypes.clone();
      } else {
         System.arraycopy(paramTypes, 0, this.preparedTypes, 0, paramTypes.length);
      }
   }

   @Nullable
   int[] getPrepareTypes() {
      return this.preparedTypes;
   }

   @Nullable
   String getStatementName() {
      return this.statementName;
   }

   boolean isPreparedFor(int[] paramTypes, short deallocateEpoch) {
      if (this.statementName != null && this.preparedTypes != null) {
         if (this.deallocateEpoch != deallocateEpoch) {
            return false;
         } else {
            assert paramTypes.length == this.preparedTypes.length : String.format("paramTypes:%1$d preparedTypes:%2$d", paramTypes.length, this.preparedTypes.length);

            BitSet unspecified = this.unspecifiedParams;

            for(int i = 0; i < paramTypes.length; ++i) {
               int paramType = paramTypes[i];
               int preparedType = this.preparedTypes[i];
               if (paramType != preparedType && (paramType != 0 || unspecified == null || !unspecified.get(i))) {
                  if (LOGGER.isLoggable(Level.FINER)) {
                     LOGGER.log(Level.FINER, "Statement {0} does not match new parameter types. Will have to un-prepare it and parse once again. To avoid performance issues, use the same data type for the same bind position. Bind index (1-based) is {1}, preparedType was {2} (after describe {3}), current bind type is {4}", new Object[]{this.statementName, i + 1, Oid.toString(unspecified != null && unspecified.get(i) ? 0 : preparedType), Oid.toString(preparedType), Oid.toString(paramType)});
                  }

                  return false;
               }
            }

            return true;
         }
      } else {
         return false;
      }
   }

   boolean hasUnresolvedTypes() {
      if (this.preparedTypes == null) {
         return true;
      } else {
         return this.unspecifiedParams != null && !this.unspecifiedParams.isEmpty();
      }
   }

   @Nullable
   byte[] getEncodedStatementName() {
      return this.encodedStatementName;
   }

   void setFields(@Nullable Field[] fields) {
      this.fields = fields;
      this.resultSetColumnNameIndexMap = null;
      this.cachedMaxResultRowSize = null;
      this.needUpdateFieldFormats = fields != null;
      this.hasBinaryFields = false;
   }

   @Nullable
   Field[] getFields() {
      return this.fields;
   }

   boolean needUpdateFieldFormats() {
      if (this.needUpdateFieldFormats) {
         this.needUpdateFieldFormats = false;
         return true;
      } else {
         return false;
      }
   }

   public void resetNeedUpdateFieldFormats() {
      this.needUpdateFieldFormats = this.fields != null;
   }

   public boolean hasBinaryFields() {
      return this.hasBinaryFields;
   }

   public void setHasBinaryFields(boolean hasBinaryFields) {
      this.hasBinaryFields = hasBinaryFields;
   }

   boolean isPortalDescribed() {
      return this.portalDescribed;
   }

   void setPortalDescribed(boolean portalDescribed) {
      this.portalDescribed = portalDescribed;
      this.cachedMaxResultRowSize = null;
   }

   public boolean isStatementDescribed() {
      return this.statementDescribed;
   }

   void setStatementDescribed(boolean statementDescribed) {
      this.statementDescribed = statementDescribed;
      this.cachedMaxResultRowSize = null;
   }

   public boolean isEmpty() {
      return this.getNativeSql().isEmpty();
   }

   void setCleanupRef(PhantomReference<?> cleanupRef) {
      PhantomReference<?> oldCleanupRef = this.cleanupRef;
      if (oldCleanupRef != null) {
         oldCleanupRef.clear();
         oldCleanupRef.enqueue();
      }

      this.cleanupRef = cleanupRef;
   }

   void unprepare() {
      PhantomReference<?> cleanupRef = this.cleanupRef;
      if (cleanupRef != null) {
         cleanupRef.clear();
         cleanupRef.enqueue();
         this.cleanupRef = null;
      }

      if (this.unspecifiedParams != null) {
         this.unspecifiedParams.clear();
      }

      this.statementName = null;
      this.encodedStatementName = null;
      this.fields = null;
      this.resultSetColumnNameIndexMap = null;
      this.portalDescribed = false;
      this.statementDescribed = false;
      this.cachedMaxResultRowSize = null;
   }

   public int getBatchSize() {
      return 1;
   }

   NativeQuery getNativeQuery() {
      return this.nativeQuery;
   }

   public final int getBindCount() {
      return this.nativeQuery.bindPositions.length * this.getBatchSize();
   }

   @Nullable
   public Map<String, Integer> getResultSetColumnNameIndexMap() {
      Map<String, Integer> columnPositions = this.resultSetColumnNameIndexMap;
      if (columnPositions == null && this.fields != null) {
         columnPositions = PgResultSet.createColumnNameIndexMap(this.fields, this.sanitiserDisabled);
         if (this.statementName != null) {
            this.resultSetColumnNameIndexMap = columnPositions;
         }
      }

      return columnPositions;
   }

   public SqlCommand getSqlCommand() {
      return this.nativeQuery.getCommand();
   }
}
