package lol.hub.headlessbot.behavior.nodes.decorators;

import lol.hub.headlessbot.behavior.State;
import lol.hub.headlessbot.behavior.nodes.Node;

import static lol.hub.headlessbot.behavior.State.FAILURE;
import static lol.hub.headlessbot.behavior.State.RUNNING;

public final class UntilFailureNode extends DecoratorNode {
    public UntilFailureNode(Node child) {
        super(child);
    }

    @Override
    public State tick() {
        if (child().tick() == FAILURE) {
            return FAILURE;
        }
        return RUNNING;
    }
}
