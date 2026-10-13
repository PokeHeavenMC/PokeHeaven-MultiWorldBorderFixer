package us.potatoboy.worldborderfix.mixin;

import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import us.potatoboy.worldborderfix.BorderWithWorld;
import us.potatoboy.worldborderfix.WorldBorderState;

@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin {
    @Inject(method = "save", at = @At("HEAD"))
    private void saveBorder(CallbackInfo ci) {
        ServerWorld world = (ServerWorld) (Object) this;

        // Some worlds save before their border is restored (e.g. Multiworld saves from its world
        // constructor, before ServerWorldEvents.LOAD fires). Writing the default border then would
        // overwrite the saved one.
        if (!((BorderWithWorld) world.getWorldBorder()).isLoaded()) return;

        WorldBorderState worldBorderState = world.getPersistentStateManager().getOrCreate(WorldBorderState.TYPE, WorldBorderState.ID);
        worldBorderState.fromBorder(world.getWorldBorder());
    }
}
