package dev.andante.teamtextresolve.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.StructureBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.StructureBlockBlockEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.StructureTemplate;
import net.minecraft.structure.StructureTemplateManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(StructureBlock.class)
public class StructureBlockMixin {
    @Inject(method = "neighborUpdate", at = @At("TAIL"))
    private void onNeighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify, CallbackInfo ci) {
        if (!(world instanceof ServerWorld serverWorld)) {
            return;
        }

        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (!(blockEntity instanceof StructureBlockBlockEntity structureBlockBlockEntity)) {
            return;
        }

        if (sourceBlock == Blocks.EMERALD_BLOCK) {
            Identifier id = Identifier.tryParse(structureBlockBlockEntity.getTemplateName());
            if (id == null) {
                return;
            }

            StructureTemplateManager manager = serverWorld.getStructureTemplateManager();
            Optional<StructureTemplate> template = manager.getTemplate(id);
            if (template.isEmpty()) {
                return;
            }

            structureBlockBlockEntity.setSize(template.get().getSize());
            structureBlockBlockEntity.markDirty();
            world.updateListeners(pos, state, state, Block.NOTIFY_ALL);
        }
    }
}
