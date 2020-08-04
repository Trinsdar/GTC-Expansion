package gtc_expansion.util;

import ic2.core.fluid.IC2Tank;
import ic2.core.util.obj.ITankListener;

import java.util.ArrayList;
import java.util.List;

public class GTCXTank extends IC2Tank {
    String debug = "";
    List<ITankListener> listeners = new ArrayList<>();
    public GTCXTank(int capacity) {
        super(capacity);
    }

    public GTCXTank setDebug(String debug) {
        this.debug = debug;
        return this;
    }

    @Override
    public void addListener(ITankListener list) {
        if (!listeners.contains(list)) listeners.add(list);
    }

    @Override
    protected void onContentsChanged() {
        for (ITankListener list : listeners){
            list.onTankChanged(this);
        }
    }

    public void removeListener(ITankListener list){
        listeners.remove(list);
    }

    @Override
    public String toString() {
        return "GTCXTank{" +
                "debug='" + debug + '\'' +
                ", listeners=" + listeners +
                '}';
    }
}
