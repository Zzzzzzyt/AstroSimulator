package com.zzzyt.sanae3d.math;

import com.zzzyt.sanae3d.entity.Entity;

public class PhyUtil {
	public static final double G=6.6743015e-11;
	
	public static double gravity(Entity a,Entity b) {
		return G*a.getMass()*b.getMass()/a.dst2(b);
	}
	
	public static Vec3 posDelta(Vec3 v,Vec3 a,double t) {
		return v.mul(t).add(a.mul(t*t/2));
	}
}
