package lol.hub.headlessbot.behavior.nodes.decorators;

import lol.hub.headlessbot.behavior.State;
import lol.hub.headlessbot.behavior.nodes.Node;

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
