package dataStructures.spacial;

import org.joml.Vector3f;

/**
 * Node of the octree. Stores dimension and position parameters
 */
public class OctoNode {

    ////////////////////////////////////////////////////////////////////////////////////////////
    // CHILDREN PARAMETERS:                                                                   //
    // FIRST_CHILD: POINTER TO THE FIRST ELEMENT OR NODE IN THE NODE                          //
    // COUNT: NUMBER OF ELEMENT IN THE NODE. A NEGATIVE MEANS THE NODE CONTAINS FURTHER NODES //
    ////////////////////////////////////////////////////////////////////////////////////////////
    private int firstChild = -1;
    private int count = 0;

    ////////////////////////
    // SPACIAL PARAMETERS //
    ////////////////////////
    private float x, y, z;
    private float width, height, length;

    protected OctoNode(Vector3f corner, Vector3f dimensions) {
        this.x = corner.x;
        this.y = corner.y;
        this.z = corner.z;
        this.width = dimensions.x;
        this.height = dimensions.y;
        this.length = dimensions.z;
    }

    /**
     * Check if a sphere overlaps with the node.
     * @param x x coordinate of the center of the sphere
     * @param y y coordinate of the center of the sphere
     * @param z z coordinate of the center of the sphere
     * @param radius radius of the sphere
     * @return whether the sphere overlaps with the node
     */
    protected boolean contains(float x, float y, float z, float radius) {
        if (this.x > x + radius) return false;
        if (this.y > y + radius) return false;
        if (this.z > z + radius) return false;
        if (this.width + this.x < x - radius) return false;
        if (this.height + this.y < y - radius) return false;
        if (this.length + this.z < z - radius) return false;
        return true;
    }

    /**
     * Checks if the node contains nodes or elements.
     * @return whether it is a leaf
     */
    protected boolean isALeaf() {
        return this.count != -1;
    }

    /**
     * Check if the node is a leaf that contains no elements
     * @return whether it is a leaf with no elements
     */
    protected boolean isAnEmptyLeaf() {
        return this.count == 0;
    }

    /**
     * Check if the node has reached the limit of elements
     * @param capacity maximum number of elements that the node can contain
     * @return whether it is overflowing
     */
    protected boolean isOverFlowing(int capacity) {
        return this.count >= capacity;
    }

    /**
     * Get the pointer to the first element of node of the node
     * @return pointer to the first child
     */
    protected int getFirstChild() {
        return firstChild;
    }

    /**
     * Get number of children in the node
     * @return count of children
     */
    protected int getCount() {
        return this.count;
    }

    /**
     * Set first element or node in the node
     * @param firstChild new first child
     */
    protected void setFirstChild(int firstChild) {
        this.firstChild = firstChild;
    }

    /**
     * Convert the node to a leaf
     */
    protected void setToLeaf() {
        this.count = 0;
        this.firstChild = -1;
    }

    /**
     * Set the node to a branch
     */
    protected void setToBranch() {
        this.count = -1;
    }

    /**
     * Add an element to a leaf
     */
    protected void addElement() {
        this.count++;
    }

    /**
     * Remove an element from a leaf
     */
    protected void removeElement() {
        this.count--;
    }

    protected Vector3f getCorner() {
        return new Vector3f(x, y, z);
    }

    protected Vector3f getHalfDimensions() {
        return new Vector3f(width/2, height/2, length/2);
    }
}
