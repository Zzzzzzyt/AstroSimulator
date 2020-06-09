package com.zzzyt.sanae3d.entity;

import com.zzzyt.sanae3d.SanaeConfig;
import com.zzzyt.sanae3d.math.PhyUtil;
import com.zzzyt.sanae3d.math.Vec3;

public class PointMass extends Entity {	
	public double mass;
	public Vec3 pos;
	public Vec3 vel;
	public Vec3 acc;
	
	public boolean isTiny() {
		return mass<SanaeConfig.tinyMass;
	}
	
	public PointMass(double mass,Vec3 pos,Vec3 v) {
		super();
		this.mass=mass;
		this.pos=pos.clone();
		this.vel=v.clone();
		this.acc=new Vec3();
	}
	
	public PointMass(double mass,Vec3 pos) {
		super();
		this.mass=mass;
		this.pos=pos.clone();
		this.vel=new Vec3();
		this.acc=new Vec3();
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
	public Vec3 getPos() {
		return getPos(sanae.time);
	}
	
	@Override
	public Vec3 getPos(long t) {
		return pos.add(PhyUtil.posDelta(vel, acc, (double)(t-time)/1000d));
	}
	
	@Override
	public Vec3 getAbsolutePos() {
		return getAbsolutePos(sanae.time);
	}
	
	@Override
	public Vec3 getAbsolutePos(long t) {
		if(node==null)return pos.add(PhyUtil.posDelta(vel, acc, (double)(t-time)/1000d));
		return node.getAbsoluteNodePos().add(pos.add(PhyUtil.posDelta(vel, acc, (double)(t-time)/1000d)));
	}

	@Override
	public Vec3 getVel(long t) {
		return vel.add(acc.mul((double)(t-time)/1000d));
	}

	@Override
	public Vec3 getAcc(long t) {
		return acc;
	}
	
	@Override
	public void setPos(Vec3 v) {
		this.pos.set(v);
	}

	@Override
	public void setVel(Vec3 v) {
		this.vel.set(v);
	}

	@Override
	public void setAcc(Vec3 v) {
		this.acc.set(v);
	}
	
}
