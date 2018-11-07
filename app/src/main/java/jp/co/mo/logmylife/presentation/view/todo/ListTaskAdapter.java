package jp.co.mo.logmylife.presentation.view.todo;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.amulyakhare.textdrawable.util.ColorGenerator;

import java.util.ArrayList;
import java.util.HashMap;

import jp.co.mo.logmylife.R;
import jp.co.mo.logmylife.domain.repository.TaskDataRepository;

public class ListTaskAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<HashMap<String, String>> mData;

    public ListTaskAdapter(Context context, ArrayList<HashMap<String, String>> data) {
        this.mContext = context;
        this.mData = data;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ListTaskViewHolder holder = null;
        if(convertView == null) {
            holder = new ListTaskViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.task_list_row, parent, false);
            holder.taskImage = convertView.findViewById(R.id.task_image);
            holder.taskName = convertView.findViewById(R.id.task_name);
            holder.taskDate = convertView.findViewById(R.id.task_date);

            convertView.setTag(holder);
        } else {
            holder = (ListTaskViewHolder) convertView.getTag();
        }
        holder.taskImage.setId(position);
        holder.taskName.setId(position);
        holder.taskDate.setId(position);

        HashMap<String, String> song = mData.get(position);

        try{
            holder.taskName.setText(song.get(TaskDataRepository.KEY_TASK));
            holder.taskDate.setText(song.get(TaskDataRepository.KEY_DATE));

            ColorGenerator generator = ColorGenerator.MATERIAL;
            int color = generator.getColor(getItem(position));
            holder.taskImage.setTextColor(color);
            holder.taskImage.setText(Html.fromHtml("&#11044;"));

        } catch (Exception e) {
            e.printStackTrace();
        }

        return convertView;
    }
}

class ListTaskViewHolder {
    TextView taskImage;
    TextView taskName, taskDate;
}
