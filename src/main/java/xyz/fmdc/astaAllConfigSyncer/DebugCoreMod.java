/// Copyright (c) 2022 anatawa12 and other contributors
/// This file is part of *All Config Syncer, released under MIT License
/// See LICENSE at https://github.com/freedom-mod-development-community/asta-all-config-syncer for more details

package xyz.fmdc.astaAllConfigSyncer;

import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraftforge.fml.common.asm.transformers.deobf.FMLRemappingAdapter;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import java.util.Map;

// the coremod to enable DeobfuscationTransformer for *All
public final class DebugCoreMod implements IFMLLoadingPlugin {
    @Override
    public String[] getASMTransformerClass() {
        return new String[] { "xyz.fmdc.astaAllConfigSyncer.DebugCoreMod$Transformer" };
    }

    @Override
    public String getModContainerClass() {
        return null;
    }

    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {
    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }

    public static final class Transformer implements IClassTransformer {

        @Override
        public byte[] transform(String name, String transformedName, byte[] basicClass) {
            if (!name.startsWith("net.minecraft.scalar.") || basicClass == null) return basicClass;

            ClassReader classReader = new ClassReader(basicClass);
            ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);
            classReader.accept(new FMLRemappingAdapter(classWriter), ClassReader.EXPAND_FRAMES);
            return classWriter.toByteArray();
        }
    }
}
