package dk.au.mad22spring.janesbuns.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


// Generated with JSONschema2pojo
public class Currency {

    @SerializedName("currencyName")
    @Expose
    private String currencyName;
    @SerializedName("currencySymbol")
    @Expose
    private String currencySymbol;
    @SerializedName("id")
    @Expose
    private String id;

    public List<Currency> currencies;

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public String getCurrencySymbol() {
        return currencySymbol;
    }

    public void setCurrencySymbol(String currencySymbol) {
        this.currencySymbol = currencySymbol;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}

