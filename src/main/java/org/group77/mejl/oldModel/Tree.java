package org.group77.mejl.oldModel;


import java.io.Serializable;
import java.util.*;


public class Tree<T> implements Serializable{
    private Tree<T> parent;
    private final List<Tree<T>> children = new ArrayList<>();
    private final T t;

    public Tree(T t) {
        this.t = t;
        this.parent = null;
    }

    public Tree(T t, Tree<T> parent) {
        this.t = t;
        this.parent = parent;
    }

    public void add(Tree<T> node){
        getChildren().add(node);
        node.setParent(this);
    }

    public void setParent(Tree<T> parent){
       this.parent = parent;
    }

    public T getT() {
        return t;
    }

    public Tree<T> getRoot(){
        Tree<T> node = this;
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

    public List<Tree<T>> getChildren(){
        return this.children;
    }

    protected Tree<T> getParent(){
        return this.parent;
    }
}

