package dev.dfonline.codeclient.mixin;

import dev.dfonline.codeclient.CodeClient;
import dev.dfonline.codeclient.DevClip;
import dev.dfonline.codeclient.action.impl.MoveToSpawn;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public class MClientPlayerEntity {
    @Shadow @Final public ClientPlayNetworkHandler networkHandler;

    private boolean lastSneaking = false;

    @Inject(method = "tick", at = @At("HEAD"))
    private void tick(CallbackInfo ci) {
        CodeClient.onTick();
        CodeClient.currentAction.onTick();
    }

    @Inject(method = "sendMovementPackets", at = @At("HEAD"), cancellable = true)
    private void sendMovementPackets(CallbackInfo ci) {
        if(CodeClient.currentAction instanceof MoveToSpawn mts) if(mts.moveModifier()) ci.cancel();
        if(DevClip.ignoresWalls()) {
        ci.cancel();
            Vec3d pos = DevClip.handleSeverPosition();
            ClientPlayerEntity player = CodeClient.MC.player;
            this.networkHandler.sendPacket(new PlayerMoveC2SPacket.Full(pos.x, pos.y, pos.z, player.getYaw(), player.getPitch(), false));

            boolean sneaking = player.isSneaking();
            if (sneaking != this.lastSneaking) {
                ClientCommandC2SPacket.Mode mode = sneaking ? ClientCommandC2SPacket.Mode.PRESS_SHIFT_KEY : ClientCommandC2SPacket.Mode.RELEASE_SHIFT_KEY;
                this.networkHandler.sendPacket(new ClientCommandC2SPacket(player, mode));
                this.lastSneaking = sneaking;
            }
        }
    }

    @Inject(method = "pushOutOfBlocks", at = @At("HEAD"), cancellable = true)
    private void onPushOutOfBlocks(double x, double z, CallbackInfo ci) {
        if(DevClip.ignoresWalls()) ci.cancel();
    }
}
