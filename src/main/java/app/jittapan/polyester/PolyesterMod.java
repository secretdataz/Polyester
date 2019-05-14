package app.jittapan.polyester;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.FabricKeyBinding;
import net.fabricmc.fabric.api.client.keybinding.KeyBindingRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PolyesterMod implements ClientModInitializer {
    public static String MOD_NAME = "polyester";
    public static PolyesterMod INSTANCE;
    public static Logger LOGGER;

    public static FabricKeyBinding toggleXrayMode;
    public static FabricKeyBinding toggleFullBright;
    public static FabricKeyBinding toggleCaveMode;

    private boolean fullBrightEnabled = false;
    private boolean xrayEnabled = false;
    private boolean caveModeEnabled = false;
    private double gamma = 0.;

    private List<String> features = new ArrayList<>();

    private static List<Block> XRAY_BLOCKS;
    private static List<Block> CAVE_BLOCKS;

    @Override
    public void onInitializeClient() {
        INSTANCE = this;
        LOGGER = LogManager.getLogger("Polyester");

        KeyBindingRegistry.INSTANCE.addCategory(MOD_NAME);
        toggleFullBright = FabricKeyBinding.Builder.create(new Identifier(MOD_NAME, "fullbright"), InputUtil.Type.KEYSYM, 72, MOD_NAME).build(); // H
        toggleXrayMode = FabricKeyBinding.Builder.create(new Identifier(MOD_NAME, "xray"), InputUtil.Type.KEYSYM, 78, MOD_NAME).build(); // N
        toggleCaveMode = FabricKeyBinding.Builder.create(new Identifier(MOD_NAME, "cave"), InputUtil.Type.KEYSYM, 67, MOD_NAME).build(); // C
        KeyBindingRegistry.INSTANCE.register(toggleXrayMode);
        KeyBindingRegistry.INSTANCE.register(toggleFullBright);
        KeyBindingRegistry.INSTANCE.register(toggleCaveMode);

        XRAY_BLOCKS = Arrays.asList(Blocks.IRON_ORE, Blocks.COAL_ORE, Blocks.DIAMOND_ORE,
                Blocks.GOLD_ORE, Blocks.EMERALD_ORE, Blocks.REDSTONE_ORE, Blocks.OBSIDIAN, Blocks.DIAMOND_BLOCK,
                Blocks.IRON_ORE, Blocks.GOLD_BLOCK, Blocks.EMERALD_BLOCK, Blocks.END_PORTAL,
                Blocks.END_PORTAL_FRAME, Blocks.NETHER_PORTAL, Blocks.BEACON, Blocks.SPAWNER, Blocks.BOOKSHELF,
                Blocks.LAVA, Blocks.WATER, Blocks.NETHER_WART, Blocks.BLUE_ICE, Blocks.DRAGON_WALL_HEAD,
                Blocks.DRAGON_HEAD, Blocks.DRAGON_EGG, Blocks.NETHER_QUARTZ_ORE, Blocks.CHEST,
                Blocks.TRAPPED_CHEST, Blocks.DISPENSER, Blocks.DROPPER, Blocks.LAPIS_ORE, Blocks.LAPIS_BLOCK,
                Blocks.TNT, Blocks.CLAY, Blocks.WET_SPONGE, Blocks.SPONGE, Blocks.OAK_PLANKS, Blocks.CONDUIT,
                Blocks.ENDER_CHEST);

        CAVE_BLOCKS = Arrays.asList(Blocks.DIRT, Blocks.GRASS, Blocks.GRAVEL,
                Blocks.GRASS_BLOCK, Blocks.GRASS_PATH, Blocks.SAND, Blocks.SANDSTONE, Blocks.RED_SAND);
    }

    public void fullBright(boolean flag) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (!flag) {
            mc.options.gamma = gamma;
            features.remove("Fullbright");
        } else {
            this.gamma = mc.options.gamma;
            mc.options.gamma = 30.;
            features.add("Fullbright");
        }
        fullBrightEnabled = flag;
    }

    public void xray(boolean flag) {
        if (caveModeEnabled) {
            caveMode(false);
        }

        xrayEnabled = flag;

        if (flag) {
            features.add("X-Ray");
        } else {
            features.remove("X-Ray");
        }

        reloadRenderers();
    }

    public void caveMode(boolean flag) {
        if (xrayEnabled) {
            xray(false);
        }

        caveModeEnabled = flag;

        if(flag) {
            features.add("Cave");
        } else {
            features.remove("Cave");
        }

        reloadRenderers();
    }

    public boolean shouldProcess() {
        return xrayEnabled || caveModeEnabled;
    }

    public void handleKeybinds() {
        while (toggleFullBright.wasPressed()) {
            fullBright(!fullBrightEnabled);
        }

        while (toggleXrayMode.wasPressed()) {
            xray(!xrayEnabled);
        }

        while (toggleCaveMode.wasPressed()) {
            caveMode(!caveModeEnabled);
        }
    }

    public String getFeaturesString() {
        return String.join(" + ", features);
    }

    public boolean shouldRenderSide(BlockState blockState_1, BlockView blockView_1, BlockPos blockPos_1, Direction direction_1) {
        if (caveModeEnabled) {
            return !CAVE_BLOCKS.contains(blockState_1.getBlock()) && (blockView_1.getBlockState(blockPos_1.offset(direction_1)).isAir());
        } else if (xrayEnabled) {
            return XRAY_BLOCKS.contains(blockState_1.getBlock());
        }

        throw new IllegalStateException(); // Should not reach this part
    }

    public void reloadRenderers() {
        MinecraftClient.getInstance().worldRenderer.reload();
    }
}
