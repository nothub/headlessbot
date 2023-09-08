package lol.hub.headlessbot.behaviour.nodes.decorators;

import lol.hub.headlessbot.behaviour.State;
import lol.hub.headlessbot.behaviour.nodes.Node;

public final class TimeoutNode extends DecoratorNode {
    public TimeoutNode(Node child) {
        super(child);
    }

    @Override
    public State tick() {
        // TODO
        return null;
    }
}
