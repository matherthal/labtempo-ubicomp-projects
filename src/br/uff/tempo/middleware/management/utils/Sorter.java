package br.uff.tempo.middleware.management.utils;

import java.util.ArrayList;
import java.util.List;

import br.uff.tempo.middleware.comm.current.api.Tuple;

public class Sorter<T extends Position> {
	
	List<Tuple<String, T>> list;
	T reference;
	int begin;
	int end;
	
	public Sorter(T reference) {
		this(new ArrayList<Tuple<String,T>>(), reference);
	}
	
	public Sorter(List<Tuple<String,T>> list, T reference) {
		this.list = list;
		this.reference = reference;
		begin = 0;
		end = list.size()-1;
	}
	
	public double getMetric(T arg){
		return reference.getDistance(arg);
	}
	
	
	public T remove(T obj){
		return obj;
	}
	
	public void insert(T obj) {
		
	}
	
	public ArrayList<String> sort() {
		ArrayList<String> result = new ArrayList<String>();
		quick_sort();
		for (Tuple<String, T> tuple : list) {
			result.add(tuple.key);
		}
		return result;
	}
	
	public void quick_sort() {
	        int half;
	
	        if (begin < end) {
	                half = partition();
	                int prevEnd = end;
	                end = half;
	                quick_sort();
	                begin = half +1;
	                end = prevEnd;
	                quick_sort();
	        }
	}
	
	public int partition() {
	        Tuple<String, T> pivo;
	        int topo, i;
	        pivo = list.get(begin);
	        topo = begin;
	
	        for (i = begin + 1; i <= end; i++) {
	                if ((getMetric((T)list.get(i).value) < getMetric((T)pivo.value))) {
	                        list.set(topo,list.get(i));
	                        list.set(i, list.get(topo + 1));
	                        topo++; 
	                }
	        }
	        list.set(topo,pivo);
	        return topo;
	}
	

}
