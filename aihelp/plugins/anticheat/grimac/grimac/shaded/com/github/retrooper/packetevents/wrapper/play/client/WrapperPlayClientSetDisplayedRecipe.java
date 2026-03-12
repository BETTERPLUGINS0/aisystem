package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.recipe.RecipeDisplayId;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.ResourceLocation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;

public class WrapperPlayClientSetDisplayedRecipe extends PacketWrapper<WrapperPlayClientSetDisplayedRecipe> {
   @ApiStatus.Obsolete
   private ResourceLocation recipe;
   private RecipeDisplayId recipeId;

   public WrapperPlayClientSetDisplayedRecipe(PacketReceiveEvent event) {
      super(event);
   }

   @ApiStatus.Obsolete
   public WrapperPlayClientSetDisplayedRecipe(ResourceLocation recipe) {
      super((PacketTypeCommon)PacketType.Play.Client.SET_DISPLAYED_RECIPE);
      this.recipe = recipe;
   }

   public WrapperPlayClientSetDisplayedRecipe(RecipeDisplayId recipeId) {
      super((PacketTypeCommon)PacketType.Play.Client.SET_DISPLAYED_RECIPE);
      this.recipeId = recipeId;
   }

   public void read() {
      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_21_2)) {
         this.recipeId = RecipeDisplayId.read(this);
      } else {
         this.recipe = this.readIdentifier();
      }

   }

   public void write() {
      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_21_2)) {
         RecipeDisplayId.write(this, this.recipeId);
      } else {
         this.writeIdentifier(this.recipe);
      }

   }

   public void copy(WrapperPlayClientSetDisplayedRecipe packet) {
      this.recipe = packet.recipe;
      this.recipeId = packet.recipeId;
   }

   @ApiStatus.Obsolete
   public ResourceLocation getRecipe() {
      return this.recipe;
   }

   @ApiStatus.Obsolete
   public void setRecipe(ResourceLocation recipe) {
      this.recipe = recipe;
   }

   public RecipeDisplayId getRecipeId() {
      return this.recipeId;
   }

   public void setRecipeId(RecipeDisplayId recipeId) {
      this.recipeId = recipeId;
   }
}
