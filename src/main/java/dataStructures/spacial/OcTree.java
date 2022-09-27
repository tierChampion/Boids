package dataStructures.spacial;

import org.joml.Vector3f;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Octree spacial data structure. Recursively split space equally in 8 octants
 *
 * @param <E> Spacial datatype. Needs a getPosition function that returns a Vector3f
 */
public class OcTree<E> {

    //////////////////////////////////////////////////////////////////////////////
    // TREE PARAMETERS:                                                         //
    // DATA: RAW POSITIONAL DATA TYPES (BOIDS)                                  //
    // ELEMENTS: POINTERS TO THE DATA                                           //
    // NODES: BRANCHES THAT CONTAIN OTHER NODES OR LEAVES THAT CONTAIN ELEMENTS //
    //////////////////////////////////////////////////////////////////////////////
    private List<E> data = new ArrayList<>();
    private List<OctoElement> elements = new ArrayList<>();
    private List<OctoNode> nodes = new ArrayList<>();

    // Maximum number of children on a leaf
    private final int capacity;
    // Pointer to node not attached to any leaf
    private int freeNode = -1;

    /**
     * Create the octree data structure
     *
     * @param data              Raw data type
     * @param capacity          Maximum elements per node
     * @param initialCorner     3D position of the corner of the octree
     * @param initialDimensions 3D dimensions of the octree
     */
    public OcTree(List<E> data, int capacity, Vector3f initialCorner, Vector3f initialDimensions) {
        this.capacity = capacity;
        prepData(data);
        insertInitialData(initialCorner, initialDimensions);
    }

    /**
     * Create the octree elements from the raw data
     *
     * @param data Raw data
     */
    private void prepData(List<E> data) {
        this.data = data;
        for (int i = 0; i < data.size(); i++) {
            this.elements.add(new OctoElement(i));
        }
    }

    /**
     * Insert all elements inside the tree
     *
     * @param initialCorner     Corner of the octree
     * @param initialDimensions Dimensions of the octree
     */
    private void insertInitialData(Vector3f initialCorner, Vector3f initialDimensions) {
        // Add the root node
        this.nodes.add(new OctoNode(initialCorner, initialDimensions));
        // Add all the following elements
        for (int i = 0; i < this.elements.size(); i++) {
            insertElementInNode(i, 0);
        }
    }

    /**
     * Insert a single element inside the octree
     *
     * @param currentElement element to insert
     * @param currentNode    node to check for insertion
     * @return whether the element was successfully inserted
     */
    private boolean insertElementInNode(int currentElement, int currentNode) {

        // Process if element is inside the node
        if (!elementPositionIsContained(currentElement, currentNode, 0)) return false;

        OctoNode presentNode = this.nodes.get(currentNode);
        // Add element to normal leaf
        if (presentNode.isALeaf() && !presentNode.isOverFlowing(this.capacity)) {
            if (presentNode.isAnEmptyLeaf()) {
                presentNode.setFirstChild(currentElement);
            }
            else {
                // Find index of the last element in the list
                int indexInElementList = presentNode.getFirstChild();
                int nextInList = indexInElementList;
                while (nextInList != -1) {
                    indexInElementList = nextInList;
                    nextInList = this.elements.get(indexInElementList).getNextElement();
                }

                this.elements.get(indexInElementList).setNextElement(currentElement);
            }
            presentNode.addElement();
            return true;
        }
        // Expand leaf to branch before inserting
        else if (presentNode.isALeaf()) {

            int listSize = presentNode.getCount();
            int element = presentNode.getFirstChild();
            presentNode.setToBranch();
            if (freeNode == -1) {
                Vector3f corner = presentNode.getCorner();
                Vector3f newDimensions = presentNode.getHalfDimensions();

                // Add all 8 new nodes
                OctoNode backLeftDown = new OctoNode(corner, newDimensions);
                OctoNode backLeftUp =
                        new OctoNode(
                                new Vector3f(corner).add(0, newDimensions.y, 0), newDimensions);
                OctoNode backRightDown =
                        new OctoNode(
                                new Vector3f(corner).add(newDimensions.x, 0, 0), newDimensions);
                OctoNode backRightUp =
                        new OctoNode(
                                new Vector3f(corner).add(newDimensions.x, newDimensions.y, 0),
                                newDimensions);
                OctoNode frontLeftDown =
                        new OctoNode(
                                new Vector3f(corner).add(0, 0, newDimensions.z), newDimensions);
                OctoNode frontLeftUp =
                        new OctoNode(
                                new Vector3f(corner).add(0, newDimensions.y, newDimensions.z),
                                newDimensions);
                OctoNode frontRightDown =
                        new OctoNode(
                                new Vector3f(corner).add(newDimensions.x, 0, newDimensions.z),
                                newDimensions);
                OctoNode frontRightUp =
                        new OctoNode(new Vector3f(corner).add(newDimensions), newDimensions);

                presentNode.setFirstChild(this.nodes.size());
                this.nodes.add(backLeftDown);
                this.nodes.add(backLeftUp);
                this.nodes.add(backRightDown);
                this.nodes.add(backRightUp);
                this.nodes.add(frontLeftDown);
                this.nodes.add(frontLeftUp);
                this.nodes.add(frontRightDown);
                this.nodes.add(frontRightUp);
            } else {
                presentNode.setFirstChild(freeNode);
                freeNode = -1;
            }

            // Store the elements that were in the leaf
            for (int i = 0; i < listSize; i++) {
                OctoElement elementToPlace = this.elements.get(element);
                element = elementToPlace.getNextElement();
                elementToPlace.setNextElement(-1);
                boolean elementWasInserted = false;
                int j = 0;
                while (!elementWasInserted && j < 8) {
                    int childIndex = presentNode.getFirstChild() + j;
                    elementWasInserted =
                            insertElementInNode(elementToPlace.getId(), childIndex);
                    j++;
                }
            }
            return true;
        }
        // Add element to branch
        else {
            boolean elementWasInserted = false;
            int i = 0;
            while (!elementWasInserted && i < 8) {
                int childIndex = presentNode.getFirstChild() + i;
                elementWasInserted = insertElementInNode(currentElement, childIndex);
                i++;
            }
            return true;
        }
    }

