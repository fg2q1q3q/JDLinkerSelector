package chihane.jdaddressselector.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

import chihane.jdaddressselector.AddressProvider;
import chihane.jdaddressselector.AddressSelector;
import chihane.jdaddressselector.BottomDialog;
import chihane.jdaddressselector.OnAddressSelectedListener;
import chihane.jdaddressselector.model.City;
import chihane.jdaddressselector.model.County;
import chihane.jdaddressselector.model.Province;
import chihane.jdaddressselector.model.Street;
import mlxy.utils.T;

public class MainActivity extends AppCompatActivity implements OnAddressSelectedListener {
    List<Province> provinces = new ArrayList<>();
    List<City> citys = new ArrayList<>();
    List<County> counties = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        for (int i = 0; i < 10; i++) {
            Province p = new Province();
            p.id = i + 1;
            p.name = "省份名称" + p.id;
            provinces.add(p);
            for (int j = 0; j < i + 2; j++) {
                City c = new City();
                c.province_id = p.id;
                c.id = i * 3 + j;
                c.name = "市区名称" + c.id;
                citys.add(c);
                if (p.id > 7) {
                    for (int k = 0; k < 3; k++) {
                        County ct = new County();
                        ct.city_id = c.id;
                        ct.id = c.id * 3 + k;
                        ct.name = "区域名称" + ct.id;
                        counties.add(ct);
                    }
                }
            }
        }
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.frameLayout);

        AddressSelector selector = new AddressSelector(this);
        selector.setOnAddressSelectedListener(this);
//        selector.setAddressProvider(new DefaultAddressProvider());
        selector. setAddressProvider(new AddressProvider() {
            @Override
            public void provideProvinces(AddressReceiver<Province> addressReceiver) {
                Log.d("zxl",provinces.size()+"");
                addressReceiver.send(provinces);
            }

            @Override
            public void provideCitiesWith(int provinceId, AddressReceiver<City> addressReceiver) {
                addressReceiver.send(citys);
            }

            @Override
            public void provideCountiesWith(int cityId, AddressReceiver<County> addressReceiver) {
                addressReceiver.send(counties);
            }

            @Override
            public void provideStreetsWith(int countyId, AddressReceiver<Street> addressReceiver) {
                addressReceiver.send(null);
            }
        });
        assert frameLayout != null;
        frameLayout.addView(selector.getView());

        Button buttonBottomDialog = (Button) findViewById(R.id.buttonBottomDialog);
        assert buttonBottomDialog != null;
        buttonBottomDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                BottomDialog.show(MainActivity.this, MainActivity.this);
                BottomDialog dialog = new BottomDialog(MainActivity.this);
                dialog.setOnAddressSelectedListener(MainActivity.this);
                dialog.show();
            }
        });
    }

    @Override
    public void onAddressSelected(Province province, City city, County county, Street street) {
        String s =
                (province == null ? "" : province.name) +
                (city == null ? "" : "\n" + city.name) +
                (county == null ? "" : "\n" + county.name) +
                (street == null ? "" : "\n" + street.name);

        T.showShort(this, s);
    }
}
