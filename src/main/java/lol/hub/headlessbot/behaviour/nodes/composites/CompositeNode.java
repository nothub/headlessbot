package lol.hub.headlessbot.behaviour.nodes.composites;

import lol.hub.headlessbot.behaviour.nodes.Node;

import java.util.List;

/*   parent 1-* children   */
public abstract class CompositeNode extends Node {
    private final List<Node> children;
    private int index;

    public CompositeNode(Node... children) {
        if (children.length == 0) throw new IllegalStateException("composite node without children");
        this.children = List.of(children);
        this.index = 0;
    }

    public List<Node> children() {
        return children;
    }

    public Node next() {
        return children.get(index++);
    }

    public boolean isEnd() {
        return index >= children().size();
    }

    public void reset() {
        index = 0;
    }
}
