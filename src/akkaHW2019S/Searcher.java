package akkaHW2019S;

import akka.actor.UntypedActor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * this actor implements the search for a path that satisfies the project requirements
 *
 * @author M. Kokar
 */
public class Searcher extends UntypedActor {

    private final int N;
    private final int START_NODE;
    private final int DESIRED_LENGTH;
    private final int FINISHED_STATE;

    private double[][] distance;
    private double minTourCost = Double.POSITIVE_INFINITY;

    private List<Integer> tour = new ArrayList<>();
    private boolean ranSolver = false;

    private Solution solution;
    //private final String name;


    public Searcher(int startNode, int desiredLength, double[][] distance) {
        this.distance = distance;
        this.DESIRED_LENGTH = desiredLength;
        N = distance.length;
        START_NODE = startNode;
        // Validate inputs.
        if (N <= 2) throw new IllegalStateException("TSP on 0, 1 or 2 nodes doesn't make sense.");
        if (N != distance[0].length) throw new IllegalArgumentException("Matrix must be square (N x N)");
        if (START_NODE < 0 || START_NODE >= N)
            throw new IllegalArgumentException("Starting node must be: 0 <= startNode < N");
        if (N > 32)
            throw new IllegalArgumentException("Matrix too large! A matrix that size for the DP TSP problem with a time complexity of" +
                    "O(n^2*2^n) requires way too much computation for any modern home computer to handle");
        // The finished state is when the finished state mask has all bits are set to
        // one (meaning all the nodes have been visited).
        FINISHED_STATE = (1 << N) - 1;
        // this.name = name;
        // TODO
    }


    @Override
    public void onReceive(Object msg) throws Throwable {
        //Code to implement
        if (msg instanceof String) {
            if (msg.equals("Start Solving")) {
                Solution sol = solve();
                this.solution = sol;
                //sol.setAgentName(this.name);
                sol.setAgentName(getSelf().path().name());
                //System.out.println(getSelf().path().name());
                //System.out.println(sol);
                getSender().tell(sol, getSelf());


            }
        }
        if (msg instanceof SearchMessage) {
            SearchMessage searchMessage = (SearchMessage) msg;
            Solution sol = solve();
            this.solution = sol;
            sol.setAgentName(getSelf().path().name());
//            for (ActorRef actorRef : searchMessage.getActorRefList()) {
//                actorRef.tell(sol, getSelf());
//            }
            getSender().tell(sol, getSelf());


        } else if (msg instanceof Solution) {
            Solution sol = (Solution) msg;
            String name = sol.getAgentName();
            double minTourCost = sol.getMinTourCost();
            if (sol.getMinTourCost() > this.DESIRED_LENGTH) {
                System.out.println(getSelf().path().name() + " Message: " + "Not able to find Path with length less than desired Length");
                context().stop(getSelf());
                return;
            }
            if (name.equals(getSelf().path().name())) {
                System.out.println(getSelf().path().name() + " Path: " + this.solution.getTour() + " Length: " + (int) this.minTourCost + " Message: " + "I Won");
            } else {
                System.out.println(getSelf().path().name() + " Path: " + this.solution.getTour() + " Length: " + (int) this.minTourCost + " Message: " + name + " won");
            }
            context().stop(getSelf());
        }
    }


    public Solution solve() {
        // Run the solver
        int state = 1 << START_NODE;
        Double[][] memo = new Double[N][1 << N];
        Integer[][] prev = new Integer[N][1 << N];
        minTourCost = tsp(START_NODE, state, memo, prev);
        // Regenerate path
        int index = START_NODE;
        while (true) {
            //tour.add((int) distance[0][index]);
            tour.add(index);
            Integer nextIndex = prev[index][state];
            if (nextIndex == null) break;
            int nextState = state | (1 << nextIndex);
            state = nextState;
            index = nextIndex;
        }
        //tour.add((int) distance[0][START_NODE]);
        tour.add(START_NODE);
        ranSolver = true;
        return new Solution(minTourCost, tour, new Date(), System.currentTimeMillis());


    }

    private double tsp(int i, int state, Double[][] memo, Integer[][] prev) {
        // Done this tour. Return cost of going back to start node.
        if (state == FINISHED_STATE) return distance[i][START_NODE];
        // Return cached answer if already computed.
        if (memo[i][state] != null) return memo[i][state];
        double minCost = Double.POSITIVE_INFINITY;
        int index = -1;
        for (int next = 0; next < N; next++) {
            // Skip if the next node has already been visited.
            if ((state & (1 << next)) != 0) continue;
            int nextState = state | (1 << next);
            double newCost = distance[i][next] + tsp(next, nextState, memo, prev);
            if (newCost < minCost) {
                minCost = newCost;
                index = next;
            }
        }
        prev[i][state] = index;
        return memo[i][state] = minCost;
    }

}
