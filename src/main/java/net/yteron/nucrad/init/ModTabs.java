package net.yteron.nucrad.init;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class ModTabs {
    public static final ItemGroup NUCRAD = new ItemGroup("nucradTab") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ModItems.METALP.get());
        }

    };

}
