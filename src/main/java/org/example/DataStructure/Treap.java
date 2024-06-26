package org.example.DataStructure;

import org.apache.commons.lang3.StringUtils;
import org.example.Domain.IGenerable;

import java.util.ArrayList;
import java.util.List;

public class Treap<K extends Comparable<K>, V, P extends Comparable<P>> {
    private TreeElement root;
    private int count;
    private final IGenerable<P> generable;

    public Treap(IGenerable<P> generable) {
        this.generable = generable;
    }

    public V find(K key) {
        TreeElement current = getTreeElement(key);
        if (current != null) {
            return current.data;
        } else {
            throw new RuntimeException("Item with key " + key + " was not found!");
        }
    }

    private TreeElement getTreeElement(K key) {
        TreeElement current = root;
        while (current != null && !key.equals(current.key)) {
            if (key.compareTo(current.key) < 0) {
                current = current.leftChild;
            } else {
                current = current.rightChild;
            }
        }
        return current;
    }

    private TreeElement getLastPossibleElement(K key) {
        TreeElement current = root;
        TreeElement lastPossibleElement = root;
        while (current != null && !key.equals(current.key)) {
            lastPossibleElement = current;
            if (key.compareTo(current.key) < 0) {
                current = current.leftChild;
            } else {
                current = current.rightChild;
            }
        }
        return lastPossibleElement;
    }

    public V insert(K key, V value) {
        count++;
        if (root == null) {
            root = new TreeElement(key, value, generable.generate(), null);
            return root.data;
        }
        TreeElement parent = getLastPossibleElement(key);
        TreeElement insertedElement = new TreeElement(key, value, generable.generate(), parent);

        insertElementToCorrectSide(parent, insertedElement);


        PerformInsertRotation(insertedElement);
        // We must also set correct root after rotations if inserted element should be root
        if (insertedElement.parent == null) {
            root = insertedElement;
        }
        return value;
    }

    private void insertElementToCorrectSide(TreeElement parent, TreeElement insertedElement) {
        if (insertedElement.key.compareTo(parent.key) < 0) {
            parent.leftChild = insertedElement;
        } else {
            parent.rightChild = insertedElement;
        }
        insertedElement.parent = parent;
    }

    public V delete(K key) {
        TreeElement toRemove = getTreeElement(key);
        if (toRemove == null) {
            throw new RuntimeException("This will not throw exception, just for debugging");
        }
        count--;
        // First perform rotations while the element is not leaf
        PerformDeleteRotation(toRemove);

        // Then remove element
        if (toRemove.parent == null) {
            root = null;
        } else {
            if (toRemove.parent.leftChild == toRemove) {
                toRemove.parent.leftChild = null;
            } else {
                toRemove.parent.rightChild = null;
            }
        }

        return toRemove.data;
    }

    private boolean isNullOrLeaf(TreeElement element) {
        if (element == null) {
            return true;
        }
        return element.rightChild == null && element.leftChild == null;
    }

    private void PerformInsertRotation(TreeElement insertedElement) {
        while (insertedElement.parent != null && insertedElement.priority.compareTo(insertedElement.parent.priority) >= 1) {
            TreeElement previousParent = insertedElement.parent;
            //insertedElement.parent = previousParent.parent;

            if (previousParent.leftChild == insertedElement) {
                // pravá rotace
                performRightRotation(insertedElement, previousParent);
            } else {
                // levá rotace
                performLeftRotation(insertedElement, previousParent);
            }
        }
    }

    private void performLeftRotation(TreeElement rotatedElement, TreeElement previousParent) {
        updateParentReferences(rotatedElement, previousParent);

        previousParent.rightChild = rotatedElement.leftChild;
        if (previousParent.rightChild != null) {
            previousParent.rightChild.parent = previousParent;
        }
        rotatedElement.leftChild = previousParent;
        rotatedElement.leftChild.parent = rotatedElement;
    }

    private void performRightRotation(TreeElement rotatedElement, TreeElement previousParent) {
        // Update reference to parent and also parent.parent children
        updateParentReferences(rotatedElement, previousParent);

        previousParent.leftChild = rotatedElement.rightChild;
        if (previousParent.leftChild != null) {
            previousParent.leftChild.parent = previousParent;
        }
        rotatedElement.rightChild = previousParent;
        rotatedElement.rightChild.parent = rotatedElement;
    }

