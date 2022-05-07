package com.github.alexeysharandin.quarkus.lambdada.deployment;

import com.github.alexeysharandin.quarkus.lambdada.runtime.ProfilerConfig;
import com.github.alexeysharandin.quarkus.lambdada.deployment.asm.LambdaClassTransformer;
import io.quarkus.deployment.ApplicationArchive;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.ApplicationArchivesBuildItem;
import io.quarkus.deployment.builditem.BytecodeTransformerBuildItem;
import io.quarkus.deployment.builditem.LaunchModeBuildItem;
import org.jboss.jandex.ClassInfo;
import org.jboss.jandex.IndexView;
import org.jboss.logging.Logger;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("unused")
public class LambdaBuildProcessor {
    private static final Logger LOGGER = Logger.getLogger(LambdaBuildProcessor.class);

    @BuildStep
    void process(
            final ApplicationArchivesBuildItem applicationArchivesBuildItem,
            final BuildProducer<BytecodeTransformerBuildItem> transformerBuildProducer,
            final LaunchModeBuildItem launchMode,
            final ProfilerConfig config) {

        boolean enabled = launchMode.getLaunchMode().isDevOrTest() || config.enabled;
        if(!config.enabled) {
            LOGGER.info("Profiler included to project but not enabled. Skip transformation.");
            return;

        }

        LOGGER.info("Transform system classes");

        Set<ClassInfo> classes = new HashSet<>();

        Collection<ApplicationArchive> rootArchive = new ArrayList<>();
        rootArchive.add(applicationArchivesBuildItem.getRootArchive());
        applyApplicationArchive(rootArchive, classes);
        //applyApplicationArchive(applicationArchivesBuildItem.getApplicationArchives(), classes);

        for (ClassInfo info : classes) {
            String className = info.toString();
            //reflectiveClassProducer.produce(ReflectiveClassBuildItem.serializationClass(className));
            transformClass(transformerBuildProducer, className);
        }
    }

    private void applyApplicationArchive(Collection<ApplicationArchive> applicationArchives, Set<ClassInfo> classes) {
        for (ApplicationArchive archive : applicationArchives) {
            IndexView index = archive.getIndex();
            classes.addAll(index.getKnownClasses());
        }
    }

    private void transformClass(BuildProducer<BytecodeTransformerBuildItem> transformerBuildProducer, String className) {
        LOGGER.debug("Transform class: " + className);
        BytecodeTransformerBuildItem item =
                new BytecodeTransformerBuildItem.Builder()
                        .setClassToTransform(className)
                        .setClassReaderOptions(ClassReader.EXPAND_FRAMES
                                | ClassWriter.COMPUTE_FRAMES)
                        .setVisitorFunction((ignored, visitor) ->
                                new LambdaClassTransformer(visitor, className))
                        .build();

        transformerBuildProducer.produce(item);
    }
}
