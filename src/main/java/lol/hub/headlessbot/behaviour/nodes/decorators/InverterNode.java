package lol.hub.headlessbot.behaviour.nodes.decorators;

import lol.hub.headlessbot.behaviour.State;
import lol.hub.headlessbot.behaviour.nodes.Node;

import static lol.hub.headlessbot.behaviour.State.FAILURE;
import static lol.hub.headlessbot.behaviour.State.SUCCESS;

// NOT
public class InverterNode extends DecoratorNode {
    public InverterNode(Node child) {
        super(child);
    }

    @Override
    public State tick() {
        switch (child().tick()) {
            case SUCCESS -> {
                return FAILURE;
            }
            case FAILURE -> {
                return SUCCESS;
            }
        }
        throw new IllegalStateException(String.format("child node state must be SUCCESS or FAILURE but was %s", child().tick().name()));
    }
}
