package dev.dfonline.codeclient.mixin.render.hud;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.dfonline.codeclient.hypercube.item.Scope;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtString;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemRenderer.class)
public class MItemRenderer {
    @Inject(method = "renderGuiItemOverlay(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/item/ItemStack;IILjava/lang/String;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;push()V", shift = At.Shift.AFTER))
    private void additionalItemRendering(MatrixStack matrices, TextRenderer textRenderer, ItemStack stack, int x, int y, String countLabel, CallbackInfo ci) {
        NbtCompound nbt = stack.getNbt();
        if(nbt == null) return;
        NbtCompound pbv = (NbtCompound) nbt.get("PublicBukkitValues");
        if(pbv == null) return;
        NbtString varItem = (NbtString) pbv.get("hypercube:varitem");
        if(varItem == null) return;
        JsonObject var = JsonParser.parseString(varItem.asString()).getAsJsonObject();
        if(!var.get("id").getAsString().equals("var")) return;
        JsonObject data = var.get("data").getAsJsonObject();
        String scopeName = data.get("scope").getAsString();

        matrices.translate(0.0F, 0.0F, 200.0F);
        Scope scope = Scope.valueOf(scopeName);
        textRenderer.drawWithShadow(matrices,Text.literal(scope.shortName).formatted(scope.color),x,y,0xFFFFFF);
        matrices.translate(0.0F, 0.0F, -200.0F);
    }
}
