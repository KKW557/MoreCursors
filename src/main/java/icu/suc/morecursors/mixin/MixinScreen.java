package icu.suc.morecursors.mixin;

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
    private static Long ARROW_CURSOR;
    @Unique
    private static Long HAND_CURSOR;
    @Unique
    private static Long NOT_ALLOWED_CURSOR;
    @Unique
    private static Long IBEAM_CURSOR;

    @Unique
    private static void setCursor(long handle, long cursor) {
        if (Objects.equals(cursor, 0)) {
            return;
        }
        GLFW.glfwSetCursor(handle, cursor);
    }

    @Unique
    private static boolean adjustCursor(long handle, Element element) {
        if (Objects.isNull(ARROW_CURSOR)) {
            ARROW_CURSOR = GLFW.glfwCreateStandardCursor(GLFW.GLFW_ARROW_CURSOR);
        }
        if (Objects.isNull(HAND_CURSOR)) {
            HAND_CURSOR = GLFW.glfwCreateStandardCursor(GLFW.GLFW_HAND_CURSOR);
        }
        if (Objects.isNull(NOT_ALLOWED_CURSOR)) {
            NOT_ALLOWED_CURSOR = GLFW.glfwCreateStandardCursor(GLFW.GLFW_NOT_ALLOWED_CURSOR);
        }
        if (Objects.isNull(IBEAM_CURSOR)) {
            IBEAM_CURSOR = GLFW.glfwCreateStandardCursor(GLFW.GLFW_IBEAM_CURSOR);
        }
        if (element instanceof ClickableWidget clickable && clickable.visible && clickable.isHovered()) {
            if (!clickable.active) {
                setCursor(handle, NOT_ALLOWED_CURSOR);
            } else if (clickable instanceof TextFieldWidget) {
                setCursor(handle, IBEAM_CURSOR);
            } else {
                setCursor(handle, HAND_CURSOR);
            }
            return true;
        }
        return false;
    }

    @Inject(method = "render", at = @At("TAIL"))
    private void modifyCursor(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        var mc = MinecraftClient.getInstance();
        var handle = mc.getWindow().getHandle();
        var screen = MinecraftClient.getInstance().currentScreen;

        if (screen == null) {
            return;
        }

        boolean hovered = false;
        for (Element element : screen.children()) {
            hovered = adjustCursor(handle, element) || hovered;
        }

        if (!hovered) {
            setCursor(handle, ARROW_CURSOR);
        }
    }
}
