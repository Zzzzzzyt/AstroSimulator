package com.zzzyt.sanae3d;

import com.zzzyt.sanae3d.entity.Entity;
import com.zzzyt.sanae3d.math.PhyUtil;
import com.zzzyt.sanae3d.math.Vec3;

public class SimThread extends Thread {
	public final int id;
	private final Sanae3d sanae;
	public long goal;
	public long time;
	public boolean busy;
	
	public SimThread() {
		this.sanae=Sanae3d.sanae;
		this.id=sanae.workers.size();
		setName("Physics Thread "+id);
	}
	
	public SimThread(int id) {
		this.id=id;
		this.sanae=Sanae3d.sanae;
		setName("Physics Thread "+id);
	}
	
	public void run() {
		while(true) {
			busy=true;
			while(time<goal) {
				long minTime=Long.MAX_VALUE;
				for(int i=id;i<sanae.size();i+=sanae.workerCount()) {
					Entity me=sanae.get(i);
					long timeOld=me.getTime();
					if(time>=me.getTime()+me.getStep()) {
						Vec3 atmp=new Vec3();
						Vec3 vtmp=me.getVel(timeOld).clone();
						Vec3 ptmp=me.getPos(timeOld).clone();
						for(int j=0;j<sanae.size();j++) {
							if(j==i)continue;
							Entity e=sanae.get(j);
							if(e.isTiny())continue;
							sanae.counter1++;
							double tmp=PhyUtil.gravity(me,e,time)/me.getMass();
							atmp.addin(Octree.dst(e,me,time).setLen(tmp));
						}
						ptmp.addin(PhyUtil.posDelta(vtmp, atmp, (double)(time-timeOld)/1000d));
						vtmp.addin(atmp.mul((double)(time-timeOld)/1000d));
						long mstep=PhyUtil.estimateStep(me.getAcc(me.getTime()), atmp,time-timeOld);
						me.setAcc(atmp);
						me.setVel(vtmp);
						me.setPos(ptmp);
						me.setTime(time);
						me.setStep(mstep);
					}
					minTime=Math.min(minTime, me.getTime()+me.getStep());
				}
				time=minTime;
			}
			busy=false;
//			System.out.println("[Thread "+id+"] Finished.");
			try {
				sleep(100);
			} catch (InterruptedException e) {
			}
		}
	}
}
