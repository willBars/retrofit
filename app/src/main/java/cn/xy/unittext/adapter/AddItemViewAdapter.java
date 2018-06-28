package cn.xy.unittext.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import cn.xy.unittext.R;

/**
 * Created by pxw on 2018/6/1.
 */
public class AddItemViewAdapter extends RecyclerView.Adapter<AddItemViewAdapter.MyHodler>{

    private int addItemView;
    public AddItemViewAdapter(int addItemView) {
        this.addItemView = addItemView;
        notifyDataSetChanged();
    }

    @Override
    public MyHodler onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_float_layout, parent, false);
        MyHodler myHodler = new MyHodler(inflate);
        return myHodler;
    }

    @Override
    public void onBindViewHolder(MyHodler holder, int position) {

    }

    @Override
    public int getItemCount() {
        return addItemView>0?addItemView:0;

    }

    public class MyHodler extends RecyclerView.ViewHolder {
        EditText editKey;
        EditText editValue;
        public MyHodler(View itemView) {
            super(itemView);
             editKey = itemView.findViewById(R.id.input_key);
             editValue = itemView.findViewById(R.id.input_value);
        }
    }
}
