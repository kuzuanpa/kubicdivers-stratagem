package cn.kuzuanpa.kubicdivers.stratagem.common.stratagem.types;

import cn.kuzuanpa.kubicdivers.stratagem.common.stratagem.IStratagem;

import java.util.List;
/**A special case of null, just for something that don't accept null arguments**/
public class EmptyStratagem implements IStratagem {
    @Override
    public String getId() {
        return "e";
    }

    @Override
    public String getName() {
        return "";
    }

    @Override
    public List<Direction> getSequence() {
        return List.of();
    }

    @Override
    public int getCooldown() {
        return Integer.MAX_VALUE;
    }

    @Override
    public StratagemType getType() {
        return StratagemType.STRIKE;
    }
}
