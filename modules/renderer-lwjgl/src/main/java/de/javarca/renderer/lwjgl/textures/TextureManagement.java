package de.javarca.renderer.lwjgl.textures;

import static java.util.Objects.requireNonNull;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.stb.STBImage;
import org.lwjgl.stb.STBImageWrite;
import org.lwjgl.stb.STBTTFontinfo;
import org.lwjgl.stb.STBTruetype;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

public class TextureManagement {
    public static int loadTexture(byte[] imageData) {
        int textureId;

        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer width = stack.mallocInt(1);
            IntBuffer height = stack.mallocInt(1);
            IntBuffer channels = stack.mallocInt(1);

            ByteBuffer imageBuffer = ByteBuffer.allocateDirect(imageData.length).put(imageData);
            imageBuffer.flip();

            ByteBuffer decodedImage = STBImage.stbi_load_from_memory(imageBuffer, width, height, channels, 4);
            if (decodedImage == null) {
                throw new RuntimeException("Failed to load image: " + STBImage.stbi_failure_reason());
            }
            textureId = loadTextureFromBuffer(decodedImage, width.get(0), width.get(0));
        }

        return textureId;
    }

    public static int renderCharacter(char character) {
        byte[] font;
        try (var stream = TextureManagement.class.getResourceAsStream("font.ttf")) {
            font = requireNonNull(stream).readAllBytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try (MemoryStack stack = MemoryStack.stackPush()) {
            ByteBuffer fontBuffer = MemoryUtil.memAlloc(font.length);
            fontBuffer.put(font).flip();

            STBTTFontinfo fontInfo = STBTTFontinfo.create();
            if (!STBTruetype.stbtt_InitFont(fontInfo, fontBuffer)) {
                MemoryUtil.memFree(fontBuffer);
                throw new RuntimeException("Failed to initialize font.");
            }

            int fontSize = 120;
            float scale = STBTruetype.stbtt_ScaleForPixelHeight(fontInfo, fontSize);

            // Get font metrics
            IntBuffer ascent = stack.mallocInt(1);
            IntBuffer descent = stack.mallocInt(1);
            IntBuffer lineGap = stack.mallocInt(1);
            STBTruetype.stbtt_GetFontVMetrics(fontInfo, ascent, descent, lineGap);

            // Get character bitmap
            IntBuffer widthBuffer = stack.mallocInt(1);
            IntBuffer heightBuffer = stack.mallocInt(1);
            IntBuffer xOffset = stack.mallocInt(1);
            IntBuffer yOffset = stack.mallocInt(1);

            ByteBuffer bitmap = STBTruetype.stbtt_GetCodepointBitmap(
                    fontInfo, scale, scale, character, widthBuffer, heightBuffer, xOffset, yOffset);

            if (bitmap == null) {
                MemoryUtil.memFree(fontBuffer);
                throw new RuntimeException("Failed to generate character bitmap.");
            }

            int charWidth = widthBuffer.get(0);
            int charHeight = heightBuffer.get(0);

            // Convert grayscale bitmap to RGBA
            ByteBuffer rgbaBuffer = BufferUtils.createByteBuffer(charWidth * charHeight * 4);
            for (int i = 0; i < charWidth * charHeight; i++) {
                byte gray = bitmap.get(i);
                rgbaBuffer.put((byte) 0); // Red
                rgbaBuffer.put((byte) 0); // Green
                rgbaBuffer.put((byte) 0); // Blue
                rgbaBuffer.put(gray); // Alpha
            }
            rgbaBuffer.flip();

            return loadTextureFromBuffer(rgbaBuffer, widthBuffer.get(0), heightBuffer.get(0));
        }
    }

    private static int loadTextureFromBuffer(ByteBuffer imageBuffer, int width, int height) {
        int textureId = GL11.glGenTextures();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);
        GL11.glTexImage2D(
                GL11.GL_TEXTURE_2D,
                0,
                GL11.GL_RGBA,
                width,
                height,
                0,
                GL11.GL_RGBA,
                GL11.GL_UNSIGNED_BYTE,
                imageBuffer);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);

        STBImage.stbi_image_free(imageBuffer);
        return textureId;
    }

    public static void renderTexture(int textureId, float x, float y, float width, float height) {
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);

        GL11.glBegin(GL11.GL_QUADS);
        GL11.glTexCoord2f(0, 0);
        GL11.glVertex2f(x, y);
        GL11.glTexCoord2f(1, 0);
        GL11.glVertex2f(x + width, y);
        GL11.glTexCoord2f(1, 1);
        GL11.glVertex2f(x + width, y + height);
        GL11.glTexCoord2f(0, 1);
        GL11.glVertex2f(x, y + height);
        GL11.glEnd();
    }

    public static void saveScreenshot(String folderPath, int width, int height) {
        if (folderPath == null) {
            return;
        }

        ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * 4);
        GL11.glFinish();
        GL11.glReadPixels(0, 0, width, height, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);
        ByteBuffer flippedBuffer = BufferUtils.createByteBuffer(width * height * 4);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width * 4; x++) {
                flippedBuffer.put((height - y - 1) * width * 4 + x, buffer.get(y * width * 4 + x));
            }
        }

        File folder = new File(folderPath);
        //noinspection ResultOfMethodCallIgnored
        folder.mkdirs();
        STBImageWrite.stbi_write_png(
                new File(folder, "screen.png").getAbsolutePath(), width, height, 4, flippedBuffer, width * 4);
    }
}
