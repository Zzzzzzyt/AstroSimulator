package com.zzzyt.as.util;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;

public class StyleUtil {
	
	public static float squaredAvg(float x, float y, float a, float b) {
		return (float) Math.sqrt((x * x * a + y * y * b) / (a + b));
	}
	
	public static Color mixColorSquared(float r1, float g1, float b1, float r2, float g2, float b2, float a, float b) {
		float r3 = MathUtils.clamp(squaredAvg(r1, r2, a, b)/255,0,1);
		float g3 = MathUtils.clamp(squaredAvg(g1, g2, a, b)/255,0,1);
		float b3 = MathUtils.clamp(squaredAvg(b1, b2, a, b)/255,0,1);
		return new Color(r3,g3,b3,1);
	}

	public static Color colorize(float x) {
		if (x <= 0.25) {
			return mixColorSquared(255, 0, 0, 255, 127, 0, 0.25f - x, x);
		} else if (x <= 0.5) {
			return mixColorSquared(255, 127, 0, 0, 255, 0, 0.5f - x, x - 0.25f);
		} else if (x <= 0.75) {
			return mixColorSquared(0, 255, 0, 0, 0, 255, 0.75f - x, x - 0.5f);
		}
		return mixColorSquared(0, 0, 255, 255, 0, 255, 1.0f - x, x - 0.75f);
	}
}
