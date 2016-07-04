简单介绍下Android Studio下使用xUtils存储数据到本地

**1.在*Module*中的*build.gradle*里添加以下依赖**


```
compile 'org.xutils:xutils:3.3.36'
```
**2.添加以下权限**

```
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
```

**3.xUtils在使用前要初始化一下，推荐在自定义Application的onCreate中执行。**

```
@Override
	public void onCreate() {
		super.onCreate();
		Log.e("", "application");
		mInstance = this;
		x.Ext.init(this);
		x.Ext.setDebug(true); // 是否输出debug日志

	}
```

**4.数据库的一些配置信息**

声明变量：

```
private DbManager.DaoConfig daoConfig;
private DbManager db;
```

设置数据库的名字，存放目录，版本号等信息：
```
public DbManager.DaoConfig getDaoConfig() {
		DbManager.DaoConfig daoConfig = new DbManager.DaoConfig()
				.setDbName("my.db")
				// 不设置dbDir时, 默认存储在app的私有目录.
				.setDbDir(new File("/test")) 
				.setDbVersion(1)
				.setDbOpenListener(new DbManager.DbOpenListener() {
					@Override
					public void onDbOpened(DbManager db) {
						// 开启WAL, 对写入加速提升巨大
						db.getDatabase().enableWriteAheadLogging();
					}
				});
		return daoConfig;
	}

public void setDaoConfig(DbManager.DaoConfig daoConfig) {
	this.daoConfig = daoConfig;
}

public DbManager getDb() {
	return db;
}

public void setDb(DbManager db) {
	this.db = db;
}
```

**5.程序主界面布局：activity_main.xml**
```
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="#E5E5E5"
    android:orientation="vertical"
    tools:context=".activity.MainActivity">

    <Button
        android:id="@+id/btn_network"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginBottom="@dimen/activity_vertical_margin"
        android:text="发起网络请求"/>

    <Button
        android:id="@+id/btn_database"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginBottom="@dimen/activity_vertical_margin"
        android:text="从数据库中取数据"/>

    <Button
        android:id="@+id/btn_delete"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="删除数据库中数据"/>

</LinearLayout>

```

![这里写图片描述](http://img.blog.csdn.net/20160704021715977)

**数据列表布局：others.xml**

```
<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="#E5E5E5"
    tools:context=".activity.MainActivity">

    <android.support.v7.widget.RecyclerView
        android:id="@+id/news_list"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />
</RelativeLayout>

```

**列表每一项：item_card_layout.xml**

```
<?xml version="1.0" encoding="utf-8"?>
<android.support.v7.widget.CardView xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:card_view="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:layout_marginLeft="8dp"
    android:layout_marginRight="8dp"
    android:layout_marginTop="8dp"
    android:foreground="?attr/selectableItemBackground"
    android:orientation="horizontal"
    card_view:cardElevation="1dp">

    <RelativeLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent">

        <ImageView
            android:id="@+id/iv_cover"
            android:layout_width="160dp"
            android:layout_height="150dp"
            android:layout_centerVertical="true"
            android:scaleType="fitXY" />

        <TextView
            android:id="@+id/tv_title"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_toRightOf="@id/iv_cover"
            android:gravity="left"
            android:padding="8dp"
            android:text="奇虎360宣布通过私有化决议"
            android:textSize="16sp" />

        <TextView
            android:id="@+id/tv_description"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_toRightOf="@id/iv_cover"
            android:layout_below="@id/tv_title"
            android:gravity="left"
            android:padding="8dp"
            android:textColor="#bbb"
            android:text="互联网头条"
            android:textSize="14sp" />

        <TextView
            android:id="@+id/tv_ctime"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_toRightOf="@id/iv_cover"
            android:layout_below="@id/tv_description"
            android:gravity="left"
            android:padding="8dp"
            android:textColor="#bbb"
            android:text="2016-03-31"
            android:textSize="14sp" />

    </RelativeLayout>
</android.support.v7.widget.CardView>
```
![这里写图片描述](http://img.blog.csdn.net/20160704022120673)

**6.数据实体表 News**

```
@Table(name = "news")
public class News {

    @Column(name = "newsid", isId = true, autoGen = true)
    private int newsid;

    @Column(name = "ctime")
    private String ctime;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "picUrl")
    private String picUrl;

    @Column(name = "url")
    private String url;

	getter...
	setter...
}
```

**7.MainActivity.java文件**

```
public class MainActivity extends AppCompatActivity {

    @Bind(R.id.btn_network)
    Button btnNetwork;
    @Bind(R.id.btn_database)
    Button btnDatabase;
    @Bind(R.id.btn_delete)
    Button btnDelete;
    private DbManager db;
    private List<News> newsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }


    @OnClick({R.id.btn_network, R.id.btn_database,R.id.btn_delete})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_network:
                startActivity(new Intent(this, NetWorkActivity.class));
                break;
            case R.id.btn_database:
                startActivity(new Intent(this, DbActivity.class));
                break;
            case R.id.btn_delete:
                delete();
                break;
        }
    }

    public void delete(){
        db = x.getDb(CustomApplication.getInstance().getDaoConfig());

		//从数据库中取出所有数据
        try {
            newsList = db.selector(News.class).findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }
        //删除数据
        for (News news : newsList){
            try {
                db.delete(news);
            } catch (DbException e) {
                e.printStackTrace();
            }
        }
        Toast.makeText(MainActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
    }

}
```

**8.适配器文件: NewsAdapter.java**

```
public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> implements View.OnClickListener {

    private static List<News> newsList;
    private LayoutInflater inflater;
    private Context mContext;
    private OnRecyclerViewItemClickListener mOnItemClickListener = null;
    static int pos = 0;
    private String imagePath;

    public NewsAdapter(Context context, List<News> newsList) {
        this.mContext = context;
        this.newsList = newsList;
        inflater = LayoutInflater.from(context);
    }

    //define interface
    public static interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, Object data);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v;
        v = inflater.inflate(R.layout.item_card_layout, viewGroup, false);
        ViewHolder vh = new ViewHolder(v);
        //将创建的View注册点击事件
        v.setOnClickListener(this);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        // 给ViewHolder设置元素
        News news = newsList.get(i);
        viewHolder.tv_ctime.setText(news.getCtime());
        viewHolder.tv_title.setText(news.getTitle());
        viewHolder.tv_description.setText(news.getDescription());

        //将数据保存在itemView的Tag中，以便点击时进行获取
        viewHolder.itemView.setTag(i);
        Picasso.with(mContext).load(news.getPicUrl()).into(viewHolder.iv_cover);

    }

    @Override
    public int getItemCount() {
        // 返回数据总数
        return newsList == null ? 0 : newsList.size();
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取数据
            mOnItemClickListener.onItemClick(v, v.getTag());
        }
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    // 重写的自定义ViewHolder
    public static class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.iv_cover)
        ImageView iv_cover;
        @Bind(R.id.tv_title)
        TextView tv_title;
        @Bind(R.id.tv_description)
        TextView tv_description;
        @Bind(R.id.tv_ctime)
        TextView tv_ctime;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
```

**9.运行截图：**
![image](https://raw.githubusercontent.com/dongzhaoqi/xUtilsTest/master/687474703a2f2f696d672e626c6f672e6373646e2e6e65742f3230313630373034303233313331303838.gif)
