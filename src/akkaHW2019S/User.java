package akkaHW2019S;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.util.Scanner;

/**
 * Main class for your estimation actor system.
 *
 * @author Akash Nagesh and M. Kokar
 */
public class User {

    public static void main(String[] args) throws Exception {
        ActorSystem system = ActorSystem.create("EstimationSystem");
        Props solverProps = Props.create(Solver.class);
        ActorRef searcherActor = system.actorOf(solverProps, "searcherActor");
        searcherActor.tell("StartProcessing", null);
        //system.terminate();















        /*
         * Create the Solver Actor and send it the StartProcessing
         * message. Once you get back the response, use it to print the result.
         * Remember, there is only one actor directly under the ActorSystem.
         * Also, do not forget to shutdown the actorsystem
         */
    }

}
