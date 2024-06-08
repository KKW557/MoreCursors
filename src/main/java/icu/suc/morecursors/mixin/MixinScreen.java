package icu.suc.morecursors.mixin;

import icu.suc.morecursors.MoreCursors;
import icu.suc.morecursors.management.CursorManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Screen.class)
public abstract class MixinScreen {

    @Inject(method = "render", at = @At("TAIL"))
    private void cursorShape(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {

        Screen screen = MinecraftClient.getInstance().currentScreen;

        if (screen == null) {
            return;
        }

        CursorManager cursorManager = MoreCursors.getInstance().getCursorManager();

        boolean hovered = false;
        for (Element element : screen.children()) {
            hovered = cursorManager.autoSet(element) || hovered;
        }

        if (!hovered) {
            cursorManager.setCursor(CursorManager.ARROW_CURSOR);
        }
    }
}
