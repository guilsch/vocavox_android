//package com.guilsch.multivoc;
//
//import android.content.Context;
//import android.graphics.Color;
//import android.graphics.drawable.ColorDrawable;
//
//import com.baoyz.swipemenulistview.SwipeMenuCreator;
//import com.baoyz.swipemenulistview.SwipeMenuItem;
//
//import autovalue.shaded.com.google$.common.base.$Converter;
//
//public class SwipeMenuCustomCreator implements SwipeMenuCreator {
//    private Context context;
//
//    public SwipeMenuCustomCreator(Context context) {
//        this.context = context;
//    }
//
//    @Override
//    public void create(com.baoyz.swipemenulistview.SwipeMenu menu) {
//        // create "open" item
//        SwipeMenuItem openItem = new SwipeMenuItem(context);
//        // set item background
//        openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
//                0xCE)));
//        // set item width
//        openItem.setWidth(90);
//        // set item title
//        openItem.setTitle("Open");
//        // set item title fontsize
//        openItem.setTitleSize(18);
//        // set item title font color
//        openItem.setTitleColor(Color.WHITE);
//        // add to menu
//        menu.addMenuItem(openItem);
//
//        // create "delete" item
//        SwipeMenuItem deleteItem = new SwipeMenuItem(context);
//        // set item background
//        deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
//                0x3F, 0x25)));
//        // set item width
//        deleteItem.setWidth(90);
//        // set a icon
////        deleteItem.setIcon(R.drawable.ic_delete);
//        // add to menu
//        menu.addMenuItem(deleteItem);
//    }
//}