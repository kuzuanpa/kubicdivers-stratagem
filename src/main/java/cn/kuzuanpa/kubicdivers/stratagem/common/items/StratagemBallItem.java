package cn.kuzuanpa.kubicdivers.stratagem.common.items;

import cn.kuzuanpa.kubicdivers.stratagem.common.stratagem.IStratagem;
import cn.kuzuanpa.kubicdivers.stratagem.common.entity.StratagemBallEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class StratagemBallItem extends Item {
    public StratagemBallItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, Player player, @NotNull InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);

        if (!hasStratagem(itemstack)) {
            return InteractionResultHolder.fail(itemstack);
        }

        level.playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.SNOWBALL_THROW, SoundSource.NEUTRAL,
                0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));

        if (!level.isClientSide) {
            StratagemBallEntity ballEntity = new StratagemBallEntity(level, player);
            ballEntity.setItem(itemstack);
            ballEntity.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1.5F, 1.0F);
            level.addFreshEntity(ballEntity);
        }

        player.awardStat(Stats.ITEM_USED.get(this));
        if (!player.getAbilities().instabuild) {
            itemstack.shrink(1);
        }

        return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide());
    }

    public static void setStratagem(ItemStack stack, IStratagem stratagem) {
        if (stratagem == null) return;

        CompoundTag tag = stack.getOrCreateTag();
        tag.putString("StratagemId", stratagem.getId());
        tag.putString("StratagemName", stratagem.getName());
        tag.putInt("Cooldown", stratagem.getCooldown());
    }

    @Nullable
    public static String getStratagemId(ItemStack stack) {
        if (!stack.hasTag()) return null;
        return stack.getTag().getString("StratagemId");
    }

    public static boolean hasStratagem(ItemStack stack) {
        return getStratagemId(stack) != null && !getStratagemId(stack).isEmpty();
    }

    @Override
    public @NotNull String getDescriptionId(@NotNull ItemStack stack) {
        if (hasStratagem(stack)) {
            return "item.kubicdivers_stratagem.stratagem_ball.activated";
        }
        return super.getDescriptionId(stack);
    }
}