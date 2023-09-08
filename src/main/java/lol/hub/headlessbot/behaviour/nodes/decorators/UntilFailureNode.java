package lol.hub.headlessbot.behaviour.nodes.decorators;

import lol.hub.headlessbot.behaviour.State;
import lol.hub.headlessbot.behaviour.nodes.Node;

import static lol.hub.headlessbot.behaviour.State.FAILURE;
import static lol.hub.headlessbot.behaviour.State.RUNNING;

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
