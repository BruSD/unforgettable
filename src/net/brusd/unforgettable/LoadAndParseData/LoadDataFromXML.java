package net.brusd.unforgettable.LoadAndParseData;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;


import net.brusd.unforgettable.AppDatabase.AppDB;
import net.brusd.unforgettable.GlobalPackeg.SharedPreferencesSticker;
import net.brusd.unforgettable.ActivityPackeg.MainActivity;
import net.brusd.unforgettable.R;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


import java.io.IOException;
import java.io.InputStream;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by BruSD on 04.01.14.
 */
public class LoadDataFromXML {
    private static AppDB appDB = null;

    public static void LoadDataFromXML(Activity activity){
        new LoadDataFromXMLAsyncTask(activity).execute();
    }

    private static void addThemeToDB(NodeList nodeListTheme){
        for (int i = 0; i < nodeListTheme.getLength(); i++) {
            Element entry = (Element) nodeListTheme.item(i);
            Element _themeName = (Element) entry.getElementsByTagName("theme-name").item(0);
            Element _themeDescription = (Element) entry.getElementsByTagName("theme-description").item(0);
            int curentlyTemeID = appDB.addThemeGetThemeId(_themeName.getTextContent(), _themeDescription.getTextContent());
            NodeList nodeListQuote =    entry.getElementsByTagName("quote");

            if (nodeListQuote.getLength() > 0){
                addQuoteToDB(nodeListQuote, curentlyTemeID);
            }
            appDB.close();

        }
    }
    private static void addQuoteToDB(NodeList nodeListQuote, int themeID){
        for (int i = 0; i < nodeListQuote.getLength(); i++) {
            Element quote = (Element) nodeListQuote.item(i);
            Element _quote = (Element) quote.getElementsByTagName("quote-text").item(0);
            Element _quoteSorce = (Element) quote.getElementsByTagName("quote-sorce").item(0);

            appDB.addNewQuote(_quote.getTextContent(), _quoteSorce.getTextContent(), themeID);
        }
    }

    private  static  class LoadDataFromXMLAsyncTask extends AsyncTask{
        private Activity activity;
        private ProgressDialog dialog;

        public  LoadDataFromXMLAsyncTask(Activity _activity){
            this.activity = _activity;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(activity);
            dialog.setCancelable(false);
            dialog.setMessage(activity.getResources().getString(R.string.load_data_string));
            dialog.show();
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            InputStream is = null;
            try {
                is = activity.getAssets().open("quote.xml");
            } catch (IOException e) {
                e.printStackTrace();
            }
            //File fXmlFile = activity.getBaseContext().getResources.getXml("res/xml/data.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = null;

            try {
                dBuilder = dbFactory.newDocumentBuilder();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            }
            Document doc = null;
            try {
                doc = dBuilder.parse(is);
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Element element = doc.getDocumentElement();

            NodeList nodeListTheme = element.getElementsByTagName("categori");

            if (nodeListTheme.getLength() > 0){
                appDB = AppDB.getInstance(activity);
                addThemeToDB(nodeListTheme);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            if(dialog!=null && dialog.isShowing())
                dialog.dismiss();

            SharedPreferencesSticker.setDataLoadFromXMLTrue(activity);
            if (activity != null && activity instanceof MainActivity){
                ((MainActivity)activity).commitQuoteFragment();
            }

        }
    }

}
