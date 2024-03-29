package com.tristankechlo.whatdidijustkill.client;

import com.tristankechlo.whatdidijustkill.config.types.ToastTheme;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public abstract class AbstractEntityToast implements Toast {

    private final Component firstLine; // not null
    private final Component secondLine; // might be null
    protected ResourceLocation backgroundTexture = ToastTheme.ADVANCEMENT.getBackgroundTexture();
    protected int displayTime = 2000;
    protected boolean textShadow = true;

    protected AbstractEntityToast(Component firstLine, Component secondLine) {
        this.firstLine = firstLine;
        this.secondLine = secondLine;
    }

    @Override
    public Visibility render(GuiGraphics graphics, ToastComponent parent, long displayTime) {
        // render background texture
        graphics.blitSprite(this.backgroundTexture, 0, 0, this.width(), this.height());

        // draw entity texture
        this.renderEntityImage(graphics);

        // draw text
        if (this.secondLine != null) {
            graphics.drawString(parent.getMinecraft().font, this.secondLine, 30, 17, 16777215, this.textShadow);
        }
        int y = this.secondLine == null ? 12 : 7;
        graphics.drawString(parent.getMinecraft().font, this.firstLine, 30, y, 16777215, this.textShadow);

        // remove toast when time is over
        return (double) displayTime >= this.displayTime * parent.getNotificationDisplayTimeMultiplier()
                ? Visibility.HIDE
                : Visibility.SHOW;
    }

    protected abstract void renderEntityImage(GuiGraphics graphics);

}
