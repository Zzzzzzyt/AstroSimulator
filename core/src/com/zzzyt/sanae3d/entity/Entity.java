package com.zzzyt.sanae3d.entity;

import com.zzzyt.sanae3d.math.Vec3;

public interface Entity {
	public long getTime();
	public void setTime(long t);
	public long getStep();
	public void setStep(long step);
	public double getMass();
	public double getInertia();
	public int getId();
	public Vec3 getPos();
	public Vec3 getPos(long t);
	public Vec3 getVelocity(long t);
	public Vec3 getAcceleration(long t);
	public void setPos(Vec3 v);
	public void setVelocity(Vec3 v);
	public void setAcceleration(Vec3 v);
	public ComputeList getComputedList();
	public double dst(long t,Entity e);
	public double dst2(long t,Entity e);
	public boolean isTiny();
}
