package musta.belmo.plugins.camelcase.ast;

public interface Transformer {
    void transformPsi();

    String getActionName();

    boolean isApplied();
}
