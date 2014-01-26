package net.brusd.unforgettable.GlobalPackeg;

/**
 * Created by BruSD on 23.01.14.
 */
public class Quote {
    private int quoteID;
    private String quote;
    private String quoteSource;
    private int quoteThemeID;
    private String themeQuoteName;
    private boolean isQuoteFavorite = false;


    public Quote(int _quoteID, String _quote, String _quoteSource, int _quoteThemeID){
        this.quoteID = _quoteID;
        this.quote = _quote;
        this.quoteSource = _quoteSource;
        this.quoteThemeID = _quoteThemeID;
    }

    public int getQuoteID (){
        return  quoteID;
    }
    public String getQuote(){
        return quote;
    }
    public String getQuoteSource(){
        return quoteSource;
    }
    public int getQuoteThemeID(){
        return quoteThemeID;
    }

    public void setThemeQuoteName(String _themeQuoteName){
        this.themeQuoteName = _themeQuoteName;
    }

    public String getThemeQuoteName(){
        return themeQuoteName;
    }

    public void setQuoteIsFavorite(boolean _isQuoteFavorite){
        this.isQuoteFavorite = _isQuoteFavorite;
    }

    public boolean isFavorite(){
        return isQuoteFavorite;
    }
}
