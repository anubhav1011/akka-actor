package akkaHW2019S;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URL;
import java.util.Scanner;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.dispatch.sysmsg.Terminate;

/**
 * This is the main actor and the only actor that is created directly under the
 * {@code ActorSystem} This actor creates 4 child actors
 * {@code Searcher}
 *
 * @author Akash Nagesh and M. Kokar
 */
public class Solver extends UntypedActor {


    private double[][] citiesArray;

    private static final int SEARCHER_AGENTS = 4;

    private boolean solutionSubmitted = false;

    public Solver() {
        init();
    }

    private void init() {
        try {
            this.citiesArray = getCitiesArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


    }


    @Override
    public void onReceive(Object msg) throws FileNotFoundException {
        //System.out.println("Entering with message " + msg);
        if (msg instanceof String) {
            String message = (String) msg;
            if (message.equals("StartProcessing")) {
                for (int i = 1; i < 5; i++) {
                    // System.out.println("Creating");
                    Props propsSearcher = Props.create(Searcher.class, 0, this.citiesArray, "Searcher Agent " + i);
                    ActorRef searcherActor = getContext().actorOf(propsSearcher, "Agent" + i);
                    searcherActor.tell("Start Solving", getSelf());
                }
            }

        } else if (msg instanceof Solution) {
            if (!solutionSubmitted) {
                Solution sol = (Solution) msg;
                System.out.println("Winner is" + sol);
                this.solutionSubmitted = true;
                for (ActorRef actor : getContext().getChildren()) {
                    actor.tell(sol, getSelf());
                }
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

}