    /**
     * Find the objects that overlap with a sphere
     *
     * @param position position of the center of the sphere
     * @param radius   radius of the sphere
     * @return list of objects in the sphere
     */
    public List<E> query(Vector3f position, float radius) {
        LinkedList<Integer> ids = getQueriedId(position, radius, 0);
        List<E> queriedData = new ArrayList<>();
        for (int id : ids) {
            queriedData.add(this.data.get(id));
        }
        return queriedData;
    }

    /**
     * Get the pointers to the elements that are within the sphere
     *
     * @param position position of the center of the sphere
     * @param radius   radius of the sphere
     * @param node     current node
     * @return pointers of the queried elements
     */
    private LinkedList<Integer> getQueriedId(Vector3f position, float radius, int node) {

        LinkedList<Integer> queriedElements = new LinkedList<>();
        OctoNode currentNode = this.nodes.get(node);

        // Skip nodes that don't overlap with the sphere
        if (!currentNode.contains(position.x, position.y, position.z, radius)) {
            return queriedElements;
        }
        if (currentNode.isALeaf()) {
            // Add elements that overlap with the sphere
            if (!currentNode.isAnEmptyLeaf()) {
                int element = currentNode.getFirstChild();
                int nextElement = element;
                for (int i = 0; i < currentNode.getCount(); i++) {
                    Vector3f elementPosition = getObjectPosition(this.data.get(nextElement));
                    if (elementPosition != null && new Vector3f(elementPosition).sub(position).lengthSquared()
                            < radius * radius) {
                        queriedElements.add(nextElement);
                    }
                    element = nextElement;
                    nextElement = this.elements.get(element).getNextElement();
                }
            }
            return queriedElements;
        } else {
            // Process sub-nodes
            int firstChildNode = currentNode.getFirstChild();
            for (int i = 0; i < 8; i++) {
                queriedElements.addAll(getQueriedId(position, radius, firstChildNode + i));
            }
        }

        return queriedElements;
    }

    /**
     * Update the octree after the raw data moved
     */
    public void update() {

        LinkedList<Integer> elementsToMove = getElementsToReinsert(0);
        for (int element : elementsToMove) {
            insertElementInNode(element, 0);
        }

        cleanUp();
    }

