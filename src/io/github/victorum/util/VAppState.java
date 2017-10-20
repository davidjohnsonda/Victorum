package io.github.victorum.util;

import com.jme3.app.state.BaseAppState;

import io.github.victorum.Victorum;

public abstract class VAppState extends BaseAppState{

    public final Victorum getVictorum(){
        return ((Victorum) getApplication());
    }

}
