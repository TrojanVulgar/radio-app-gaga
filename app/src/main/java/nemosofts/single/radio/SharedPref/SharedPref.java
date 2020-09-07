package nemosofts.single.radio.SharedPref;

import android.content.Context;
import android.content.SharedPreferences;

import nemosofts.single.radio.Receiver.ItemNemosofts;
import nemosofts.single.radio.Receiver.Setting;


public class SharedPref {

    private Context context;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public SharedPref(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences("setting", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }


    public void setIsFirstPurchaseCode(Boolean flag) {
        editor.putBoolean("firstopenpurchasecode", flag);
        editor.apply();
    }

    public Boolean getIsFirstPurchaseCode() {
        return sharedPreferences.getBoolean("firstopenpurchasecode", true);
    }

    public void setPurchaseCode(ItemNemosofts itemAbout) {
        editor.putString("purchase_code", itemAbout.getPurchase_code());
        editor.putString("product_name", itemAbout.getProduct_name());
        editor.putString("purchase_date", itemAbout.getPurchase_date());
        editor.putString("buyer_name", itemAbout.getBuyer_name());
        editor.putString("license_type", itemAbout.getLicense_type());
        editor.putString("nemosofts_key", itemAbout.getNemosofts_key());
        editor.putString("package_name", itemAbout.getPackage_name());
        editor.apply();
    }

    public void getPurchaseCode() {
        Setting.itemAbout = new ItemNemosofts(
                sharedPreferences.getString("purchase_code",""),
                sharedPreferences.getString("product_name",""),
                sharedPreferences.getString("purchase_date",""),
                sharedPreferences.getString("buyer_name",""),
                sharedPreferences.getString("license_type",""),
                sharedPreferences.getString("nemosofts_key",""),
                sharedPreferences.getString("package_name","")
        );
    }



}
