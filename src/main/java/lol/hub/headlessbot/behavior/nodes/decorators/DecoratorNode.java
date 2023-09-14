package lol.hub.headlessbot.behavior.nodes.decorators;

import lol.hub.headlessbot.behavior.nodes.Node;

/*   parent 1-1 child   */
public abstract class DecoratorNode extends Node {
    private final Node child;

    public DecoratorNode(Node child) {
        this.child = child;
    }

    public Node child() {
        return child;
    }
}
