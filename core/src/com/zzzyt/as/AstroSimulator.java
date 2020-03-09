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
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
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
	
	public double speed=1;

	@Override
	public void create() {
		instances = new ArrayList<ModelInstance>();
		trace = new ArrayList<ArrayList<Vector3>>();
		shape = new ShapeRenderer();
		sanae = new Sanae3d(16);
		env = new Environment();
		env.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
		env.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));
		batch = new ModelBatch();
		cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		controller=new CameraInputController(cam);
		input.addProcessor(controller);
		Gdx.input.setInputProcessor(input);
		cam.position.set(300f, 300f, 300f);
		cam.lookAt(0, 0, 0);
		cam.near = 1f;
		cam.far = 1000f;
		cam.update();
		controller.translateUnits=10;
		
		ModelBuilder modelBuilder = new ModelBuilder();
		int count = 10;
		sanae.addEntity(new PointMass(1e17, Vec3.ZERO.clone()));
		instances.add(new ModelInstance(modelBuilder.createSphere(10f, 10f, 10f, 20, 20,
				new Material(ColorAttribute.createDiffuse(Color.BLUE)),
				Usage.Position | Usage.Normal)));
		trace.add(new ArrayList<Vector3>());
		for (int i = 0; i < count; i++) {
			sanae.addEntity(new PointMass(1,
					new Vec3(Math.random() * 200 - 100, Math.random() * 200 - 100, Math.random() * 200 - 100),
					new Vec3(Math.random() * 1000 - 500, Math.random() * 1000 - 500, Math.random() * 1000 - 500)));
			instances.add(new ModelInstance(modelBuilder.createSphere(1f, 1f, 1f, 20, 20,
					new Material(ColorAttribute.createDiffuse(StyleUtil.colorize((float) i / count))),
					Usage.Position | Usage.Normal)));
			trace.add(new ArrayList<Vector3>());
		}
	}

	@Override
	public void render() {
		handler.handle();
		
		sanae.sim((double) Gdx.graphics.getDeltaTime()*speed);

		controller.update();
		cam.update();
		shape.setProjectionMatrix(cam.combined);

		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		batch.begin(cam);
		for (int i = 0; i < instances.size(); i++) {
			Vector3 pos = sanae.entities.get(i).getPos().toGdx();
			instances.get(i).transform.setTranslation(pos);
			List<Vector3> qq = trace.get(i);
			if (qq.size() <= 10) {
				qq.add(pos);
			} else if (qq.get(qq.size() - 1).dst(pos) > 100) {
				qq.add(pos);
			} else {
				Vector3 v1 = qq.get(qq.size() - 1).cpy().sub(qq.get(qq.size() - 2).cpy());
				v1 = v1.scl(1 / v1.len());
				Vector3 v2 = pos.cpy().sub(qq.get(qq.size() - 1).cpy());
				v2 = v2.scl(1 / v2.len());
				if (v1.dot(v2) > -0.7) {
					qq.add(pos);
				}
			}
			batch.render(instances.get(i), env);
		}
		batch.end();

		for (int i = 0; i < trace.size(); i++) {
			float ii = (float) i / trace.size();
			shape.begin(ShapeRenderer.ShapeType.Line);
			Color tmp = StyleUtil.colorize(ii);
			tmp.a = 0.5f;
			shape.setColor(tmp);
			Vector3 last = null;
			for (int k = 0; k < trace.get(i).size(); k++) {
				Vector3 j = trace.get(i).get(k);
				if (last != null) {
					shape.line(last, j);
				}
				last = j;
			}
			shape.line(last, sanae.entities.get(i).getPos().toGdx());
			shape.end();
		}
	}

	@Override
	public void dispose() {
		batch.dispose();
	}

	@Override
	public void resize(int width, int height) {
		if(cam!=null)reloadCam();
	}

	public void reloadCam() {
		Vector3 oldPos=cam.position.cpy();
		Vector3 oldDir=cam.direction.cpy();
		cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
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
