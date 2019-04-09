package akkaHW2019S;

import akka.actor.ActorRef;

import java.util.List;

public class SearchMessage {

    private List<ActorRef> actorRefList;

    private double[][] citiesArray;

    private int startNode;

    public SearchMessage(List<ActorRef> actorRefList, double[][] citiesArray, int startNode) {
        this.actorRefList = actorRefList;
        this.citiesArray = citiesArray;
        this.startNode = startNode;
    }

    public List<ActorRef> getActorRefList() {
        return actorRefList;
    }

    public void setActorRefList(List<ActorRef> actorRefList) {
        this.actorRefList = actorRefList;
    }

    public double[][] getCitiesArray() {
        return citiesArray;
    }

    public void setCitiesArray(double[][] citiesArray) {
        this.citiesArray = citiesArray;
    }

    public int getStartNode() {
        return startNode;
    }

    public void setStartNode(int startNode) {
        this.startNode = startNode;
    }
}
