package com.zzzyt.as.desktop.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Vector3;
import com.zzzyt.as.AstroSimulator;
import com.zzzyt.as.input.Handler;

public class DesktopHandler implements Handler {
	public float translateUnits = 1f;
	AstroSimulator as;
	
	@Override
	public void handle() {
		if(Gdx.input.isKeyPressed(Keys.W)) {
			Vector3 dir=as.cam.direction.cpy();
			as.cam.translate(dir.scl(translateUnits));
		}
		if(Gdx.input.isKeyPressed(Keys.S)) {
			Vector3 dir=as.cam.direction.cpy();
			as.cam.translate(dir.scl(-translateUnits));
		}
		if(Gdx.input.isKeyPressed(Keys.SPACE)) {
			Vector3 dir=as.cam.up.cpy();
			as.cam.translate(dir.scl(translateUnits));
		}
		if(Gdx.input.isKeyPressed(Keys.SHIFT_LEFT)) {
			Vector3 dir=as.cam.up.cpy();
			as.cam.translate(dir.scl(-translateUnits));
		}
		if(Gdx.input.isKeyPressed(Keys.A)) {
			Vector3 dir=as.cam.up.cpy().crs(as.cam.direction);;
			as.cam.translate(dir.scl(translateUnits));
		}
		if(Gdx.input.isKeyPressed(Keys.D)) {
			Vector3 dir=as.cam.up.cpy().crs(as.cam.direction);;
			as.cam.translate(dir.scl(-translateUnits));
		}
	}

	public DesktopHandler() {
		this.as=AstroSimulator.as;
	}
	
}
