package io.github.viciscat.rrvbugshowcase.client;

import cc.cassian.rrv.api.event.OverlayManagementEvents;
import cc.cassian.rrv.common.overlay.BlockingGuiComponent;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.resources.Identifier;
import net.minecraft.util.CommonColors;

public class RrvbugshowcaseClient implements ClientModInitializer {
    private static final Identifier IDENTIFIER = Identifier.fromNamespaceAndPath("rrv_bug", "moving_square");
    private final int squareSize = 50;
    private int squareY;
    private int squareDirection = 1;

    @Override
    public void onInitializeClient() {
        OverlayManagementEvents.registerExclusionArea((screen, instance, _) -> {
            if (!(screen instanceof InventoryScreen)) return;
            instance.setExclusionArea(new BlockingGuiComponent(IDENTIFIER, screen.width - squareSize - 10, squareY, squareSize, squareSize));
        });
        ScreenEvents.AFTER_INIT.register((_, screen, _, _) -> {
            if (!(screen instanceof InventoryScreen)) return;
            squareY = 50;
            squareDirection = 1;
            ScreenEvents.afterTick(screen).register(_ -> {
                squareY += squareDirection;
                if (squareDirection == 1 && squareY >= screen.height - 50) squareDirection = -1;
                else if (squareDirection == -1 && squareY <= 0) squareDirection = 1;
            });
            ScreenEvents.afterExtract(screen).register((_, graphics, _, _, _) -> graphics.fill(screen.width - squareSize - 10, squareY, screen.width - 5, squareY + 50, CommonColors.RED));
        });
    }
}
