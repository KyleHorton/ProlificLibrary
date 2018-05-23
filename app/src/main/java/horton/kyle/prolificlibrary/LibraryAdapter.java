package horton.kyle.prolificlibrary;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

/**
 * Prolific Library Application
 * Author: Kyle Horton
 * 5/22/2018
 *
 * A custom adapter created to display the list of books in the listview on the BrowseBooksActivity.
 */

public class LibraryAdapter extends ArrayAdapter<BookDetails> {

    private Context context;
    private List<BookDetails> bookDetails;

    private int bookId;
    private String title = "";
    private String author = "";

    public LibraryAdapter(Context context, List<BookDetails> bookDetails) {
        super(context, R.layout.adapter_layout, bookDetails);

        this.context = context;
        this.bookDetails = bookDetails;
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

        final BookDetails details = bookDetails.get(position);
        title = details.getBookTitle();
        author = details.getAuthor();

        textView.setText(title);
        textView2.setText(author);

        // grabs the position of the item clicked and the associated id
        // sends id with an intent and starts the display activity
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListView parent = (ListView) v.getParent();
                int pos = parent.getPositionForView(v);
                bookId = bookDetails.get(pos).getBookId();
                Log.d("ID", "" + Integer.toString(bookId));
                Context context = getContext();
                Intent intent = new Intent(getContext(), DisplayDetailActivity.class);
                intent.putExtra("id", bookId);
                context.startActivity(intent);

            }
        });


        return view;
    }
}
