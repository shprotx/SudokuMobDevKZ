package ru.shprot.sudokumobdevkz.model.game.utils;

import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageView;

import ru.shprot.sudokumobdevkz.R;

public class LoadingOverlay {

    private static final int STEP_DEGREES = 45;
    private static final long STEP_DELAY_MS = 70;

    private final View root;
    private final ImageView wheel;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private float currentRotation = 0f;

    private final Runnable rotateRunnable = new Runnable() {
        @Override
        public void run() {
            currentRotation -= STEP_DEGREES;
            if (currentRotation <= -360f) currentRotation = 0f;
            wheel.setRotation(currentRotation);
            handler.postDelayed(this, STEP_DELAY_MS);
        }
    };

    public LoadingOverlay(View rootView) {
        root = rootView.findViewById(R.id.loadingRoot);
        wheel = rootView.findViewById(R.id.loadingWheel);
    }

    public void show() {
        currentRotation = 0f;
        root.setVisibility(View.VISIBLE);
        handler.post(rotateRunnable);
    }

    public void hide() {
        root.setVisibility(View.GONE);
        handler.removeCallbacks(rotateRunnable);
    }

    public boolean isVisible() {
        return root.getVisibility() == View.VISIBLE;
    }
}
