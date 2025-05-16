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

@Mixin(Screen.class)
public abstract class MixinScreen {
    @Unique
    private static final long ARROW_CURSOR = GLFW.glfwCreateStandardCursor(GLFW.GLFW_ARROW_CURSOR);
    @Unique
    private static final long HAND_CURSOR = GLFW.glfwCreateStandardCursor(GLFW.GLFW_HAND_CURSOR);
    @Unique
    private static final long NOT_ALLOWED_CURSOR = GLFW.glfwCreateStandardCursor(GLFW.GLFW_NOT_ALLOWED_CURSOR);
    @Unique
    private static final long IBEAM_CURSOR = GLFW.glfwCreateStandardCursor(GLFW.GLFW_IBEAM_CURSOR);

    @Unique
    private static boolean adjustCursor(long handle, Element element) {
        if (element instanceof ClickableWidget clickable && clickable.visible && clickable.isHovered()) {
            if (!clickable.active) {
                GLFW.glfwSetCursor(handle, NOT_ALLOWED_CURSOR);
            } else if (clickable instanceof TextFieldWidget) {
                GLFW.glfwSetCursor(handle, IBEAM_CURSOR);
            } else {
                GLFW.glfwSetCursor(handle, HAND_CURSOR);
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
            GLFW.glfwSetCursor(handle, ARROW_CURSOR);
        }
    }
}
