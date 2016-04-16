package com.java.lib.oil;

/**
 * simple class which handle single key value pair.
 * @param <K> the pair key class
 * @param <V> the pair value class
 */
public class KVPair<K, V> {

    private K k;
    private V v;

    public KVPair(){

    }

    public KVPair(K k, V v){
        this.k = k;
        this.v = v;
    }

    public void setKey(K k){
        this.k = k;
    }

    public K getKey(){
        return k;
    }

    public void setValue(V v){
        this.v = v;
    }

    public V getValue(){
        return v;
    }

    public Object get(int index){
        return index == 0 ? k : v;
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

	@Override
    public String toString(){
        return k.toString() + ": " + v.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof KVPair) {
            KVPair another = (KVPair) obj;
            if(!GlobalMethods.getInstance().checkEqual(this.k, another.k)) {
                return false;
            }
            if(!GlobalMethods.getInstance().checkEqual(this.v, another.v)) {
                return false;
            }
            return true;
        }
        return false;
    }
}
