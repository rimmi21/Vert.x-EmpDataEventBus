package io.vertx.blog.first;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.ext.web.Router;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

//Below is the main verticle i have used
public class MyFirstVerticle extends AbstractVerticle {


    @Override
    //Future<void> so that there is no return in execution
    public void start(Future<Void> fut) {
//        EmployeeDataManually(); // It's for the "by-default" employee data i have created

        // Router object created
        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create());
        //It routes requests arriving on “/“ to the given handler.
        router.route("/").handler(routingContext -> {
            HttpServerResponse response = routingContext.response(); //created a PI for the server side response
            response
                    .putHeader("content-type", "text/html")
                    .end("Local host is listening at 8080</h1>");
        });

        // Serve static resources from the /assets directory
       // router.route("/assets/*").handler(StaticHandler.create("assets")); //Created all these routers to push API Server reqs
       // router.get("/api/employee").handler(this::getAll);//router to handle the GET requests on “/api/employee” by calling the getAll method
        //router.route("/api/employee*").handler(BodyHandler.create());
        //router.post("/api/employee").handler(this::addOne);
        router.post("/api/dummy").handler(this::jss);
        vertx.createHttpServer().requestHandler(router::accept) //**accept -> method from the router obj. And instructs vert.x to call the accept method of the router when it receives a request.
                .listen(
                        // Retrieve the port from the configuration,
                        // default to  port 8080
                        config().getInteger("http.port", 8080),
                        result -> {
                            if (result.succeeded()) {
                                DeploymentOptions workver = new DeploymentOptions().setWorker(true).setInstances(10);
                                vertx.deployVerticle(Employee.class.getName(),workver);                               //vertx.deployVerticle(Employee.class.getName());
                                fut.complete();
                            } else {
                                fut.fail(result.cause());
                            }
                        }
                );
    }

    private void jss(RoutingContext routingContext) {
        //System.out.println(routingContext.request().getHeader("name"));
        //System.out.println(routingContext.getBodyAsJson().toString());
        vertx.eventBus().send("message.archive",routingContext.getBodyAsString());
        routingContext.response().end("Message sent through Event Bus");
    }



}