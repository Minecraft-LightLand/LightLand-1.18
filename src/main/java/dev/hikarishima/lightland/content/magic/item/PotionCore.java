package dev.hikarishima.lightland.content.magic.item;

import dev.hikarishima.lightland.init.data.LangData;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
public class PotionCore extends Item {

    public PotionCore(Item.Properties props) {
        super(props.stacksTo(1));
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> list, TooltipFlag flag) {
        list.add(MagicScroll.getTarget(stack).text());
        list.add(LangData.IDS.POTION_RADIUS.get(MagicScroll.getRadius(stack)));
        PotionUtils.addPotionTooltip(stack, list, 1);
    }

    public boolean isFoil(ItemStack stack) {
        return true;
    }

}
