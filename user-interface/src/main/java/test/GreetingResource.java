package test;

import io.quarkus.runtime.StartupEvent;
import io.smallrye.mutiny.Uni;

import javax.enterprise.event.Observes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


@Path("/hello")

public class GreetingResource {
/*    @Inject
    Vertx vertx;*/

    void onLoad(@Observes StartupEvent event) {


        System.out.println("GreetingResource.OnLoad");
/*        TestClass testClass = new TestClass();
        testClass.sayHello("OnLoad");*/
        //testClass.test("OnLoad");
        //TestClass.main(null);
        System.out.println("Run http://localhost:8080/hello/vertx to check lambda");
        System.out.println("Run http://localhost:8080/q/lamdbada to check profiler UI");

        //SlowContextualFunction
        //new Exception().printStackTrace();
        //io.quarkus.arc.deployment.ArcProcessor
        //io.quarkus.arc.runtime.devconsole.InvocationInterceptor
        //QuarkusClassLoader
    }
/*
    @GET
    @Path("reactive")
    @Produces(MediaType.TEXT_PLAIN)
    public Uni<String> helloReactive() {
        //org.jboss.resteasy.reactive.common.core.AbstractResteasyReactiveContext
        return Uni.createFrom().item("Hello from RESTEasy Reactive").onItem().transform(f -> {
            return new TestClass().sayHello("Vertx");
        });
    }

    @GET
    @Path("static")
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        Context ctx = vertx.getOrCreateContext();
        ctx.putLocal("random", random());
        return new TestClass().sayHello("Static");
    }*/

    @GET
    @Path("vertx")
    @Produces(MediaType.TEXT_PLAIN)
    public Uni<String> mutinyVertxTest() {
        //new Exception().printStackTrace();
        // Somethere io.vertx.core.impl.DuplicatedContext created
        Uni<String> uni = Uni
                .createFrom()
                .item( () -> this.a("start"))
                .onItem()
                .transform((s) -> s + " magic ")
                .onItem()
                .transform(this::b)
                .onItem()
                .transform(this::c);

        return uni;
    }

    /*private void vertxTest() {
        Future<String> a = Future.future(h -> h.complete(a()));
        Future<String> b = Future.future(h -> h.complete(b(a.result())));
        Future<String> c = Future.future(h -> h.complete(b(b.result())));
        c.onComplete(h -> System.out.println("Vertx result:" + h.result()));
    }*/

    /*private void vertxContextTest() {
        io.vertx.core.Vertx vertx = io.vertx.core.Vertx.vertx();
        vertx.getOrCreateContext();
        VertxOptions options;

        Future<String> a = vertx.executeBlocking(h -> h.complete(a()));
        Future<String> b = vertx.executeBlocking(h -> h.complete(b(a.result())));
        Future<String> c = vertx.executeBlocking(h -> h.complete(c(b.result())));
        c.onComplete(h -> System.out.println("Vertx result:" + h.result()));
    }
*/
/*    private io.vertx.core.Vertx vertx() {
        return this.vertx.getDelegate();
    }*/


/*    private Context ctx() {
        return Vertx.currentContext();
    }*/

    private String a(String s) {
        //System.out.println("A() called in ctx = " + ctx());
        return "a";
    }

    private String b(String s) {
        //System.out.println("B() called in ctx = " + ctx());
        return s + "b";
    }

    private String c(String s) {
        //System.out.println("C() called in ctx = " + ctx());
        return s + "c";
    }

   /* private String random() {
        return UUID.randomUUID().toString();
    }*/

}