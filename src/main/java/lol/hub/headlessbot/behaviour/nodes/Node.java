package lol.hub.headlessbot.behaviour.nodes;

import lol.hub.headlessbot.behaviour.Tickable;

public abstract class Node implements Tickable {
    private Node parent = null;

    public Node parent() {
        return parent;
    }

    public void parent(Node parent) {
        if (this.parent != null) throw new IllegalStateException("parent is already defined");
        this.parent = parent;
    }
}
