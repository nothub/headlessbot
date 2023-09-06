package lol.hub.headlessbot.behaviour.nodes.decorators;

import lol.hub.headlessbot.behaviour.nodes.Node;

public abstract class DecoratorNode extends Node {
    public final Node child;

    public DecoratorNode(Node parent, Node child) {
        super(parent);
        this.child = child;
    }
}
