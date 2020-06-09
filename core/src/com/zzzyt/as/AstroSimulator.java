package com.zzzyt.as;

import java.util.ArrayList;
import java.util.List;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.WindowedMean;
import com.zzzyt.as.input.Handler;
import com.zzzyt.as.util.StyleUtil;
import com.zzzyt.sanae3d.Sanae3d;
import com.zzzyt.sanae3d.entity.Entity;
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
	
	public BitmapFont debugFont;
	public SpriteBatch overlay;
	public WindowedMean fpsCounter;
	
	public double speed=50000;

	private int flag=0;
	
	private long last1=0;
	
	@Override
	public void create() {
		instances = new ArrayList<ModelInstance>();
		trace = new ArrayList<ArrayList<Vector3>>();
		shape = new ShapeRenderer();
		sanae = new Sanae3d(1);
		env = new Environment();
		env.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
		env.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));
		batch = new ModelBatch();
		overlay=new SpriteBatch();
		debugFont=new BitmapFont(Gdx.files.internal("font/debug.fnt"),Gdx.files.internal("font/debug.png"),false);
        debugFont.setColor(Color.WHITE);
		cam = new PerspectiveCamera(70, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		controller=new CameraInputController(cam);
		input.addProcessor(controller);
		Gdx.input.setInputProcessor(input);
		cam.position.set(0f, 200f, 0f);
		cam.lookAt(0, 0, 0);
		cam.near = 1f;
		cam.far = 10000f;
		cam.update();
		controller.translateUnits=10;
		
		fpsCounter=new WindowedMean(30);
		
		ModelBuilder modelBuilder = new ModelBuilder();
		Model tmp=modelBuilder.createSphere(5f, 5f, 5f, 5, 5,
				new Material(ColorAttribute.createDiffuse(Color.WHITE)),Usage.Position | Usage.Normal);
		
		sanae.add(new PointMass(7e29,
				new Vec3(0,1e10,0),
				new Vec3(0,-10000,0)));
		sanae.add(new PointMass(7e29,
				new Vec3(0.5e11,0,0),
				new Vec3(0,10000,28000)));
		sanae.add(new PointMass(7e29,
				new Vec3(1e11,-1e10,0),
				new Vec3(0,0,-28000)));
		
		instances.add(new ModelInstance(tmp));
		trace.add(new ArrayList<Vector3>());
		instances.add(new ModelInstance(tmp));
		trace.add(new ArrayList<Vector3>());
		instances.add(new ModelInstance(tmp));
		trace.add(new ArrayList<Vector3>());
	}

	@Override
	public void render() {
//		handler.handle();
		if(flag>10) {
			System.out.println("Sent signal.");
			System.out.println("t="+(double)sanae.time/1000/60/60/24/365);
			System.out.println("counter1="+sanae.counter1);
			System.out.println("delta1="+(sanae.counter1-last1));
			System.out.println("speed="+speed);
			System.out.println("time="+Gdx.graphics.getDeltaTime());
			System.out.println("step="+sanae.get(0).getStep());
			System.out.println();
			sanae.sim(speed/30);
		}
		else {
			flag++;
		}
		
		controller.update();
		cam.update();
		shape.setProjectionMatrix(cam.combined);

		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		batch.begin(cam);
		for (int i = 0; i < instances.size(); i++) {
			Vector3 pos = ((PointMass)sanae.get(i)).pos.clone().mul(1e-9).toGdx();
			instances.get(i).transform.setTranslation(pos);
			if(i<3) {
				List<Vector3> qq = trace.get(i);
				if(qq.size()==0||qq.get(qq.size()-1).dst(pos)>0.05) {
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
			}
			batch.render(instances.get(i), env);
		}
		batch.end();

		for (int m = 0; m < 3; m++) {
			int i=(m*1)%trace.size();
			float ii = (float) i / trace.size();
			shape.begin(ShapeRenderer.ShapeType.Line);
			Color tmp = StyleUtil.colorize(ii);
			tmp.a = 0.1f;
			shape.setColor(tmp);
			Vector3 last = null;
			for (int k = 0; k < trace.get(i).size(); k++) {
				Vector3 j = trace.get(i).get(k);
				if (last != null) {
					shape.line(last, j);
				}
				last = j;
			}
			shape.line(last, sanae.get(i).getPos(sanae.workers.get(0).time).mul(1e-9).toGdx());
			shape.end();
		}
		
		fpsCounter.addValue((float)(1/Gdx.graphics.getDeltaTime()));
		overlay.begin();
		long stp=Integer.MAX_VALUE;
		for(Entity e:sanae.entities) {
			stp=Math.min(stp, e.getStep());
		}
		debugFont.draw(overlay, String.format("t=%.3f   lag=%.3f  step=%d  calccnt=%d", sanae.get(0).getTime()/8.64e7,(sanae.time-sanae.get(0).getTime())/8.64e7,stp,sanae.counter1-last1), 10, 15);
		debugFont.draw(overlay, String.format("fps=%.2f speed=%.2f",fpsCounter.getMean() ,speed),10,30);
		overlay.end();
		
		last1=sanae.counter1;
	}

	@Override
	public void dispose() {
		batch.dispose();
	}

	@Override
	public void resize(int width, int height) {
		if(cam!=null)reloadCam();
		overlay=new SpriteBatch();
	}

	public void reloadCam() {
		Vector3 oldPos=cam.position.cpy();
		Vector3 oldDir=cam.direction.cpy();
		cam = new PerspectiveCamera(70, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		cam.position.set(oldPos);
		cam.lookAt(oldPos.add(oldDir));
		cam.near = 1f;
		cam.far = 1000f;
		cam.update();
		controller.camera=cam;
	}

	public AstroSimulator() {
		as = this;
		input=new InputMultiplexer();
	}
}