    /**
     * Get the list of elements that are no longer inside their node and need to be reinserted into the tree
     *
     * @param node Current node
     * @return list of pointers with no leaf
     */
    public LinkedList<Integer> getElementsToReinsert(int node) {
        LinkedList<Integer> elementsToUpdate = new LinkedList<>();

        OctoNode currentNode = this.nodes.get(node);
        if (currentNode.isAnEmptyLeaf()) {
            return elementsToUpdate;
        } else if (currentNode.isALeaf()) {
            int element = currentNode.getFirstChild();
            int nextElement = element;

            for (int i = 0; i < currentNode.getCount(); i++) {
                // Check if element is still inside leaf
                Vector3f elementPosition = getObjectPosition(this.data.get(nextElement));
                if (elementPosition != null && !currentNode.contains(
                        elementPosition.x, elementPosition.y, elementPosition.z, 0)) {
                    elementsToUpdate.add(nextElement);
                    // Remove element from node
                    if (nextElement == currentNode.getFirstChild()) {
                        if (currentNode.getCount() == 1) {
                            // Set to empty leaf
                            currentNode.removeElement();
                            currentNode.setFirstChild(-1);
                        } else {
                            // Set new first element
                            nextElement = this.elements.get(nextElement).getNextElement();
                            this.elements.get(element).setNextElement(-1);
                            element = nextElement;
                            currentNode.setFirstChild(nextElement);
                            currentNode.removeElement();
                        }
                    } else {
                        // Link element before with element after
                        this.elements
                                .get(element)
                                .setNextElement(this.elements.get(nextElement).getNextElement());
                        this.elements.get(nextElement).setNextElement(-1);
                        nextElement = this.elements.get(element).getNextElement();
                        currentNode.removeElement();
                        if (currentNode.isAnEmptyLeaf()) {
                            currentNode.setFirstChild(-1);
                        }
                    }

                } else {
                    // Step forward in the elements of the leaf
                    element = nextElement;
                    nextElement = this.elements.get(nextElement).getNextElement();
                }
            }
            return elementsToUpdate;
        } else {
            // Process all the sub-nodes
            int childNode = currentNode.getFirstChild();
            for (int i = 0; i < 8; i++) {
                elementsToUpdate.addAll(getElementsToReinsert(childNode + i));
            }
        }
        return elementsToUpdate;
    }

    /**
     * Cleans up the tree. If all children of a branch are empty, then that branch becomes a new leaf
     */
    private void cleanUp() {

        // Stack of branches to process
        LinkedList<Integer> nodesToProcess = new LinkedList<>();

        // Add the root if it is a branch
        if (!this.nodes.get(0).isALeaf()) {
            nodesToProcess.add(0);
        }
        while (nodesToProcess.size() > 0) {
            int nodeIndex = nodesToProcess.removeLast();
            OctoNode currentNode = this.nodes.get(nodeIndex);
            int numEmptyLeaves = 0;
            // Check all nodes in the branch
            for (int i = 0; i < 8; i++) {
                int childIndex = currentNode.getFirstChild() + i;
                OctoNode child = this.nodes.get(childIndex);
                if (child.isAnEmptyLeaf()) {
                    numEmptyLeaves++;
                    // Node is a branch and need to be processed
                } else if (!child.isALeaf()) {
                    nodesToProcess.add(childIndex);
                }
            }
            // Change branch to leaf
            if (numEmptyLeaves == 8) {
                freeNode = currentNode.getFirstChild();
                currentNode.setToLeaf();
            }
        }
    }

    /**
     * Check if the element is contained in the node
     *
     * @param elementId Element to check for containment
     * @param nodeId    Node to check for encasement
     * @param radius    Range of containment
     * @return whether the element is inside the node
     */
    private boolean elementPositionIsContained(int elementId, int nodeId, float radius) {
        Vector3f position = getObjectPosition(this.data.get(elementId));
        OctoNode node = this.nodes.get(nodeId);

        assert position != null;
        return node.contains(position.x, position.y, position.z, radius);
    }

    /**
     * Return the position of the element inside the octree
     *
     * @param object Object of the data type to find the position of
     * @return Position of the object
     */
    private Vector3f getObjectPosition(E object) {
        Method method;

        try {
            // GET THE METHOD AND RETURN THE METHOD
            method = object.getClass().getMethod("getPosition");
            return (Vector3f) method.invoke(object);
        } catch (NoSuchMethodException e) {
            System.err.println(
                    "Method in the class "
                            + object.getClass()
                            + " with name getPosition"
                            + " not found.");
        } catch (IllegalAccessException | InvocationTargetException e) {
            System.err.println(
                    "Method in the class "
                            + object.getClass()
                            + " with name  getPosition"
                            + " could not be called.");
        }
        return null;
    }
}
