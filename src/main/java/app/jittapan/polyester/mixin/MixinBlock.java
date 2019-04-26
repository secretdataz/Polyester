package app.jittapan.polyester.mixin;

import app.jittapan.polyester.PolyesterMod;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Block.class)
public abstract class MixinBlock {

    @Inject(at = @At("HEAD"), method="shouldDrawSide", cancellable = true)
    private static void shouldDrawSide(BlockState blockState_1, BlockView blockView_1, BlockPos blockPos_1, Direction direction_1, CallbackInfoReturnable<Boolean> ci) {
        if(PolyesterMod.INSTANCE.shouldProcess()) {
            ci.setReturnValue(PolyesterMod.INSTANCE.shouldRenderSide(blockState_1, blockView_1, blockPos_1, direction_1));
        }
    }
}
