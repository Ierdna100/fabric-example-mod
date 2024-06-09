package college.andrei.mixinHelpers.dto;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;

public record DeathMessageReturn(String key, Entity killer, ItemStack killItem) {
}
