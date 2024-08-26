package com.tristankechlo.whatdidijustkill.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.tristankechlo.whatdidijustkill.config.types.ToastTheme;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.network.chat.Component;

public abstract class AbstractEntityToast implements Toast {

    private final Component firstLine; // not null
    private final Component secondLine; // might be null
    protected int backgroundTextureOffsetY = ToastTheme.ADVANCEMENT.getOffsetY();
    protected int displayTime = 2000;
    protected boolean textShadow = true;

    protected AbstractEntityToast(Component firstLine, Component secondLine) {
        this.firstLine = firstLine;
        this.secondLine = secondLine;
    }

    @Override
    public Visibility render(PoseStack poseStack, ToastComponent parent, long displayTime) {
        if (!ToastHandler.toastsEnabled) {
            return Visibility.HIDE;
        }

        // render background texture
        RenderSystem.setShaderTexture(0, TEXTURE);
        GuiComponent.blit(poseStack, 0, 0, 0, this.backgroundTextureOffsetY, this.width(), this.height(), 256, 256);

        // draw entity texture
        this.renderEntityImage(poseStack);

        // draw text
        if (this.secondLine != null) {
            if (this.textShadow) {
                parent.getMinecraft().font.drawShadow(poseStack, this.secondLine, 30, 17, 16777215);
            }
            parent.getMinecraft().font.draw(poseStack, this.secondLine, 30, 17, 16777215);
        }
        int y = this.secondLine == null ? 12 : 7;
        if (this.textShadow) {
            parent.getMinecraft().font.drawShadow(poseStack, this.firstLine, 30, y, 16777215);
        }
        parent.getMinecraft().font.draw(poseStack, this.firstLine, 30, y, 16777215);

        // remove toast when time is over
        return (double) displayTime >= this.displayTime ? Visibility.HIDE : Visibility.SHOW;
    }

    protected abstract void renderEntityImage(PoseStack graphics);

}
