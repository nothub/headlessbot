package not.hub.headlessbot.fsm;

import java.util.function.Predicate;

public class DtDecision<P> extends DtNode {

    private final Predicate<P> predicate;
    private DtNode y;
    private DtNode n;

    public DtDecision(String name, Predicate<P> predicate) {
        super(name);
        this.predicate = predicate;
    }

    public DtDecision(String name, Predicate<P> predicate, DtNode y, DtNode n) {
        this(name, predicate);
        this.y = y;
        this.n = n;
    }

    public void setY(DtNode y) {
        this.y = y;
    }

    public void setN(DtNode n) {
        this.n = n;
    }

    public final DtNode decide(P input) {
        if (y == null || n == null) throw new IllegalArgumentException("Child node missing!");
        return predicate.test(input) ? y : n;
    }

    @Override
    public Boolean isLeaf() {
        return false;
    }

}
