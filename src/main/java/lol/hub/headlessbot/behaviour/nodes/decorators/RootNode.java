package lol.hub.headlessbot.behaviour.nodes.decorators;

import lol.hub.headlessbot.behaviour.State;
import lol.hub.headlessbot.behaviour.nodes.Node;

public final class RootNode extends DecoratorNode {
    public RootNode(Node child) {
        super(null, child);
    }

    @Override
    public State run() {
        return child.run();
    }
}
