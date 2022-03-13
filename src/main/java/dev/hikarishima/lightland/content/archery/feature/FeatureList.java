package dev.hikarishima.lightland.content.archery.feature;

import dev.hikarishima.lightland.content.archery.feature.types.FlightControlFeature;
import dev.hikarishima.lightland.content.archery.feature.types.OnHitFeature;
import dev.hikarishima.lightland.content.archery.feature.types.OnPullFeature;
import dev.hikarishima.lightland.content.archery.feature.types.OnShootFeature;

import java.util.ArrayList;
import java.util.List;

public class FeatureList {

	public static boolean canMerge(FeatureList a, FeatureList b) {
		return a.flight == null || b.flight == null;
	}

	public static FeatureList merge(FeatureList a, FeatureList b) {
		if (a.flight != null && b.flight != null) {
			return null;
		}
		FeatureList ans = new FeatureList();
		ans.pull.addAll(a.pull);
		ans.pull.addAll(b.pull);
		ans.shot.addAll(a.shot);
		ans.shot.addAll(b.shot);
		ans.hit.addAll(a.hit);
		ans.hit.addAll(b.hit);
		ans.flight = a.flight != null ? a.flight : b.flight;
		return ans;
	}

	public List<OnPullFeature> pull = new ArrayList<>();
	public List<OnShootFeature> shot = new ArrayList<>();
	public FlightControlFeature flight = null;
	public List<OnHitFeature> hit = new ArrayList<>();

	public FlightControlFeature getFlightControl() {
		return flight == null ? FlightControlFeature.INSTANCE : flight;
	}

	public FeatureList add(BowArrowFeature feature) {
		if (feature instanceof OnPullFeature f) pull.add(f);
		if (feature instanceof OnShootFeature f) shot.add(f);
		if (feature instanceof FlightControlFeature f) flight = f;
		if (feature instanceof OnHitFeature f) hit.add(f);
		return this;
	}

	public void end() {

	}
}
