package college.andrei.mixin;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.SaveProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(MinecraftServer.class)
public interface MinecraftServerAccessor {
    @Accessor("timeReference")
    long getTimeReference();
}
