package com.uwsoft.editor.renderer.components.shader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Array;

/**
 * {@link com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader} for {@link ShaderProgram} instances. The shader program data is loaded asynchronously. The shader
 * is then created on the rendering thread, synchronously.
 *
 * @author Szymon "Veldrin" Jab?o?ski
 */
public class ShaderLoader extends AsynchronousAssetLoader<ShaderProgram, ShaderParameter> {
    private final static String TAG = "ShaderLoader";

    private String vertProgram;
    private String fragProgram;

    public ShaderLoader(FileHandleResolver resolver) {
        super(resolver);
    }

    @Override
    public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, ShaderParameter parameter) {
        return null;
    }

    @Override
    public void loadAsync(AssetManager manager, String fileName, FileHandle file, ShaderParameter parameter) {
        String vertexPath = fileName.substring(0, fileName.indexOf("+"));
        String fragmentPath = fileName.substring(fileName.indexOf("+") + 1, fileName.length());

        FileHandle vertexFile = Gdx.files.internal(vertexPath);
        FileHandle fragmentFile = Gdx.files.internal(fragmentPath);

        if (vertexFile.exists() && fragmentFile.exists()) {
            vertProgram = vertexFile.readString();
            fragProgram = fragmentFile.readString();
        }
    }

    @Override
    public ShaderProgram loadSync(AssetManager manager, String fileName, FileHandle file, ShaderParameter parameter) {
        ShaderProgram.pedantic = true;
        String vertexProgram = vertProgram;
        String fragmentProgram = prependPrecision(fragProgram);
        ShaderProgram shader = new ShaderProgram(vertexProgram, fragmentProgram);
        if (!shader.isCompiled()) {
            Gdx.app.error(TAG, "Error during shader compilation: " + shader.getLog());
            Gdx.app.error(TAG, "VertexShader used:");
            Gdx.app.error(TAG, vertexProgram);
            Gdx.app.error(TAG, "FragmentShader used:");
            Gdx.app.error(TAG, fragmentProgram);
        } else {
            Gdx.app.log(TAG, "Shader '" + fileName + "' loaded successfully");
        }

        return shader;
    }

    /**
     * Ads precision definitions for the fragment program
     *
     * @param program to add the definition to
     * @return the combined String
     */
    protected String prependPrecision(String program) {
        StringBuilder builder = new StringBuilder();
        builder.append("#ifdef GL_ES\n")
                .append("#define LOWP lowp\n")
                .append("precision mediump float;\n")
                .append("#else\n")
                .append("#define LOWP \n")
                .append("#endif\n")
                .append(program);
        return builder.toString();
    }
}
