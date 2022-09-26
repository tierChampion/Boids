package dataStructures.spacial;

import org.joml.Vector3f;

public class OctoNode {

    private int firstChild = -1;
    // count != 1 means it is not a leaf
    private int count = 0;

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

    protected boolean contains(float x, float y, float z, float radius) {
        if (this.x > x + radius) return false;
        if (this.y > y + radius) return false;
        if (this.z > z + radius) return false;
        if (this.width + this.x < x - radius) return false;
        if (this.height + this.y < y - radius) return false;
        if (this.length + this.z < z - radius) return false;
        return true;
    }

    protected boolean isALeaf() {
        return this.count != -1;
    }

    protected boolean isAnEmptyLeaf() {
        return this.count == 0;
    }

    protected boolean isOverFlowing(int capacity) {
        return this.count >= capacity;
    }

    protected int getFirstChild() {
        return firstChild;
    }

    protected int getCount() {
        return this.count;
    }

    protected void setFirstChild(int firstChild) {
        this.firstChild = firstChild;
    }

    protected void setToLeaf() {
        this.count = 0;
        this.firstChild = -1;
    }

    protected void setToBranch() {
        this.count = -1;
    }

    protected void addElement() {
        this.count++;
    }

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
