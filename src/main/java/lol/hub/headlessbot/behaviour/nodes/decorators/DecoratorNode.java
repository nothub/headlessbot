package lol.hub.headlessbot.behaviour.nodes.decorators;

import lol.hub.headlessbot.behaviour.nodes.Node;

/*   parent 1-1 child   */
public abstract class DecoratorNode extends Node {
    final Node child;

    public DecoratorNode(Node child) {
        this.child = child;
    }
}
