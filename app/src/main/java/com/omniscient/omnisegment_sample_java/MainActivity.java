package com.omniscient.omnisegment_sample_java;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.omniscient.omnisegment_java.OmniAnalytics;
import com.omniscient.omnisegment_java.tools.beacon.CompleteRegistration;
import com.omniscient.omnisegment_java.tools.beacon.Impression;
import com.omniscient.omnisegment_java.tools.beacon.Product;
import com.omniscient.omnisegment_java.tools.beacon.Promotion;
import com.omniscient.omnisegment_sample_java.tools.adapter.RecycleAdapter;
import com.omniscient.omnisegment_sample_java.tools.model.Item;
import com.omniscient.omnisegment_sample_java.tools.model.OmniSearchKeyword;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity{

    Spinner spCheckout;
    RecyclerView recycleView;
    String TAG = this.getClass().getName();

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        OmniAnalytics.getInstance(this).processIntent(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initRecycleView();
        initSpinner();
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }
                        // Get new Instance ID token
                        String token = task.getResult().getToken();
                        Log.d(TAG, "onComplete: "+token);
                        Bundle bundle = new Bundle();
                        bundle.putString(OmniAnalytics.PayloadType.EVENT_ACTION, OmniAnalytics.EventActionType.RegisterToken);
                        bundle.putString(OmniAnalytics.PayloadType.EVENT_CATEGORY, OmniAnalytics.EventCategoryType.PushNotification);
                        bundle.putString(OmniAnalytics.EventActionType.RegisterToken, token);
                        OmniAnalytics.getInstance(MainActivity.this).logEvent(OmniAnalytics.HitType.EVENT, bundle);
                    }
                });
    }

    private void initRecycleView(){
        recycleView = findViewById(R.id.recycleView);
        recycleView.setLayoutManager(new LinearLayoutManager(this));    //线性布局

        ArrayList<Item> itemList = new ArrayList<>();
        itemList.add(new Item("Login") {
            @Override
            public void collect() {
                OmniAnalytics.setUserId("eason");
            }
        });

        itemList.add(new Item("Logout") {
            @Override
            public void collect() {
                OmniAnalytics.setUserId("");
            }
        });

        itemList.add(new Item("Complete Registration") {
            @Override
            public void collect() {
                CompleteRegistration completeRegistration = new CompleteRegistration();
                completeRegistration.setBirthdayYear("1994");
                completeRegistration.setGender(OmniAnalytics.GenderType.MALE);
                completeRegistration.setCity("Taipei");
                completeRegistration.setRegType("regType");

                Bundle bundle = new Bundle();
                bundle.putString(OmniAnalytics.PayloadType.EVENT_CATEGORY, OmniAnalytics.EventCategoryType.COMPLETE_REGISTRATION);
                bundle.putString(OmniAnalytics.PayloadType.EVENT_ACTION, OmniAnalytics.EventActionType.COMPLETE_REGISTRATION);
                bundle.putSerializable(OmniAnalytics.EventActionType.COMPLETE_REGISTRATION, completeRegistration);
                OmniAnalytics.getInstance(MainActivity.this).logEvent(OmniAnalytics.HitType.EVENT, bundle);
            }
        });

        itemList.add(new Item("Content Group") {
            @Override
            public void collect() {
                Bundle bundle = new Bundle();
                bundle.putString(OmniAnalytics.PayloadType.CONTENT_GROUP, "Home");
                OmniAnalytics.getInstance(MainActivity.this).logEvent(OmniAnalytics.HitType.EVENT, bundle);
            }
        });

        itemList.add(new Item("Send Action Beacon(Search from Gson)") {
            @Override
            public void collect() {
                OmniSearchKeyword keywordBean = new OmniSearchKeyword("美白");
                Gson gson=  new GsonBuilder().create();
                String jsonString = gson.toJson(keywordBean);

                Bundle bundle = new Bundle();
                bundle.putString(OmniAnalytics.PayloadType.EVENT_LABEL, jsonString);
                bundle.putString(OmniAnalytics.PayloadType.EVENT_ACTION, "search");
                bundle.putString(OmniAnalytics.PayloadType.EVENT_CATEGORY, "search");
                OmniAnalytics.getInstance(MainActivity.this).logEvent(OmniAnalytics.HitType.EVENT, bundle);
            }
        });

        itemList.add(new Item("Send Action Beacon(Search)") {
            @Override
            public void collect() {
                Bundle bundle = new Bundle();
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("search_string", "美白");
                    bundle.putString(OmniAnalytics.PayloadType.EVENT_LABEL, jsonObject.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                bundle.putString(OmniAnalytics.PayloadType.EVENT_ACTION, "search");
                bundle.putString(OmniAnalytics.PayloadType.EVENT_CATEGORY, "search");
                OmniAnalytics.getInstance(MainActivity.this).logEvent(OmniAnalytics.HitType.EVENT, bundle);
            }
        });

        itemList.add(new Item("Send Action Beacon(json string for Search)") {
            @Override
            public void collect() {
                Bundle bundle = new Bundle();

                String jsonString = "{\"search_string\":\"美白\"}";
                bundle.putString(OmniAnalytics.PayloadType.EVENT_LABEL, jsonString);

                bundle.putString(OmniAnalytics.PayloadType.EVENT_ACTION, "search");
                bundle.putString(OmniAnalytics.PayloadType.EVENT_CATEGORY, "search");
                OmniAnalytics.getInstance(MainActivity.this).logEvent(OmniAnalytics.HitType.EVENT, bundle);
            }
        });

        itemList.add(new Item("Send Action Beacon") {
            @Override
            public void collect() {
                Bundle bundle = new Bundle();
                try {
                    JSONObject jsonObject = new JSONObject();
                    JSONObject inner_json = new JSONObject();
                    inner_json.put("inner", "jsonobject");
                    jsonObject.put("xxx", inner_json);
                    bundle.putString(OmniAnalytics.PayloadType.EVENT_LABEL, jsonObject.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                bundle.putString(OmniAnalytics.PayloadType.EVENT_ACTION, OmniAnalytics.EventActionType.CLICK_MENU);
                bundle.putString(OmniAnalytics.PayloadType.EVENT_CATEGORY, "TestCategory");
                bundle.putInt(OmniAnalytics.PayloadType.EVENT_VALUE, 123);
                OmniAnalytics.getInstance(MainActivity.this).logEvent(OmniAnalytics.HitType.EVENT, bundle);
            }
        });

        itemList.add(new Item("Product Impressions") {
            @Override
            public void collect() {
                Impression impression1 = new Impression("12345","Triblend Android T-Shirt");
                impression1.setPrice("15.25");
                impression1.setBrand("Google");
                impression1.setCategory("Apparel");
                impression1.setVariant("Gray");
                impression1.setList("Search Results");
                impression1.setPosition("1");

                Impression impression2 = new Impression("123455","Same list T-Shirt");
                impression2.setPrice("15.25");
                impression2.setBrand("Google");
                impression2.setCategory("Apparel");
                impression2.setVariant("Gray");
                impression2.setList("Search Results");
                impression2.setPosition("2");

                Impression impression3 = new Impression("67890","Donut Friday Scented T-Shirt");
                impression3.setPrice("33.75");
                impression3.setBrand("Google");
                impression3.setCategory("Apparel");
                impression3.setVariant("Black");
                impression3.setPosition("1");

                ArrayList<Impression> arrayList = new ArrayList<>(Arrays.asList(impression1, impression2, impression3));

                Bundle bundle = new Bundle();
                bundle.putString(OmniAnalytics.PayloadType.CONTENT_GROUP, "default category list");
                bundle.putString(OmniAnalytics.PayloadType.CURRENCY_CODE, "TWD");
                bundle.putSerializable(OmniAnalytics.DataType.IMPRESSIONS, arrayList);
                OmniAnalytics.getInstance(MainActivity.this).logEvent(OmniAnalytics.HitType.PAGEVIEW, bundle);
            }
        });

        itemList.add(new Item("Product Click") {
            @Override
            public void collect() {
                Product product1 = new Product("product1_id", "product1_name", "10.10", 1);
                product1.setBrand("Omni_Brand");
                product1.setCategory("Omni_Category");
                product1.setVariant("Omni_Variant");
                product1.setCoupon("Omni_Coupon");

                ArrayList<Product> arrayList = new ArrayList<>(Arrays.asList(product1));

                Bundle bundle = new Bundle();
                bundle.putString(OmniAnalytics.PayloadType.PRODUCT_ACTION_LIST, "Search Results");
                bundle.putString(OmniAnalytics.PayloadType.CURRENCY_CODE, "TWD");
                bundle.putString(OmniAnalytics.PayloadType.EVENT_ACTION, OmniAnalytics.EventActionType.CLICK_PRODUCT);
                bundle.putSerializable(OmniAnalytics.DataType.PRODUCTS, arrayList);
                OmniAnalytics.getInstance(MainActivity.this).logEvent(OmniAnalytics.HitType.EVENT, bundle);
            }
        });

        itemList.add(new Item("Product Detail Impressions") {
            @Override
            public void collect() {
                Product product1 = new Product("product1_id", "product1_name", "10.10", 1);
                product1.setBrand("Omni_Brand");
                product1.setCategory("Omni_Category");
                product1.setVariant("Omni_Variant");
                product1.setCoupon("Omni_Coupon");

                ArrayList<Product> arrayList = new ArrayList<>(Arrays.asList(product1));

                Bundle bundle = new Bundle();
                bundle.putString(OmniAnalytics.PayloadType.PRODUCT_ACTION_LIST, "category list");
                bundle.putString(OmniAnalytics.PayloadType.CURRENCY_CODE, "TWD");
                bundle.putSerializable(OmniAnalytics.PayloadType.PRODUCT_ACTION, OmniAnalytics.ActionType.DETAIL);
                bundle.putSerializable(OmniAnalytics.DataType.PRODUCTS, arrayList);
                OmniAnalytics.getInstance(MainActivity.this).logEvent(OmniAnalytics.HitType.PAGEVIEW, bundle);
            }
        });

        itemList.add(new Item("Add To Cart") {
            @Override
            public void collect() {
                Product product1 = new Product("product1_id", "product1_name", "10.10", 1);
                product1.setBrand("Omni_Brand");
                product1.setCategory("Omni_Category");
                product1.setVariant("Omni_Variant");
                product1.setCoupon("Omni_Coupon");

                ArrayList<Product> arrayList = new ArrayList<>(Arrays.asList(product1));

                Bundle bundle = new Bundle();
                bundle.putString(OmniAnalytics.PayloadType.CURRENCY_CODE, "TWD");
                bundle.putString(OmniAnalytics.PayloadType.EVENT_ACTION, OmniAnalytics.EventActionType.ADD_TO_CART);
                bundle.putSerializable(OmniAnalytics.DataType.PRODUCTS, arrayList);
                OmniAnalytics.getInstance(MainActivity.this).logEvent(OmniAnalytics.HitType.EVENT, bundle);
            }
        });

        itemList.add(new Item("Remove From Cart") {
            @Override
            public void collect() {
                Product product1 = new Product("product1_id", "product1_name", "10.10", 1);
                product1.setBrand("Omni_Brand");
                product1.setCategory("Omni_Category");
                product1.setVariant("Omni_Variant");
                product1.setCoupon("Omni_Coupon");

                ArrayList<Product> arrayList = new ArrayList<>(Arrays.asList(product1));

                Bundle bundle = new Bundle();
                bundle.putString(OmniAnalytics.PayloadType.CURRENCY_CODE, "TWD");
                bundle.putString(OmniAnalytics.PayloadType.EVENT_ACTION, OmniAnalytics.EventActionType.REMOVE_FROM_CART);
                bundle.putSerializable(OmniAnalytics.DataType.PRODUCTS, arrayList);
                OmniAnalytics.getInstance(MainActivity.this).logEvent(OmniAnalytics.HitType.EVENT, bundle);
            }
        });

        itemList.add(new Item("Promotion Impressions") {
            @Override
            public void collect() {
                Promotion promotion1 = new Promotion("JUNE_PROMO13","June Sale");
                promotion1.setCreative("banner1");
                promotion1.setPosition("slot1");

                Promotion promotion2 = new Promotion("FREE_SHIP13","Free Shipping Promo");
                promotion2.setCreative("skyscraper1");
                promotion2.setPosition("slot2");

                ArrayList<Promotion> arrayList = new ArrayList<>(Arrays.asList(promotion1, promotion2));

                Bundle bundle = new Bundle();
                bundle.putSerializable(OmniAnalytics.DataType.PROMOTIONS, arrayList);
                OmniAnalytics.getInstance(MainActivity.this).logEvent(OmniAnalytics.HitType.PAGEVIEW, bundle);
            }
        });

        itemList.add(new Item("Promotion Clicks") {
            @Override
            public void collect() {
                Promotion promotion1 = new Promotion("JUNE_PROMO13","June Sale");
                promotion1.setCreative("banner1");
                promotion1.setPosition("slot1");

                ArrayList<Promotion> arrayList = new ArrayList<>(Arrays.asList(promotion1));

                Bundle bundle = new Bundle();
                bundle.putString(OmniAnalytics.PayloadType.EVENT_ACTION, OmniAnalytics.EventActionType.CLICK_PROMOTION);
                bundle.putSerializable(OmniAnalytics.DataType.PROMOTIONS, arrayList);
                OmniAnalytics.getInstance(MainActivity.this).logEvent(OmniAnalytics.HitType.PAGEVIEW, bundle);
            }
        });

        itemList.add(new Item("Purchases") {
            @Override
            public void collect() {
                Product product1 = new Product("product1_id", "product1_name", "10.10", 1);
                product1.setBrand("Omni_Brand");
                product1.setCategory("Omni_Category");
                product1.setVariant("Omni_Variant");
                product1.setCoupon("Omni_Coupon");
                Product product2 = new Product("product2_id", "product2_name", "12.12", 2);
                Product product3 = new Product("product3_id", "product3_name", "15.15", 3);

                ArrayList<Product> arrayList = new ArrayList<>(Arrays.asList(product1, product2, product3));

                Bundle bundle = new Bundle();
                bundle.putString(OmniAnalytics.PayloadType.EVENT_ACTION, OmniAnalytics.EventActionType.PURCHASE);
                bundle.putFloat(OmniAnalytics.PayloadType.REVENUE, 20.20f);
                bundle.putSerializable(OmniAnalytics.DataType.PRODUCTS, arrayList);
                OmniAnalytics.getInstance(MainActivity.this).logEvent(OmniAnalytics.HitType.EVENT, bundle);
            }
        });

        itemList.add(new Item("Full Refunds") {
            @Override
            public void collect() {
                Bundle bundle = new Bundle();
                bundle.putString(OmniAnalytics.PayloadType.EVENT_ACTION, OmniAnalytics.EventActionType.REFUND);
                bundle.putString(OmniAnalytics.PayloadType.TRANSACTION_ID, "ABC");
                bundle.putString(OmniAnalytics.PayloadType.PRODUCT_ACTION, "custom_product_action");
                OmniAnalytics.getInstance(MainActivity.this).logEvent(OmniAnalytics.HitType.EVENT, bundle);
            }
        });

        itemList.add(new Item("Partial Refunds") {
            @Override
            public void collect() {
                Product product1 = new Product("product1_id", "product1_name", "10.10", 1);
                product1.setBrand("Omni_Brand");
                product1.setCategory("Omni_Category");
                product1.setVariant("Omni_Variant");
                product1.setCoupon("Omni_Coupon");
                Product product2 = new Product("product2_id", "product2_name", "12.12", 2);
                Product product3 = new Product("product3_id", "product3_name", "15.15", 3);

                ArrayList<Product> arrayList = new ArrayList<>(Arrays.asList(product1, product2, product3));

                Bundle bundle = new Bundle();
                bundle.putString(OmniAnalytics.PayloadType.EVENT_ACTION, OmniAnalytics.EventActionType.REFUND);
                bundle.putString(OmniAnalytics.PayloadType.TRANSACTION_ID, "ABC");
                bundle.putString(OmniAnalytics.PayloadType.PRODUCT_ACTION, "custom_product_action");
                bundle.putSerializable(OmniAnalytics.DataType.PRODUCTS, arrayList);
                OmniAnalytics.getInstance(MainActivity.this).logEvent(OmniAnalytics.HitType.EVENT, bundle);
            }
        });
        recycleView.setAdapter(new RecycleAdapter(this, itemList));
    }

    private void initSpinner(){
        spCheckout = findViewById(R.id.spCheckout);
        final String[] checkoutArray = new String[]{"Choose One", "ATM", "Credit Card"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, checkoutArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCheckout.setAdapter(adapter);
        spCheckout.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putString(OmniAnalytics.PayloadType.EVENT_ACTION, OmniAnalytics.EventActionType.CLICK_CHECKOUT);
                bundle.putString(OmniAnalytics.PayloadType.PRODUCT_ACTION, "custom_product_action");
                bundle.putInt(OmniAnalytics.PayloadType.CHECKOUT_STEP, position);
                bundle.putString(OmniAnalytics.PayloadType.CHECKOUT_STEP_OPTION, checkoutArray[position]);
                OmniAnalytics.getInstance(MainActivity.this).logEvent(OmniAnalytics.HitType.EVENT, bundle);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        OmniAnalytics.getInstance(this).setScreenName("MainActivityScreen", "MainActivityScreenClass");
    }

    public void nextClick(View view) {
        startActivity(new Intent(this,NextActivity.class));
    }
}
