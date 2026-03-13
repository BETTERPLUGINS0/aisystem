package com.nisovin.shopkeepers.shopobjects.living.types;

import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.api.shopkeeper.ShopCreationData;
import com.nisovin.shopkeepers.lang.Messages;
import com.nisovin.shopkeepers.shopkeeper.AbstractShopkeeper;
import com.nisovin.shopkeepers.shopobjects.ShopObjectData;
import com.nisovin.shopkeepers.shopobjects.entity.base.BaseEntityShopObjectCreationContext;
import com.nisovin.shopkeepers.shopobjects.entity.base.BaseEntityShopObjectType;
import com.nisovin.shopkeepers.ui.editor.Button;
import com.nisovin.shopkeepers.ui.editor.EditorView;
import com.nisovin.shopkeepers.ui.editor.ShopkeeperActionButton;
import com.nisovin.shopkeepers.util.data.property.BasicProperty;
import com.nisovin.shopkeepers.util.data.property.Property;
import com.nisovin.shopkeepers.util.data.property.value.PropertyValue;
import com.nisovin.shopkeepers.util.data.serialization.InvalidDataException;
import com.nisovin.shopkeepers.util.data.serialization.java.EnumSerializers;
import com.nisovin.shopkeepers.util.inventory.ItemUtils;
import com.nisovin.shopkeepers.util.java.EnumUtils;
import java.util.List;
import java.util.Objects;
import org.bukkit.Material;
import org.bukkit.entity.Panda;
import org.bukkit.entity.Panda.Gene;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.Nullable;

public class PandaShop extends BabyableShop<Panda> {
   public static final Property<Gene> GENE;
   private final PropertyValue<Gene> geneProperty;

   public PandaShop(BaseEntityShopObjectCreationContext context, BaseEntityShopObjectType<PandaShop> shopObjectType, AbstractShopkeeper shopkeeper, @Nullable ShopCreationData creationData) {
      super(context, shopObjectType, shopkeeper, creationData);
      PropertyValue var10001 = new PropertyValue(GENE);
      PandaShop var10002 = (PandaShop)Unsafe.initialized(this);
      Objects.requireNonNull(var10002);
      this.geneProperty = var10001.onValueChanged(var10002::applyGene).build(this.properties);
   }

   public void load(ShopObjectData shopObjectData) throws InvalidDataException {
      super.load(shopObjectData);
      this.geneProperty.load(shopObjectData);
   }

   public void save(ShopObjectData shopObjectData, boolean saveAll) {
      super.save(shopObjectData, saveAll);
      this.geneProperty.save(shopObjectData);
   }

   protected void onSpawn() {
      super.onSpawn();
      this.applyGene();
   }

   public List<Button> createEditorButtons() {
      List<Button> editorButtons = super.createEditorButtons();
      editorButtons.add(this.getGeneEditorButton());
      return editorButtons;
   }

   public Gene getGene() {
      return (Gene)this.geneProperty.getValue();
   }

   public void setGene(Gene gene) {
      this.geneProperty.setValue(gene);
   }

   public void cycleGene(boolean backwards) {
      this.setGene((Gene)EnumUtils.cycleEnumConstant(Gene.class, this.getGene(), backwards));
   }

   private void applyGene() {
      Panda entity = (Panda)this.getEntity();
      if (entity != null) {
         Gene gene = this.getGene();
         entity.setMainGene(gene);
         entity.setHiddenGene(gene);
      }
   }

   private ItemStack getGeneEditorItem() {
      ItemStack iconItem = new ItemStack(Material.PANDA_SPAWN_EGG);
      ItemUtils.setDisplayNameAndLore(iconItem, Messages.buttonPandaVariant, Messages.buttonPandaVariantLore);
      return iconItem;
   }

   private Button getGeneEditorButton() {
      return new ShopkeeperActionButton() {
         @Nullable
         public ItemStack getIcon(EditorView editorView) {
            return PandaShop.this.getGeneEditorItem();
         }

         protected boolean runAction(EditorView editorView, InventoryClickEvent clickEvent) {
            boolean backwards = clickEvent.isRightClick();
            PandaShop.this.cycleGene(backwards);
            return true;
         }
      };
   }

   static {
      GENE = (new BasicProperty()).dataKeyAccessor("gene", EnumSerializers.lenient(Gene.class)).defaultValue(Gene.NORMAL).build();
   }
}
