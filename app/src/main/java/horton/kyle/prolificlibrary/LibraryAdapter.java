package horton.kyle.prolificlibrary;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Kyleh on 5/21/2018.
 */

public class LibraryAdapter extends ArrayAdapter<BookDetails> {

    private Context context;
    private List<BookDetails> values;

    public LibraryAdapter(Context context, List<BookDetails> values) {
        super(context, R.layout.adapter_layout, values);

        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            LayoutInflater inflater =
                    (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.adapter_layout, parent, false);
        }

            TextView textView = (TextView) view.findViewById(R.id.list_title);
            TextView textView2 = (TextView) view.findViewById(R.id.list_author);

            BookDetails item = values.get(position);
            String title = item.getBookTitle();
            String author = item.getAuthor();
            textView.setText(title);
            textView2.setText(author);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return view;
    }
}
