package com.zzzyt.sanae3d;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.zzzyt.sanae3d.entity.Entity;
import com.zzzyt.sanae3d.math.Vec3;

public class OctreeNode {
	public Sanae3d sanae;
	
	public Vec3 pos, absolutePos;
	public double size;
	public boolean isLeaf;
	public OctreeNode par;
	public OctreeNode[] ch;
	public List<Entity> entity;

	public double mass;
	public long time;
	public Vec3 sumPos, sumVel, sumAcc;
	public Vec3 center;
	public int depth;

	public OctreeNode(OctreeNode parent, int position) {
		super();
		this.sanae=Sanae3d.sanae;
		this.size = parent.size / 2;
		this.pos = Octree.offset[position].clone().mul(this.size);
		this.absolutePos = this.pos.add(parent.absolutePos);
		this.par = parent;
		this.ch = new OctreeNode[8];
		this.isLeaf = true;
		this.entity = new CopyOnWriteArrayList<Entity>();
		this.mass = 0;
		this.center = this.pos.clone();
		this.sumPos = new Vec3();
		this.sumVel = new Vec3();
		this.sumAcc = new Vec3();
		this.time = 0;
		this.depth = parent.depth + 1;
	}

	public OctreeNode() {
		super();
		this.sanae=Sanae3d.sanae;
		this.size = 0;
		this.pos = new Vec3();
		this.absolutePos = new Vec3();
		this.par = null;
		this.ch = new OctreeNode[8];
		this.isLeaf = true;
		this.entity = new CopyOnWriteArrayList<Entity>();
		this.mass = 0;
		this.center = this.pos.clone();
		this.sumPos = new Vec3();
		this.sumVel = new Vec3();
		this.sumAcc = new Vec3();
		this.time = 0;
		this.depth = 0;
	}

	public Vec3 getNodePos() {
		return pos;
	}

	public Vec3 getAbsoluteNodePos() {
		return absolutePos;
	}
	
	public boolean add(Entity e) {
		if (!inside(e.getAbsolutePos())) {
			return false;
		}
		e.setPos(e.getPos().subin(pos));
		if (isLeaf) {
			entity.add(e);
			e.node=this;
			return true;
		}
		Vec3 tmp = e.getPos();
		int position = 0;
		if (tmp.x >= pos.x + size / 2) {
			position += 1;
		}
		if (tmp.y >= pos.y + size / 2) {
			position += 2;
		}
		if (tmp.z >= pos.z + size / 2) {
			position += 4;
		}
		if (ch[position] == null) {
			ch[position] = new OctreeNode(this, position);
			isLeaf=false;
		}
		return ch[position].add(e);
	}

	public void refresh() {
		Entity[] tmp=(Entity[]) entity.toArray();
		entity.clear();
		for(int i=0;i<tmp.length;i++) {
			if(!inside(tmp[i].getPos())) {
				OctreeNode newNode=tmp[i].getNode();
				Vec3 p=tmp[i].getPos();
				while(true) {
					if(newNode==null) {
						sanae.octree.add(tmp[i]);
						break;
					}
					else if(newNode.inside(p)) {
						newNode.add(tmp[i]);
						break;
					}
					newNode=newNode.par;
				}
			}
			else {
				entity.add(tmp[i]);
			}
		}
		this.sumPos = new Vec3();
		this.sumVel = new Vec3();
		this.sumAcc = new Vec3();
		this.center = new Vec3();
		this.mass = 0;
		for (Entity e : entity) {
			time = Math.max(time, e.getTime());
		}
		for (Entity e : entity) {
			sumPos.addin(e.getPos(time));
			sumVel.addin(e.getVel(time));
			sumAcc.addin(e.getAcc(time));
			center.addin(e.getPos(time).mul(e.getMass()));
			mass += e.getMass();
		}
		center.div(mass);
	}

	public boolean inside(Vec3 v) {
		return pos.x <= v.x && v.x < pos.x + size && pos.y <= v.y && v.y < pos.y + size && pos.z <= v.z
				&& v.z < pos.z + size;
	}

}
