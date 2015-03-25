package helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;

import java.nio.ByteBuffer;

public class ScreenShot implements Runnable
{
    private static int fileCounter = 0;
    private Pixmap pixmap;

    @Override
    public void run()
    {
        saveScreenshot();
    }

    public void prepare()
    {
        getScreenshot(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
    }

    public void saveScreenshot()
    {
        FileHandle fh;
        do{
            fh = new FileHandle("screenshots/screenshot.png");
            fh.delete();
            //fh = new FileHandle("screenshots/screenshot.png");
        }while (fh.exists());
        PixmapIO.writePNG(fh, pixmap);
        pixmap.dispose();
    }

    public void getScreenshot(int x, int y, int w, int h, boolean yDown)
    {
        Gdx.gl.glPixelStorei(GL20.GL_PACK_ALIGNMENT, 1);
        pixmap = new Pixmap(w, h, Pixmap.Format.RGBA8888);
        Gdx.gl.glReadPixels(x, y, w, h, GL20.GL_RGBA, GL20.GL_UNSIGNED_BYTE, pixmap.getPixels());
        if (yDown) {
            // Flip the pixmap upside down
            ByteBuffer pixels = pixmap.getPixels();
            int numBytes = w * h * 4;
            byte[] lines = new byte[numBytes];
            int numBytesPerLine = w * 4;
            for (int i = 0; i < h; i++) {
                pixels.position((h - i - 1) * numBytesPerLine);
                pixels.get(lines, i * numBytesPerLine, numBytesPerLine);
            }
            pixels.clear();
            pixels.put(lines);
        }
    }
}
