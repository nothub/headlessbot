package lol.hub.headlessbot.behaviour.nodes.leafs;

import lol.hub.headlessbot.MC;
import lol.hub.headlessbot.behaviour.State;
import net.minecraft.client.MinecraftClient;

import java.util.function.Function;

public final class FunctionNode extends LeafNode {
    private final Function<MinecraftClient, State> func;

    public FunctionNode(Function<MinecraftClient, State> func) {
        this.func = func;
    }

    @Override
    public State run() {
        return func.apply(MC.client());
    }
}
