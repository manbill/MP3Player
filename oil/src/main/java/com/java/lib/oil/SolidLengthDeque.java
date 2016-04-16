package com.java.lib.oil;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * if this class does not have enough elements to meet its capacity, it acts like normal collection.
 * after that, if you still call add to add element, nothing will happen. use addLast, addFirst instead, or other dequeue method.
 * you can use addFirstSafely or addLastSafely to get the removed element to do your special job.
 * @author liutiantian
 *
 * @param <E> deque item class
 */
public class SolidLengthDeque<E> implements Deque<E> {
    private int capacity;
    private Object[] contents;
    private int position;

    public SolidLengthDeque(int capacity) {
        this.capacity = capacity;
        this.contents = new Object[capacity];
        this.position = 0;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        // TODO Auto-generated method stub
        if(c == null || c.isEmpty()) {
            return false;
        }
        for(E e : c) {
            addLast(e);
        }
        return true;
    }

    @Override
    public void clear() {
        // TODO Auto-generated method stub
        this.contents = new Object[this.capacity];
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        // TODO Auto-generated method stub
        Iterator<?> it = c.iterator();
        ll:
            while(it.hasNext()) {
                Object e = it.next();
                for(int i = 0; i < this.contents.length; ++i) {
                    if(GlobalMethods.getInstance().checkEqual(this.contents[i], e)) {
                        continue ll;
                    }
                }
                return false;
            }
        return true;
    }

    @Override
    public boolean isEmpty() {
        // TODO Auto-generated method stub
        return this.position == 0;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        // TODO Auto-generated method stub
        Object[] contents = new Object[this.capacity];
        this.position = 0;
        for(int i = 0; i < this.contents.length; ++i) {
            if(c.contains(this.contents[i])) {
                continue;
            }
            contents[this.position++] = this.contents[i];
        }
        this.contents = contents;
        return true;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        // TODO Auto-generated method stub
        Object[] contents = new Object[this.capacity];
        this.position = 0;
        for(int i = 0; i < this.contents.length; ++i) {
            if(!c.contains(this.contents[i])) {
                continue;
            }
            contents[this.position++] = this.contents[i];
        }
        this.contents = contents;
        return true;
    }

