package lol.hub.headlessbot.behaviour.nodes.decorators;

import lol.hub.headlessbot.behaviour.State;
import lol.hub.headlessbot.behaviour.nodes.Node;

/*   parent 0-1 child   */
public final class RootNode extends DecoratorNode {

    public RootNode(Node child) {
        super(child);
    }

    @Override
    public State tick() {
        return child().tick();
    }

    @Override
    public void parent(Node parent) {
        throw new IllegalStateException("root node can not have parent");
    }
}
