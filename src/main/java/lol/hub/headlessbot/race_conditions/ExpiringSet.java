package lol.hub.headlessbot.race_conditions;

import org.jetbrains.annotations.NotNull;

import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Automatically clears expired entries on access.
 * <p>
 * Backed by a ConcurrentHashMap.
 * <p>
 * Only timings equal or greater then 5 milliseconds are supported!
 *
 * @param <T> Type of entries.
 */
public class ExpiringSet<T> implements Set<T> {

    private final Map<T, ExpiringFlag> map = new ConcurrentHashMap<>();

    private final int ttl;
    private final ChronoUnit unit;

    /**
     * Only timings equal or greater then 5 milliseconds are supported!
     *
     * @param ttl  Time value until expiration
     * @param unit Time unit
     */
    public ExpiringSet(int ttl, ChronoUnit unit) {
        if ((unit == ChronoUnit.NANOS && ttl < 5_000_000)
            || (unit == ChronoUnit.MICROS && ttl < 5_000)
            || (unit == ChronoUnit.MILLIS && ttl < 5))
            throw new IllegalArgumentException("ExpiringBoolean timings smaller then 5 milliseconds are not supported due to inconsistency!");
        this.ttl = ttl;
        this.unit = unit;
    }

    private void removeExpired() {
        for (Map.Entry<T, ExpiringFlag> entry : map.entrySet()) {
            if (!entry.getValue().isActive()) {
                map.remove(entry.getKey());
            }
        }
    }

    @Override
    public boolean add(final @NotNull T element) {
        add(element, new ExpiringFlag(ttl, unit));
        // set always changes because of new timeout
        return true;
    }

    public boolean add(final @NotNull T element, final @NotNull ExpiringFlag flag) {
        map.put(element, flag);
        // set always changes because of new timeout
        return true;
    }

    @Override
    public boolean addAll(final @NotNull Collection<? extends T> collection) {
        addAll(collection, new ExpiringFlag(ttl, unit));
        // set always changes because of new timeout
        return true;
    }

    public boolean addAll(final @NotNull Collection<? extends T> collection, final @NotNull ExpiringFlag flag) {
        for (T element : collection) add(element, flag);
        // set always changes because of new timeout
        return true;
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public int size() {
        removeExpired();
        return map.keySet().size();
    }

    @Override
    public boolean isEmpty() {
        removeExpired();
        return map.isEmpty();
    }

    @Override
    public boolean contains(final @NotNull Object element) {
        removeExpired();
        //noinspection SuspiciousMethodCalls
        return map.containsKey(element);
    }

    @Override
    public boolean containsAll(final @NotNull Collection<?> collection) {
        removeExpired();
        for (Object element : collection) if (!contains(element)) return false;
        return true;
    }

    @Override
    public boolean remove(final @NotNull Object element) {
        removeExpired();
        int size = map.size();
        map.remove(element);
        return size != map.size();
    }

    @Override
    public boolean removeAll(final @NotNull Collection<?> collection) {
        removeExpired();
        boolean changed = false;
        for (Object element : collection) if (remove(element)) changed = true;
        return changed;
    }

    @Override
    public boolean retainAll(final @NotNull Collection<?> collection) {
        removeExpired();
        int size = map.size();
        for (T element : map.keySet()) if (!collection.contains(element)) map.remove(element);
        return size != map.size();
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        removeExpired();
        return map.keySet().iterator();
    }

    @NotNull
    @Override
    public Object @NotNull [] toArray() {
        removeExpired();
        return map.keySet().toArray();
    }

    @NotNull
    @Override
    public <U> U @NotNull [] toArray(final @NotNull U @NotNull [] array) {
        removeExpired();
        return map.keySet().toArray(array);
    }

}
