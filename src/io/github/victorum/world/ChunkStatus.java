package io.github.victorum.world;

public enum ChunkStatus{
    POST_INIT,
    AWAITING_DATA,
    HOLDING_DATA,
    AWAITING_MESH_REFRESH,
    IDLE,
    UNLOADED;
}
