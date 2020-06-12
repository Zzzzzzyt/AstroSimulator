package com.zzzyt.sanae3d.math;

import com.badlogic.gdx.math.MathUtils;
import com.zzzyt.sanae3d.SanaeConfig;
import com.zzzyt.sanae3d.entity.Entity;

public class PhyUtil {
	public static final double G=6.6743015e-11;
	
	public static double gravity(Entity b,Entity a,long t) {
		return G*a.getMass()*b.getMass()/a.dst2(t,b);
	}
	
	public static double gravity(double m1,double m2,Vec3 p1,Vec3 p2) {
		return G*m1*m2/p1.dst2(p2);
	}
	
	public static Vec3 posDelta(Vec3 v,Vec3 a,double t) {
		return v.mul(t).add(a.mul(t*t/2));
	}
	
	public static long estimateStep(Vec3 a1,Vec3 a2) {
		Vec3 a=a2.sub(a1);
		long step=Math.round(SanaeConfig.stepFactor/a.mod());
		return MathUtils.clamp(step, SanaeConfig.leastStep, SanaeConfig.mostStep);
	}
}
