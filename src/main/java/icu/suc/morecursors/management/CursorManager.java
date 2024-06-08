package icu.suc.morecursors.management;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.Window;
import org.lwjgl.glfw.GLFW;

public class CursorManager {

    private final Window window;
    private long handle;
    private long cursor;

    public static long ARROW_CURSOR = GLFW.glfwCreateStandardCursor(GLFW.GLFW_ARROW_CURSOR);
    public static long HAND_CURSOR = GLFW.glfwCreateStandardCursor(GLFW.GLFW_HAND_CURSOR);
    public static long NOT_ALLOWED_CURSOR = GLFW.glfwCreateStandardCursor(GLFW.GLFW_NOT_ALLOWED_CURSOR);
    public static long IBEAM_CURSOR = GLFW.glfwCreateStandardCursor(GLFW.GLFW_IBEAM_CURSOR);

    public CursorManager(MinecraftClient client) {
        this.window = client.getWindow();
        this.handle = window.getHandle();
        this.cursor = ARROW_CURSOR;
    }

    private boolean shouldSet(long cursor) {
        if (this.handle != this.window.getHandle()) {
            return true;
        }

        return this.cursor != cursor;
    }

    public void setCursor(long cursor) {
        if (shouldSet(cursor)) {
            this.handle = window.getHandle();
            this.cursor = cursor;
            GLFW.glfwSetCursor(this.handle, cursor);
        }
    }

    public boolean autoSet(Element element) {

        if (element instanceof ClickableWidget && ((ClickableWidget) element).visible && ((ClickableWidget) element).isHovered()) {

            if (!((ClickableWidget) element).active) {
                setCursor(CursorManager.NOT_ALLOWED_CURSOR);
            } else if (element instanceof TextFieldWidget) {
                setCursor(CursorManager.IBEAM_CURSOR);
            } else {
                setCursor(CursorManager.HAND_CURSOR);
            }

            return true;
        }

        return false;
    }
}
