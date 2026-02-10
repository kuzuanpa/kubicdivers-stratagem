package cn.kuzuanpa.kubicdivers.stratagem.client.event;

import cn.kuzuanpa.kubicdivers.stratagem.KubicdiversStratagemMod;
import cn.kuzuanpa.kubicdivers.stratagem.client.StratagemInputHandler;
import cn.kuzuanpa.kubicdivers.stratagem.common.HotbarManager;
import cn.kuzuanpa.kubicdivers.stratagem.common.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(modid = KubicdiversStratagemMod.MOD_ID, value = Dist.CLIENT)
public class InputEvents {
    private static boolean ctrlWasDown = false;
    private static boolean inStratagemMode = false;
    private static boolean hasActivatedBall = false;

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        int key = event.getKey();
        int action = event.getAction();

        Player player = Minecraft.getInstance().player;
        if (player == null) return;

        boolean ctrlDown = isCtrlKeyDown();

        if (ctrlDown != ctrlWasDown) {
            ctrlWasDown = ctrlDown;

            if (ctrlDown) {
                if (!HotbarManager.isPlayerInStratagemMode(player)) {
                    StratagemInputHandler.getInstance().setActive(true);
                    HotbarManager.moveStratagemSlot(player);
                    inStratagemMode = true;
                    hasActivatedBall = false;
                    KubicdiversStratagemMod.LOGGER.debug("Stratagem mode activated");
                }
            } else {
                if (inStratagemMode) {
                    if (!hasActivatedBall && StratagemInputHandler.getInstance().getSelectedStratagem() != null) {
                        hasActivatedBall = true;
                        KubicdiversStratagemMod.LOGGER.debug("Stratagem ball activated, ready to throw");
                    } else {
                        StratagemInputHandler.getInstance().setActive(false);
                        inStratagemMode = false;
                        hasActivatedBall = false;
                        player.getInventory().removeItem(8, 64);

                        if (HotbarManager.isPlayerInStratagemMode(player)) {
                            HotbarManager.deactivateStratagemMode(player);
                        }

                        KubicdiversStratagemMod.LOGGER.debug("Stratagem mode deactivated");
                    }
                }
            }
        }

        if (inStratagemMode && !hasActivatedBall) {
            if (key == GLFW.GLFW_KEY_W || key == GLFW.GLFW_KEY_A ||
                    key == GLFW.GLFW_KEY_S || key == GLFW.GLFW_KEY_D ||
                    key == GLFW.GLFW_KEY_UP || key == GLFW.GLFW_KEY_DOWN ||
                    key == GLFW.GLFW_KEY_LEFT || key == GLFW.GLFW_KEY_RIGHT) {
                StratagemInputHandler.getInstance().onKeyInput(key, action);
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onMouseInput(InputEvent.MouseButton event) {
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            StratagemInputHandler.getInstance().update();

            Player player = Minecraft.getInstance().player;
            if (player != null && inStratagemMode && hasActivatedBall) {
                if (player.getMainHandItem().getItem() != ModItems.STRATAGEM_BALL.get()) {
                    StratagemInputHandler.getInstance().setActive(false);
                    HotbarManager.deactivateStratagemMode(player);
                    inStratagemMode = false;
                    hasActivatedBall = false;

                    if (HotbarManager.isPlayerInStratagemMode(player)) {
                        HotbarManager.forceDeactivate(player);
                    }
                }
            }
        }
    }

    private static boolean isCtrlKeyDown() {
        long windowHandle = Minecraft.getInstance().getWindow().getWindow();
        return GLFW.glfwGetKey(windowHandle, GLFW.GLFW_KEY_LEFT_CONTROL) == GLFW.GLFW_PRESS ||
                GLFW.glfwGetKey(windowHandle, GLFW.GLFW_KEY_RIGHT_CONTROL) == GLFW.GLFW_PRESS;
    }
}