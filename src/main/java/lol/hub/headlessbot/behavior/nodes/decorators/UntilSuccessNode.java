package lol.hub.headlessbot.behavior.nodes.decorators;

import lol.hub.headlessbot.behavior.State;
import lol.hub.headlessbot.behavior.nodes.Node;

import static lol.hub.headlessbot.behavior.State.RUNNING;
import static lol.hub.headlessbot.behavior.State.SUCCESS;

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
