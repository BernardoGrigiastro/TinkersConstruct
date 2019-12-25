package slimeknights.tconstruct.tools.common.network;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.inventory.Container;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.recipe.Recipe;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import io.netty.buffer.ByteBuf;
import slimeknights.mantle.network.AbstractPacket;
import slimeknights.tconstruct.tools.common.inventory.ContainerCraftingStation;

// not threadsafe!
public class LastRecipeMessage extends AbstractPacket {

  private Recipe recipe;

  public LastRecipeMessage() {
  }

  public LastRecipeMessage(Recipe recipe) {
    this.recipe = recipe;
  }

  @Override
  public IMessage handleClient(NetHandlerPlayClient netHandler) {
    Container container = MinecraftClient.getMinecraft().player.openContainer;
    if(container instanceof ContainerCraftingStation) {
      ((ContainerCraftingStation) container).updateLastRecipeFromServer(recipe);
    }
    return null;
  }

  @Override
  public IMessage handleServer(NetHandlerPlayServer netHandler) {
    // only sent to server
    throw new UnsupportedOperationException("Clientside only");
  }

  @Override
  public void fromBytes(ByteBuf buf) {
    recipe = CraftingManager.REGISTRY.getObjectById(buf.readInt());
  }

  @Override
  public void toBytes(ByteBuf buf) {
    buf.writeInt(CraftingManager.REGISTRY.getIDForObject(recipe));
  }
}
