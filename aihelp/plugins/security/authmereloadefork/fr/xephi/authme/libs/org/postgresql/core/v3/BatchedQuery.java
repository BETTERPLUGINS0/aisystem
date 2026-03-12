package fr.xephi.authme.libs.org.postgresql.core.v3;

import fr.xephi.authme.libs.org.postgresql.core.NativeQuery;
import fr.xephi.authme.libs.org.postgresql.core.ParameterList;
import org.checkerframework.checker.nullness.qual.Nullable;

public class BatchedQuery extends SimpleQuery {
   @Nullable
   private String sql;
   private final int valuesBraceOpenPosition;
   private final int valuesBraceClosePosition;
   private final int batchSize;
   @Nullable
   private BatchedQuery[] blocks;

   public BatchedQuery(NativeQuery query, TypeTransferModeRegistry transferModeRegistry, int valuesBraceOpenPosition, int valuesBraceClosePosition, boolean sanitiserDisabled) {
      super(query, transferModeRegistry, sanitiserDisabled);
      this.valuesBraceOpenPosition = valuesBraceOpenPosition;
      this.valuesBraceClosePosition = valuesBraceClosePosition;
      this.batchSize = 1;
   }

   private BatchedQuery(BatchedQuery src, int batchSize) {
      super(src);
      this.valuesBraceOpenPosition = src.valuesBraceOpenPosition;
      this.valuesBraceClosePosition = src.valuesBraceClosePosition;
      this.batchSize = batchSize;
   }

   public BatchedQuery deriveForMultiBatch(int valueBlock) {
      if (this.getBatchSize() != 1) {
         throw new IllegalStateException("Only the original decorator can be derived.");
      } else if (valueBlock == 1) {
         return this;
      } else {
         int index = Integer.numberOfTrailingZeros(valueBlock) - 1;
         if (valueBlock <= 128 && valueBlock == 1 << index + 1) {
            if (this.blocks == null) {
               this.blocks = new BatchedQuery[7];
            }

            BatchedQuery bq = this.blocks[index];
            if (bq == null) {
               bq = new BatchedQuery(this, valueBlock);
               this.blocks[index] = bq;
            }

            return bq;
         } else {
            throw new IllegalArgumentException("Expected value block should be a power of 2 smaller or equal to 128. Actual block is " + valueBlock);
         }
      }
   }

   public int getBatchSize() {
      return this.batchSize;
   }

   public String getNativeSql() {
      if (this.sql != null) {
         return this.sql;
      } else {
         this.sql = this.buildNativeSql((ParameterList)null);
         return this.sql;
      }
   }

   private String buildNativeSql(@Nullable ParameterList params) {
      String sql = null;
      String nativeSql = super.getNativeSql();
      int batchSize = this.getBatchSize();
      if (batchSize < 2) {
         return nativeSql;
      } else if (nativeSql == null) {
         sql = "";
         return sql;
      } else {
         int valuesBlockCharCount = 0;
         int[] bindPositions = this.getNativeQuery().bindPositions;
         int[] chunkStart = new int[1 + bindPositions.length];
         int[] chunkEnd = new int[1 + bindPositions.length];
         chunkStart[0] = this.valuesBraceOpenPosition;
         int length;
         int pos;
         int valuesBlockCharCount;
         if (bindPositions.length == 0) {
            valuesBlockCharCount = this.valuesBraceClosePosition - this.valuesBraceOpenPosition + 1;
            chunkEnd[0] = this.valuesBraceClosePosition + 1;
         } else {
            chunkEnd[0] = bindPositions[0];
            valuesBlockCharCount = valuesBlockCharCount + (chunkEnd[0] - chunkStart[0]);

            for(length = 0; length < bindPositions.length; ++length) {
               int startIndex = bindPositions[length] + 2;

               for(pos = length < bindPositions.length - 1 ? bindPositions[length + 1] : this.valuesBraceClosePosition + 1; startIndex < pos && Character.isDigit(nativeSql.charAt(startIndex)); ++startIndex) {
               }

               chunkStart[length + 1] = startIndex;
               chunkEnd[length + 1] = pos;
               valuesBlockCharCount += chunkEnd[length + 1] - chunkStart[length + 1];
            }
         }

         length = nativeSql.length();
         length += NativeQuery.calculateBindLength(bindPositions.length * batchSize);
         length -= NativeQuery.calculateBindLength(bindPositions.length);
         length += (valuesBlockCharCount + 1) * (batchSize - 1);
         StringBuilder s = new StringBuilder(length);
         if (bindPositions.length > 0 && params == null) {
            s.append(nativeSql, 0, this.valuesBraceClosePosition + 1);
            pos = bindPositions.length + 1;
         } else {
            pos = 1;
            ++batchSize;
            s.append(nativeSql, 0, this.valuesBraceOpenPosition);
         }

         for(int i = 2; i <= batchSize; ++i) {
            if (i > 2 || pos != 1) {
               s.append(',');
            }

            s.append(nativeSql, chunkStart[0], chunkEnd[0]);

            for(int j = 1; j < chunkStart.length; ++j) {
               if (params == null) {
                  NativeQuery.appendBindName(s, pos++);
               } else {
                  s.append(params.toString(pos++, true));
               }

               s.append(nativeSql, chunkStart[j], chunkEnd[j]);
            }
         }

         s.append(nativeSql, this.valuesBraceClosePosition + 1, nativeSql.length());
         sql = s.toString();

         assert params != null || s.length() == length : "Predicted length != actual: " + length + " !=" + s.length();

         return sql;
      }
   }

   public String toString(@Nullable ParameterList params) {
      return this.getBatchSize() < 2 ? super.toString(params) : this.buildNativeSql(params);
   }
}
