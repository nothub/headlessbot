package lol.hub.headlessbot.behaviour.nodes;

import lol.hub.headlessbot.behaviour.State;

/*   parent 0-1 child   */
public final class RootNode extends Node {
    final Node child;

    public RootNode(Node child) {
        this.child = child;
    }

    @Override
    public State run() {
        return child.run();
    }

    @Override
    public void parent(Node parent) {
        throw new IllegalStateException("root node can not have parent");
    }
}
