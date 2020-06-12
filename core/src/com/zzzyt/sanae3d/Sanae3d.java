package com.zzzyt.sanae3d;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.CopyOnWriteArrayList;

import com.zzzyt.sanae3d.entity.Entity;

public class Sanae3d {
	public final long step=1;
	public long time;
	public static Sanae3d sanae;
	public List<SimThread> workers;
	public List<Entity> entities;
	private int nextId;
	public int idcount=0;
	public long counter1=0;
	public long counter2=0;
	
	public void sim(double time) {
		long ltime=Math.round(time*1000);
		if(!anyBusy()) {
			for(SimThread s:workers) {
				s.sort=true;
			}
		}
		sanae.time+=ltime;
		for(SimThread s:workers) {
			s.goal+=ltime;
			s.interrupt();
		}
	}
	
	public int workerCount() {
		return workers.size();
	}
	
	public int size() {
		return entities.size();
	}
	
	public Entity get(int i) {
		return entities.get(i);
	}
	
	public void add(Entity e) {
		if(!e.isTiny()) {
			for(Entity i:entities) {
				if(i!=null)i.getComputedList().add(e);
			}
		}
		while(nextId<entities.size()&&entities.get(nextId)!=null)nextId++;
		if(nextId>=entities.size()) {
			entities.add(e);
			nextId++;
		}
		else {
			entities.set(nextId, e);
			while(nextId<entities.size()&&entities.get(nextId)!=null)nextId++;
		}
	}
	
	public void remove(Entity e) {
		int id=e.getId();
		entities.set(id,null);
		nextId=Math.min(nextId, id);
		if(!e.isTiny()) {
			for(Entity i:entities) {
				i.getComputedList().remove(e);
			}
		}
	}
	
	public SimThread addWorker() {
		SimThread s=new SimThread();
		workers.add(s);
		s.start();
		return s;
	}
	
	public Sanae3d(){
		sanae=this;
		this.nextId=0;
		this.entities=new Vector<Entity>();
		this.workers=new CopyOnWriteArrayList<SimThread>();
	}
	
	public Sanae3d(int cnt) {
		sanae=this;
		this.nextId=0;
		this.entities=new Vector<Entity>();
		this.workers=new ArrayList<SimThread>();
		for(int i=0;i<cnt;i++) {
			addWorker();
		}
	}

	public boolean anyBusy() {
		boolean flag=false;
		for(SimThread s:workers) {
			flag|=s.busy;
		}
		return flag;
	}
}
