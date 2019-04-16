package akkaHW2019S;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URL;
import java.util.Scanner;

/**
 * Main class for your estimation actor system.
 *
 * @author Akash Nagesh and M. Kokar
 */
public class User {

    double[][] citiesArray;

    int startNode;

    int maximumPathLength;



    public void init() {
        boolean valid = false;
        while (!valid) {
            Scanner myObj = new Scanner(System.in);
            System.out.println("Please enter file Name without extension, Ex: \"cities\"");
            String fileName = myObj.nextLine();
            System.out.println("File name is: " + fileName + ".txt");
            System.out.println("Enter start City index");
            this.startNode = myObj.nextInt();
            System.out.println("Enter desired length");
            this.maximumPathLength = myObj.nextInt();
            this.citiesArray = getCitiesArray(fileName);
            if (this.citiesArray == null) {
                continue;
            }
            valid = true;

        }


    }


    public static void main(String[] args) throws Exception {
        User user = new User();
        user.init();
        ActorSystem system = ActorSystem.create("EstimationSystem");
        //double[][] citiesArray = getCitiesArray();
        Props solverProps = Props.create(Solver.class, user.startNode, user.maximumPathLength, user.citiesArray);
        // Props propsSearcher = Props.create(Solver.class, 0citiesArray);
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


    private double[][] getCitiesArray(String fileName) {
        URL path = User.class.getResource(fileName + ".txt");
        if (path == null) {
            System.out.println("File does not exist. Please enter valid file name");
            return null;
        }
        File f = new File(path.getFile());
        // reader = new BufferedReader(new FileReader(f));
        Scanner sc = null;
        try {
            sc = new Scanner(new BufferedReader(new FileReader(f)));
        } catch (FileNotFoundException e) {
            System.out.println("File does not exist. Please enter valid file name");
        }
        int rows = 0;
        //int columns = 0;
        while (sc.hasNextLine()) {
            //columns = sc.nextLine().trim().split(" ").length;
            sc.nextLine();
            rows++;
        }
//        if (rows != columns) {
//            System.out.println("Rows: " + rows + " not equal to columns: " + columns);
//            System.out.println("Enter information again");
//            return null;
//        } else {
//            System.out.println("Rows: " + rows + " equal to columns: " + columns);
//        }
        double[][] citiesArray = new double[rows][rows];
        try {
            sc = new Scanner(new BufferedReader(new FileReader(f)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < citiesArray.length; i++) {
            String[] line = sc.nextLine().trim().split(" ");
            if (line.length != rows) {
                System.out.println("Not a square matrix, Please provide valid file");
                System.out.println("Enter information again");
                return null;
            }
            for (int j = 0; j < line.length; j++) {
                citiesArray[i][j] = Integer.parseInt(line[j]);
            }

        }
        if (this.startNode > rows - 1) {
            System.out.println("Please enter valid city index");
            return null;
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
