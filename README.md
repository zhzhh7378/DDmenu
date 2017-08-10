# DDmenu
[![](https://jitpack.io/v/zhzhh7378/DDmenu.svg)](https://jitpack.io/#zhzhh7378/DDmenu)
## 1、功能描述

> 如果觉得对你有用的话，点一下右上的星星赞一下吧!

## 2、 效果 

<p align="center">
  <img src="https://github.com/zhzhh7378/DDmenu/blob/master/device-2017-08-10-151737.png">
</p>
<p align="center">
  <img src="https://github.com/zhzhh7378/DDmenu/blob/master/device-2017-08-10-151808.png">
</p>

## 3、如何使用
1.依赖设置：

	dependencies {
	        compile 'com.github.zhzhh7378:DDmenu:1.0'
	}
	
2，在布局中引用

   
   
       <?xml version="1.0" encoding="utf-8"?>
          <RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
              xmlns:app="http://schemas.android.com/apk/res-auto"
              xmlns:tools="http://schemas.android.com/tools"
              android:layout_width="match_parent"
              android:layout_height="match_parent"
              tools:context="cc.crm.ddmenu.MainActivity">
       <cc.crm.dropdownmenu.DropDownMenu
           android:id="@+id/my_menu"
           android:layout_centerHorizontal="true"
           android:layout_marginTop="50dp"
           android:layout_width="wrap_content"
           app:ddnenu_item_select_color="@color/colorAccent"
           app:ddnenu_item_defaut_color="@color/ddmenu_text_color"
           android:layout_height="wrap_content" >
           <TextView
               android:layout_width="match_parent"
               android:layout_height="wrap_content"
               android:layout_margin="3dp"
               android:background="@drawable/item_bg"
               android:paddingLeft="8dp"
               android:paddingRight="8dp"
               android:paddingTop="5dp"
               android:paddingBottom="5dp"
               android:textSize="12sp"
               android:text="one" />
   
   
           <TextView
               android:layout_width="match_parent"
               android:layout_height="wrap_content"
               android:layout_margin="3dp"
               android:textSize="12sp"
               android:background="@drawable/item_bg"
               android:paddingLeft="8dp"
               android:paddingRight="8dp"
               android:paddingTop="5dp"
               android:paddingBottom="5dp"
               android:text="two" />
   
   
           <TextView
               android:layout_width="match_parent"
               android:layout_height="wrap_content"
               android:layout_margin="3dp"
               android:background="@drawable/item_bg"
               android:paddingLeft="8dp"
               android:paddingRight="8dp"
               android:paddingTop="5dp"
               android:paddingBottom="5dp"
               android:textSize="12sp"
               android:text="three" />
   
       </cc.crm.dropdownmenu.DropDownMenu>
        </RelativeLayout>
3,在代码中调用：
         
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


