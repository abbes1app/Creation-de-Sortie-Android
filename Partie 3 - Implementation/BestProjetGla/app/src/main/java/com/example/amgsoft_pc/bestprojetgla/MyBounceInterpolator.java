package com.example.amgsoft_pc.bestprojetgla;

import android.view.animation.Interpolator;

public class MyBounceInterpolator implements Interpolator {
    private double mAmplitude = 1;
    private double mFrequency = 10;

    public MyBounceInterpolator() {
    }

    public MyBounceInterpolator(double amplitude, double frequency) {
        mAmplitude = amplitude;
        mFrequency = frequency;
    }

    public float getInterpolation(float time) {
        return (float) ((-1 * Math.pow(Math.E, -time / mAmplitude) * Math.cos(mFrequency * time) + 1) + 0.5f) / 1.5f;
    }
}