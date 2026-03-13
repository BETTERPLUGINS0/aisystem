package com.nisovin.shopkeepers.util.data.matcher;

import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.util.bukkit.ConfigUtils;
import com.nisovin.shopkeepers.util.data.container.DataContainer;
import com.nisovin.shopkeepers.util.data.path.DataPath;
import com.nisovin.shopkeepers.util.java.MathUtils;
import com.nisovin.shopkeepers.util.java.Validate;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Objects;
import java.util.Map.Entry;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.checkerframework.checker.nullness.qual.Nullable;

public class DataMatcher {
   public static final DataMatcher EQUALITY = new DataMatcher();
   public static final DataMatcher FUZZY_NUMBERS = new DataMatcher() {
      protected DataMatcher.Result matchNumbers(DataPath path, Number number1, Number number2) {
         return MathUtils.fuzzyEquals(number1.doubleValue(), number2.doubleValue()) ? DataMatcher.Result.NO_MISMATCH : DataMatcher.Result.mismatch(path, number1, number2);
      }
   };

   protected DataMatcher() {
   }

   public final boolean matches(@Nullable Object leftObject, @Nullable Object rightObject) {
      return !this.match(leftObject, rightObject).isMismatch();
   }

   public final DataMatcher.Result match(@Nullable Object leftObject, @Nullable Object rightObject) {
      return this.match(DataPath.EMPTY, leftObject, rightObject);
   }

   protected final DataMatcher.Result match(DataPath path, @Nullable Object leftObject, @Nullable Object rightObject) {
      assert path != null;

      return leftObject == rightObject ? DataMatcher.Result.NO_MISMATCH : this.matchObjects(path, leftObject, rightObject);
   }

   protected DataMatcher.Result matchObjects(DataPath path, @Nullable Object leftObject, @Nullable Object rightObject) {
      assert path != null;

      DataMatcher.Result result = this.checkMatchDataContainers(path, leftObject, rightObject);
      if (result != null) {
         return result;
      } else {
         result = this.checkMatchLists(path, leftObject, rightObject);
         if (result != null) {
            return result;
         } else {
            result = this.checkMatchConfigurationSerializables(path, leftObject, rightObject);
            if (result != null) {
               return result;
            } else {
               result = this.checkMatchNumbers(path, leftObject, rightObject);
               return result != null ? result : this.matchObjectsExact(path, leftObject, rightObject);
            }
         }
      }
   }

   @Nullable
   protected DataMatcher.Result checkMatchDataContainers(DataPath path, @Nullable Object leftObject, @Nullable Object rightObject) {
      assert path != null;

      boolean leftIsDataContainer = DataContainer.isDataContainer(leftObject);
      boolean rightIsDataContainer = DataContainer.isDataContainer(rightObject);
      if (leftIsDataContainer && rightIsDataContainer) {
         DataContainer leftContainer = DataContainer.ofNonNull(Unsafe.assertNonNull(leftObject));
         DataContainer rightContainer = DataContainer.ofNonNull(Unsafe.assertNonNull(rightObject));
         return this.matchDataContainers(path, leftContainer, rightContainer);
      } else if (leftIsDataContainer ^ rightIsDataContainer) {
         return DataMatcher.Result.mismatch(path, leftObject, rightObject);
      } else {
         assert !leftIsDataContainer && !rightIsDataContainer;

         return null;
      }
   }

   protected DataMatcher.Result matchDataContainers(DataPath path, DataContainer leftDataContainer, DataContainer rightDataContainer) {
      assert path != null && leftDataContainer != null && rightDataContainer != null;

      Map<? extends String, ?> leftValues = leftDataContainer.getValues();
      Map<? extends String, ?> rightValues = rightDataContainer.getValues();
      if (leftValues.size() != rightValues.size()) {
         return DataMatcher.Result.mismatch(path, leftValues, rightValues);
      } else {
         Iterator leftValuesIterator = leftValues.entrySet().iterator();

         DataMatcher.Result entryMatchResult;
         do {
            if (!leftValuesIterator.hasNext()) {
               return DataMatcher.Result.NO_MISMATCH;
            }

            Entry<? extends String, ?> entry = (Entry)leftValuesIterator.next();
            String key = (String)entry.getKey();
            Object leftValue = entry.getValue();

            assert key != null && leftValue != null;

            Object rightValue = rightValues.get(key);
            DataPath entryPath = path.append(key);
            entryMatchResult = this.match(entryPath, leftValue, rightValue);
         } while(!entryMatchResult.isMismatch());

         return entryMatchResult;
      }
   }

