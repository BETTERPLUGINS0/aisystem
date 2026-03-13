package com.nisovin.shopkeepers.shopkeeper.offers;

import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.api.shopkeeper.offers.BookOffer;
import com.nisovin.shopkeepers.util.annotations.ReadOnly;
import com.nisovin.shopkeepers.util.data.container.DataContainer;
import com.nisovin.shopkeepers.util.data.container.value.DataValue;
import com.nisovin.shopkeepers.util.data.property.BasicProperty;
import com.nisovin.shopkeepers.util.data.property.Property;
import com.nisovin.shopkeepers.util.data.property.validation.java.IntegerValidators;
import com.nisovin.shopkeepers.util.data.property.validation.java.StringValidators;
import com.nisovin.shopkeepers.util.data.serialization.DataLoader;
import com.nisovin.shopkeepers.util.data.serialization.DataSaver;
import com.nisovin.shopkeepers.util.data.serialization.DataSerializer;
import com.nisovin.shopkeepers.util.data.serialization.InvalidDataException;
import com.nisovin.shopkeepers.util.data.serialization.MissingDataException;
import com.nisovin.shopkeepers.util.data.serialization.java.DataContainerSerializers;
import com.nisovin.shopkeepers.util.data.serialization.java.NumberSerializers;
import com.nisovin.shopkeepers.util.data.serialization.java.StringSerializers;
import com.nisovin.shopkeepers.util.java.Validate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.checkerframework.checker.nullness.qual.Nullable;

public class SKBookOffer implements BookOffer {
   private final String bookTitle;
   private final int price;
   private static final Property<String> BOOK_TITLE;
   private static final Property<Integer> PRICE;
   public static final DataSerializer<BookOffer> SERIALIZER;
   public static final DataSerializer<List<? extends BookOffer>> LIST_SERIALIZER;

   public SKBookOffer(String bookTitle, int price) {
      Validate.notEmpty(bookTitle, "bookTitle is null or empty");
      Validate.isTrue(price > 0, "price has to be positive");
      this.bookTitle = bookTitle;
      this.price = price;
   }

   public String getBookTitle() {
      return this.bookTitle;
   }

   public int getPrice() {
      return this.price;
   }

   public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("SKBookOffer [bookTitle=");
      builder.append(this.bookTitle);
      builder.append(", price=");
      builder.append(this.price);
      builder.append("]");
      return builder.toString();
   }

   public int hashCode() {
      int prime = true;
      int result = 1;
      int result = 31 * result + this.bookTitle.hashCode();
      result = 31 * result + this.price;
      return result;
   }

   public boolean equals(@Nullable Object obj) {
      if (this == obj) {
         return true;
      } else if (obj == null) {
         return false;
      } else if (!(obj instanceof SKBookOffer)) {
         return false;
      } else {
         SKBookOffer other = (SKBookOffer)obj;
         if (!this.bookTitle.equals(other.bookTitle)) {
            return false;
         } else {
            return this.price == other.price;
         }
      }
   }

   public static void saveOffers(DataValue dataValue, @Nullable @ReadOnly List<? extends BookOffer> offers) {
      Validate.notNull(dataValue, (String)"dataValue is null");
      if (offers == null) {
         dataValue.clear();
      } else {
         Object offerListData = LIST_SERIALIZER.serialize(offers);
         dataValue.set(offerListData);
      }
   }

   public static List<? extends BookOffer> loadOffers(DataValue dataValue) throws InvalidDataException {
      Validate.notNull(dataValue, (String)"dataValue is null");
      Object offerListData = dataValue.get();
      return offerListData == null ? Collections.emptyList() : (List)LIST_SERIALIZER.deserialize(offerListData);
   }

   public static boolean migrateOffers(DataValue dataValue, String logPrefix) throws InvalidDataException {
      Validate.notNull(logPrefix, (String)"logPrefix is null");
      return false;
   }

   static {
      BOOK_TITLE = (new BasicProperty()).dataKeyAccessor("book", StringSerializers.SCALAR).validator(StringValidators.NON_EMPTY).build();
      PRICE = (new BasicProperty()).dataKeyAccessor("price", NumberSerializers.INTEGER).validator(IntegerValidators.POSITIVE).build();
      SERIALIZER = new DataSerializer<BookOffer>() {
         @Nullable
         public Object serialize(BookOffer value) {
            Validate.notNull(value, (String)"value is null");
            DataContainer offerData = DataContainer.create();
            offerData.set((DataSaver)SKBookOffer.BOOK_TITLE, value.getBookTitle());
            offerData.set((DataSaver)SKBookOffer.PRICE, value.getPrice());
            return offerData.serialize();
         }

         public BookOffer deserialize(Object data) throws InvalidDataException {
            DataContainer offerData = (DataContainer)DataContainerSerializers.DEFAULT.deserialize(data);

            try {
               String bookTitle = (String)offerData.get((DataLoader)SKBookOffer.BOOK_TITLE);
               int price = (Integer)offerData.get((DataLoader)SKBookOffer.PRICE);
               return new SKBookOffer(bookTitle, price);
            } catch (MissingDataException var5) {
               throw new InvalidDataException(var5.getMessage(), var5);
            }
         }
      };
      LIST_SERIALIZER = new DataSerializer<List<? extends BookOffer>>() {
         @Nullable
         public Object serialize(@ReadOnly List<? extends BookOffer> value) {
            Validate.notNull(value, (String)"value is null");
            DataContainer offerListData = DataContainer.create();
            int id = 1;

            for(Iterator var4 = value.iterator(); var4.hasNext(); ++id) {
               BookOffer offer = (BookOffer)var4.next();
               Validate.notNull(offer, (String)"list of offers contains null");
               offerListData.set(String.valueOf(id), SKBookOffer.SERIALIZER.serialize(offer));
            }

            return offerListData.serialize();
         }

         public List<? extends BookOffer> deserialize(Object data) throws InvalidDataException {
            DataContainer offerListData = (DataContainer)DataContainerSerializers.DEFAULT.deserialize(data);
            Set<? extends String> keys = offerListData.getKeys();
            List<BookOffer> offers = new ArrayList(keys.size());

            BookOffer offer;
            for(Iterator var5 = keys.iterator(); var5.hasNext(); offers.add(offer)) {
               String id = (String)var5.next();
               Object offerData = Unsafe.assertNonNull(offerListData.get(id));

               try {
                  offer = (BookOffer)SKBookOffer.SERIALIZER.deserialize(offerData);
               } catch (InvalidDataException var10) {
                  throw new InvalidDataException("Invalid book offer " + id + ": " + var10.getMessage(), var10);
               }
            }

            return offers;
         }
      };
   }
}
