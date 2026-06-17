package us.potatoboy.worldborderfix;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.minecraft.world.World;
import net.minecraft.world.border.WorldBorder;

public class MultiWorldBorder implements ModInitializer {
	@Override
	public void onInitialize() {
		// Fires for every ServerWorld, including Fantasy runtime dimensions created by
		// Multiworld (the published Fantasy lib invokes this event when adding a world).
		ServerWorldEvents.LOAD.register((server, world) -> {
			WorldBorder border = world.getWorldBorder();

			if (world.getRegistryKey() != World.OVERWORLD) {
				WorldBorderState state = world.getPersistentStateManager()
					.getOrCreate(WorldBorderState.TYPE, WorldBorderState.ID);

				border.setCenter(state.getCenterX(), state.getCenterZ());
				border.setSize(state.getSize());
				border.setSafeZone(state.getBuffer());
				border.setDamagePerBlock(state.getDamagePerBlock());
				border.setWarningBlocks(state.getWarningBlocks());
				border.setWarningTime(state.getWarningTime());
			}

			border.addListener(new PerWorldBorderListener(world));
		});
	}
}
