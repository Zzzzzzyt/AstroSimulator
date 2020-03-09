package com.zzzyt.sanae3d.entity;

import com.zzzyt.sanae3d.math.Vec3;

public interface Entity {
	public double getMass();
	public double getInertia();
	public int getId();
	public Vec3 getPos();
	public Vec3 getVelocity();
	public Vec3 getAcceleration();
	public void setPos(Vec3 v);
	public void setVelocity(Vec3 v);
	public void setAcceleration(Vec3 v);
	public ComputeList getComputedList();
	public double dst(Entity e);
	public double dst2(Entity e);
}
