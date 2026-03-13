package com.nisovin.shopkeepers.shopobjects.block.base;

import com.nisovin.shopkeepers.shopobjects.block.AbstractBlockShopObjectType;
import java.util.List;
import org.checkerframework.checker.nullness.qual.Nullable;

public abstract class BaseBlockShopObjectType<T extends BaseBlockShopObject> extends AbstractBlockShopObjectType<T> {
   protected BaseBlockShopObjectType(String identifier, List<? extends String> aliases, @Nullable String permission, Class<T> shopObjectType) {
      super(identifier, aliases, permission, shopObjectType);
   }
}
