package auth.plugin.mc.mapper;

public interface Mapper<F, T> {

    T map(F object);
}
