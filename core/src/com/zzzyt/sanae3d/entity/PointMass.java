package com.zzzyt.sanae3d.entity;

import com.zzzyt.sanae3d.Sanae3d;
import com.zzzyt.sanae3d.SanaeConfig;
import com.zzzyt.sanae3d.math.PhyUtil;
import com.zzzyt.sanae3d.math.Vec3;

public class PointMass implements Entity {
	private Sanae3d sanae;
	
	public final int id;
	public long time;
	public long step;
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
		this.id=sanae.idcount;
		sanae.idcount++;
		this.mass=mass;
		this.pos=pos.clone();
		this.v=Vec3.ZERO.clone();
		this.a=Vec3.ZERO.clone();
		this.list=new ComputeList(this);
		this.time=sanae.time;
		this.step=1;
		for(Entity e:sanae.entities) {
			if(!e.isTiny())this.list.add(e);
		}
		this.list.sort(time);
	}
	
	public PointMass(double mass,Vec3 pos,Vec3 v) {
		super();
		this.sanae=Sanae3d.sanae;
		this.id=sanae.idcount;
		sanae.idcount++;
		this.mass=mass;
		this.pos=pos.clone();
		this.v=v.clone();
		this.a=Vec3.ZERO.clone();
		this.list=new ComputeList(this);
		this.time=sanae.time;
		this.step=1;
		for(Entity e:sanae.entities) {
			if(!e.isTiny())this.list.add(e);
		}
		this.list.sort(time);
	}
	
	@Override
	public long getTime() {
		return time;
	}
	
	@Override
	public void setTime(long t) {
		if(t<time) {
			System.out.println("[WARNING]Set time before actual time");
		}
		this.time=t;
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
		return getPos(sanae.time);
	}
	
	@Override
	public Vec3 getPos(long t) {
		return pos.add(PhyUtil.posDelta(v, a, (double)(t-time)/1000d));
	}

	@Override
	public Vec3 getVelocity(long t) {
		return v.add(a.mul((double)(t-time)/1000d));
	}

	@Override
	public Vec3 getAcceleration(long t) {
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
	public double dst(long t,Entity e) {
		return getPos(t).dst(e.getPos(t));
	}
	
	@Override
	public double dst2(long t,Entity e) {
		return getPos(t).dst2(e.getPos(t));
	}

	@Override
	public long getStep() {
		return step;
	}

	@Override
	public void setStep(long step) {
		this.step=step;
	}
}
