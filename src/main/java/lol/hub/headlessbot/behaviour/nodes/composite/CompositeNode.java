package lol.hub.headlessbot.behaviour.nodes.composite;

import lol.hub.headlessbot.behaviour.nodes.Node;

public abstract class CompositeNode extends Node {
    public CompositeNode(Node parent) {
        super(parent);
    }
}
