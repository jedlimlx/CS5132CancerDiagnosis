public class Node<T> {
    private T item;
    public void setItem(T item)
    {
        this.item = item;
    }
    public T getItem()
    {
        return this.item;
    }

    private Integer feature;
    public void setFeature(Integer feature)
    {
        this.feature = feature;
    }
    public Integer getFeature()
    {
        return this.feature;
    }

    private Double threshhold;
    public void setThreshhold(Double threshhold)
    {
        this.threshhold = threshhold;
    }
    public Double getThreshhold()
    {
        return this.threshhold;
    }

    Node<T>[] neighbours; //Package accessibility
    public void setLeft(Node<T> left)
    {
        this.neighbours[0] = left;
    }
    public Node<T> getLeft()
    {
        return this.neighbours[0];
    }

    public void setRight(Node<T> right)
    {
        this.neighbours[1] = right;
    }
    public Node<T> getRight()
    {
        return this.neighbours[1];
    }

    public Node(Integer feature, Double threshhold, Node<T> left, Node<T> right, T item)
    {
        setFeature(feature);

        setThreshhold(threshhold);

        this.neighbours = new Node[2];
        setLeft(left);
        setRight(right);

        setItem(item);
    }

    public Node(Integer feature, Double threshold, Node<T> left, Node<T> right)
    {
        this(feature, threshold, left, right, null);
    }

    public Node(T item)
    {
        this(null, null, null, null, item);
    }

    public boolean is_leaf()
    {
        return getItem() != null;
    }

    public String toString()
    {
        String line1 = "Item is: " + getItem();
        String line2 = "Feature is: " + getFeature();
        String line3 = "Threshhold is: " + getThreshhold();

        String line4LeftHalf, line4RightHalf;
        if(getLeft() != null)
            line4LeftHalf = "Left is not null";
        else
            line4LeftHalf = "Left is null";

        if(getRight() != null)
            line4RightHalf = "Right is not null";
        else
            line4RightHalf = "Right is null";

        String line4 = line4LeftHalf + ", " + line4RightHalf;
        return line1 + "\n" + line2 + "\n" + line3 + "\n" + line4;
    }
}
