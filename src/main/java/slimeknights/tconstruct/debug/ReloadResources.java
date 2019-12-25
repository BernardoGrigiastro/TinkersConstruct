package slimeknights.tconstruct.debug;

import net.minecraft.client.MinecraftClient;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

import javax.annotation.Nonnull;

public class ReloadResources extends CommandBase {

  @Override
  public String getName() {
    return "reloadResources";
  }

  @Override
  public String getUsage(ICommandSender sender) {
    return "/reloadResources";
  }

  @Override
  public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args)
      throws CommandException {
    if(sender.getEntityWorld().isClient) {
      MinecraftClient.getMinecraft().refreshResources();
    }
  }

  @Override
  public int getRequiredPermissionLevel() {
    return 0;
  }
}
