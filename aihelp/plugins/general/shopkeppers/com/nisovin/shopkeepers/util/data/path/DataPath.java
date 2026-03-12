package com.nisovin.shopkeepers.util.data.path;

import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.util.data.container.DataContainer;
import com.nisovin.shopkeepers.util.java.CollectionUtils;
import com.nisovin.shopkeepers.util.java.ConversionUtils;
import com.nisovin.shopkeepers.util.java.Validate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class DataPath {
   public static final DataPath EMPTY = new DataPath();
   private final List<? extends String> segments;

   private DataPath() {
      this(Collections.emptyList(), true);
   }

   public DataPath(String segment) {
      this(Collections.singletonList(Validate.notEmpty(segment, "segment is null or empty")), true);
   }

   public DataPath(List<? extends String> segments) {
      this(segments, false);
   }

   private DataPath(List<? extends String> segments, boolean assumeSafe) {
      if (assumeSafe) {
         this.segments = segments;
      } else {
         Validate.notNull(segments, (String)"segments is null");
         segments.forEach((segment) -> {
            Validate.notEmpty(segment, "one of the segments is null or empty");
         });
         this.segments = Collections.unmodifiableList(new ArrayList(segments));
      }

   }

   public List<? extends String> getSegments() {
      return this.segments;
   }

   public int getLength() {
      return this.segments.size();
   }

   public boolean isEmpty() {
      return this.getLength() == 0;
   }

   @Nullable
   public String getFirstSegment() {
      return this.isEmpty() ? null : (String)this.segments.get(0);
   }

   @Nullable
   public String getLastSegment() {
      return this.isEmpty() ? null : (String)this.segments.get(this.segments.size() - 1);
   }

   @Nullable
   public DataPath getParentPath() {
      return this.isEmpty() ? null : this.getSubPath(0, this.segments.size() - 1);
   }

   @Nullable
   public DataPath getChildPath() {
      return this.isEmpty() ? null : this.getSubPath(1, this.segments.size());
   }

   public DataPath getSubPath(int fromIndex, int toIndex) {
      return new DataPath(this.segments.subList(fromIndex, toIndex), true);
   }

   public DataPath append(String segment) {
      Validate.notEmpty(segment, "segment is null or empty");
      List<? extends String> newSegments = CollectionUtils.unmodifiableCopyAndAdd(this.segments, segment);
      return new DataPath(newSegments, true);
   }

   public DataPath append(DataPath path) {
      Validate.notNull(path, (String)"path is null");
      if (this.isEmpty()) {
         return path;
      } else if (path.isEmpty()) {
         return this;
      } else {
         List<? extends String> newSegments = CollectionUtils.unmodifiableCopyAndAddAll(this.segments, path.getSegments());
         return new DataPath(newSegments, true);
      }
   }

   @Nullable
   public Object resolve(@Nullable Object dataObject) {
      if (dataObject == null) {
         return null;
      } else if (this.isEmpty()) {
         return dataObject;
      } else {
         String nextSegment = (String)Unsafe.assertNonNull(this.getFirstSegment());
         Object nextElement = null;
         DataContainer dataContainer = DataContainer.of(dataObject);
         if (dataContainer != null) {
            nextElement = dataContainer.get(nextSegment);
         } else {
            if (!(dataObject instanceof List)) {
               return null;
            }

            List<?> list = (List)dataObject;
            Integer index = ConversionUtils.parseInt(nextSegment);
            if (index != null) {
               nextElement = list.get(index);
            }
         }

         if (nextElement != null) {
            DataPath childPath = (DataPath)Unsafe.assertNonNull(this.getChildPath());
            return childPath.resolve(nextElement);
         } else {
            return null;
         }
      }
   }

   public int hashCode() {
      int prime = true;
      int result = 1;
      int result = 31 * result + this.segments.hashCode();
      return result;
   }

   public boolean equals(@Nullable Object obj) {
      if (this == obj) {
         return true;
      } else if (!(obj instanceof DataPath)) {
         return false;
      } else {
         DataPath other = (DataPath)obj;
         return this.segments.equals(other.segments);
      }
   }

   public String toString() {
      return this.segments.toString();
   }
}
