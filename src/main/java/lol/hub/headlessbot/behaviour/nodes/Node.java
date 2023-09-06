package lol.hub.headlessbot.behaviour.nodes;

import lol.hub.headlessbot.behaviour.State;

public abstract class Node {
    private Node parent = null;

    public abstract State run();

    public Node parent() {
        return parent;
    }

    public void parent(Node parent) {
        if (this.parent != null) throw new IllegalStateException("parent is already defined");
        this.parent = parent;
    }
}
