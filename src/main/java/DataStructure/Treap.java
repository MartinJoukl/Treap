package DataStructure;

import org.apache.commons.lang3.RandomUtils;

public class Treap<K extends Comparable<K>, V> {
    private TreeElement root;
    private int count;

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
            root = new TreeElement(key, value, RandomUtils.nextLong(0, 100), null);
            return root.data;
        }
        TreeElement parent = getLastPossibleElement(key);
        TreeElement insertedElement = new TreeElement(key, value, RandomUtils.nextLong(0, 100), parent);

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
        if (isNullOrLeaf(toRemove)) {
            //It is leaf, just remove

            if (toRemove.parent == null) {
                root = null;
            } else {
                if (toRemove.parent.leftChild == toRemove) {
                    toRemove.parent.leftChild = null;
                } else {
                    toRemove.parent.rightChild = null;
                }
            }
        } else {
            TreeElement rightChild = toRemove.rightChild;
            TreeElement leftChild = toRemove.leftChild;
            TreeElement removedElementParent = toRemove.parent;
            if (isNullOrLeaf(rightChild) && isNullOrLeaf(leftChild)) {
                TreeElement childWithHigherPriority;
                TreeElement otherChild;


                if (rightChild.priority.compareTo(leftChild.priority) >= 0) {
                    childWithHigherPriority = rightChild;
                    otherChild = leftChild;
                } else {
                    childWithHigherPriority = leftChild;
                    otherChild = rightChild;
                }
                // set connect parent
                childWithHigherPriority.parent = removedElementParent;

                // removed element was root
                if (removedElementParent == null) {
                    root = childWithHigherPriority;
                } else {
                    // Determine which side of the removed element's parent should be replaced
                    if (removedElementParent.leftChild == toRemove) {
                        removedElementParent.leftChild = childWithHigherPriority;

                    } else {
                        removedElementParent.rightChild = childWithHigherPriority;
                    }
                }
                //insert other child to correct position
                insertElementToCorrectSide(childWithHigherPriority, otherChild);
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
            insertedElement.parent = previousParent.parent;

            if (previousParent.leftChild == insertedElement) {
                // pravá rotace
                performRightRotation(insertedElement, previousParent);
            } else {
                // levá rotace
                performLeftRotation(insertedElement, previousParent);
            }
        }
    }

    private void performLeftRotation(TreeElement rotatedElement, TreeElement parent) {
        parent.rightChild = rotatedElement.leftChild;
        if (parent.rightChild != null) {
            parent.rightChild.parent = parent;
        }
        rotatedElement.leftChild = parent;
        rotatedElement.leftChild.parent = rotatedElement;
    }

    private void performRightRotation(TreeElement rotatedElement, TreeElement parent) {
        parent.leftChild = rotatedElement.rightChild;
        if (parent.leftChild != null) {
            parent.leftChild.parent = parent;
        }
        rotatedElement.rightChild = parent;
        rotatedElement.rightChild.parent = rotatedElement;
    }

    private void PerformDeleteRotation(TreeElement toRemove) {
        while (!isNullOrLeaf(toRemove)) {
            TreeElement childWithHigherPriority;


            if (toRemove.leftChild == null || toRemove.rightChild.priority.compareTo(toRemove.leftChild.priority) >= 0) {
                childWithHigherPriority = toRemove.rightChild;
            } else {
                childWithHigherPriority = toRemove.leftChild;
            }

            if (childWithHigherPriority.parent.leftChild == toRemove) {
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

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    private class TreeElement {
        Long priority;
        V data;
        K key;
        TreeElement leftChild;
        TreeElement rightChild;
        TreeElement parent;

        public TreeElement(K key, V data, Long priority, TreeElement parent) {
            this.priority = priority;
            this.data = data;
            this.key = key;
            this.parent = parent;
        }
    }
}
