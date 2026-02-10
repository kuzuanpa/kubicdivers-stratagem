package cn.kuzuanpa.kubicdivers.stratagem.common;

import cn.kuzuanpa.kubicdivers.stratagem.KubicdiversStratagemMod;
import cn.kuzuanpa.kubicdivers.stratagem.common.items.StratagemBallItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, KubicdiversStratagemMod.MOD_ID);

    public static final RegistryObject<Item> STRATAGEM_BALL = ITEMS.register("stratagem_ball",
            () -> new StratagemBallItem(new Item.Properties()
                    .stacksTo(1)
                    .durability(0)));
}