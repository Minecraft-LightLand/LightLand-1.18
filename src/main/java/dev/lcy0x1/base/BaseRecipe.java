package dev.lcy0x1.base;

import dev.lcy0x1.util.RecSerializer;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public abstract class BaseRecipe<
        Rec extends SRec,
        SRec extends BaseRecipe<?, SRec, Inv>,
        Inv extends BaseRecipe.RecInv<SRec>>
        implements Recipe<Inv> {

    private final RecType<Rec, SRec, Inv> factory;
    public ResourceLocation id;

    public BaseRecipe(ResourceLocation id, RecType<Rec, SRec, Inv> fac) {
        this.id = id;
        factory = fac;
    }

    @Override
    public abstract boolean matches(Inv inv, Level world);

    @Override
    public abstract ItemStack assemble(Inv inv);

    @Override
    public abstract boolean canCraftInDimensions(int r, int c);

    @Override
    public abstract ItemStack getResultItem();

    @Override
    public final ResourceLocation getId() {
        return id;
    }

    @Override
    public final RecipeSerializer<?> getSerializer() {
        return factory;
    }

    @Override
    public final RecipeType<?> getType() {
        return factory.type;
    }

    public interface RecInv<R extends BaseRecipe<?, R, ?>> extends Container {

    }

    public static class RecType<Rec extends SRec, SRec extends BaseRecipe<?, SRec, Inv>, Inv extends RecInv<SRec>>
            extends RecSerializer<Rec, Inv> {

        public final RecipeType<SRec> type;

        public RecType(Class<Rec> rec, RecipeType<SRec> type) {
            super(rec);
            this.type = type;
        }

    }

}