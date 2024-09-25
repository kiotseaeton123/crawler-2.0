package org.winnie.utils;

import java.util.Objects;

/**
 * generic utility class that represents object pairs
 * @author winnie
 */
public class Pair<K,V> {
    private K key;
    private V value;

    /**
     * constructor takes two generic objects
     * @param key - first object
     * @param value - second object
     */
    public Pair(K key,V value){
        this.key=key;
        this.value=value;
    }
    /**
     * key getter
     * @return key type
     */
    public K getKey(){
        return this.key;
    }
    /**
     * value getter
     * @return value type
     */
    public V getValue(){
        return this.value;
    }


    /**
     * override equals to allow pair comparison
     * @param o - object
     */
    @Override
    public boolean equals(Object o){
        // same instance
        if(this==o)return true;
        // null object or different types aren't equal
        if(o==null||this.getClass()!=o.getClass())return false;

        // cast object to pair and return comparison
        Pair<?,?> pair=(Pair<?,?>)o;
        return Objects.equals(this.key,pair.key) && Objects.equals(this.value,pair.value);
    }

    /**
     * override hashcode, 2 equal objects have same hash code
     */
    @Override
    public int hashCode(){
        return Objects.hash(this.key,this.value);
    }
}
