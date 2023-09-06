package lol.hub.headlessbot.behaviour.nodes;

public abstract class Node {
    public final Node parent;

    public Node(Node parent) {
        this.parent = parent;
    }
}
