package com.zzzyt.sanae3d.math;

import com.badlogic.gdx.math.Vector3;

public class Vec3 {
	public static final Vec3 ZERO=new Vec3(0,0,0);
	public static final Vec3 X=new Vec3(1,0,0);
	public static final Vec3 Y=new Vec3(0,1,0);
	public static final Vec3 Z=new Vec3(0,0,1);
	
	public double x,y,z;
	
	public double getX() {
		return x;
	}
	public double getY() {
		return y;
	}
	
	public double getZ() {
		return z;
	}
	
	public void setX(double x) {
		this.x = x;
	}
	
	public void setY(double y) {
		this.y = y;
	}
	
	public void setZ(double z) {
		this.z = z;
	}
	
	public void setZero() {
		this.x=0;
		this.y=0;
		this.z=0;
	}
	
	public void set(Vec3 v) {
		this.x=v.x;
		this.y=v.y;
		this.z=v.z;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(x);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(y);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(z);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}
	 
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Vec3 other = (Vec3) obj;
		if (Double.doubleToLongBits(x) != Double.doubleToLongBits(other.x))
			return false;
		if (Double.doubleToLongBits(y) != Double.doubleToLongBits(other.y))
			return false;
		if (Double.doubleToLongBits(z) != Double.doubleToLongBits(other.z))
			return false;
		return true;
	}
	 
	 public String toString() {
		 return "("+x+", "+y+", "+z+")";
	 }
	 
	 public Vector3 toGdx() {
		 return new Vector3((float)x,(float)y,(float)z);
	 }
	 
	 public Vec3 clone() {
		 return new Vec3(x,y,z);
	 }
	 
	 public Vec3() {
		 this.x=0d;
		 this.y=0d;
		 this.z=0d;
	 }
	 
	 public Vec3(double x,double y,double z) {
		 this.x=x;
		 this.y=y;
		 this.z=z;
	 }
	 
	 public Vec3(float x,float y,float z) {
		 this.x=x;
		 this.y=y;
		 this.z=z;
	 }
	 
	 public Vec3(Vector3 v) {
		 this.x=v.x;
		 this.y=v.y;
		 this.z=v.z;
	 }
	 
	 public Vec3(Vec3 v) {
		 this.x=v.x;
		 this.y=v.y;
		 this.z=v.z;
	 }
	 
	 public Vec3 addin(Vec3 a) {
		 x+=a.x;
		 y+=a.y;
		 z+=a.z;
		 return this;
	 }
	 
	 public static Vec3 add(Vec3 a,Vec3 b) {
		 return new Vec3(a.x+b.x,a.y+b.y,a.z+b.z);
	 }
	 
	 public Vec3 add(Vec3 a) {
		 return new Vec3(x+a.x,y+a.y,z+a.z);
	 }
	 
	 public Vec3 subin(Vec3 a) {
		 x-=a.x;
		 y-=a.y;
		 z-=a.z;
		 return this;
	 }
	 
	 public static Vec3 sub(Vec3 a,Vec3 b) {
		 return new Vec3(a.x-b.x,a.y-b.y,a.z-b.z);
	 }
	 
	 public Vec3 sub(Vec3 a) {
		 return new Vec3(x-a.x,y-a.y,z-a.z);
	 }
	 
	 public Vec3 mulin(double a) {
		 x*=a;
		 y*=a;
		 z*=a;
		 return this;
	 }
	 
	 public static Vec3 mul(Vec3 a,double b) {
		 return new Vec3(a.x*b,a.y*b,a.z*b);
	 }
	 
	 public static Vec3 mul(double b,Vec3 a) {
		 return new Vec3(a.x*b,a.y*b,a.z*b);
	 }
	 
	 public Vec3 mul(double a) {
		 return new Vec3(x*a,y*a,z*a);
	 }
	 
	 public Vec3 divin(double a) {
		 x/=a;
		 y/=a;
		 z/=a;
		 return this;
	 }
	 
	 public static Vec3 div(Vec3 a,double b) {
		 return new Vec3(a.x/b,a.y/b,a.z/b);
	 }
	 
	 public Vec3 div(double a) {
		 return new Vec3(x/a,y/a,z/a);
	 }
	 
	 public double dot(Vec3 a) {
		 return x*a.x+y*a.y+z*a.z;
	 }
	 
	 public static double dot(Vec3 a,Vec3 b) {
		 return a.x*b.x+a.y*b.y+a.z*b.z;
	 }
	 
	 public void crsin(Vec3 a) {
		 this.x=y*a.z-z*a.y;
		 this.y=z*a.x-x*a.z;
		 this.z=x*a.y-y*a.x;
	 }
	 
	 public Vec3 crs(Vec3 a) {
		 return new Vec3(
				 y*a.z-z*a.y,
				 z*a.x-x*a.z,
				 x*a.y-y*a.x);
	 }
	 
	 public static Vec3 crs(Vec3 a,Vec3 b) {
		 return new Vec3(
				 a.y*b.z-a.z*b.y,
				 a.z*b.x-a.x*b.z,
				 a.x*b.y-a.y*b.x);
	 }
	 
	 public double mod() {
		 return Math.sqrt(x*x+y*y+z*z);
	 }
	 
	 public static double mod(Vec3 a) {
		 return Math.sqrt(a.x*a.x+a.y*a.y+a.z*a.z);
	 }
	 
	 public double mod2() {
		 return x*x+y*y+z*z;
	 }
	 
	 public static double mod2(Vec3 a) {
		 return a.x*a.x+a.y*a.y+a.z*a.z;
	 }
	 
	 public double dst(Vec3 a) {
		 double xx=a.x-x;
		 double yy=a.y-y;
		 double zz=a.z-z;
		 return Math.sqrt(xx*xx+yy*yy+zz*zz);
	 }
	 
	 public static double dst(Vec3 a,Vec3 b) {
		 double xx=a.x-b.x;
		 double yy=a.y-b.y;
		 double zz=a.z-b.z;
		 return Math.sqrt(xx*xx+yy*yy+zz*zz);
	 }
	 
	 public double dst2(Vec3 a) {
		 double xx=a.x-x;
		 double yy=a.y-y;
		 double zz=a.z-z;
		 return xx*xx+yy*yy+zz*zz;
	 }
	 
	 public static double dst2(Vec3 a,Vec3 b) {
		 double xx=a.x-b.x;
		 double yy=a.y-b.y;
		 double zz=a.z-b.z;
		 return xx*xx+yy*yy+zz*zz;
	 }
	 
	 public void unitfy() {
		 double mod=Math.sqrt(x*x+y*y+z*z);
		 this.x/=mod;
		 this.y/=mod;
		 this.z/=mod;
	 }
	 
	 public Vec3 unit() {
		 double mod=Math.sqrt(x*x+y*y+z*z);
		 return new Vec3(x/mod,y/mod,z/mod);
	 }
	 
	 public Vec3 unit(Vec3 a) {
		 double mod=Math.sqrt(a.x*a.x+a.y*a.y+a.z*a.z);
		 return new Vec3(a.x/mod,a.y/mod,a.z/mod);
	 }
	 
	 public Vec3 setLen(double a) {
		 double mod=Math.sqrt(x*x+y*y+z*z);
		 x=x/mod*a;
		 y=y/mod*a;
		 z=z/mod*a;
		 return this;
	 }
	 
	 public static Vec3 setLen(Vec3 a,double b) {
		 double mod=Math.sqrt(a.x*a.x+a.y*a.y+a.z*a.z);
		 a.x=a.x/mod*b;
		 a.y=a.y/mod*b;
		 a.z=a.z/mod*b;
		 return a;
	 }
}
