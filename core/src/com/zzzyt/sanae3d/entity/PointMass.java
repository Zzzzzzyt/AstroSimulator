package com.zzzyt.sanae3d.entity;

import com.zzzyt.sanae3d.Sanae3d;
import com.zzzyt.sanae3d.SanaeConfig;
import com.zzzyt.sanae3d.math.Vec3;

public class PointMass implements Entity {
	private Sanae3d sanae;
	
	public final int id;
	public double mass;
	public Vec3 pos;
	public Vec3 v;
	public Vec3 a;
	public ComputeList list;
	
	public boolean isTiny() {
		return mass<SanaeConfig.tinyMass;
	}
	
	public PointMass(double mass,Vec3 pos) {
		super();
		this.sanae=Sanae3d.sanae;
		this.id=sanae.nextId;
		sanae.nextId++;
		this.mass=mass;
		this.pos=pos.clone();
		this.v=Vec3.ZERO.clone();
		this.a=Vec3.ZERO.clone();
		this.list=new ComputeList(this);
		for(Entity e:sanae.entities) {
			this.list.add(e);
		}
		this.list.sort();
	}
	
	public PointMass(double mass,Vec3 pos,Vec3 v) {
		super();
		this.sanae=Sanae3d.sanae;
		this.id=sanae.nextId;
		sanae.nextId++;
		this.mass=mass;
		this.pos=pos.clone();
		this.v=v.clone();
		this.a=Vec3.ZERO.clone();
		this.list=new ComputeList(this);
		for(Entity e:sanae.entities) {
			this.list.add(e);
		}
		this.list.sort();
	}
	
	@Override
	public double getMass() {
		return mass;
	}

	@Override
	public double getInertia() {
		return 0;
	}

	@Override
	public int getId() {
		return id;
	}

	@Override
	public Vec3 getPos() {
		return pos;
	}

	@Override
	public Vec3 getVelocity() {
		return v;
	}

	@Override
	public Vec3 getAcceleration() {
		return a;
	}
	
	@Override
	public void setPos(Vec3 v) {
		this.pos.set(v);
	}

	@Override
	public void setVelocity(Vec3 v) {
		this.v.set(v);
	}

	@Override
	public void setAcceleration(Vec3 v) {
		this.a.set(v);
	}

	@Override
	public ComputeList getComputedList() {
		return list;
	}

	@Override
	public double dst(Entity e) {
		return pos.dst(e.getPos());
	}
	
	@Override
	public double dst2(Entity e) {
		return pos.dst2(e.getPos());
	}
}
