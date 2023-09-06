package lol.hub.headlessbot.behaviour.nodes.composites;

import lol.hub.headlessbot.behaviour.State;
import lol.hub.headlessbot.behaviour.nodes.Node;

// OR
public class SelectorNode extends CompositeNode {
    public SelectorNode(Node... children) {
        super(children);
    }

    @Override
    public State run() {
        for (Node child : children) {
            if (child.run() == State.SUCCESS) return State.SUCCESS;
        }
        return State.FAILURE;
    }
}
