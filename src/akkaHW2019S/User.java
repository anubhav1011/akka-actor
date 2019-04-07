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
//        ActorSystem system = ActorSystem.create("EstimationSystem");
//        Props searcherProps = Props.create(Searcher.class);
//        ActorRef searcherActor = system.actorOf(searcherProps, "searcherActor");
        URL path = User.class.getResource("cities.txt");
        File f = new File(path.getFile());
        // reader = new BufferedReader(new FileReader(f));
        Scanner sc = new Scanner(new BufferedReader(new FileReader(f)));
        int rows = 0;
        while (sc.hasNextLine()) {
            sc.nextLine();
            rows++;
        }
        int[][] citiesArray = new int[rows][rows];
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













        /*
         * Create the Solver Actor and send it the StartProcessing
         * message. Once you get back the response, use it to print the result.
         * Remember, there is only one actor directly under the ActorSystem.
         * Also, do not forget to shutdown the actorsystem
         */
    }

}
