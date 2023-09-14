package lol.hub.headlessbot.behavior.nodes.composites;

import lol.hub.headlessbot.behavior.nodes.Node;

import java.util.List;

/*   parent 1-* children   */
public abstract class CompositeNode extends Node {
    private final List<Node> children;

    public CompositeNode(Node... children) {
        if (children.length == 0) throw new IllegalStateException("composite node without children");
        this.children = List.of(children);
    }

    public List<Node> children() {
        return children;
    }
}
