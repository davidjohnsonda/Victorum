package io.github.victorum.block;

public final class TextureCoordinates{
    private static final float TEXTURE_ATLAS_SIZE_IN_TEXTURES_X = 3;
    private static final float TEXTURE_ATLAS_SIZE_IN_TEXTURES_Y = 3;
    private final int texX, texY;

    public TextureCoordinates(int texX, int texY){
        this.texX = texX;
        this.texY = texY;
    }

    public int getTexX(){
        return texX;
    }

    public int getTexY(){
        return texY;
    }

    public float getAtlasStartX(){
        return texX/TEXTURE_ATLAS_SIZE_IN_TEXTURES_X;
    }

    public float getAtlasStartY(){
        return texY/TEXTURE_ATLAS_SIZE_IN_TEXTURES_Y;
    }

    public float getAtlasEndX(){
        return (texX+1)/TEXTURE_ATLAS_SIZE_IN_TEXTURES_X;
    }

    public float getAtlasEndY(){
        return (texY+1)/TEXTURE_ATLAS_SIZE_IN_TEXTURES_Y;
    }

}
