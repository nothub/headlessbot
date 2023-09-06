package lol.hub.headlessbot.behaviour.nodes.decorators;

import lol.hub.headlessbot.behaviour.nodes.Node;

public class RepeaterNode extends DecoratorNode {

    public RepeaterNode(Node parent, Node child, boolean untilFail) {
        super(parent, child);
    }
}
