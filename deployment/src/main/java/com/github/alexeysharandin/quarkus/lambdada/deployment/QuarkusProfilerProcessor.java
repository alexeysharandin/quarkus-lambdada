package com.github.alexeysharandin.quarkus.lambdada.deployment;

import com.github.alexeysharandin.quarkus.lambdada.io.sqlite.EmbeddedStackTraceWriter;
import com.github.alexeysharandin.quarkus.lambdada.io.StackTraceWriter;
import com.github.alexeysharandin.quarkus.lambdada.runtime.*;
import com.github.alexeysharandin.quarkus.lambdada.deployment.build.ProfilerDumperBuildItem;
import com.github.alexeysharandin.quarkus.lambdada.deployment.build.ProfilerPluginBuildItem;
import io.quarkus.arc.deployment.SyntheticBeanBuildItem;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.annotations.ExecutionTime;
import io.quarkus.deployment.annotations.Record;
import io.quarkus.deployment.builditem.BytecodeTransformerBuildItem;
import io.quarkus.deployment.builditem.CombinedIndexBuildItem;
import io.quarkus.deployment.builditem.FeatureBuildItem;
import io.quarkus.deployment.builditem.LaunchModeBuildItem;
import org.jboss.logging.Logger;

import javax.inject.Singleton;
import java.util.*;


public class QuarkusProfilerProcessor {
    private static final Logger LOGGER = Logger.getLogger(QuarkusProfilerProcessor.class);

//    private final ClassPool classPool;
    private final Set<String> processedClasses = new HashSet<>();

    private static final String FEATURE = "quarkus-profiler";

    public QuarkusProfilerProcessor() {}

    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE);
    }

    @BuildStep
    void instrument(final CombinedIndexBuildItem index,
                    final LaunchModeBuildItem launchMode,
                    final BuildProducer<BytecodeTransformerBuildItem> transformerBuildProducer,
                    final ProfilerConfig config) {

        boolean enabled = launchMode.getLaunchMode().isDevOrTest() || config.enabled;
        if (enabled) {
            LOGGER.info("Profiler Processor enabled. Begin classes transformation");
            //LambdaBuildProcessor.process(index, transformerBuildProducer);
            //registerClassList(trackable, transformerBuildProducer);
            //registerAnnotations(index, transformerBuildProducer);

            //createBean(dumpers, plugins, syntheticBuildProducer);

        } else {
            LOGGER.info("Profiler Processor disabled. Skip transformation");
        }
    }

    @SuppressWarnings("unused")
    @BuildStep
    @Record(ExecutionTime.STATIC_INIT)
    public void createProfilerBean(
            List<ProfilerDumperBuildItem> dumperBuildItems,
            List<ProfilerPluginBuildItem> pluginBuildItems,
            BuildProducer<SyntheticBeanBuildItem> syntheticBuildProducer,
            ProfilerRecorder rec
    ) {
        LOGGER.info("Create Profiler Bean");
        List<StackTraceWriter> stackTraceWriters = registerDumpers(dumperBuildItems);
        List<ProfilerPlugin> profilerPlugins = registerPlugins(pluginBuildItems);

        SyntheticBeanBuildItem bean = SyntheticBeanBuildItem
                .configure(ProfilerRuntimeRecorder.class)
                .scope(Singleton.class)
                .runtimeValue(rec.create(stackTraceWriters, profilerPlugins))
                .unremovable()
                .done();
        syntheticBuildProducer.produce(bean);
    }

    private List<StackTraceWriter> registerDumpers(List<ProfilerDumperBuildItem> dumpers) {
        LOGGER.info("Register dumpers");
        List<StackTraceWriter> result = new ArrayList<>();
        if (dumpers.size() == 0) {
            LOGGER.info("Dumpers not found. Register default: " + EmbeddedStackTraceWriter.class.getName());
            result.add(new EmbeddedStackTraceWriter());
        } else {
            for (ProfilerDumperBuildItem item : dumpers) {
                Class<? extends StackTraceWriter> clazz = item.dumperClass();
                try {
                    result.add(clazz.getDeclaredConstructor().newInstance());
                    LOGGER.info("Dumper created for class: " + clazz.getName());
                } catch (Exception e) {
                    LOGGER.error("Unable to create Dumper for: " + clazz.getName(), e);
                }
            }
        }
        return result;
    }

    private List<ProfilerPlugin> registerPlugins(List<ProfilerPluginBuildItem> plugins) {
        LOGGER.info("Register plugins");
        List<ProfilerPlugin> result = new ArrayList<>();

        for (ProfilerPluginBuildItem item : plugins) {
            for (Class<? extends ProfilerPlugin> clazz : item.plugins()) {
                try {
                    result.add(clazz.getDeclaredConstructor().newInstance());
                    LOGGER.info("ProfilePlugin created for class: " + clazz.getName());
                } catch (Exception e) {
                    LOGGER.error("Unable to create ProfilerPlugin for " + clazz.getName(), e);
                }
            }
        }

        return result;
    }

