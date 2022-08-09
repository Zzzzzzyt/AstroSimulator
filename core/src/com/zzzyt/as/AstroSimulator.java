package com.zzzyt.as;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.zzzyt.as.input.Handler;
import com.zzzyt.as.util.StyleUtil;
import com.zzzyt.sanae3d.Sanae3d;
import com.zzzyt.sanae3d.entity.PointMass;
import com.zzzyt.sanae3d.math.Vec3;

public class AstroSimulator extends ApplicationAdapter {
    public static AstroSimulator as;

    public Sanae3d sanae;
    public Environment env;
    public PerspectiveCamera cam;
    public InputMultiplexer input;
    public ModelBatch batch;
    public ShapeRenderer shape;
    public List<ModelInstance> instances;
    public List<ArrayList<Vector3>> trace;
    public CameraInputController controller;
    public Handler handler;

    public List<ArrayList<Vec3>> tmppos;

    //    public double speed = 500000;
    public double speed = 200000;

    private int flag = 0;

    private long last1 = 0, last2 = 0;
    private static final boolean playback = true;
    private static final int bodyCount = 1;

    @Override
    public void create() {
//        Gdx.input.setCursorCatched(true);
        instances = new ArrayList<ModelInstance>();
        trace = new ArrayList<ArrayList<Vector3>>();
        shape = new ShapeRenderer();
        sanae = new Sanae3d(1);
        env = new Environment();
        env.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        env.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));
        batch = new ModelBatch();
        cam = new PerspectiveCamera(70, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        controller = new CameraInputController(cam);
        input.addProcessor(controller);
        Gdx.input.setInputProcessor(input);
        cam.position.set(0f, 200f, 100f);
        cam.lookAt(0, 0, 0);
        cam.near = 1f;
        cam.far = 10000f;
        cam.update();
        controller.translateUnits = 10;

        tmppos = new ArrayList<ArrayList<Vec3>>();
        for (int i = 0; i < bodyCount + 3; i++) {
            tmppos.add(new ArrayList<Vec3>());
        }

        sanae.add(new PointMass(7e29, new Vec3(0, 1e10, 0), new Vec3(0, -10000, 0)));
        sanae.add(new PointMass(7e29, new Vec3(0.5e11, 0, 0), new Vec3(0, 10000, 28000)));
        sanae.add(new PointMass(7e29, new Vec3(1e11, -1e10, 0), new Vec3(0, 0, -28000)));
//        sanae.add(new PointMass(7e29, new Vec3(1e11, -1e10, 0), new Vec3(0, 0, -29000)));
        for (int i = 0; i < bodyCount; i++) {
//            sanae.add(new PointMass(1, new Vec3(1e11, -1e10, 5e10 + i * 1e9), new Vec3(0, 0, -25000 + i * 1000)));
//            sanae.add(new PointMass(1, new Vec3(1e11, -1e10, 9e10), new Vec3(0, 0, 15000)));
            sanae.add(new PointMass(1, new Vec3(0, 2.4e10, 0), new Vec3(30000, -10000, 1000)));
//            sanae.add(new PointMass(1, new Vec3(1e11, -3e10, 0), new Vec3(-10000, 0, 0)));
        }
        instances.add(new ModelInstance(generateModel(new Color(255 / 255f, 128 / 255f, 128 / 255f, 1f), 5f)));
        trace.add(new ArrayList<Vector3>());
        instances.add(new ModelInstance(generateModel(new Color(255 / 255f, 255 / 255f, 128 / 255f, 1f), 5f)));
        trace.add(new ArrayList<Vector3>());
        instances.add(new ModelInstance(generateModel(new Color(128 / 255f, 192 / 255f, 255 / 255f, 1f), 5f)));
        trace.add(new ArrayList<Vector3>());
        for (int i = 0; i < bodyCount; i++) {
//            instances.add(new ModelInstance(generateModel(StyleUtil.colorize((float) i / bodyCount))));
            instances.add(new ModelInstance(generateModel(Color.WHITE, 3f)));
            trace.add(new ArrayList<Vector3>());
        }

        if (playback) {
            File f = new File("D:/MyJava/AstroSimulator/out.txt");
            try {
                Scanner sc = new Scanner(new FileInputStream(f));
                int cnt = 0;
                for (int j = 0; j < 4; j++) {
                    System.out.println(j);
                    int sz = sc.nextInt();
                    System.out.println(sz);
//                    boolean valid = j == 0 || j == 1 || j == 2 || j == 43;
                    boolean valid = true;
                    System.out.println(valid);
                    for (int i = 0; i < sz; i++) {
                        Vec3 tmp = new Vec3(sc.nextDouble(), sc.nextDouble(), sc.nextDouble());
                        if (valid) {
                            tmppos.get(cnt).add(tmp);
                        }
                    }
                    if (valid) {
                        System.out.println(tmppos.get(cnt).get(sz - 1).mod());
                        cnt++;
                    }
                }
                sc.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void render() {
        handler.handle();
        if (flag > 240) {
//            System.out.println("Sent signal.");
//            System.out.println("t=" + (double) sanae.time / 1000 / 60 / 60 / 24 / 365.24);
            System.out.println("t=" + (double) sanae.workers.get(0).time / 1000 / 60 / 60 / 24 / 365.24);
//            System.out.println("realT=" + (double) sanae.workers.get(0).time / 1000 / 60 / 60 / 24 / 365.24);
//            System.out.println("counter1=" + sanae.counter1);
//            System.out.println("counter2=" + sanae.counter2);
//            System.out.println("delta1=" + (sanae.counter1 - last1));
//            System.out.println("delta2=" + (sanae.counter2 - last2));
//            System.out.println("speed=" + speed);
//            System.out.println("time=" + Gdx.graphics.getDeltaTime());
//            System.out.println("step=" + sanae.get(1).getStep());
//            System.out.println();
            last1 = sanae.counter1;
            last2 = sanae.counter2;
            if (!playback) sanae.sim(speed / 30);
            sanae.time += Math.round(speed / 30 * 1000);
        } else {
            flag++;
        }
//		double mod=0;
//		long stp=0;
//		for(Entity e:sanae.entities) {
//			mod=Math.max(mod,e.getAcceleration(e.getTime()).mod());
//			if(e.getPos().mod()>400)continue;
//			stp=Math.max(stp,e.getStep());
//		}
//		System.out.println(mod);
//		System.out.println(stp);

        controller.update();
//		cam.position.set(sanae.get(1).getPos().mul(1e-9).add(new Vec3(0,10,0)).toGdx());
        cam.update();
        shape.setProjectionMatrix(cam.combined);

        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        for (int i = 0; i < instances.size(); i++) {
//			Vector3 pos = sanae.get(i).getPos().mul(1e-9).toGdx();
            Vector3 pos = null;
            if (playback) {
                pos = tmppos.get(i).get((int) (sanae.time / 10000000)).clone().mul(1e-9).toGdx();
            } else {
                pos = sanae.get(i).getPos(sanae.workers.get(0).time).clone().mul(1e-9).toGdx();
            }
            instances.get(i).transform.setTranslation(pos);
//            if (i < 3) {
            List<Vector3> qq = trace.get(i);
            if (qq.size() == 0 || qq.get(qq.size() - 1).dst(pos) > 0.05) {
                if (qq.size() <= 100) {
                    qq.add(pos);
                } else if (qq.get(qq.size() - 1).dst(pos) > 100) {
                    qq.add(pos);
                } else {
                    Vector3 v1 = qq.get(qq.size() - 1).cpy().sub(qq.get(qq.size() - 2).cpy());
                    v1 = v1.scl(1 / v1.len());
                    Vector3 v2 = pos.cpy().sub(qq.get(qq.size() - 1).cpy());
                    v2 = v2.scl(1 / v2.len());
                    if (v1.dot(v2) > -0.9) {
                        qq.add(pos);
                    }
                }
            }
//            }
        }

        shape.begin(ShapeRenderer.ShapeType.Line);
        shape.setColor(Color.DARK_GRAY);
        for (int i = -100; i <= 100; i += 10) {
//            shape.line(i + 50f, -30f, -100f, i + 50f, -30f, 100f);
//            shape.line(-100f + 50f, -30f, i, 100f + 50f, -30f, i);
            betterLine(shape, i + 50f, -30f, -100f, i + 50f, -30f, 100f);
            betterLine(shape, -100f + 50f, -30f, i, 100f + 50f, -30f, i);
        }
        shape.end();

        for (int m = 0; m < trace.size(); m++) {
            // if(sanae.get(i).getPos().mod()>400)continue;
            Color tmp = null;
            if (m < 3) {
                int i = m % 3;
                float ii = (float) i / 3f;
                tmp = StyleUtil.colorize(ii);
            } else {
                tmp = Color.GRAY;
            }
            shape.begin(ShapeRenderer.ShapeType.Line);

            tmp.a = 0.1f;
            shape.setColor(tmp);
            Vector3 last = null;
            int size = trace.get(m).size();
            int start = Math.max(0, size - 500);
            for (int k = start; k < size; k++) {
                Vector3 j = trace.get(m).get(k);
                if (last != null) {
//                    shape.line(last, j);
                    betterLine(shape, last.x, last.y, last.z, j.x, j.y, j.z);
                }
                last = j;
            }
//			shape.line(last, sanae.get(i).getPos(sanae.workers.get(0).time).mul(1e-9).toGdx());
            shape.end();
        }


        batch.begin(cam);
        for (int i = 0; i < instances.size(); i++) {
            batch.render(instances.get(i), env);
        }
        batch.end();

        if (playback) {
            cam.position.set(200 * MathUtils.cos((float) sanae.time / 1000 / 60 / 60 / 24 / 365.24f * 5 + 1f),
                    100f, 200 * MathUtils.sin((float) sanae.time / 1000 / 60 / 60 / 24 / 365.24f * 5 + 1f));
            cam.direction.set(cam.position);
            cam.direction.scl(-1);
            cam.direction.nor();
            cam.up.set(-100 * MathUtils.cos((float) sanae.time / 1000 / 60 / 60 / 24 / 365.24f * 5 + 1f), 200f,
                    -100 * MathUtils.sin((float) sanae.time / 1000 / 60 / 60 / 24 / 365.24f * 5 + 1f));
            cam.up.nor();
            cam.position.scl(0.5f);
            cam.position.add(50f, 0f, 0f);
            cam.update();
        }

        if (!playback) {
            if (sanae.workers.get(0).time > 1000 * 60 * 60 * 24 * 365.24 * 5) {
                for (int i = 0; i < bodyCount; i++) {
                    System.out.println(i);
                    System.out.println(sanae.get(3 + i).getPos().mod());
                }
                File f = new File("D:/MyJava/AstroSimulator/out.txt");
                try {
                    PrintWriter os = new PrintWriter(new FileOutputStream(f));
                    for (int j = 0; j < tmppos.size(); j++) {
                        int tmp = tmppos.get(j).size();
                        os.println(tmp);
                        for (int i = 0; i < tmp; i++) {
                            os.printf("%.10f %.10f %.10f\n", tmppos.get(j).get(i).x, tmppos.get(j).get(i).y,
                                    tmppos.get(j).get(i).z);
                        }
                    }
                    System.out.println("saved");
                    os.close();
                    Gdx.app.exit();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private Model generateModel(Color color, float size) {
        ModelBuilder modelBuilder = new ModelBuilder();
        return modelBuilder.createSphere(size, size, size, 32, 32,
                new Material(ColorAttribute.createDiffuse(color)),
                Usage.Position | Usage.Normal);
    }

    @Override
    public void dispose() {
        batch.dispose();
    }

    @Override
    public void resize(int width, int height) {
        if (cam != null)
            reloadCam();
    }

    public void reloadCam() {
        Vector3 oldPos = cam.position.cpy();
        Vector3 oldDir = cam.direction.cpy();
        cam = new PerspectiveCamera(70, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam.position.set(oldPos);
        cam.lookAt(oldPos.add(oldDir));
        cam.near = 1f;
        cam.far = 1000f;
        cam.update();
        controller.camera = cam;
    }

    public Vector2 project(float x, float y, float z) {
        Vector3 tmp = cam.project(new Vector3(x, y, z));
        return new Vector2(tmp.x, tmp.y);
    }

    public void betterLine(ShapeRenderer shape, float x1, float y1, float z1, float x2, float y2, float z2) {
        float k = 0.075f;
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                for (int dz = -1; dz <= 1; dz++) {
                    shape.line(x1 + dx * k, y1 + dy * k, z1 + dz * k, x2 + dx * k, y2 + dy * k, z2 + dz * k);
                }
            }
        }

    }

    public AstroSimulator() {
        as = this;
        input = new InputMultiplexer();
    }
}
