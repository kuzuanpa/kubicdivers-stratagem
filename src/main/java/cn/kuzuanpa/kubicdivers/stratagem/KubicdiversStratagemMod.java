package cn.kuzuanpa.kubicdivers.stratagem;

import cn.kuzuanpa.kubicdivers.stratagem.common.ModEntities;
import cn.kuzuanpa.kubicdivers.stratagem.common.ModItems;
import cn.kuzuanpa.kubicdivers.stratagem.common.stratagem.StratagemManager;
import cn.kuzuanpa.kubicdivers.stratagem.network.CooldownSyncPacket;
import cn.kuzuanpa.kubicdivers.stratagem.network.S2CSyncAvailableStratagemsPacket;
import cn.kuzuanpa.kubicdivers.stratagem.network.SetStratagemBallPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(KubicdiversStratagemMod.MOD_ID)
public class KubicdiversStratagemMod {
    public static final String MOD_ID = "kubicdivers_stratagem";
    public static final Logger LOGGER = LogManager.getLogger();

    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel NETWORK_CHANNEL = NetworkRegistry.newSimpleChannel(
            ResourceLocation.fromNamespaceAndPath(MOD_ID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public KubicdiversStratagemMod(FMLJavaModLoadingContext context) {
        IEventBus modEventBus = context.getModEventBus();

        ModItems.ITEMS.register(modEventBus);
        ModEntities.ENTITIES.register(modEventBus);

        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::clientSetup);

        MinecraftForge.EVENT_BUS.register(this);

        LOGGER.info("kubicdivers_stratagem initializing...");
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            StratagemManager.init();

            NETWORK_CHANNEL.registerMessage(0,
                    CooldownSyncPacket.class,
                    CooldownSyncPacket::encode,
                    CooldownSyncPacket::decode,
                    CooldownSyncPacket::handle
            );

            NETWORK_CHANNEL.registerMessage(1,
                    SetStratagemBallPacket.class,
                    SetStratagemBallPacket::encode,
                    SetStratagemBallPacket::decode,
                    SetStratagemBallPacket::handle
            );
            NETWORK_CHANNEL.registerMessage(2,
                    S2CSyncAvailableStratagemsPacket.class,
                    S2CSyncAvailableStratagemsPacket::encode,
                    S2CSyncAvailableStratagemsPacket::decode,
                    S2CSyncAvailableStratagemsPacket::handle
            );
            LOGGER.info("Common setup completed");
        });
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        event.enqueueWork(() -> LOGGER.info("Client setup completed"));
    }

}