package com.zzzyt.sanae3d.entity;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Vector;

import com.zzzyt.sanae3d.Sanae3d;

public class ComputeList implements Iterable<Entity>, List<Entity> {

	Entity me;
	List<Entity> list;
	
	public ComputeList(Entity me) {
		this.me=me;
		this.list=new Vector<Entity>(256);
	}
	
	public void sort(long t) {
		final double tmp[]=new double[Sanae3d.sanae.size()+10];
		for(Entity e:list) {
			tmp[e.getId()]=e.getMass()/me.dst2(t,e);
		}
		Collections.sort(list, new Comparator<Entity>() {
			@Override
			public int compare(Entity a, Entity b) {
				return Double.compare(tmp[b.getId()],tmp[a.getId()]);
			}
		});
	}
	
	@Override
	public boolean add(Entity e) {
		return list.add(e);
	}
	@Override
	public void add(int index, Entity element) {
		list.add(index, element);
	}
	@Override
	public boolean addAll(Collection<? extends Entity> c) {
		return list.addAll(c);
	}
	@Override
	public boolean addAll(int index, Collection<? extends Entity> c) {
		return list.addAll(index, c);
	}
	@Override
	public void clear() {
		list.clear();
	}
	@Override
	public boolean contains(Object o) {
		return list.contains(o);
	}
	@Override
	public boolean containsAll(Collection<?> c) {
		return list.containsAll(c);
	}
	@Override
	public Entity get(int index) {
		return list.get(index);
	}
	@Override
	public int indexOf(Object o) {
		return list.indexOf(o);
	}
	@Override
	public boolean isEmpty() {
		return list.isEmpty();
	}
	@Override
	public int lastIndexOf(Object o) {
		return list.lastIndexOf(o);
	}
	@Override
	public ListIterator<Entity> listIterator() {
		return list.listIterator();
	}
	@Override
	public ListIterator<Entity> listIterator(int index) {
		return list.listIterator(index);
	}
	@Override
	public boolean remove(Object o) {
		return list.remove(o);
	}
	@Override
	public Entity remove(int index) {
		return list.remove(index);
	}
	@Override
	public boolean removeAll(Collection<?> c) {
		return list.removeAll(c);
	}
	@Override
	public boolean retainAll(Collection<?> c) {
		return list.retainAll(c);
	}
	@Override
	public Entity set(int index, Entity element) {
		return list.set(index, element);
	}
	@Override
	public int size() {
		return list.size();
	}
	@Override
	public List<Entity> subList(int fromIndex, int toIndex) {
		return list.subList(fromIndex, toIndex);
	}
	@Override
	public Object[] toArray() {
		return list.toArray();
	}
	@Override
	public <T> T[] toArray(T[] a) {
		return list.toArray(a);
	}
	@Override
	public Iterator<Entity> iterator() {
		return list.iterator();
	}
	
}
