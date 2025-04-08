package net.mehvahdjukaar.supplementaries.common.block.faucet;

import com.mojang.datafixers.util.Pair;
import net.mehvahdjukaar.moonlight.api.fluids.FluidContainerList;
import net.mehvahdjukaar.supplementaries.common.utils.ItemsUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

// For fluids that have a container item where the container is just air (e.g. slime, honey), try inserting that item into a targeted container
public class AirContainerInteraction implements FaucetTarget.Tile {
    @Override
    public Integer fill(Level level, BlockPos pos, BlockEntity target, FluidOffer offer) {
        ItemStack air = Items.AIR.getDefaultInstance();
        for (int i = offer.minAmount(); i <= offer.fluid().getCount(); i++) {
            Pair<ItemStack, FluidContainerList.Category> pair = offer.fluid().copyWithCount(i).toItem(air, true);
            if (pair != null) {
                ItemStack stack = ItemsUtil.tryAddingItem(pair.getFirst(), level, Direction.UP, target);
                if (stack.isEmpty()) {
                    return i;
                }
            }
        }

        return null;
    }
}
