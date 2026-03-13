package com.nisovin.shopkeepers.util.java;

public interface Range {
   int getStartIndex(int var1);

   int getEndIndex(int var1);

   public static class PageRange implements Range {
      private final int page;
      private final int elementsPerPage;

      public PageRange(int page, int elementsPerPage) {
         Validate.isTrue(page >= 1, "page must be positive");
         Validate.isTrue(elementsPerPage >= 1, "elementsPerPage must be positive");
         this.page = page;
         this.elementsPerPage = elementsPerPage;
      }

      public int getMaxPage(int totalElements) {
         return Math.max(1, (int)Math.ceil((double)totalElements / (double)this.elementsPerPage));
      }

      public int getActualPage(int totalElements) {
         int maxPage = this.getMaxPage(totalElements);
         return Math.max(1, Math.min(this.page, maxPage));
      }

      public int getStartIndex(int totalElements) {
         int actualPage = this.getActualPage(totalElements);
         return (actualPage - 1) * this.elementsPerPage;
      }

      public int getEndIndex(int totalElements) {
         int actualPage = this.getActualPage(totalElements);
         return actualPage * this.elementsPerPage;
      }

      public String toString() {
         StringBuilder builder = new StringBuilder();
         builder.append("PageRange [page=");
         builder.append(this.page);
         builder.append(", elementsPerPage=");
         builder.append(this.elementsPerPage);
         builder.append("]");
         return builder.toString();
      }
   }

   public static class ExplicitRange implements Range {
      private final int startIndex;
      private final int endIndex;

      public ExplicitRange(int startIndex, int endIndex) {
         Validate.isTrue(startIndex >= 0, "startIndex cannot be negative");
         Validate.isTrue(endIndex >= 0, "endIndex cannot be negative");
         Validate.isTrue(endIndex > startIndex, "endIndex must be greater than startIndex");
         this.startIndex = startIndex;
         this.endIndex = endIndex;
      }

      public int getStartIndex(int totalElements) {
         return this.startIndex;
      }

      public int getEndIndex(int totalElements) {
         return this.endIndex;
      }

      public String toString() {
         StringBuilder builder = new StringBuilder();
         builder.append("ExplicitRange [startIndex=");
         builder.append(this.startIndex);
         builder.append(", endIndex=");
         builder.append(this.endIndex);
         builder.append("]");
         return builder.toString();
      }
   }
}
