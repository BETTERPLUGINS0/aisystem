package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.recipe.RecipeDisplayId;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.recipe.display.RecipeDisplay;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.ResourceLocation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;

public class WrapperPlayServerCraftRecipeResponse extends PacketWrapper<WrapperPlayServerCraftRecipeResponse> {
   private int windowId;
   @ApiStatus.Obsolete
   private RecipeDisplayId recipeId;
   @ApiStatus.Obsolete
   private ResourceLocation recipeKey;
   private RecipeDisplay<?> recipeDisplay;

   public WrapperPlayServerCraftRecipeResponse(PacketSendEvent event) {
      super(event);
   }

   public WrapperPlayServerCraftRecipeResponse(int windowId, int recipeId) {
      this(windowId, new RecipeDisplayId(recipeId));
   }

   public WrapperPlayServerCraftRecipeResponse(int windowId, RecipeDisplayId recipeId) {
      super((PacketTypeCommon)PacketType.Play.Server.CRAFT_RECIPE_RESPONSE);
      this.windowId = windowId;
      this.recipeId = recipeId;
   }

   public WrapperPlayServerCraftRecipeResponse(int windowId, String recipeKey) {
      this(windowId, new ResourceLocation(recipeKey));
   }

   public WrapperPlayServerCraftRecipeResponse(int windowId, ResourceLocation recipeKey) {
      super((PacketTypeCommon)PacketType.Play.Server.CRAFT_RECIPE_RESPONSE);
      this.windowId = windowId;
      this.recipeKey = recipeKey;
   }

   public WrapperPlayServerCraftRecipeResponse(int windowId, RecipeDisplay<?> recipeDisplay) {
      super((PacketTypeCommon)PacketType.Play.Server.CRAFT_RECIPE_RESPONSE);
      this.windowId = windowId;
      this.recipeDisplay = recipeDisplay;
   }

   public void read() {
      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_21_2)) {
         this.windowId = this.readContainerId();
         this.recipeDisplay = RecipeDisplay.read(this);
      } else {
         this.windowId = this.readByte();
         if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_13)) {
            this.recipeKey = this.readIdentifier();
         } else {
            this.recipeId = RecipeDisplayId.read(this);
         }
      }

   }

   public void write() {
      this.writeContainerId(this.windowId);
      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_21_2)) {
         RecipeDisplay.write(this, this.recipeDisplay);
      } else if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_13)) {
         this.writeIdentifier(this.recipeKey);
      } else {
         RecipeDisplayId.write(this, this.recipeId);
      }

   }

   public void copy(WrapperPlayServerCraftRecipeResponse wrapper) {
      this.windowId = wrapper.windowId;
      this.recipeId = wrapper.recipeId;
      this.recipeKey = wrapper.recipeKey;
      this.recipeDisplay = wrapper.recipeDisplay;
   }

   public int getWindowId() {
      return this.windowId;
   }

   public void setWindowId(int windowId) {
      this.windowId = windowId;
   }

   /** @deprecated */
   @Deprecated
   public <T> T getRecipe() {
      return this.serverVersion.isOlderThan(ServerVersion.V_1_21_2) && this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_13) ? this.recipeKey : this.recipeId.getId();
   }

   /** @deprecated */
   @Deprecated
   public <T> void setRecipe(T recipe) {
      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_13)) {
         this.recipeKey = new ResourceLocation((String)recipe);
      } else {
         this.recipeId = new RecipeDisplayId((Integer)recipe);
      }

   }

   public ResourceLocation getRecipeKey() {
      return this.recipeKey;
   }

   public void setRecipeKey(ResourceLocation recipeKey) {
      this.recipeKey = recipeKey;
   }

   public RecipeDisplayId getRecipeId() {
      return this.recipeId;
   }

   public void setRecipeId(RecipeDisplayId recipeId) {
      this.recipeId = recipeId;
   }

   public RecipeDisplay<?> getRecipeDisplay() {
      return this.recipeDisplay;
   }

   public void setRecipeDisplay(RecipeDisplay<?> recipeDisplay) {
      this.recipeDisplay = recipeDisplay;
   }
}
