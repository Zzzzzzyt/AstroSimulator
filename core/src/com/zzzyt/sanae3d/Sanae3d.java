package com.zzzyt.sanae3d;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.zzzyt.sanae3d.entity.Entity;

public class Sanae3d {
	public final long step=1;
	public static Sanae3d sanae;
	public List<SimThread> workers;
	public List<Entity> entities;
	public int nextId=0;
	
	public void sim(double time) {
		long ltime=Math.round(time*1000);
		if(!anyBusy()) {
			for(SimThread s:workers) {
				s.sort=true;
			}
		}
		for(SimThread s:workers) {
			s.goal+=ltime;
		}
	}
	
	public void addEntity(Entity e) {
		for(Entity i:entities) {
			i.getComputedList().add(e);
		}
		entities.add(e);
	}
	
	public void removeEntity(Entity e) {
		entities.remove(e);
		for(Entity i:entities) {
			i.getComputedList().remove(e);
		}
	}
	
	public SimThread addWorker() {
		SimThread s=new SimThread(workers.size());
		s.setName("Physics Thread "+s.id);
		workers.add(s);
		s.start();
		return s;
	}
	
	public Sanae3d(){
		sanae=this;
		this.entities=new Vector<Entity>();
		this.workers=new ArrayList<SimThread>();
	}
	
	public Sanae3d(int cnt) {
		sanae=this;
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
