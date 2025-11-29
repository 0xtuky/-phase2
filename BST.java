
public class BST<T> {
    private BSTNode<T> root;

    public BST() {
        this.root = null;
    }

    // ================= INSERT (O(log n)) =================
    public void insert(int key, T data) {
        root = insertRec(root, key, data);
    }

    private BSTNode<T> insertRec(BSTNode<T> root, int key, T data) {
        // If the tree is empty, return a new node
        if (root == null) {
            return new BSTNode<>(key, data);
        }

        // Otherwise, recur down the tree
        if (key < root.key) {
            root.left = insertRec(root.left, key, data);
        } else if (key > root.key) {
            root.right = insertRec(root.right, key, data);
        } else {
            // Key already exists, update data (optional, depends on requirement)
            root.data = data;
        }

        return root;
    }

    // ================= FIND (O(log n)) =================
    public T find(int key) {
        BSTNode<T> res = findRec(root, key);
        if (res != null) {
            return res.data;
        }
        return null;
    }

    private BSTNode<T> findRec(BSTNode<T> root, int key) {
        if (root == null || root.key == key) {
            return root;
        }
        if (key < root.key) {
            return findRec(root.left, key);
        }
        return findRec(root.right, key);
    }

    // ================= DELETE =================
    public boolean delete(int key) {
        if (find(key) == null) return false;
        root = deleteRec(root, key);
        return true;
    }

    private BSTNode<T> deleteRec(BSTNode<T> root, int key) {
        if (root == null) return root;

        if (key < root.key) {
            root.left = deleteRec(root.left, key);
        } else if (key > root.key) {
            root.right = deleteRec(root.right, key);
        } else {
            // Node with only one child or no child
            if (root.left == null) return root.right;
            if (root.right == null) return root.left;

            // Node with two children: Get the inorder successor (smallest in the right subtree)
            root.key = minValue(root.right);
            root.data = find(root.key); // Update data to match the moved key

            // Delete the inorder successor
            root.right = deleteRec(root.right, root.key);
        }
        return root;
    }

    private int minValue(BSTNode<T> root) {
        int minv = root.key;
        while (root.left != null) {
            minv = root.left.key;
            root = root.left;
        }
        return minv;
    }

    // ================= TRAVERSAL (To List) =================
    // Returns all elements in sorted order (In-Order Traversal)
    public CustomLinkedList<T> getAll() {
        CustomLinkedList<T> list = new CustomLinkedList<>();
        inOrderRec(root, list);
        return list;
    }

    private void inOrderRec(BSTNode<T> root, CustomLinkedList<T> list) {
        if (root != null) {
            inOrderRec(root.left, list);
            list.add(root.data);
            inOrderRec(root.right, list);
        }
    }
}