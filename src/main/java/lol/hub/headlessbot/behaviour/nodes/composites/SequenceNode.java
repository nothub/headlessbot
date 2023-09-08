package lol.hub.headlessbot.behaviour.nodes.composites;

import lol.hub.headlessbot.behaviour.State;
import lol.hub.headlessbot.behaviour.nodes.Node;

import static lol.hub.headlessbot.behaviour.State.*;

/**
 * AND
 * Execute children in order until a child returns failure.
 */
public final class SequenceNode extends CompositeNode {

    public SequenceNode(Node... children) {
        super(children);
    }

    @Override
    public State tick() {
        switch (next().tick()) {
            case SUCCESS -> {
                if (isEnd()) {
                    reset();
                    return SUCCESS;
                }
            }
            case FAILURE -> {
                reset();
                return FAILURE;
            }
        }
        return RUNNING;
    }
}