   @Nullable
   protected DataMatcher.Result checkMatchLists(DataPath path, @Nullable Object leftObject, @Nullable Object rightObject) {
      assert path != null;

      boolean leftIsList = leftObject instanceof List;
      boolean rightIsList = rightObject instanceof List;
      if (leftIsList && rightIsList) {
         return this.matchLists(path, (List)Unsafe.castNonNull(leftObject), (List)Unsafe.castNonNull(rightObject));
      } else if (leftIsList ^ rightIsList) {
         return DataMatcher.Result.mismatch(path, leftObject, rightObject);
      } else {
         assert !leftIsList && !rightIsList;

         return null;
      }
   }

   protected DataMatcher.Result matchLists(DataPath path, List<?> leftList, List<?> rightList) {
      assert path != null && leftList != null && rightList != null;

      if (leftList.size() != rightList.size()) {
         return DataMatcher.Result.mismatch(path, leftList, rightList);
      } else {
         ListIterator<?> leftIterator = leftList.listIterator();
         ListIterator<?> rightIterator = rightList.listIterator();

         for(int index = 0; leftIterator.hasNext() && rightIterator.hasNext(); ++index) {
            Object leftElement = leftIterator.next();
            Object rightElement = rightIterator.next();
            DataPath elementPath = path.append(String.valueOf(index));
            DataMatcher.Result elementMatchResult = this.match(elementPath, leftElement, rightElement);
            if (elementMatchResult.isMismatch()) {
               return elementMatchResult;
            }
         }

         return DataMatcher.Result.NO_MISMATCH;
      }
   }

   @Nullable
   protected DataMatcher.Result checkMatchConfigurationSerializables(DataPath path, @Nullable Object leftObject, @Nullable Object rightObject) {
      assert path != null;

      Object leftSerialized = leftObject;
      if (leftObject instanceof ConfigurationSerializable) {
         leftSerialized = ConfigUtils.serialize((ConfigurationSerializable)leftObject);
      }

      Object rightSerialized = rightObject;
      if (rightObject instanceof ConfigurationSerializable) {
         rightSerialized = ConfigUtils.serialize((ConfigurationSerializable)rightObject);
      }

      return this.checkMatchDataContainers(path, leftSerialized, rightSerialized);
   }

   @Nullable
   protected DataMatcher.Result checkMatchNumbers(DataPath path, @Nullable Object leftObject, @Nullable Object rightObject) {
      assert path != null;

      boolean leftIsNumber = leftObject instanceof Number;
      boolean rightIsNumber = rightObject instanceof Number;
      if (leftIsNumber && rightIsNumber) {
         return this.matchNumbers(path, (Number)Unsafe.castNonNull(leftObject), (Number)Unsafe.castNonNull(rightObject));
      } else if (leftIsNumber ^ rightIsNumber) {
         return DataMatcher.Result.mismatch(path, leftObject, rightObject);
      } else {
         assert !leftIsNumber && !rightIsNumber;

         return null;
      }
   }

   protected DataMatcher.Result matchNumbers(DataPath path, Number leftNumber, Number rightNumber) {
      assert path != null && leftNumber != null && rightNumber != null;

      return this.matchObjectsExact(path, leftNumber, rightNumber);
   }

   protected final DataMatcher.Result matchObjectsExact(DataPath path, @Nullable Object leftObject, @Nullable Object rightObject) {
      assert path != null;

      return Objects.equals(leftObject, rightObject) ? DataMatcher.Result.NO_MISMATCH : DataMatcher.Result.mismatch(path, leftObject, rightObject);
   }

   public static class Result {
      public static final DataMatcher.Result NO_MISMATCH = new DataMatcher.Result();
      @Nullable
      private DataPath path;
      @Nullable
      private final Object leftObject;
      @Nullable
      private final Object rightObject;

      public static DataMatcher.Result mismatch(DataPath path, @Nullable Object leftObject, @Nullable Object rightObject) {
         Validate.notNull(path, (String)"path is null");
         return new DataMatcher.Result(path, leftObject, rightObject);
      }

      private Result() {
         this((DataPath)null, (Object)null, (Object)null);
      }

      public Result(@Nullable DataPath path, @Nullable Object leftObject, @Nullable Object rightObject) {
         if (path == null) {
            Validate.isTrue(leftObject == null, "path is null, but leftObject is not null");
            Validate.isTrue(rightObject == null, "path is null, but rightObject is not null");
         } else {
            Validate.isTrue(leftObject != null || rightObject != null, "mismatch but the leftObject and rightObject are both null");
         }

         this.path = path;
         this.leftObject = leftObject;
         this.rightObject = rightObject;
      }

      public final boolean isMismatch() {
         return this.path != null;
      }

      @Nullable
      public final DataPath getPath() {
         return this.path;
      }

      @Nullable
      public final Object getLeftObject() {
         return this.leftObject;
      }

      @Nullable
      public final Object getRightObject() {
         return this.rightObject;
      }
   }
}
