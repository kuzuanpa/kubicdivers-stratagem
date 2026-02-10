package cn.kuzuanpa.kubicdivers.stratagem.client;

import cn.kuzuanpa.kubicdivers.stratagem.KubicdiversStratagemMod;
import cn.kuzuanpa.kubicdivers.stratagem.common.HotbarManager;
import cn.kuzuanpa.kubicdivers.stratagem.common.StratagemPlayerManager;
import cn.kuzuanpa.kubicdivers.stratagem.common.stratagem.IStratagem;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class StratagemInputHandler {
    private static StratagemInputHandler instance;

    private boolean isActive = false;
    private final List<IStratagem.Direction> currentInput = new ArrayList<>();
    private IStratagem selectedStratagem = null;
    private int inputTimeout = 0;
    private final int maxTimeout = 120;
    private final List<IStratagem> availableStratagems = new ArrayList<>();

    private int totalInputs = 0;
    private int successfulActivations = 0;

    public static StratagemInputHandler getInstance() {
        if (instance == null) {
            instance = new StratagemInputHandler();
        }
        return instance;
    }

    private StratagemInputHandler() {
        refreshAvailableStratagems();
    }

    public void refreshAvailableStratagems() {
        availableStratagems.clear();
        availableStratagems.addAll(StratagemPlayerManager.getAvailStratagem(Minecraft.getInstance().player));
        availableStratagems.sort(Comparator.comparingInt(a -> a.getSequence().size()));
    }

    public void onKeyInput(int key, int action) {
        if (!isActive) return;

        if (action != GLFW.GLFW_PRESS) {
            return;
        }

        IStratagem.Direction direction = IStratagem.Direction.fromKeyCode(key);
        if (direction == null) {
            return;
        }

        processDirectionInput(direction);
    }

    private void processDirectionInput(IStratagem.Direction direction) {
        currentInput.add(direction);
        totalInputs++;
        inputTimeout = maxTimeout;

        KubicdiversStratagemMod.LOGGER.debug("Input added: {}, Sequence length: {}",
                direction, currentInput.size());

        checkForCompleteMatch();

        playInputSound();
    }

    private void checkForCompleteMatch() {
        for (IStratagem stratagem : availableStratagems) {
            List<IStratagem.Direction> sequence = stratagem.getSequence();
            if (sequence.equals(currentInput)) {
                selectedStratagem = stratagem;
                successfulActivations++;

                KubicdiversStratagemMod.LOGGER.info("Complete match found: {} (Sequence length: {})",
                        stratagem.getName(), sequence.size());
                activateStratagem();
                return;
            }
        }

        if (!isValidPartialInput()) {
            KubicdiversStratagemMod.LOGGER.debug("Invalid input sequence, clearing");
            currentInput.clear();
            selectedStratagem = null;
            playErrorSound();
        } else {
            KubicdiversStratagemMod.LOGGER.debug("Valid partial input, continuing...");
        }
    }

    private boolean isValidPartialInput() {
        for (IStratagem stratagem : availableStratagems) {
            List<IStratagem.Direction> sequence = stratagem.getSequence();
            if (currentInput.size() <= sequence.size()) {
                boolean matches = true;
                for (int i = 0; i < currentInput.size(); i++) {
                    if (currentInput.get(i) != sequence.get(i)) {
                        matches = false;
                        break;
                    }
                }
                if (matches) return true;
            }
        }
        return false;
    }

    private void activateStratagem() {
        if (selectedStratagem != null && Minecraft.getInstance().player != null) {
            Player player = Minecraft.getInstance().player;

            KubicdiversStratagemMod.LOGGER.info("Activating stratagem: {}", selectedStratagem.getName());

            HotbarManager.replaceStratagemBall(player, selectedStratagem);

            playSuccessSound();

            currentInput.clear();
            inputTimeout = 0;
        }
    }

    public float getInputTimeoutProgress() {
        return inputTimeout > 0 ? (float)inputTimeout / maxTimeout : 0.0f;
    }

    private void playInputSound() {
        // TODO
    }

    private void playErrorSound() {
        // TODO
    }

    private void playSuccessSound() {
        // TODO
    }

    public void update() {
        if (inputTimeout > 0) {
            inputTimeout--;
            if (inputTimeout == 0 && !currentInput.isEmpty()) {
                KubicdiversStratagemMod.LOGGER.debug("Input timeout, clearing sequence");
                currentInput.clear();
                selectedStratagem = null;
                playErrorSound();
            }
        }
    }

    public void setActive(boolean active) {
        this.isActive = active;
        if (!active) {
            reset();
        }
    }

    public boolean isActive() {
        return isActive;
    }

    private void reset() {
        currentInput.clear();
        selectedStratagem = null;
        inputTimeout = 0;
    }

    public List<IStratagem.Direction> getCurrentInput() {
        return new ArrayList<>(currentInput);
    }

    public IStratagem getSelectedStratagem() {
        return selectedStratagem;
    }

    public List<IStratagem> getAvailableStratagems() {
        return availableStratagems;
    }

    //for stats...maybe we will make stats or not
    public int getTotalInputs() { return totalInputs; }
    public int getSuccessfulActivations() { return successfulActivations; }
    public float getSuccessRate() {
        return totalInputs > 0 ? (float)successfulActivations / totalInputs : 0.0f;
    }
}