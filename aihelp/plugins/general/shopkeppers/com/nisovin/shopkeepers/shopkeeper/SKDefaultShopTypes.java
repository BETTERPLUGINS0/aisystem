package com.nisovin.shopkeepers.shopkeeper;

import com.nisovin.shopkeepers.SKShopkeepersPlugin;
import com.nisovin.shopkeepers.api.shopkeeper.DefaultShopTypes;
import com.nisovin.shopkeepers.shopkeeper.admin.regular.RegularAdminShopType;
import com.nisovin.shopkeepers.shopkeeper.player.book.BookPlayerShopType;
import com.nisovin.shopkeepers.shopkeeper.player.buy.BuyingPlayerShopType;
import com.nisovin.shopkeepers.shopkeeper.player.sell.SellingPlayerShopType;
import com.nisovin.shopkeepers.shopkeeper.player.trade.TradingPlayerShopType;
import java.util.ArrayList;
import java.util.List;

public class SKDefaultShopTypes implements DefaultShopTypes {
   private final RegularAdminShopType adminShopType = new RegularAdminShopType();
   private final SellingPlayerShopType sellingPlayerShopType = new SellingPlayerShopType();
   private final BuyingPlayerShopType buyingPlayerShopType = new BuyingPlayerShopType();
   private final TradingPlayerShopType tradingPlayerShopType = new TradingPlayerShopType();
   private final BookPlayerShopType bookPlayerShopType = new BookPlayerShopType();

   public List<? extends AbstractShopType<?>> getAll() {
      List<AbstractShopType<?>> shopTypes = new ArrayList();
      shopTypes.add(this.adminShopType);
      shopTypes.add(this.sellingPlayerShopType);
      shopTypes.add(this.buyingPlayerShopType);
      shopTypes.add(this.tradingPlayerShopType);
      shopTypes.add(this.bookPlayerShopType);
      return shopTypes;
   }

   public RegularAdminShopType getAdminShopType() {
      return this.getRegularAdminShopType();
   }

   public RegularAdminShopType getRegularAdminShopType() {
      return this.adminShopType;
   }

   public SellingPlayerShopType getSellingPlayerShopType() {
      return this.sellingPlayerShopType;
   }

   public BuyingPlayerShopType getBuyingPlayerShopType() {
      return this.buyingPlayerShopType;
   }

   public TradingPlayerShopType getTradingPlayerShopType() {
      return this.tradingPlayerShopType;
   }

   public BookPlayerShopType getBookPlayerShopType() {
      return this.bookPlayerShopType;
   }

   public static SKDefaultShopTypes getInstance() {
      return SKShopkeepersPlugin.getInstance().getDefaultShopTypes();
   }

   public static RegularAdminShopType ADMIN_REGULAR() {
      return getInstance().getRegularAdminShopType();
   }

   public static SellingPlayerShopType PLAYER_SELLING() {
      return getInstance().getSellingPlayerShopType();
   }

   public static BuyingPlayerShopType PLAYER_BUYING() {
      return getInstance().getBuyingPlayerShopType();
   }

   public static TradingPlayerShopType PLAYER_TRADING() {
      return getInstance().getTradingPlayerShopType();
   }

   public static BookPlayerShopType PLAYER_BOOK() {
      return getInstance().getBookPlayerShopType();
   }
}
