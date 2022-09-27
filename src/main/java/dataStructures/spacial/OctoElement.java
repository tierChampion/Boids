package dataStructures.spacial;

/**
 * Element of the octree
 */
public class OctoElement {

    // Position in the array of the element
    ////////////////////////////////////////////////////////////////////////////////////////////////////
    // VARIABLES:                                                                                     //
    // ID: POINTER TO THE OBJECT                                                                      //
    // NEXT_ELEMENT: POINTER TO THE NEXT ELEMENT IN THE LINKED LIST (-1 means it is the last element) //
    ////////////////////////////////////////////////////////////////////////////////////////////////////
    private final int id;
    private int nextElement = -1;

    protected OctoElement(int id) {
        this.id = id;
    }

    ////////////////////////////
    // PARAMETER MANIPULATORS //
    ////////////////////////////
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
