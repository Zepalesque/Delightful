package net.brnbrd.delightful.common.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.List;

// Item is enabled if tag is not empty
public class ReverseTagItem extends DItem {
	private final @NotNull TagKey<Item> tag;

	public ReverseTagItem(Properties pProperties, @NotNull TagKey<Item> tag) {
		super(pProperties);
		this.tag = tag;
	}

	@Override
	public boolean enabled() {
		return this.isTag() && super.enabled();
	}

	public boolean isTag() {
		var tags = ForgeRegistries.ITEMS.tags();
		return tags != null && tags.isKnownTagName(this.tag) && !tags.getTag(this.tag).isEmpty();
	}

	@Override
	public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> comps, @NotNull TooltipFlag pIsAdvanced) {
		if (super.enabled() && !this.isTag()) {
			comps.add(Component.translatable("tooltip.requires_tag"));
			comps.add(Component.literal(this.tag.location().toString()).withStyle(ChatFormatting.UNDERLINE));
		} else {
			super.appendHoverText(stack, level, comps, pIsAdvanced);
		}
	}
}