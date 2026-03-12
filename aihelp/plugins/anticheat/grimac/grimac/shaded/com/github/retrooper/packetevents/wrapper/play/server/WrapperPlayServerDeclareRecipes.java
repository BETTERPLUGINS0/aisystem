package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.recipe.Recipe;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.recipe.RecipePropertySet;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.recipe.SingleInputOptionDisplay;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.ResourceLocation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import java.util.List;
import java.util.Map;

public class WrapperPlayServerDeclareRecipes extends PacketWrapper<WrapperPlayServerDeclareRecipes> {
   @ApiStatus.Obsolete
   private Recipe<?>[] recipes;
   private Map<ResourceLocation, RecipePropertySet> itemSets;
   private List<SingleInputOptionDisplay> stonecutterRecipes;

   public WrapperPlayServerDeclareRecipes(PacketSendEvent event) {
      super(event);
   }

   @ApiStatus.Obsolete
   public WrapperPlayServerDeclareRecipes(Recipe<?>[] recipes) {
      super((PacketTypeCommon)PacketType.Play.Server.DECLARE_RECIPES);
      this.recipes = recipes;
   }

   public WrapperPlayServerDeclareRecipes(Map<ResourceLocation, RecipePropertySet> itemSets, List<SingleInputOptionDisplay> stonecutterRecipes) {
      super((PacketTypeCommon)PacketType.Play.Server.DECLARE_RECIPES);
      this.itemSets = itemSets;
      this.stonecutterRecipes = stonecutterRecipes;
   }

   public void read() {
      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_21_2)) {
         this.itemSets = this.readMap(PacketWrapper::readIdentifier, RecipePropertySet::read);
         this.stonecutterRecipes = this.readList(SingleInputOptionDisplay::read);
      } else {
         this.recipes = (Recipe[])this.readArray(Recipe::read, Recipe.class);
      }

   }

   public void write() {
      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_21_2)) {
         this.writeMap(this.itemSets, PacketWrapper::writeIdentifier, RecipePropertySet::write);
         this.writeList(this.stonecutterRecipes, SingleInputOptionDisplay::write);
      } else {
         this.writeArray(this.recipes, Recipe::write);
      }

   }

   public void copy(WrapperPlayServerDeclareRecipes wrapper) {
      this.recipes = wrapper.recipes;
      this.itemSets = wrapper.itemSets;
      this.stonecutterRecipes = wrapper.stonecutterRecipes;
   }

   @ApiStatus.Obsolete
   public Recipe<?>[] getRecipes() {
      return this.recipes;
   }

   @ApiStatus.Obsolete
   public void setRecipes(Recipe<?>[] recipes) {
      this.recipes = recipes;
   }

   public Map<ResourceLocation, RecipePropertySet> getItemSets() {
      return this.itemSets;
   }

   public void setItemSets(Map<ResourceLocation, RecipePropertySet> itemSets) {
      this.itemSets = itemSets;
   }

   public List<SingleInputOptionDisplay> getStonecutterRecipes() {
      return this.stonecutterRecipes;
   }

   public void setStonecutterRecipes(List<SingleInputOptionDisplay> stonecutterRecipes) {
      this.stonecutterRecipes = stonecutterRecipes;
   }
}
