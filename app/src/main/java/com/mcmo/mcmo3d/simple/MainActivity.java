package com.mcmo.mcmo3d.simple;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.mcmo.mcmo3d.gl.math.matrix.Matrix;
import com.mcmo.mcmo3d.simple.demo.Image2DDemoActivity;
import com.mcmo.mcmo3d.simple.demo.LightActivity;
import com.mcmo.mcmo3d.simple.demo.VideoActivity;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    private ListView lv;
    private ArrayList<ExampleItem> datas=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        lv= (ListView) findViewById(R.id.lv);
        MyAdapter adapter = new MyAdapter();
        lv.setAdapter(adapter);
    }
    private void initData(){
        datas.add(new ExampleItem("video",VideoActivity.class));
        datas.add(new ExampleItem("2DDemo",Image2DDemoActivity.class));
        datas.add(new ExampleItem("Ambient",LightActivity.class));
        float[] lhs=new float[16];
        float[] rhs=new float[16];
        Matrix.setIdentity(lhs,0);
        Matrix.setIdentity(rhs,0);
        android.opengl.Matrix.setRotateM(lhs,0,45,1,0,0);
//        android.opengl.Matrix.setRotateM(rhs,0,45,1,0,0);
        android.opengl.Matrix.translateM(rhs,0,100,2,14);
        Log.e("gll", "initData lhs "+ Matrix.toString(lhs,0));
        Log.e("gll", "initData rhs "+ Matrix.toString(rhs,0));
        float[] result=new float[16];
        Matrix.rotateM(result,0,rhs,0,46,1.2f,9f,0.7f);
        Log.e("GLLLL", "initData me "+ Arrays.toString(result));

        android.opengl.Matrix.rotateM(result,0,rhs,0,46,1.2f,9f,0.7f);
        Log.e("GLLLL", "initData sy " + Arrays.toString(result));

    }
    private class MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return datas.size();
        }

        @Override
        public Object getItem(int position) {
            return datas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            Button btn=null;
            if(convertView == null){
                convertView=new Button(MainActivity.this);
                AbsListView.LayoutParams lp=new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                convertView.setLayoutParams(lp);
            }
            btn= (Button) convertView;
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try{
                        Intent intent = new Intent(MainActivity.this, Class.forName(datas.get(position).name));
                        startActivity(intent);
                    }catch (Exception e){
                        Toast.makeText(MainActivity.this, "error", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            btn.setText(datas.get(position).text);
            return convertView;
        }
    }
    private class ExampleItem{
        private String text;
        private String name;

        public ExampleItem(String text, Class clazz) {
            this.text = text;
            this.name = clazz.getName();
        }
    }
}
