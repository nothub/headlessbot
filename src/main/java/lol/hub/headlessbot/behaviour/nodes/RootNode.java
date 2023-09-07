package lol.hub.headlessbot.behaviour.nodes;

import lol.hub.headlessbot.behaviour.State;

/*   parent 0-1 child   */
public final class RootNode extends Node {
    private final Node child;

    public RootNode(Node child) {
        this.child = child;
        child.parent(this);
    }

    @Override
    public State tick() {
        return child.tick();
    }

    @Override
    public void parent(Node parent) {
        throw new IllegalStateException("root node can not have parent");
    }
}
