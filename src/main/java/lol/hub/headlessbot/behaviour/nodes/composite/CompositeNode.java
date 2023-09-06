package lol.hub.headlessbot.behaviour.nodes.composite;

import lol.hub.headlessbot.behaviour.nodes.Node;

import java.util.List;

/*   parent 1-* children   */
public abstract class CompositeNode extends Node {
    final List<Node> children;

    public CompositeNode(Node... children) {
        this.children = List.of(children);
    }
}
