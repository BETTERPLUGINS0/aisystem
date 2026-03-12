package com.nisovin.shopkeepers.api.shopkeeper.offers;

import com.nisovin.shopkeepers.api.internal.ApiInternals;

public interface BookOffer {
   static BookOffer create(String bookTitle, int price) {
      return ApiInternals.getInstance().createBookOffer(bookTitle, price);
   }

   String getBookTitle();

   int getPrice();
}
