package io.github.victorum.block;

/*
Key to the textures (Coords listed as x,y)
------------------------------------------
(1,1) Grass block top
(1,2) Gravel block side
(1,3) Log block side
(1,4) Water block side
(1,5) Red phosphorus block side
(2,1) Grass block side
(2,2) Granite block side
(2,3) Log block top
(2,4) Sand block side
(2,5) Pitchblende block side
(3,1) Dirt block side
(3,2) Stone block side
(3,3) Leaf block side
(3,4) Basalt block side
*/

public final class TextureCoordinates{
    private static final float TEXTURE_ATLAS_SIZE_IN_TEXTURES_X = 3;
    private static final float TEXTURE_ATLAS_SIZE_IN_TEXTURES_Y = 5;
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