/*    private void registerClassList(
            List<ProfilerTrackableClassListBuildItem> trackable,
            BuildProducer<BytecodeTransformerBuildItem> transformerBuildProducer
    ) {
        LOGGER.info("Register class list");
        for (ProfilerTrackableClassListBuildItem item : trackable) {
            for (Class<?> clazz : item.trackable()) {
                String className = clazz.getName();
                LOGGER.info("Process class: " + className);
                transformClass(transformerBuildProducer, className);
            }
        }
    }*/

/*    private void registerAnnotations(
            CombinedIndexBuildItem index,
            BuildProducer<BytecodeTransformerBuildItem> transformerBuildProducer) {
        LOGGER.info("Register annotations");
        Collection<AnnotationInstance> annotations = index.getIndex().getAnnotations(DotName.createSimple(Profilable.class.getName()));
        for (AnnotationInstance i : annotations) {
            String className = i.target().asClass().name().toString();
            LOGGER.info("Process annotated class: " + className);
            transformClass(transformerBuildProducer, className);
        }
    }*/

/*    private void transformClass(BuildProducer<BytecodeTransformerBuildItem> transformerBuildProducer, String className) {
        LOGGER.info("Transform bytecode for class: " + className);

        BytecodeTransformerBuildItem item =
                new BytecodeTransformerBuildItem.Builder()
                        .setClassToTransform(className)
                        .setClassReaderOptions(ClassReader.EXPAND_FRAMES
                                | ClassWriter.COMPUTE_FRAMES)
                        .setVisitorFunction((ignored, visitor) -> new InvocationClassVisitor(visitor, className))
                        .build();

        transformerBuildProducer.produce(item);
    }*/

    /*private static class InvocationClassVisitor extends ClassVisitor {
        private final String className;
        private final String classNameBinary;

        public InvocationClassVisitor(ClassVisitor visitor, String className) {
            super(Opcodes.ASM9, visitor);
            this.className = className;
            this.classNameBinary = className.replace(".", "/");
        }

        *//*@Override
        public MethodVisitor visitMethod(int access, String methodName, String descriptor, String signature, String[] exceptions) {
            MethodVisitor visitor = super.visitMethod(access, methodName, descriptor, signature, exceptions);
            if (visitor == null) {
                return null;
            }
            if("<init>".equals(methodName) || "<clinit>".equals(methodName)) {
                return visitor;
            }

            LOGGER.info("Visit method: class = " + className + ", methodName = " + methodName + ", access = " + access  + ", descriptor = " + descriptor + ", signature = " + signature + ", exceptions = " + Arrays.deepToString(exceptions));
            return new MethodTransformer(visitor, className, classNameBinary, access, methodName, descriptor);
        }*//*
    }*/
}
