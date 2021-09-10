package org.group77.mejl.controllers;

import javafx.scene.control.TreeItem;

import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TreeItemRecursive<T, U> extends TreeItem<U> {
    public TreeItemRecursive(T t, Function<T, U> funcData, Function<T, Collection<? extends T>> funcChildren) {
        super(funcData.apply(t));
        getChildren().addAll(funcChildren.apply(t)
                .stream()
                .map(t1 -> new TreeItemRecursive<T, U>(t1, funcData, funcChildren))
                .collect(Collectors.toList()));
    }
}