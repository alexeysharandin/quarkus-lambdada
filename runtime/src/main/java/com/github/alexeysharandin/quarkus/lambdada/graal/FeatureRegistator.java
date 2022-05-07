package com.github.alexeysharandin.quarkus.lambdada.graal;

import com.oracle.svm.core.annotate.AutomaticFeature;
import org.graalvm.nativeimage.hosted.Feature;
import org.jboss.logging.Logger;

import java.util.Set;

@AutomaticFeature
public class FeatureRegistator implements Feature {
    private static final Logger LOGGER = Logger.getLogger(FeatureRegistator.class);
    @Override
    public void beforeAnalysis(BeforeAnalysisAccess a) {
        LOGGER.info("beforeAnalysis:access = " + a);
        LOGGER.warn("LambaCapture implemented in svm 22.1.x. But in mvn central only 20.0.0.2 available. Check maven repo late.");

        /*FeatureImpl.BeforeAnalysisAccessImpl access = (FeatureImpl.BeforeAnalysisAccessImpl) a;
        AnalysisUniverse universe = access.getUniverse();
        registredClasses = new HashSet<>();
        Function<Object, Object> function = o -> {
            Class<?> clazz = o.getClass();
            if (o instanceof SerializableLambdaMarker & !registredClasses.contains(clazz.getName())) {
                System.out.println("Find serializable marker for class: " + clazz + " register for reflection");
                try {
                    RuntimeReflection.register(SerializedLambda.class);

                    dumpClass(o, clazz);


                    Method m = ReflectionUtil.lookupMethod(clazz, "writeReplace");
                    //Method m = clazz.getDeclaredMethod("writeReplace");
                    RuntimeSerializationSupport support = ImageSingletons.lookup(RuntimeSerializationSupport.class);
                    support.registerLambdaCapturingClass(ConfigurationCondition.alwaysTrue(), clazz.getName());
                    RuntimeReflection.register(clazz);
                    RuntimeReflection.register(m);
                    System.out.println("Method registered");
                } catch (Exception e) {
                    System.out.println("ERROR: writeReplace not found");
                    e.printStackTrace();
                }
                //RuntimeReflection.register(clazz);
                registredClasses.add(clazz.getName());

            }
            return o;
        };*/

        //universe.registerObjectReplacer(function);
        Feature.super.beforeAnalysis(a);
    }

    /*private void dumpClass(Object o, Class<?> clazz) throws IllegalAccessException {
        System.out.println("Interfaces: " + Arrays.toString(clazz.getInterfaces()));

        dumpMethods(clazz);
        Field[] declaredFields = clazz.getDeclaredFields();
        for(Field ff : declaredFields) {
            String name = ff.getName();
            System.out.println("declaredField = " + name);
            if(name.contains("LAMBDA")) {
                ff.setAccessible(true);
                Object x = ff.get(o);
                System.out.println("object = " + x + " " + Arrays.toString(x.getClass().getInterfaces()));
                dumpMethods(x.getClass());
                Field[] fields = x.getClass().getDeclaredFields();
                System.out.println("fields = " + Arrays.toString(fields));
                Method m = ReflectionUtil.lookupMethod(clazz, "writeReplace");
                RuntimeReflection.register(x.getClass());
                RuntimeReflection.register(m);
                System.out.println("Registred");
                //RuntimeSerializationSupport support = ImageSingletons.lookup(RuntimeSerializationSupport.class);
                //support.registerLambdaCapturingClass(ConfigurationCondition.alwaysTrue(), o.getClass().getName());

            }
            //dumpClass(x, x.getClass());
        }
    }

    private void dumpMethods(Class<?> clazz) {
        Method[] declaredMethods = clazz.getDeclaredMethods();
        for(Method mm : declaredMethods) {
            System.out.println("declaredMethod = " + mm.toString());

        }
    }*/


    @Override
    public void duringAnalysis(DuringAnalysisAccess a) {
        LOGGER.info("duringAnalysis:access = " + a);
        //com.oracle.svm.hosted.lambda.LambdaProxyRenamingSubstitutionProcessor
        /*OptionValues options = new OptionValuesImpl();
        DebugContext debug = new Builder(options, Collections.emptyList()).build();
        GraalJVMCICompiler compiler = (GraalJVMCICompiler) JVMCI.getRuntime().getCompiler();
        Providers providers = compiler.getGraalRuntime().getCapability(RuntimeProvider.class).getHostBackend().getProviders();
        final HotSpotJITClassInitializationPlugin initializationPlugin = new HotSpotJITClassInitializationPlugin();
        LambdaUtils.findStableLambdaName(initializationPlugin, providers, type, options, debug, this);
        */Feature.super.duringAnalysis(a);
    }

    @Override
    public void afterAnalysis(AfterAnalysisAccess a) {
        LOGGER.info("afterAnalysis:access = " + a);

        Feature.super.afterAnalysis(a);
    }


    @Override
    public void cleanup() {
        Feature.super.cleanup();
    }
}
