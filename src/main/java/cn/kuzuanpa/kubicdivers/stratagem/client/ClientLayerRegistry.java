package cn.kuzuanpa.kubicdivers.stratagem.client;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;

import static cn.kuzuanpa.kubicdivers.stratagem.KubicdiversStratagemMod.MOD_ID;

public class ClientLayerRegistry {
    public static final ModelLayerLocation SENTRY_LAYER = new ModelLayerLocation(
            new ResourceLocation(MOD_ID, "gatling_sentry"), "main");
}
