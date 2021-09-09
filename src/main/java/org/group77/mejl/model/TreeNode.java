package org.group77.mejl.model;


import java.io.Serializable;
import java.util.*;


public class TreeNode<T> implements Serializable{
    private TreeNode<T> parent;
    private final List<TreeNode<T>> children = new ArrayList<>();
    private final T t;

    public TreeNode(T t) {
        this.t = t;
        this.parent = null;
    }

    public TreeNode(T t, TreeNode<T> parent) {
        this.t = t;
        this.parent = parent;
    }

    public void add(TreeNode<T> node){
        getChildren().add(node);
        node.setParent(this);
    }

    public void setParent(TreeNode<T> parent){
       this.parent = parent;
    }

    public T getT() {
        return t;
    }

    public TreeNode<T> getRoot(){
        TreeNode<T> node = this;
        while(true){
            if(node.getParent() == null){
                return node;
            }
            node = node.getParent();
        }

    }

    @Override
    public String toString() {
        return t.toString();
    }

    public List<TreeNode<T>> getChildren(){
        return this.children;
    }

    protected TreeNode<T> getParent(){
        return this.parent;
    }
}

