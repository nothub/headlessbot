package lol.hub.headlessbot.behaviour.nodes.composites;

import lol.hub.headlessbot.behaviour.State;
import lol.hub.headlessbot.behaviour.nodes.Node;

import static lol.hub.headlessbot.behaviour.State.*;

/**
 * AND
 * Execute children in order, one per tick, until a child returns failure.
 */
public final class SequenceNode extends CompositeNode {
    private int index = 0;

    public SequenceNode(Node... children) {
        super(children);
    }

    @Override
    public synchronized State tick() {
        switch (children().get(index++).tick()) {
            case SUCCESS -> {
                if (index >= children().size()) {
                    index = 0;
                    return SUCCESS;
                }
            }
            case FAILURE -> {
                index = 0;
                return FAILURE;
            }
        }
        return RUNNING;
    }
}
