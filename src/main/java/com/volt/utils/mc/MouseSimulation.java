package com.volt.utils.mc;

import org.lwjgl.glfw.GLFW;
import com.volt.IMinecraft;
import com.volt.mixin.MinecraftClientAccessor;
import com.volt.mixin.MouseHandlerAccessor;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class MouseSimulation implements IMinecraft {
	public static HashMap<Integer, Boolean> mouseButtons = new HashMap<>();
	public static ExecutorService clickExecutor = Executors.newFixedThreadPool(100);

	public static MouseHandlerAccessor getMouseHandler() {
		return (MouseHandlerAccessor) ((MinecraftClientAccessor) mc).getMouse();
	}

	public static boolean isMouseButtonPressed(int keyCode) {
		Boolean key = mouseButtons.get(keyCode);
		return key != null ? key : false;
	}

	public static void mousePress(int keyCode) {
		mouseButtons.put(keyCode, true);
		getMouseHandler().press(mc.getWindow().getHandle(), keyCode, GLFW.GLFW_PRESS, 0);
	}

	public static void mouseRelease(int keyCode) {
		getMouseHandler().press(mc.getWindow().getHandle(), keyCode, GLFW.GLFW_RELEASE, 0);
	}

	public static void mouseClick(int keyCode, int millis) {
		clickExecutor.submit(() -> {
			try {
				MouseSimulation.mousePress(keyCode);
				Thread.sleep(millis);
				MouseSimulation.mouseRelease(keyCode);
			} catch (InterruptedException ignored) {

			}
		});
	}

	public static void mouseClick(int keyCode) {
		mouseClick(keyCode, 35);
	}
}
