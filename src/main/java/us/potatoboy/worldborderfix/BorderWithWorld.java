package us.potatoboy.worldborderfix;

import net.minecraft.world.World;

public interface BorderWithWorld {
	World getWorld();

	void setWorld(World world);

	/**
	 * Whether the border has been restored from its saved state. Until then it holds default values
	 * and must not be written back to disk, or it would overwrite the saved border.
	 */
	boolean isLoaded();

	void setLoaded(boolean loaded);
}
