package cc.crm.ddmenu;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import cc.crm.dropdownmenu.DropDownMenu;

public class MainActivity extends AppCompatActivity {
    DropDownMenu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        menu=findViewById(R.id.my_menu);
        menu.setItemClickListener(new DropDownMenu.MyItemClickListener<String>() {
            @Override
            public void onItemViewClick(View v, String s, int position, int type) {
                Toast.makeText(MainActivity.this,s+"",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