    @Override
    public Object[] toArray() {
        // TODO Auto-generated method stub
        return this.contents;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T[] toArray(T[] a) {
        // TODO Auto-generated method stub
        if(a == null || a.length != this.contents.length) {
            throw new IllegalArgumentException("array can not be null or has a different length");
        }
        for(int i = 0; i < a.length; ++i) {
            a[i] = (T) this.contents[i];
        }
        return a;
    }

    @Override
    public boolean add(E e) {
        // TODO Auto-generated method stub
        if(this.position == this.capacity) {
            return false;
        }
        this.contents[this.position++] = e;
        return true;
    }

    @Override
    public void addFirst(E e) {
        // TODO Auto-generated method stub
        Object[] contents = new Object[this.capacity];
        System.arraycopy(this.contents, 0, contents, 1, this.capacity - 1);
        this.contents = contents;
        if(this.position < this.capacity) {
            ++this.position;
        }

        this.contents[0] = e;
    }

    /**
     * this method will return the removed element
     * @param e the element you want to insert
     * @return the removed element from the tail
     */
    public E addFirstSafely(E e) {
        @SuppressWarnings("unchecked")
        E res = (E) this.contents[this.capacity - 1];
        Object[] contents = new Object[this.capacity];
        System.arraycopy(this.contents, 0, contents, 1, this.capacity - 1);
        this.contents = contents;
        if(this.position < this.capacity) {
            ++this.position;
        }
        this.contents[0] = e;
        return res;
    }

    @Override
    public void addLast(E e) {
        // TODO Auto-generated method stub
        if(this.position < this.capacity) {
            this.contents[this.position++] = e;
            return;
        }
        Object[] contents = new Object[this.capacity];
        System.arraycopy(this.contents, 1, contents, 0, this.capacity - 1);
        this.contents = contents;
        this.contents[this.position - 1] = e;
    }

    /**
     * this method will return the removed element
     * @param e the element you want to insert
     * @return the removed element from the head
     */
    public E addLastSafely(E e) {
        if(this.position < this.capacity) {
            this.contents[this.position] = e;
            ++this.position;
            return null;
        }
        @SuppressWarnings("unchecked")
        E res = (E) this.contents[0];
        Object[] contents = new Object[this.capacity];
        System.arraycopy(this.contents, 1, contents, 0, this.capacity - 1);
        this.contents = contents;
        this.contents[0] = e;
        return res;
    }

    @Override
    public boolean contains(Object o) {
        // TODO Auto-generated method stub
        if(this.position == 0) {
            return false;
        }
        for(int i = 0; i < this.position; ++i) {
            if(GlobalMethods.getInstance().checkEqual(this.contents[i], o)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Iterator<E> descendingIterator() {
        // TODO Auto-generated method stub
        return new DescendingIterator();
    }

    @SuppressWarnings("unchecked")
    @Override
    public E element() throws NoSuchElementException {
        // TODO Auto-generated method stub
        if(this.position == 0) {
            throw new NoSuchElementException("deque is empty.");
        }
        return (E) this.contents[0];
    }

    @SuppressWarnings("unchecked")
    @Override
    public E getFirst() {
        // TODO Auto-generated method stub
        if(this.position == 0) {
            throw new NoSuchElementException("deque is empty.");
        }
        return (E) this.contents[0];
    }

    @SuppressWarnings("unchecked")
    @Override
    public E getLast() {
        // TODO Auto-generated method stub
        if(this.position == 0) {
            throw new NoSuchElementException("deque is empty.");
        }
        return (E) this.contents[this.position];
    }

    @Override
    public Iterator<E> iterator() {
        // TODO Auto-generated method stub
        return new DeqIterator();
    }

    @Override
    public boolean offer(E e) {
        // TODO Auto-generated method stub
        return offerLast(e);
    }

    @Override
    public boolean offerFirst(E e) {
        // TODO Auto-generated method stub
        addFirst(e);
        return true;
    }

    @Override
    public boolean offerLast(E e) {
        // TODO Auto-generated method stub
        addLast(e);
        return true;
    }

    @Override
    public E peek() {
        // TODO Auto-generated method stub
        return peekFirst();
    }

    @SuppressWarnings("unchecked")
    @Override
    public E peekFirst() {
        // TODO Auto-generated method stub
        if(this.position == 0) {
            return null;
        }
        return (E) this.contents[0];
    }

    @SuppressWarnings("unchecked")
    @Override
    public E peekLast() {
        // TODO Auto-generated method stub
        if(this.position == 0) {
            return null;
        }
        return (E) this.contents[this.position - 1];
    }

    @Override
    public E poll() {
        // TODO Auto-generated method stub
        return pollFirst();
    }

    @Override
    public E pollFirst() {
        // TODO Auto-generated method stub
        if(this.position == 0) {
            return null;
        }
        if(this.position == 1) {
            return pollLast();
        }
        @SuppressWarnings("unchecked")
        E res = (E) this.contents[0];
        Object[] contents = new Object[this.capacity];
        System.arraycopy(this.contents, 1, contents, 0, this.position - 1);
        this.contents = contents;
        --this.position;
        return res;
    }

    @SuppressWarnings("unchecked")
    @Override
    public E pollLast() {
        // TODO Auto-generated method stub
        if(this.position == 0) {
            return null;
        }
        return (E) this.contents[this.position-- - 1];
    }

    @Override
    public E pop() {
        // TODO Auto-generated method stub
        return removeFirst();
    }

    @Override
    public void push(E e) {
        // TODO Auto-generated method stub
        addFirst(e);
    }

    @Override
    public E remove() {
        // TODO Auto-generated method stub
        return removeFirst();
    }

    @Override
    public boolean remove(Object o) {
        // TODO Auto-generated method stub
        if(this.position == 0) {
            return false;
        }
        for(int i = 0; i < this.position; ++i) {
            if(GlobalMethods.getInstance().checkEqual(this.contents[i], o)) {
                delete(i);
                return true;
            }
        }
        return false;
    }

    @Override
    public E removeFirst() throws NoSuchElementException {
        // TODO Auto-generated method stub
        if(this.position == 0) {
            throw new NoSuchElementException("deque is empty.");
        }
        return delete(0);
    }

    @Override
    public boolean removeFirstOccurrence(Object o) {
        // TODO Auto-generated method stub
        if(this.position == 0) {
            return false;
        }
        for(int i = 0; i < this.position; ++i) {
            if(GlobalMethods.getInstance().checkEqual(this.contents[i], o)) {
                delete(i);
                return true;
            }
        }
        return false;
    }

    @Override
    public E removeLast() throws NoSuchElementException {
        // TODO Auto-generated method stub
        if(this.position == 0) {
            throw new NoSuchElementException("deque is empty.");
        }
        return delete(this.position - 1);
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        // TODO Auto-generated method stub
        if(this.position == 0) {
            return false;
        }
        for(int i = this.position; i > 0; --i) {
            if(GlobalMethods.getInstance().checkEqual(this.contents[i - 1], o)) {
                delete(i);
                return true;
            }
        }
        return false;
    }

    private E delete(int index) {
        if(index >= this.position || index < 0) {
            return null;
        }
        @SuppressWarnings("unchecked")
        E delete = (E) this.contents[index];
        Object[] contents = new Object[this.capacity];
        System.arraycopy(this.contents, 0, contents, 0, index);
        if(index + 1 < this.position) {
            System.arraycopy(this.contents, index + 1, contents, index, this.position - index - 1);
        }
        --this.position;
        this.contents = contents;
        return delete;
    }

    @Override
    public int size() {
        // TODO Auto-generated method stub
        return this.position;
    }

    private class DeqIterator implements Iterator<E> {
        private int current = 0;

        @Override
        public boolean hasNext() {
            // TODO Auto-generated method stub
            return this.current != SolidLengthDeque.this.position;
        }

        @SuppressWarnings("unchecked")
        @Override
        public E next() {
            // TODO Auto-generated method stub
            if(this.current == SolidLengthDeque.this.position) {
                throw new NoSuchElementException();
            }
            return (E) SolidLengthDeque.this.contents[this.current++];
        }

        @Override
        public void remove() {
            // TODO Auto-generated method stub
            if(SolidLengthDeque.this.position == 0) {
                throw new NoSuchElementException();
            }
            delete(--this.current);
        }
    }

    private class DescendingIterator implements Iterator<E> {
        private int current = SolidLengthDeque.this.position;

        @Override
        public boolean hasNext() {
            // TODO Auto-generated method stub
            return this.current != 0;
        }

        @SuppressWarnings("unchecked")
        @Override
        public E next() {
            // TODO Auto-generated method stub
            if(this.current == 0) {
                throw new NoSuchElementException();
            }
            return (E) SolidLengthDeque.this.contents[--this.current];
        }

        @Override
        public void remove() {
            // TODO Auto-generated method stub
            if(SolidLengthDeque.this.position == 0) {
                throw new NoSuchElementException();
            }
            delete(this.current);
        }
    }
}
