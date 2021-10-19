package org.group77.mailMe.controller;

import org.group77.mailMe.model.Control;

interface FilterControl {
    public void init(Control control);
    public void applyFilter(Control control);
}
