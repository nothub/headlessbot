package lol.hub.headlessbot.behaviour.nodes.composites;

import lol.hub.headlessbot.behaviour.State;
import lol.hub.headlessbot.behaviour.nodes.Node;

import static lol.hub.headlessbot.behaviour.State.*;

// OR
public class SelectorNode extends CompositeNode {
    public SelectorNode(Node... children) {
        super(children);
    }

    @Override
    public State tick() {
        switch (next().tick()) {
            case SUCCESS -> {
                reset();
                return SUCCESS;
            }
            case FAILURE -> {
                if (isEnd()) {
                    reset();
                    return FAILURE;
                }
            }
        }
        return RUNNING;
    }
}
