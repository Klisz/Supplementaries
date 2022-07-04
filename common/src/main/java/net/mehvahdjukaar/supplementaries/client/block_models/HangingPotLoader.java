package net.mehvahdjukaar.supplementaries.client.block_models;


import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraftforge.client.model.IModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;

public class HangingPotLoader implements IModelLoader<HangingPotGeometry> {

    @Override
    public void onResourceManagerReload(ResourceManager resourceManager) {
    }

    @Override
    public HangingPotGeometry read(JsonDeserializationContext context, JsonObject json) {
        BlockModel model = ModelLoaderRegistry.ExpandedBlockModelDeserializer.INSTANCE
                .getAdapter(BlockModel.class).fromJsonTree(json.get("rope"));
        return new HangingPotGeometry(model);
    }
}
