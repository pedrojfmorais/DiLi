package pt.isec.gps.dili.model.fsm.concreteStates;

import pt.isec.gps.dili.model.data.DiLi;
import pt.isec.gps.dili.model.fsm.DiliContext;
import pt.isec.gps.dili.model.fsm.DiliState;
import pt.isec.gps.dili.model.fsm.IDiliState;

public abstract class DiliAdapter implements IDiliState {
    DiliContext context;
    DiLi data;

    public DiliAdapter(DiliContext context, DiLi data){
        this.context = context;
        this.data = data;
    }

    void changeState(DiliState state){context.changeState(state.createState(context, data));}
}
