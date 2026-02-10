package cn.kuzuanpa.kubicdivers.stratagem.common.stratagem;

import cn.kuzuanpa.kubicdivers.stratagem.common.entity.BeaconEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface IStratagem {
    String getId();
    String getName();
    List<Direction> getSequence();
    int getCooldown();
    /**Type, only used in beacon color currently**/
    StratagemType getType();
    /**duration, used by orbital barrage**/
    default @Nullable ResourceLocation getIcon(){return null;}
    default int getDuration(){return 0;}
    default int getActivationDelay() { return 100; }
    default int getPreActivateTime() { return 40; }
    default void preActivate(Level level, Vec3 targetPos, BeaconEntity beacon) {}
    default void activate(Level level, Vec3 targetPos, BeaconEntity beacon) {}
    default void tick(Level level, Vec3 targetPos, BeaconEntity beacon, int tick) {}
    enum Direction {
        UP, DOWN, LEFT, RIGHT;
        public static Direction fromKeyCode(int keyCode) {
            return switch(keyCode) {
                case 265, 87 -> UP;
                case 264, 83 -> DOWN;
                case 263, 65 -> LEFT;
                case 262, 68 -> RIGHT;
                default -> null;
            };
        }

        public String getDisplaySymbol() {
            return switch(this) {
                case UP -> "↑";
                case DOWN -> "↓";
                case LEFT -> "←";
                case RIGHT -> "→";
            };
        }
    }
    enum StratagemType {
        HELLPOD("Hellpod", 0x0000FF, true),
        STRIKE("Orbital", 0xFF0000, false);

        private final String displayName;
        private final int color;

        StratagemType(String displayName, int color, boolean requiresHellpod) {
            this.displayName = displayName;
            this.color = color;
        }

        public String getDisplayName() { return displayName; }
        public int getColor() { return color; }
    }
}