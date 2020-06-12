package com.zzzyt.sanae3d;

import com.zzzyt.sanae3d.entity.Entity;
import com.zzzyt.sanae3d.math.Vec3;

public class Octree {
	public static final Vec3 offset[]= {
			new Vec3(0,0,0),
			new Vec3(1,0,0),
			new Vec3(0,1,0),
			new Vec3(1,1,0),
			new Vec3(0,0,1),
			new Vec3(1,0,1),
			new Vec3(0,1,1),
			new Vec3(1,1,1)};
	public OctreeNode root;
	public OctreeNode[] roots;
	
	public Octree() {
		this.root=new OctreeNode();
		root.pos=new Vec3();
		root.size=Double.MAX_VALUE;
		this.roots=new OctreeNode[8];
		for(int i=0;i<8;i++) {
			this.roots[i]=new OctreeNode();
			this.roots[i].par=root;
			this.roots[i].pos=offset[i].clone().mul(-SanaeConfig.octreeDefaultSize);
			this.roots[i].size=SanaeConfig.octreeDefaultSize;
		}
	}
	
	public boolean add(Entity e) {
		for(int i=0;i<8;i++) {
			if(roots[i].add(e))return true;
		}
		return false;
	}
	
	public static Vec3 dst(Entity a,Entity b,long t) {
		OctreeNode na=a.getNode();
		OctreeNode nb=b.getNode();
		Vec3 pa=a.getPos(t);
		Vec3 pb=b.getPos(t);
		while(na.depth<nb.depth) {
			pb.addin(nb.pos);
			nb=nb.par;
		}
		while(na.depth>nb.depth) {
			pa.addin(na.pos);
			na=na.par;
		}
		while(na!=nb) {
			pa.addin(na.pos);
			na=na.par;
			pb.addin(nb.pos);
			nb=nb.par;
		}
		return pa.sub(pb);
	}
}
