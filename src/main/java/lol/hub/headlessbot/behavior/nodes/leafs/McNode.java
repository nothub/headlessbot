package lol.hub.headlessbot.behavior.nodes.leafs;

import lol.hub.headlessbot.MC;
import lol.hub.headlessbot.behavior.State;
import net.minecraft.client.MinecraftClient;

import java.util.function.Function;

public abstract class McNode extends LeafNode {
    private final Function<MinecraftClient, State> func;

    public McNode(Function<MinecraftClient, State> func) {
        this.func = func;
    }

    @Override
    public State tick() {
        return func.apply(MC.client());
    }
}
