package dataStructures.spacial;

public class OctoElement {

    private final int id;

    private int nextElement = -1;

    protected OctoElement(int id) {
        this.id = id;
    }

    protected int getNextElement() {
        return nextElement;
    }

    protected void setNextElement(int nextElement) {
        this.nextElement = nextElement;
    }

    protected int getId(){
        return this.id;
    }

}
