package lol.hub.headlessbot.behaviour.nodes.composites;

import lol.hub.headlessbot.behaviour.State;
import lol.hub.headlessbot.behaviour.nodes.Node;

import static lol.hub.headlessbot.behaviour.State.FAILURE;
import static lol.hub.headlessbot.behaviour.State.SUCCESS;

/**
 * AND
 * Execute children in order, all in one tick, until a child returns failure.
 */
public class SequenceAllNode extends CompositeNode {
    public SequenceAllNode(Node... children) {
        super(children);
    }

    @Override
    public synchronized State tick() {
        for (Node child : children()) {
            if (child.tick() == FAILURE) {
                return FAILURE;
            }
        }
        return SUCCESS;
    }
}
