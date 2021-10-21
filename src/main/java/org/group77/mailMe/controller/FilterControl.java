package org.group77.mailMe.controller;

import org.group77.mailMe.Control;

interface FilterControl {
    void init(Control control);
    void init(Control control, SearchControl searchControl);
    void applyFilter(Control control);
    void applySorting(Control control);
}
