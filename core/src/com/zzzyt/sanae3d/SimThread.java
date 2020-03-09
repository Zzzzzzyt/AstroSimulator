package com.zzzyt.sanae3d;

import com.zzzyt.sanae3d.entity.Entity;
import com.zzzyt.sanae3d.math.PhyUtil;

public class SimThread extends Thread {
	public final int id;
	private final Sanae3d sanae;
	public long goal;
	public long now;
	private long step;
	private int lastsorted;
	public boolean busy;
	public boolean sort;
	
	public SimThread() {
		this.id=0;
		this.sanae=Sanae3d.sanae;
	}
	
	public SimThread(int id) {
		this.id=id;
		this.sanae=Sanae3d.sanae;
	}
	
	public void run() {
		step=sanae.step;
		double dstep=step/1000d;
		while(true) {
			if(now>=goal||sanae.anyBusy()) {
				busy=false;
				if(sort) {
					int cnt=Math.min((sanae.entities.size()-id)/sanae.workers.size(), SanaeConfig.sortSize);
					for(int i=0;i<cnt;i++) {
						sanae.entities.get(lastsorted).getComputedList().sort();
						lastsorted+=sanae.workers.size();
						if(lastsorted>=sanae.entities.size()) {
							lastsorted=Math.min(sanae.entities.size()-1, id);
						}
					}
				}
				sort=false;
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			else {
				busy=true;
				now+=step;
				for(int i=id;i<sanae.entities.size();i+=sanae.workers.size()) {
					Entity me=sanae.entities.get(i);
					me.getAcceleration().setZero();
					for(int j=0;j<SanaeConfig.mostCompSize&&j<me.getComputedList().size();j++) {
						Entity e=me.getComputedList().get(j);
						double tmp=PhyUtil.gravity(e, me)/me.getMass();
						if(j>=SanaeConfig.leastCompSize
							&&tmp<SanaeConfig.ignoredAcceleration) {
							break;
						}
						me.getAcceleration().a(e.getPos().sub(me.getPos()).setLen(tmp));
					}
					me.getPos().a(PhyUtil.posDelta(me.getVelocity(), me.getAcceleration(), dstep));
					me.getVelocity().a(me.getAcceleration().mul(dstep));
				}
			}
		}
	}
}
