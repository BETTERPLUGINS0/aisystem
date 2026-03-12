package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.recipe.RecipeDisplayId;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.ResourceLocation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;

public class WrapperPlayClientCraftRecipeRequest extends PacketWrapper<WrapperPlayClientCraftRecipeRequest> {
   private int windowId;
   @ApiStatus.Obsolete
   private ResourceLocation recipeKey;
   private RecipeDisplayId recipeId;
   private boolean makeAll;

   public WrapperPlayClientCraftRecipeRequest(PacketReceiveEvent event) {
      super(event);
   }

   public WrapperPlayClientCraftRecipeRequest(int windowId, int recipeId, boolean makeAll) {
      this(windowId, new RecipeDisplayId(recipeId), makeAll);
   }

   public WrapperPlayClientCraftRecipeRequest(int windowId, RecipeDisplayId recipeId, boolean makeAll) {
      super((PacketTypeCommon)PacketType.Play.Client.CRAFT_RECIPE_REQUEST);
      this.windowId = windowId;
      this.recipeId = recipeId;
      this.makeAll = makeAll;
   }

   public WrapperPlayClientCraftRecipeRequest(int windowId, String recipeKey, boolean makeAll) {
      this(windowId, new ResourceLocation(recipeKey), makeAll);
   }

   public WrapperPlayClientCraftRecipeRequest(int windowId, ResourceLocation recipeKey, boolean makeAll) {
      super((PacketTypeCommon)PacketType.Play.Client.CRAFT_RECIPE_REQUEST);
      this.windowId = windowId;
      this.recipeKey = recipeKey;
      this.makeAll = makeAll;
   }

   public void read() {
      this.windowId = this.readContainerId();
      if (this.serverVersion.isOlderThan(ServerVersion.V_1_21_2) && this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_13)) {
         this.recipeKey = this.readIdentifier();
      } else {
         this.recipeId = RecipeDisplayId.read(this);
      }

      this.makeAll = this.readBoolean();
   }

   public void write() {
      this.writeContainerId(this.windowId);
      if (this.serverVersion.isOlderThan(ServerVersion.V_1_21_2) && this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_13)) {
         this.writeIdentifier(this.recipeKey);
      } else {
         RecipeDisplayId.write(this, this.recipeId);
      }

      this.writeBoolean(this.makeAll);
   }

   public void copy(WrapperPlayClientCraftRecipeRequest wrapper) {
      this.windowId = wrapper.windowId;
      this.recipeId = wrapper.recipeId;
      this.recipeKey = wrapper.recipeKey;
      this.makeAll = wrapper.makeAll;
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

   public boolean isMakeAll() {
      return this.makeAll;
   }

   public void setMakeAll(boolean makeAll) {
      this.makeAll = makeAll;
   }
}
