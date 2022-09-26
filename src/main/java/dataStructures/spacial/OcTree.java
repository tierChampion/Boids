package dataStructures.spacial;

import org.joml.Vector3f;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class OcTree<E> {

    private List<E> data = new ArrayList<>();
    private List<OctoElement> elements = new ArrayList<>();
    private List<OctoNode> nodes = new ArrayList<>();

    private int capacity;
    // KEEPS THE FIRST LEVEL OF NODES WHICH ARE NOT USED
    // MIGHT WANT TO USE MANY OF THEM SO THAT IT CAN HAVE MANY SETS OF FREE NODE
    private int freeNode = -1;

    public OcTree(List<E> data, int capacity, Vector3f initialCorner, Vector3f initialDimensions) {
        this.capacity = capacity;
        prepData(data);
        insertInitialData(initialCorner, initialDimensions);
    }

    private void prepData(List<E> data) {
        this.data = data;
        for (int i = 0; i < data.size(); i++) {
            this.elements.add(new OctoElement(i));
        }
    }

    private void insertInitialData(Vector3f initialCorner, Vector3f initialDimensions) {
        // ADD ROOT NODE
        this.nodes.add(new OctoNode(initialCorner, initialDimensions));
        for (int i = 0; i < this.elements.size(); i++) {
            // INSERT THE I-TH ELEMENT, STARTING FROM THE ROOT NODE
            insertElementInNode(i, 0);
        }
    }

    private boolean insertElementInNode(int currentElement, int currentNode) {
        // CHECK IF THE POSITION IS INSIDE NODE
        if (elementPositionIsContained(currentElement, currentNode, 0)) {
            OctoNode presentNode = this.nodes.get(currentNode);
            // MAJOR CASE 1: NODE IS AT THE BOTTOM OF THE TREE
            if (presentNode.isALeaf() && !presentNode.isOverFlowing(this.capacity)) {
                // MINOR CASE 1: NODE IS EMPTY
                if (presentNode.isAnEmptyLeaf()) {
                    // Set first child
                    presentNode.setFirstChild(currentElement);
                }
                // MINOR CASE 2: NODE IS NOT EMPTY
                else {
                    // Find index of the last element in the list
                    int indexInElementList = presentNode.getFirstChild();
                    int nextInList = indexInElementList;
                    int compteur = 0;
                    while (nextInList != -1) {
                        indexInElementList = nextInList;
                        nextInList = this.elements.get(indexInElementList).getNextElement();
                        compteur++;
                        if (compteur > capacity + 5) {
                            System.out.println("HELP!");
                        }
                    }

                    this.elements.get(indexInElementList).setNextElement(currentElement);
                }
                presentNode.addElement();
                return true;
            }
            // MAJOR CASE 2: NODE IS AT ITS MAXIMUM CAPACITY AND NEED TO BE EXTENDED
            else if (presentNode.isALeaf()) {

                int listSize = presentNode.getCount();
                int element = presentNode.getFirstChild();
                presentNode.setToBranch();
                // SET NODES
                if (freeNode == -1) {
                    Vector3f corner = presentNode.getCorner();
                    Vector3f newDimensions = presentNode.getHalfDimensions();

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

                // STORE ELEMENT IN PRESENT NODE IN CHILDREN
                // THIS IS A PROBLEM
                for (int i = 0; i < listSize; i++) {
                    OctoElement elementToPlace = this.elements.get(element);
                    element = elementToPlace.getNextElement();
                    if (element == -1 && i < listSize - 1) {
                        System.out.println("WHEN STORING ELEMENTS IN THE NEW CHILDREN, AN ELEMENT IN THE MIDDLE OF THE " +
                                "LIST WAS -1");
                    }
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
            // MAJOR CASE 3: NODE IS A BRANCH, DO THE OTHER CASES WITH ITS CHILDREN
            else {
                // BRANCH, SO GO TO CHILDREN (RECURSIVE)
                boolean elementWasInserted = false;
                int i = 0;
                while (!elementWasInserted && i < 8) {
                    int childIndex = presentNode.getFirstChild() + i;
                    elementWasInserted = insertElementInNode(currentElement, childIndex);
                    i++;
                }
                return true;
            }
        } else {
            return false;
        }
    }

    public List<E> query(Vector3f position, float radius) {
        LinkedList<Integer> ids = getQueriedId(position, radius, 0);
        List<E> queriedData = new ArrayList<>();
        for (int id : ids) {
            queriedData.add(this.data.get(id));
        }
        return queriedData;
    }

    private LinkedList<Integer> getQueriedId(Vector3f position, float radius, int node) {

        LinkedList<Integer> queriedElements = new LinkedList<>();
        OctoNode currentNode = this.nodes.get(node);

        if (!currentNode.contains(position.x, position.y, position.z, radius)) {
            return queriedElements;
        }
        if (currentNode.isALeaf()) {
            if (!currentNode.isAnEmptyLeaf()) {
                int element = currentNode.getFirstChild();
                int nextElement = element;
                for (int i = 0; i < currentNode.getCount(); i++) {
                    Vector3f elementPosition = getObjectPosition(this.data.get(nextElement));
                    if (new Vector3f(elementPosition).sub(position).lengthSquared()
                            < radius * radius) {
                        queriedElements.add(nextElement);
                    }
                    element = nextElement;
                    nextElement = this.elements.get(element).getNextElement();
                }
            }
            return queriedElements;
        } else {
            int firstChildNode = currentNode.getFirstChild();
            for (int i = 0; i < 8; i++) {
                queriedElements.addAll(getQueriedId(position, radius, firstChildNode + i));
            }
        }

        return queriedElements;
    }

    public void update() {

        // GET ELEMENTS THAT ARE NO LONGER IN THE NODE, ADD THEM TO THE LIST OF ONES TO UPDATE
        LinkedList<Integer> elementsToMove = getElementsToReinsert(0);
        // REINSERT THEM IN THE OCTREE
        for (int element : elementsToMove) {
            insertElementInNode(element, 0);
        }

        // CLEAN UP THE NODES THAT ARE TOTALLY EMPTY
        cleanUp();
    }

    // ANOTHER PROBLEM
    public LinkedList<Integer> getElementsToReinsert(int node) {
        LinkedList<Integer> elementsToUpdate = new LinkedList<>();

        OctoNode currentNode = this.nodes.get(node);
        if (currentNode.isAnEmptyLeaf()) {
            return elementsToUpdate;
        } else if (currentNode.isALeaf()) {
            int element = currentNode.getFirstChild();
            int nextElement = element;

            // WORK IF THE REMOVED ELEMENT IS AT THE END

            for (int i = 0; i < currentNode.getCount(); i++) {
                // System.out.println("BOOM");
                if (nextElement == -1) {
                    System.out.println("IN GET REINSERTED, AN ELEMENT IN THE MIDDLE OF THE LIST IS -1");
                }
                Vector3f elementPosition = getObjectPosition(this.data.get(nextElement));
                if (!currentNode.contains(
                        elementPosition.x, elementPosition.y, elementPosition.z, 0)) {
                    elementsToUpdate.add(nextElement);
                    // DECOUPLE IT IN THE LINKED LIST
                    // TROUBLE CASE: THE FIRST ELEMENT IN LIST IS THE WRONG ONE
                    if (nextElement == currentNode.getFirstChild()) {
                        if (currentNode.getCount() == 1) {
                            currentNode.removeElement();
                            currentNode.setFirstChild(-1);
                        } else {
                            nextElement = this.elements.get(nextElement).getNextElement();
                            this.elements.get(element).setNextElement(-1);
                            // PROBLEM, THEY ARE SET TO THE SAME THING
                            element = nextElement;
                            currentNode.setFirstChild(nextElement);
                            currentNode.removeElement();
                        }
                    } else {
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
                    element = nextElement;
                    nextElement = this.elements.get(nextElement).getNextElement();
                }
            }
            return elementsToUpdate;
        } else {
            // ADD ALL ELEMENTS TO UPDATE OF CHILDREN
            int childNode = currentNode.getFirstChild();
            for (int i = 0; i < 8; i++) {
                elementsToUpdate.addAll(getElementsToReinsert(childNode + i));
            }
        }
        return elementsToUpdate;
    }

    private void cleanUp() {

        LinkedList<Integer> nodesToProcess = new LinkedList<>();
        // PROCESS ROOT IF NOT LEAF
        if (this.nodes.get(0).getCount() == -1) {
            nodesToProcess.add(0);
        }

        while (nodesToProcess.size() > 0) {
            int nodeIndex = nodesToProcess.removeLast();
            OctoNode currentNode = this.nodes.get(nodeIndex);
            int numEmptyLeaves = 0;
            for (int i = 0; i < 8; i++) {
                int childIndex = currentNode.getFirstChild() + i;
                OctoNode child = this.nodes.get(childIndex);
                // CHECK ITS CHILDREN, KEEP THE NUMBER OF THEM THAT ARE EMPTY AND ADD THE NON LEAF
                // CHILDREN TO THE LIST TO PROCESS
                if (child.isAnEmptyLeaf()) {
                    numEmptyLeaves++;
                } else if (!child.isALeaf()) {
                    nodesToProcess.add(childIndex);
                }
            }
            // IF ALL CHILDREN ARE EMPTY, REMOVE THEM AND MAKE THIS NODE A NEW EMPTY LEAF
            if (numEmptyLeaves == 8) {
                freeNode = currentNode.getFirstChild();
                currentNode.setToLeaf();
            }
        }
    }

    private boolean elementPositionIsContained(int elementId, int nodeId, float radius) {
        Vector3f position = getObjectPosition(this.data.get(elementId));
        OctoNode node = this.nodes.get(nodeId);

        assert position != null;
        return node.contains(position.x, position.y, position.z, radius);
    }

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
