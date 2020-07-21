package io.vertx.blog.first;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.concurrent.atomic.AtomicInteger;

public class Employee extends AbstractVerticle {

    @Override
    //Future<void> so that there is no return in execution
    public void start(Future<Void> fut) {
        vertx.eventBus().consumer("message.archive", this::archive);
        System.out.println("Instances printing");
        fut.complete();
    }

    private <T> void archive(Message<T> tMessage) {
        String body=tMessage.body().toString();
        JsonObject jsonObject=new JsonObject(body);
        JsonArray jsonArray=jsonObject.getJsonArray("employee");
        for(int i=0;i<jsonArray.size();i++){
            JsonObject jsonObject1=jsonArray.getJsonObject(i);
            String name=jsonObject1.getString("name");
            String age=jsonObject1.getString("age");
            String address=jsonObject1.getString("address");
            String salary=jsonObject1.getString("salary");
            System.out.println(name+" "+age+" "+address+" "+salary);

        }
    }

}