package not.hub.headlessbot;

import cc.neckbeard.utils.ExpiringFlag;

import java.time.temporal.ChronoUnit;

public class Cooldowns {

    public static final ExpiringFlag CONNECT = new ExpiringFlag(20, ChronoUnit.SECONDS, false);
    public static final ExpiringFlag BARITONE = new ExpiringFlag(5, ChronoUnit.SECONDS, false);

    public static void await(ExpiringFlag flag, boolean reset) {
        while (flag.isValid()) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException ex) {
                Log.error(Cooldowns.class, ex.getMessage());
            }
        }
        if (reset) flag.reset();
    }

}
