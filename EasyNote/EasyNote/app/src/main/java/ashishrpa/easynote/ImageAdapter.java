package ashishrpa.easynote;

/**
 * Created by WINDOWS 8.1 on 12/4/2016.
 */
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    LayoutInflater inflater;

    // Keep all Images in array
    public Integer[] mThumbIds = {
            R.drawable.note,
            R.drawable.memo,
            R.drawable.shopping,
            R.drawable.todo
    };

    // Keep all Images in array
    public String[] titles = {
            "NOTES",
            "MEMO",
            "SHOPPING",
            "TO DO"
    };

    // Constructor
    public ImageAdapter(Context c){
        mContext = c;
    }

    @Override
    public int getCount() {
        return mThumbIds.length;
    }

    @Override
    public Object getItem(int position) {
        return mThumbIds[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        //inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        inflater = LayoutInflater.from(mContext);
        if(convertView == null){
            convertView = inflater.inflate(R.layout.grid_logos,parent,false);
            holder = new ViewHolder();
            holder.bindView(convertView);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder)convertView.getTag();
        }

        holder.imageLogo.setImageResource(mThumbIds[position]);
        holder.textView_title.setText(titles[position]);
        return convertView;
//        TextView textView = new TextView(mContext);
//        ImageView imageView = new ImageView(mContext);
//        imageView.setImageResource(mThumbIds[position]);
//        textView.setTitle(titles[position]);
//        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//        imageView.setLayoutParams(new GridView.LayoutParams(150, 150));
//        return imageView;
    }

    public class ViewHolder{
        ImageView imageLogo;
        TextView textView_title;
        void bindView(View convertView){
            imageLogo = (ImageView) convertView.findViewById(R.id.image_number);
            textView_title = (TextView) convertView.findViewById(R.id.textview_title);
        }
    }

}