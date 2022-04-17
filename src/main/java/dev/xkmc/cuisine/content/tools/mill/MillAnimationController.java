package dev.xkmc.cuisine.content.tools.mill;

import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;

public class MillAnimationController extends AnimationController<MillBlockEntity> {

	private double last_tick = 0, frame = 0;
	private boolean paused = true;

	public MillAnimationController(MillBlockEntity animatable) {
		super(animatable, "main", 0, MillAnimationController::test);
	}

	private static PlayState test(AnimationEvent<MillBlockEntity> event) {
		if (event.getController() instanceof MillAnimationController controller) {
			controller.setAnimation(new AnimationBuilder().addAnimation("rotate", true));
			controller.testFrame(event.getAnimatable());
		}
		return PlayState.CONTINUE;
	}

	private void testFrame(MillBlockEntity te) {
		paused = te.rotate_time == 0;
	}

	@Override
	protected double adjustTick(double tick) {
		if (last_tick == 0) {
			last_tick = tick;
		}
		if (!paused) {
			frame += tick - last_tick;
		}
		last_tick = tick;
		if (currentAnimation != null && currentAnimation.animationLength != null)
			frame %= currentAnimation.animationLength;
		return frame;
	}
}
