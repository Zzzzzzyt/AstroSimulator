package com.zzzyt.sanae3d.entity;

import com.zzzyt.sanae3d.Octree;
import com.zzzyt.sanae3d.OctreeNode;
import com.zzzyt.sanae3d.Sanae3d;
import com.zzzyt.sanae3d.math.Vec3;

public abstract class Entity {
	public long time,step;
	public final int id;
	public Sanae3d sanae;
	public OctreeNode node;
	
	public Entity() {
		super();
		this.sanae=Sanae3d.sanae;
		this.node=sanae.octree.root;
		this.id=sanae.idcount;
		sanae.idcount++;
		this.time=sanae.time;
		this.step=1;
	}
	
	public int getId() {
		return id;
	}
	
	public long getTime() {
		return time;
	}
	
	public void setTime(long t) {
		if(t<time) {
			System.out.println("[WARNING]Set time before actual time");
		}
		this.time=t;
	}
	
	public long getStep() {
		return step;
	}

	public void setStep(long step) {
		this.step=step;
	}
	
	public OctreeNode getNode() {
		return node;
	}
	
	public void setNode(OctreeNode node) {
		this.node=node;
	}
	
	public double dst(long t,Entity e) {
		return Octree.dst(this, e, t).mod();
	}
	
	public double dst2(long t,Entity e) {
		return Octree.dst(this, e, t).mod2();
	}
	
	public abstract double getMass();
	public abstract double getInertia();
	
	public abstract Vec3 getPos();
	public abstract Vec3 getPos(long t);
	public abstract Vec3 getAbsolutePos();
	public abstract Vec3 getAbsolutePos(long t);
	public abstract Vec3 getVel(long t);
	public abstract Vec3 getAcc(long t);
	public abstract void setPos(Vec3 v);
	public abstract void setVel(Vec3 v);
	public abstract void setAcc(Vec3 v);
	public abstract boolean isTiny();
}
