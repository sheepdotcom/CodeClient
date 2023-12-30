package dev.dfonline.codeclient.mixin;

import dev.dfonline.codeclient.CodeClient;
import dev.dfonline.codeclient.config.Config;
import dev.dfonline.codeclient.location.Creator;
import dev.dfonline.codeclient.location.Plot;
import dev.dfonline.codeclient.location.Spawn;
import dev.dfonline.codeclient.switcher.NodeSwitcher;
import dev.dfonline.codeclient.switcher.SpeedSwitcher;
import dev.dfonline.codeclient.switcher.StateSwitcher;
import net.minecraft.client.Keyboard;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Keyboard.class)
public class MKeyboard {
    @Unique
    private static final int DEBUG_KEY = GLFW.GLFW_KEY_F3;
    @Unique
    private static final int stateSwitcherKey = GLFW.GLFW_KEY_F4;
    @Unique
    private static final int speedSwitcherKey = GLFW.GLFW_KEY_F5;

    @Inject(method = "processF3", at = @At("HEAD"), cancellable = true)
    private void handleF3(int key, CallbackInfoReturnable<Boolean> cir) {
        if(key == stateSwitcherKey && Config.getConfig().EnableSwitchers) {
            if(CodeClient.location instanceof Plot && Config.getConfig().ModeSwitcher) {
                CodeClient.MC.setScreen(new StateSwitcher());
                cir.setReturnValue(true);
            }
            if(CodeClient.location instanceof Spawn && Config.getConfig().NodeSwitcher) {
                CodeClient.MC.setScreen(new NodeSwitcher());
                cir.setReturnValue(true);
            }
        }
        if(key == speedSwitcherKey) {
            if(CodeClient.location instanceof Creator) {
                CodeClient.MC.setScreen(new SpeedSwitcher());
                cir.setReturnValue(true);
            }
        }
    }
}
