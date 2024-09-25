package org.winnie.utils;

import java.util.Objects;

public class Pair<K,V> {
    private K key;
    private V value;

    public Pair(K key,V value){
        this.key=key;
        this.value=value;
    }
    public K getKey(){
        return this.key;
    }
    public V getV(){
        return this.value;
    }

    // allow Pair comparisons
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

    // two objects that are equals() have same hash
    @Override
    public int hashCode(){
        return Objects.hash(this.key,this.value);
    }
}
