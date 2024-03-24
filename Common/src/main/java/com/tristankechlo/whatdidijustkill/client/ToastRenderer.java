package com.tristankechlo.whatdidijustkill.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;

import java.util.LinkedList;

public class ToastRenderer {

    public static final LinkedList<Toast> TOASTS = new LinkedList<>();

    public static void render(GuiGraphics graphics, float partialTick) {
        if (TOASTS.isEmpty()) {
            return;
        }
        float scale = 1F;

        PoseStack poseStack = graphics.pose();
        poseStack.pushPose();
        poseStack.scale(scale, scale, scale);

        TOASTS.getFirst().render(graphics, partialTick);

        poseStack.popPose();
    }

}
