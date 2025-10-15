package model;

public enum TipoOperacaoEnum {
    GANHA_GANHA("GAN"),
    TRES_POR_UM("3x1"),
    DIVIDENDO3X("DIV"),
    ESTRATEGICA("EST");

    private final String dbValue;

    TipoOperacaoEnum(String dbValue) {
        this.dbValue = dbValue;
    }

    public String getDbValue() {
        return dbValue;
    }
}
