package com.zzzyt.as.desktop.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Vector3;
import com.zzzyt.as.AstroSimulator;

public class DesktopProcessor implements InputProcessor {
	public float rotateAngle = 120f;
	public float scrollFactor = -2f;
	private AstroSimulator as;
	private float startX,startY;
	private int button;
	
	@Override
	public boolean keyDown(int keycode) {
		if(keycode==Keys.UP) {
			as.speed*=1.2;
		}
		else if(keycode==Keys.DOWN) {
			as.speed/=1.2;
		}
		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		return true;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		startX = screenX;
		startY = screenY;
		this.button = button;
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		final float deltaX = (screenX - startX) / Gdx.graphics.getWidth();
		final float deltaY = (startY - screenY) / Gdx.graphics.getHeight();
		startX = screenX;
		startY = screenY;
		return process(deltaX, deltaY, button);
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		as.cam.translate(as.cam.direction.cpy().scl(scrollFactor*amount));
		return true;
	}

	protected boolean process (float deltaX, float deltaY, int button) {
		if(button==Buttons.RIGHT) {
			as.cam.rotate(as.cam.up, deltaX * rotateAngle);
			Vector3 left=as.cam.up.cpy().crs(as.cam.direction);
			as.cam.rotate(left,deltaY*rotateAngle);
			as.cam.update();
			return true;
		}
		return false;
	}
	
	public DesktopProcessor() {
		this.as=AstroSimulator.as;
	}
}
