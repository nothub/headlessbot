package lol.hub.headlessbot.behavior.nodes.decorators;

import lol.hub.headlessbot.behavior.State;
import lol.hub.headlessbot.behavior.nodes.Node;

import java.util.function.Supplier;

import static lol.hub.headlessbot.behavior.State.FAILURE;

/**
 * IF
 * see MaybeRunNode
 */
public final class ConditionNode extends DecoratorNode {
    private final Supplier<Boolean> condition;

    public ConditionNode(Node child, Supplier<Boolean> condition) {
        super(child);
        this.condition = condition;
    }

    @Override
    public State tick() {
        if (condition.get()) return child().tick();
        return FAILURE;
    }
}
