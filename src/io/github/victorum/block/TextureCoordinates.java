package io.github.victorum.block;

public final class TextureCoordinates {
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

}
