package not.hub.headlessbot.util;

import net.minecraft.entity.player.EntityPlayer;

public class ChunkLocation {

    public final int x;
    public final int z;

    public ChunkLocation(int x, int z) {
        this.x = x;
        this.z = z;
    }

    public static ChunkLocation of(int x, int z) {
        return new ChunkLocation(x, z);
    }

    public static ChunkLocation of(EntityPlayer player) {
        return new ChunkLocation(player.chunkCoordX, player.chunkCoordZ);
    }

    public long key() {
        return key(x, z);
    }

    /**
     * @see <a href="https://papermc.io/javadocs/paper/1.12/org/bukkit/Chunk.html#getChunkKey--">org.bukkit.Chunk#getChunkKey()</a>
     */
    public static long key(int x, int z) {
        return (long) x & 4294967295L | ((long) z & 4294967295L) << 32;
    }

}
