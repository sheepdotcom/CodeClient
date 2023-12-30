package dev.dfonline.codeclient.switcher;

import dev.dfonline.codeclient.CodeClient;
import dev.dfonline.codeclient.location.*;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public class NodeSwitcher extends GenericSwitcher {
    public NodeSwitcher() {
        super(Text.literal("Node Switcher"), GLFW.GLFW_KEY_F3, GLFW.GLFW_KEY_F4);
    }

    @Override
    protected void init() {
        footer = Text.literal("[ F4 ]").formatted(Formatting.AQUA).append(Text.literal(" Next").formatted(Formatting.WHITE));
        selected = 0;
        super.init();
    }

    @Override
    List<Option> getOptions() {
        ArrayList<Option> options = new ArrayList<>();
        options.add(new Option(Text.literal("Node 1"), Items.DIAMOND_BLOCK.getDefaultStack(), () -> joinNode("node1")));
        options.add(new Option(Text.literal("Node 2"), Items.REDSTONE_BLOCK.getDefaultStack(), () -> joinNode("node2")));
        options.add(new Option(Text.literal("Node 3"), Items.POLISHED_ANDESITE.getDefaultStack(), () -> joinNode("node3")));
        options.add(new Option(Text.literal("Node 4"), Items.GRASS_BLOCK.getDefaultStack(), () -> joinNode("node4")));
        options.add(new Option(Text.literal("Node 5"), Items.SAND.getDefaultStack(), () -> joinNode("node5")));
        options.add(new Option(Text.literal("Node 6"), Items.RED_SAND.getDefaultStack(), () -> joinNode("node6")));
        options.add(new Option(Text.literal("Node 7"), Items.PINK_TERRACOTTA.getDefaultStack(), () -> joinNode("node7")));
        options.add(new Option(Text.literal("Node Beta"), Items.STRUCTURE_BLOCK.getDefaultStack(), () -> joinNode("beta")));
        return options;
    }

    private void joinNode(String node) {
        CodeClient.MC.getNetworkHandler().sendCommand("server " + node);
    }
}
