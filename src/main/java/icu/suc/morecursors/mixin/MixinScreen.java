package icu.suc.morecursors.mixin;

import icu.suc.morecursors.Cursors;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(Screen.class)
public abstract class MixinScreen {
    @Unique
    private static long CURRENT_CURSOR;

    @Unique
    private static void setCursor(long handle, long cursor) {
        if (Objects.equals(cursor, 0) || Objects.equals(cursor, CURRENT_CURSOR)) {
            return;
        }
        GLFW.glfwSetCursor(handle, cursor);
        CURRENT_CURSOR = cursor;
    }

    @Unique
    private static boolean adjustCursor(long handle, Element element) {
        if (element instanceof ClickableWidget clickable && clickable.visible && clickable.isHovered()) {
            if (!clickable.active) {
                setCursor(handle, Cursors.NOT_ALLOWED_CURSOR);
            } else if (clickable instanceof TextFieldWidget) {
                setCursor(handle, Cursors.IBEAM_CURSOR);
            } else {
                setCursor(handle, Cursors.HAND_CURSOR);
            }
            return true;
        }
        return false;
    }

    @Inject(method = "render", at = @At("TAIL"))
    private void modifyCursor(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        var mc = MinecraftClient.getInstance();
        var handle = mc.getWindow().getHandle();

        if (Objects.equals(handle, 0)) {
            return;
        }

        var screen = MinecraftClient.getInstance().currentScreen;

        if (Objects.isNull(screen)) {
            return;
        }

        for (int i = screen.children().size() - 1; i >= 0; i--) {
            if (adjustCursor(handle, screen.children().get(i))) {
                return;
            }
        }

        setCursor(handle, Cursors.ARROW_CURSOR);
    }
}
