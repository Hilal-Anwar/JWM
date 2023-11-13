package com.jwm;

import io.github.humbleui.jwm.*;
import io.github.humbleui.jwm.skija.EventFrameSkija;
import io.github.humbleui.skija.*;
import io.github.humbleui.types.IRect;

import java.util.function.Consumer;


public class Test implements Consumer<Event> {
    public Window window;
    public static int PADDING = 10;
    public static int COLS = 4, ROWS = 3;
    public static Font FONT12 = new Font(FontMgr.getDefault().matchFamilyStyleCharacter("", FontStyle.ITALIC,
            null, "↑".codePointAt(0)), 12);
    public static Font FONT24 = new Font(FontMgr.getDefault().matchFamilyStyle("", FontStyle.ITALIC), 24);
    public static Font FONT48 = new Font(FontMgr.getDefault().matchFamilyStyle("", FontStyle.ITALIC), 48);

    public float lastScale = 1f;
    public boolean initialized = false;
    public boolean paused = true;
    public PanelScreens panelScreens;
    public PanelRendering panelRendering;

    public Test() {
        window = App.makeWindow();
        window.setEventListener(this);
        panelScreens = new PanelScreens(window);
        panelRendering = new PanelRendering(window);
        var scale = window.getScreen().getScale();
        int count = App._windows.size() - 1;
        Screen screen = App.getScreens()[count / 5 % App.getScreens().length];
        IRect bounds = screen.getWorkArea();
        window.setTitle("TicTacToe");
        window.setWindowSize(bounds.getWidth() / 2, bounds.getHeight() / 2);
        panelScreens.setTitleStyle(panelScreens.titleStyles.get(count));
        switch (count % 5) {
            case 0 -> window.setWindowPosition(bounds.getLeft() + bounds.getWidth() / 4,
                    bounds.getTop() + bounds.getHeight() / 4);
            case 1 -> window.setWindowPosition(bounds.getLeft(), bounds.getTop());
            case 2 -> window.setWindowPosition(bounds.getLeft() + bounds.getWidth() / 2, bounds.getTop());
            case 3 -> window.setWindowPosition(bounds.getLeft(), bounds.getTop() + bounds.getHeight() / 2);
            case 4 ->
                    window.setWindowPosition(bounds.getLeft() + bounds.getWidth() / 2,
                            bounds.getTop() + bounds.getHeight() / 2);
        }
        window.setVisible(true);
        initialized = true;
    }

    public void paint(Canvas canvas, int width, int height) {
        float scale = window.getScreen().getScale();
        PADDING = (int) (10 * scale);
        int panelWidth = (width - (COLS + 1) * PADDING) / COLS;
        int panelHeight = (height - (ROWS + 1) * PADDING) / ROWS;
        if (panelWidth <= 0 || panelHeight <= 0)
            return;

        if (lastScale != scale) {
            FONT12.setSize(12 * scale);
            FONT24.setSize(24 * scale);
            FONT48.setSize(48 * scale);
        }
        canvas.clear(0xFFfafafa);
        int canvasCount = canvas.save();

        // First row
        //panelScreens.paint(canvas, PADDING,
                //PADDING, width, height, scale);
        //panelRendering.paint(canvas, PADDING + (panelWidth + PADDING) * 2, PADDING + (panelHeight + PADDING), panelWidth, panelHeight, scale);




        // Colored bars
       /* try (var paint = new Paint()) {
            var barSize = 3 * scale;

            // left
            paint.setColor(0xFFe76f51);
            canvas.drawRect(Rect.makeXYWH(0, 0, barSize, 100 * scale), paint);
            canvas.drawRect(Rect.makeXYWH(0, height / 2f - 50 * scale, barSize, 100 * scale), paint);
            canvas.drawRect(Rect.makeXYWH(0, height - 100 * scale, barSize, 100 * scale), paint);

            // top
            paint.setColor(0xFF2a9d8f);
            canvas.drawRect(Rect.makeXYWH(0, 0, 100 * scale, barSize), paint);
            canvas.drawRect(Rect.makeXYWH(width / 2f - 50 * scale, 0, 100 * scale, barSize), paint);
            canvas.drawRect(Rect.makeXYWH(width - 100 * scale, 0, 100 * scale, barSize), paint);

            // right
            paint.setColor(0xFFe9c46a);
            canvas.drawRect(Rect.makeXYWH(width - barSize, 0, barSize, 100 * scale), paint);
            canvas.drawRect(Rect.makeXYWH(width - barSize, height / 2f - 50 * scale, barSize, 100 * scale), paint);
            canvas.drawRect(Rect.makeXYWH(width - barSize, height - 100 * scale, barSize, 100 * scale), paint);

            // bottom
            paint.setColor(0xFFFFFFFF);
            canvas.drawRect(Rect.makeXYWH(0, height - barSize, 100 * scale, barSize), paint);
            canvas.drawRect(Rect.makeXYWH(width / 2f - 50 * scale, height - barSize, 100 * scale, barSize), paint);
            canvas.drawRect(Rect.makeXYWH(width - 100 * scale, height - barSize, 100 * scale, barSize), paint);
        }*/
        canvas.restoreToCount(canvasCount);
    }

    @Override
    public void accept(Event e) {
        if (!initialized)
            return;
        if (e instanceof EventWindowClose)
        {
            if (App._windows.isEmpty())
                App.terminate();
            return;
        }
        panelScreens.accept(e);
        if (e instanceof EventFrame)
        {
            if (!paused)
                window.requestFrame();
        }
        else if (e instanceof EventFrameSkija ee)
        {
            Surface s = ee.getSurface();
            paint(s.getCanvas(), s.getWidth(), s.getHeight());
        }
        else if (e instanceof EventWindowCloseRequest)
        {
            window.close();
        }
    }

    public static void main(String[] args) {
        App.start(Test::new);
    }
}
