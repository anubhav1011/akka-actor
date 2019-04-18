package akkaHW2019S;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import akka.actor.ActorRef;
import akka.actor.DeadLetter;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.dispatch.sysmsg.Terminate;
import akka.event.Logging;
import akka.event.LoggingAdapter;

/**
 * This is the main actor and the only actor that is created directly under the
 * {@code ActorSystem} This actor creates 4 child actors
 * {@code Searcher}
 *
 * @author Akash Nagesh and M. Kokar
 */
public class Solver extends UntypedActor {

    LoggingAdapter log = Logging.getLogger(getContext().system(), this);


    private double[][] citiesArray;

    private int counter = 4;

    private int startNode;

    private int desiredLength;

    private static final int SEARCHER_AGENTS = 5;

    private boolean solutionSubmitted = false;


    public Solver(int startNode, int desiredLength, double[][] citiesArray) {
        this.citiesArray = citiesArray;
        this.startNode = startNode;
        this.desiredLength = desiredLength;
        //init();
    }
//    private void init() {
//        try {
//            this.citiesArray = getCitiesArray();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//
//
//    }


    @Override
    public void onReceive(Object msg) throws FileNotFoundException {
        //System.out.println("Entering with message " + msg);
        if (msg instanceof String) {
            String message = (String) msg;
            Props propsSearcher = Props.create(Searcher.class, this.startNode, this.desiredLength, this.citiesArray);
            if (message.equals("StartProcessing")) {
                List<ActorRef> actorRefs = new ArrayList<>();
                for (int i = 1; i < SEARCHER_AGENTS; i++) {
                    // System.out.println("Creating");
                    ActorRef searcherActor = getContext().actorOf(propsSearcher, "Agent" + i);
                    //searcherActor.tell("Start Solving", getSelf());
                    actorRefs.add(searcherActor);
                }
                SearchMessage searchMessage = new SearchMessage(actorRefs, this.citiesArray, 0);
                for (ActorRef actor : getContext().getChildren()) {
                    actor.tell(searchMessage, getSelf());
                }


            }

        } else if (msg instanceof Solution) {
            counter--;
            if (!solutionSubmitted) {
                Solution sol = (Solution) msg;
                System.out.println("#############################");
                System.out.println("Please find below the results");
                System.out.println("#############################");
                System.out.println();
                this.solutionSubmitted = true;
                for (ActorRef actor : getContext().getChildren()) {
                    actor.tell(sol, getSelf());
                }


            }
            if (counter == 0) {
                context().stop(getSelf());
                context().system().terminate();

            }
        }
        //Code to implement
    }


    private double[][] getCitiesArray() throws FileNotFoundException {
        URL path = User.class.getResource("cities.txt");
        File f = new File(path.getFile());
        // reader = new BufferedReader(new FileReader(f));
        Scanner sc = new Scanner(new BufferedReader(new FileReader(f)));
        int rows = 0;
        while (sc.hasNextLine()) {
            sc.nextLine();
            rows++;
        }
        double[][] citiesArray = new double[rows][rows];
        sc = new Scanner(new BufferedReader(new FileReader(f)));
        for (int i = 0; i < citiesArray.length; i++) {
            String[] line = sc.nextLine().trim().split(" ");
            for (int j = 0; j < line.length; j++) {
                citiesArray[i][j] = Integer.parseInt(line[j]);
            }

        }
        for (int i = 0; i < citiesArray.length; i++) {
            for (int j = 0; j < citiesArray[i].length; j++) {
                System.out.print(citiesArray[i][j] + " ");
            }
            System.out.println();
        }
        return citiesArray;
    }


    @Override
    public void unhandled(Object message) {
        // clean up resources here ...
    }


}
