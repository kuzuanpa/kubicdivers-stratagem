package cn.kuzuanpa.kubicdivers.stratagem.client;

import cn.kuzuanpa.kubicdivers.stratagem.common.PlayerCooldownManager;
import cn.kuzuanpa.kubicdivers.stratagem.common.stratagem.IStratagem;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.Mth;

import java.util.List;
import java.util.stream.Collectors;

public class StratagemHUD {
    private static final int HUD_WIDTH = 120;
    private static int HUDHeight = 220;

    private static final int STRATAGEM_ENTRY_HEIGHT = 24;

    private final Minecraft minecraft = Minecraft.getInstance();

    public void render(GuiGraphics guiGraphics, float partialTick) {
        StratagemInputHandler handler = StratagemInputHandler.getInstance();
        if (!handler.isActive()) return;
        HUDHeight = handler.getAvailableStratagems().size()*25;
        int x = 4, y = 4;

        renderHUDBackground(guiGraphics, x, y);

        
        renderHeader(guiGraphics, x, y, handler);

        renderStratagemList(guiGraphics, x, y + 2, handler);
    }

    private void renderHUDBackground(GuiGraphics guiGraphics, int x, int y) {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        guiGraphics.fill(x, y, x + HUD_WIDTH, y + HUDHeight, 0x90444444);

        int borderColor = 0xFF00AA00;
        int glowColor = 0x4000FF00;

        for (int i = 1; i < 3; i++) {
            guiGraphics.renderOutline(x - i, y - i, HUD_WIDTH + i*2, HUDHeight + i*2, glowColor);
        }

        guiGraphics.renderOutline(x, y, HUD_WIDTH, HUDHeight, borderColor);

        RenderSystem.disableBlend();
    }

    private void renderHeader(GuiGraphics guiGraphics, int x, int y, StratagemInputHandler handler) {
        if (!handler.getCurrentInput().isEmpty()) {
            float timeoutProgress = handler.getInputTimeoutProgress();
            int progressBarWidth = HUD_WIDTH-8;
            int progressBarX = x +4;

            guiGraphics.fill(progressBarX, y, progressBarX + progressBarWidth, y + 1, 0x80555555);

            int filledWidth = (int)(progressBarWidth * timeoutProgress);
            int progressColor = Mth.hsvToRgb(timeoutProgress * 0.33f, 1.0f, 1.0f);
            guiGraphics.fill(progressBarX, y, progressBarX + filledWidth, y + 1, 0xFF000000 | progressColor);
        }
    }

    private void renderStratagemList(GuiGraphics guiGraphics, int baseX, int baseY, StratagemInputHandler handler) {
        List<IStratagem> availableStratagems = handler.getAvailableStratagems();
        List<IStratagem.Direction> currentInput = handler.getCurrentInput();

        var stratagemsByType = availableStratagems.stream()
                .collect(Collectors.groupingBy(IStratagem::getType));

        int currentY = baseY;

        for (IStratagem.StratagemType type : IStratagem.StratagemType.values()) {
            List<IStratagem> typeStratagems = stratagemsByType.get(type);
            if (typeStratagems == null || typeStratagems.isEmpty()) continue;
            for (IStratagem stratagem : typeStratagems) {
                boolean isMatching = isStratagemMatchingInput(stratagem, currentInput);
                boolean isSelected = stratagem == handler.getSelectedStratagem();
                boolean isInCooldown = PlayerCooldownManager.isOnCooldown(minecraft.player, stratagem);
                boolean isAvailable = !isInCooldown && (currentInput.isEmpty() || isMatching || isSelected);

                int bgColor = 0x10000000;
                if (isSelected) bgColor = 0x4000AA00;
                else if (isMatching) bgColor = 0x20AAAA00;

                guiGraphics.fill(baseX + 2, currentY, baseX + HUD_WIDTH - 2 , currentY + STRATAGEM_ENTRY_HEIGHT, bgColor);

                int u = 0, v = 0;

                RenderSystem.enableBlend();
                if(stratagem.getIcon()!=null)guiGraphics.blit(stratagem.getIcon(),
                        baseX + 4, currentY + 3,
                        u, v, 16, 16, 16, 16);

                int nameColor = !isAvailable ? 0x888888 : isSelected ? 0x00FF00 : isMatching ? 0xFFFF00 : 0xFFFFFF;
                guiGraphics.drawString(minecraft.font, stratagem.getName(), baseX + 24, currentY + 3, nameColor, false);

                if(isInCooldown) {
                    String cooldown = (PlayerCooldownManager.getRemainingCooldownMillis(minecraft.player, stratagem)/ 1000) + "s";
                    guiGraphics.drawString(minecraft.font, cooldown, baseX + 24, currentY + 14, 0xAAAAAA, false);
                }else renderStratagemSequence(guiGraphics, stratagem, baseX + 24, currentY + 14, currentInput, isAvailable);

                if(!isAvailable)guiGraphics.fill(baseX + 2, currentY, baseX + HUD_WIDTH - 2 , currentY + STRATAGEM_ENTRY_HEIGHT, 0x66333333);

                currentY += STRATAGEM_ENTRY_HEIGHT;

                if (currentY > baseY + HUDHeight - 10) {
                    break;
                }
            }
        }
    }

    private boolean isStratagemMatchingInput(IStratagem stratagem, List<IStratagem.Direction> currentInput) {
        if (currentInput.isEmpty()) return false;

        List<IStratagem.Direction> sequence = stratagem.getSequence();
        if (currentInput.size() > sequence.size()) return false;

        for (int i = 0; i < currentInput.size(); i++) {
            if (currentInput.get(i) != sequence.get(i)) {
                return false;
            }
        }
        return true;
    }

    private void renderStratagemSequence(GuiGraphics guiGraphics, IStratagem stratagem, int x, int y, List<IStratagem.Direction> currentInput, boolean isAvailable) {
        List<IStratagem.Direction> sequence = stratagem.getSequence();

        for (int i = 0; i < sequence.size(); i++) {
            IStratagem.Direction dir = sequence.get(i);
            String symbol = dir.getDisplaySymbol();

            int color;
            if(!isAvailable)color = 0x777777;
            else if (i < currentInput.size()) color = 0x00FF00;
            else color = 0xCCCCCC;


            guiGraphics.drawString(minecraft.font, symbol, x + i * 12, y, color, false);
        }
    }
}