    private void updateParentReferences(TreeElement rotatedElement, TreeElement previousParent) {
        rotatedElement.parent = previousParent.parent;

        // previous parent child also needs to change
        if (previousParent.parent != null) {
            if (previousParent.parent.rightChild == previousParent) {
                previousParent.parent.rightChild = rotatedElement;
            } else if (previousParent.parent.leftChild == previousParent) {
                previousParent.parent.leftChild = rotatedElement;
            }
        }
    }

    private void PerformDeleteRotation(TreeElement toRemove) {
        while (!isNullOrLeaf(toRemove)) {
            TreeElement childWithHigherPriority;

            if (toRemove.leftChild == null || (toRemove.rightChild != null && toRemove.rightChild.priority.compareTo(toRemove.leftChild.priority) >= 0)) {
                childWithHigherPriority = toRemove.rightChild;
            } else {
                childWithHigherPriority = toRemove.leftChild;
            }

            if (toRemove.leftChild == childWithHigherPriority) {
                // pravá rotace
                performRightRotation(childWithHigherPriority, toRemove);
            } else {
                // levá rotace
                performLeftRotation(childWithHigherPriority, toRemove);
            }
            // Check if child is root
            if (childWithHigherPriority.parent == null) {
                root = childWithHigherPriority;
            }
        }
    }

    @Override
    public String toString() {
        List<TreeElement> list = new ArrayList<>();
        if (root != null) {
            list.add(root);
        } else {
            return "";
        }
        int numberOfElementsInRow = 1;
        int numberOfNullsInCurrentRow = 0;
        int gotElements = 0;

        List<String> rows = new ArrayList<>();
        List<List<String>> builtStrings = new ArrayList<>();

        while (!list.isEmpty()) {
            TreeElement currentElement = list.remove(0);
            gotElements++;
            if (currentElement != null) {
                rows.add(StringUtils.center(
                        String.format("%.14s", currentElement.key + (":") + currentElement.priority), 14)
                );

                list.add(currentElement.leftChild);
                list.add(currentElement.rightChild);
            } else {
                rows.add(String.format("%.14s", StringUtils.center("-".repeat(12), 14)));
                numberOfNullsInCurrentRow++;
                //Add virtual filling nulls
                list.add(null);
                list.add(null);
            }
            if (gotElements == numberOfElementsInRow) {
                if (numberOfNullsInCurrentRow == numberOfElementsInRow) {
                    break;
                }
                // next row
                numberOfElementsInRow *= 2;
                gotElements = 0;
                numberOfNullsInCurrentRow = 0;
                builtStrings.add(rows);
                rows = new ArrayList<>();
            }
        }
        StringBuilder resultBuilder = new StringBuilder();
        int numberOfElements = builtStrings.get(builtStrings.size() - 1).size();

        for (int i = 0; i < builtStrings.size(); i++) {
            List<String> currentElements = builtStrings.get(i);
            int numberOfCurrentElements = builtStrings.get(i).size();

            for (int j = 0; j < currentElements.size(); j++) {
                int addedCount = ((((numberOfElements / numberOfCurrentElements) / 2)) * 14 / 2);

                for (int k = i + 1; k < builtStrings.size(); k++) {
                    int numberOfNextElements = builtStrings.get(k).size();
                    if (numberOfNextElements > 0) {
                        addedCount += (((numberOfElements / numberOfNextElements) / 2)) * 14 / 2;
                    }
                }

                resultBuilder
                        .append(" ".repeat(addedCount))
                        .append(currentElements.get(j))
                        .append(" ".repeat(addedCount));
            }
            resultBuilder.append('\n');
        }
        return resultBuilder.toString();
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getTreeDepth() {
        List<TreeElement> list = new ArrayList<>();
        if (root != null) {
            list.add(root);
        }
        int maxDepth = 0;

        while (!list.isEmpty()) {
            TreeElement currentElement = list.remove(0);
            if (currentElement.leftChild != null) {
                list.add(currentElement.leftChild);
            }
            if (currentElement.rightChild != null) {
                list.add(currentElement.rightChild);
            }
            int currentDepth = 1;
            // test only leaves
            if (isNullOrLeaf(currentElement)) {
                while (currentElement.parent != null) {
                    currentDepth += 1;
                    currentElement = currentElement.parent;
                }
                if (currentDepth > maxDepth) {
                    maxDepth = currentDepth;
                }
            }
        }
        return maxDepth;
    }

    private class TreeElement {
        P priority;
        V data;
        K key;
        TreeElement leftChild;
        TreeElement rightChild;
        TreeElement parent;

        public TreeElement(K key, V data, P priority, TreeElement parent) {
            this.priority = priority;
            this.data = data;
            this.key = key;
            this.parent = parent;
        }
    }
}
