package akkaHW2019S;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Solution {

    private double minTourCost;

    private List<Integer> tour;

    private Date time;

    private long timeinMillis;

    private String agentName;


    public Solution(double minTourCost, List<Integer> tour, Date time, long timeinMillis) {
        this.minTourCost = minTourCost;
        this.tour = tour;
        this.time = time;
        this.timeinMillis = timeinMillis;
    }

    public double getMinTourCost() {
        return minTourCost;
    }

    public void setMinTourCost(double minTourCost) {
        this.minTourCost = minTourCost;
    }

    public List<Integer> getTour() {
        return tour;
    }

    public void setTour(List<Integer> tour) {
        this.tour = tour;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    @Override
    public String toString() {
        return "Solution{" +
                "minTourCost=" + minTourCost +
                ", tour=" + tour +
                ", time=" + time +
                ", timeinMillis=" + timeinMillis +
                ", agentName='" + agentName + '\'' +
                '}';
    }
}
