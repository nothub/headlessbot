package lol.hub.headlessbot.behaviour;

import lol.hub.headlessbot.behaviour.nodes.decorators.RepeatNode;
import lol.hub.headlessbot.behaviour.nodes.decorators.RootNode;
import lol.hub.headlessbot.behaviour.nodes.leafs.LeafNode;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static lol.hub.headlessbot.behaviour.State.RUNNING;
import static lol.hub.headlessbot.behaviour.State.SUCCESS;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TreeTests {
    @Test
    void simple() {
        var tree = new BehaviourTree(new RootNode(new LeafNode() {
            @Override
            public State tick() {
                return SUCCESS;
            }
        }));

        assertEquals(tree.tick(), SUCCESS);
    }

    @Test
    void repeat() {
        AtomicInteger i = new AtomicInteger();
        var tree = new BehaviourTree(new RootNode(new RepeatNode(new LeafNode() {
            @Override
            public State tick() {
                i.incrementAndGet();
                return SUCCESS;
            }
        }, 3, false)));

        assertEquals(RUNNING, tree.tick());
        assertEquals(1, i.get());

        assertEquals(RUNNING, tree.tick());
        assertEquals(2, i.get());

        assertEquals(SUCCESS, tree.tick());
        assertEquals(3, i.get());
    }
}
