package us.potatoboy.worldborderfix.mixin;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.WorldGenerationProgressListener;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.border.WorldBorderListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import us.potatoboy.worldborderfix.MultiWorldBorder;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin {
	@Shadow
	public abstract ServerWorld getOverworld();

	@Redirect(method = "createWorlds", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/border/WorldBorder;addListener(Lnet/minecraft/world/border/WorldBorderListener;)V"))
	private void addListener(WorldBorder worldBorder, WorldBorderListener listener) {}

	@Redirect(method = "createWorlds", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/PlayerManager;setMainWorld(Lnet/minecraft/server/world/ServerWorld;)V"))
	private void setBorderListeners(PlayerManager playerManager, ServerWorld world) {}

	// Vanilla loads the overworld border from level.dat after ServerWorldEvents.LOAD has fired,
	// so the overworld is restored here instead of in the LOAD handler.
	@Inject(method = "createWorlds", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/border/WorldBorder;load(Lnet/minecraft/world/border/WorldBorder$Properties;)V", shift = At.Shift.AFTER))
	private void loadOverworldBorder(WorldGenerationProgressListener worldGenerationProgressListener, CallbackInfo ci) {
		MultiWorldBorder.loadBorder(getOverworld(), false);
	}
}
