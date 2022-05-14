package com.github.alexeysharandin.quarkus.lambdada.deployment;

/*
import com.github.alexeysharandin.quarkus.lambdada.runtime.ProfilerConfig;
import io.quarkus.deployment.IsDevelopment;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.annotations.ExecutionTime;
import io.quarkus.deployment.annotations.Record;
import io.quarkus.deployment.builditem.LaunchModeBuildItem;
import io.quarkus.deployment.pkg.builditem.CurateOutcomeBuildItem;
import io.quarkus.devconsole.spi.DevConsoleRuntimeTemplateInfoBuildItem;
import io.quarkus.vertx.http.deployment.NonApplicationRootPathBuildItem;
import io.quarkus.vertx.http.deployment.RouteBuildItem;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Supplier;
*/

@SuppressWarnings("unused")
public class DevUiDeploymentProcessor {
   /* @BuildStep
    @Record(ExecutionTime.RUNTIME_INIT)
    void handler(
            LaunchModeBuildItem launch,
            NonApplicationRootPathBuildItem nonApplicationRootPathBuildItem,
            BuildProducer<RouteBuildItem> routeBuildProducer,
            ProfilerConfig config
    ) {
        System.out.println("handle router");
        if(launch.getLaunchMode().isDevOrTest() || config.enabled  ) {
            System.out.println("router fired");
            RouteBuildItem build = nonApplicationRootPathBuildItem.routeBuilder()
                    .route(config.path)
                    .routeConfigKey("quarkus.lamdbada.path")
                    .build();
            routeBuildProducer.produce(build);
        }
    }

    @BuildStep(onlyIf = IsDevelopment.class)
    public DevConsoleRuntimeTemplateInfoBuildItem collectBeanInfo(
            CurateOutcomeBuildItem curateOutcomeBuildItem
    ) {
        System.out.println("handle collectBeanInfo");
        return new DevConsoleRuntimeTemplateInfoBuildItem("records",
                new LamdbadaDevUiSupplier(), this.getClass(), curateOutcomeBuildItem);
    }

    private static class LamdbadaDevUiSupplier implements Supplier<Collection<Object>> {
        @Override
        public Collection<Object> get() {
            return new ArrayList<>();
        }
    }*/
}
