package us.potatoboy.worldborderfix.mixin;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.border.WorldBorderListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin {
	@Redirect(method = "createWorlds", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/border/WorldBorder;addListener(Lnet/minecraft/world/border/WorldBorderListener;)V"))
	private void addListener(WorldBorder worldBorder, WorldBorderListener listener) {}

	@Redirect(method = "createWorlds", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/PlayerManager;setMainWorld(Lnet/minecraft/server/world/ServerWorld;)V"))
	private void setBorderListeners(PlayerManager playerManager, ServerWorld world) {}
}
