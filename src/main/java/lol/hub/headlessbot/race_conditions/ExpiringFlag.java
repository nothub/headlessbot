package lol.hub.headlessbot.race_conditions;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

/**
 * An ExpiringFlag is valid while its time to live was not exceeded.
 */
public class ExpiringFlag {

    private final int ttl;
    private final ChronoUnit unit;
    private Instant start;

    /**
     * Only timings equal or greater then 5 milliseconds are supported!
     *
     * @param ttl  Time value until expiration
     * @param unit Time unit
     */
    public ExpiringFlag(int ttl, ChronoUnit unit) {
        this(ttl, unit, true);
    }

    /**
     * Only timings equal or greater then 5 milliseconds are supported!
     *
     * @param ttl  Time value until expiration
     * @param unit Time unit
     * @param init Initial value state
     */
    public ExpiringFlag(int ttl, ChronoUnit unit, boolean init) {
        if ((unit == ChronoUnit.NANOS && ttl < 5_000_000)
            || (unit == ChronoUnit.MICROS && ttl < 5_000)
            || (unit == ChronoUnit.MILLIS && ttl < 5))
            throw new IllegalArgumentException("ExpiringBoolean timings smaller then 5 milliseconds are not supported due to inconsistency!");
        this.ttl = ttl;
        this.unit = unit;
        this.start = init ? Instant.now() : Instant.ofEpochMilli(0);
    }

    /**
     * @return Returns true if expired, returns false if still active
     */
    public boolean isExpired() {
        return start.plus(ttl, unit).isBefore(Instant.now());
    }

    /**
     * @return Returns true if still active, returns false if expired
     */
    public boolean isActive() {
        return !isExpired();
    }

    /**
     * Reset the flag, setting the start time to the current time.
     */
    public void reset() {
        this.start = Instant.now();
    }

    /**
     * Expire the flag, setting the start time to the far past.
     */
    public void expire() {
        this.start = Instant.MIN;
    }

}
