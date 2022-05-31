package test;

import io.smallrye.mutiny.Uni;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Path("test")
public class SomeTestResource {
    @GET
    @Path("asyncroles/{id}")
    @Produces(MediaType.TEXT_PLAIN) // enter
    public Uni<List<String>> getUniUserRoles(@PathParam("id") long id) {
        return Uni
                .createFrom()
                .item(isUserExists(id))
                .onItem()
                .transform(loadRoles(id))
                .onItem()
                .ifNotNull()
                .transform(this::filterRoles);

    } // vertx.subscribe

    private Function<Boolean, List<String>> loadRoles(long id) {
        return b -> b ? loadUserRoles(id) : null;
    }

    private List<String> filterRoles(List<String> roles) {
        roles.remove("admin");
        return roles;
    }

    private List<String> doSomething(List<String> roles) {
        roles.add("something new");
        return roles;
    }

    private List<String> loadUserRoles(long id) {
        List<String> result = new ArrayList<>();
        if (id == 1) {
            result.add("User 1");
        }
        result.add("user");
        result.add("moderator");
        result.add("admin");
        return result;
    }

    private boolean isUserExists(Long id) {
        // go to DB and check is ID exists
        return id == 1;
    }
}
