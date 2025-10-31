package model;

public enum TipoOperacaoEnum {
	DIVIDENDO3X("DIV"),
    GANHA_GANHA("GAN"),
    TRES_PRA_UM("3x1"),
    ESTRATEGICA("EST");

    private final String dbValue;

    TipoOperacaoEnum(String dbValue) {
        this.dbValue = dbValue;
    }

    public String getDbValue() {
        return dbValue;
    }
}
