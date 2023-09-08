package lol.hub.headlessbot.behaviour.nodes.decorators;

import lol.hub.headlessbot.behaviour.State;
import lol.hub.headlessbot.behaviour.nodes.Node;

import static lol.hub.headlessbot.behaviour.State.RUNNING;
import static lol.hub.headlessbot.behaviour.State.SUCCESS;

public final class UntilSuccessNode extends DecoratorNode {
    public UntilSuccessNode(Node child) {
        super(child);
    }

    @Override
    public State tick() {
        if (child().tick() == SUCCESS) {
            return SUCCESS;
        }
        return RUNNING;
    }
}
