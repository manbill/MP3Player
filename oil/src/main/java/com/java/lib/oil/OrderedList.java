package com.java.lib.oil;

import java.util.Vector;

public class OrderedList<E extends Comparable<E>> extends Vector<E> {
    private static final long serialVersionUID = 87419241977195451L;

    public static final int SORT_TYPE_ASC = 0;
    public static final int SORT_TYPE_DESC = 1;

    private int sortType;

    public OrderedList() {
        super();
        this.sortType = OrderedList.SORT_TYPE_ASC;
    }

    public OrderedList(int capacity, int sortType) {
        super(capacity);
        this.sortType = sortType;
    }

    @Override
    public boolean add(E e) {
        if(this.isEmpty()) {
            return super.add(e);
        }
        switch(this.sortType) {
            case OrderedList.SORT_TYPE_ASC:
                addAsc(e);
                break;
            case OrderedList.SORT_TYPE_DESC:
                addDesc(e);
                break;
        }
        return true;
    }

    private final void addAsc(E e) {
        if(e.compareTo(elementAt(0)) < 0) {
            add(0, e);
            return;
        }

        int ls = size();
        for(int i = ls - 1; i >= 0; --i) {
            if(e.compareTo(elementAt(i)) >= 0) {
                if(i + 1 == ls) {
                    super.add(e);
                    return;
                }
                add(i + 1, e);
                return;
            }
        }
        super.add(e);
    }

    private final void addDesc(E e) {
        if(e.compareTo(elementAt(0)) > 0) {
            add(0, e);
            return;
        }

        int ls = size();
        for(int i = 0; i < ls; ++i) {
            if(e.compareTo(elementAt(i)) <= 0) {
                if(i + 1 == ls) {
                    super.add(e);
                    return;
                }
                add(i + 1, e);
                return;
            }
        }
        super.add(e);
    }

    public int getSortType() {
        return this.sortType;
    }

    public void setSortType(int sortType) {
        this.sortType = sortType;
    }

    public void sort(int sortType) {
        this.sortType = sortType;
        if(isEmpty() || size() == 1) {
            return;
        }
        switch(this.sortType) {
            case OrderedList.SORT_TYPE_ASC:
                sortAsc();
                break;
            case OrderedList.SORT_TYPE_DESC:
                sortDesc();
                break;
        }
    }

    private final void sortAsc() {
        for(int i = 0; i < size(); ++i) {
            E min = elementAt(i);
            for(int j = i + 1; j < size(); ++j) {
                E tem = elementAt(j);
                if(tem.compareTo(min) < 0) {
                    set(i, tem);
                    set(j, min);
                    min = tem;
                }
            }
        }
    }

    private final void sortDesc() {
        for(int i = 0; i < size(); ++i) {
            E max = elementAt(i);
            for(int j = i + 1; j < size(); ++j) {
                E tem = elementAt(j);
                if(tem.compareTo(max) > 0) {
                    set(i, tem);
                    set(j, max);
                    max = tem;
                }
            }
        }
    }
}
