package app.huangtong.baselibrary.base_adapter.base_recyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import java.util.List;
import app.huangtong.baselibrary.base_adapter.base_recyclerView.base.ItemViewDelegate;
import app.huangtong.baselibrary.base_adapter.base_recyclerView.base.ViewHolder;

/**
 * Created by zhy on 16/4/9.
 * 鴻洋的BaseRecyclerView
 * http://blog.csdn.net/lmj623565791/article/details/51118836/
 * https://github.com/hongyangAndroid/baseAdapter
 */
public abstract class CommonAdapter<T> extends MultiItemTypeAdapter<T>
{
    protected Context mContext;
    protected int mLayoutId;
    protected List<T> mDatas;
    protected LayoutInflater mInflater;

    public CommonAdapter(final Context context, final int layoutId, List<T> datas)
    {
        super(context, datas);
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mLayoutId = layoutId;
        mDatas = datas;

        addItemViewDelegate(new ItemViewDelegate<T>()
        {
            @Override
            public int getItemViewLayoutId()
            {
                return layoutId;
            }

            @Override
            public boolean isForViewType( T item, int position)
            {
                return true;
            }

            @Override
            public void convert(ViewHolder holder, T t, int position)
            {
                CommonAdapter.this.convert(holder, t, position);
            }
        });
    }
    //我自己加的
    public void setData(List<T> mDatas) {
        this.mDatas = mDatas;
        notifyDataSetChanged();
    }

    protected abstract void convert(ViewHolder holder, T t, int position);


}
