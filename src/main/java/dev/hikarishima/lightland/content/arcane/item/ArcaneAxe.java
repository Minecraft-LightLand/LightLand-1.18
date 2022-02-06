package dev.hikarishima.lightland.content.arcane.item;

import dev.hikarishima.lightland.content.arcane.internal.Arcane;
import dev.hikarishima.lightland.content.arcane.internal.ArcaneItemCraftHelper;
import dev.hikarishima.lightland.content.arcane.internal.ArcaneItemUseHelper;
import dev.hikarishima.lightland.content.arcane.internal.IArcaneItem;
import dev.hikarishima.lightland.content.common.capability.LLPlayerData;
import dev.lcy0x1.base.Proxy;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
public class ArcaneAxe extends AxeItem implements IArcaneItem {

    private final int mana;

    public ArcaneAxe(Tier tier, float attack, float speed, Properties props, int mana) {
        super(tier, attack, speed, props);
        this.mana = mana;
    }

    public static void add(ItemStack stack, List<Component> list) {
        List<Arcane> arcane = ArcaneItemCraftHelper.getAllArcanesOnItem(stack);
        Player pl = Proxy.getPlayer();
        LLPlayerData handler = pl == null ? null : LLPlayerData.get(pl);
        for (Arcane a : arcane) {
            boolean red = handler != null && !handler.magicAbility.isArcaneTypeUnlocked(a.type);
            TranslatableComponent text = a.type.getDesc();
            if (red)
                text.withStyle(ChatFormatting.RED);
            list.add(text.append(": ").append(a.getDesc()));
        }
    }



    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> list, TooltipFlag flag) {
        add(stack, list);
    }

    public boolean isFoil(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if (tag != null && tag.getBoolean("foil"))
            return true;
        return ArcaneItemUseHelper.isAxeCharged(stack);
    }

    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity user) {
        return true;
    }

    public boolean mineBlock(ItemStack stack, Level w, BlockState state, BlockPos pos, LivingEntity user) {
        return true;
    }

    public boolean showDurabilityBar(ItemStack stack) {
        return true;
    }

    public double getDurabilityForDisplay(ItemStack stack) {
        return 1 - 1.0 * ArcaneItemUseHelper.getArcaneMana(stack) / getMaxMana(stack);
    }

    public int getRGBDurabilityForDisplay(ItemStack stack) {
        return 0xFFFFFF;
    }

    @Override
    public int getMaxMana(ItemStack stack) {
        return mana;
    }

}
