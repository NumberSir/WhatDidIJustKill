package com.tristankechlo.whatdidijustkill.client;

import com.tristankechlo.whatdidijustkill.config.types.ToastTheme;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public abstract class AbstractEntityToast implements Toast {

    private final Component firstLine; // not null
    private final Component secondLine; // might be null
    protected ResourceLocation backgroundTexture = ToastTheme.ADVANCEMENT.getBackgroundTexture();
    protected int displayTime = 2000;
    protected boolean textShadow = true;
    private Toast.Visibility wantedVisibility = Visibility.HIDE;

    protected AbstractEntityToast(Component firstLine, Component secondLine) {
        this.firstLine = firstLine;
        this.secondLine = secondLine;
    }

    @Override
    public void update(ToastManager toastManager, long displayTime) {
        if (!ToastHandler.toastsEnabled) {
            this.wantedVisibility = Visibility.HIDE;
            return;
        }
        // remove toast when time is over
        this.wantedVisibility = (double) displayTime >= this.displayTime * toastManager.getNotificationDisplayTimeMultiplier()
                ? Visibility.HIDE
                : Visibility.SHOW;
    }

    @Override
    public Visibility getWantedVisibility() {
        return this.wantedVisibility;
    }

    @Override
    public void render(GuiGraphics graphics, Font font, long displayTime) {
        // render background texture
        graphics.blitSprite(RenderType::guiTextured, this.backgroundTexture, 0, 0, this.width(), this.height());

        // draw entity texture
        this.renderEntityImage(graphics);

        // draw text
        if (this.secondLine != null) {
            graphics.drawString(font, this.secondLine, 30, 17, 16777215, this.textShadow);
        }
        int y = this.secondLine == null ? 12 : 7;
        graphics.drawString(font, this.firstLine, 30, y, 16777215, this.textShadow);
    }

    protected abstract void renderEntityImage(GuiGraphics graphics);

}
