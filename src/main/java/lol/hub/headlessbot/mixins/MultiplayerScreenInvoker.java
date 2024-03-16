package lol.hub.headlessbot.mixins;

import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.network.ServerInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(MultiplayerScreen.class)
public interface MultiplayerScreenInvoker {

    @Invoker("connect")
    public void invokeConnect(ServerInfo entry);

}
