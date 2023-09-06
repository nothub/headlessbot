package lol.hub.headlessbot.behaviour.nodes.composite;

import lol.hub.headlessbot.behaviour.nodes.Node;

import java.util.List;

public class SequenceNode extends CompositeNode {
    public final List<Node> children;

    public SequenceNode(Node parent, Node... children) {
        super(parent);
        this.children = List.of(children);
    }
}
