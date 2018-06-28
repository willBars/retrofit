package cn.xy.unittext.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.xy.unittext.R;
import cn.xy.unittext.bean.URLBean;


/**
 * Created by pxw on 2018/5/29.
 */
public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.AddreddHodler> {


    private List<URLBean> urlBeans;
    public AddressAdapter(List<URLBean> urlBeans) {
        this.urlBeans = urlBeans;
        notifyDataSetChanged();
    }

    @Override
    public AddreddHodler onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.sub__recyc_view, parent, false);
        AddreddHodler addreddHodler = new AddreddHodler(inflate);
        return addreddHodler;
    }

    @Override
    public void onBindViewHolder(AddreddHodler holder, int position) {
        final URLBean urlBean = urlBeans.get(position);
        final String url = urlBean.url;
        if(!TextUtils.isEmpty(url)) {
            holder.mSavaAddressUrl.setText(url);
        }
        holder.mDeleteAddressUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                urlBeans.remove(urlBean);
                mItemInnerClickListener.ClickImageButtonDel(urlBean);
            }
        });
        holder.mSavaAddressUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemInnerClickListener.ClickTextGetURl(url);
            }
        });
    }

    @Override
    public int getItemCount() {
        return urlBeans != null?urlBeans.size():0;
    }

    static class AddreddHodler extends RecyclerView.ViewHolder {

        TextView mSavaAddressUrl;
        ImageView mDeleteAddressUrl;
        public AddreddHodler(View itemView) {
            super(itemView);
            mSavaAddressUrl = itemView.findViewById(R.id.sava_address_url);
            mDeleteAddressUrl = itemView.findViewById(R.id.delete_address_url);
        }
    }
    private recycViewItemClick mItemInnerClickListener;

    public void setOnItemDeleteClickListener(recycViewItemClick mItemInnerClickListener) {
        this.mItemInnerClickListener = mItemInnerClickListener;
    }
    public interface recycViewItemClick{
        public void ClickImageButtonDel(URLBean urlBean);

        public void ClickTextGetURl(String URL);
    }
}
