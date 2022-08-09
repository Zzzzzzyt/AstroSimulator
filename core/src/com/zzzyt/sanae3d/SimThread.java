package com.zzzyt.sanae3d;

import com.zzzyt.as.AstroSimulator;
import com.zzzyt.sanae3d.entity.Entity;
import com.zzzyt.sanae3d.math.PhyUtil;
import com.zzzyt.sanae3d.math.Vec3;

public class SimThread extends Thread {
    public final int id;
    private final Sanae3d sanae;
    public long goal;
    public long time;
    private int lastsorted;
    public boolean busy;
    public boolean sort;

    private long last = -1;

    public SimThread() {
        this.sanae = Sanae3d.sanae;
        this.id = sanae.workers.size();
        setName("Physics Thread " + id);
    }

    public SimThread(int id) {
        this.id = id;
        this.sanae = Sanae3d.sanae;
        setName("Physics Thread " + id);
    }

    public void run() {
        while (true) {
            busy = true;
            while (time < goal) {
                long minTime = Long.MAX_VALUE;
                for (int i = id; i < sanae.size(); i += sanae.workerCount()) {
                    Entity me = sanae.get(i);
                    long time2 = me.getTime();
                    if (time >= me.getTime() + me.getStep()) {
                        Vec3 atmp = Vec3.ZERO.clone();
                        Vec3 vtmp = me.getVelocity(time2).clone();
                        Vec3 ptmp = me.getPos(time2).clone();
                        for (int j = 0; j < SanaeConfig.mostCompSize && j < me.getComputedList().size(); j++) {
                            sanae.counter1++;
                            Entity e = me.getComputedList().get(j);
                            Vec3 epos = e.getPos(time).clone();
                            double tmp = PhyUtil.gravity(me.getMass(), e.getMass(), ptmp, epos) / me.getMass();
                            if (j >= SanaeConfig.leastCompSize && tmp < SanaeConfig.ignoredAcceleration) {
                                break;
                            }
                            atmp.a(epos.sub(ptmp).setLen(tmp));
                        }
                        ptmp.a(PhyUtil.posDelta(vtmp, atmp, (double) (time - time2) / 1000d));
                        vtmp.a(atmp.mul((double) (time - time2) / 1000d));
                        long mstep = PhyUtil.estimateStep(me.getAcceleration(me.getTime()), atmp);
                        me.setAcceleration(atmp);
                        me.setVelocity(vtmp);
                        me.setPos(ptmp);
                        me.setTime(time);
                        me.setStep(mstep);
                    }
                    minTime = Math.min(minTime, me.getTime() + me.getStep());
                }
                time = minTime;
                long now = time / 10000000;
                if (now > last) {
                    last = now;
                    // TODO
                    for (int i = 0; i < AstroSimulator.as.tmppos.size(); i++) {
                        AstroSimulator.as.tmppos.get(i).add(sanae.get(i).getPos(now * 10000000));
                    }
                }
            }
            busy = false;
            if (sort) {
                int cnt = Math.min((sanae.entities.size() - id) / sanae.workers.size(), SanaeConfig.sortSize);
                for (int i = 0; i < cnt; i++) {
                    if (sanae.entities.get(lastsorted).isTiny())
                        continue;
                    sanae.counter2++;
                    sanae.entities.get(lastsorted).getComputedList().sort(time);
                    lastsorted += sanae.workers.size();
                    if (lastsorted >= sanae.entities.size()) {
                        lastsorted = Math.min(sanae.entities.size() - 1, id);
                    }
                }
            }
            sort = false;
//			System.out.println("[Thread "+id+"] Finished.");
            try {
                sleep(100);
            } catch (InterruptedException e) {
            }
        }
    }
}
