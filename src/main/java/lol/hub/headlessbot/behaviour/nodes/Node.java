package lol.hub.headlessbot.behaviour.nodes;

import lol.hub.headlessbot.behaviour.State;

public abstract class Node {
    public final Node parent;

    public Node(Node parent) {
        this.parent = parent;
    }

    public abstract State run();
}
