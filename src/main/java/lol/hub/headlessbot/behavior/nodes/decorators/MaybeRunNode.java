package lol.hub.headlessbot.behavior.nodes.decorators;

import lol.hub.headlessbot.behavior.State;
import lol.hub.headlessbot.behavior.nodes.Node;

import java.util.function.Supplier;

import static lol.hub.headlessbot.behavior.State.SUCCESS;

/**
 * IF
 * see ConditionNode
 */
public final class MaybeRunNode extends DecoratorNode {
    private final Supplier<Boolean> condition;

    public MaybeRunNode(Node child, Supplier<Boolean> condition) {
        super(child);
        this.condition = condition;
    }

    @Override
    public State tick() {
        if (condition.get()) return child().tick();
        return SUCCESS;
    }
}
