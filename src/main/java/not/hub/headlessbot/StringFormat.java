package not.hub.headlessbot;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;

public class StringFormat {

    public static String of(EntityPlayer player) {
        return of(player.getPosition()) + " " + of(player.dimension);
    }

    public static String of(BlockPos position) {
        return position.getX() + "x " + position.getY() + "y " + position.getZ() + "z";
    }

    public static String of(int dim) {
        if (dim == 0) return "OVERWORLD";
        else if (dim == 1) return "END";
        else if (dim == -1) return "NETHER";
        else return "CUSTOM (" + dim + ")";
    }

}
