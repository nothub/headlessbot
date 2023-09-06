package lol.hub.headlessbot.behaviour.nodes.composites;

import lol.hub.headlessbot.behaviour.State;
import lol.hub.headlessbot.behaviour.nodes.Node;

// AND
public class SequenceNode extends CompositeNode {
    public SequenceNode(Node... children) {
        super(children);
    }

    @Override
    public State run() {
        for (Node child : children) {
            if (child.run() == State.FAILURE) return State.FAILURE;
        }
        return State.SUCCESS;
    }
}
