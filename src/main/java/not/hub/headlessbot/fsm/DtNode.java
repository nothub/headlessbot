package not.hub.headlessbot.fsm;

import java.util.function.Supplier;

public class DtNode {

    private final Runnable r;
    private final Supplier<Boolean> s;
    private DtNode y;
    private DtNode n;

    public DtNode(Runnable r) {
        this.r = r;
        this.s = null;
    }

    public DtNode(Supplier<Boolean> s) {
        this.r = null;
        this.s = s;
    }

    public DtNode(Supplier<Boolean> s, DtNode y, DtNode n) {
        this(s);
        this.y = y;
        this.n = n;
    }

    public void setY(DtNode y) {
        this.y = y;
    }

    public void setN(DtNode n) {
        this.n = n;
    }

    public DtNode next() {
        if (r != null) r.run();
        else if (s == null || y == null || n == null) throw new IllegalStateException("Invalid Node");
        else return s.get() ? y : n;
        return null;
    }

    public void walk() {
        DtNode n = this;
        while (n != null) {
            n = n.next();
        }
    }

}
